package com.midokter.app.ui.activity.main.ui.appointment


import android.content.Intent
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.midokter.app.R
import com.midokter.app.base.BaseFragment
import com.midokter.app.databinding.FragmentUpcomingAppointmentBinding
import com.midokter.app.repositary.model.AppointmentResponse
import com.midokter.app.ui.activity.visitedDoctor.VisitedDoctorsDetailActivity
import com.midokter.app.ui.adapter.IAppointmentListener
import com.midokter.app.ui.adapter.UpcomingAppointmentsListAdapter
import com.midokter.app.utils.ViewUtils
import kotlinx.android.synthetic.main.fragment_upcoming_appointment.*

/**
 * A simple [Fragment] subclass.
 */
class UpcomingAppointmentFragment : BaseFragment<FragmentUpcomingAppointmentBinding>(),AppointmentNavigator,IAppointmentListener {

    val upcomingAppmts: ArrayList<String> = ArrayList()
    private lateinit var viewModel: AppointmentViewModel
    private lateinit var mDataBinding: FragmentUpcomingAppointmentBinding
    private var mAdapter: UpcomingAppointmentsListAdapter? = null

    override fun getLayoutId(): Int = R.layout.fragment_upcoming_appointment

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {
        //addMenus()
        mDataBinding = mViewDataBinding as FragmentUpcomingAppointmentBinding
        viewModel = ViewModelProviders.of(this).get(AppointmentViewModel::class.java)
        mDataBinding.viewmodel = viewModel
        viewModel.navigator = this
        initAdapter()
        observeResponse()

    }
    private fun initAdapter() {
        showLoading()
        viewModel.getAppointment()
        mAdapter = UpcomingAppointmentsListAdapter( viewModel.mUpcominglist!!,activity!!,this)
        mDataBinding.adapter = mAdapter
        mDataBinding.rvUpcomingAppointments.addItemDecoration(
            DividerItemDecoration(
                activity!!,
                DividerItemDecoration.VERTICAL
            )
        )
        mDataBinding.rvUpcomingAppointments.layoutManager =LinearLayoutManager(activity!!)
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
            mAdapter = UpcomingAppointmentsListAdapter(viewModel.mUpcominglist!!,activity!!,this)
            mDataBinding.adapter = mAdapter
            mAdapter!!.notifyDataSetChanged()
            hideLoading()




        })
        viewModel.getErrorObservable().observe(this, Observer<String> { message ->
            hideLoading()
            ViewUtils.showToast(activity!!, message, false)
        })
    }
    override fun onitemclick(selectedItem: AppointmentResponse.Upcomming.Appointment) {
        val intent = Intent(activity!!, VisitedDoctorsDetailActivity::class.java)
       startActivity(intent);
    }

    override fun onCancelclick(selectedItem: AppointmentResponse.Upcomming.Appointment) {

    }

    private fun addMenus() {
        upcomingAppmts.add("Dr.Bretto")
        rv_upcoming_appointments.layoutManager = LinearLayoutManager(context)
        //rv_upcoming_appointments.adapter = context?.let { UpcomingAppointmentsListAdapter(upcomingAppmts, it) }
    }
}
