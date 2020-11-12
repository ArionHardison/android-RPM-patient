package com.telehealthmanager.app.ui.activity.payment

import android.content.Intent
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.telehealthmanager.app.BaseApplication
import com.telehealthmanager.app.R
import com.telehealthmanager.app.base.BaseActivity
import com.telehealthmanager.app.data.PreferenceHelper
import com.telehealthmanager.app.databinding.ActivityPatientDetailsBinding
import com.telehealthmanager.app.databinding.ActivityPaymentBinding
import com.telehealthmanager.app.ui.activity.patientDetail.PatientDetailNavigator
import com.telehealthmanager.app.ui.activity.patientDetail.PatientDetailViewModel
import com.telehealthmanager.app.ui.activity.success.SuccessActivity
import kotlinx.android.synthetic.main.activity_payment.*

class PaymentActivity : BaseActivity<ActivityPaymentBinding>() , PaymentNavigator {

    override fun getLayoutId(): Int = R.layout.activity_payment

    private val preferenceHelper = PreferenceHelper(BaseApplication.baseApplication)
    private lateinit var viewModel: PaymentViewModel
    private lateinit var mBinding: ActivityPaymentBinding

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mBinding = mViewDataBinding as ActivityPaymentBinding
        viewModel = ViewModelProviders.of(this).get(PaymentViewModel::class.java)
        mBinding.viewmodel = viewModel
        viewModel.navigator = this

    }
}
