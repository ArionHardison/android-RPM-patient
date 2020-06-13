package com.midokter.app.ui.activity.register

import androidx.lifecycle.MutableLiveData
import com.midokter.app.base.BaseActivity
import com.midokter.app.base.BaseViewModel
import com.midokter.app.repositary.AppRepository
import com.midokter.app.repositary.WebApiConstants
import com.midokter.app.repositary.model.RegisterResponse

class RegisterViewModel : BaseViewModel<RegisterNavigator>() {
    private val appRepository = AppRepository.instance()
    var mRegisterResponse = MutableLiveData<RegisterResponse>()
    fun Signup(hashMap: HashMap<String, Any>) {
        getCompositeDisposable().add(appRepository.signup(this, hashMap))
    }

    fun pickDate() {navigator.pickDate()}
    fun nextclick() {navigator.performValidation()}
}