package com.telehealthmanager.app.ui.activity.medicalrecorddetails

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.telehealthmanager.app.BaseApplication
import com.telehealthmanager.app.BuildConfig
import com.telehealthmanager.app.R
import com.telehealthmanager.app.base.BaseActivity
import com.telehealthmanager.app.data.PreferenceHelper
import com.telehealthmanager.app.databinding.ActivityMedicalRecordDetailsBinding
import com.telehealthmanager.app.repositary.WebApiConstants
import com.telehealthmanager.app.repositary.model.Hospital1
import com.telehealthmanager.app.repositary.model.Medical
import com.telehealthmanager.app.repositary.model.Speciality
import com.telehealthmanager.app.utils.ViewUtils

class MedicalRecordDetailsActivity : BaseActivity<ActivityMedicalRecordDetailsBinding>(), MedicalRecordDetailsNavigator {

    private val preferenceHelper = PreferenceHelper(BaseApplication.baseApplication)
    private lateinit var viewModel: MedicalRecordDetailsViewModel
    private lateinit var mDataBinding: ActivityMedicalRecordDetailsBinding

    override fun getLayoutId(): Int = R.layout.activity_medical_record_details

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mDataBinding = mViewDataBinding as ActivityMedicalRecordDetailsBinding
        viewModel = ViewModelProviders.of(this).get(MedicalRecordDetailsViewModel::class.java)
        mDataBinding.viewmodel = viewModel
        viewModel.navigator = this

        initUi()
        initApiCal()
        observeResponse()
    }

    private fun initUi() {
        mDataBinding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun initApiCal() {
        val mMedicalRecord: Medical = intent.getSerializableExtra(WebApiConstants.IntentPass.MEDICAL_RECORD) as Medical
        val hospital: Hospital1 = mMedicalRecord.hospital
        viewModel.mDoctorName.set(hospital.first_name + " " + hospital.last_name)
        viewModel.mConsultedOnDate.set(ViewUtils.getScheduleDayFormat(mMedicalRecord.scheduled_at))
        viewModel.mRecordName.set("Allopath")

        if (hospital.doctor_profile != null) {
            Glide.with(this)
                .load(BuildConfig.BASE_IMAGE_URL + hospital.doctor_profile.profile_pic)
                .placeholder(R.drawable.place_holder_image)
                .error(R.drawable.place_holder_image)
                .fallback(R.drawable.place_holder_image)
                .into(mDataBinding.mrDocPics)
        }

        if (hospital.doctor_profile.speciality != null) {
            val specialities: Speciality = hospital.doctor_profile.speciality
            viewModel.mSpecialist.set(specialities.name)
        }
    }

    private fun observeResponse() {

    }

}
