package com.midokter.app.ui.activity.findDoctors

import android.app.TimePickerDialog
import android.content.Intent
import android.text.format.DateFormat
import android.widget.TimePicker
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.midokter.app.BaseApplication
import com.midokter.app.R
import com.midokter.app.base.BaseActivity
import com.midokter.app.data.PreferenceHelper
import com.midokter.app.data.PreferenceKey
import com.midokter.app.data.setValue
import com.midokter.app.databinding.ActivityFindDoctorBookingBinding
import com.midokter.app.ui.activity.patientDetail.PatientDetailsActivity
import devs.mulham.horizontalcalendar.HorizontalCalendar
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener
import kotlinx.android.synthetic.main.activity_find_doctor_booking.*
import java.lang.String
import java.text.SimpleDateFormat
import java.util.*


class FindDoctorBookingActivity : BaseActivity<ActivityFindDoctorBookingBinding>(),
    FindDoctorsNavigator, TimePickerDialog.OnTimeSetListener {

    private val preferenceHelper = PreferenceHelper(BaseApplication.baseApplication)
    private var horizontalCalendar: HorizontalCalendar? = null
    private lateinit var viewModel: FindDoctorsViewModel
    private lateinit var mDataBinding: ActivityFindDoctorBookingBinding

    override fun getLayoutId(): Int = R.layout.activity_find_doctor_booking

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mDataBinding = mViewDataBinding as ActivityFindDoctorBookingBinding
        viewModel = ViewModelProviders.of(this).get(FindDoctorsViewModel::class.java)
        mDataBinding.viewmodel = viewModel
        viewModel.navigator = this

        mDataBinding.toolbar7.setNavigationOnClickListener {
            finish()
        }

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

            preferenceHelper.setValue(
                PreferenceKey.SCHEDULED_DATE,
                viewModel.selectedDate!!.get(Calendar.YEAR).toString() +"-"+(viewModel.selectedDate!!.get(Calendar.MONTH)+1).toString() +"-"+(viewModel.selectedDate!!.get(Calendar.DAY_OF_MONTH)+1).toString() + " " + hourOfDay + ":" + minute + ":" + "00"
            )
            if (mDataBinding.radioButton.isChecked) {
                preferenceHelper.setValue(PreferenceKey.VISIT_PURPOSE, "Follow up")
            } else {
                preferenceHelper.setValue(PreferenceKey.VISIT_PURPOSE, "Consultation")
            }

            val intent = Intent(applicationContext, PatientDetailsActivity::class.java)
            startActivity(intent);

        } else {
            Toast.makeText(applicationContext, "Invalid Time", Toast.LENGTH_LONG).show()
        }

    }

}
