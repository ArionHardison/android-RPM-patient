package com.midokter.app.ui.activity.register

import android.content.Intent
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.midokter.app.BaseApplication
import com.midokter.app.R
import com.midokter.app.base.BaseActivity
import com.midokter.app.data.PreferenceHelper
import com.midokter.app.data.PreferenceKey
import com.midokter.app.data.setValue
import com.midokter.app.databinding.ActivityRegisterEmailBinding
import com.midokter.app.repositary.WebApiConstants
import com.midokter.app.utils.ViewUtils
import kotlinx.android.synthetic.main.activity_register_email.*
import kotlinx.android.synthetic.main.activity_register_name.*

class RegisterEmailActivity : BaseActivity<ActivityRegisterEmailBinding>() {
    private lateinit var viewModel: RegisterViewModel
    private lateinit var mDataBinding: ActivityRegisterEmailBinding
    private val preferenceHelper = PreferenceHelper(BaseApplication.baseApplication)


    override fun getLayoutId(): Int = R.layout.activity_register_email;

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mDataBinding = mViewDataBinding as ActivityRegisterEmailBinding
        viewModel = ViewModelProviders.of(this).get(RegisterViewModel::class.java)
        mDataBinding.viewmodel = viewModel


        mDataBinding.next.setOnClickListener {
            if (mViewDataBinding.email.text.toString().isNullOrBlank()) {
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
