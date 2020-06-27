package com.midokter.app.ui.activity.addreminder

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.midokter.app.BuildConfig
import com.midokter.app.R
import com.midokter.app.base.BaseActivity
import com.midokter.app.databinding.ActivityAddReminderBinding
import com.midokter.app.repositary.model.ArticleResponse
import com.midokter.app.repositary.model.ReminderResponse
import com.midokter.app.utils.ViewUtils
import java.text.Format
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class AddReminderActivity : BaseActivity<ActivityAddReminderBinding>(), AddReminderNavigator{
    var fromCalendar = Calendar.getInstance()
    var dateSetListener: DatePickerDialog.OnDateSetListener? = null
    lateinit var mViewDataBinding: ActivityAddReminderBinding
    lateinit var mViewModel : AddReminderViewModel
    override fun getLayoutId(): Int = R.layout.activity_add_reminder
    var isEdit: Boolean= false

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        this.mViewDataBinding = mViewDataBinding as ActivityAddReminderBinding
        mViewModel = ViewModelProviders.of(this).get(AddReminderViewModel::class.java)
        mViewModel.navigator = this
        mViewDataBinding.viewmodel = mViewModel

        initializing()
        observeSuccessResponse()
        observeErrorResponse()
        observeShowLoading()
        initIntentData()
    }

    private fun initIntentData() {
        val reminder =
            intent.getSerializableExtra("reminder") as ReminderResponse.Reminder?
        if(reminder!=null) {
            isEdit=true
            mViewModel.name.set(reminder.name)
            mViewModel.patientId.set(reminder.patientId)
            val notify = if (reminder.notifyMe == 0) false else true
            mViewModel.notifyme.set(notify)
            mViewModel.notifymeValue.set(reminder.notifyMe)
            val alarm = if (reminder.alarm == 0) false else true
            mViewModel.alarm.set(alarm)
            mViewModel.alarmValue.set(reminder.alarm)
            mViewModel.id.set(reminder.id)

            mViewModel.displayFromDate.set(ViewUtils.getDisplayDayFormat(reminder.date))
            mViewModel.displayfromTime.set(ViewUtils.getDisplayTimeFormat(reminder.time))
            mViewModel.fromDate.value = reminder.date
            mViewModel.fromTime.value = reminder.time
            mViewDataBinding.btnSubmit.text=getString(R.string.save_changes)
            mViewDataBinding.tvReminderTitle.text=getString(R.string.edit_reminder)
        }
    }

    private fun observeErrorResponse() {
        mViewModel.getErrorObservable().observe(this, Observer<String> { message ->
            mViewModel.loadingProgress.value = false
            ViewUtils.showToast(this@AddReminderActivity, message, false)
        })
    }

    private fun initializing() {

        mViewModel.fromDate.value= SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            .format(fromCalendar.getTime())

        mViewModel.displayFromDate.set(
            SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
                .format(fromCalendar.getTime()))


        val f: Format = SimpleDateFormat("HH:mm:ss")
        mViewModel.fromTime.value = f.format(fromCalendar.getTime())
        try {
            mViewModel.displayfromTime.set(
                SimpleDateFormat("h:mm a", Locale.getDefault()).format(
                    SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                        .parse(mViewModel.fromTime.value)
                )
            )
        } catch (e: ParseException) {
            e.printStackTrace()
        }


        dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth -> // TODO Auto-generated method stub
                fromCalendar.set(Calendar.YEAR, year)
                fromCalendar.set(Calendar.MONTH, monthOfYear)
                fromCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val sdf =
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                mViewModel.fromTime.value = sdf.format(fromCalendar.getTime())
                //scheduleDate.setText(schedule_date);
                val fmtOut =
                    SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
                mViewModel.displayFromDate.set(fmtOut.format(fromCalendar.getTime()))
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
        val dialog = DatePickerDialog(this@AddReminderActivity, dateSetListener, fromCalendar.get(Calendar.YEAR), fromCalendar.get(
            Calendar.MONTH), fromCalendar.get(Calendar.DAY_OF_MONTH))
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
        mTimePicker = TimePickerDialog(this@AddReminderActivity,
            TimePickerDialog.OnTimeSetListener { timePicker, selectedHour, selectedMinute -> //scheduleTime.setText(schedule_time);
                fromCalendar.set(Calendar.HOUR_OF_DAY, selectedHour)
                fromCalendar.set(Calendar.MINUTE, selectedMinute)
                val currentTimeTwoHours = Calendar.getInstance()
                currentTimeTwoHours.add(Calendar.MINUTE, -1)
                if (fromCalendar.getTimeInMillis() < currentTimeTwoHours.timeInMillis) {

                    ViewUtils.showToast(
                        this@AddReminderActivity,
                        getString(R.string.past_time_error), false
                    )
                    return@OnTimeSetListener
                }
                val f: Format = SimpleDateFormat("HH:mm:ss")
                mViewModel.fromTime.value = f.format(fromCalendar.getTime())
                val sdf =
                    SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                var dateObj: Date? = null
                try {
                    dateObj = sdf.parse(mViewModel.fromTime.value)
                    val formatTime =
                        SimpleDateFormat("h:mm a", Locale.getDefault())
                            .format(dateObj)
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
            } else if (fromCalendar.getTimeInMillis() < currentTimeTwoHours.timeInMillis) {
                ViewUtils.showToast(
                    this@AddReminderActivity,
                    getString(R.string.past_time_error), false
                )
            } else {
                mViewModel.addReminderAPI()
            }
        }
    }
}