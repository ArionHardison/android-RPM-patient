package com.telehealthmanager.app.ui.activity.findDoctors

import android.app.TimePickerDialog
import android.content.Intent
import android.view.View
import android.widget.TimePicker
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.telehealthmanager.app.BaseApplication
import com.telehealthmanager.app.BuildConfig
import com.telehealthmanager.app.R
import com.telehealthmanager.app.base.BaseActivity
import com.telehealthmanager.app.data.PreferenceHelper
import com.telehealthmanager.app.data.PreferenceKey
import com.telehealthmanager.app.data.getValue
import com.telehealthmanager.app.data.setValue
import com.telehealthmanager.app.databinding.ActivityFindDoctorBookingBinding
import com.telehealthmanager.app.repositary.WebApiConstants
import com.telehealthmanager.app.repositary.model.BookedResponse
import com.telehealthmanager.app.repositary.model.DoctorListResponse
import com.telehealthmanager.app.repositary.model.Hospital
import com.telehealthmanager.app.repositary.model.MainResponse
import com.telehealthmanager.app.ui.activity.patientDetail.PatientDetailsActivity
import com.telehealthmanager.app.ui.calander_view.Tools
import com.telehealthmanager.app.ui.timeslot.*
import com.telehealthmanager.app.utils.CustomBackClick
import com.telehealthmanager.app.utils.ViewUtils
import java.lang.String
import java.text.SimpleDateFormat
import java.util.*


class FindDoctorBookingActivity : BaseActivity<ActivityFindDoctorBookingBinding>(), TimePickerDialog.OnTimeSetListener, CustomBackClick, IListener, IAppointmentListener {

    private val preferenceHelper = PreferenceHelper(BaseApplication.baseApplication)
    private lateinit var viewModel: FindDoctorsViewModel
    private lateinit var mDataBinding: ActivityFindDoctorBookingBinding
    private val sdf1 = SimpleDateFormat("yyyy-MM-dd")

    var timingList: MutableList<Hospital.Timing>? = arrayListOf()
    var timeSlotDayAdapter: TimeSlotDayAdapter? = null
    var timeSlotTimeAdapter: TimeSlotTimeAdapter? = null
    var timeAllList = ArrayList<SlotSelectionItem>()
    var timeAllWithDateList = ArrayList<SlotSelectionItem>()
    var pos: Int = 0

    override fun getLayoutId(): Int = R.layout.activity_find_doctor_booking

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mDataBinding = mViewDataBinding as ActivityFindDoctorBookingBinding
        viewModel = ViewModelProvider(this).get(FindDoctorsViewModel::class.java)
        mDataBinding.viewmodel = viewModel
        observeResponse()

        initTimeSlot()

        mDataBinding.searchDocName.text = preferenceHelper.getValue(PreferenceKey.SELECTED_DOC_NAME, "").toString()
        mDataBinding.searchDocSpec.text = preferenceHelper.getValue(PreferenceKey.SELECTED_DOC_Special, "").toString()
        mDataBinding.searchDocHospName.text = preferenceHelper.getValue(PreferenceKey.SELECTED_DOC_ADDRESS, "").toString()

        if (preferenceHelper.getValue(PreferenceKey.SELECTED_DOC_Special, "").toString().isEmpty())
            mDataBinding.searchDocSpec.visibility = View.GONE

        if (preferenceHelper.getValue(PreferenceKey.SELECTED_DOC_IMAGE, "").toString() != "") {
            ViewUtils.setDocViewGlide(
                this@FindDoctorBookingActivity, mDataBinding.searchDocImg,
                BuildConfig.BASE_IMAGE_URL + preferenceHelper.getValue(PreferenceKey.SELECTED_DOC_IMAGE, "").toString()
            )
        }


        val startTime: Calendar = Calendar.getInstance()
        startTime.add(Calendar.DATE, 0)

        val endTime = Calendar.getInstance()
        endTime.add(Calendar.MONTH, 6)

        val datesToBeColored: ArrayList<kotlin.String> = ArrayList()
        datesToBeColored.add(Tools.getFormattedDateToday())
        viewModel.mSelectedScheduleDate.set(sdf1.format(System.currentTimeMillis()))
        mDataBinding.calanderView.setUpCalendar(startTime.timeInMillis, endTime.timeInMillis, datesToBeColored) { date, strDate ->
            viewModel.mSelectedScheduleDate.set(strDate)
            viewModel.selectedDate = date
        }

        mDataBinding.button17.setOnClickListener {
            /*      var hour: Int = 0
                  var minute: Int = 0
                  val calendar: Calendar = Calendar.getInstance()
                  hour = calendar.get(Calendar.HOUR_OF_DAY)
                  minute = calendar.get(Calendar.MINUTE)
                  val timePickerDialog = TimePickerDialog(this, this@FindDoctorBookingActivity, hour, minute, false)
                  timePickerDialog.show()*/
            perFormClick()
        }

        viewModel.setOnClickListener(this@FindDoctorBookingActivity)
        viewModel.toolBarTile.value = getString(R.string.booking)
    }

    private fun observeResponse() {
        viewModel.mBookedResponse.observe(this@FindDoctorBookingActivity, Observer<BookedResponse> {
            loadingObservable.value = false
            if (it.status) {
                val intent = Intent(this@FindDoctorBookingActivity, PatientDetailsActivity::class.java)
                startActivity(intent)
            } else
                ViewUtils.showToast(this@FindDoctorBookingActivity, it.message, false)
        })

        viewModel.getErrorObservable().observe(this, Observer<kotlin.String> { message ->
            loadingObservable.value = false
            ViewUtils.showToast(this@FindDoctorBookingActivity, message, false)
        })
    }

    private fun initTimeSlot() {
        val details = intent.getSerializableExtra(WebApiConstants.IntentPass.DoctorProfile) as? DoctorListResponse.specialities.DoctorProfile
        val favDoctor = intent.getSerializableExtra(WebApiConstants.IntentPass.FavDoctorProfile) as? MainResponse.Doctor
        val searchDoctor = intent.getSerializableExtra(WebApiConstants.IntentPass.SearchDoctorProfile) as? Hospital
        when {
            details != null -> {
                details.let {
                    if (it.hospital.isNotEmpty()) {
                        it.hospital[0].let { hospital ->
                            timingList = hospital.timing as MutableList<Hospital.Timing>?
                        }
                    }
                }
            }
            favDoctor != null -> {
                favDoctor.hospital?.let {
                    timingList = it.timing as MutableList<Hospital.Timing>?
                }
            }
            searchDoctor != null -> {
                searchDoctor.let {
                    timingList = it.timing as MutableList<Hospital.Timing>?
                }
            }
        }

        for (i in timingList?.indices!!) {
            val dateFormat = SimpleDateFormat("hh:mm:ss")
            val date = dateFormat.parse(timingList!![i].start_time)
            val endDate = dateFormat.parse(timingList!![i].end_time)
            val timeDff: Long = endDate!!.time - date!!.time
            val timeDifference =roundToMin(Date(timeDff))
            val difference: Long = timeDifference!!.time
            val days = (difference / (1000 * 60 * 60 * 24));
            val hours = ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));

            val selectionItem = SlotSelectionItem()
            selectionItem.day = timingList?.get(i)?.day.toString()
            selectionItem.selectedItem = i == 0

            val calendar = Calendar.getInstance()
            calendar.time = date

            val timeList = ArrayList<SloItem>()
            val timeStringList = ArrayList<kotlin.String>()
            for (i in 0 until hours) {
                calendar.add(Calendar.HOUR, 1)
                val simpleDateFormat = SimpleDateFormat("HH:mm")
                val time = simpleDateFormat.format(calendar.time)
                timeStringList.add(time)
            }

            val morningList = ArrayList<SlotTimeItem>()
            val afterNoonList = ArrayList<SlotTimeItem>()
            val eveningList = ArrayList<SlotTimeItem>()
            for (i in 0 until timeStringList.size) {
                val hour = timeStringList[i].get(0).plus(timeStringList[i][1].toString())
                if (getSession(hour.toInt()).toLowerCase(Locale.ROOT) == "morning") {
                    morningList.add(SlotTimeItem(false, timeStringList.get(i)))
                }
                if (getSession(hour.toInt()).toLowerCase(Locale.ROOT) == "afternoon") {
                    afterNoonList.add(SlotTimeItem(false, timeStringList.get(i)))
                }
                if (getSession(hour.toInt()).toLowerCase(Locale.ROOT) == "evening") {
                    eveningList.add(SlotTimeItem(false, timeStringList.get(i)))
                }
            }

            timeList.add(SloItem("Morning", morningList))
            timeList.add(SloItem("Afternoon", afterNoonList))
            timeList.add(SloItem("Evening", eveningList))
            selectionItem.slotSelectionItem = timeList
            timeAllList.add(selectionItem)
        }

        val calendar = Calendar.getInstance()
        val day = calendar[Calendar.DAY_OF_WEEK]
        for (i in 0..29) {
            if (i == 0) {
                for (j in 0 until timeAllList.size) {
                    if (timeAllList[j].day.toLowerCase(Locale.ROOT).contains(getDays(day))) {
                        val slotSelectionItem = SlotSelectionItem()
                        slotSelectionItem.selectedItem = true
                        slotSelectionItem.date = getShowDate(calendar.time)
                        slotSelectionItem.day = getDays(day)
                        slotSelectionItem.date1 = calendar.time
                        slotSelectionItem.slotSelectionItem = timeAllList.get(j).slotSelectionItem
                        timeAllWithDateList.add(slotSelectionItem)

                    }
                }
            } else {
                calendar.add(Calendar.DATE, pos + 1);
                val day: Int = calendar[Calendar.DAY_OF_WEEK]
                for (j in 0 until timeAllList.size) {
                    if (timeAllList[j].day.toLowerCase(Locale.ROOT).contains(getDays(day))) {
                        val slotSelectionItem = SlotSelectionItem()
                        slotSelectionItem.selectedItem = false
                        slotSelectionItem.date = getShowDate(calendar.time)
                        slotSelectionItem.day = getDays(day)
                        slotSelectionItem.date1 = calendar.time
                        slotSelectionItem.slotSelectionItem = timeAllList.get(j).slotSelectionItem
                        timeAllWithDateList.add(slotSelectionItem)

                    }
                }
            }
        }

        timeSlotDayAdapter = TimeSlotDayAdapter(this, timeAllWithDateList, this)
        mDataBinding.rvDay.adapter = timeSlotDayAdapter
        mDataBinding.rvDay.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        if (timeAllList.size > 0) {
            timeSlotTimeAdapter =
                timeAllList.get(0).slotSelectionItem?.let {
                    TimeSlotTimeAdapter(this, it, this, true)
                }
            mDataBinding.rvTime.adapter = timeSlotTimeAdapter
            mDataBinding.rvTime.layoutManager = LinearLayoutManager(this)
        }
        pos = 0
    }

    private fun getSession(hour: Int): kotlin.String {
        var txt = ""
        when (hour) {
            in 0..11 -> {
                txt = "Morning"
            }
            in 12..17 -> {
                txt = "Afternoon"
            }
            in 18..23 -> {
                txt = "Evening"
            }
        }
        return txt
    }

    private fun getShowDate(date: Date): kotlin.String {
        val simpleDateFormat = SimpleDateFormat("dd MMM")
        return simpleDateFormat.format(date)
    }

    private fun getDays(day: Int): kotlin.String {
        when (day) {
            Calendar.SUNDAY -> return "sun"
            Calendar.MONDAY -> return "mon"
            Calendar.TUESDAY -> return "tue"
            Calendar.WEDNESDAY -> return "wed"
            Calendar.THURSDAY -> return "thu"
            Calendar.FRIDAY -> return "fri"
            Calendar.SATURDAY -> return "sat"
        }
        return ""
    }

    override fun clickBackPress() {
        finish()
    }


    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        viewModel.selectedDate!!.set(Calendar.HOUR_OF_DAY, hourOfDay)
        viewModel.selectedDate!!.set(Calendar.MINUTE, minute)
        val currentTime = Calendar.getInstance()
        currentTime.add(Calendar.MINUTE, -1)
        if (viewModel.selectedDate!!.timeInMillis < currentTime.timeInMillis) {
            ViewUtils.showToast(this@FindDoctorBookingActivity, getString(R.string.past_time_error), false)
            return
        }

        val selectedHour = if (hourOfDay < 10) "0" + String.valueOf(hourOfDay) else String.valueOf(hourOfDay)
        val selectedMinutes = if (minute < 10) "0" + String.valueOf(minute) else String.valueOf(minute)

        preferenceHelper.setValue(PreferenceKey.SCHEDULED_DATE, viewModel.mSelectedScheduleDate.get().toString() + " " + selectedHour + ":" + selectedMinutes + ":" + "00")

        perFormClick()
    }

    private fun perFormClick() {
        try {
            var time = ""
            var date: Date? = null
            for (i in 0..timeAllWithDateList.get(pos).slotSelectionItem?.size?.minus(1)!!) {
                for (j in 0..timeAllWithDateList.get(pos).slotSelectionItem?.get(i)?.sessionTimeList?.size?.minus(
                    1
                )!!) {
                    if (timeAllWithDateList.get(pos).slotSelectionItem?.get(i)?.sessionTimeList?.get(j)?.isSelected!!) {
                        time = timeAllWithDateList.get(pos).slotSelectionItem?.get(i)?.sessionTimeList!!.get(j).time
                        break
                    }
                }
            }

            for (i in 0..timeAllWithDateList.size - 1) {
                if (timeAllWithDateList.get(i).selectedItem) {
                    date = timeAllWithDateList.get(i).date1!!
                }
            }

            if (date == null) {
                Toast.makeText(this, "Please select date", Toast.LENGTH_SHORT).show()
                return
            }
            if (time.isEmpty()) {
                Toast.makeText(this, "Please select time", Toast.LENGTH_SHORT).show()
                return
            }
            if (mDataBinding.radioButton.isChecked) {
                preferenceHelper.setValue(PreferenceKey.VISIT_PURPOSE, mDataBinding.radioButton.text.toString())
                preferenceHelper.setValue(PreferenceKey.BOOKED_FOR, "follow_up")
            } else {
                preferenceHelper.setValue(PreferenceKey.VISIT_PURPOSE, mDataBinding.radioButton2.text.toString())
                preferenceHelper.setValue(PreferenceKey.BOOKED_FOR, "consultation")
            }

            preferenceHelper.setValue(PreferenceKey.SCHEDULED_DATE, ViewUtils.getDateFormat(date) + " " + time + ":00")
            val intent = Intent(this@FindDoctorBookingActivity, PatientDetailsActivity::class.java)
            startActivity(intent)
        } catch (e: Exception) {

        }
    }

    override fun onItemClick(position: Int, position2: Int, selectionItem: SlotTimeItem) {
        for (i in 0..timeAllWithDateList[pos].slotSelectionItem?.size?.minus(1)!!) {
            for (j in 0..timeAllWithDateList[pos].slotSelectionItem?.get(i)?.sessionTimeList?.size?.minus(1)!!) {
                if (position == i) {
                    timeAllWithDateList[pos].slotSelectionItem?.get(i)?.sessionTimeList?.get(j)?.isSelected = j == position2
                } else {
                    timeAllWithDateList[pos].slotSelectionItem?.get(i)?.sessionTimeList?.get(j)?.isSelected = false
                }
            }
        }

        timeSlotTimeAdapter = timeAllWithDateList[pos].slotSelectionItem?.let {
            TimeSlotTimeAdapter(this, it, this, pos == 0)
        }

        mDataBinding.rvTime.adapter = timeSlotTimeAdapter
        mDataBinding.rvTime.layoutManager = LinearLayoutManager(this)
        mDataBinding.rvTime.scrollToPosition(position)
    }

    override fun onItemClick(position: Int, selectionItem: SlotSelectionItem) {
        pos = position
        for (i in 0..timeAllWithDateList.size - 1) {
            timeAllWithDateList.get(i).selectedItem = i == position
        }
        timeSlotDayAdapter = TimeSlotDayAdapter(this, timeAllWithDateList, this)
        mDataBinding.rvDay.adapter = timeSlotDayAdapter

        timeSlotTimeAdapter = timeAllWithDateList.get(position).slotSelectionItem?.let {
            TimeSlotTimeAdapter(
                this, it,
                this, position == 0
            )
        }
        mDataBinding.rvTime.adapter = timeSlotTimeAdapter
        mDataBinding.rvTime.layoutManager = LinearLayoutManager(this)
    }

    fun roundToMin(d: Date?): Date? {
        val date: Calendar = GregorianCalendar()
        date.time = d
        val deltaMin = date[Calendar.SECOND] / 30
        date[Calendar.SECOND] = 0
        date[Calendar.MILLISECOND] = 0
        date.add(Calendar.MINUTE, deltaMin)
        return date.time
    }

}
