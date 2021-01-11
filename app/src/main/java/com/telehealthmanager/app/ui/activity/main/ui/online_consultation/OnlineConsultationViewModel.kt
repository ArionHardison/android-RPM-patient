package com.telehealthmanager.app.ui.activity.main.ui.online_consultation

import androidx.lifecycle.MutableLiveData
import com.telehealthmanager.app.base.BaseViewModel
import com.telehealthmanager.app.repositary.AppRepository
import com.telehealthmanager.app.repositary.model.chatmodel.ChatListResponse
import com.telehealthmanager.app.repositary.model.chatmodel.ChatStatusResponse

class OnlineConsultationViewModel : BaseViewModel<OnlineConsultationNavigator>() {

    private val appRepository = AppRepository.instance()
    var mChatResponse = MutableLiveData<ChatListResponse>()
    var mChatStatusResponse = MutableLiveData<ChatStatusResponse>()
    var loadingProgress = MutableLiveData<Boolean>()

    fun getChats() {
        loadingProgress.value = true
        getCompositeDisposable().add(appRepository.repositoryChats(this))
    }

    fun getChatStatus(id:Int) {
        loadingProgress.value = true
        getCompositeDisposable().add(appRepository.repositoryChatStatus(this,id))
    }
}