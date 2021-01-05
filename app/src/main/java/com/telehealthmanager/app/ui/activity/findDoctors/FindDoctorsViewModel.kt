package com.telehealthmanager.app.ui.activity.findDoctors

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.telehealthmanager.app.base.BaseViewModel
import com.telehealthmanager.app.repositary.AppRepository
import com.telehealthmanager.app.repositary.model.BookedResponse
import com.telehealthmanager.app.repositary.model.CategoryResponse
import com.telehealthmanager.app.repositary.model.DoctorListResponse
import com.telehealthmanager.app.repositary.pagination.FindDoctorSource
import java.util.*
import kotlin.collections.HashMap

class FindDoctorsViewModel : BaseViewModel<FindDoctorsNavigator>() {
    private val TAG = "FindDoctorsViewModel"

    var mCategoryResponse = MutableLiveData<CategoryResponse>()
    var mCategoryList: MutableList<CategoryResponse.Category>? = arrayListOf()
    var mFirstCategoryList: MutableList<CategoryResponse.Category>? = arrayListOf()
    var selectedDate: Calendar? = Calendar.getInstance()
    var mSelectedScheduleDate: ObservableField<String> = ObservableField("")
    var mCategoryId: ObservableField<Int> = ObservableField(0)
    var mDoctorResponse = MutableLiveData<DoctorListResponse>()
    var mDoctorList: MutableList<DoctorListResponse.specialities.DoctorProfile>? = arrayListOf()

    val queryLiveData: MutableLiveData<String> = MutableLiveData<String>("")

    private val appRepository = AppRepository.instance()
    var mBookedResponse = MutableLiveData<BookedResponse>()

    fun getCategorys() {
        getCompositeDisposable().add(appRepository.getCategorys(this))
    }

    fun getDoctorByCategorys(id: Int, hashMap: HashMap<String, Any>) {
        getCompositeDisposable().add(appRepository.getDoctorByCategorys(this, id, hashMap))
    }

    fun getDoctorFilterCategories(hashMap: HashMap<String, Any>) {
        getCompositeDisposable().add(appRepository.getFiltersDoctors(this, mCategoryId.get()!!.toInt(), hashMap))
    }


    fun clickSearchDoctors() {
        navigator.openSearchDoctors()
    }

    val doctorsList = Pager(PagingConfig(pageSize = 10)) {
        FindDoctorSource(appRepository,  this)
    }.flow.cachedIn(viewModelScope)
}