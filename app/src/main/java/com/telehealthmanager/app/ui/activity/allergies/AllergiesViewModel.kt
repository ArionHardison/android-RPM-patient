package com.telehealthmanager.app.ui.activity.allergies

import androidx.lifecycle.MutableLiveData
import com.telehealthmanager.app.base.BaseViewModel
import com.telehealthmanager.app.repositary.AppRepository
import com.telehealthmanager.app.repositary.model.ProfileResponse
import com.telehealthmanager.app.repositary.model.RelativeList

class AllergiesViewModel :BaseViewModel<AllergiesNavigator>() {
    private val appRepository = AppRepository.instance()
    var mProfileResponse = MutableLiveData<ProfileResponse>()
    var mAllergiesList: MutableList<ProfileResponse.Allergies>? = arrayListOf()

    fun getProfile() {
        getCompositeDisposable().add(appRepository.getProfile(this))
    }

    fun clickAddAllergies(){
        navigator.openAddAllergies()
    }

    fun clickNoAllergies(){
        navigator.noAllergies()
    }
}