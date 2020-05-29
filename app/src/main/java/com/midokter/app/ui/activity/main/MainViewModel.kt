package com.midokter.app.ui.activity.main

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.midokter.app.base.BaseViewModel
import com.midokter.app.repositary.AppRepository
import com.midokter.app.repositary.model.ProfileResponse

class MainViewModel : BaseViewModel<MainNavigator>(){
    private val appRepository = AppRepository.instance()
   // var mhomeResponse = MutableLiveData<HomeResponse>()
    var mProfileResponse = MutableLiveData<ProfileResponse>()
    var name : ObservableField<String> = ObservableField("")
    var imageurl : ObservableField<String> = ObservableField("")
    var profilepercentage : ObservableField<String> = ObservableField("")

    fun getprofile() {

        getCompositeDisposable().add(appRepository.getProfile(this))
    }
}