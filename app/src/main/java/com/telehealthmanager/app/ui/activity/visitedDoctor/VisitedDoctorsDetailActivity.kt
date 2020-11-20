package com.telehealthmanager.app.ui.activity.visitedDoctor

import android.content.Intent
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.telehealthmanager.app.BuildConfig
import com.telehealthmanager.app.R
import com.telehealthmanager.app.base.BaseActivity
import com.telehealthmanager.app.databinding.ActivityVisitedDoctorsDetailBinding
import com.telehealthmanager.app.repositary.WebApiConstants
import com.telehealthmanager.app.repositary.model.AppointmentResponse
import com.telehealthmanager.app.repositary.model.FeedbackResponse
import com.telehealthmanager.app.repositary.model.MainResponse
import com.telehealthmanager.app.repositary.model.Response
import com.telehealthmanager.app.ui.activity.thankyou.ThankyouActivity
import com.telehealthmanager.app.utils.CustomBackClick
import com.telehealthmanager.app.utils.ViewUtils
import java.util.*


class VisitedDoctorsDetailActivity : BaseActivity<ActivityVisitedDoctorsDetailBinding>(),
    VisitedDoctorsNavigator, CustomBackClick {

    private lateinit var viewModel: VisitedDoctorsViewModel
    private lateinit var mDataBinding: ActivityVisitedDoctorsDetailBinding
    private var experiences: String = "UNLIKE"
    override fun getLayoutId(): Int = R.layout.activity_visited_doctors_detail

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mDataBinding = mViewDataBinding as ActivityVisitedDoctorsDetailBinding
        viewModel = ViewModelProviders.of(this).get(VisitedDoctorsViewModel::class.java)
        mDataBinding.viewmodel = viewModel
        viewModel.navigator = this
        initIntentData()
        observeResponse()
        viewModel.setOnClickListener(this@VisitedDoctorsDetailActivity)
        viewModel.toolBarTile.value = resources.getString(R.string.visted_doctor)

        mDataBinding.button14.setOnClickListener {
            val intent = Intent(applicationContext, ThankyouActivity::class.java)
            startActivity(intent);
        }
        mDataBinding.btncancel.setOnClickListener {
            loadingObservable.value = true
            val hashMap: HashMap<String, Any> = HashMap()
            hashMap[WebApiConstants.IntentPass.ID] = viewModel.mupcomingDoctorDetails.value!!.id
            viewModel.cancelclick(hashMap)
        }
    }

    override fun clickBackPress() {
        finish()
    }

    private fun initIntentData() {
        if (intent.getBooleanExtra(WebApiConstants.IntentPass.iscancel, false)) {
            val details = intent.getSerializableExtra(WebApiConstants.IntentPass.Appointment) as? AppointmentResponse.Upcomming.Appointment
            viewModel.mupcomingDoctorDetails.value = details
            viewModel.id.set(viewModel.mupcomingDoctorDetails.value!!.id)
            viewModel.name.set(viewModel.mupcomingDoctorDetails.value!!.hospital?.first_name.plus(" ").plus(viewModel.mupcomingDoctorDetails.value!!.hospital?.last_name))
            viewModel.bookfor.set(viewModel.mupcomingDoctorDetails.value!!.booking_for ?: "")
            viewModel.scheduled_at.set(ViewUtils.getDayAndTimeFormat(viewModel.mupcomingDoctorDetails.value!!.scheduled_at ?: ""))
            viewModel.status.set(viewModel.mupcomingDoctorDetails.value!!.status ?: "")
            viewModel.specialit.set(viewModel.mupcomingDoctorDetails.value!!.hospital?.doctor_profile?.certification ?: "")
            viewModel.catagiery.set(viewModel.mupcomingDoctorDetails.value!!.hospital?.doctor_profile?.speciality?.name ?: "")
            if (viewModel.mupcomingDoctorDetails.value!!.hospital?.clinic != null)
                viewModel.Clinic.set(
                    viewModel.mupcomingDoctorDetails.value!!.hospital?.clinic?.name.plus(
                        ","
                    ).plus(viewModel.mupcomingDoctorDetails.value!!.hospital?.clinic?.address ?: "")
                )
            if (details!!.hospital?.doctor_profile?.profile_pic != "") {
                ViewUtils.setImageViewGlide(
                    this@VisitedDoctorsDetailActivity, mDataBinding.imageView12,
                    BuildConfig.BASE_IMAGE_URL + viewModel.mupcomingDoctorDetails.value!!.hospital?.doctor_profile?.profile_pic
                )
            }
            mDataBinding.iscancel = true
        } else {
            mDataBinding.iscancel = false
            if (intent.getSerializableExtra(WebApiConstants.IntentPass.Appointment) != null) {
                val Appointment = intent.getSerializableExtra(WebApiConstants.IntentPass.Appointment) as? AppointmentResponse.Previous.Appointment
                viewModel.mPastDoctorDetails.value = Appointment
                viewModel.id.set(viewModel.mPastDoctorDetails.value!!.id)
                viewModel.name.set(viewModel.mPastDoctorDetails.value!!.hospital?.first_name?.plus(" ").plus(viewModel.mPastDoctorDetails.value!!.hospital?.last_name))
                viewModel.bookfor.set(viewModel.mPastDoctorDetails.value!!.booking_for ?: "")
                viewModel.scheduled_at.set(ViewUtils.getDayAndTimeFormat(viewModel.mPastDoctorDetails.value!!.scheduled_at ?: ""))
                viewModel.status.set(viewModel.mPastDoctorDetails.value!!.status ?: "")
                viewModel.specialit.set(viewModel.mPastDoctorDetails.value!!.hospital?.doctor_profile?.speciality?.name ?: "")
                viewModel.catagiery.set(viewModel.mPastDoctorDetails.value!!.hospital?.doctor_profile?.speciality?.name ?: "")
                if (viewModel.mPastDoctorDetails.value!!.hospital?.clinic != null)
                    viewModel.Clinic.set(viewModel.mPastDoctorDetails.value!!.hospital?.clinic?.name.plus(",").plus(viewModel.mPastDoctorDetails.value!!.hospital?.clinic?.address))
                if (Appointment!!.hospital?.doctor_profile != null && Appointment!!.hospital?.doctor_profile.profile_pic != "") {
                    ViewUtils.setImageViewGlide(
                        this@VisitedDoctorsDetailActivity,
                        mDataBinding.imageView12,
                        BuildConfig.BASE_IMAGE_URL + viewModel.mPastDoctorDetails.value!!.hospital.doctor_profile?.profile_pic
                    )
                }
            } else if (intent.getSerializableExtra(WebApiConstants.IntentPass.VisitedDoctor) != null) {
                val VisitedDoctor = intent.getSerializableExtra(WebApiConstants.IntentPass.VisitedDoctor) as? MainResponse.VisitedDoctor
                viewModel.mVisitedDoctorDetails.value = VisitedDoctor
                viewModel.id.set(viewModel.mVisitedDoctorDetails.value!!.id)
                viewModel.name.set(viewModel.mVisitedDoctorDetails.value!!.hospital?.first_name.plus(" ").plus(viewModel.mVisitedDoctorDetails.value!!.hospital?.last_name))
                viewModel.bookfor.set(viewModel.mVisitedDoctorDetails.value!!.booking_for ?: "")
                viewModel.scheduled_at.set(ViewUtils.getDayAndTimeFormat(viewModel.mVisitedDoctorDetails.value!!.scheduled_at ?: ""))
                viewModel.status.set(viewModel.mVisitedDoctorDetails.value!!.status ?: "")
                viewModel.catagiery.set(viewModel.mVisitedDoctorDetails.value!!.hospital?.doctor_profile?.speciality?.name ?: "")
                viewModel.specialit.set(viewModel.mVisitedDoctorDetails.value!!.hospital?.doctor_profile?.speciality?.name ?: "")
                if (viewModel.mVisitedDoctorDetails.value!!.hospital?.clinic != null)
                    viewModel.Clinic.set(viewModel.mVisitedDoctorDetails.value!!.hospital?.clinic?.name?.plus(",").plus(viewModel.mVisitedDoctorDetails.value!!.hospital?.clinic?.address!!))
                if (VisitedDoctor!!.hospital.doctor_profile != null && VisitedDoctor!!.hospital.doctor_profile.profile_pic != "") {
                    ViewUtils.setImageViewGlide(
                        this@VisitedDoctorsDetailActivity,
                        mDataBinding.imageView12,
                        BuildConfig.BASE_IMAGE_URL + viewModel.mVisitedDoctorDetails.value!!.hospital.doctor_profile?.profile_pic
                    )

                }
            }

        }
    }

    private fun observeResponse() {
        viewModel.getErrorObservable().observe(this, Observer<String> { message ->
            hideLoading()
            ViewUtils.showToast(this@VisitedDoctorsDetailActivity, message, false)
        })

        viewModel.mCancelResponse.observe(this, Observer<Response> {
            hideLoading()
            ViewUtils.showToast(this@VisitedDoctorsDetailActivity, it.message, true)
            val intent = Intent()
            setResult(RESULT_OK, intent)
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
        mDataBinding.imageView13.setImageResource(R.drawable.ic_like_green)
        mDataBinding.imageView14.setImageResource(R.drawable.ic_gray_unlike)
    }

    override fun onunlike() {
        experiences = "DISLIKE"
        mDataBinding.imageView13.setImageResource(R.drawable.ic_gray_like)
        mDataBinding.imageView14.setImageResource(R.drawable.ic_like_red)
    }

    override fun onSubmit() {
        if (mDataBinding.divider2.text.toString().equals("")) {
            ViewUtils.showToast(this@VisitedDoctorsDetailActivity, "Please enter " + resources.getString(R.string.consulted_for), false)
            return
        }
        if (mDataBinding.editText7.text.toString().equals("")) {
            ViewUtils.showToast(this@VisitedDoctorsDetailActivity, "Please enter comment", false)
            return
        }
        loadingObservable.value = true
        val hashMap: HashMap<String, Any> = HashMap()
        hashMap[WebApiConstants.Feedback.hospital_id] = viewModel.id.get().toString()
        hashMap[WebApiConstants.Feedback.experiences] = experiences
        hashMap[WebApiConstants.Feedback.visited_for] = viewModel.bookfor.get().toString()
        hashMap[WebApiConstants.Feedback.rating] = mDataBinding.rbRatingBar.rating.toInt().toString()
        hashMap[WebApiConstants.Feedback.title] = mDataBinding.divider2.text.toString()
        hashMap[WebApiConstants.Feedback.comments] = mDataBinding.editText7.text.toString()
        viewModel.postfeedback(hashMap)
    }

}
