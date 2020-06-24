package com.midokter.app.ui.activity.searchGlobal

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
import com.midokter.app.databinding.ActivityGlobalSearchBinding
import com.midokter.app.repositary.WebApiConstants
import com.midokter.app.repositary.model.Hospital
import com.midokter.app.repositary.model.SearchResponse
import com.midokter.app.ui.adapter.SearchDoctorsListAdapter
import com.midokter.app.ui.adapter.SearchcatagoryListAdapter
import com.midokter.app.utils.ViewUtils
import java.util.HashMap

class SearchGlobalActivity : BaseActivity<ActivityGlobalSearchBinding>(),SearchGlobalNavigator {



    private lateinit var viewModel: SearchGlobalViewModel
    private lateinit var mDataBinding: ActivityGlobalSearchBinding
    private var mAdapter: SearchDoctorsListAdapter? = null
    private var mcatagoryAdapter: SearchcatagoryListAdapter? = null

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


            loadingObservable.value = false




        })
        viewModel.getErrorObservable().observe(this, Observer<String> { message ->
            loadingObservable.value = false
            ViewUtils.showToast(this@SearchGlobalActivity, message, false)
        })
    }

    private fun initAdapter() {
        mAdapter = SearchDoctorsListAdapter( viewModel.mDoctorslist!!,this@SearchGlobalActivity)
        mDataBinding.doctoradapter = mAdapter
        mDataBinding.rvDoctorsList.addItemDecoration(
            DividerItemDecoration(
                applicationContext,
                DividerItemDecoration.HORIZONTAL
            )
        )
        mDataBinding.rvDoctorsList.layoutManager =LinearLayoutManager(applicationContext)
        mAdapter!!.notifyDataSetChanged()
//catagory
        mcatagoryAdapter = SearchcatagoryListAdapter( viewModel.mCatagorylist!!,this@SearchGlobalActivity)
        mDataBinding.specialitiesadapter = mcatagoryAdapter
        mDataBinding.rvSpecialities.addItemDecoration(
            DividerItemDecoration(
                applicationContext,
                DividerItemDecoration.HORIZONTAL
            )
        )
        mDataBinding.rvSpecialities.layoutManager =LinearLayoutManager(applicationContext)
        mcatagoryAdapter!!.notifyDataSetChanged()

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
