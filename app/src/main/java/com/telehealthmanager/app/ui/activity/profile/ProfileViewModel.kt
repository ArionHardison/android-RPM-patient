package com.telehealthmanager.app.ui.activity.profile

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.telehealthmanager.app.base.BaseViewModel
import com.telehealthmanager.app.repositary.AppRepository
import com.telehealthmanager.app.repositary.WebApiConstants
import com.telehealthmanager.app.repositary.model.AddUpdateRelative
import com.telehealthmanager.app.repositary.model.ProfileResponse
import com.telehealthmanager.app.repositary.model.RelativeResponse
import com.telehealthmanager.app.utils.ViewUtils
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ProfileViewModel : BaseViewModel<ProfileNavigator>() {
    private val appRepository = AppRepository.instance()
    var mProfileResponse = MutableLiveData<ProfileResponse>()
    var mAddRelativeResponse = MutableLiveData<AddUpdateRelative>()
    var mRelativeResponse = MutableLiveData<RelativeResponse>()
    var mEditPatientResponse = MutableLiveData<ProfileResponse>()

    var firstName: ObservableField<String> = ObservableField("")
    var patientId: ObservableField<String> = ObservableField()
    var id: ObservableField<Int> = ObservableField()
    var lastName: ObservableField<String> = ObservableField("")
    var number: ObservableField<String> = ObservableField("")
    var email: ObservableField<String> = ObservableField("")
    var gender: ObservableField<String> = ObservableField("")
    var dob: ObservableField<String> = ObservableField("")
    var bloodgroup: ObservableField<String> = ObservableField("")
    var marital: ObservableField<String> = ObservableField("")
    var height: ObservableField<String> = ObservableField("")
    var weight: ObservableField<String> = ObservableField("")
    var emgcontact: ObservableField<String> = ObservableField("")
    var location: ObservableField<String> = ObservableField("")

    var allergies: ObservableField<String> = ObservableField("")
    var current_medications: ObservableField<String> = ObservableField("")
    var past_medications: ObservableField<String> = ObservableField("")
    var chronic_diseases: ObservableField<String> = ObservableField("")
    var injuries: ObservableField<String> = ObservableField("")
    var surgeries: ObservableField<String> = ObservableField("")

    var smoking: ObservableField<String> = ObservableField("")
    var alcohol: ObservableField<String> = ObservableField("")
    var activity: ObservableField<String> = ObservableField("")
    var food: ObservableField<String> = ObservableField("")
    var occupation: ObservableField<String> = ObservableField("")
    var relativeId: ObservableField<String> = ObservableField("")
    var viewType: ObservableField<String> = ObservableField("")

    var updateText: ObservableField<String> = ObservableField("")

    fun getProfile() {
        getCompositeDisposable().add(appRepository.repositoryProfile(this))
    }

    fun openAllergies() {
        navigator.onClickAllergies()
    }

    fun openLocation() {
        navigator.onClickLocation()
    }


    fun choiceMartial() {
        navigator.onClickMartial()
    }

    fun choiceGender() {
        navigator.onClickGender()
    }

    fun updatePatient() {
        getCompositeDisposable().add(appRepository.editPatientApi(this, requestWithOutImage()))
    }

    fun updatePatientWithImage(fileBody: MultipartBody.Part) {
        getCompositeDisposable().add(appRepository.editPatientWithImageApi(id.get()!!, this, requestWithImage(), fileBody))
    }


    fun getRelateProfile() {
        getCompositeDisposable().add(appRepository.getRelativePatientApi(this, relativeId.get().toString()))
    }


    fun addRelative() {
        val hashMap: HashMap<String, Any> = requestWithOutImage()
        hashMap["patient_id"] = patientId.get().toString()
        getCompositeDisposable().add(
            appRepository.addRelativePatientApi(
                this,
                hashMap
            )
        )
    }

    fun addRelativeWithImage(fileBody: MultipartBody.Part?) {
        val hashMap: HashMap<String, RequestBody> = requestWithImage()
        hashMap["patient_id"] = ViewUtils.convertRequestBody(patientId.get().toString())
        getCompositeDisposable().add(
            appRepository.addRelativePatientApi(
                this,
                hashMap,
                fileBody!!
            )
        )
    }


    fun updateRelative() {
        getCompositeDisposable().add(
            appRepository.updateRelativePatientApi(
                this,
                relativeId.get()!!.toString(),
                requestWithOutImage()
            )
        )
    }

    fun updateRelativeWithImage(fileBody: MultipartBody.Part?) {
        getCompositeDisposable().add(
            appRepository.updateRelativePatientApi(
                this,
                relativeId.get()!!.toString(),
                requestWithImage(),
                fileBody!!
            )
        )
    }

    private fun requestWithOutImage(): HashMap<String, Any> {
        val hashMap: HashMap<String, Any> = HashMap()
        hashMap[WebApiConstants.EditPatient.FIRST_NAME] = firstName.get()!!
        hashMap[WebApiConstants.EditPatient.LAST_NAME] = lastName.get()!!
        hashMap[WebApiConstants.EditPatient.PHONE] = number.get()!!
        hashMap[WebApiConstants.EditPatient.EMAIL] = email.get()!!
        hashMap[WebApiConstants.EditPatient.GENDER] = gender.get()!!
        hashMap[WebApiConstants.EditPatient.DOB] = dob.get()!!
        hashMap[WebApiConstants.EditPatient.BLOOD_GROUP] = bloodgroup.get()!!
        hashMap[WebApiConstants.EditPatient.MERITAL_STATUS] = marital.get()!!
        hashMap[WebApiConstants.EditPatient.HEIGHT] = height.get()!!
        hashMap[WebApiConstants.EditPatient.WEIGHT] = weight.get()!!
        hashMap[WebApiConstants.EditPatient.EMERGENCY_CONTACT] = emgcontact.get()!!
        hashMap[WebApiConstants.EditPatient.LOCATION] = location.get()!!
        hashMap[WebApiConstants.EditPatient.ALLERGIES] = allergies.get()!!
        hashMap[WebApiConstants.EditPatient.CURRENT_MEDICATIONS] = current_medications.get()!!
        hashMap[WebApiConstants.EditPatient.PAST_MEDICATIONS] = past_medications.get()!!
        hashMap[WebApiConstants.EditPatient.CHRONIC_DISEASES] = chronic_diseases.get()!!
        hashMap[WebApiConstants.EditPatient.INJURIES] = injuries.get()!!
        hashMap[WebApiConstants.EditPatient.SURGERIES] = surgeries.get()!!
        hashMap[WebApiConstants.EditPatient.SMOKING] = smoking.get()!!
        hashMap[WebApiConstants.EditPatient.ALCOHOL] = alcohol.get()!!
        hashMap[WebApiConstants.EditPatient.ACTIVITY] = activity.get()!!
        hashMap[WebApiConstants.EditPatient.FOOD] = food.get()!!
        hashMap[WebApiConstants.EditPatient.OCCUPATION] = occupation.get()!!
        return hashMap
    }

    private fun requestWithImage(): HashMap<String, RequestBody> {
        val hashMap: HashMap<String, RequestBody> = HashMap()
        hashMap[WebApiConstants.EditPatient.FIRST_NAME] = ViewUtils.convertRequestBody(firstName.get()!!)
        hashMap[WebApiConstants.EditPatient.LAST_NAME] = ViewUtils.convertRequestBody(lastName.get()!!)
        hashMap[WebApiConstants.EditPatient.PHONE] = ViewUtils.convertRequestBody(number.get()!!)
        hashMap[WebApiConstants.EditPatient.EMAIL] = ViewUtils.convertRequestBody(email.get()!!)
        hashMap[WebApiConstants.EditPatient.GENDER] = ViewUtils.convertRequestBody(gender.get()!!)
        hashMap[WebApiConstants.EditPatient.DOB] = ViewUtils.convertRequestBody(dob.get()!!)
        hashMap[WebApiConstants.EditPatient.BLOOD_GROUP] = ViewUtils.convertRequestBody(bloodgroup.get()!!)
        hashMap[WebApiConstants.EditPatient.MERITAL_STATUS] = ViewUtils.convertRequestBody(marital.get()!!)
        hashMap[WebApiConstants.EditPatient.HEIGHT] = ViewUtils.convertRequestBody(height.get()!!)
        hashMap[WebApiConstants.EditPatient.WEIGHT] = ViewUtils.convertRequestBody(weight.get()!!)
        hashMap[WebApiConstants.EditPatient.EMERGENCY_CONTACT] = ViewUtils.convertRequestBody(emgcontact.get()!!)
        hashMap[WebApiConstants.EditPatient.LOCATION] = ViewUtils.convertRequestBody(location.get()!!)
        hashMap[WebApiConstants.EditPatient.ALLERGIES] = ViewUtils.convertRequestBody(allergies.get()!!)
        hashMap[WebApiConstants.EditPatient.CURRENT_MEDICATIONS] = ViewUtils.convertRequestBody(current_medications.get()!!)
        hashMap[WebApiConstants.EditPatient.PAST_MEDICATIONS] = ViewUtils.convertRequestBody(past_medications.get()!!)
        hashMap[WebApiConstants.EditPatient.CHRONIC_DISEASES] = ViewUtils.convertRequestBody(chronic_diseases.get()!!)
        hashMap[WebApiConstants.EditPatient.INJURIES] = ViewUtils.convertRequestBody(injuries.get()!!)
        hashMap[WebApiConstants.EditPatient.SURGERIES] = ViewUtils.convertRequestBody(surgeries.get()!!)
        hashMap[WebApiConstants.EditPatient.SMOKING] = ViewUtils.convertRequestBody(smoking.get()!!)
        hashMap[WebApiConstants.EditPatient.ALCOHOL] = ViewUtils.convertRequestBody(alcohol.get()!!)
        hashMap[WebApiConstants.EditPatient.ACTIVITY] = ViewUtils.convertRequestBody(activity.get()!!)
        hashMap[WebApiConstants.EditPatient.FOOD] = ViewUtils.convertRequestBody(food.get()!!)
        hashMap[WebApiConstants.EditPatient.OCCUPATION] = ViewUtils.convertRequestBody(occupation.get()!!)
        return hashMap
    }
}