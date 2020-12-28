package com.telehealthmanager.app.ui.activity.findDoctors

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.telehealthmanager.app.base.BaseViewModel
import com.telehealthmanager.app.repositary.AppRepository
import com.telehealthmanager.app.repositary.model.BookedResponse
import com.telehealthmanager.app.repositary.model.CategoryResponse
import com.telehealthmanager.app.repositary.model.DoctorListResponse
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

    private val VISIBLE_THRESHOLD = 5

    fun listScrolled(visibleItemCount: Int, lastVisibleItem: Int, totalItemCount: Int, item: DoctorListResponse.specialities.DoctorProfile) {
        if ((visibleItemCount + lastVisibleItem + VISIBLE_THRESHOLD >= totalItemCount)) {
            Log.d(TAG, "listScrolled: True $totalItemCount LAST ITEM $lastVisibleItem VISIBLE COUNT $visibleItemCount DOCTOR ID ${item.id}")
            val hashMap: HashMap<String, Any> = HashMap()
            hashMap["page"] = item.id
            if (queryLiveData.value.toString() != "") {
                hashMap.put("search", queryLiveData.value.toString())
                getDoctorByCategorys(mCategoryId.get()!!.toInt(), hashMap)
            } else {
                getDoctorByCategorys(mCategoryId.get()!!.toInt(), hashMap)
            }
        }
    }
}