package com.midokter.app.ui.activity.findDoctors

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.midokter.app.BaseApplication
import com.midokter.app.R
import com.midokter.app.base.BaseActivity
import com.midokter.app.data.PreferenceHelper
import com.midokter.app.data.PreferenceKey
import com.midokter.app.data.setValue
import com.midokter.app.databinding.ActivityFindDoctorsListBinding
import com.midokter.app.repositary.WebApiConstants
import com.midokter.app.repositary.model.DoctorListResponse
import com.midokter.app.ui.activity.searchDoctor.SearchDoctorDetailActivity
import com.midokter.app.ui.adapter.FindDoctorListAdapter
import com.midokter.app.ui.adapter.IDoctorListener
import com.midokter.app.utils.ViewUtils
import kotlinx.android.synthetic.main.activity_find_doctors_list.*
import java.io.Serializable

class FindDoctorsListActivity : BaseActivity<ActivityFindDoctorsListBinding>(),FindDoctorsNavigator,IDoctorListener {

    val doctorList: ArrayList<String> = ArrayList()

    private lateinit var viewModel: FindDoctorsViewModel
    private lateinit var mDataBinding: ActivityFindDoctorsListBinding
    private var mAdapter: FindDoctorListAdapter? = null

    private val preferenceHelper = PreferenceHelper(BaseApplication.baseApplication)

    override fun getLayoutId(): Int = R.layout.activity_find_doctors_list

    override fun initView(mViewDataBinding: ViewDataBinding?) {

        mDataBinding = mViewDataBinding as ActivityFindDoctorsListBinding
        viewModel = ViewModelProviders.of(this).get(FindDoctorsViewModel::class.java)
        mDataBinding.viewmodel = viewModel
        viewModel.navigator = this

        initApiCal( intent.getIntExtra(WebApiConstants.IntentPass.ID,1))
        initAdapter()
        observeResponse()
        //categoriesList()

    }


    private fun initApiCal(id: Int) {
        loadingObservable.value = true
        viewModel.getDoctorByCategorys(id)

    }
    private fun observeResponse() {

        viewModel.mDoctorResponse.observe(this, Observer<DoctorListResponse> {


            viewModel.mDoctorslist = it.Specialities.doctor_profile as MutableList<DoctorListResponse.specialities.DoctorProfile>?
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

    override fun onbookclick(selectedItem: DoctorListResponse.specialities.DoctorProfile) {
        preferenceHelper.setValue(PreferenceKey.SELECTED_DOC_ID, selectedItem.doctor_id.toString())
        preferenceHelper.setValue(PreferenceKey.SELECTED_DOC_NAME, selectedItem.medical_assoc_name)
        preferenceHelper.setValue(PreferenceKey.SELECTED_DOC_IMAGE, selectedItem.profile_pic)
        preferenceHelper.setValue(PreferenceKey.SELECTED_DOC_ADDRESS, selectedItem.address)
         val intent = Intent(this@FindDoctorsListActivity, FindDoctorBookingActivity::class.java)
    startActivity(intent);
}

    override fun onitemclick(selectedItem: DoctorListResponse.specialities.DoctorProfile) {
        val intent = Intent(this@FindDoctorsListActivity, SearchDoctorDetailActivity::class.java)
        intent.putExtra(WebApiConstants.IntentPass.DoctorProfile,selectedItem as Serializable)
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
