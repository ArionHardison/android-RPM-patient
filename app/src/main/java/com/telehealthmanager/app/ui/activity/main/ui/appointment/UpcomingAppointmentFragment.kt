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
import com.telehealthmanager.app.databinding.FragmentUpcomingAppointmentBinding
import com.telehealthmanager.app.repositary.WebApiConstants
import com.telehealthmanager.app.repositary.model.Appointment
import com.telehealthmanager.app.ui.activity.visitedDoctor.VisitedDoctorsDetailActivity
import com.telehealthmanager.app.ui.adapter.IAppointmentListener
import com.telehealthmanager.app.ui.adapter.UpcomingAppointmentsListAdapter
import com.telehealthmanager.app.utils.ViewUtils
import java.io.Serializable
import java.util.*
import kotlin.collections.set

/**
 * A simple [Fragment] subclass.
 */
class UpcomingAppointmentFragment : BaseFragment<FragmentUpcomingAppointmentBinding>(),
    AppointmentNavigator, IAppointmentListener {

    private lateinit var viewModel: AppointmentViewModel
    private lateinit var mDataBinding: FragmentUpcomingAppointmentBinding
    private val ON_REQUEST_CANCEL = 100

    override fun getLayoutId(): Int = R.layout.fragment_upcoming_appointment

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {
        mDataBinding = mViewDataBinding as FragmentUpcomingAppointmentBinding
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
        viewModel.upcomingProgress.value = true
        viewModel.getAppointment()
    }

    private fun initAdapter() {
        val mAdapter = UpcomingAppointmentsListAdapter(this)
        mDataBinding.rvUpcomingAppointments.addItemDecoration(DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL))
        mDataBinding.rvUpcomingAppointments.layoutManager = LinearLayoutManager(requireActivity())
        mDataBinding.rvUpcomingAppointments.adapter = mAdapter
        viewModel.mResponseUpcoming.observe(this, {
            viewModel.upcomingProgress.value = false
            if (!it.upcomming.appointments.isNullOrEmpty()) {
                mDataBinding.tvNotFound.visibility = View.GONE
                mAdapter.addItem(it.upcomming.appointments.toMutableList())
            } else {
                mDataBinding.tvNotFound.visibility = View.VISIBLE
            }
        })

    }

    private fun observeResponse() {

        viewModel.getErrorObservable().observe(this, { message ->
            viewModel.loadingProgress.value = false
            ViewUtils.showToast(requireActivity(), message, false)
        })

        viewModel.mCancelResponse.observe(this, {
            viewModel.loadingProgress.value = false
            if (it.status)
                ViewUtils.showToast(requireActivity(), it.message, true)
            initApiCal()
        })

        viewModel.loadingProgress.observe(this, {
            if (it)
                showLoading()
            else
                hideLoading()
        })

        viewModel.upcomingProgress.observe(this, {
            mDataBinding.isProgressVisible = it
        })
    }

    override fun onItemClick(selectedItem: Appointment) {
        val intent = Intent(requireActivity(), VisitedDoctorsDetailActivity::class.java)
        intent.putExtra(WebApiConstants.IntentPass.iscancel, true)
        intent.putExtra(WebApiConstants.IntentPass.Appointment, selectedItem as Serializable)
        startActivityForResult(intent, ON_REQUEST_CANCEL);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ON_REQUEST_CANCEL) {
            if (resultCode != Activity.RESULT_CANCELED) {
                initApiCal()
            }
        }
    }

    override fun onCancelClick(selectedItem: Appointment) {
        viewModel.loadingProgress.value = true
        val hashMap: HashMap<String, Any> = HashMap()
        hashMap[WebApiConstants.AddAppointment.ID] = selectedItem.id.toString()
        viewModel.cancelAppointment(hashMap)
    }
}
