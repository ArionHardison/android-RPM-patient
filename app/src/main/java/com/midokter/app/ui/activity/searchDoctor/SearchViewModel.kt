package com.midokter.app.ui.activity.searchDoctor

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.midokter.app.base.BaseViewModel
import com.midokter.app.repositary.AppRepository
import com.midokter.app.repositary.model.DoctorListResponse
import com.midokter.app.repositary.model.MainResponse

class SearchViewModel : BaseViewModel<SearchNavigator>(){
    var mDoctorResponse = MutableLiveData<MainResponse>()
    var mDoctorslist: MutableList<MainResponse.SearchDoctor>? = arrayListOf()
    var listsize : ObservableField<String> = ObservableField("0")

    var search: ObservableField<String> = ObservableField("")
    var mDoctorProfile = MutableLiveData<DoctorListResponse.specialities.DoctorProfile>()

    var mfeedbacklist: MutableList<DoctorListResponse.specialities.DoctorProfile.Hospital.Feedback>? = arrayListOf()
    var mphotoslist: MutableList<DoctorListResponse.specialities.DoctorProfile.Hospital.Feedback>? = arrayListOf()
    var mservcielist: MutableList<DoctorListResponse.specialities.DoctorProfile.Hospital.DoctorService>? = arrayListOf()
    private val appRepository = AppRepository.instance()

    var name: ObservableField<String> = ObservableField("")
    var specialities: ObservableField<String> = ObservableField("")
    var degree: ObservableField<String> = ObservableField("")
    var branch: ObservableField<String> = ObservableField("")
    var percentage: ObservableField<String> = ObservableField("")
    var experience: ObservableField<String> = ObservableField("")
    var fee: ObservableField<Int> = ObservableField()
    var mor_time: ObservableField<String> = ObservableField("")
    var eve_time: ObservableField<String> = ObservableField("")
    var clinic: ObservableField<String> = ObservableField("")
    var clinic_address: ObservableField<String> = ObservableField("")

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

    fun gethome(hashMap: HashMap<String, Any>) {
        getCompositeDisposable().add(appRepository.getHome(this,hashMap))
    }
}