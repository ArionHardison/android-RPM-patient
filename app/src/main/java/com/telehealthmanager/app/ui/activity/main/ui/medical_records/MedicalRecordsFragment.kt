package com.telehealthmanager.app.ui.activity.main.ui.medical_records

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.telehealthmanager.app.R
import com.telehealthmanager.app.base.BaseFragment
import com.telehealthmanager.app.data.Constant
import com.telehealthmanager.app.databinding.FragmentMedicalRecordsBinding
import com.telehealthmanager.app.repositary.WebApiConstants
import com.telehealthmanager.app.repositary.model.MedicalRecord
import com.telehealthmanager.app.ui.activity.addmedicalrecord.AddMedicalRecords
import com.telehealthmanager.app.ui.activity.addmedicalrecord.DoctorMedicalRecords
import com.telehealthmanager.app.ui.adapter.IMedicalRecordClick
import com.telehealthmanager.app.ui.adapter.MedicalRecordsListAdapter
import com.telehealthmanager.app.utils.ViewUtils

class MedicalRecordsFragment : BaseFragment<FragmentMedicalRecordsBinding>(),
    MedicalRecordsNavigator, IMedicalRecordClick {

    private lateinit var viewModel: MedicalRecordsViewModel
    private lateinit var mDataBinding: FragmentMedicalRecordsBinding
    private var mAdapter: MedicalRecordsListAdapter? = null

    override fun getLayoutId(): Int = R.layout.fragment_medical_records

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {
        mDataBinding = mViewDataBinding as FragmentMedicalRecordsBinding
        viewModel = ViewModelProvider(this).get(MedicalRecordsViewModel::class.java)
        mDataBinding.viewmodel = viewModel
        viewModel.navigator = this
        initApiCal()
        initAdapter()
        observeResponse()

        mDataBinding.rvMedicalRecords.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && mDataBinding.addMedicalRecord.isShown) {
                    mDataBinding.addMedicalRecord.hide()
                } else if (dy < 0 && !mDataBinding.addMedicalRecord.isShown) {
                    mDataBinding.addMedicalRecord.show()
                }
            }
        })

    }

    private fun initApiCal() {
        viewModel.loadingProgress.value = true
        viewModel.getMedicalRecord()
    }

    private fun initAdapter() {
        mDataBinding.rvMedicalRecords.layoutManager = LinearLayoutManager(activity!!)
        mDataBinding.rvMedicalRecords.addItemDecoration(DividerItemDecoration(activity!!, DividerItemDecoration.VERTICAL))
    }

    private fun observeResponse() {

        viewModel.loadingProgress.observe(this, Observer {
            if (it)
                showLoading()
            else
                hideLoading()
        })

        viewModel.mMedicalRecordResponse.observe(this, Observer {
            viewModel.loadingProgress.value = false
            if (it != null && it.status == 200) {
                if (!it.medical.isNullOrEmpty()) {
                    mDataBinding.tvNotFound.visibility = View.GONE
                    viewModel.mRecordList = it.medical as MutableList<MedicalRecord.Medical>?
                    mAdapter = MedicalRecordsListAdapter(viewModel.mRecordList!!, activity!!, this)
                    mDataBinding.adapter = mAdapter
                    mAdapter!!.notifyDataSetChanged()
                } else {
                    mDataBinding.tvNotFound.visibility = View.VISIBLE
                }
            }
        })

        viewModel.getErrorObservable().observe(this, Observer<String> { message ->
            viewModel.loadingProgress.value = false
            ViewUtils.showToast(activity!!, message, false)
        })
    }

    override fun goToMedicalRecord() {
        val intent = Intent(activity!!, AddMedicalRecords::class.java)
        startActivityForResult(intent, Constant.ADD_MEDICAL_RECORD)
    }

    override fun onItemClicked(item: MedicalRecord.Medical) {
        val intent = Intent(context, DoctorMedicalRecords::class.java)
        intent.putExtra(WebApiConstants.IntentPass.ID, item.id)
        startActivityForResult(intent, Constant.ADD_MEDICAL_RECORD)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constant.ADD_MEDICAL_RECORD) {
            if (resultCode != Activity.RESULT_CANCELED) {
                initApiCal()
            }
        }
    }
}
