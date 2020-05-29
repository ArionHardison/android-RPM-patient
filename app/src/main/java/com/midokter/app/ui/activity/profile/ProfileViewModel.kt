package com.midokter.app.ui.activity.profile

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.midokter.app.base.BaseViewModel
import com.midokter.app.repositary.AppRepository
import com.midokter.app.repositary.model.ProfileResponse

class ProfileViewModel : BaseViewModel<ProfileNavigator>(){
    private val appRepository = AppRepository.instance()
    var mProfileResponse = MutableLiveData<ProfileResponse>()
    var name : ObservableField<String> = ObservableField("")
    var number : ObservableField<String> = ObservableField("")
    var email : ObservableField<String> = ObservableField("")
    var gender : ObservableField<String> = ObservableField("")
    var dob : ObservableField<String> = ObservableField("")
    var bloodgroup : ObservableField<String> = ObservableField("")
    var marital : ObservableField<String> = ObservableField("")
    var height : ObservableField<String> = ObservableField("")
    var weight : ObservableField<String> = ObservableField("")
    var emgcontact : ObservableField<String> = ObservableField("")
    var location : ObservableField<String> = ObservableField("")

    var allergies : ObservableField<String> = ObservableField("")
    var current_medications : ObservableField<String> = ObservableField("")
    var past_medications : ObservableField<String> = ObservableField("")
    var chronic_diseases : ObservableField<String> = ObservableField("")
    var injuries : ObservableField<String> = ObservableField("")
    var surgeries : ObservableField<String> = ObservableField("")

    var smoking : ObservableField<String> = ObservableField("")
    var alcohol : ObservableField<String> = ObservableField("")
    var activity : ObservableField<String> = ObservableField("")
    var food : ObservableField<String> = ObservableField("")
    var occupation : ObservableField<String> = ObservableField("")

    fun getprofile() {

        getCompositeDisposable().add(appRepository.getProfile(this))
    }
}