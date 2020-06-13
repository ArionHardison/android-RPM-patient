package com.midokter.app.ui.activity.medicalrecorddetails

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.midokter.app.base.BaseViewModel
import com.midokter.app.repositary.AppRepository
import com.midokter.app.repositary.model.Medical
import com.midokter.app.repositary.model.MedicalRecord

class MedicalRecordDetailsViewModel : BaseViewModel<MedicalRecordDetailsNavigator>() {
    private val appRepository: AppRepository = AppRepository.instance()

    var mDoctorName: ObservableField<String> = ObservableField("")
    var mSpecialist: ObservableField<String> = ObservableField("")
    var mConsultedOnDate: ObservableField<String> = ObservableField("")
    var mRecordName: ObservableField<String> = ObservableField("")

}