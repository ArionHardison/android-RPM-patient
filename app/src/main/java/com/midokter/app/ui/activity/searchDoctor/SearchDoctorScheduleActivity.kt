package com.midokter.app.ui.activity.searchDoctor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.midokter.app.R
import com.midokter.app.base.BaseActivity
import com.midokter.app.databinding.ActivitySearchDoctorScheduleBinding
import com.midokter.app.ui.activity.findDoctors.FindDoctorsListActivity
import com.midokter.app.ui.activity.patientDetail.PatientDetailsActivity
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
