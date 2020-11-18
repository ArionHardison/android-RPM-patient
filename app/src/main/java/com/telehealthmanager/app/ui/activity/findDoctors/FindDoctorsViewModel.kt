package com.telehealthmanager.app.ui.activity.findDoctors

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.telehealthmanager.app.base.BaseViewModel
import com.telehealthmanager.app.repositary.AppRepository
import com.telehealthmanager.app.repositary.model.BookedResponse
import com.telehealthmanager.app.repositary.model.CategoryResponse
import com.telehealthmanager.app.repositary.model.DoctorListResponse
import java.util.*

class FindDoctorsViewModel : BaseViewModel<FindDoctorsNavigator>() {
    var mCategoryResponse = MutableLiveData<CategoryResponse>()
    var mCategoryList: MutableList<CategoryResponse.Category>? = arrayListOf()
    var mFirstCategoryList: MutableList<CategoryResponse.Category>? = arrayListOf()
    var selectedDate: Calendar? = Calendar.getInstance()
    var mSelectedScheduleDate: ObservableField<String> = ObservableField("")
    var mCategoryId: ObservableField<Int> = ObservableField(0)
    var mDoctorResponse = MutableLiveData<DoctorListResponse>()
    var mDoctorList: MutableList<DoctorListResponse.specialities.DoctorProfile>? = arrayListOf()

    private val appRepository = AppRepository.instance()
    var mBookedResponse = MutableLiveData<BookedResponse>()
    fun getCategorys() {
        getCompositeDisposable().add(appRepository.getCategorys(this))
    }

    fun getDoctorByCategorys(id: Int) {
        getCompositeDisposable().add(appRepository.getDoctorByCategorys(this, id))
    }

    fun getDoctorFilterCategories(hashMap: HashMap<String, Any>) {
        getCompositeDisposable().add(appRepository.getFiltersDoctors(this, mCategoryId.get()!!.toInt(), hashMap))
    }

    fun BookDoctor(hashMap: HashMap<String, Any>) {
        getCompositeDisposable().add(appRepository.bookdoctor(this, hashMap))
    }
}