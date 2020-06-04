package com.midokter.app.ui.activity.findDoctors

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import com.midokter.app.databinding.ActivityFindDoctorCategoriesBinding
import com.midokter.app.databinding.ActivityFindDoctorsListBinding
import com.midokter.app.repositary.model.DoctorListResponse
import com.midokter.app.ui.activity.searchDoctor.SearchDoctorDetailActivity
import com.midokter.app.ui.adapter.CategoriesListAdapter
import com.midokter.app.ui.adapter.FindDoctorListAdapter
import com.midokter.app.ui.adapter.IDoctorListener
import com.midokter.app.utils.ViewUtils
import kotlinx.android.synthetic.main.activity_find_doctor_categories.*
import kotlinx.android.synthetic.main.activity_find_doctors_list.*

class FindDoctorsListActivity : BaseActivity<ActivityFindDoctorsListBinding>(),FindDoctorsNavigator,IDoctorListener {

    val doctorList: ArrayList<String> = ArrayList()

    private lateinit var viewModel: FindDoctorsViewModel
    private lateinit var mDataBinding: ActivityFindDoctorsListBinding
    private var mAdapter: FindDoctorListAdapter? = null

    override fun getLayoutId(): Int = R.layout.activity_find_doctors_list

    override fun initView(mViewDataBinding: ViewDataBinding?) {

        mDataBinding = mViewDataBinding as ActivityFindDoctorsListBinding
        viewModel = ViewModelProviders.of(this).get(FindDoctorsViewModel::class.java)
        mDataBinding.viewmodel = viewModel
        viewModel.navigator = this

        initApiCal()
        initAdapter()
        observeResponse()
        //categoriesList()

    }
    private fun initApiCal() {
        loadingObservable.value = true
        viewModel.getDoctorByCategorys()

    }
    private fun observeResponse() {

        viewModel.mDoctorResponse.observe(this, Observer<DoctorListResponse> {


            viewModel.mDoctorslist = it.doctor as MutableList<DoctorListResponse.Doctor>?
            if (viewModel.mDoctorslist!!.size > 0) {
                mDataBinding.tvNotFound.visibility = View.GONE
            } else {
                mDataBinding.tvNotFound.visibility = View.VISIBLE
            }
            mAdapter = FindDoctorListAdapter(viewModel.mDoctorslist!!,this@FindDoctorsListActivity,this)
            mDataBinding.adapter = mAdapter
            mAdapter!!.notifyDataSetChanged()
            loadingObservable.value = false




        })
        viewModel.getErrorObservable().observe(this, Observer<String> { message ->
            loadingObservable.value = false
            ViewUtils.showToast(this@FindDoctorsListActivity, message, false)
        })
    }

    private fun initAdapter() {
        mAdapter = FindDoctorListAdapter( viewModel.mDoctorslist!!,this@FindDoctorsListActivity,this)
        mDataBinding.adapter = mAdapter
         mDataBinding.rvFinddoctorsList.addItemDecoration(
             DividerItemDecoration(
                 applicationContext,
                 DividerItemDecoration.VERTICAL
             )
         )
        mDataBinding.rvFinddoctorsList.layoutManager =LinearLayoutManager(applicationContext)
        mAdapter!!.notifyDataSetChanged()

        mDataBinding.editText9.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.length>0)
                    mDataBinding.adapter!!.filter.filter(s)
                else{
                  // mAdapter = FindDoctorListAdapter( viewModel.mDoctorslist!!, this@FindDoctorsListActivity,this)
                    mDataBinding.adapter = mAdapter
                    mDataBinding.adapter!!.notifyDataSetChanged()
                }

            }

        })
    }

    override fun onbookclick(selectedItem: DoctorListResponse.Doctor) {
         val intent = Intent(this@FindDoctorsListActivity, FindDoctorBookingActivity::class.java)
    startActivity(intent);
}

    override fun onitemclick(selectedItem: DoctorListResponse.Doctor) {
        val intent = Intent(this@FindDoctorsListActivity, SearchDoctorDetailActivity::class.java)
        startActivity(intent);

    }

    override fun oncallclick(phone: String) {

    }

    private fun categoriesList() {
        rv_finddoctors_list.layoutManager = LinearLayoutManager(applicationContext)
        //rv_finddoctors_list.adapter = applicationContext?.let { FindDoctorListAdapter(doctorList, it,F) }
        doctorList.add("Dr.Alvin")
        doctorList.add("Dr.Madison")
        doctorList.add("Dr.Joe")
        doctorList.add("Dr.Mellisa")
        doctorList.add("Dr.Glen")
    }
}
