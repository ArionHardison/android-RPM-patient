package com.midokter.app.ui.activity.otp


import android.annotation.SuppressLint
import android.content.Intent
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.midokter.app.BaseApplication
import com.midokter.app.R
import com.midokter.app.base.BaseActivity
import com.midokter.app.data.Constant
import com.midokter.app.data.PreferenceHelper
import com.midokter.app.data.PreferenceKey
import com.midokter.app.data.setValue
import com.midokter.app.databinding.ActivityOtpBinding
import com.midokter.app.ui.activity.main.MainActivity
import com.midokter.app.ui.activity.register.RegisterNameActivity
import com.midokter.app.utils.ViewUtils

import com.midokter.doctor.repositary.model.LoginResponse



class OTPActivity : BaseActivity<ActivityOtpBinding>(), OTPNavigator {

    private val TAG: String = "PhoneNumberActivity"
    private lateinit var viewModel: OTPViewModel
    private lateinit var mDataBinding: ActivityOtpBinding

    private val preferenceHelper = PreferenceHelper(BaseApplication.baseApplication)

    override fun getLayoutId(): Int = R.layout.activity_otp

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mDataBinding = mViewDataBinding as ActivityOtpBinding
        viewModel = ViewModelProviders.of(this).get(OTPViewModel::class.java)
        mDataBinding.viewmodel = viewModel
        viewModel.navigator = this
        getIntentData()


        observeResponse()


    }



    @SuppressLint("SetTextI18n")
    private fun getIntentData() {
        //setIntent Data on Text
        if (intent.getStringExtra(Constant.IntentData.MOBILE_NUMBER) != null) {
            //viewModel.countryCode.value = intent.getStringExtra(Constant.IntentData.COUNTRY_CODE)
            viewModel.mobile.value = intent.getStringExtra(Constant.IntentData.MOBILE_NUMBER)
            viewModel.otp.value =intent.getStringExtra(Constant.IntentData.OTP)
            //viewModel.islogin.value =intent.getBooleanExtra(Constant.IntentData.ISLOGIN)

        }
    }




    private fun observeResponse() {
        viewModel.loginResponse.observe(this@OTPActivity, Observer<LoginResponse> {
            loadingObservable.value = false
            goToHome(it)
        })

        viewModel.getErrorObservable().observe(this, Observer<String> { message ->
            loadingObservable.value = false
            ViewUtils.showToast(this@OTPActivity, message, false)
        })
    }

    override fun goResendOTP() {

    }
    private fun goToHome(data: LoginResponse) {

        if (data.getToken().isNullOrBlank())
            ViewUtils.showToast(this@OTPActivity, "Login Failed", false)
        else {
            preferenceHelper.setValue(PreferenceKey.ACCESS_TOKEN, data.getToken())
            openNewActivity(this@OTPActivity, MainActivity::class.java, true)
            finishAffinity()
        }
    }


    override fun goToEmail() {
        openNewActivity(this@OTPActivity, RegisterNameActivity::class.java, true)
    }
    override fun goNext() {
        if (mDataBinding.pinEntry.text.isNullOrBlank()) {
            ViewUtils.showToast(this@OTPActivity, getString(R.string.enter_otp), false)
        } else {
            viewModel.otp.value = mDataBinding.pinEntry.text.toString()

            loadingObservable.value = true
            viewModel.otpLogin()
        }
    }
}