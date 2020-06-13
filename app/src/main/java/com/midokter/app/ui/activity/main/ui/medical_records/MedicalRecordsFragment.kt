package com.midokter.app.ui.activity.main.ui.medical_records

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.midokter.app.BaseApplication
import com.midokter.app.R
import com.midokter.app.base.BaseFragment
import com.midokter.app.data.PreferenceHelper
import com.midokter.app.databinding.FragmentMedicalRecordsBinding
import com.midokter.app.repositary.model.Medical
import com.midokter.app.ui.adapter.MedicalRecordsListAdapter
import com.midokter.app.utils.ViewUtils
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
        rv_medical_records.layoutManager = LinearLayoutManager(context) as RecyclerView.LayoutManager?
        // rv_medical_records.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }

    private fun observeResponse() {
        viewModel.mMedicalRecordResponse.observe(this, Observer {
            hideLoading()
            if (it != null && it.status == 200) {
                medicalRecords = it.medical
                if (medicalRecords.isNotEmpty()) {
                    rv_medical_records.adapter = context?.let { MedicalRecordsListAdapter(medicalRecords, it) }
                } else {
                    ViewUtils.showToast(activity!!, "No Record found", false)
                }
            }
        })

        viewModel.getErrorObservable().observe(this, Observer<String> { message ->
            hideLoading()
        })
    }
}
