package com.midokter.app.ui.activity.login

import android.content.Intent
import android.widget.Button
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.ViewDataBinding
import com.midokter.app.R
import com.midokter.app.base.BaseActivity
import com.midokter.app.databinding.ActivityLoginBinding
import com.midokter.app.ui.activity.main.MainActivity
import com.midokter.app.ui.activity.register.RegisterNameActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    override fun getLayoutId(): Int = R.layout.activity_login;

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        button.setOnClickListener {
            val intent = Intent(applicationContext,RegisterNameActivity::class.java)
            startActivity(intent);
        }
    }
}

