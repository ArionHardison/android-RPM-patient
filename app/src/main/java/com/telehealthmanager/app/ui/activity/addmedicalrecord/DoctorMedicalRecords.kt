package com.telehealthmanager.app.ui.activity.addmedicalrecord

import android.content.Intent
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.telehealthmanager.app.R
import com.telehealthmanager.app.base.BaseActivity
import com.telehealthmanager.app.data.Constant
import com.telehealthmanager.app.databinding.ActivityDoctorMedicalRecordBinding
import com.telehealthmanager.app.repositary.WebApiConstants
import com.telehealthmanager.app.repositary.model.ResponseMedicalDetails
import com.telehealthmanager.app.ui.adapter.DoctorRecordAdapter
import com.telehealthmanager.app.ui.adapter.IDoctorRecordClick
import com.telehealthmanager.app.utils.CustomBackClick
import com.telehealthmanager.app.utils.ViewUtils
import java.io.Serializable

class DoctorMedicalRecords : BaseActivity<ActivityDoctorMedicalRecordBinding>(), DoctorMedicalRecordsNavigator, CustomBackClick, IDoctorRecordClick {

    private lateinit var viewModel: DoctorMedicalRecordsViewModel
    private lateinit var mDataBinding: ActivityDoctorMedicalRecordBinding
    private var mAdapter: DoctorRecordAdapter? = null

    override fun getLayoutId(): Int = R.layout.activity_doctor_medical_record

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mDataBinding = mViewDataBinding as ActivityDoctorMedicalRecordBinding
        viewModel = ViewModelProviders.of(this).get(DoctorMedicalRecordsViewModel::class.java)
        mDataBinding.viewmodel = viewModel
        viewModel.navigator = this
        viewModel.toolBarTile.value = getString(R.string.patient_medical_record)
        viewModel.setOnClickListener(this@DoctorMedicalRecords)
        initApi()
        initObservable()
    }

    override fun clickBackPress() {
        finish()
    }

    private fun initApi() {
        viewModel.loadingProgress.value = true
        viewModel.getRecordsList(intent.getIntExtra(WebApiConstants.IntentPass.ID, 1).toString())
    }

    private fun initObservable() {
        viewModel.loadingProgress.observe(this, Observer {
            if (it)
                showLoading()
            else
                hideLoading()
        })

        viewModel.mResponseMedicalDetails.observe(this, Observer {
            loadingObservable.value = false
            if (!it.record_details.isNullOrEmpty()) {
                mDataBinding.tvNotFound.visibility = View.GONE
                viewModel.mRecordDetailList = it.record_details as MutableList<ResponseMedicalDetails.RecordDetail>
                mDataBinding.rvMedicalRecords.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
                mDataBinding.rvMedicalRecords.layoutManager = LinearLayoutManager(this)
                mAdapter = DoctorRecordAdapter(viewModel.mRecordDetailList!!, this, this)
                mDataBinding.adapter = mAdapter
                mAdapter!!.notifyDataSetChanged()
            } else {
                mDataBinding.tvNotFound.visibility = View.VISIBLE
            }
            if (!it.record_details.isNullOrEmpty()) {
                viewModel.mRecordDetailList = it.record_details as MutableList<ResponseMedicalDetails.RecordDetail>
            }
        })

        viewModel.getErrorObservable().observe(this, Observer<String> { message ->
            viewModel.loadingProgress.value = false
            ViewUtils.showToast(this, message, false)
        })
    }

    override fun onItemClicked(item: ResponseMedicalDetails.RecordDetail) {
        val intent = Intent(this, MedicalRecordDetails::class.java)
        intent.putExtra(WebApiConstants.IntentPass.MEDICAL_RECORD, item as Serializable)
        startActivity(intent)
    }

}