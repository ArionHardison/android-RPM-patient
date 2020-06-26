package com.midokter.app.ui.activity.main.ui.remainder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.midokter.app.base.BaseViewModel

class RemainderViewModel : BaseViewModel<RemainderNavigator>() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is slideshow Fragment"
    }
    val text: LiveData<String> = _text


    fun onNewRemainderClicked(){
        navigator.onNewRemainderClicked()
    }
}