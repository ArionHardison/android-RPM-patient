package com.telehealthmanager.app.ui.activity.searchGlobal

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.telehealthmanager.app.base.BaseViewModel
import com.telehealthmanager.app.repositary.AppRepository
import com.telehealthmanager.app.repositary.model.SearchResponse

class SearchGlobalViewModel : BaseViewModel<SearchGlobalNavigator>() {
    private val appRepository = AppRepository.instance()
    var loadingProgress = MutableLiveData<Boolean>()

    var mResponse = MutableLiveData<SearchResponse>()
    var mCategoryList: MutableList<SearchResponse.Specialities>? = arrayListOf()
    var mClinicList: MutableList<SearchResponse.Clinic>? = arrayListOf()
    var search: ObservableField<String> = ObservableField("")

    fun getGlobalSearch(hashMap: HashMap<String, Any>) {
        getCompositeDisposable().add(appRepository.repositoryGlobalSearch(this, hashMap))
    }
}