package com.midokter.app.ui.activity.main.ui.wallet

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.midokter.app.base.BaseViewModel
import com.midokter.app.repositary.AppRepository
import com.midokter.app.repositary.WebApiConstants
import com.midokter.app.repositary.model.ProfileResponse
import com.midokter.app.repositary.model.WalletResponse

class WalletViewModel : BaseViewModel<WalletNavigator>() {

    private val appRepository = AppRepository.instance()
    var loadingProgress = MutableLiveData<Boolean>()
    var mProfileResponse = MutableLiveData<ProfileResponse>()
    var mWalletResponse = MutableLiveData<WalletResponse>()
    var balance : ObservableField<String> = ObservableField("$0")
    var enteredMoney : ObservableField<String> = ObservableField("")
    private val _text = MutableLiveData<String>().apply {
        value = "This is tools Fragment"
    }
    val text: LiveData<String> = _text


    fun getprofile() {
        loadingProgress.value=true
        getCompositeDisposable().add(appRepository.getProfile(this))
    }

    fun onAddMoneyClicked(){
        navigator.onAddMoneyClicked()
    }

    fun addMoneyToWallet() {
        val hashMap : HashMap<String,Any> = HashMap()
        loadingProgress.value=true
        hashMap[WebApiConstants.Wallet.AMOUNT] =enteredMoney.get()!!
        getCompositeDisposable().add(appRepository.addMoneyToWallet(this,hashMap))
    }
}