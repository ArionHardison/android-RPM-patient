package com.telehealthmanager.app.ui.activity.main.ui.wallet

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.telehealthmanager.app.base.BaseViewModel
import com.telehealthmanager.app.repositary.AppRepository
import com.telehealthmanager.app.repositary.WebApiConstants
import com.telehealthmanager.app.repositary.model.ProfileResponse
import com.telehealthmanager.app.repositary.model.WalletResponse

class WalletViewModel : BaseViewModel<WalletNavigator>() {

    private val appRepository = AppRepository.instance()
    var loadingProgress = MutableLiveData<Boolean>()
    var mProfileResponse = MutableLiveData<ProfileResponse>()
    var balance : ObservableField<String> = ObservableField("$0")
    var enteredMoney : ObservableField<String> = ObservableField("")

    fun getProfile() {
        loadingProgress.value=true
        getCompositeDisposable().add(appRepository.getProfile(this))
    }

    fun onAddMoneyClicked(){
        navigator.onAddMoneyClicked()
    }

}