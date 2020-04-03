package com.midokter.app.ui.activity.main.ui.appointment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.midokter.app.base.BaseViewModel

class AppointmentViewModel : BaseViewModel<AppointmentNavigator>() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is gallery Fragment"
    }
    val text: LiveData<String> = _text
}