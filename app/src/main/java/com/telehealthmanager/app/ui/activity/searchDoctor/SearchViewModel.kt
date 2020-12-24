package com.telehealthmanager.app.ui.activity.searchDoctor

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.telehealthmanager.app.BuildConfig
import com.telehealthmanager.app.base.BaseViewModel
import com.telehealthmanager.app.repositary.ApiInterface
import com.telehealthmanager.app.repositary.AppRepository
import com.telehealthmanager.app.repositary.BaseRepository
import com.telehealthmanager.app.repositary.model.DoctorListResponse
import com.telehealthmanager.app.repositary.model.Hospital
import com.telehealthmanager.app.repositary.model.MainResponse
import com.telehealthmanager.app.repositary.model.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.collections.set

class SearchViewModel : BaseViewModel<SearchNavigator>() {

    private val TAG = "SearchViewModel"
    private val appRepository = AppRepository.instance()

    var mDoctorResponse = MutableLiveData<MainResponse>()
    var mFavResponse = MutableLiveData<Response>()
    var loadingProgress = MutableLiveData<Boolean>(false)

    var listsize = MutableLiveData<String>("0")
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

    fun gethome(hashMap: HashMap<String, Any>) {
        loadingProgress.value = true
        getCompositeDisposable().add(appRepository.getHome(this, hashMap))
    }

    private val VISIBLE_THRESHOLD = 5

    fun listScrolled(visibleItemCount: Int, lastVisibleItem: Int, totalItemCount: Int, item: Hospital) {
        if ((visibleItemCount + lastVisibleItem + VISIBLE_THRESHOLD >= totalItemCount) && !loadingProgress.value!!) {
            Log.d(TAG, "listScrolled: True $totalItemCount LAST ITEM $lastVisibleItem VISIBLE COUNT $visibleItemCount DOCTOR ID ${item.id}")
            val hashMap: HashMap<String, Any> = HashMap()
            hashMap["page"] = item.id
            getSearchDoctors(hashMap)
        }
    }

    fun getSearchDoctors(hashMap: HashMap<String, Any>) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = BaseRepository().createApiClient(BuildConfig.BASE_URL, ApiInterface::class.java).getDoctorsList(hashMap)
            mDoctorResponse.postValue(response)
        }
    }

}