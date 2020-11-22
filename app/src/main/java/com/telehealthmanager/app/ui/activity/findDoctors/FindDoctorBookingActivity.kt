package com.telehealthmanager.app.ui.activity.findDoctors

import android.app.TimePickerDialog
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.TimePicker
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.telehealthmanager.app.BaseApplication
import com.telehealthmanager.app.BuildConfig
import com.telehealthmanager.app.R
import com.telehealthmanager.app.base.BaseActivity
import com.telehealthmanager.app.data.PreferenceHelper
import com.telehealthmanager.app.data.PreferenceKey
import com.telehealthmanager.app.data.getValue
import com.telehealthmanager.app.data.setValue
import com.telehealthmanager.app.databinding.ActivityFindDoctorBookingBinding
import com.telehealthmanager.app.repositary.model.BookedResponse
import com.telehealthmanager.app.ui.activity.patientDetail.PatientDetailsActivity
import com.telehealthmanager.app.ui.calander_view.Tools
import com.telehealthmanager.app.utils.CustomBackClick
import com.telehealthmanager.app.utils.ViewUtils
import java.lang.String
import java.text.SimpleDateFormat
import java.util.*


class FindDoctorBookingActivity : BaseActivity<ActivityFindDoctorBookingBinding>(),
    FindDoctorsNavigator, TimePickerDialog.OnTimeSetListener, CustomBackClick {

    private val preferenceHelper = PreferenceHelper(BaseApplication.baseApplication)
    private lateinit var viewModel: FindDoctorsViewModel
    private lateinit var mDataBinding: ActivityFindDoctorBookingBinding
    val bookDoctor_Map: HashMap<kotlin.String, Any> = HashMap()
    val sdf1 = SimpleDateFormat("yyyy-MM-dd")

    override fun getLayoutId(): Int = R.layout.activity_find_doctor_booking

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mDataBinding = mViewDataBinding as ActivityFindDoctorBookingBinding
        viewModel = ViewModelProviders.of(this).get(FindDoctorsViewModel::class.java)
        mDataBinding.viewmodel = viewModel
        viewModel.navigator = this
        observeResponse()

        mDataBinding.searchDocName.text =
            preferenceHelper.getValue(PreferenceKey.SELECTED_DOC_NAME, "").toString()
        if (preferenceHelper.getValue(PreferenceKey.SELECTED_DOC_Special, "").toString().isEmpty())
            mDataBinding.searchDocSpec.visibility = View.GONE
        mDataBinding.searchDocSpec.text =
            preferenceHelper.getValue(PreferenceKey.SELECTED_DOC_Special, "").toString()
        mDataBinding.searchDocHospName.text =
            preferenceHelper.getValue(PreferenceKey.SELECTED_DOC_ADDRESS, "").toString()
        Glide.with(this@FindDoctorBookingActivity)
            .load(
                BuildConfig.BASE_IMAGE_URL + preferenceHelper.getValue(
                    PreferenceKey.SELECTED_DOC_IMAGE,
                    ""
                ).toString()
            )
            .placeholder(R.drawable.doc_place_holder)
            .error(R.drawable.doc_place_holder)
            .fallback(R.drawable.doc_place_holder)
            .into(mDataBinding.searchDocImg)


        val starttime = Calendar.getInstance()
        starttime.add(Calendar.DATE, 0)

        val endtime = Calendar.getInstance()
        endtime.add(Calendar.MONTH, 6)

        val datesToBeColored: ArrayList<kotlin.String> = ArrayList()
        datesToBeColored.add(Tools.getFormattedDateToday())
        viewModel.mSelectedScheduleDate.set(sdf1.format(System.currentTimeMillis()))
        mDataBinding.calanderView.setUpCalendar(starttime.timeInMillis, endtime.timeInMillis, datesToBeColored) { date, strDate ->
            viewModel.mSelectedScheduleDate.set(strDate)
            viewModel.selectedDate = date
            Log.e("mSelectedSchedul==>", "" + viewModel.mSelectedScheduleDate.get().toString())
        }
        Log.e("mSelectedSchedul==>", "" + viewModel.mSelectedScheduleDate.get().toString())

        mDataBinding.button17.setOnClickListener {
            var hour: Int = 0
            var minute: Int = 0
            val calendar: Calendar = Calendar.getInstance()
            hour = calendar.get(Calendar.HOUR_OF_DAY)
            minute = calendar.get(Calendar.MINUTE)
            val timePickerDialog = TimePickerDialog(this, this@FindDoctorBookingActivity, hour, minute, false)
            timePickerDialog.show()
        }

        viewModel.setOnClickListener(this@FindDoctorBookingActivity)
        viewModel.toolBarTile.value = getString(R.string.booking)
    }

    override fun clickBackPress() {
        finish()
    }


    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        viewModel.selectedDate!!.set(Calendar.HOUR_OF_DAY, hourOfDay)
        viewModel.selectedDate!!.set(Calendar.MINUTE, minute)
        val currentTime = Calendar.getInstance()
        currentTime.add(Calendar.MINUTE, -1)
        if (viewModel.selectedDate!!.getTimeInMillis() < currentTime.timeInMillis) {
            ViewUtils.showToast(this@FindDoctorBookingActivity, getString(R.string.past_time_error), false)
            return
        }

        val selectedHour = if (hourOfDay < 10) "0" + String.valueOf(hourOfDay) else String.valueOf(hourOfDay)
        val selectedMinutes = if (minute < 10) "0" + String.valueOf(minute) else String.valueOf(minute)

        preferenceHelper.setValue(PreferenceKey.SCHEDULED_DATE, viewModel.mSelectedScheduleDate.get().toString() + " " + selectedHour + ":" + selectedMinutes + ":" + "00")
        if (mDataBinding.radioButton.isChecked) {
            preferenceHelper.setValue(PreferenceKey.VISIT_PURPOSE, mDataBinding.radioButton.text.toString())
        } else {
            preferenceHelper.setValue(PreferenceKey.VISIT_PURPOSE, mDataBinding.radioButton2.text.toString())
        }
        performclick()
    }

    private fun observeResponse() {
        viewModel.mBookedResponse.observe(this@FindDoctorBookingActivity, Observer<BookedResponse> {
            loadingObservable.value = false
            if (it.status) {
                val intent =
                    Intent(this@FindDoctorBookingActivity, PatientDetailsActivity::class.java)
                startActivity(intent);

            } else
                ViewUtils.showToast(this@FindDoctorBookingActivity, it.message, false)
        })
        viewModel.getErrorObservable().observe(this, Observer<kotlin.String> { message ->
            loadingObservable.value = false
            ViewUtils.showToast(this@FindDoctorBookingActivity, message, false)
        })
    }

    fun performclick() {
        // loadingObservable.value = true
        bookDoctor_Map["doctor_id"] = preferenceHelper.getValue(PreferenceKey.SELECTED_DOC_ID, "").toString()
        bookDoctor_Map["selectedPatient"] = preferenceHelper.getValue(PreferenceKey.PATIENT_ID, 0).toString()
        bookDoctor_Map["booking_for"] = preferenceHelper.getValue(PreferenceKey.VISIT_PURPOSE, "").toString()
        bookDoctor_Map["scheduled_at"] = preferenceHelper.getValue(PreferenceKey.SCHEDULED_DATE, "").toString()
        bookDoctor_Map["consult_time"] = "15"
        bookDoctor_Map["appointment_type"] = "OFFLINE"
        bookDoctor_Map["description"] = ""

        // loadingObservable.value = true
        // viewModel.BookDoctor(bookDoctor_Map)
        val intent = Intent(this@FindDoctorBookingActivity, PatientDetailsActivity::class.java)
        startActivity(intent);
    }

}
