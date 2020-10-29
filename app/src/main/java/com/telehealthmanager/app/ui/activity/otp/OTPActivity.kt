package com.telehealthmanager.app.ui.activity.otp


import android.annotation.SuppressLint
import android.util.Log
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
import com.telehealthmanager.app.databinding.ActivityOtpBinding
import com.telehealthmanager.app.ui.activity.main.MainActivity
import com.telehealthmanager.app.ui.activity.register.RegisterNameActivity
import com.telehealthmanager.app.utils.ViewUtils

import com.telehealthmanager.doctor.repositary.model.LoginResponse



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

        mDataBinding.backarrow.setOnClickListener {
            finish()
        }
        getIntentData()


        observeResponse()
        mDataBinding.mobile.setText(viewModel.otp.value.toString())

    }



    @SuppressLint("SetTextI18n")
    private fun getIntentData() {
        //setIntent Data on Text
        if (intent.getStringExtra(Constant.IntentData.MOBILE_NUMBER) != null) {
            //viewModel.countryCode.value = intent.getStringExtra(Constant.IntentData.COUNTRY_CODE)
            viewModel.mobile.value = intent.getStringExtra(Constant.IntentData.MOBILE_NUMBER)
            viewModel.otp.value =intent.getStringExtra(Constant.IntentData.OTP)
            viewModel.islogin.value =intent.getBooleanExtra(Constant.IntentData.ISLOGIN,false)

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

        if (data.getToken().isNullOrBlank()) {
            ViewUtils.showToast(this@OTPActivity, data.geterror(), false)
            //openNewActivity(this@OTPActivity, RegisterNameActivity::class.java, false)
        }else {
            preferenceHelper.setValue(PreferenceKey.ACCESS_TOKEN, data.getToken())
            Log.e("ACCESS_TOKEN",data.getToken())
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
        }else if (mDataBinding.pinEntry.text.toString().equals(viewModel.otp.value.toString())){
            if (viewModel.islogin.value?.equals(true)!!){
                loadingObservable.value = true
                viewModel.signIn()
            }else {
                viewModel.otp.value = mDataBinding.pinEntry.text.toString()
                preferenceHelper.setValue(PreferenceKey.OTP,mDataBinding.pinEntry.text.toString())
                openNewActivity(this@OTPActivity, RegisterNameActivity::class.java, false)
                finish()
            }
        } else {
            ViewUtils.showToast(this@OTPActivity, getString(R.string.invalid_otp), false)
        }
    }
}