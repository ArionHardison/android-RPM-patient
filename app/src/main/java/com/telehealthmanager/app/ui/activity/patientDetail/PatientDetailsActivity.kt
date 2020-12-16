package com.telehealthmanager.app.ui.activity.patientDetail

import android.content.Intent
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.telehealthmanager.app.BaseApplication
import com.telehealthmanager.app.BuildConfig
import com.telehealthmanager.app.R
import com.telehealthmanager.app.base.BaseActivity
import com.telehealthmanager.app.data.PreferenceHelper
import com.telehealthmanager.app.data.PreferenceKey
import com.telehealthmanager.app.data.getValue
import com.telehealthmanager.app.databinding.ActivityPatientDetailsBinding
import com.telehealthmanager.app.repositary.model.BookedResponse
import com.telehealthmanager.app.ui.activity.success.SuccessActivity
import com.telehealthmanager.app.utils.CustomBackClick
import com.telehealthmanager.app.utils.ViewUtils
import java.util.*

class PatientDetailsActivity : BaseActivity<ActivityPatientDetailsBinding>(),
    PatientDetailNavigator, CustomBackClick {

    private val preferenceHelper = PreferenceHelper(BaseApplication.baseApplication)
    private lateinit var viewModel: PatientDetailViewModel
    private lateinit var mDataBinding: ActivityPatientDetailsBinding


    override fun getLayoutId(): Int = R.layout.activity_patient_details

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mDataBinding = mViewDataBinding as ActivityPatientDetailsBinding
        viewModel = ViewModelProviders.of(this).get(PatientDetailViewModel::class.java)
        mDataBinding.viewmodel = viewModel
        viewModel.navigator = this

        mDataBinding.textView63.text = preferenceHelper.getValue(PreferenceKey.VISIT_PURPOSE, "").toString()
        mDataBinding.textView65.text = ViewUtils.getDayAndTimeFormat(preferenceHelper.getValue(PreferenceKey.SCHEDULED_DATE, "").toString())
        mDataBinding.textView67.text = preferenceHelper.getValue(PreferenceKey.SELECTED_DOC_NAME, "Dr.Alvin").toString()
        mDataBinding.textView68.text = preferenceHelper.getValue(PreferenceKey.SELECTED_DOC_ADDRESS, "The Apollo,Manhattan").toString()

        if (preferenceHelper.getValue(PreferenceKey.SELECTED_DOC_IMAGE, "").toString() != "") {
            ViewUtils.setDocViewGlide(
                this@PatientDetailsActivity, mDataBinding.imageView20,
                BuildConfig.BASE_IMAGE_URL + preferenceHelper.getValue(PreferenceKey.SELECTED_DOC_IMAGE, "").toString()
            )
        }

        mDataBinding.button18.setOnClickListener {
            if (mDataBinding.nameEt.text.toString().isNullOrBlank()) {
                Toast.makeText(applicationContext, "Invalid Name", Toast.LENGTH_LONG).show()
            } else if (mDataBinding.emailEt.text.toString().isNullOrBlank()) {
                Toast.makeText(applicationContext, "Invalid email", Toast.LENGTH_LONG).show()
            } else if (mDataBinding.phoneEt.text.toString().isNullOrBlank()) {
                Toast.makeText(applicationContext, "Invalid Phone number", Toast.LENGTH_LONG).show()
            } else if (!mDataBinding.radioCard.isChecked && !mDataBinding.radioWallet.isChecked) {
                Toast.makeText(applicationContext, "Please Select payment mode", Toast.LENGTH_LONG).show()
            } else {
                loadingObservable.value = true
                val bookMap: HashMap<String, Any> = HashMap()
                bookMap["selectedPatient"] = preferenceHelper.getValue(PreferenceKey.PATIENT_ID, 0).toString()
                bookMap["doctor_id"] = preferenceHelper.getValue(PreferenceKey.SELECTED_DOC_ID, "").toString()
                bookMap["booking_for"] = preferenceHelper.getValue(PreferenceKey.VISIT_PURPOSE, "").toString()
                bookMap["scheduled_at"] = preferenceHelper.getValue(PreferenceKey.SCHEDULED_DATE, "").toString()
                bookMap["consult_time"] = "15"
                bookMap["service_id"] = preferenceHelper.getValue(PreferenceKey.SELECTED_DOC_SPECIALITY_ID, "0").toString()
                bookMap["appointment_type"] = "OFFLINE"
                bookMap["description"] = "Appointment"
                if (mDataBinding.radioCard.isChecked) {
                    bookMap["payment_mode"] = "stripe"
                    bookMap["card_id "] = ""
                } else if (mDataBinding.radioWallet.isChecked) {
                    bookMap["payment_mode"] = "wallet"
                }
                viewModel.bookDoctor(bookMap)
            }
        }

        viewModel.setOnClickListener(this@PatientDetailsActivity)
        viewModel.toolBarTile.value = getString(R.string.enter_patient_details)
        observeResponse()
    }

    override fun clickBackPress() {
        finish()
    }

    private fun observeResponse() {
        viewModel.mBookedResponse.observe(this@PatientDetailsActivity, Observer<BookedResponse> {
            loadingObservable.value = false
            if (it.message == null) {
                goToBooked()
            } else
                ViewUtils.showToast(this@PatientDetailsActivity, it.message, false)
        })

        viewModel.getErrorObservable().observe(this, Observer<String> { message ->
            loadingObservable.value = false
            ViewUtils.showToast(this@PatientDetailsActivity, message, false)
        })
    }

    override fun goToBooked() {
        val intent = Intent(applicationContext, SuccessActivity::class.java)
        startActivity(intent);
        finish()
    }
}
