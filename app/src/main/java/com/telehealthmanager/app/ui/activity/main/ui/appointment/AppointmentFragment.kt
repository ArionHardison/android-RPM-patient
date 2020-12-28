package com.telehealthmanager.app.ui.activity.main.ui.appointment

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.telehealthmanager.app.R
import com.telehealthmanager.app.base.BaseFragment
import com.telehealthmanager.app.databinding.FragmentAppointmentBinding
import com.telehealthmanager.app.repositary.model.Appointment
import com.telehealthmanager.app.repositary.model.AppointmentResponse
import com.telehealthmanager.app.ui.adapter.MyPagerAdapter
import com.telehealthmanager.app.utils.ViewUtils
import kotlinx.android.synthetic.main.fragment_appointment.*


class AppointmentFragment : BaseFragment<FragmentAppointmentBinding>(), AppointmentNavigator {

    private lateinit var viewModel: AppointmentViewModel
    private lateinit var mDataBinding: FragmentAppointmentBinding
    override fun getLayoutId(): Int = R.layout.fragment_appointment

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {
        val fragmentAdapter = this.childFragmentManager!!.let { MyPagerAdapter(it) }
        // val fragmentAdapter = MyPagerAdapter(this.childFragmentManager)
        viewpager_main.adapter = fragmentAdapter
        tabs_main.setupWithViewPager(viewpager_main)
        setHasOptionsMenu(false);
        mDataBinding = mViewDataBinding as FragmentAppointmentBinding
        viewModel = ViewModelProvider(this).get(AppointmentViewModel::class.java)
        mDataBinding.viewmodel = viewModel
        viewModel.navigator = this

       /* initApiCal()
        observeResponse()*/
    }

    private fun initApiCal() {
        showLoading()
        viewModel.getAppointment()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear();
    }

   /* private fun observeResponse() {

        viewModel.mResponse.observe(this, Observer<AppointmentResponse> {
            hideLoading()
            viewModel.mUpcomingList = it.upcomming.appointments as MutableList<Appointment>?
            viewModel.mPreviousList = it.previous.appointments as MutableList<Appointment>?
        })
        viewModel.getErrorObservable().observe(this, Observer<String> { message ->
            hideLoading()
            ViewUtils.showToast(requireActivity(), message, false)
        })
    }
*/

}