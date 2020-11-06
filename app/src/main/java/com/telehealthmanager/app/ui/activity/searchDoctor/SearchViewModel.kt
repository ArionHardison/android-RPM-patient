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
    var mDoctorResponse = MutableLiveData<MainResponse>()
    var mDoctorslist: MutableList<Hospital>? = arrayListOf()

    private val appRepository = AppRepository.instance()
    var mFavResponse = MutableLiveData<Response>()

    var listsize: ObservableField<String> = ObservableField("0")
    var search: ObservableField<String> = ObservableField("")

    var mDoctorProfile = MutableLiveData<DoctorListResponse.specialities.DoctorProfile>()
    var mfeedbacklist: MutableList<Hospital.Feedback>? = arrayListOf()
    var mphotoslist: MutableList<Hospital.Clinic.photos>? = arrayListOf()
    var mservcielist: MutableList<Hospital.DoctorService>? = arrayListOf()
    var mTiminglist: MutableList<Hospital.Timing>? = arrayListOf()


    var mfavDoctorProfile = MutableLiveData<MainResponse.Doctor>()
    var msearchDoctorProfile = MutableLiveData<Hospital>()

    var id: ObservableField<Int> = ObservableField()
    var name: ObservableField<String> = ObservableField("")
    var profile_pic: ObservableField<String> = ObservableField("")
    var specialities: ObservableField<String> = ObservableField("")
    var degree: ObservableField<String> = ObservableField("")
    var branch: ObservableField<String> = ObservableField("")
    var percentage: ObservableField<String> = ObservableField("0%")
    var experience: ObservableField<String> = ObservableField("0")
    var fee: ObservableField<String> = ObservableField()
    var clinic: ObservableField<String> = ObservableField("")
    var clinic_address: ObservableField<String> = ObservableField("")
    var favourite: ObservableField<String> = ObservableField("false")
    fun FavClick() {
        navigator.onfavclick()
    }

    fun ShareClick() {
        navigator.onshareclick()
    }

    fun BookClick() {
        navigator.Onbookclick()
    }

    fun ViewallClick() {
        navigator.ViewallClick()
    }

    fun addfav(hashMap: HashMap<String, Any>) {
        getCompositeDisposable().add(appRepository.addfav(this, hashMap))
    }

    fun gethome(hashMap: HashMap<String, Any>) {
        getCompositeDisposable().add(appRepository.getHome(this, hashMap))
    }
}