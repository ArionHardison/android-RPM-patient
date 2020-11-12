package com.telehealthmanager.app.ui.activity.main.ui.settings

import androidx.lifecycle.MutableLiveData
import com.telehealthmanager.app.base.BaseViewModel
import com.telehealthmanager.app.repositary.AppRepository
import com.telehealthmanager.app.repositary.model.Response

class SettingViewModel : BaseViewModel<SettingNavigator>(){
    private val appRepository = AppRepository.instance()
    var mLogoutResponse = MutableLiveData<Response>()

    fun performLogout(){
        navigator.logoutClick()
    }

    fun logout(hashMap: HashMap<String, Any>) {
        getCompositeDisposable().add(appRepository.logout(this,hashMap))
    }
}