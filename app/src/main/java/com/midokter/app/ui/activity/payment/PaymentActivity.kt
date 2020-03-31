package com.midokter.app.ui.activity.payment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.midokter.app.R
import com.midokter.app.base.BaseActivity
import com.midokter.app.databinding.ActivityPaymentBinding
import com.midokter.app.ui.activity.success.SuccessActivity
import kotlinx.android.synthetic.main.activity_payment.*

class PaymentActivity : BaseActivity<ActivityPaymentBinding>() {

    override fun getLayoutId(): Int = R.layout.activity_payment

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        button20.setOnClickListener {
            val intent = Intent(applicationContext, SuccessActivity::class.java)
            startActivity(intent);
        }
    }
}
