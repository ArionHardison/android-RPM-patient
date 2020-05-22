package com.midokter.app.ui.activity.login

import android.content.Intent

import com.midokter.app.utils.ViewUtils

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.midokter.app.BaseApplication
import com.midokter.app.R
import com.midokter.app.base.BaseActivity
import com.midokter.app.data.Constant
import com.midokter.app.data.PreferenceHelper
import com.midokter.app.databinding.ActivityLoginBinding
import com.midokter.app.repositary.WebApiConstants
import com.midokter.app.ui.activity.otp.OTPActivity
import com.midokter.app.ui.activity.register.RegisterNameActivity

import com.midokter.doctor.repositary.model.OtpResponse


class LoginActivity : BaseActivity<ActivityLoginBinding>(),LoginNavigator {
    private lateinit var viewModel: LoginViewModel
    private lateinit var mDataBinding: ActivityLoginBinding
    private val preferenceHelper = PreferenceHelper(BaseApplication.baseApplication)
    private var COUNTRY_CODE :String ="+91"

    override fun getLayoutId(): Int = R.layout.activity_login;

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mDataBinding = mViewDataBinding as ActivityLoginBinding
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        mDataBinding.viewmodel = viewModel
        viewModel.navigator = this
        observeResponse()
        Register_Map.put(WebApiConstants.SignIn.COUNTRY_CODE,mDataBinding.mobile.text.toString())
        mDataBinding.countryCodePicker.selectedCountryCodeWithPlus

    }
    override fun performValidation() {
        hideKeyboard()

        if (mDataBinding.mobile.text.toString().isNullOrBlank()) {
            ViewUtils.showToast(this@LoginActivity, R.string.error_invalid_mobile, false)
        }/*else if (!ValidationUtils.isMinLength(mDataBinding.mobile.text.toString(),6)){
            ViewUtils.showToast(this@LoginActivity, R.string.error_invalid_mobile_, false)
        }*/ else {
            loadingObservable.value = true
            viewModel.SendOTP()

        }
    }

    private fun observeResponse() {
        viewModel.motpResponse.observe(this@LoginActivity, Observer<OtpResponse> {
            loadingObservable.value = false
            if (it.success)
                goToOTP(it)
            else
                ViewUtils.showToast(this@LoginActivity, it.message, false)
        })

        viewModel.getErrorObservable().observe(this, Observer<String> { message ->
            loadingObservable.value = false
            ViewUtils.showToast(this@LoginActivity, message, false)
        })
    }


        //openNewActivity(this@LoginActivity, EmailActivity::class.java, true)

    private fun goToOTP(data: OtpResponse) {
        ViewUtils.showToast(this@LoginActivity, data.message, true)
        val intent = Intent(this@LoginActivity, OTPActivity::class.java)
        intent.putExtra(Constant.IntentData.MOBILE_NUMBER, viewModel.mobile.value)
        intent.putExtra(Constant.IntentData.COUNTRY_CODE, viewModel.countryCode.value)
        intent.putExtra(Constant.IntentData.OTP, data.otp)
        startActivity(intent)
        finish()
        Register_Map.put(WebApiConstants.SignUp.COUNTRY_CODE, mDataBinding.countryCodePicker.selectedCountryCodeWithPlus)
        Register_Map.put(WebApiConstants.SignUp.MOBILE,mDataBinding.mobile.text.toString())

    }
}

