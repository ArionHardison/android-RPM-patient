package com.midokter.app.ui.activity.searchGlobal

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.midokter.app.base.BaseViewModel
import com.midokter.app.repositary.AppRepository
import com.midokter.app.repositary.model.*

class SearchGlobalViewModel : BaseViewModel<SearchGlobalNavigator>(){
    var mResponse = MutableLiveData<SearchResponse>()
    var mDoctorslist: MutableList<Hospital>? = arrayListOf()
    var mCatagorylist: MutableList<SearchResponse.Specialities>? = arrayListOf()
    private val appRepository = AppRepository.instance()


    var listsize : ObservableField<String> = ObservableField("0")
    var search: ObservableField<String> = ObservableField("")

    var mfeedbacklist: MutableList<Hospital.Feedback>? = arrayListOf()
    var mphotoslist: MutableList<Hospital.Clinic.photos>? = arrayListOf()
    var mservcielist: MutableList<Hospital.DoctorService>? = arrayListOf()
    var mTiminglist: MutableList<Hospital.Timing>? = arrayListOf()




    fun getgloblsearch(hashMap: HashMap<String, Any>) {
        getCompositeDisposable().add(appRepository.getgloblsearch(this,hashMap))
    }
}