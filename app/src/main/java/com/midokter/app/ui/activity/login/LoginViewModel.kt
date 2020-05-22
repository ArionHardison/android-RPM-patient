package com.midokter.app.ui.activity.login

import androidx.lifecycle.MutableLiveData
import com.midokter.app.base.BaseViewModel
import com.midokter.app.extensions.default
import com.midokter.app.repositary.AppRepository
import com.midokter.app.repositary.WebApiConstants
import com.midokter.doctor.repositary.model.OtpResponse

class LoginViewModel : BaseViewModel<LoginNavigator>() {
    private val appRepository = AppRepository.instance()
    var motpResponse = MutableLiveData<OtpResponse>()


    var mobile = MutableLiveData<String>().default("")
    var countryCode= MutableLiveData<String>().default("91")

    fun LoginClick(){

    }

    fun ContinueClick(){
        navigator.performValidation()
    }
    fun SendOTP(){
        val hashMap: HashMap<String, Any> = HashMap()
        hashMap[WebApiConstants.SignIn.MOBILE] = countryCode.value!!.trim().plus(mobile.value!!.trim())
        getCompositeDisposable().add(appRepository.Sendotp(this, hashMap))
    }

}