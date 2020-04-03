package com.midokter.app.ui.activity.searchDoctor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.midokter.app.R
import com.midokter.app.base.BaseActivity
import com.midokter.app.databinding.ActivitySearchDoctorDetailBinding
import com.midokter.app.ui.activity.patientDetail.PatientDetailsActivity
import kotlinx.android.synthetic.main.activity_search_doctor_detail.*

class SearchDoctorDetailActivity : BaseActivity<ActivitySearchDoctorDetailBinding>() {
    override fun getLayoutId(): Int = R.layout.activity_search_doctor_detail

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        button28.setOnClickListener {
            val intent = Intent(applicationContext, SearchDoctorScheduleActivity::class.java)
            startActivity(intent);
        }
    }
}
