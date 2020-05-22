package com.midokter.app.ui.activity.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.midokter.app.BaseApplication
import com.midokter.app.BuildConfig
import com.midokter.app.R
import com.midokter.app.base.BaseActivity
import com.midokter.app.data.PreferenceHelper
import com.midokter.app.data.PreferenceKey
import com.midokter.app.data.setValue
import com.midokter.app.databinding.ActivityRegisterFinalBinding
import com.midokter.app.databinding.ActivityRegisterGenderBinding
import com.midokter.app.repositary.WebApiConstants
import com.midokter.app.repositary.model.RegisterResponse
import com.midokter.app.ui.activity.main.MainActivity
import com.midokter.app.utils.ViewUtils
import com.midokter.doctor.repositary.model.LoginResponse
import com.midokter.doctor.repositary.model.OtpResponse
import kotlinx.android.synthetic.main.activity_register_final.*
import kotlinx.android.synthetic.main.activity_register_gender.*

class RegisterFinalActivity : BaseActivity<ActivityRegisterFinalBinding>() {
    private lateinit var viewModel: RegisterViewModel
    private lateinit var mDataBinding: ActivityRegisterFinalBinding
    private val preferenceHelper = PreferenceHelper(BaseApplication.baseApplication)


    override fun getLayoutId(): Int = R.layout.activity_register_final

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mDataBinding = mViewDataBinding as ActivityRegisterFinalBinding
        viewModel = ViewModelProviders.of(this).get(RegisterViewModel::class.java)
        mDataBinding.viewmodel = viewModel

        if (mViewDataBinding.dob.text.toString().isNullOrBlank()) {
            ViewUtils.showToast(this@RegisterFinalActivity, R.string.error_invalid_name, false)
        } else {
            Register_Map.put(WebApiConstants.SignUp.DOB, mDataBinding.dob.text.toString())
            Register_Map[WebApiConstants.SocialLogin.DEVICE_TOKEN] = BaseApplication.getCustomPreference!!.getString(
                PreferenceKey.DEVICE_TOKEN, "111") as String
            Register_Map[WebApiConstants.SocialLogin.DEVICE_ID] = BaseApplication.getCustomPreference!!.getString(
                PreferenceKey.DEVICE_ID, "111") as String
            Register_Map[WebApiConstants.SignIn.CLIENT_ID] = BuildConfig.CLIENT_ID
            Register_Map[WebApiConstants.SignIn.CLIENT_SECRET] = BuildConfig.CLIENT_SECRET
            loadingObservable.value = true
            viewModel.Signup(Register_Map)

        }

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

}
