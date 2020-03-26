package com.midokter.app.ui.activity.main.ui.medical_records

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.midokter.app.base.BaseViewModel

class MedicalRecordsViewModel : BaseViewModel<MedicalRecordsNavigator>() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is share Fragment"
    }
    val text: LiveData<String> = _text
}