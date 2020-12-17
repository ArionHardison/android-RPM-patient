package com.telehealthmanager.app.ui.activity.patientDetail

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.telehealthmanager.app.base.BaseViewModel
import com.telehealthmanager.app.repositary.AppRepository
import com.telehealthmanager.app.repositary.model.BookedResponse

class PatientDetailViewModel : BaseViewModel<PatientDetailNavigator>(){
    private val appRepository = AppRepository.instance()
    var mBookedResponse = MutableLiveData<BookedResponse>()
    var paymentMode: ObservableField<String> = ObservableField("")
    var mSelectedCard: ObservableField<String> = ObservableField("")
    var paymentType: ObservableField<String> = ObservableField("")


    fun bookDoctor(hashMap: HashMap<String, Any>) {
        getCompositeDisposable().add(appRepository.bookDoctor(this, hashMap))
    }

}