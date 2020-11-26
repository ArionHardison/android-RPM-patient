package com.telehealthmanager.app.ui.activity.main.ui.appointment


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.telehealthmanager.app.R
import com.telehealthmanager.app.base.BaseFragment
import com.telehealthmanager.app.databinding.FragmentUpcomingAppointmentBinding
import com.telehealthmanager.app.repositary.WebApiConstants
import com.telehealthmanager.app.repositary.model.Appointment
import com.telehealthmanager.app.repositary.model.AppointmentResponse
import com.telehealthmanager.app.repositary.model.Response
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
    private var mAdapter: UpcomingAppointmentsListAdapter? = null
    private val ON_REQUEST_CANCEL = 100

    override fun getLayoutId(): Int = R.layout.fragment_upcoming_appointment

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {
        mDataBinding = mViewDataBinding as FragmentUpcomingAppointmentBinding
        viewModel = ViewModelProviders.of(this).get(AppointmentViewModel::class.java)
        mDataBinding.viewmodel = viewModel
        viewModel.navigator = this
        initApiCal()
        initAdapter()
        observeResponse()
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
        showLoading()
        viewModel.getAppointment()
    }

    private fun initAdapter() {
        mAdapter = UpcomingAppointmentsListAdapter(viewModel.mUpcomingList!!, activity!!, this)
        mDataBinding.adapter = mAdapter
        mDataBinding.rvUpcomingAppointments.addItemDecoration(DividerItemDecoration(activity!!, DividerItemDecoration.VERTICAL))
        mDataBinding.rvUpcomingAppointments.layoutManager = LinearLayoutManager(activity!!)
        mAdapter!!.notifyDataSetChanged()
    }

    private fun observeResponse() {
        viewModel.mResponse.observe(this, Observer<AppointmentResponse> {
            hideLoading()
            viewModel.mUpcomingList = it.upcomming.appointments as MutableList<Appointment>?
            viewModel.mPreviousList = it.previous.appointments as MutableList<Appointment>?
            if (viewModel.mUpcomingList!!.size > 0) {
                mDataBinding.tvNotFound.visibility = View.GONE
            } else {
                mDataBinding.tvNotFound.visibility = View.VISIBLE
            }
            mAdapter = UpcomingAppointmentsListAdapter(viewModel.mUpcomingList!!, activity!!, this)
            mDataBinding.adapter = mAdapter
            mAdapter!!.notifyDataSetChanged()
            hideLoading()
        })

        viewModel.getErrorObservable().observe(this, Observer<String> { message ->
            hideLoading()
            ViewUtils.showToast(activity!!, message, false)
        })

        viewModel.mCancelResponse.observe(this, Observer<Response> {
            hideLoading()
            if (it.status)
                ViewUtils.showToast(activity!!, it.message, true)
            initApiCal()
        })
    }

    override fun onItemClick(selectedItem: Appointment) {
        val intent = Intent(activity!!, VisitedDoctorsDetailActivity::class.java)
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
        showLoading()
        val hashMap: HashMap<String, Any> = HashMap()
        hashMap[WebApiConstants.AddAppointment.ID] = selectedItem.id.toString()
        viewModel.cancelAppointment(hashMap)
    }
}
