package com.telehealthmanager.app.ui.activity.main.ui.appointment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.telehealthmanager.app.base.BaseViewModel
import com.telehealthmanager.app.repositary.AppRepository
import com.telehealthmanager.app.repositary.model.Appointment
import com.telehealthmanager.app.repositary.model.AppointmentResponse
import com.telehealthmanager.app.repositary.model.Response

class AppointmentViewModel : BaseViewModel<AppointmentNavigator>() {

    var mResponse = MutableLiveData<AppointmentResponse>()
    var mCancelResponse = MutableLiveData<Response>()
    var mUpcomingList: MutableList<Appointment>? = arrayListOf()
    var mPreviousList: MutableList<Appointment>? = arrayListOf()
    var loadingProgress = MutableLiveData<Boolean>()

    private val appRepository = AppRepository.instance()

    fun getAppointment() {
        getCompositeDisposable().add(appRepository.getAppointment(this))
    }
    fun cancelAppointment(hashMap: HashMap<String, Any>) {
        getCompositeDisposable().add(appRepository.cancelAppointment(this,hashMap))
    }
}