package com.midokter.app.ui.activity.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.midokter.app.R
import com.midokter.app.base.BaseActivity
import com.midokter.app.databinding.ActivityRegisterGenderBinding
import kotlinx.android.synthetic.main.activity_register_email.*
import kotlinx.android.synthetic.main.activity_register_gender.*

class RegisterGenderActivity : BaseActivity<ActivityRegisterGenderBinding>() {
    override fun getLayoutId(): Int = R.layout.activity_register_gender

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        button6.setOnClickListener {
            val intent = Intent(applicationContext,RegisterFinalActivity::class.java)
            startActivity(intent);
        }
        button7.setOnClickListener {
            val intent = Intent(applicationContext,RegisterFinalActivity::class.java)
            startActivity(intent);
        }
        button8.setOnClickListener {
            val intent = Intent(applicationContext,RegisterFinalActivity::class.java)
            startActivity(intent);
        }
    }
}
