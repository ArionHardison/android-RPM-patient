package com.telehealthmanager.app.ui.activity.searchGlobal

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.telehealthmanager.app.base.BaseViewModel
import com.telehealthmanager.app.repositary.AppRepository
import com.telehealthmanager.app.repositary.model.Hospital
import com.telehealthmanager.app.repositary.model.SearchResponse

class SearchGlobalViewModel : BaseViewModel<SearchGlobalNavigator>() {
    private val appRepository = AppRepository.instance()
    var mResponse = MutableLiveData<SearchResponse>()
    var mDoctorsList: MutableList<Hospital>? = arrayListOf()
    var mCategoryList: MutableList<SearchResponse.Specialities>? = arrayListOf()
    var mClinicList: MutableList<SearchResponse.Clinic>? = arrayListOf()
    var listsize: ObservableField<String> = ObservableField("0")
    var search: ObservableField<String> = ObservableField("")

    var mFeedbackList: MutableList<Hospital.Feedback>? = arrayListOf()
    var mPhotosList: MutableList<Hospital.Clinic.photos>? = arrayListOf()
    var mServiceList: MutableList<Hospital.DoctorService>? = arrayListOf()
    var mTimingList: MutableList<Hospital.Timing>? = arrayListOf()

    var loadingProgress = MutableLiveData<Boolean>()

    fun getGlobalSearch(hashMap: HashMap<String, Any>) {
        getCompositeDisposable().add(appRepository.getGloblSearch(this, hashMap))
    }
}