package com.midokter.app.ui.activity.main.ui.wallet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.midokter.app.base.BaseViewModel

class WalletViewModel : BaseViewModel<WalletNavigator>() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is tools Fragment"
    }
    val text: LiveData<String> = _text
}