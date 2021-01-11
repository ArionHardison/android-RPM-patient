package com.telehealthmanager.app.ui.activity.main.ui.faq

import androidx.lifecycle.MutableLiveData
import com.telehealthmanager.app.base.BaseViewModel
import com.telehealthmanager.app.repositary.AppRepository
import com.telehealthmanager.app.repositary.model.FaqList
import com.telehealthmanager.app.repositary.model.FaqResponse

class FaqViewModel : BaseViewModel<FaqNavigator>(){
    private val appRepository = AppRepository.instance()
    var loadingProgress = MutableLiveData<Boolean>()
    var mFaqResponse = MutableLiveData<FaqResponse>()
    var mFaqList: MutableList<FaqList>? = arrayListOf()

    fun initApi() {
        loadingProgress.value = true
        appRepository.repositoryFaq(this)
    }
}