package com.midokter.app.ui.activity.visitedDoctor

import android.content.Intent
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.midokter.app.BuildConfig
import com.midokter.app.R
import com.midokter.app.base.BaseActivity
import com.midokter.app.databinding.ActivityVisitedDoctorsBinding
import com.midokter.app.databinding.ActivityVisitedDoctorsDetailBinding
import com.midokter.app.repositary.WebApiConstants
import com.midokter.app.repositary.model.AppointmentResponse
import com.midokter.app.repositary.model.FeedbackResponse
import com.midokter.app.repositary.model.MainResponse
import com.midokter.app.repositary.model.Response
import com.midokter.app.ui.activity.thankyou.ThankyouActivity
import com.midokter.app.utils.ViewUtils
import kotlinx.android.synthetic.main.activity_visited_doctors_detail.*
import java.util.HashMap

class VisitedDoctorsDetailActivity : BaseActivity<ActivityVisitedDoctorsDetailBinding>(),VisitedDoctorsNavigator {

    private lateinit var viewModel: VisitedDoctorsViewModel
    private lateinit var mDataBinding: ActivityVisitedDoctorsDetailBinding
    private  var experiences: String = "UNLIKE"
    override fun getLayoutId(): Int = R.layout.activity_visited_doctors_detail

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mDataBinding = mViewDataBinding as ActivityVisitedDoctorsDetailBinding
        viewModel = ViewModelProviders.of(this).get(VisitedDoctorsViewModel::class.java)
        mDataBinding.viewmodel = viewModel
        viewModel.navigator = this
        initIntentData()
        observeResponse()
        mDataBinding. button14.setOnClickListener {
            val intent = Intent(applicationContext, ThankyouActivity::class.java)
            startActivity(intent);
        }
        mDataBinding.toolbar4.setNavigationOnClickListener {
            finish()
        }
        mDataBinding.btncancel.setOnClickListener {
            loadingObservable.value = true
            val hashMap: HashMap<String, Any> = HashMap()
            hashMap[WebApiConstants.IntentPass.ID] = viewModel.mupcomingDoctorDetails.value!!.id
            viewModel.cancelclick(hashMap)
        }
    }
    private fun initIntentData() {
        if (intent.getBooleanExtra(WebApiConstants.IntentPass.iscancel,false)){

            val details = intent.getSerializableExtra(WebApiConstants.IntentPass.Appointment) as? AppointmentResponse.Upcomming.Appointment
            viewModel.mupcomingDoctorDetails.value = details
            viewModel.id.set(viewModel.mupcomingDoctorDetails.value!!.id)
            viewModel.name.set(
                viewModel.mupcomingDoctorDetails.value!!.hospital.first_name.plus(" ").plus(
                    viewModel.mupcomingDoctorDetails.value!!.hospital.last_name
                )
            )
            viewModel.bookfor.set(viewModel.mupcomingDoctorDetails.value!!.booking_for)
            viewModel.scheduled_at.set(ViewUtils.getDayAndTimeFormat(viewModel.mupcomingDoctorDetails.value!!.scheduled_at))
            viewModel.status.set(viewModel.mupcomingDoctorDetails.value!!.status)
            viewModel.specialit.set(viewModel.mupcomingDoctorDetails.value!!.hospital?.doctor_profile?.certification)
            viewModel.catagiery.set(viewModel.mupcomingDoctorDetails.value!!.hospital?.doctor_profile?.speciality?.name)
            viewModel.Clinic.set(viewModel.mupcomingDoctorDetails.value!!.hospital.clinic?.name.plus(",").plus(viewModel.mupcomingDoctorDetails.value!!.hospital.clinic?.address))
            if (details!!.hospital.doctor_profile!= null && details!!.hospital.doctor_profile.profile_pic != "") {
                ViewUtils.setImageViewGlide(this@VisitedDoctorsDetailActivity,mDataBinding.imageView12,BuildConfig.BASE_IMAGE_URL+viewModel.mupcomingDoctorDetails.value!!.hospital.doctor_profile?.profile_pic)

            }
            mDataBinding.iscancel = true

        }else {
            mDataBinding.iscancel = false
            if (intent.getSerializableExtra(WebApiConstants.IntentPass.Appointment)!=null) {
                val Appointment = intent.getSerializableExtra(WebApiConstants.IntentPass.Appointment) as? AppointmentResponse.Previous.Appointment
                viewModel.mPastDoctorDetails.value = Appointment
                viewModel.id.set(viewModel.mPastDoctorDetails.value!!.id)
                viewModel.name.set(
                    viewModel.mPastDoctorDetails.value!!.hospital.first_name.plus(" ").plus(
                        viewModel.mPastDoctorDetails.value!!.hospital.last_name
                    )
                )
                viewModel.bookfor.set(viewModel.mPastDoctorDetails.value!!.booking_for)
                viewModel.scheduled_at.set(ViewUtils.getDayAndTimeFormat(viewModel.mPastDoctorDetails.value!!.scheduled_at))
                viewModel.status.set(viewModel.mPastDoctorDetails.value!!.status)
                viewModel.specialit.set(viewModel.mPastDoctorDetails.value!!.hospital?.doctor_profile?.speciality?.name)
                viewModel.catagiery.set(viewModel.mPastDoctorDetails.value!!.hospital?.doctor_profile?.speciality?.name)
                viewModel.Clinic.set(viewModel.mPastDoctorDetails.value!!.hospital.clinic!!.name.plus(",").plus(viewModel.mPastDoctorDetails.value!!.hospital.clinic!!.address))
                if (Appointment!!.hospital.doctor_profile!= null && Appointment!!.hospital.doctor_profile.profile_pic != "") {
                    ViewUtils.setImageViewGlide(this@VisitedDoctorsDetailActivity,mDataBinding.imageView12,BuildConfig.BASE_IMAGE_URL+viewModel.mPastDoctorDetails.value!!.hospital.doctor_profile?.profile_pic)

                }
            }else if (intent.getSerializableExtra(WebApiConstants.IntentPass.VisitedDoctor)!=null){
                val VisitedDoctor = intent.getSerializableExtra(WebApiConstants.IntentPass.VisitedDoctor) as? MainResponse.VisitedDoctor
                viewModel.mVisitedDoctorDetails.value = VisitedDoctor
                viewModel.id.set(viewModel.mVisitedDoctorDetails.value!!.id)
                viewModel.name.set(viewModel.mVisitedDoctorDetails.value!!.hospital.first_name.plus(" ").plus(
                        viewModel.mVisitedDoctorDetails.value!!.hospital.last_name
                    )
                )
                viewModel.bookfor.set(viewModel.mVisitedDoctorDetails.value!!.booking_for)
                viewModel.scheduled_at.set(ViewUtils.getDayAndTimeFormat(viewModel.mVisitedDoctorDetails.value!!.scheduled_at))
                viewModel.status.set(viewModel.mVisitedDoctorDetails.value!!.status)
                viewModel.specialit.set(viewModel.mVisitedDoctorDetails.value!!.hospital?.doctor_profile?.speciality?.name)
              viewModel.Clinic.set(viewModel.mVisitedDoctorDetails.value!!.hospital?.clinic?.name?.plus(",").plus(viewModel.mVisitedDoctorDetails.value!!.hospital?.clinic?.address!!))

                if (VisitedDoctor!!.hospital.doctor_profile!= null && VisitedDoctor!!.hospital.doctor_profile.profile_pic != "") {
                    ViewUtils.setImageViewGlide(this@VisitedDoctorsDetailActivity,mDataBinding.imageView12,BuildConfig.BASE_IMAGE_URL+viewModel.mVisitedDoctorDetails.value!!.hospital.doctor_profile?.profile_pic)

                }
            }

        }
    }
    private fun observeResponse(){
        viewModel.getErrorObservable().observe(this, Observer<String> { message ->
            hideLoading()
            ViewUtils.showToast(this@VisitedDoctorsDetailActivity, message, false)
        })
        viewModel.mCancelResponse.observe(this, Observer<Response> {

            hideLoading()
            ViewUtils.showToast(this@VisitedDoctorsDetailActivity, it.message, true)
            finish()


        })
        viewModel.mFeedbackResponse.observe(this, Observer<FeedbackResponse> {

            hideLoading()
            ViewUtils.showToast(this@VisitedDoctorsDetailActivity, it.message, true)
            openNewActivity(this@VisitedDoctorsDetailActivity, ThankyouActivity::class.java, true)


        })
    }

    override fun onlike() {
        experiences = "LIKE"

    }

    override fun onunlike() {
        experiences = "DISLIKE"
    }

    override fun onSubmit() {
        loadingObservable.value = true
        val hashMap: HashMap<String, Any> = HashMap()
        hashMap[WebApiConstants.Feedback.hospital_id] = viewModel.id.get().toString()
        hashMap[WebApiConstants.Feedback.experiences] =experiences
        hashMap[WebApiConstants.Feedback.visited_for] =  mDataBinding.divider2.text.toString()
        hashMap[WebApiConstants.Feedback.comments] = mDataBinding.editText7.text.toString()
        viewModel.postfeedback(hashMap)
    }

}
