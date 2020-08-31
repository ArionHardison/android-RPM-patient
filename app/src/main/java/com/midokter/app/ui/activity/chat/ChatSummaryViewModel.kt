package com.midokter.app.ui.activity.chat

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.midokter.app.base.BaseViewModel
import com.midokter.app.repositary.AppRepository
import com.midokter.app.repositary.model.ChatPromoResponse
import com.midokter.app.repositary.model.MessageResponse
import kotlinx.android.synthetic.main.content_chat_summary.view.*
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