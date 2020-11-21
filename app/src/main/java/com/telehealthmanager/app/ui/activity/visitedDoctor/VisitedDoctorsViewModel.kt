package com.telehealthmanager.app.ui.activity.visitedDoctor

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.telehealthmanager.app.base.BaseViewModel
import com.telehealthmanager.app.repositary.AppRepository
import com.telehealthmanager.app.repositary.model.*

class VisitedDoctorsViewModel : BaseViewModel<VisitedDoctorsNavigator>(){

    var mVisitedAppointmentDoc = MutableLiveData<VisitedAppointmentDoc>()
    var mAppointmentDetails = MutableLiveData<Appointment>()
    var mVisitedDoctorDetails = MutableLiveData<Appointment>()
    var mCancelResponse = MutableLiveData<Response>()
    var mFeedbackResponse = MutableLiveData<FeedbackResponse>()

    var id : ObservableField<Int> = ObservableField()
    var mDoctorID : ObservableField<String> = ObservableField("")
    var name : ObservableField<String> = ObservableField("")
    var specialit : ObservableField<String> = ObservableField("")
    var catagiery : ObservableField<String> = ObservableField("")
    var mClinic : ObservableField<String> = ObservableField("")
    var bookfor : ObservableField<String> = ObservableField("")
    var scheduledAt : ObservableField<String> = ObservableField("")
    var status : ObservableField<String> = ObservableField("")
    var mComment : ObservableField<String> = ObservableField("")
    var mTitle : ObservableField<String> = ObservableField("")

    private val appRepository = AppRepository.instance()

    fun onlike() {
        navigator.onlike()
    }

    fun onunlike() {
        navigator.onunlike()
    }

    fun onSubmit() {
        navigator.onSubmit()
    }

    fun postFeedback(hashMap: HashMap<String, Any>) {
        getCompositeDisposable().add(appRepository.postfeedback(this,hashMap))
    }

    fun getListVisitedDoc() {
        getCompositeDisposable().add(appRepository.getVisitedDoc(this))
    }

    fun cancelClick(hashMap: HashMap<String, Any>) {
        getCompositeDisposable().add(appRepository.cancelAppointment(this,hashMap))
    }
}