package com.midokter.app.ui.activity.patientDetail

import android.content.Intent
import androidx.databinding.ViewDataBinding
import com.midokter.app.R
import com.midokter.app.base.BaseActivity
import com.midokter.app.databinding.ActivityPatientDetailsBinding
import com.midokter.app.ui.activity.payment.PaymentActivity
import kotlinx.android.synthetic.main.activity_patient_details.*

class PatientDetailsActivity : BaseActivity<ActivityPatientDetailsBinding>() {

    override fun getLayoutId(): Int = R.layout.activity_patient_details

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        button18.setOnClickListener {
            val intent = Intent(applicationContext, PaymentActivity::class.java)
            startActivity(intent);
        }
    }
}
