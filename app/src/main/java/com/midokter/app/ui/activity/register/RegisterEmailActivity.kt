package com.midokter.app.ui.activity.register

import android.content.Intent
import androidx.databinding.ViewDataBinding
import com.midokter.app.R
import com.midokter.app.base.BaseActivity
import com.midokter.app.databinding.ActivityRegisterEmailBinding
import kotlinx.android.synthetic.main.activity_register_email.*
import kotlinx.android.synthetic.main.activity_register_name.*

class RegisterEmailActivity : BaseActivity<ActivityRegisterEmailBinding>() {
    
    override fun getLayoutId(): Int = R.layout.activity_register_email;

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        button5.setOnClickListener {
            val intent = Intent(applicationContext,RegisterGenderActivity::class.java)
            startActivity(intent);
        }    }
}
