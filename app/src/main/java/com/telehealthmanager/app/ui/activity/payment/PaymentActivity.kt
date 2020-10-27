package com.telehealthmanager.app.ui.activity.payment

import android.content.Intent
import androidx.databinding.ViewDataBinding
import com.telehealthmanager.app.R
import com.telehealthmanager.app.base.BaseActivity
import com.telehealthmanager.app.databinding.ActivityPaymentBinding
import com.telehealthmanager.app.ui.activity.success.SuccessActivity
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
