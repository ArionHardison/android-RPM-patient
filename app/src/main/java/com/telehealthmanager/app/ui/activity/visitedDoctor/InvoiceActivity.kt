package com.telehealthmanager.app.ui.activity.visitedDoctor

import android.content.Intent
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.telehealthmanager.app.BaseApplication
import com.telehealthmanager.app.R
import com.telehealthmanager.app.base.BaseActivity
import com.telehealthmanager.app.data.PreferenceHelper
import com.telehealthmanager.app.data.PreferenceKey
import com.telehealthmanager.app.data.getValue
import com.telehealthmanager.app.databinding.ActivityInvoiceBinding
import com.telehealthmanager.app.repositary.WebApiConstants
import com.telehealthmanager.app.repositary.model.Appointment

class InvoiceActivity : BaseActivity<ActivityInvoiceBinding>() {

    private lateinit var viewModel: VisitedDoctorsViewModel
    private lateinit var mDataBinding: ActivityInvoiceBinding
    private val preferenceHelper = PreferenceHelper(BaseApplication.baseApplication)

    override fun getLayoutId(): Int = R.layout.activity_invoice

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mDataBinding = mViewDataBinding as ActivityInvoiceBinding
        viewModel = ViewModelProviders.of(this).get(VisitedDoctorsViewModel::class.java)
        mDataBinding.viewmodel = viewModel
        initIntentData()

        mDataBinding.toolbarBackImg.setOnClickListener {
            onBackPressed()
        }

        mDataBinding.tvDone.setOnClickListener {
            finish()
        }
    }

    private fun initIntentData() {
        val appointment: Appointment? = intent.getSerializableExtra(WebApiConstants.IntentPass.Appointment) as? Appointment
        viewModel.mAppointmentDetails.value = appointment
        /*     if (viewModel.mAppointmentDetails.value!!.invoice != null) {
                 viewModel.mSpecialityFees.set(getAmountFormat(appointment!!.invoice!!.specialityFees.toString()))
                 viewModel.mConsultingFees.set(getAmountFormat(appointment.invoice!!.consultingFees.toString()))
                 viewModel.mGrossTotal.set(getAmountFormat(appointment.invoice.gross.toString()))
                 viewModel.mTotalPaid.set(getAmountFormat(appointment.invoice.totalPay.toString()))
             } else {*/
        val zeroAmount = getAmountFormat("0")
        viewModel.mSpecialityFees.set(zeroAmount)
        viewModel.mConsultingFees.set(zeroAmount)
        viewModel.mGrossTotal.set(zeroAmount)
        viewModel.mTotalPaid.set(zeroAmount)
        // }
    }

    private fun getAmountFormat(amountStr: String): String {
        return String.format(
            "%s %s",
            preferenceHelper.getValue(PreferenceKey.CURRENCY, "$"),
            amountStr
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}