package com.midokter.app.ui.activity.visitedDoctor

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.midokter.app.base.BaseViewModel
import com.midokter.app.repositary.AppRepository
import com.midokter.app.repositary.model.AppointmentResponse
import com.midokter.app.repositary.model.FeedbackResponse
import com.midokter.app.repositary.model.MainResponse
import com.midokter.app.repositary.model.Response

class VisitedDoctorsViewModel : BaseViewModel<VisitedDoctorsNavigator>(){
    var mDoctorResponse = MutableLiveData<MainResponse>()
    var mDoctorslist: MutableList<MainResponse.VisitedDoctor>? = arrayListOf()
    var mPastDoctorDetails = MutableLiveData<AppointmentResponse.Previous.Appointment>()
    var mupcomingDoctorDetails = MutableLiveData<AppointmentResponse.Upcomming.Appointment>()
    var mVisitedDoctorDetails = MutableLiveData<MainResponse.VisitedDoctor>()
    var mCancelResponse = MutableLiveData<Response>()
    var mFeedbackResponse = MutableLiveData<FeedbackResponse>()

    var id : ObservableField<Int> = ObservableField()
    var name : ObservableField<String> = ObservableField("")
    var specialit : ObservableField<String> = ObservableField("")
    var catagiery : ObservableField<String> = ObservableField("")
    var Clinic : ObservableField<String> = ObservableField("")
    var bookfor : ObservableField<String> = ObservableField("")
    var scheduled_at : ObservableField<String> = ObservableField("")
    var status : ObservableField<String> = ObservableField("")



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
    fun postfeedback(hashMap: HashMap<String, Any>) {
        getCompositeDisposable().add(appRepository.postfeedback(this,hashMap))
    }
    fun gethome(hashMap: HashMap<String, Any>) {
        getCompositeDisposable().add(appRepository.getHome(this,hashMap))
    }


    fun cancelclick(hashMap: HashMap<String, Any>) {
        getCompositeDisposable().add(appRepository.cancelAppointment(this,hashMap))
    }
}