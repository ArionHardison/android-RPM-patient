package com.midokter.app.ui.activity.patientDetail

import android.content.Intent
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.midokter.app.BaseApplication
import com.midokter.app.BuildConfig
import com.midokter.app.R
import com.midokter.app.base.BaseActivity
import com.midokter.app.data.PreferenceHelper
import com.midokter.app.data.PreferenceKey
import com.midokter.app.data.getValue
import com.midokter.app.databinding.ActivityFindDoctorBookingBinding
import com.midokter.app.databinding.ActivityPatientDetailsBinding
import com.midokter.app.repositary.WebApiConstants
import com.midokter.app.repositary.model.BookedResponse
import com.midokter.app.repositary.model.RegisterResponse
import com.midokter.app.ui.activity.findDoctors.FindDoctorsViewModel
import com.midokter.app.ui.activity.payment.PaymentActivity
import com.midokter.app.ui.activity.success.SuccessActivity
import com.midokter.app.utils.ViewUtils
import kotlinx.android.synthetic.main.activity_patient_details.*
import java.util.HashMap

class PatientDetailsActivity : BaseActivity<ActivityPatientDetailsBinding>(),
    PatientDetailNavigator {

    private val preferenceHelper = PreferenceHelper(BaseApplication.baseApplication)
    private lateinit var viewModel: PatientDetailViewModel
    private lateinit var mDataBinding: ActivityPatientDetailsBinding
    val bookDoctor_Map: HashMap<String, Any> = HashMap()

    override fun getLayoutId(): Int = R.layout.activity_patient_details

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mDataBinding = mViewDataBinding as ActivityPatientDetailsBinding
        viewModel = ViewModelProviders.of(this).get(PatientDetailViewModel::class.java)
        mDataBinding.viewmodel = viewModel
        viewModel.navigator = this

        mDataBinding.textView63.text = preferenceHelper.getValue(PreferenceKey.VISIT_PURPOSE,"").toString()
        mDataBinding.textView65.text = ViewUtils.getDayAndTimeFormat(preferenceHelper.getValue(PreferenceKey.SCHEDULED_DATE,"").toString())
        mDataBinding.textView67.text = preferenceHelper.getValue(PreferenceKey.SELECTED_DOC_NAME,"Dr.Alvin").toString()
        mDataBinding.textView68.text = preferenceHelper.getValue(PreferenceKey.SELECTED_DOC_ADDRESS,"The Apollo,Manhattan").toString()

        if (preferenceHelper.getValue(PreferenceKey.SELECTED_DOC_IMAGE,"").toString()!=""){
            ViewUtils.setImageViewGlide(this@PatientDetailsActivity,mDataBinding.imageView20,
                BuildConfig.BASE_IMAGE_URL+preferenceHelper.getValue(PreferenceKey.SELECTED_DOC_IMAGE,"").toString())
        }

        mDataBinding.button18.setOnClickListener {

            if (mDataBinding.nameEt.text.toString().isNullOrBlank()){
                Toast.makeText(applicationContext, "Invalid Name", Toast.LENGTH_LONG).show()
            }else if (mDataBinding.emailEt.text.toString().isNullOrBlank()){
                Toast.makeText(applicationContext, "Invalid email", Toast.LENGTH_LONG).show()
            }else if (mDataBinding.phoneEt.text.toString().isNullOrBlank()){
                Toast.makeText(applicationContext, "Invalid Phone number", Toast.LENGTH_LONG).show()
            }else{
                loadingObservable.value = true
                bookDoctor_Map["doctor_id"]= preferenceHelper.getValue(PreferenceKey.SELECTED_DOC_ID,"").toString()
                bookDoctor_Map["booking_for"]= preferenceHelper.getValue(PreferenceKey.VISIT_PURPOSE,"").toString()
                bookDoctor_Map["scheduled_at"]= preferenceHelper.getValue(PreferenceKey.SCHEDULED_DATE,"").toString()
                bookDoctor_Map["consult_time"]= "15"
                bookDoctor_Map["appointment_type"]= "OFFLINE"
                bookDoctor_Map["description"]= ""

//                loadingObservable.value = true
                viewModel.BookDoctor(bookDoctor_Map)

            }
        }

        mDataBinding.toolbar6.setNavigationOnClickListener {
            finish()
        }

        observeResponse()

    }

    private fun observeResponse() {
        viewModel.mBookedResponse.observe(this@PatientDetailsActivity, Observer<BookedResponse> {
            loadingObservable.value = false
            if (it.status) {

                goToBooked()

            }else
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
