package com.midokter.app.ui.activity.addreminder

import android.widget.CompoundButton
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.midokter.app.base.BaseViewModel
import com.midokter.app.extensions.default
import com.midokter.app.repositary.AppRepository
import com.midokter.app.repositary.WebApiConstants
import com.midokter.app.repositary.model.AddRemainderResponse

class AddReminderViewModel : BaseViewModel<AddReminderNavigator>(){
    private val appRepository = AppRepository.instance()
    var name : ObservableField<String> = ObservableField("")
    var id : ObservableField<Int> = ObservableField(0)
    var patientId : ObservableField<Int> = ObservableField(0)
    var fromDate = MutableLiveData<String>().default("")
    var displayFromDate: ObservableField<String> = ObservableField("")
    var fromTime = MutableLiveData<String>().default("")
    var displayfromTime: ObservableField<String> = ObservableField("")
    var alarm : ObservableField<Boolean> = ObservableField(false)
    var notifyme : ObservableField<Boolean> = ObservableField(false)
    var alarmValue : ObservableField<Int> = ObservableField(0)
    var notifymeValue : ObservableField<Int> = ObservableField(0)

    var mAddRemainderResponse = MutableLiveData<AddRemainderResponse>()
    var loadingProgress = MutableLiveData<Boolean>()

    fun onSubmitClicked() {
        navigator.validateAddRemainder()
    }
    fun onBackButtonClicked() {
        navigator.onBackButtonClicked()
    }

    fun addReminderAPI() {
        val hashMap: HashMap<String, Any> = HashMap()
        hashMap[WebApiConstants.AddRemainder.NAME] = name.get()!!
        hashMap[WebApiConstants.AddRemainder.DATE] = fromDate.value!!
        hashMap[WebApiConstants.AddRemainder.TIME] = fromTime.value!!
        hashMap[WebApiConstants.AddRemainder.ALARM] = alarmValue.get()!!
        hashMap[WebApiConstants.AddRemainder.NOTIFY_ME] = notifymeValue.get()!!
        loadingProgress.value = true
        getCompositeDisposable().add(appRepository.addRemainder(this, hashMap))
    }


    fun onFromTimeClicked() {
        navigator.onFromTimeClicked()
    }

    fun onAlarmChanged(buttonView: CompoundButton, isChecked: Boolean) {
        alarm.set(isChecked)
        if(isChecked)
            alarmValue.set(1)
        else
            alarmValue.set(0)

    }

    fun onNotifyMeChanged(buttonView: CompoundButton, isChecked: Boolean) {
        if(isChecked)
            notifymeValue.set(1)
        else
            notifymeValue.set(0)
        notifyme.set(isChecked)
    }

    fun onFromDateClicked() {
        navigator.onFromDateClicked()
    }
}