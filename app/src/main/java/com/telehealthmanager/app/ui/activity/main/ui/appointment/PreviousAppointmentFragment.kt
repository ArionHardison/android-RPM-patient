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
import com.telehealthmanager.app.databinding.FragmentPreviousAppointmentBinding
import com.telehealthmanager.app.repositary.WebApiConstants
import com.telehealthmanager.app.repositary.model.AppointmentResponse
import com.telehealthmanager.app.ui.activity.visitedDoctor.VisitedDoctorsDetailActivity
import com.telehealthmanager.app.ui.adapter.PreviousAppointmentsListAdapter
import com.telehealthmanager.app.utils.ViewUtils
import kotlinx.android.synthetic.main.fragment_previous_appointment.*
import java.io.Serializable

/**
 * A simple [Fragment] subclass.
 */
class PreviousAppointmentFragment : BaseFragment<FragmentPreviousAppointmentBinding>(),AppointmentNavigator,
    PreviousAppointmentsListAdapter.IAppointmentListener {

    val previousAppmts: ArrayList<String> = ArrayList()
    private lateinit var viewModel: AppointmentViewModel
    private lateinit var mDataBinding: FragmentPreviousAppointmentBinding
    private var mAdapter: PreviousAppointmentsListAdapter? = null
    private val ON_CHANGE_CODE = 100

    override fun getLayoutId(): Int = R.layout.fragment_previous_appointment

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {
        //addMenus()
        mDataBinding = mViewDataBinding as FragmentPreviousAppointmentBinding
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
        mAdapter = PreviousAppointmentsListAdapter( viewModel.mPreviouslist!!,activity!!,this)
        mDataBinding.adapter = mAdapter
        mDataBinding.rvPreviousAppointments.addItemDecoration(
            DividerItemDecoration(
                activity!!,
                DividerItemDecoration.VERTICAL
            )
        )
        mDataBinding.rvPreviousAppointments.layoutManager =LinearLayoutManager(activity!!)
        mAdapter!!.notifyDataSetChanged()
    }

    private fun observeResponse() {
        viewModel.mResponse.observe(this, Observer<AppointmentResponse> {
            hideLoading()
            viewModel.mUpcominglist = it.upcomming.appointments as MutableList<AppointmentResponse.Upcomming.Appointment>?
            viewModel.mPreviouslist = it.previous.appointments as MutableList<AppointmentResponse.Previous.Appointment>?

            if (viewModel.mPreviouslist!!.size > 0) {
                mDataBinding.tvNotFound.visibility = View.GONE
            } else {
                mDataBinding.tvNotFound.visibility = View.VISIBLE
            }
            mAdapter = PreviousAppointmentsListAdapter(viewModel.mPreviouslist!!,activity!!,this@PreviousAppointmentFragment)
            mDataBinding.adapter = mAdapter
            mAdapter!!.notifyDataSetChanged()
        })

        viewModel.getErrorObservable().observe(this, Observer<String> { message ->
            hideLoading()
            ViewUtils.showToast(activity!!, message, false)
        })
    }

    private fun addMenus() {
        previousAppmts.add("Dr.Bretto")
        previousAppmts.add("Dr.John")
        previousAppmts.add("Dr.Jenifer")
        previousAppmts.add("Dr.Virundha")
        rv_previous_appointments.layoutManager = LinearLayoutManager(context)
        //rv_previous_appointments.adapter = context?.let { PreviousAppointmentsListAdapter(previousAppmts, it) }
    }

    override fun onitemclick(selectedItem: AppointmentResponse.Previous.Appointment) {
        val intent = Intent(context, VisitedDoctorsDetailActivity::class.java)
        intent.putExtra(WebApiConstants.IntentPass.iscancel, false)
        intent.putExtra(WebApiConstants.IntentPass.Appointment, selectedItem as Serializable)
        startActivityForResult(intent, ON_CHANGE_CODE);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ON_CHANGE_CODE) {
            if (resultCode != Activity.RESULT_CANCELED) {
                initAdapter()
            }
        }
    }
}
