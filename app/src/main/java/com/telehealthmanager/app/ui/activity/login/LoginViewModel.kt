package com.telehealthmanager.app.ui.activity.login

import androidx.lifecycle.MutableLiveData
import com.telehealthmanager.app.base.BaseViewModel
import com.telehealthmanager.app.extensions.default
import com.telehealthmanager.app.repositary.AppRepository
import com.telehealthmanager.app.repositary.WebApiConstants
import com.telehealthmanager.doctor.repositary.model.OtpResponse

class LoginViewModel : BaseViewModel<LoginNavigator>() {
    private val appRepository = AppRepository.instance()
    var motpResponse = MutableLiveData<OtpResponse>()
    var mobile = MutableLiveData<String>().default("")
    var countryCode= MutableLiveData<String>().default("+91")

    fun ContinueClick(){
        navigator.performValidation()
    }

    fun SendOTP(){
        val hashMap: HashMap<String, Any> = HashMap()
        hashMap[WebApiConstants.SignIn.MOBILE] = countryCode.value!!.trim().plus(mobile.value!!.trim())
        getCompositeDisposable().add(appRepository.Sendotp(this, hashMap))
    }

}