package com.midokter.app.ui.activity.findDoctors

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.midokter.app.R
import com.midokter.app.base.BaseActivity
import com.midokter.app.databinding.ActivityFindDoctorBookingBinding
import kotlinx.android.synthetic.main.activity_find_doctor_booking.*

class FindDoctorBookingActivity : BaseActivity<ActivityFindDoctorBookingBinding>() {
    override fun getLayoutId(): Int = R.layout.activity_find_doctor_booking

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        button17.setOnClickListener {
            val intent = Intent(applicationContext, FindDoctorPatientDetailsActivity::class.java)
            startActivity(intent);
        }
    }
}
