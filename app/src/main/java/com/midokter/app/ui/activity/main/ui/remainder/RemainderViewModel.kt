package com.midokter.app.ui.activity.main.ui.remainder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.midokter.app.base.BaseViewModel
import com.midokter.app.repositary.AppRepository
import com.midokter.app.repositary.model.AddRemainderResponse
import com.midokter.app.repositary.model.ArticleResponse
import com.midokter.app.repositary.model.ReminderResponse

class RemainderViewModel : BaseViewModel<RemainderNavigator>() {
    private val appRepository = AppRepository.instance()

    var mReminderResponse = MutableLiveData<ReminderResponse>()
    var loadingProgress = MutableLiveData<Boolean>()
    var mReminders :MutableList<ReminderResponse.Reminder>? = arrayListOf()

    private val _text = MutableLiveData<String>().apply {
        value = "This is slideshow Fragment"
    }
    val text: LiveData<String> = _text


    fun onNewRemainderClicked(){
        navigator.onNewRemainderClicked()
    }

    fun getReminders() {

        loadingProgress.value = true
        getCompositeDisposable().add(appRepository.getReminders(this))
    }
}