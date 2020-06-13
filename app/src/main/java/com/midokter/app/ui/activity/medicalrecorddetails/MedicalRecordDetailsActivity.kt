package com.midokter.app.ui.activity.medicalrecorddetails

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.midokter.app.BaseApplication
import com.midokter.app.BuildConfig
import com.midokter.app.R
import com.midokter.app.base.BaseActivity
import com.midokter.app.data.PreferenceHelper
import com.midokter.app.databinding.ActivityMedicalRecordDetailsBinding
import com.midokter.app.repositary.WebApiConstants
import com.midokter.app.repositary.model.Hospital1
import com.midokter.app.repositary.model.Medical
import com.midokter.app.repositary.model.Speciality
import com.midokter.app.utils.ViewUtils

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
