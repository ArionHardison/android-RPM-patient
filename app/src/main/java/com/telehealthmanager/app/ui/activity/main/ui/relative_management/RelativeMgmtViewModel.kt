package com.telehealthmanager.app.ui.activity.main.ui.relative_management

import androidx.lifecycle.MutableLiveData
import com.telehealthmanager.app.base.BaseViewModel
import com.telehealthmanager.app.repositary.AppRepository
import com.telehealthmanager.app.repositary.model.chatmodel.ChatListResponse
import com.telehealthmanager.app.repositary.model.chatmodel.ChatStatusResponse

class RelativeMgmtViewModel : BaseViewModel<RelativeMgmtNavigator>() {
    private val appRepository = AppRepository.instance()
    var loadingProgress = MutableLiveData<Boolean>()

    fun clickAddRelative(){
        navigator.openAddRelative()
    }
}