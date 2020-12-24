package com.telehealthmanager.app.ui.activity.addmedicalrecord

import android.content.Intent
import android.net.Uri
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.telehealthmanager.app.BaseApplication
import com.telehealthmanager.app.BuildConfig
import com.telehealthmanager.app.R
import com.telehealthmanager.app.base.BaseActivity
import com.telehealthmanager.app.data.PreferenceHelper
import com.telehealthmanager.app.databinding.ActivityMedicalRecordDetailsBinding
import com.telehealthmanager.app.repositary.WebApiConstants
import com.telehealthmanager.app.repositary.model.ResponseMedicalDetails
import com.telehealthmanager.app.utils.CustomBackClick
import java.io.Serializable


class MedicalRecordDetails : BaseActivity<ActivityMedicalRecordDetailsBinding>(), DoctorMedicalRecordsNavigator, CustomBackClick {
    private val preferenceHelper = PreferenceHelper(BaseApplication.baseApplication)
    private lateinit var viewModel: DoctorMedicalRecordsViewModel
    private lateinit var mDataBinding: ActivityMedicalRecordDetailsBinding

    override fun getLayoutId(): Int = R.layout.activity_medical_record_details

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mDataBinding = mViewDataBinding as ActivityMedicalRecordDetailsBinding
        viewModel = ViewModelProvider(this).get(DoctorMedicalRecordsViewModel::class.java)
        mDataBinding.viewmodel = viewModel
        viewModel.navigator = this
        viewModel.toolBarTile.value = getString(R.string.patient_record)
        viewModel.setOnClickListener(this@MedicalRecordDetails)
        intentData()

        mDataBinding.viewFile.setOnClickListener {
            val intent = Intent(this, RecordImageFile::class.java)
            intent.putExtra("file", viewModel.mViewFile.get().toString())
            startActivity(intent)
        }
    }

    private fun intentData() {
        val medicalData = intent.getSerializableExtra(WebApiConstants.IntentPass.MEDICAL_RECORD) as ResponseMedicalDetails.RecordDetail
        if (medicalData != null) {
            viewModel.mTestTitle.set(medicalData.title)
            viewModel.mTestDescription.set(medicalData.instruction)
            if (medicalData?.file != null) {
                viewModel.mViewFile.set(BuildConfig.BASE_IMAGE_URL + medicalData?.file?.toString())
            }
        }
    }

    override fun clickBackPress() {
        finish()
    }
}