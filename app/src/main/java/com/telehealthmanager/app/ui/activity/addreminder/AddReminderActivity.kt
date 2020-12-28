package com.telehealthmanager.app.ui.activity.addreminder

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.telehealthmanager.app.R
import com.telehealthmanager.app.base.BaseActivity
import com.telehealthmanager.app.databinding.ActivityAddReminderBinding
import com.telehealthmanager.app.repositary.model.ReminderResponse
import com.telehealthmanager.app.utils.CustomBackClick
import com.telehealthmanager.app.utils.ViewUtils
import java.text.Format
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class AddReminderActivity : BaseActivity<ActivityAddReminderBinding>(), AddReminderNavigator, CustomBackClick {

    private var fromCalendar: Calendar = Calendar.getInstance()
    private var dateSetListener: DatePickerDialog.OnDateSetListener? = null
    lateinit var mDataBinding: ActivityAddReminderBinding
    lateinit var mViewModel: AddReminderViewModel
    private var isEdit: Boolean = false

    override fun getLayoutId(): Int = R.layout.activity_add_reminder

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mDataBinding = mViewDataBinding as ActivityAddReminderBinding
        mViewModel = ViewModelProvider(this).get(AddReminderViewModel::class.java)
        mViewModel.navigator = this
        mDataBinding.viewmodel = mViewModel
        mDataBinding.isEdit=true

        mViewModel.setOnClickListener(this@AddReminderActivity)
        mViewModel.toolBarTile.value = getString(R.string.add_new_remainder)

        initializing()
        observeSuccessResponse()
        observeErrorResponse()
        observeShowLoading()
        initIntentData()
    }

    override fun clickBackPress() {
        finish()
    }

    private fun initIntentData() {
        val reminder: ReminderResponse.Reminder? = intent.getSerializableExtra("reminder") as ReminderResponse.Reminder?
        if (reminder != null) {
            isEdit = true
            mDataBinding.isEdit=false
            mDataBinding.btnSubmit.visibility = View.GONE
            mViewModel.name.set(reminder.name)
            mViewModel.toolBarTile.value = reminder.name
            mViewModel.patientId.set(reminder.patientId)
            val notify = reminder.notifyMe != 0
            mViewModel.notifyme.set(notify)
            mViewModel.notifymeValue.set(reminder.notifyMe)
            val alarm = reminder.alarm != 0
            mViewModel.alarm.set(alarm)
            mViewModel.alarmValue.set(reminder.alarm)
            mViewModel.id.set(reminder.id)

            mViewModel.displayFromDate.set(ViewUtils.getDisplayDayFormat(reminder.date))
            mViewModel.displayfromTime.set(ViewUtils.getDisplayTimeFormat(reminder.time))
            mViewModel.fromDate.value = reminder.date
            mViewModel.fromTime.value = reminder.time
            mDataBinding.btnSubmit.text = getString(R.string.save_changes)
            mViewModel.toolBarTile.value = reminder.name
        }
    }

    private fun observeErrorResponse() {
        mViewModel.getErrorObservable().observe(this, Observer<String> { message ->
            mViewModel.loadingProgress.value = false
            ViewUtils.showToast(this@AddReminderActivity, message, false)
        })
    }

    private fun initializing() {

        mViewModel.fromDate.value = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(fromCalendar.time)
        mViewModel.displayFromDate.set(SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(fromCalendar.time))
        val f: Format = SimpleDateFormat("HH:mm:ss")
        mViewModel.fromTime.value = f.format(fromCalendar.time)
        try {
            mViewModel.displayfromTime.set(SimpleDateFormat("h:mm a", Locale.getDefault()).format(SimpleDateFormat("HH:mm:ss", Locale.getDefault()).parse(mViewModel.fromTime.value)))
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth -> // TODO Auto-generated method stub
                fromCalendar.set(Calendar.YEAR, year)
                fromCalendar.set(Calendar.MONTH, monthOfYear)
                fromCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                mViewModel.fromTime.value = sdf.format(fromCalendar.time)
                //scheduleDate.setText(schedule_date);
                val fmtOut = SimpleDateFormat("dd/mm/yy", Locale.getDefault())
                mViewModel.displayFromDate.set(fmtOut.format(fromCalendar.time))
            }
    }

    private fun observeShowLoading() {
        mViewModel.loadingProgress.observe(this, Observer {
            if (!it) {
                hideLoading()
            } else {
                showLoading()
            }
        })
    }

    private fun observeSuccessResponse() {
        mViewModel.mAddRemainderResponse.observe(this, Observer {
            mViewModel.loadingProgress.value = true
            ViewUtils.showToast(this@AddReminderActivity, getString(R.string.reminder_added_successfully), true)
            finish()
        })
    }

    override fun onFromDateClicked() {
        val dialog = DatePickerDialog(
            this@AddReminderActivity, dateSetListener, fromCalendar.get(Calendar.YEAR), fromCalendar.get(
                Calendar.MONTH
            ), fromCalendar.get(Calendar.DAY_OF_MONTH)
        )
        dialog.datePicker.minDate = System.currentTimeMillis() - 1000
        dialog.show()
    }

    override fun onBackButtonClicked() {
        finish()
    }

    override fun onFromTimeClicked() {
        val hour: Int = fromCalendar.get(Calendar.HOUR_OF_DAY)
        val minute: Int = fromCalendar.get(Calendar.MINUTE)
        val mTimePicker: TimePickerDialog
        mTimePicker = TimePickerDialog(
            this@AddReminderActivity,
            TimePickerDialog.OnTimeSetListener { timePicker, selectedHour, selectedMinute -> //scheduleTime.setText(schedule_time);
                fromCalendar.set(Calendar.HOUR_OF_DAY, selectedHour)
                fromCalendar.set(Calendar.MINUTE, selectedMinute)
                val currentTimeTwoHours = Calendar.getInstance()
                currentTimeTwoHours.add(Calendar.MINUTE, -1)
                if (fromCalendar.timeInMillis < currentTimeTwoHours.timeInMillis) {
                    ViewUtils.showToast(this@AddReminderActivity, getString(R.string.past_time_error), false)
                    return@OnTimeSetListener
                }
                val f: Format = SimpleDateFormat("HH:mm:ss")
                mViewModel.fromTime.value = f.format(fromCalendar.time)
                val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                var dateObj: Date? = null
                try {
                    dateObj = sdf.parse(mViewModel.fromTime.value)
                    val formatTime = SimpleDateFormat("h:mm a", Locale.getDefault()).format(dateObj)
                    mViewModel.displayfromTime.set(formatTime)
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
            }, hour, minute, false
        ) //Yes 24 hour time
        mTimePicker.show()
    }

    override fun validateAddRemainder() {
        if (!isEdit) {
            val currentTimeTwoHours = Calendar.getInstance()
            currentTimeTwoHours.add(Calendar.MINUTE, -5)
            if (mViewModel.name.get()!!.equals("")) {
                ViewUtils.showToast(this, getString(R.string.please_enter_reminder_name), false)
            } else if (fromCalendar.timeInMillis < currentTimeTwoHours.timeInMillis) {
                ViewUtils.showToast(this@AddReminderActivity, getString(R.string.past_time_error), false)
            } else {
                mViewModel.addReminderAPI()
            }
        } else {
            val currentTimeTwoHours = Calendar.getInstance()
            currentTimeTwoHours.add(Calendar.MINUTE, -5)
            if (mViewModel.name.get()!! == "") {
                ViewUtils.showToast(this, getString(R.string.please_enter_reminder_name), false)
            } else if (fromCalendar.timeInMillis < currentTimeTwoHours.timeInMillis) {
                ViewUtils.showToast(this@AddReminderActivity, getString(R.string.past_time_error), false)
            } else {
                mViewModel.addReminderAPI()
            }
        }
    }
}