package com.midokter.app.ui.activity.findDoctors

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.midokter.app.R
import com.midokter.app.base.BaseActivity
import com.midokter.app.databinding.ActivityFindDoctorPatientDetailsBinding
import com.midokter.app.ui.activity.payment.PaymentActivity
import kotlinx.android.synthetic.main.activity_find_doctor_patient_details.*

class FindDoctorPatientDetailsActivity : BaseActivity<ActivityFindDoctorPatientDetailsBinding>() {

    override fun getLayoutId(): Int = R.layout.activity_find_doctor_patient_details

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        button18.setOnClickListener {
            val intent = Intent(applicationContext, PaymentActivity::class.java)
            startActivity(intent);
        }
    }
}
