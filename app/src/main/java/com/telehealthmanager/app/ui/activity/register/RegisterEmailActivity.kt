package com.telehealthmanager.app.ui.activity.register

import android.content.Intent
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.telehealthmanager.app.BaseApplication
import com.telehealthmanager.app.R
import com.telehealthmanager.app.base.BaseActivity
import com.telehealthmanager.app.data.PreferenceHelper
import com.telehealthmanager.app.data.PreferenceKey
import com.telehealthmanager.app.data.setValue
import com.telehealthmanager.app.databinding.ActivityRegisterEmailBinding
import com.telehealthmanager.app.utils.ValidationUtils
import com.telehealthmanager.app.utils.ViewUtils
import kotlinx.android.synthetic.main.activity_register_email.*

class RegisterEmailActivity : BaseActivity<ActivityRegisterEmailBinding>() {
    private lateinit var viewModel: RegisterViewModel
    private lateinit var mDataBinding: ActivityRegisterEmailBinding
    private val preferenceHelper = PreferenceHelper(BaseApplication.baseApplication)


    override fun getLayoutId(): Int = R.layout.activity_register_email;

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mDataBinding = mViewDataBinding as ActivityRegisterEmailBinding
        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
        mDataBinding.viewmodel = viewModel


        mDataBinding.next.setOnClickListener {
            if (mViewDataBinding.email.text.toString().isNullOrBlank()) {
                ViewUtils.showToast(this@RegisterEmailActivity, R.string.error_invalid_email_address, false)
            }else if(!ValidationUtils.isValidEmail(email.text.toString())) {
                ViewUtils.showToast(this@RegisterEmailActivity, R.string.error_invalid_email_address, false)
            } else {

                preferenceHelper.setValue(PreferenceKey.EMAIL,mDataBinding.email.text.toString())
                val intent = Intent(applicationContext, RegisterGenderActivity::class.java)
                startActivity(intent);

            }
        }
        mDataBinding.backarrow.setOnClickListener {
            finish()
        }
    }
}
