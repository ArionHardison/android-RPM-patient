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

import com.midokter.app.databinding.ActivityRegisterNameBinding
import com.midokter.app.repositary.WebApiConstants

import com.midokter.app.utils.ViewUtils


class RegisterNameActivity : BaseActivity<ActivityRegisterNameBinding>() {
    private lateinit var viewModel: RegisterViewModel
    private lateinit var mDataBinding: ActivityRegisterNameBinding
    private val preferenceHelper = PreferenceHelper(BaseApplication.baseApplication)

    override fun getLayoutId(): Int = R.layout.activity_register_name

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mDataBinding = mViewDataBinding as ActivityRegisterNameBinding
        viewModel = ViewModelProviders.of(this).get(RegisterViewModel::class.java)
        mDataBinding.viewmodel = viewModel

        mDataBinding.next.setOnClickListener {

            if (mViewDataBinding.firstName.text.toString().isNullOrBlank()) {
                ViewUtils.showToast(this@RegisterNameActivity, R.string.error_invalid_first_name, false)
            }else if (mViewDataBinding.lastName.text.toString().isNullOrBlank()) {
                ViewUtils.showToast(this@RegisterNameActivity, R.string.error_invalid__last_name, false)
            } else {
                preferenceHelper.setValue(PreferenceKey.FIRST_NAME, mDataBinding.firstName.text.toString())
                preferenceHelper.setValue(PreferenceKey.FIRST_NAME, mDataBinding.lastName.text.toString())
                val intent = Intent(applicationContext,RegisterEmailActivity::class.java)
                startActivity(intent);

            }

        }
        mDataBinding.backarrow.setOnClickListener {
            finish()
        }
    }
}
