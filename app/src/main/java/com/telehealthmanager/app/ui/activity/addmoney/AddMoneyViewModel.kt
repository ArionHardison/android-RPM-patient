package com.telehealthmanager.app.ui.activity.addmoney

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.telehealthmanager.app.base.BaseViewModel
import com.telehealthmanager.app.repositary.AppRepository
import com.telehealthmanager.app.repositary.model.CardList
import com.telehealthmanager.app.repositary.model.CardSuccessMessage
import com.telehealthmanager.app.repositary.model.WalletAddSuccess
import com.telehealthmanager.app.repositary.model.WalletResponse
import java.util.*
import kotlin.collections.HashMap

class AddMoneyViewModel : BaseViewModel<AddMoneyNavigator>() {
    private val appRepository = AppRepository.instance()
    var loadingProgress = MutableLiveData<Boolean>()
    var mCardListResponse = MutableLiveData<List<CardList>>()
    var mAddCardSuccess = MutableLiveData<CardSuccessMessage>()
    var mDeletedSuccess = MutableLiveData<CardSuccessMessage>()
    var mAddMoneySuccess = MutableLiveData<WalletAddSuccess>()
    var mWalletAmount: ObservableField<String> = ObservableField("")
    var mSelectedCard: ObservableField<String> = ObservableField("")
    var mDeletedCard: ObservableField<String> = ObservableField("")
    var mEnablePay: ObservableField<Boolean> = ObservableField(false)

    fun clickAddCardBtn() {
        navigator.openAddCard()
    }

    fun clickAddMoney() {
        navigator.addMoney()
    }

    fun getCards() {
        getCompositeDisposable().add(appRepository.getCardList(this))
    }

    fun addCard(hashMap: HashMap<String,Any>){
        loadingProgress.value = true
        getCompositeDisposable().add(appRepository.goToAddAddCard(this, hashMap))
    }

    fun addMoneyWallet(hashMap: HashMap<String,Any>){
        loadingProgress.value = true
        getCompositeDisposable().add(appRepository.goToAddMoney(this, hashMap))
    }

    fun deleteCard(hashMap: HashMap<String,Any>){
        loadingProgress.value = true
        getCompositeDisposable().add(appRepository.goToDeleteCard(this, hashMap))
    }

}