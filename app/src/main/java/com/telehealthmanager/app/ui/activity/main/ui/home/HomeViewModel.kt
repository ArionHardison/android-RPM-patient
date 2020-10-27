package com.telehealthmanager.app.ui.activity.main.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.telehealthmanager.app.base.BaseViewModel

class HomeViewModel : BaseViewModel<HomeNavigator>() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
}