package com.telehealthmanager.app.ui.activity.main.ui.medical_records

import androidx.lifecycle.MutableLiveData
import com.telehealthmanager.app.base.BaseViewModel
import com.telehealthmanager.app.repositary.AppRepository
import com.telehealthmanager.app.repositary.model.MainResponse
import com.telehealthmanager.app.repositary.model.MedicalRecord
import com.telehealthmanager.app.repositary.model.ResponseDoctors

class MedicalRecordsViewModel : BaseViewModel<MedicalRecordsNavigator>() {
    private val appRepository = AppRepository.instance()
    var mMedicalRecordResponse = MutableLiveData<MedicalRecord>()
    var mRecordList: MutableList<MedicalRecord.Medical>? = arrayListOf()

    var loadingProgress = MutableLiveData<Boolean>()

    fun getMedicalRecord() {
        getCompositeDisposable().add(appRepository.getMedicalRecord(this))
    }

    fun clickMedicalRecord(){
        navigator.goToMedicalRecord()
    }
}