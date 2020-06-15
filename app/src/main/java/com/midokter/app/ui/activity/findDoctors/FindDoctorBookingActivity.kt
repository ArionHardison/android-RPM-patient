package com.midokter.app.ui.activity.findDoctors

import android.app.TimePickerDialog
import android.content.Intent
import android.text.format.DateFormat
import android.widget.TimePicker
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.midokter.app.BaseApplication
import com.midokter.app.BuildConfig
import com.midokter.app.R
import com.midokter.app.base.BaseActivity
import com.midokter.app.data.PreferenceHelper
import com.midokter.app.data.PreferenceKey
import com.midokter.app.data.getValue
import com.midokter.app.data.setValue
import com.midokter.app.databinding.ActivityFindDoctorBookingBinding
import com.midokter.app.repositary.model.BookedResponse
import com.midokter.app.ui.activity.patientDetail.PatientDetailsActivity
import com.midokter.app.utils.ViewUtils
import devs.mulham.horizontalcalendar.HorizontalCalendar
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener
import kotlinx.android.synthetic.main.activity_find_doctor_booking.*
import java.lang.String
import java.text.SimpleDateFormat
import java.util.*
import androidx.lifecycle.Observer


class FindDoctorBookingActivity : BaseActivity<ActivityFindDoctorBookingBinding>(),
    FindDoctorsNavigator, TimePickerDialog.OnTimeSetListener {

    private val preferenceHelper = PreferenceHelper(BaseApplication.baseApplication)
    private var horizontalCalendar: HorizontalCalendar? = null
    private lateinit var viewModel: FindDoctorsViewModel
    private lateinit var mDataBinding: ActivityFindDoctorBookingBinding
    val bookDoctor_Map: HashMap<kotlin.String, Any> = HashMap()

    override fun getLayoutId(): Int = R.layout.activity_find_doctor_booking

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mDataBinding = mViewDataBinding as ActivityFindDoctorBookingBinding
        viewModel = ViewModelProviders.of(this).get(FindDoctorsViewModel::class.java)
        mDataBinding.viewmodel = viewModel
        viewModel.navigator = this
        observeResponse()
        mDataBinding.toolbar7.setNavigationOnClickListener {
            finish()
        }
mDataBinding.searchDocName.text = preferenceHelper.getValue(PreferenceKey.SELECTED_DOC_NAME,"Dr test").toString()
        mDataBinding.searchDocSpec.text = preferenceHelper.getValue(PreferenceKey.SELECTED_DOC_Special,"Dr test").toString()
        mDataBinding.searchDocHospName.text = preferenceHelper.getValue(PreferenceKey.SELECTED_DOC_ADDRESS,"Dr test").toString()
        Glide.with(this@FindDoctorBookingActivity)
            .load(BuildConfig.BASE_IMAGE_URL +  preferenceHelper.getValue(PreferenceKey.SELECTED_DOC_IMAGE,"Dr test").toString())
            .placeholder(R.drawable.user_placeholder)
            .error(R.drawable.user_placeholder)
            .fallback(R.drawable.user_placeholder)
            .into(mDataBinding.searchDocImg)


        val endDate: Calendar = Calendar.getInstance()
        endDate.add(Calendar.MONTH, 1)
        val startDate: Calendar = Calendar.getInstance()
        startDate.add(Calendar.MONTH, -1)

        horizontalCalendar =
            HorizontalCalendar.Builder(this, R.id.calendarView)
                .range(startDate, endDate)
                .configure().showTopText(false).end()
                .datesNumberOnScreen(7)
                .build()

        horizontalCalendar!!.setCalendarListener(object : HorizontalCalendarListener() {
            override fun onDateSelected(date: Calendar?, position: Int) {
                if (isBeforeToday(date!!)) {
                    mViewDataBinding.button17.setAlpha(.5f);
                    mViewDataBinding.button17.setClickable(false);
                    mViewDataBinding.button17.setAlpha(.5f);
                    mViewDataBinding.button17.setClickable(false);
                } else {
                    mViewDataBinding.button17.setAlpha(1f);
                    mViewDataBinding.button17.setClickable(true)
                    mViewDataBinding.button17.setAlpha(1f);
                    mViewDataBinding.button17.setClickable(true);
                }
                viewModel.selectedDate = date
                viewModel.mYearMonth.set(SimpleDateFormat("MMMM yyyy").format(date!!.time))
            }
        })

        mDataBinding.button17.setOnClickListener {
            var hour: Int = 0
            var minute: Int = 0
            val calendar: Calendar = Calendar.getInstance()
            hour = calendar.get(Calendar.HOUR)
            minute = calendar.get(Calendar.MINUTE)
            val timePickerDialog = TimePickerDialog(
                this, this@FindDoctorBookingActivity, hour, minute,
                DateFormat.is24HourFormat(this)
            )
            timePickerDialog.show()

        }
    }

    fun isBeforeToday(d: Calendar): Boolean {
        val cal = Calendar.getInstance()
        val currentYear: Int
        val currentMonth: Int
        val currentDay: Int
        currentYear = cal[Calendar.YEAR]
        currentMonth = cal[Calendar.MONTH]
        currentDay = cal[Calendar.DAY_OF_MONTH]
        if (d.time.after(cal.time)) {
            return false
        } else return !(d.get(Calendar.YEAR) == currentYear && d.get(Calendar.MONTH) == currentMonth && d.get(
            Calendar.DAY_OF_MONTH
        ) == currentDay)

    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {

        val datetime = Calendar.getInstance()
        val c = Calendar.getInstance()
        datetime[Calendar.YEAR] = viewModel.selectedDate!!.get(Calendar.YEAR)
        datetime[Calendar.MONTH] = viewModel.selectedDate!!.get(Calendar.MONTH)+1
        datetime[Calendar.DAY_OF_MONTH] = viewModel.selectedDate!!.get(Calendar.DAY_OF_MONTH)
        datetime[Calendar.HOUR_OF_DAY] = hourOfDay
        datetime[Calendar.MINUTE] = minute
        if (datetime.timeInMillis >= c.timeInMillis) {
            val hour = hourOfDay % 12

            val selectedhour  =          if(hourOfDay<10)"0"+ String.valueOf(hourOfDay)else String.valueOf(hourOfDay)
            val selectedminutes  =          if(minute<10)"0"+ String.valueOf(minute)else String.valueOf(minute)
            preferenceHelper.setValue(
                PreferenceKey.SCHEDULED_DATE,
                viewModel.selectedDate!!.get(Calendar.YEAR).toString() +"-"+(viewModel.selectedDate!!.get(Calendar.MONTH)+1).toString() +"-"+(viewModel.selectedDate!!.get(Calendar.DAY_OF_MONTH)+1).toString() + " " + selectedhour + ":" + selectedminutes + ":" + "00"
            )
            if (mDataBinding.radioButton.isChecked) {
                preferenceHelper.setValue(PreferenceKey.VISIT_PURPOSE, "follow_up")
            } else {
                preferenceHelper.setValue(PreferenceKey.VISIT_PURPOSE, "consultation")
            }

            performclick()

        } else {
            Toast.makeText(applicationContext, "Invalid Time", Toast.LENGTH_LONG).show()
        }

    }
    private fun observeResponse() {
        viewModel.mBookedResponse.observe(this@FindDoctorBookingActivity, Observer<BookedResponse> {
            loadingObservable.value = false
            if (it.status) {
                val intent = Intent(this@FindDoctorBookingActivity, PatientDetailsActivity::class.java)
                startActivity(intent);

            }else
                ViewUtils.showToast(this@FindDoctorBookingActivity, it.message, false)


        })
        viewModel.getErrorObservable().observe(this, Observer<kotlin.String> { message ->
            loadingObservable.value = false
            ViewUtils.showToast(this@FindDoctorBookingActivity, message, false)
        })


    }
    fun performclick(){
        loadingObservable.value = true
        bookDoctor_Map["doctor_id"]= preferenceHelper.getValue(PreferenceKey.SELECTED_DOC_ID,"").toString()
        bookDoctor_Map["booking_for"]= preferenceHelper.getValue(PreferenceKey.VISIT_PURPOSE,"").toString()
        bookDoctor_Map["scheduled_at"]= preferenceHelper.getValue(PreferenceKey.SCHEDULED_DATE,"").toString()
        bookDoctor_Map["consult_time"]= "15"
        bookDoctor_Map["appointment_type"]= "OFFLINE"
        bookDoctor_Map["description"]= ""

//                loadingObservable.value = true
        viewModel.BookDoctor(bookDoctor_Map)
    }

}
