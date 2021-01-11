package com.telehealthmanager.app.ui.activity.main.ui.settings

import android.content.Intent
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

    fun performShare(){
        navigator.ratingClick()
    }

    fun performAbout(){
        navigator.aboutClick()
    }

    fun logout(hashMap: HashMap<String, Any>) {
        getCompositeDisposable().add(appRepository.repositoryLogOut(this,hashMap))
    }
}