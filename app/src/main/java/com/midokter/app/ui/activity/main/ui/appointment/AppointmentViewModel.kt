package com.midokter.app.ui.activity.main.ui.appointment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.midokter.app.base.BaseViewModel
import com.midokter.app.repositary.AppRepository
import com.midokter.app.repositary.model.AppointmentResponse

class AppointmentViewModel : BaseViewModel<AppointmentNavigator>() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is gallery Fragment"
    }
    val text: LiveData<String> = _text
    var mResponse = MutableLiveData<AppointmentResponse>()
    var mUpcominglist: MutableList<AppointmentResponse.Upcomming.Appointment>? = arrayListOf()
    var mPreviouslist: MutableList<AppointmentResponse.Previous.Appointment>? = arrayListOf()
    private val appRepository = AppRepository.instance()

    fun getAppointment() {
        getCompositeDisposable().add(appRepository.getAppointment(this))
    }
}