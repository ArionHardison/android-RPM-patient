package com.midokter.app.ui.activity.otp

import androidx.lifecycle.MutableLiveData
import com.midokter.app.base.BaseViewModel
import com.midokter.app.extensions.default
import com.midokter.app.repositary.AppRepository
import com.midokter.app.repositary.WebApiConstants
import com.midokter.doctor.repositary.model.LoginResponse


class OTPViewModel : BaseViewModel<OTPNavigator>() {

    var loginResponse = MutableLiveData<LoginResponse>()
    //val accessTokenResponse = MutableLiveData<AccessTokenResponse>()
    var otp = MutableLiveData<String>().default("")
    var mobile = MutableLiveData<String?>().default("")
    var countryCode = MutableLiveData<String>().default("91")
    var islogin = MutableLiveData<Boolean>().default(false)
    private val appRepository = AppRepository.instance()


    fun resendOtp() {
        navigator.goNext()
    }

    fun LoginClick(){
        navigator.goToEmail()
    }
    fun verifyOtp() {
        navigator.goToEmail()
    }

   fun otpLogin() {
        val hashMap : HashMap<String,Any> = HashMap()
        hashMap[WebApiConstants.SignIn.OTP] = otp.value!!.trim()
        hashMap[WebApiConstants.SignIn.MOBILE] =  countryCode.value!!.trim().plus(mobile.value!!.trim())
        getCompositeDisposable().add(appRepository.otpSignIn(this,hashMap))
    }

   /* fun signIn() {
        loadingProgress.value = true
        val hashMap : HashMap<String,Any> = HashMap()
        hashMap[WebApiConstants.SignIn.LOGIN_TYPE] = WebApiConstants.SignIn.LOGIN
        hashMap[WebApiConstants.SignIn.MOBILE] = mobile.value!!.trim()
        hashMap[WebApiConstants.SignIn.COUNTRY_CODE] = countryCode.value!!.removePrefix("+")
        getCompositeDisposable().add(appRepository.numberSignIn(this,hashMap))
    }*/
}