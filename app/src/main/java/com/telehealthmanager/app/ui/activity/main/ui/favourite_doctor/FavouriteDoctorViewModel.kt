package com.telehealthmanager.app.ui.activity.main.ui.favourite_doctor

import androidx.lifecycle.MutableLiveData
import com.telehealthmanager.app.base.BaseViewModel
import com.telehealthmanager.app.repositary.AppRepository
import com.telehealthmanager.app.repositary.model.MainResponse

class FavouriteDoctorViewModel : BaseViewModel<FavouriteDoctorNavigator>(){
    var mDoctorResponse = MutableLiveData<MainResponse>()
    var mDoctorsList: MutableList<MainResponse.Doctor>? = arrayListOf()
    private val appRepository = AppRepository.instance()

    fun gethome(hashMap: HashMap<String, Any>) {
        getCompositeDisposable().add(appRepository.getHome(this,hashMap))
    }
}