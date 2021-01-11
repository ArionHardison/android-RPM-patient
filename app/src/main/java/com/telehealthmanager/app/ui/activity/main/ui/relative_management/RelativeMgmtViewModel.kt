package com.telehealthmanager.app.ui.activity.main.ui.relative_management

import androidx.lifecycle.MutableLiveData
import com.telehealthmanager.app.BaseApplication
import com.telehealthmanager.app.base.BaseViewModel
import com.telehealthmanager.app.data.PreferenceHelper
import com.telehealthmanager.app.data.PreferenceKey
import com.telehealthmanager.app.data.getValue
import com.telehealthmanager.app.repositary.AppRepository
import com.telehealthmanager.app.repositary.model.RelativeList
import com.telehealthmanager.app.repositary.model.RelativeResponse

class RelativeMgmtViewModel : BaseViewModel<RelativeMgmtNavigator>() {
    private val appRepository = AppRepository.instance()
    var loadingProgress = MutableLiveData<Boolean>()
    var mRelativeResponse = MutableLiveData<RelativeResponse>()
    var mRelativeList: MutableList<RelativeList>? = arrayListOf()

    private val preferenceHelper = PreferenceHelper(BaseApplication.baseApplication)

    fun clickAddRelative() {
        navigator.openAddRelative()
    }

    fun initApi() {
        loadingProgress.value = true
        val hashMap: HashMap<String, Any> = HashMap()
        hashMap["patient_id"] = preferenceHelper.getValue(PreferenceKey.PATIENT_ID, 1).toString()
        appRepository.repositoryRelativeLists(this, hashMap)
    }

}