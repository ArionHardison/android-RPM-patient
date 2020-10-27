package com.telehealthmanager.app.ui.activity.searchDoctor

import android.content.Intent
import androidx.databinding.ViewDataBinding
import com.telehealthmanager.app.R
import com.telehealthmanager.app.base.BaseActivity
import com.telehealthmanager.app.databinding.ActivitySearchDoctorScheduleBinding
import com.telehealthmanager.app.ui.activity.patientDetail.PatientDetailsActivity
import kotlinx.android.synthetic.main.activity_search_doctor_schedule.*

class SearchDoctorScheduleActivity : BaseActivity<ActivitySearchDoctorScheduleBinding>() {
    override fun getLayoutId(): Int = R.layout.activity_search_doctor_schedule

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        button29.setOnClickListener {
            val intent = Intent(applicationContext, PatientDetailsActivity::class.java)
            startActivity(intent);
        }
    }
}
