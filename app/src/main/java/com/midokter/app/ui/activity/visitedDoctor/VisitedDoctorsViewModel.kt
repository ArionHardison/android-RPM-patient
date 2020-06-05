package com.midokter.app.ui.activity.visitedDoctor

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.midokter.app.base.BaseViewModel
import com.midokter.app.repositary.AppRepository
import com.midokter.app.repositary.model.AppointmentResponse
import com.midokter.app.repositary.model.FeedbackResponse
import com.midokter.app.repositary.model.MainResponse

class VisitedDoctorsViewModel : BaseViewModel<VisitedDoctorsNavigator>(){
    var mDoctorResponse = MutableLiveData<MainResponse>()
    var mDoctorslist: MutableList<MainResponse.VisitedDoctor>? = arrayListOf()
    var mPastDoctorDetails = MutableLiveData<AppointmentResponse.Previous.Appointment>()
    var mupcomingDoctorDetails = MutableLiveData<AppointmentResponse.Upcomming.Appointment>()
    var name : ObservableField<String> = ObservableField("")
    var scheduled_at : ObservableField<String> = ObservableField("")
    var status : ObservableField<String> = ObservableField("")

    var mFeedbackResponse = MutableLiveData<FeedbackResponse>()
    private val appRepository = AppRepository.instance()

    fun gethome(hashMap: HashMap<String, Any>) {
        getCompositeDisposable().add(appRepository.getHome(this,hashMap))
    }
}