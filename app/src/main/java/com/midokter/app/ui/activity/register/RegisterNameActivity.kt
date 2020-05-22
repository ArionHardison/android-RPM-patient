package com.midokter.app.ui.activity.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.midokter.app.R
import com.midokter.app.base.BaseActivity
import com.midokter.app.databinding.ActivityLoginBinding
import com.midokter.app.databinding.ActivityRegisterNameBinding
import com.midokter.app.repositary.WebApiConstants
import com.midokter.app.ui.activity.login.LoginViewModel
import com.midokter.app.utils.ViewUtils
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register_name.*

class RegisterNameActivity : BaseActivity<ActivityRegisterNameBinding>() {
    private lateinit var viewModel: RegisterViewModel
    private lateinit var mDataBinding: ActivityRegisterNameBinding
    override fun getLayoutId(): Int = R.layout.activity_register_name

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mDataBinding = mViewDataBinding as ActivityRegisterNameBinding
        viewModel = ViewModelProviders.of(this).get(RegisterViewModel::class.java)
        mDataBinding.viewmodel = viewModel

        mDataBinding.next.setOnClickListener {

            if (mViewDataBinding.name.text.toString().isNullOrBlank()) {
                ViewUtils.showToast(this@RegisterNameActivity, R.string.error_invalid_name, false)
            } else {
                Register_Map.put(WebApiConstants.SignUp.FIRST_NAME, mDataBinding.name.text.toString())
                Register_Map.put(WebApiConstants.SignUp.LAST_NAME,".")
                val intent = Intent(applicationContext,RegisterEmailActivity::class.java)
                startActivity(intent);

            }

        }
        mDataBinding.backarrow.setOnClickListener {
            finish()
        }
    }
}
