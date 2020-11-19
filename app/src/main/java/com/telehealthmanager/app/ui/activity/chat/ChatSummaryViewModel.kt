package com.telehealthmanager.app.ui.activity.chat

import androidx.lifecycle.MutableLiveData
import com.telehealthmanager.app.base.BaseViewModel
import com.telehealthmanager.app.repositary.AppRepository
import com.telehealthmanager.app.repositary.model.ChatPromoResponse
import com.telehealthmanager.app.repositary.model.MessageResponse
import java.util.HashMap

class ChatSummaryViewModel: BaseViewModel<ChatNavigator>() {
    private val appRepository = AppRepository.instance()
    var mChatPromoResponse = MutableLiveData<ChatPromoResponse>()

    fun addPromoCode(hashMap: HashMap<String, Any>) {
        getCompositeDisposable().add(appRepository.addChatPromoCode(this, hashMap))
    }
}