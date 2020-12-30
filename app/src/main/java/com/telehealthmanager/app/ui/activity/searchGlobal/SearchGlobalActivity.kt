package com.telehealthmanager.app.ui.activity.searchGlobal

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.telehealthmanager.app.R
import com.telehealthmanager.app.base.BaseActivity
import com.telehealthmanager.app.databinding.ActivityGlobalSearchBinding
import com.telehealthmanager.app.repositary.WebApiConstants
import com.telehealthmanager.app.repositary.model.Hospital
import com.telehealthmanager.app.repositary.model.SearchResponse
import com.telehealthmanager.app.ui.activity.searchDoctor.SearchDoctorDetailActivity
import com.telehealthmanager.app.ui.adapter.IDoctorsSelectListener
import com.telehealthmanager.app.ui.adapter.SearchClinicListAdapter
import com.telehealthmanager.app.ui.adapter.SearchDoctorsListAdapter
import com.telehealthmanager.app.ui.adapter.SearchcatagoryListAdapter
import com.telehealthmanager.app.utils.CustomBackClick
import com.telehealthmanager.app.utils.ViewUtils
import java.io.Serializable
import java.util.*

class SearchGlobalActivity : BaseActivity<ActivityGlobalSearchBinding>(), SearchGlobalNavigator, CustomBackClick, IDoctorsSelectListener {
    private lateinit var viewModel: SearchGlobalViewModel
    private lateinit var mDataBinding: ActivityGlobalSearchBinding
    private var mAdapter: SearchDoctorsListAdapter = SearchDoctorsListAdapter(this@SearchGlobalActivity)
    private var mCategoryAdapter: SearchcatagoryListAdapter? = null
    private var mClinicAdapter: SearchClinicListAdapter? = null
    private lateinit var decorator: DividerItemDecoration

    override fun getLayoutId(): Int = R.layout.activity_global_search

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mDataBinding = mViewDataBinding as ActivityGlobalSearchBinding
        viewModel = ViewModelProvider(this).get(SearchGlobalViewModel::class.java)
        mDataBinding.viewmodel = viewModel
        viewModel.navigator = this
        initApiCal()
        initAdapter()
        observeResponse()
        viewModel.setOnClickListener(this@SearchGlobalActivity)
        viewModel.toolBarTile.value = getString(R.string.search_doctor)
    }

    override fun clickBackPress() {
        finish()
    }

    private fun initApiCal() {
        viewModel.search.set("")
        loadingObservable.value = true
        val hashMap: HashMap<String, Any> = HashMap()
        viewModel.getGlobalSearch(hashMap)
    }

    private fun observeResponse() {
        viewModel.loadingProgress.observe(this, Observer {
            if (it) {
                mDataBinding.searchProgress.visibility = View.VISIBLE
                mDataBinding.searchImage.visibility = View.GONE
            } else {
                mDataBinding.searchProgress.visibility = View.GONE
                mDataBinding.searchImage.visibility = View.VISIBLE
            }
        })

        viewModel.mResponse.observe(this, Observer<SearchResponse> {
            if (!it.doctors.isNullOrEmpty()) {
                mAdapter.addItems(it.doctors.toMutableList())
                mDataBinding.isdoctor = true
            } else {
                mDataBinding.isdoctor = false
            }

            //catagory
            viewModel.mCategoryList = it.specialities as MutableList<SearchResponse.Specialities>?
            mDataBinding.iscatagory = viewModel.mCategoryList!!.size > 0
            mCategoryAdapter = SearchcatagoryListAdapter(viewModel.mCategoryList!!, this@SearchGlobalActivity)
            mDataBinding.specialitiesadapter = mCategoryAdapter
            mCategoryAdapter!!.notifyDataSetChanged()

            //clinic
            viewModel.mClinicList = it.clinic as MutableList<SearchResponse.Clinic>?
            mDataBinding.isclinic = viewModel.mClinicList!!.size > 0
            mClinicAdapter = SearchClinicListAdapter(viewModel.mClinicList!!, this@SearchGlobalActivity)
            mDataBinding.clinicadapter = mClinicAdapter
            mClinicAdapter!!.notifyDataSetChanged()
            loadingObservable.value = false
            viewModel.loadingProgress.value = false
        })

        viewModel.getErrorObservable().observe(this, { message ->
            loadingObservable.value = false
            viewModel.loadingProgress.value = false
            ViewUtils.showToast(this@SearchGlobalActivity, message, false)
        })
    }

    private fun initAdapter() {
        decorator = DividerItemDecoration(applicationContext, LinearLayoutManager.VERTICAL)
        decorator.setDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.divider)!!)

        mDataBinding.rvDoctorsList.addItemDecoration(decorator)
        mDataBinding.rvDoctorsList.layoutManager = LinearLayoutManager(applicationContext)
        mDataBinding.rvDoctorsList.adapter = mAdapter

        //catagory
        mCategoryAdapter = SearchcatagoryListAdapter(viewModel.mCategoryList!!, this@SearchGlobalActivity)
        mDataBinding.specialitiesadapter = mCategoryAdapter

        mDataBinding.rvSpecialities.addItemDecoration(decorator)
        mDataBinding.rvSpecialities.layoutManager = LinearLayoutManager(applicationContext)
        mCategoryAdapter!!.notifyDataSetChanged()

        //clinic
        mClinicAdapter = SearchClinicListAdapter(viewModel.mClinicList!!, this@SearchGlobalActivity)
        mDataBinding.clinicadapter = mClinicAdapter
        mDataBinding.rvClinic.addItemDecoration(decorator)
        mDataBinding.rvClinic.layoutManager = LinearLayoutManager(applicationContext)
        mClinicAdapter!!.notifyDataSetChanged()

        mDataBinding.editText9.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.isNotEmpty()) {
                    val hashMap: HashMap<String, Any> = HashMap()
                    hashMap[WebApiConstants.Home.SEARCH] = s.toString()
                    viewModel.getGlobalSearch(hashMap)
                    viewModel.loadingProgress.value = true
                    viewModel.search.set(s.toString())
                } else {
                    initApiCal()
                }
            }
        })
    }

    override fun onItemSelect(item: Hospital) {
        val intent = Intent(applicationContext, SearchDoctorDetailActivity::class.java)
        intent.putExtra(WebApiConstants.IntentPass.SearchDoctorProfile, item as Serializable)
        startActivity(intent);
    }
}
