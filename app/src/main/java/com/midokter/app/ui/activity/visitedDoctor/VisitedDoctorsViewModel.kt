package com.midokter.app.ui.activity.visitedDoctor

import androidx.lifecycle.MutableLiveData
import com.midokter.app.base.BaseViewModel
import com.midokter.app.repositary.AppRepository
import com.midokter.app.repositary.model.MainResponse

class VisitedDoctorsViewModel : BaseViewModel<VisitedDoctorsNavigator>(){
    var mDoctorResponse = MutableLiveData<MainResponse>()
    var mDoctorslist: MutableList<MainResponse.VisitedDoctor>? = arrayListOf()
    private val appRepository = AppRepository.instance()

    fun gethome(hashMap: HashMap<String, Any>) {
        getCompositeDisposable().add(appRepository.getHome(this,hashMap))
    }
}