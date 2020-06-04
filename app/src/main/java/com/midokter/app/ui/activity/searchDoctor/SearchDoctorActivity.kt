package com.midokter.app.ui.activity.searchDoctor

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.midokter.app.R
import com.midokter.app.base.BaseActivity
import com.midokter.app.databinding.ActivitySearchDoctorBinding
import com.midokter.app.repositary.WebApiConstants
import com.midokter.app.repositary.model.MainResponse
import com.midokter.app.ui.adapter.SearchDoctorsListAdapter
import com.midokter.app.utils.ViewUtils
import kotlinx.android.synthetic.main.content_search_doctor.*
import java.util.HashMap

class SearchDoctorActivity : BaseActivity<ActivitySearchDoctorBinding>(),SearchNavigator {

    val searchDoctors: ArrayList<String> = ArrayList()
    private lateinit var viewModel: SearchViewModel
    private lateinit var mDataBinding: ActivitySearchDoctorBinding
    private var mAdapter: SearchDoctorsListAdapter? = null


    override fun getLayoutId(): Int = R.layout.activity_search_doctor

    override fun initView(mViewDataBinding: ViewDataBinding?) {
      //  addSearchDoctors()
        mDataBinding = mViewDataBinding as ActivitySearchDoctorBinding
        viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)
        mDataBinding.viewmodel = viewModel
        //viewModel.navigator = this

        initApiCal()
        initAdapter()
        observeResponse()

    }

    private fun initApiCal() {
        loadingObservable.value = true
        val hashMap: HashMap<String, Any> = HashMap()
        viewModel.gethome(hashMap)

    }
    private fun observeResponse() {

        viewModel.mDoctorResponse.observe(this, Observer<MainResponse> {


            viewModel.mDoctorslist = it.search_doctors as MutableList<MainResponse.SearchDoctor>?
            if (viewModel.mDoctorslist!!.size > 0) {
                viewModel.listsize.set(it.search_doctors.size.toString())
                mDataBinding.tvNotFound.visibility = View.GONE
            } else {
                mDataBinding.tvNotFound.visibility = View.VISIBLE
            }
            mAdapter = SearchDoctorsListAdapter(viewModel.mDoctorslist!!,this@SearchDoctorActivity)
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
        mAdapter = SearchDoctorsListAdapter( viewModel.mDoctorslist!!,this@SearchDoctorActivity)
        mDataBinding.adapter = mAdapter
        mDataBinding.rvSerachDoctors.addItemDecoration(
            DividerItemDecoration(
                applicationContext,
                DividerItemDecoration.VERTICAL
            )
        )
        mDataBinding.rvSerachDoctors.layoutManager =LinearLayoutManager(applicationContext)
        mAdapter!!.notifyDataSetChanged()

        mDataBinding.editText13.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.length>0) {
                    loadingObservable.value = true
                    val hashMap: HashMap<String, Any> = HashMap()
                    hashMap[WebApiConstants.Home.SEARCH] = s.toString()
                    viewModel.gethome(hashMap)
                }else{
                    initApiCal()
                }

            }

        })
    }

    private fun addSearchDoctors() {
        searchDoctors.add("Dr.Bretto")
        searchDoctors.add("Dr.John")
        searchDoctors.add("Dr.Jenifer")
        searchDoctors.add("Dr.Virundha")
        rv_serach_doctors.layoutManager = LinearLayoutManager(applicationContext)
        rv_serach_doctors.addItemDecoration(
            DividerItemDecoration(applicationContext,
                DividerItemDecoration.VERTICAL))
       // rv_serach_doctors.adapter = applicationContext?.let { SearchDoctorsListAdapter(searchDoctors, it) }
    }
}
