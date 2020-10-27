package com.telehealthmanager.app.ui.activity.medicalrecorddetails

import androidx.databinding.ObservableField
import com.telehealthmanager.app.base.BaseViewModel
import com.telehealthmanager.app.repositary.AppRepository

class MedicalRecordDetailsViewModel : BaseViewModel<MedicalRecordDetailsNavigator>() {
    private val appRepository: AppRepository = AppRepository.instance()

    var mDoctorName: ObservableField<String> = ObservableField("")
    var mSpecialist: ObservableField<String> = ObservableField("")
    var mConsultedOnDate: ObservableField<String> = ObservableField("")
    var mRecordName: ObservableField<String> = ObservableField("")

}