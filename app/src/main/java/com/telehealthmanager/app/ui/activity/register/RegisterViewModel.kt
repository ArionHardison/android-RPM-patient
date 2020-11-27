package com.telehealthmanager.app.ui.activity.register

import androidx.lifecycle.MutableLiveData
import com.telehealthmanager.app.base.BaseViewModel
import com.telehealthmanager.app.repositary.AppRepository
import com.telehealthmanager.app.repositary.model.RegisterResponse

class RegisterViewModel : BaseViewModel<RegisterNavigator>() {
    private val appRepository = AppRepository.instance()
    var mRegisterResponse = MutableLiveData<RegisterResponse>()
    fun Signup(hashMap: HashMap<String, Any>) {
        getCompositeDisposable().add(appRepository.signUp(this, hashMap))
    }

    fun pickDate() {navigator.pickDate()}
    fun nextclick() {navigator.performValidation()}
}