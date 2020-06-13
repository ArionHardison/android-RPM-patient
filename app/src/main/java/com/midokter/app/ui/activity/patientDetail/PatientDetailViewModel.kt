package com.midokter.app.ui.activity.patientDetail

import androidx.lifecycle.MutableLiveData
import com.midokter.app.base.BaseViewModel
import com.midokter.app.repositary.AppRepository
import com.midokter.app.repositary.model.BookedResponse
import com.midokter.app.repositary.model.RegisterResponse

class PatientDetailViewModel : BaseViewModel<PatientDetailNavigator>(){
    private val appRepository = AppRepository.instance()
    var mBookedResponse = MutableLiveData<BookedResponse>()
    fun BookDoctor(hashMap: HashMap<String, Any>) {
        getCompositeDisposable().add(appRepository.bookdoctor(this, hashMap))
    }

    fun goToBooked() {navigator.goToBooked()}

}