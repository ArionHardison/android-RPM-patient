package com.telehealthmanager.app.ui.activity.chat

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.telehealthmanager.app.base.BaseViewModel
import com.telehealthmanager.app.repositary.AppRepository
import com.telehealthmanager.app.repositary.model.ChatPromoSuccess
import java.util.*

class ChatSummaryViewModel : BaseViewModel<ChatNavigator>() {
    private val appRepository = AppRepository.instance()
    var mChatPromoResponse = MutableLiveData<ChatPromoSuccess>()


    fun addPromoCode(hashMap: HashMap<String, Any>) {
        getCompositeDisposable().add(appRepository.addChatPromoCode(this, hashMap))
    }
}