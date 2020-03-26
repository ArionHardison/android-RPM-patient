package com.midokter.app.ui.activity.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.midokter.app.R
import com.midokter.app.base.BaseActivity
import com.midokter.app.databinding.ActivityRegisterNameBinding
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register_name.*

class RegisterNameActivity : BaseActivity<ActivityRegisterNameBinding>() {

    override fun getLayoutId(): Int = R.layout.activity_register_name

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        button4.setOnClickListener {
            val intent = Intent(applicationContext,RegisterEmailActivity::class.java)
            startActivity(intent);
        }
    }
}
