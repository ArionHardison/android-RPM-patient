package com.midokter.app.ui.activity.searchDoctor

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.midokter.app.base.BaseViewModel
import com.midokter.app.repositary.AppRepository
import com.midokter.app.repositary.model.MainResponse

class SearchViewModel : BaseViewModel<SearchViewModel>(){
    var mDoctorResponse = MutableLiveData<MainResponse>()
    var mDoctorslist: MutableList<MainResponse.SearchDoctor>? = arrayListOf()
    var listsize : ObservableField<String> = ObservableField("0")
    var search: ObservableField<String> = ObservableField("")
    private val appRepository = AppRepository.instance()

    fun gethome(hashMap: HashMap<String, Any>) {
        getCompositeDisposable().add(appRepository.getHome(this,hashMap))
    }
}