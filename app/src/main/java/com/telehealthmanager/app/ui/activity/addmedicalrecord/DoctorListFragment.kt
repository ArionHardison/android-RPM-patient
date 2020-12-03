package com.telehealthmanager.app.ui.activity.addmedicalrecord

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.telehealthmanager.app.R
import com.telehealthmanager.app.base.BaseBottomDialogFragment
import com.telehealthmanager.app.databinding.FragmentDoctorListBinding
import com.telehealthmanager.app.repositary.model.ResponseDoctors
import com.telehealthmanager.app.ui.adapter.DoctorListAdapter
import com.telehealthmanager.app.ui.adapter.IDocClickListener


class DoctorListFragment : BaseBottomDialogFragment<FragmentDoctorListBinding>(), DoctorMedicalRecordsNavigator, IDocClickListener {
    private lateinit var viewModel: DoctorMedicalRecordsViewModel
    private lateinit var mDataBinding: FragmentDoctorListBinding
    private var mAdapter: DoctorListAdapter? = null
    private var iSelectDoctor: ISelectDoctor? = null

    companion object {
        fun newInstance() = DoctorListFragment()
    }

    override val layoutId: Int get() = R.layout.fragment_doctor_list

    override fun initView(mRootView: View, mViewDataBinding: ViewDataBinding) {
        mDataBinding = mViewDataBinding as FragmentDoctorListBinding
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DoctorMedicalRecordsViewModel::class.java)
        viewModel.navigator = this
        initApi()
        initObservable()
        mDataBinding.docName.setOnClickListener {
            iSelectDoctor!!.onDocClicked("0", "Others")
            dismiss()
        }
    }

    private fun initApi() {
        viewModel.loadingProgress.value = true
        viewModel.getDoctorsList()
    }

    private fun initObservable() {
        viewModel.loadingProgress.observe(this, Observer {
            if (it)
                showLoading()
            else
                hideLoading()
        })

        viewModel.mResponseDoctors.observe(this, Observer {
            viewModel.loadingProgress.value = false
            if (!it.doctor.isNullOrEmpty()) {
                viewModel.mDoctorsList = it.doctor as MutableList<ResponseDoctors.AllDoctors>
                mDataBinding.rvDoctorsList.addItemDecoration(DividerItemDecoration(activity!!, DividerItemDecoration.VERTICAL))
                mDataBinding.rvDoctorsList.layoutManager = LinearLayoutManager(activity!!)
                mAdapter = DoctorListAdapter(viewModel.mDoctorsList!!, activity!!, this)
                mDataBinding.adapter = mAdapter
                mAdapter!!.notifyDataSetChanged()
            }
        })

    }

    override fun onDocClicked(item: ResponseDoctors.AllDoctors) {
        iSelectDoctor!!.onDocClicked(item.id.toString(), item.first_name + " " + item.last_name)
        dialog!!.dismiss()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ISelectDoctor) {
            iSelectDoctor = context as ISelectDoctor
        }
    }

    override fun onDetach() {
        super.onDetach()
        iSelectDoctor=null
    }

    fun setClickListener(iListener: ISelectDoctor){
        iSelectDoctor=iListener
    }

    interface ISelectDoctor {
        fun onDocClicked(docID: String, docName: String)
    }
}

