package com.telehealthmanager.app.ui.activity.searchDoctor

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.telehealthmanager.app.base.BaseViewModel
import com.telehealthmanager.app.repositary.AppRepository
import com.telehealthmanager.app.repositary.model.DoctorListResponse
import com.telehealthmanager.app.repositary.model.Hospital
import com.telehealthmanager.app.repositary.model.MainResponse
import com.telehealthmanager.app.repositary.model.Response

class SearchViewModel : BaseViewModel<SearchNavigator>() {
    private val appRepository = AppRepository.instance()

    var mDoctorResponse = MutableLiveData<MainResponse>()
    var mDoctorList: MutableList<Hospital>? = arrayListOf()
    var mFavResponse = MutableLiveData<Response>()

    var listsize: ObservableField<String> = ObservableField("0")
    var search: ObservableField<String> = ObservableField("")

    var mDoctorProfile = MutableLiveData<DoctorListResponse.specialities.DoctorProfile>()
    var mfeedbacklist: MutableList<Hospital.Feedback>? = arrayListOf()
    var mphotoslist: MutableList<Hospital.Clinic.photos>? = arrayListOf()
    var mservcielist: MutableList<Hospital.DoctorService>? = arrayListOf()
    var mTimingList: MutableList<Hospital.Timing>? = arrayListOf()

    var mFavDoctorProfile = MutableLiveData<MainResponse.Doctor>()
    var mSearchDoctorProfile = MutableLiveData<Hospital>()

    var id: ObservableField<Int> = ObservableField()
    var name: ObservableField<String> = ObservableField("")
    var profilePic: ObservableField<String> = ObservableField("")
    var specialities: ObservableField<String> = ObservableField("")
    var specialitiesID: ObservableField<String> = ObservableField("0")
    var degree: ObservableField<String> = ObservableField("")
    var branch: ObservableField<String> = ObservableField("")
    var percentage: ObservableField<String> = ObservableField("0%")
    var experience: ObservableField<String> = ObservableField("0")
    var fee: ObservableField<String> = ObservableField()
    var clinic: ObservableField<String> = ObservableField("")
    var clinicAddress: ObservableField<String> = ObservableField("")
    var favourite: ObservableField<String> = ObservableField("false")

    fun favClick() {
        navigator.onFavClick()
    }

    fun shareClick() {
        navigator.onShareClick()
    }

    fun bookClick() {
        navigator.onBookClick()
    }

    fun viewAllClick() {
        navigator.viewAllClick()
    }

    fun clickVideoCall() {
        navigator.viewCallClick()
    }

    fun clickInfoClick() {
        navigator.viewInfoClick()
    }

    fun clickShareClick() {
        navigator.viewShareClick()
    }

    fun addFav(hashMap: HashMap<String, Any>) {
        getCompositeDisposable().add(appRepository.addFav(this, hashMap))
    }

    fun gethome(hashMap: HashMap<String, Any>) {
        getCompositeDisposable().add(appRepository.getHome(this, hashMap))
    }
}