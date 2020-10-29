package com.telehealthmanager.app.ui.activity.login

import android.content.Intent

import com.telehealthmanager.app.utils.ViewUtils

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.telehealthmanager.app.BaseApplication
import com.telehealthmanager.app.R
import com.telehealthmanager.app.base.BaseActivity
import com.telehealthmanager.app.data.Constant
import com.telehealthmanager.app.data.PreferenceHelper
import com.telehealthmanager.app.data.PreferenceKey
import com.telehealthmanager.app.data.setValue
import com.telehealthmanager.app.databinding.ActivityLoginBinding
import com.telehealthmanager.app.repositary.WebApiConstants
import com.telehealthmanager.app.ui.activity.otp.OTPActivity

import com.telehealthmanager.doctor.repositary.model.OtpResponse


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
                goToOTP(it)

        })

        viewModel.getErrorObservable().observe(this, Observer<String> { message ->
            loadingObservable.value = false
            ViewUtils.showToast(this@LoginActivity, message, false)
        })
    }


        //openNewActivity(this@LoginActivity, EmailActivity::class.java, true)

    private fun goToOTP(data: OtpResponse) {
        preferenceHelper.setValue(PreferenceKey.PHONE, mDataBinding.mobile.text.toString())
        preferenceHelper.setValue(PreferenceKey.COUNTRY_CODE, viewModel.countryCode.value)

        val intent = Intent(this@LoginActivity, OTPActivity::class.java)
        if(data.success) {
            intent.putExtra(Constant.IntentData.ISLOGIN, true)
            ViewUtils.showToast(this@LoginActivity, data.message, true)
        }else {
            intent.putExtra(Constant.IntentData.ISLOGIN, false)
            ViewUtils.showToast(this@LoginActivity, data.message, false)
        }

        intent.putExtra(Constant.IntentData.MOBILE_NUMBER, viewModel.mobile.value)
        intent.putExtra(Constant.IntentData.COUNTRY_CODE, viewModel.countryCode.value)


        intent.putExtra(Constant.IntentData.OTP, data.otp.toString())
        startActivity(intent)



    }
}

