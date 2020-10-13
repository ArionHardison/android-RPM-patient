package com.midokter.app.ui.activity.main

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.midokter.app.base.BaseViewModel
import com.midokter.app.repositary.AppRepository
import com.midokter.app.repositary.model.ProfileResponse
import com.midokter.app.repositary.model.Response
import com.midokter.app.repositary.model.VideoStatusCheck

class MainViewModel : BaseViewModel<MainNavigator>(){
    private val appRepository = AppRepository.instance()
   // var mhomeResponse = MutableLiveData<HomeResponse>()
    var mProfileResponse = MutableLiveData<ProfileResponse>()
    var name : ObservableField<String> = ObservableField("")
    var imageurl : ObservableField<String> = ObservableField("")
    var profilepercentage : ObservableField<String> = ObservableField("")
    var mLogoutResponse = MutableLiveData<Response>()
    var mVideoIncomingResponse = MutableLiveData<VideoStatusCheck>()

    fun getprofile() {

        getCompositeDisposable().add(appRepository.getProfile(this))
    }
    fun logout(hashMap: HashMap<String, Any>) {
        getCompositeDisposable().add(appRepository.logout(this,hashMap))
    }

    fun callCheckVideoAPI() {
        getCompositeDisposable().add(appRepository.videoCheckStatusAPI(this))
    }
}