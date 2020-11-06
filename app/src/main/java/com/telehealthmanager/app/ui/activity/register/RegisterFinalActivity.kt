package com.telehealthmanager.app.ui.activity.register

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.telehealthmanager.app.BaseApplication
import com.telehealthmanager.app.BuildConfig
import com.telehealthmanager.app.R
import com.telehealthmanager.app.base.BaseActivity
import com.telehealthmanager.app.data.PreferenceHelper
import com.telehealthmanager.app.data.PreferenceKey
import com.telehealthmanager.app.data.getValue
import com.telehealthmanager.app.data.setValue
import com.telehealthmanager.app.databinding.ActivityRegisterFinalBinding
import com.telehealthmanager.app.repositary.WebApiConstants
import com.telehealthmanager.app.repositary.model.RegisterResponse
import com.telehealthmanager.app.ui.activity.main.MainActivity
import com.telehealthmanager.app.utils.ViewUtils
import java.util.*

class RegisterFinalActivity : BaseActivity<ActivityRegisterFinalBinding>(), RegisterNavigator {


    private lateinit var viewModel: RegisterViewModel
    private lateinit var mDataBinding: ActivityRegisterFinalBinding
    private val preferenceHelper = PreferenceHelper(BaseApplication.baseApplication)
    private var mdobDate: String? = null


    override fun getLayoutId(): Int = R.layout.activity_register_final

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mDataBinding = mViewDataBinding as ActivityRegisterFinalBinding
        viewModel = ViewModelProviders.of(this).get(RegisterViewModel::class.java)
        mDataBinding.viewmodel = viewModel
        viewModel.navigator = this

        observeResponse()
        mDataBinding.backarrow.setOnClickListener {
            finish()
        }
    }

    private fun observeResponse() {
        viewModel.mRegisterResponse.observe(this@RegisterFinalActivity, Observer<RegisterResponse> {
            loadingObservable.value = false
            goToHome(it)

        })

        viewModel.getErrorObservable().observe(this, Observer<String> { message ->
            loadingObservable.value = false
            ViewUtils.showToast(this@RegisterFinalActivity, message, false)
        })
    }

    private fun goToHome(data: RegisterResponse) {

        if (data.token_type.isNullOrBlank())
            ViewUtils.showToast(this@RegisterFinalActivity, "Login Failed", false)
        else {
            preferenceHelper.setValue(PreferenceKey.ACCESS_TOKEN, data.access_token.original.token)
            openNewActivity(this@RegisterFinalActivity, MainActivity::class.java, true)
            finishAffinity()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun pickDate() {
        val c = Calendar.getInstance()
        val mYear = c.get(Calendar.YEAR)
        val mMonth = c.get(Calendar.MONTH)
        val mDay = c.get(Calendar.DAY_OF_MONTH)
        val now = System.currentTimeMillis() - 1000
        val maxDate = System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 3)
        val datePickerDialog =
            DatePickerDialog(
                this@RegisterFinalActivity, R.style.TransportCalenderThemeDialog,
                { view, year, monthOfYear, dayOfMonth ->
                    view.minDate = System.currentTimeMillis() - 1000
                    view.maxDate = maxDate - 1000
                    mdobDate =
                        year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth.toString()
                    mDataBinding.dob.setText(dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year)
                }, mYear, mMonth, mDay
            )
        // datePickerDialog.datePicker.minDate = now
        datePickerDialog.datePicker.maxDate = now
        datePickerDialog.show()
    }

    override fun performValidation() {
        if (mDataBinding.dob.text.toString().isNullOrBlank()) {
            ViewUtils.showToast(this@RegisterFinalActivity, R.string.error_invalid_dob, false)
        } else {
            Register_Map[WebApiConstants.SignUp.COUNTRY_CODE] =
                preferenceHelper.getValue(PreferenceKey.COUNTRY_CODE, "91")!!
            Register_Map[WebApiConstants.SignUp.PHONE] =
                preferenceHelper.getValue(PreferenceKey.COUNTRY_CODE, "91")!!
                    .toString() + preferenceHelper.getValue(PreferenceKey.PHONE, "91")!!
            Register_Map[WebApiConstants.SignUp.OTP] =
                preferenceHelper.getValue(PreferenceKey.OTP, "91")!!
            Register_Map[WebApiConstants.SignUp.EMAIL] =
                preferenceHelper.getValue(PreferenceKey.EMAIL, "demo@demo.com")!!
            Register_Map[WebApiConstants.SignUp.GENDER] =
                preferenceHelper.getValue(PreferenceKey.GENDER, "MALE")!!
            Register_Map[WebApiConstants.SignUp.DOB] = mdobDate!!
            Register_Map[WebApiConstants.SignUp.FIRST_NAME] =
                preferenceHelper.getValue(PreferenceKey.FIRST_NAME, "demo")!!
            Register_Map[WebApiConstants.SignUp.LAST_NAME] =
                preferenceHelper.getValue(PreferenceKey.LAST_NAME, "demo")!!
            Register_Map[WebApiConstants.SignUp.GRANDTYPE] = "password"
            Register_Map[WebApiConstants.SignUp.DEVICE_TOKEN] =
                BaseApplication.getCustomPreference!!.getString(
                    PreferenceKey.DEVICE_TOKEN,
                    "111"
                ) as String
            Register_Map[WebApiConstants.SignUp.DEVICE_ID] =
                BaseApplication.getCustomPreference!!.getString(
                    PreferenceKey.DEVICE_ID,
                    "111"
                ) as String
            Register_Map[WebApiConstants.SignUp.DEVICE_TYPE] = BuildConfig.DEVICE_TYPE
            Register_Map[WebApiConstants.SignIn.CLIENT_ID] = BuildConfig.CLIENT_ID
            Register_Map[WebApiConstants.SignIn.CLIENT_SECRET] = BuildConfig.CLIENT_SECRET

            loadingObservable.value = true
            viewModel.Signup(Register_Map)

        }
    }


}
