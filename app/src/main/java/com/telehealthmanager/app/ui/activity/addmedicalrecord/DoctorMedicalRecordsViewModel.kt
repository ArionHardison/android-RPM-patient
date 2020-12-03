package com.telehealthmanager.app.ui.activity.addmedicalrecord

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.telehealthmanager.app.BaseApplication
import com.telehealthmanager.app.base.BaseViewModel
import com.telehealthmanager.app.data.PreferenceHelper
import com.telehealthmanager.app.data.PreferenceKey
import com.telehealthmanager.app.data.getValue
import com.telehealthmanager.app.repositary.AppRepository
import com.telehealthmanager.app.repositary.model.MedicalRecord
import com.telehealthmanager.app.repositary.model.ResponseDoctors
import com.telehealthmanager.app.repositary.model.ResponseMedicalDetails
import com.telehealthmanager.app.repositary.model.ResponsePrescription
import com.telehealthmanager.app.utils.ViewUtils
import okhttp3.MultipartBody
import okhttp3.RequestBody

class DoctorMedicalRecordsViewModel : BaseViewModel<DoctorMedicalRecordsNavigator>() {

    private val appRepository = AppRepository.instance()
    var mMedicalRecordResponse = MutableLiveData<MedicalRecord>()
    var mAddResponse = MutableLiveData<ResponsePrescription>()
    var mResponseDoctors = MutableLiveData<ResponseDoctors>()
    var mDoctorsList: MutableList<ResponseDoctors.AllDoctors>? = arrayListOf()
    var mResponseMedicalDetails = MutableLiveData<ResponseMedicalDetails>()
    var mRecordDetailList: MutableList<ResponseMedicalDetails.RecordDetail>? = arrayListOf()

    private val preferenceHelper = PreferenceHelper(BaseApplication.baseApplication)
    var loadingProgress = MutableLiveData<Boolean>()

    //TODO ADD RECORD
    var mDoctorName: ObservableField<String> = ObservableField("")
    var mTestTitle: ObservableField<String> = ObservableField("")
    var mTestDescription: ObservableField<String> = ObservableField("")
    var mSelectedDoctor: ObservableField<String> = ObservableField("0")

    var mViewFile: ObservableField<String> = ObservableField("")

    fun addMedicalRecord(fileBody: MultipartBody.Part) {
        val hashMap: HashMap<String, RequestBody> = HashMap()
        hashMap["doctor_id"] = ViewUtils.convertRequestBody(mSelectedDoctor.get().toString().trim())
        hashMap["patient_id"] = ViewUtils.convertRequestBody(preferenceHelper.getValue(PreferenceKey.PATIENT_ID, 0).toString())
        hashMap["title"] = ViewUtils.convertRequestBody(mTestTitle.get().toString().trim())
        hashMap["instruction"] = ViewUtils.convertRequestBody(mTestDescription.get().toString().trim())
        hashMap["date"] = ViewUtils.convertRequestBody(ViewUtils.getCurrentDateTime())
        appRepository.addMedicalRecord(this, hashMap, fileBody)
    }

    fun getDoctorsList() {
        appRepository.getAllDoc(this)
    }

    fun getRecordsList(recordID: String) {
        appRepository.getMedicalRecordsList(this, recordID)
    }
}