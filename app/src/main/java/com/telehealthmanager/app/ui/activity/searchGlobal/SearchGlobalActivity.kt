package com.telehealthmanager.app.ui.activity.searchGlobal

import android.text.Editable
import android.text.TextWatcher
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.telehealthmanager.app.R
import com.telehealthmanager.app.base.BaseActivity
import com.telehealthmanager.app.databinding.ActivityGlobalSearchBinding
import com.telehealthmanager.app.repositary.WebApiConstants
import com.telehealthmanager.app.repositary.model.Hospital
import com.telehealthmanager.app.repositary.model.SearchResponse
import com.telehealthmanager.app.ui.adapter.SearchClinicListAdapter
import com.telehealthmanager.app.ui.adapter.SearchDoctorsListAdapter
import com.telehealthmanager.app.ui.adapter.SearchcatagoryListAdapter
import com.telehealthmanager.app.utils.ViewUtils
import java.util.HashMap

class SearchGlobalActivity : BaseActivity<ActivityGlobalSearchBinding>(),SearchGlobalNavigator {



    private lateinit var viewModel: SearchGlobalViewModel
    private lateinit var mDataBinding: ActivityGlobalSearchBinding
    private var mAdapter: SearchDoctorsListAdapter? = null
    private var mcatagoryAdapter: SearchcatagoryListAdapter? = null
    private var mclinicAdapter: SearchClinicListAdapter? = null
    private lateinit var decorator: DividerItemDecoration

    override fun getLayoutId(): Int = R.layout.activity_global_search

    override fun initView(mViewDataBinding: ViewDataBinding?) {

        mDataBinding = mViewDataBinding as ActivityGlobalSearchBinding
        viewModel = ViewModelProviders.of(this).get(SearchGlobalViewModel::class.java)
        mDataBinding.viewmodel = viewModel
        viewModel.navigator = this
        initApiCal()
        initAdapter()
        observeResponse()
        mDataBinding.txtcancel.setOnClickListener {
            finish()
        }
    }

    private fun initApiCal() {
        viewModel.search.set("")
        loadingObservable.value = true
        val hashMap: HashMap<String, Any> = HashMap()
        viewModel.getgloblsearch(hashMap)

    }
    private fun observeResponse() {

        viewModel.mResponse.observe(this, Observer<SearchResponse> {


            viewModel.mDoctorslist = it.doctors as MutableList<Hospital>?
            if (viewModel.mDoctorslist!!.size > 0) {
                viewModel.listsize.set(it.doctors.size.toString())
                mDataBinding.isdoctor = true
            } else {
                mDataBinding.isdoctor = false
            }

            mAdapter = SearchDoctorsListAdapter(viewModel.mDoctorslist!!,this@SearchGlobalActivity)
            mDataBinding.doctoradapter = mAdapter
            mAdapter!!.notifyDataSetChanged()
            //catagory
            viewModel.mCatagorylist = it.specialities as MutableList<SearchResponse.Specialities>?
            mDataBinding.iscatagory = viewModel.mCatagorylist!!.size > 0
            mcatagoryAdapter = SearchcatagoryListAdapter(viewModel.mCatagorylist!!,this@SearchGlobalActivity)
            mDataBinding.specialitiesadapter = mcatagoryAdapter
            mcatagoryAdapter!!.notifyDataSetChanged()

            //clinic
            viewModel.mCliniclist = it.clinic as MutableList<SearchResponse.Clinic>?
            mDataBinding.isclinic = viewModel.mCliniclist!!.size > 0
            mclinicAdapter = SearchClinicListAdapter( viewModel.mCliniclist!!,this@SearchGlobalActivity)
            mDataBinding.clinicadapter = mclinicAdapter
            mclinicAdapter!!.notifyDataSetChanged()


            loadingObservable.value = false




        })
        viewModel.getErrorObservable().observe(this, Observer<String> { message ->
            loadingObservable.value = false
            ViewUtils.showToast(this@SearchGlobalActivity, message, false)
        })
    }

    private fun initAdapter() {

         decorator = DividerItemDecoration(applicationContext, LinearLayoutManager.VERTICAL)
        decorator.setDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.divider)!!)

        mAdapter = SearchDoctorsListAdapter( viewModel.mDoctorslist!!,this@SearchGlobalActivity)
        mDataBinding.doctoradapter = mAdapter

        mDataBinding.rvDoctorsList.addItemDecoration(decorator)

        mDataBinding.rvDoctorsList.layoutManager =LinearLayoutManager(applicationContext)
        mAdapter!!.notifyDataSetChanged()
//catagory
        mcatagoryAdapter = SearchcatagoryListAdapter( viewModel.mCatagorylist!!,this@SearchGlobalActivity)
        mDataBinding.specialitiesadapter = mcatagoryAdapter

        mDataBinding.rvSpecialities.addItemDecoration(decorator)
        mDataBinding.rvSpecialities.layoutManager =LinearLayoutManager(applicationContext)
        mcatagoryAdapter!!.notifyDataSetChanged()

        //clinic
        mclinicAdapter = SearchClinicListAdapter( viewModel.mCliniclist!!,this@SearchGlobalActivity)
        mDataBinding.clinicadapter = mclinicAdapter
        mDataBinding.rvClinic.addItemDecoration(decorator)
        mDataBinding.rvClinic.layoutManager =LinearLayoutManager(applicationContext)
        mclinicAdapter!!.notifyDataSetChanged()

        mDataBinding.editText9.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.length>0) {
                    loadingObservable.value = true
                    val hashMap: HashMap<String, Any> = HashMap()
                    hashMap[WebApiConstants.Home.SEARCH] = s.toString()
                    viewModel.getgloblsearch(hashMap)
                    viewModel.search.set(s.toString())
                }else{
                    initApiCal()
                }

            }

        })
    }


}
