package com.telehealthmanager.app.ui.activity.main.ui.medical_records

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.telehealthmanager.app.base.BaseViewModel
import com.telehealthmanager.app.repositary.AppRepository
import com.telehealthmanager.app.repositary.model.MedicalRecord

class MedicalRecordsViewModel : BaseViewModel<MedicalRecordsNavigator>() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is share Fragment"
    }
    val text: LiveData<String> = _text
    var  mMedicalRecordResponse = MutableLiveData<MedicalRecord>()

    private val appRepository = AppRepository.instance()


    fun getMedicalRecord(){
        getCompositeDisposable().add(appRepository.getMedicalRecord(this))
    }
}