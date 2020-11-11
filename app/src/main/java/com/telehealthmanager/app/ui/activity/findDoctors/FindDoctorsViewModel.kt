package com.telehealthmanager.app.ui.activity.findDoctors

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.telehealthmanager.app.base.BaseViewModel
import com.telehealthmanager.app.repositary.AppRepository
import com.telehealthmanager.app.repositary.model.BookedResponse
import com.telehealthmanager.app.repositary.model.CategoryResponse
import com.telehealthmanager.app.repositary.model.DoctorListResponse
import java.text.SimpleDateFormat
import java.util.*

class FindDoctorsViewModel : BaseViewModel<FindDoctorsNavigator>() {
    var mCategoryResponse = MutableLiveData<CategoryResponse>()
    var mCategoryList: MutableList<CategoryResponse.Category>? = arrayListOf()
    var mFirstCategoryList: MutableList<CategoryResponse.Category>? = arrayListOf()

    var selectedDate: Calendar? = Calendar.getInstance()
    var mYearMonth: ObservableField<String> = ObservableField(SimpleDateFormat("MMMM yyyy").format(selectedDate!!.time))
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

    fun BookDoctor(hashMap: HashMap<String, Any>) {
        getCompositeDisposable().add(appRepository.bookdoctor(this, hashMap))
    }
}