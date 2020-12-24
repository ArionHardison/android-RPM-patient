package com.telehealthmanager.app.ui.activity.searchDoctor

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.telehealthmanager.app.R
import com.telehealthmanager.app.base.BaseActivity
import com.telehealthmanager.app.databinding.ActivitySearchDoctorBinding
import com.telehealthmanager.app.repositary.WebApiConstants
import com.telehealthmanager.app.ui.adapter.SearchDoctorsListAdapter
import com.telehealthmanager.app.utils.CustomBackClick
import com.telehealthmanager.app.utils.ViewUtils
import kotlinx.android.synthetic.main.content_search_doctor.*
import kotlinx.android.synthetic.main.toolbar_base.*
import kotlinx.android.synthetic.main.twillioi_video_activity.*
import java.util.*
import kotlin.math.abs


class SearchDoctorActivity : BaseActivity<ActivitySearchDoctorBinding>(), CustomBackClick {

    private val TAG = "SearchDoctorActivity"
    private lateinit var viewModel: SearchViewModel
    private lateinit var mDataBinding: ActivitySearchDoctorBinding
    private lateinit var mAdapter: SearchDoctorsListAdapter

    override fun getLayoutId(): Int = R.layout.activity_search_doctor

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mDataBinding = mViewDataBinding as ActivitySearchDoctorBinding
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        mDataBinding.viewmodel = viewModel
        mAdapter = SearchDoctorsListAdapter(this)
        mDataBinding.adapter = mAdapter
        setupScrollListener()

        mDataBinding.editText13.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.isNotEmpty()) {
                    mAdapter.onSearchRequest()
                    val hashMap: HashMap<String, Any> = HashMap()
                    hashMap[WebApiConstants.Home.SEARCH] = s.toString()
                    viewModel.getSearchDoctors(hashMap)
                } else {
                    mAdapter.onSearchCleared()
                }
            }
        })

        initApiCal()
        observeResponse()
        viewModel.setOnClickListener(this@SearchDoctorActivity)
        viewModel.toolBarTile.value = resources.getString(R.string.search_doctor)
    }

    private fun setupScrollListener() {
        val layoutManager = mDataBinding.rvSerachDoctors.layoutManager as LinearLayoutManager
        mDataBinding.rvSerachDoctors.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = layoutManager.itemCount
                val visibleItemCount = layoutManager.childCount
                val lastVisibleItem = layoutManager.findLastCompletelyVisibleItemPosition()
                viewModel.listScrolled(visibleItemCount, lastVisibleItem, totalItemCount, mAdapter.getItem(mAdapter.itemCount))
            }
        })
    }

    override fun clickBackPress() {
        finish()
    }

    private fun initApiCal() {
        viewModel.search.set("")
        val hashMap: HashMap<String, Any> = HashMap()
        hashMap["page"] = "0"
        viewModel.getSearchDoctors(hashMap)
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

        viewModel.mDoctorResponse.observe(this, Observer {
            Log.d(TAG, "observeResponse: ${it.search_doctors.size}")
            if (it.search_doctors.toMutableList().size > 0) {
                mDataBinding.tvNotFound.visibility = View.GONE
                mAdapter.addItems(it.search_doctors.toMutableList())
            } else {
                if (mAdapter.itemCount > 0) {
                    mDataBinding.tvNotFound.visibility = View.GONE
                } else {
                    mDataBinding.tvNotFound.visibility = View.VISIBLE
                }
            }
            viewModel.listsize.value = mAdapter.itemCount.toString()
            loadingObservable.value = false
            viewModel.loadingProgress.value = false
        })

        viewModel.getErrorObservable().observe(this, Observer<String> { message ->
            loadingObservable.value = false
            viewModel.loadingProgress.value = false
            ViewUtils.showToast(this@SearchDoctorActivity, message, false)
        })

        viewModel.loadingProgress.observe(this, Observer {
            if (!it) {
                mDataBinding.loadingTxt.visibility = View.GONE
            } else {
                mDataBinding.loadingTxt.visibility = View.VISIBLE
            }
        })
    }
}
