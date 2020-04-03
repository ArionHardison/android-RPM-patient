package com.midokter.app.ui.activity.findDoctors

import android.content.Intent
import androidx.databinding.ViewDataBinding
import com.midokter.app.R
import com.midokter.app.base.BaseActivity
import com.midokter.app.databinding.ActivityFindDoctorBookingBinding
import com.midokter.app.ui.activity.patientDetail.PatientDetailsActivity
import kotlinx.android.synthetic.main.activity_find_doctor_booking.*

class FindDoctorBookingActivity : BaseActivity<ActivityFindDoctorBookingBinding>() {
    override fun getLayoutId(): Int = R.layout.activity_find_doctor_booking

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        button17.setOnClickListener {
            val intent = Intent(applicationContext, PatientDetailsActivity::class.java)
            startActivity(intent);
        }
    }
}
