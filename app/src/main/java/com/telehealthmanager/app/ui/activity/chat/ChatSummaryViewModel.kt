package com.telehealthmanager.app.ui.activity.chat

import androidx.lifecycle.MutableLiveData
import com.telehealthmanager.app.base.BaseViewModel
import com.telehealthmanager.app.repositary.AppRepository
import com.telehealthmanager.app.repositary.model.ChatPromoResponse
import com.telehealthmanager.app.repositary.model.MessageResponse
import java.util.HashMap

class ChatSummaryViewModel: BaseViewModel<ChatNavigator>() {
    var mChatPromoResponse = MutableLiveData<ChatPromoResponse>()
    var mChatRequestResponse = MutableLiveData<MessageResponse>()

    private val appRepository = AppRepository.instance()

    fun addPromoCode(hashMap: HashMap<String, Any>) {
        getCompositeDisposable().add(appRepository.addChatPromoCode(this, hashMap))
    }

    fun payForChatRequest(hashMap: HashMap<String, Any>) {
        getCompositeDisposable().add(appRepository.payForChatRequest(this, hashMap))
    }


}