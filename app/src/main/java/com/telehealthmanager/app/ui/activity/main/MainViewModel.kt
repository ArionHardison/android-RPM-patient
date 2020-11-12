package com.telehealthmanager.app.ui.activity.main

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.telehealthmanager.app.base.BaseViewModel
import com.telehealthmanager.app.repositary.AppRepository
import com.telehealthmanager.app.repositary.model.ProfileResponse
import com.telehealthmanager.app.repositary.model.Response
import com.telehealthmanager.app.repositary.model.VideoStatusCheck

class MainViewModel : BaseViewModel<MainNavigator>(){
    private val appRepository = AppRepository.instance()
   // var mhomeResponse = MutableLiveData<HomeResponse>()
    var mProfileResponse = MutableLiveData<ProfileResponse>()
    var name : ObservableField<String> = ObservableField("")
    var imageurl : ObservableField<String> = ObservableField("")
    var profilepercentage : ObservableField<String> = ObservableField("")
    var mVideoIncomingResponse = MutableLiveData<VideoStatusCheck>()

    fun getprofile() {
        getCompositeDisposable().add(appRepository.getProfile(this))
    }


    fun callCheckVideoAPI() {
        getCompositeDisposable().add(appRepository.videoCheckStatusAPI(this))
    }
}