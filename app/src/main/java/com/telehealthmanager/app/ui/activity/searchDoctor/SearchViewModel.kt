package com.telehealthmanager.app.ui.activity.searchDoctor

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.telehealthmanager.app.base.BaseViewModel
import com.telehealthmanager.app.repositary.AppRepository
import com.telehealthmanager.app.repositary.model.DoctorListResponse
import com.telehealthmanager.app.repositary.model.Hospital
import com.telehealthmanager.app.repositary.model.MainResponse
import com.telehealthmanager.app.repositary.model.Response
import com.telehealthmanager.app.repositary.pagination.DoctorDataSource

class SearchViewModel : BaseViewModel<SearchNavigator>() {

    private val TAG = "SearchViewModel"
    private val appRepository = AppRepository.instance()

    var mDoctorResponse = MutableLiveData<MainResponse>()
    var mFavResponse = MutableLiveData<Response>()
    var loadingProgress = MutableLiveData<Boolean>(false)

    var counnt = MutableLiveData<Int>(0)
    var searchDoctorsCount: Int = 0
    var doctorsCount: Int = 0

    val queryLiveData: MutableLiveData<String> = MutableLiveData<String>("")

    private val defaultCount = MutableLiveData(0)

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
    var degreeSpecialities: ObservableField<String> = ObservableField("")
    var branch: ObservableField<String> = ObservableField("")
    var percentage: ObservableField<String> = ObservableField("0%")
    var experience: ObservableField<String> = ObservableField("0")
    var fee: ObservableField<String> = ObservableField()
    var clinic: ObservableField<String> = ObservableField("")
    var clinicAddress: ObservableField<String> = ObservableField("")
    var profileVideo: ObservableField<String> = ObservableField("")
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

    fun clickVideo() {
        navigator.viewVideoClick()
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

    fun searchRepo(queryString: String) {
        queryLiveData.postValue(queryString)
    }

    val getData = queryLiveData.switchMap {
        if (it != "") {
            searchDoctorsCount = 0
            appRepository.getSearchResults(it, this).cachedIn(viewModelScope)
        } else {
            appRepository.getSearchResults(it, this).cachedIn(viewModelScope)
            appRepository.getResults(defaultCount.value!!.toInt(), this).cachedIn(viewModelScope)
        }
    }


    fun postCount(size: Int) {
        doctorsCount += size
        Log.d(TAG, "postCount: $doctorsCount")
        counnt.postValue(doctorsCount)
    }

    fun postSearchCount(size: Int) {
        searchDoctorsCount += size
        Log.d(TAG, "postSearchCount: $searchDoctorsCount")
        counnt.postValue(searchDoctorsCount)
    }


    val doctorsList = Pager(PagingConfig(pageSize = 10)) {
        DoctorDataSource(appRepository, 0, this)
    }.flow.cachedIn(viewModelScope)

}