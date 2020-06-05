package com.midokter.app.ui.activity.visitedDoctor

import android.content.Intent
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.midokter.app.R
import com.midokter.app.base.BaseActivity
import com.midokter.app.databinding.ActivityVisitedDoctorsBinding
import com.midokter.app.databinding.ActivityVisitedDoctorsDetailBinding
import com.midokter.app.repositary.WebApiConstants
import com.midokter.app.repositary.model.AppointmentResponse
import com.midokter.app.ui.activity.thankyou.ThankyouActivity
import kotlinx.android.synthetic.main.activity_visited_doctors_detail.*

class VisitedDoctorsDetailActivity : BaseActivity<ActivityVisitedDoctorsDetailBinding>(),VisitedDoctorsNavigator {

    private lateinit var viewModel: VisitedDoctorsViewModel
    private lateinit var mDataBinding: ActivityVisitedDoctorsDetailBinding

    override fun getLayoutId(): Int = R.layout.activity_visited_doctors_detail

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mDataBinding = mViewDataBinding as ActivityVisitedDoctorsDetailBinding
        viewModel = ViewModelProviders.of(this).get(VisitedDoctorsViewModel::class.java)
        mDataBinding.viewmodel = viewModel
        viewModel.navigator = this
        initIntentData()
        mDataBinding. button14.setOnClickListener {
            val intent = Intent(applicationContext, ThankyouActivity::class.java)
            startActivity(intent);
        }
        mDataBinding.toolbar4.setNavigationOnClickListener {
            finish()
        }
    }
    private fun initIntentData() {
        if (intent.getBooleanExtra(WebApiConstants.IntentPass.iscancel,false)){

            val details = intent.getSerializableExtra(WebApiConstants.IntentPass.Appointment) as? AppointmentResponse.Upcomming.Appointment
            viewModel.mupcomingDoctorDetails.value = details
            mDataBinding.iscancel = true

        }else {
            mDataBinding.iscancel = false
            val details = intent.getSerializableExtra(WebApiConstants.IntentPass.Appointment) as? AppointmentResponse.Previous.Appointment
            viewModel.mPastDoctorDetails.value = details
        }
    }
}
