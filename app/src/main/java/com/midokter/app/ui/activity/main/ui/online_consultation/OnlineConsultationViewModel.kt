package com.midokter.app.ui.activity.main.ui.online_consultation

import androidx.lifecycle.MutableLiveData
import com.midokter.app.base.BaseViewModel
import com.midokter.app.repositary.AppRepository
import com.midokter.app.repositary.model.chatmodel.ChatListResponse

class OnlineConsultationViewModel : BaseViewModel<OnlineConsultationNavigator>() {


    private val appRepository = AppRepository.instance()

    var mChatResponse = MutableLiveData<ChatListResponse>()
    var loadingProgress = MutableLiveData<Boolean>()
    fun getChats() {
        loadingProgress.value = true
        getCompositeDisposable().add(appRepository.getChatsAPI(this))
    }
}