package com.midokter.app.ui.activity.allservice

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.midokter.app.R
import com.midokter.app.base.BaseActivity
import com.midokter.app.databinding.ActivityAllServiceBinding
import com.midokter.app.repositary.WebApiConstants
import com.midokter.app.repositary.model.Hospital
import com.midokter.app.ui.adapter.AllServiceAdapter

class AllServiceActivity : BaseActivity<ActivityAllServiceBinding>(),AllServiceNavigator{

    override fun getLayoutId()= R.layout.activity_all_service
    private lateinit var viewModel: AllServiceViewModel
    private lateinit var mDataBinding: ActivityAllServiceBinding
    private var mAdapterService: AllServiceAdapter? = null

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mDataBinding = mViewDataBinding as ActivityAllServiceBinding
        viewModel = ViewModelProviders.of(this).get(AllServiceViewModel::class.java)
        mDataBinding.viewmodel = viewModel
        viewModel.navigator = this
        initUI()
    }

    private fun initUI() {
        val serviceList = intent.getSerializableExtra(WebApiConstants.IntentPass.SERVICE_LIST) as? MutableList<Hospital.DoctorService>
        viewModel.mServiceList=serviceList
        mDataBinding.toolbarBackImg.setOnClickListener{
            finish()
        }
        if (viewModel.mServiceList != null) {
            mAdapterService =
                AllServiceAdapter(this@AllServiceActivity,viewModel.mServiceList!! )
            mDataBinding.serviceAdapter = mAdapterService
            mDataBinding.rvServices.layoutManager =
                LinearLayoutManager(
                    this@AllServiceActivity,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            mAdapterService!!.notifyDataSetChanged()
        }
    }
}