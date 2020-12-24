package com.telehealthmanager.app.ui.activity.changepassword

import android.text.TextUtils
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.telehealthmanager.app.BaseApplication
import com.telehealthmanager.app.R
import com.telehealthmanager.app.base.BaseActivity
import com.telehealthmanager.app.data.PreferenceHelper
import com.telehealthmanager.app.databinding.ActivityChangepasswordBinding
import com.telehealthmanager.app.utils.ValidationUtils
import com.telehealthmanager.app.utils.ViewUtils
import java.util.*

class ChangePasswordActivity : BaseActivity<ActivityChangepasswordBinding>(), ChangePasswordNavigator {

    private lateinit var viewModel: ChangePasswordViewModel
    private lateinit var mDataBinding: ActivityChangepasswordBinding
    val preferenceHelper = PreferenceHelper(BaseApplication.baseApplication)

    override fun getLayoutId(): Int = R.layout.activity_changepassword

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mDataBinding = mViewDataBinding as ActivityChangepasswordBinding
        viewModel = ViewModelProvider(this).get(ChangePasswordViewModel::class.java)
        mDataBinding.viewmodel = viewModel
        viewModel.navigator = this

        observeResponse()
        mDataBinding.backImage.setOnClickListener {
            finish()
        }
    }

    private fun observeResponse() {
        viewModel.mResponse.observe(this, Observer<Objects> {
            loadingObservable.value = false
            /*if (it.status) {
                ViewUtils.showToast(this@ChangePasswordActivity, it.message, true)
                finish()
            } else
                ViewUtils.showToast(this@ChangePasswordActivity, it.message, false)*/
        })

        viewModel.getErrorObservable().observe(this, Observer<String> { message ->
            loadingObservable.value = false
            ViewUtils.showToast(this@ChangePasswordActivity, message, false)
        })
    }

    override fun onSave() {
        if (TextUtils.isEmpty(viewModel.currentPassword.value!!.trim())) {
            ViewUtils.showToast(this@ChangePasswordActivity, R.string.error_invalid_password, false)
        } else if (ValidationUtils.isMinLength(viewModel.currentPassword.value!!, 5)) {
            ViewUtils.showToast(this@ChangePasswordActivity, R.string.error_invalid_password, false)
        } else if (TextUtils.isEmpty(viewModel.newPassword.value!!.trim())) {
            ViewUtils.showToast(this@ChangePasswordActivity, R.string.error_invalid_password, false)
        } else if (ValidationUtils.isMinLength(viewModel.newPassword.value!!, 5)) {
            ViewUtils.showToast(this@ChangePasswordActivity, R.string.error_invalid_password, false)
        } else if (TextUtils.isEmpty(viewModel.rePassword.value!!.trim())) {
            ViewUtils.showToast(this@ChangePasswordActivity, R.string.error_invalid_password, false)
        } else if (ValidationUtils.isMinLength(viewModel.rePassword.value!!, 5)) {
            ViewUtils.showToast(this@ChangePasswordActivity, R.string.error_invalid_password, false)
        } else {
            loadingObservable.value = true
            viewModel.changePassword()
        }
    }

}
