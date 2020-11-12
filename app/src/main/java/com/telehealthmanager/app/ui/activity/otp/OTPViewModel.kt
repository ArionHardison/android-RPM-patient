package com.telehealthmanager.app.ui.activity.otp

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.telehealthmanager.app.BaseApplication
import com.telehealthmanager.app.BuildConfig
import com.telehealthmanager.app.base.BaseViewModel
import com.telehealthmanager.app.data.PreferenceKey
import com.telehealthmanager.app.extensions.default
import com.telehealthmanager.app.repositary.AppRepository
import com.telehealthmanager.app.repositary.WebApiConstants
import com.telehealthmanager.doctor.repositary.model.LoginResponse


class OTPViewModel : BaseViewModel<OTPNavigator>() {

    var loginResponse = MutableLiveData<LoginResponse>()
    //val accessTokenResponse = MutableLiveData<AccessTokenResponse>()
    var otp = MutableLiveData<String>().default("")
    var mobile = ObservableField<String?>("")
    var countryCode = ObservableField<String>("+91")
    var isloginn = MutableLiveData<String>().default("")
    var islogin = MutableLiveData<Boolean>().default(false)
    private val appRepository = AppRepository.instance()


    fun resendOtp() {
        navigator.goNext()
    }

    fun LoginMailClick(){
        navigator.goToEmail()
    }
    fun verifyOtp() {
        navigator.goNext()
    }

   fun otpLogin() {
        val hashMap : HashMap<String,Any> = HashMap()
        hashMap[WebApiConstants.SignIn.OTP] = otp.value!!.trim()
        hashMap[WebApiConstants.SignIn.MOBILE] =  countryCode.get()!!.trim().plus(mobile.get()!!.trim())
        //getCompositeDisposable().add(appRepository.otpSignIn(this,hashMap))
    }
    fun verfiyOtp() {
        val hashMap : HashMap<String,Any> = HashMap()
        hashMap[WebApiConstants.SignIn.OTP] = otp.value!!.trim()
        hashMap[WebApiConstants.SignIn.MOBILE] =  countryCode.get()!!.trim().plus(mobile.get()!!.trim())
        getCompositeDisposable().add(appRepository.verfiyotp(this,hashMap))
    }
    fun signIn() {

        val hashMap : HashMap<String,Any> = HashMap()
        //hashMap[WebApiConstants.SignIn.EMAIL] ="patient@demo.com"
        //hashMap[WebApiConstants.SignIn.PASSWORD] = "123456"
        hashMap[WebApiConstants.SignIn.MOBILE] =countryCode.get()!!.toString()+ mobile.get()!!.trim()
        //hashMap[WebApiConstants.SignIn.COUNTRY_CODE] = countryCode.value!!.removePrefix("+")
        hashMap[WebApiConstants.SignIn.OTP] =otp.value!!
        hashMap[WebApiConstants.SignIn.GRANT_TYPE] = WebApiConstants.SignIn.OTP
        hashMap[WebApiConstants.SocialLogin.DEVICE_TYPE] = BuildConfig.DEVICE_TYPE
        hashMap[WebApiConstants.SocialLogin.DEVICE_TOKEN] = BaseApplication.getCustomPreference!!.getString(PreferenceKey.DEVICE_TOKEN, "111") as String
        hashMap[WebApiConstants.SocialLogin.DEVICE_ID] = BaseApplication.getCustomPreference!!.getString(PreferenceKey.DEVICE_ID, "111") as String
        hashMap[WebApiConstants.SignIn.CLIENT_ID] = BuildConfig.CLIENT_ID
        hashMap[WebApiConstants.SignIn.CLIENT_SECRET] = BuildConfig.CLIENT_SECRET
        getCompositeDisposable().add(appRepository.postSignIn(this, hashMap))




    }
}