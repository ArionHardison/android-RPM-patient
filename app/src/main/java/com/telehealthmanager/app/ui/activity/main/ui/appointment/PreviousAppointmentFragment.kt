package com.telehealthmanager.app.ui.activity.main.ui.appointment


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.telehealthmanager.app.R
import com.telehealthmanager.app.base.BaseFragment
import com.telehealthmanager.app.databinding.FragmentPreviousAppointmentBinding
import com.telehealthmanager.app.repositary.WebApiConstants
import com.telehealthmanager.app.repositary.model.Appointment
import com.telehealthmanager.app.ui.activity.visitedDoctor.VisitedDoctorsDetailActivity
import com.telehealthmanager.app.ui.adapter.PreviousAppointmentsListAdapter
import com.telehealthmanager.app.utils.ViewUtils
import java.io.Serializable

/**
 * A simple [Fragment] subclass.
 */
class PreviousAppointmentFragment : BaseFragment<FragmentPreviousAppointmentBinding>(), AppointmentNavigator,
    PreviousAppointmentsListAdapter.IAppointmentListener {

    private lateinit var viewModel: AppointmentViewModel
    private lateinit var mDataBinding: FragmentPreviousAppointmentBinding
    private val ON_CHANGE_CODE = 100
    val mAdapter = PreviousAppointmentsListAdapter(this)

    override fun getLayoutId(): Int = R.layout.fragment_previous_appointment

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {
        mDataBinding = mViewDataBinding as FragmentPreviousAppointmentBinding
        viewModel = ViewModelProvider(this).get(AppointmentViewModel::class.java)
        mDataBinding.viewmodel = viewModel
        viewModel.navigator = this
        initAdapter()
        observeResponse()
        initApiCal()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear();
    }

    private fun initApiCal() {
        viewModel.previousProgress.value = true
        viewModel.getAppointment()
    }

    private fun initAdapter() {

        mDataBinding.rvPreviousAppointments.addItemDecoration(DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL))
        mDataBinding.rvPreviousAppointments.adapter = mAdapter
        viewModel.mResponsePrevious.observe(this, {
            viewModel.previousProgress.value = false
            if (!it.previous.appointments.isNullOrEmpty()) {
                mDataBinding.tvNotFound.visibility = View.GONE
                mAdapter.addItem(it.previous.appointments.toMutableList())
            } else {
                mDataBinding.tvNotFound.visibility = View.VISIBLE
            }
        })
    }

    private fun observeResponse() {
        viewModel.getErrorObservable().observe(this, { message ->
            mDataBinding.isProgressVisible = false
            ViewUtils.showToast(requireActivity(), message, false)
        })

        viewModel.previousProgress.observe(this, {
            mDataBinding.isProgressVisible = it
        })
    }

    override fun onItemClick(selectedItem: Appointment) {
        val intent = Intent(requireActivity(), VisitedDoctorsDetailActivity::class.java)
        intent.putExtra(WebApiConstants.IntentPass.iscancel, false)
        intent.putExtra(WebApiConstants.IntentPass.Appointment, selectedItem as Serializable)
        startActivityForResult(intent, ON_CHANGE_CODE);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ON_CHANGE_CODE) {
            if (resultCode != Activity.RESULT_CANCELED) {
                initApiCal()
            }
        }
    }
}
