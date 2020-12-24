package com.telehealthmanager.app.ui.activity.visitedDoctor

import android.content.Intent
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.telehealthmanager.app.BaseApplication
import com.telehealthmanager.app.R
import com.telehealthmanager.app.base.BaseActivity
import com.telehealthmanager.app.data.PreferenceHelper
import com.telehealthmanager.app.data.PreferenceKey
import com.telehealthmanager.app.data.getValue
import com.telehealthmanager.app.databinding.ActivityInvoiceBinding
import com.telehealthmanager.app.repositary.WebApiConstants
import com.telehealthmanager.app.repositary.model.Appointment
import com.telehealthmanager.app.repositary.model.Invoice
import com.telehealthmanager.app.ui.activity.main.MainActivity
import kotlin.math.roundToInt

class InvoiceActivity : BaseActivity<ActivityInvoiceBinding>() {

    private lateinit var viewModel: VisitedDoctorsViewModel
    private lateinit var mDataBinding: ActivityInvoiceBinding
    private val preferenceHelper = PreferenceHelper(BaseApplication.baseApplication)

    override fun getLayoutId(): Int = R.layout.activity_invoice

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mDataBinding = mViewDataBinding as ActivityInvoiceBinding
        viewModel = ViewModelProvider(this).get(VisitedDoctorsViewModel::class.java)
        mDataBinding.viewmodel = viewModel
        initIntentData()

        mDataBinding.toolbarBackImg.setOnClickListener {
            openNewActivity(this@InvoiceActivity, MainActivity::class.java, true)
            finishAffinity()
        }

        mDataBinding.tvDone.setOnClickListener {
            openNewActivity(this@InvoiceActivity, MainActivity::class.java, true)
            finishAffinity()
        }
    }

    private fun initIntentData() {
        val invoice: Invoice? = intent.getSerializableExtra(WebApiConstants.IntentPass.Invoice) as? Invoice
        invoice.let {
            viewModel.mSpecialityFees.set(getAmountFormat(it!!.speciality_fees.toString()))
            viewModel.mConsultingFees.set(getAmountFormat(it.consulting_fees.toString()))
            viewModel.mGrossTotal.set(getAmountFormat(it.gross.toString()))
            viewModel.mTotalPaid.set(getAmountFormat(it.total_pay.toString()))
        }
    }

    private fun getAmountFormat(amountStr: String): String {
        return String.format(
            "%s %s",
            preferenceHelper.getValue(PreferenceKey.CURRENCY, "$"),
            amountStr
        )
    }
}