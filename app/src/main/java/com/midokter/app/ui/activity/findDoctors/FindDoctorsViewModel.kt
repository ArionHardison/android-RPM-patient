package com.midokter.app.ui.activity.findDoctors

import androidx.lifecycle.MutableLiveData
import com.midokter.app.base.BaseViewModel
import com.midokter.app.repositary.AppRepository
import com.midokter.app.repositary.model.CategoryResponse
import com.midokter.app.repositary.model.DoctorListResponse

class FindDoctorsViewModel : BaseViewModel<FindDoctorsNavigator>(){
    var mCategoryResponse = MutableLiveData<CategoryResponse>()
    var mCategoryslist: MutableList<CategoryResponse.Category>? = arrayListOf()

    var mDoctorResponse = MutableLiveData<DoctorListResponse>()
    var mDoctorslist: MutableList<DoctorListResponse.specialities.DoctorProfile>? = arrayListOf()
    private val appRepository = AppRepository.instance()

    fun getCategorys() {

        getCompositeDisposable().add(appRepository.getCategorys(this))
    }
    fun getDoctorByCategorys( id: Int) {

        getCompositeDisposable().add(appRepository.getDoctorByCategorys(this,id))
    }
}