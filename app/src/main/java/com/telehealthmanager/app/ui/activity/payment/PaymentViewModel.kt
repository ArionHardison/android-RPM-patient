package com.telehealthmanager.app.ui.activity.payment

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.telehealthmanager.app.base.BaseViewModel
import com.telehealthmanager.app.repositary.AppRepository
import com.telehealthmanager.app.repositary.model.MessageResponse
import java.util.*

class PaymentViewModel : BaseViewModel<PaymentNavigator>() {
    private val appRepository = AppRepository.instance()
    var loadingProgress = MutableLiveData<Boolean>()
    var mChatRequestResponse = MutableLiveData<MessageResponse>()
    var paymentMode: ObservableField<String> = ObservableField("")
    var mSelectedCard: ObservableField<String> = ObservableField("")
    var paymentType: ObservableField<String> = ObservableField("")

    fun clickAddCardBtn() {
        navigator.openAddCard()
    }

    fun clickPayment() {
        navigator.paymentInitiate()
    }

    fun payForChatRequest(hashMap: HashMap<String, Any>) {
        loadingProgress.value = true
        getCompositeDisposable().add(appRepository.repositoryPayChatRequest(this, hashMap))
    }
}