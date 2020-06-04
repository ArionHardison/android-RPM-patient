package com.midokter.app.ui.activity.main.ui.appointment


import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.midokter.app.R
import com.midokter.app.base.BaseFragment
import com.midokter.app.databinding.FragmentPreviousAppointmentBinding
import com.midokter.app.repositary.model.AppointmentResponse
import com.midokter.app.ui.adapter.PreviousAppointmentsListAdapter
import com.midokter.app.utils.ViewUtils
import kotlinx.android.synthetic.main.fragment_previous_appointment.*

/**
 * A simple [Fragment] subclass.
 */
class PreviousAppointmentFragment : BaseFragment<FragmentPreviousAppointmentBinding>(),AppointmentNavigator {

    val previousAppmts: ArrayList<String> = ArrayList()
    private lateinit var viewModel: AppointmentViewModel
    private lateinit var mDataBinding: FragmentPreviousAppointmentBinding
    private var mAdapter: PreviousAppointmentsListAdapter? = null

    override fun getLayoutId(): Int = R.layout.fragment_previous_appointment

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {
        //addMenus()
        mDataBinding = mViewDataBinding as FragmentPreviousAppointmentBinding
        viewModel = ViewModelProviders.of(this).get(AppointmentViewModel::class.java)
        mDataBinding.viewmodel = viewModel
        viewModel.navigator = this
        initAdapter()
        observeResponse()

    }
    private fun initAdapter() {
        showLoading()
        viewModel.getAppointment()
        mAdapter = PreviousAppointmentsListAdapter( viewModel.mPreviouslist!!,activity!!)
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
            mAdapter = PreviousAppointmentsListAdapter(viewModel.mPreviouslist!!,activity!!)
            mDataBinding.adapter = mAdapter
            mAdapter!!.notifyDataSetChanged()
            hideLoading()




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
}
