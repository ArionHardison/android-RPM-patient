package com.telehealthmanager.app.ui.activity.main.ui.medical_records

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.telehealthmanager.app.BaseApplication
import com.telehealthmanager.app.R
import com.telehealthmanager.app.base.BaseFragment
import com.telehealthmanager.app.data.PreferenceHelper
import com.telehealthmanager.app.databinding.FragmentMedicalRecordsBinding
import com.telehealthmanager.app.repositary.model.Medical
import com.telehealthmanager.app.ui.adapter.MedicalRecordsListAdapter
import kotlinx.android.synthetic.main.fragment_medical_records.*

class MedicalRecordsFragment : BaseFragment<FragmentMedicalRecordsBinding>(),
    MedicalRecordsNavigator {
    private val preferenceHelper = PreferenceHelper(BaseApplication.baseApplication)
    private lateinit var viewModel: MedicalRecordsViewModel
    private lateinit var mDataBinding: FragmentMedicalRecordsBinding

    private var medicalRecords: List<Medical> = ArrayList()

    override fun getLayoutId(): Int = R.layout.fragment_medical_records

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {
        mDataBinding = mViewDataBinding as FragmentMedicalRecordsBinding
        viewModel = ViewModelProviders.of(this).get(MedicalRecordsViewModel::class.java)
        mDataBinding.viewmodel = viewModel
        viewModel.navigator = this
        initApiCal()
        initAdapter()
        observeResponse()
    }

    private fun initApiCal() {
        showLoading()
        viewModel.getMedicalRecord()
    }

    private fun initAdapter() {
        mDataBinding.rvMedicalRecords.layoutManager = LinearLayoutManager(context) as RecyclerView.LayoutManager?
    }

    private fun observeResponse() {
        viewModel.mMedicalRecordResponse.observe(this, Observer {
            hideLoading()
            if (it != null && it.status == 200) {
                medicalRecords = it.medical
                if (medicalRecords.isNotEmpty()) {
                    mDataBinding.rvMedicalRecords.adapter = context?.let { MedicalRecordsListAdapter(medicalRecords, it) }
                }
            }
        })

        viewModel.getErrorObservable().observe(this, Observer<String> { message ->
            hideLoading()
        })
    }
}
