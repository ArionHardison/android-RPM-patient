package com.telehealthmanager.app.ui.activity.allservice

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.telehealthmanager.app.R
import com.telehealthmanager.app.base.BaseActivity
import com.telehealthmanager.app.databinding.ActivityAllServiceBinding
import com.telehealthmanager.app.repositary.WebApiConstants
import com.telehealthmanager.app.repositary.model.Hospital
import com.telehealthmanager.app.ui.adapter.AllServiceAdapter
import com.telehealthmanager.app.utils.CustomBackClick
import java.util.*

class AllServiceActivity : BaseActivity<ActivityAllServiceBinding>(), AllServiceNavigator, CustomBackClick {

    override fun getLayoutId() = R.layout.activity_all_service
    private lateinit var viewModel: AllServiceViewModel
    private lateinit var mDataBinding: ActivityAllServiceBinding
    private var mAdapterService: AllServiceAdapter? = null

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mDataBinding = mViewDataBinding as ActivityAllServiceBinding
        viewModel = ViewModelProvider(this).get(AllServiceViewModel::class.java)
        mDataBinding.viewmodel = viewModel
        viewModel.navigator = this
        initUI()
        viewModel.setOnClickListener(this@AllServiceActivity)
        viewModel.toolBarTile.value = resources.getString(R.string.all_services)
    }

    override fun clickBackPress() {
        finish()
    }

    private fun initUI() {
        val serviceList = intent.getSerializableExtra(WebApiConstants.IntentPass.SERVICE_LIST) as? MutableList<Hospital.DoctorService>
        viewModel.mServiceList = serviceList
        if (viewModel.mServiceList != null) {
            mAdapterService = AllServiceAdapter(this@AllServiceActivity, viewModel.mServiceList!!)
            mDataBinding.serviceAdapter = mAdapterService
            mDataBinding.rvServices.layoutManager =
                LinearLayoutManager(this@AllServiceActivity, LinearLayoutManager.VERTICAL, false)
            mAdapterService!!.notifyDataSetChanged()
        }
    }


}