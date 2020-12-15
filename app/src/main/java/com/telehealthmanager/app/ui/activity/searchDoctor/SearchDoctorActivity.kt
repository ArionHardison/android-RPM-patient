package com.telehealthmanager.app.ui.activity.searchDoctor

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.telehealthmanager.app.R
import com.telehealthmanager.app.base.BaseActivity
import com.telehealthmanager.app.databinding.ActivitySearchDoctorBinding
import com.telehealthmanager.app.repositary.WebApiConstants
import com.telehealthmanager.app.repositary.model.Hospital
import com.telehealthmanager.app.repositary.model.MainResponse
import com.telehealthmanager.app.ui.adapter.SearchDoctorsListAdapter
import com.telehealthmanager.app.utils.CustomBackClick
import com.telehealthmanager.app.utils.ViewUtils
import kotlinx.android.synthetic.main.content_search_doctor.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.MutableList
import kotlin.collections.set
import kotlin.math.abs


class SearchDoctorActivity : BaseActivity<ActivitySearchDoctorBinding>(), SearchNavigator, CustomBackClick {
    override fun viewAllClick() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun viewVideoClick() {
        TODO("Not yet implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun viewInfoClick() {
        TODO("Not yet implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun viewShareClick() {
        TODO("Not yet implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onFavClick() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onShareClick() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBookClick() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private lateinit var viewModel: SearchViewModel
    private lateinit var mDataBinding: ActivitySearchDoctorBinding
    private var mAdapter: SearchDoctorsListAdapter? = null

    override fun getLayoutId(): Int = R.layout.activity_search_doctor

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mDataBinding = mViewDataBinding as ActivitySearchDoctorBinding
        viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)
        mDataBinding.viewmodel = viewModel
        //viewModel.navigator = this
        initApiCal()
        initAdapter()
        observeResponse()
        viewModel.setOnClickListener(this@SearchDoctorActivity)
        viewModel.toolBarTile.value = resources.getString(R.string.search_doctor)
    }

    override fun clickBackPress() {
        finish()
    }

    private fun initApiCal() {
        viewModel.search.set("")
        loadingObservable.value = true
        val hashMap: HashMap<String, Any> = HashMap()
        viewModel.gethome(hashMap)
        mDataBinding.toolBar.appBar.addOnOffsetChangedListener(OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (abs(verticalOffset) == appBarLayout.totalScrollRange) {
                mDataBinding.toolBar.scrollToolbarBar.visibility = View.GONE
                mDataBinding.toolBar.toolbarVisible.visibility = View.VISIBLE
            } else if (verticalOffset == 0) {
                mDataBinding.toolBar.scrollToolbarBar.visibility = View.VISIBLE
                mDataBinding.toolBar.toolbarVisible.visibility = View.GONE
            }
        })
    }

    private fun observeResponse() {
        viewModel.mDoctorResponse.observe(this, Observer<MainResponse> {
            viewModel.mDoctorList = it.search_doctors as MutableList<Hospital>?
            if (viewModel.mDoctorList!!.size > 0) {
                viewModel.listsize.set(it.search_doctors.size.toString())
                mDataBinding.tvNotFound.visibility = View.GONE
            } else {
                mDataBinding.tvNotFound.visibility = View.VISIBLE
            }
            mAdapter = SearchDoctorsListAdapter(viewModel.mDoctorList!!, this@SearchDoctorActivity)
            mDataBinding.adapter = mAdapter
            mAdapter!!.notifyDataSetChanged()
            loadingObservable.value = false
        })

        viewModel.getErrorObservable().observe(this, Observer<String> { message ->
            loadingObservable.value = false
            ViewUtils.showToast(this@SearchDoctorActivity, message, false)
        })
    }

    private fun initAdapter() {
        val decorator = DividerItemDecoration(applicationContext, LinearLayoutManager.VERTICAL)
        decorator.setDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.divider)!!)
        mAdapter = SearchDoctorsListAdapter(viewModel.mDoctorList!!, this@SearchDoctorActivity)
        mDataBinding.adapter = mAdapter
        mDataBinding.rvSerachDoctors.addItemDecoration(decorator)

        mDataBinding.rvSerachDoctors.layoutManager = LinearLayoutManager(applicationContext)
        mAdapter!!.notifyDataSetChanged()

        mDataBinding.editText13.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.length > 0) {
                    loadingObservable.value = true
                    val hashMap: HashMap<String, Any> = HashMap()
                    hashMap[WebApiConstants.Home.SEARCH] = s.toString()
                    viewModel.gethome(hashMap)
                    viewModel.search.set(s.toString())
                } else {
                    initApiCal()
                }
            }
        })
    }
}
