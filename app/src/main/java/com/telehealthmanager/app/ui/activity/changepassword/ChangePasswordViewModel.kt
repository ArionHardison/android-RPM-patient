package com.telehealthmanager.app.ui.activity.changepassword

import androidx.lifecycle.MutableLiveData
import com.telehealthmanager.app.base.BaseViewModel
import com.telehealthmanager.app.extensions.default
import com.telehealthmanager.app.repositary.AppRepository
import com.telehealthmanager.app.repositary.WebApiConstants
import java.util.*
import kotlin.collections.HashMap

class ChangePasswordViewModel : BaseViewModel<ChangePasswordNavigator>() {

    private val appRepository = AppRepository.instance()
    var mResponse = MutableLiveData<Objects>()
    var currentPassword = MutableLiveData<String>().default("")
    var newPassword= MutableLiveData<String>().default("")
    var rePassword= MutableLiveData<String>().default("")

    fun saveClick(){
        navigator.onSave()
    }

    fun changePassword(){
        val hashMap: HashMap<String, Any> = HashMap()
        hashMap[WebApiConstants.ChangePassword.CURRENTPASSWORD] = currentPassword.value!!.trim()
        hashMap[WebApiConstants.ChangePassword.PASSWORD] = newPassword.value!!.trim()
        hashMap[WebApiConstants.ChangePassword.PASSWORDCONFIRMATION] = rePassword.value!!.trim()
        getCompositeDisposable().add(appRepository.repositoryChangePassword(this, hashMap))
    }
}