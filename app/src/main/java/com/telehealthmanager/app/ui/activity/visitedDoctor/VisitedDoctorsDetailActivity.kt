package com.telehealthmanager.app.ui.activity.visitedDoctor

import android.app.Activity
import android.content.Intent
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
import com.telehealthmanager.app.databinding.ActivityVisitedDoctorsDetailBinding
import com.telehealthmanager.app.repositary.WebApiConstants
import com.telehealthmanager.app.repositary.model.Appointment
import com.telehealthmanager.app.repositary.model.FeedbackResponse
import com.telehealthmanager.app.repositary.model.Response
import com.telehealthmanager.app.ui.activity.thankyou.ThankyouActivity
import com.telehealthmanager.app.ui.twilio.TwilloVideoActivity
import com.telehealthmanager.app.ui.twilio.model.CallRequest
import com.telehealthmanager.app.utils.CustomBackClick
import com.telehealthmanager.app.utils.ViewUtils
import java.io.Serializable
import java.util.*


class VisitedDoctorsDetailActivity : BaseActivity<ActivityVisitedDoctorsDetailBinding>(),
    VisitedDoctorsNavigator, CustomBackClick {

    private lateinit var viewModel: VisitedDoctorsViewModel
    private lateinit var mDataBinding: ActivityVisitedDoctorsDetailBinding
    private var experiences: String = "UNLIKE"
    private val preferenceHelper = PreferenceHelper(BaseApplication.baseApplication)
    private val FEED_BACK_SUCCES = 102

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
            hashMap[WebApiConstants.IntentPass.ID] = viewModel.mAppointmentDetails.value!!.id.toString()
            viewModel.cancelClick(hashMap)
        }
    }

    override fun clickBackPress() {
        finish()
    }

    private fun initIntentData() {
        mDataBinding.isRated = false
        mDataBinding.isVideo = false
        mDataBinding.isCancel = false
        mDataBinding.isStatus = false
        if (intent.getBooleanExtra(WebApiConstants.IntentPass.iscancel, false)) {
            mDataBinding.isCancel = true
            mDataBinding.isVideo = true
            val details = intent.getSerializableExtra(WebApiConstants.IntentPass.Appointment) as? Appointment
            initData(details!!)
        } else {
            if (intent.getSerializableExtra(WebApiConstants.IntentPass.Appointment) != null) {
                mDataBinding.isStatus = true
                val mAppointment = intent.getSerializableExtra(WebApiConstants.IntentPass.Appointment) as? Appointment
                if (mAppointment!!.patient_rating == 0) {
                    mDataBinding.isRated = true
                }
                initData(mAppointment)
            } else if (intent.getSerializableExtra(WebApiConstants.IntentPass.VisitedDoctor) != null) {
                val mAppointment = intent.getSerializableExtra(WebApiConstants.IntentPass.VisitedDoctor) as? Appointment
                initData(mAppointment!!)
            }
        }
    }

    private fun initData(mAppointment: Appointment) {
        viewModel.mAppointmentDetails.value = mAppointment
        viewModel.id.set(viewModel.mAppointmentDetails.value!!.id)
        if (mAppointment.hospital != null) {
            viewModel.mDoctorID.set(mAppointment.hospital?.id.toString())
            viewModel.name.set(mAppointment.hospital?.first_name?.plus(" ").plus(viewModel.mAppointmentDetails.value!!.hospital?.last_name))
            viewModel.bookfor.set(mAppointment.booking_for ?: "")
            viewModel.scheduledAt.set(ViewUtils.getDayAndTimeFormat(mAppointment.scheduled_at ?: ""))
            viewModel.specialit.set(mAppointment.hospital?.doctor_profile?.speciality?.name ?: "")
            viewModel.catagiery.set(mAppointment.hospital?.doctor_profile?.speciality?.name ?: "")

            if (viewModel.mAppointmentDetails.value!!.hospital?.clinic != null)
                viewModel.mClinic.set(viewModel.mAppointmentDetails.value!!.hospital?.clinic?.name.plus(",").plus(viewModel.mAppointmentDetails.value!!.hospital?.clinic?.address))
            if (mAppointment.hospital?.doctor_profile != null && mAppointment.hospital.doctor_profile!!.profile_pic != "") {
                ViewUtils.setDocViewGlide(
                    this@VisitedDoctorsDetailActivity,
                    mDataBinding.imageView12,
                    BuildConfig.BASE_IMAGE_URL + viewModel.mAppointmentDetails.value!!.hospital!!.doctor_profile?.profile_pic
                )
            }

            when {
                mAppointment.status.equals("CANCELLED", true) -> {
                    viewModel.status.set(mAppointment.status ?: "")
                }
                mAppointment.status.equals("CHECKEDOUT", true) -> {
                    viewModel.status.set("CONSULTED")
                }
                else -> {
                    viewModel.status.set(mAppointment.status ?: "")
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
            val intent = Intent(this@VisitedDoctorsDetailActivity, ThankyouActivity::class.java)
            startActivityForResult(intent, FEED_BACK_SUCCES);
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
        if (mDataBinding.divider2.text.toString() == "") {
            ViewUtils.showToast(this@VisitedDoctorsDetailActivity, "Please enter " + resources.getString(R.string.consulted_for), false)
            return
        }
        if (mDataBinding.editText7.text.toString() == "") {
            ViewUtils.showToast(this@VisitedDoctorsDetailActivity, "Please enter comment", false)
            return
        }
        loadingObservable.value = true
        val hashMap: HashMap<String, Any> = HashMap()
        hashMap[WebApiConstants.Feedback.appointment_id] = viewModel.id.get().toString()
        hashMap[WebApiConstants.Feedback.hospital_id] = viewModel.mDoctorID.get().toString()
        hashMap[WebApiConstants.Feedback.experiences] = experiences
        hashMap[WebApiConstants.Feedback.visited_for] = viewModel.bookfor.get().toString()
        hashMap[WebApiConstants.Feedback.rating] = mDataBinding.rbRatingBar.rating.toInt().toString()
        hashMap[WebApiConstants.Feedback.title] = mDataBinding.divider2.text.toString()
        hashMap[WebApiConstants.Feedback.comments] = mDataBinding.editText7.text.toString()
        viewModel.postFeedback(hashMap)
    }

    override fun videoCallClick() {
        val chatPath = viewModel.mDoctorID.get().toString() + "_video_" + viewModel.id.get().toString()
        val callIntent = Intent(applicationContext, TwilloVideoActivity::class.java)
        val callRequest = CallRequest(
            "" + viewModel.mDoctorID.get().toString(),
            "" + preferenceHelper.getValue(PreferenceKey.PATIENT_ID, 1),
            "" + chatPath,
            "1"
        )
        callIntent.putExtra(TwilloVideoActivity.CALL_REQUEST, callRequest)
        startActivity(callIntent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FEED_BACK_SUCCES) {
            if (resultCode != Activity.RESULT_CANCELED) {
                val intent = Intent()
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }

}
