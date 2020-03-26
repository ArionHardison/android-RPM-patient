package com.midokter.app.ui.activity.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.midokter.app.R
import com.midokter.app.base.BaseActivity
import com.midokter.app.databinding.ActivityRegisterFinalBinding
import com.midokter.app.ui.activity.main.MainActivity
import kotlinx.android.synthetic.main.activity_register_final.*
import kotlinx.android.synthetic.main.activity_register_gender.*

class RegisterFinalActivity : BaseActivity<ActivityRegisterFinalBinding>() {

    override fun getLayoutId(): Int = R.layout.activity_register_final

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        button9.setOnClickListener {
            val intent = Intent(applicationContext,MainActivity::class.java)
            startActivity(intent);
        }      }

}
