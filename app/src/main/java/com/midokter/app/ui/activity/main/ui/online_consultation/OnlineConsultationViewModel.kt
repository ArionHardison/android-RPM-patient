package com.midokter.app.ui.activity.main.ui.online_consultation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.midokter.app.base.BaseViewModel

class OnlineConsultationViewModel : BaseViewModel<OnlineConsultationNavigator>() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is gallery Fragment"
    }
    val text: LiveData<String> = _text
}