package com.telehealthmanager.app.ui.activity.patientDetail

import androidx.lifecycle.MutableLiveData
import com.telehealthmanager.app.base.BaseViewModel
import com.telehealthmanager.app.repositary.AppRepository
import com.telehealthmanager.app.repositary.model.BookedResponse

class PatientDetailViewModel : BaseViewModel<PatientDetailNavigator>(){
    private val appRepository = AppRepository.instance()
    var mBookedResponse = MutableLiveData<BookedResponse>()
    fun BookDoctor(hashMap: HashMap<String, Any>) {
        getCompositeDisposable().add(appRepository.bookdoctor(this, hashMap))
    }

    fun goToBooked() {navigator.goToBooked()}

}