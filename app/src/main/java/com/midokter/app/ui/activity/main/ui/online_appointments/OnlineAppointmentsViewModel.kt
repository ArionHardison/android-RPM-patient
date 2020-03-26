package com.midokter.app.ui.activity.main.ui.online_appointments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.midokter.app.base.BaseViewModel

class OnlineAppointmentsViewModel : BaseViewModel<OnlineAppointmentsNavigator>() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is gallery Fragment"
    }
    val text: LiveData<String> = _text
}