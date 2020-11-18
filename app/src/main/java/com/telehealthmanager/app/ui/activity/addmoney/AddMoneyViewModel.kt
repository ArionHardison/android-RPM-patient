package com.telehealthmanager.app.ui.activity.addmoney

import androidx.lifecycle.MutableLiveData
import com.telehealthmanager.app.base.BaseViewModel
import com.telehealthmanager.app.repositary.AppRepository

class AddMoneyViewModel : BaseViewModel<AddMoneyNavigator>(){
    private val appRepository = AppRepository.instance()
    var loadingProgress = MutableLiveData<Boolean>()
}