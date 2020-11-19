package com.telehealthmanager.app.ui.activity.payment

import androidx.lifecycle.MutableLiveData
import com.telehealthmanager.app.base.BaseViewModel
import com.telehealthmanager.app.repositary.AppRepository
import com.telehealthmanager.app.repositary.model.MessageResponse
import java.util.*

class PaymentViewModel : BaseViewModel<PaymentNavigator>() {
    private val appRepository = AppRepository.instance()
    var loadingProgress = MutableLiveData<Boolean>()
    var mChatRequestResponse = MutableLiveData<MessageResponse>()

    fun clickAddCardBtn() {
        navigator.openAddCard()
    }

    fun clickPayment() {
        navigator.paymentInitiate()
    }

    fun payForChatRequest(hashMap: HashMap<String, Any>) {
        loadingProgress.value = true
        getCompositeDisposable().add(appRepository.payForChatRequest(this, hashMap))
    }
}