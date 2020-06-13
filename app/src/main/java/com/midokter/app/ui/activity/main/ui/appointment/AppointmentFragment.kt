package com.midokter.app.ui.activity.main.ui.appointment

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.midokter.app.R
import com.midokter.app.base.BaseFragment
import com.midokter.app.databinding.FragmentAppointmentBinding
import com.midokter.app.repositary.model.AppointmentResponse
import com.midokter.app.ui.adapter.MyPagerAdapter
import com.midokter.app.utils.ViewUtils
import kotlinx.android.synthetic.main.fragment_appointment.*




class AppointmentFragment : BaseFragment<FragmentAppointmentBinding>(),AppointmentNavigator {

  private lateinit var viewModel: AppointmentViewModel
    private lateinit var mDataBinding: FragmentAppointmentBinding
    override fun getLayoutId(): Int = R.layout.fragment_appointment

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {


       val fragmentAdapter =this.childFragmentManager!!.let { MyPagerAdapter(it) }
       // val fragmentAdapter = MyPagerAdapter(this.childFragmentManager)
        viewpager_main.adapter = fragmentAdapter
        tabs_main.setupWithViewPager(viewpager_main)
        setHasOptionsMenu(false);
        mDataBinding = mViewDataBinding as FragmentAppointmentBinding
        viewModel = ViewModelProviders.of(this).get(AppointmentViewModel::class.java)
        mDataBinding.viewmodel = viewModel
        viewModel.navigator = this

        initApiCal()
        observeResponse()
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

    private fun observeResponse() {

        viewModel.mResponse.observe(this, Observer<AppointmentResponse> {

            hideLoading()
            viewModel.mUpcominglist = it.upcomming.appointments as MutableList<AppointmentResponse.Upcomming.Appointment>?
            viewModel.mPreviouslist = it.previous.appointments as MutableList<AppointmentResponse.Previous.Appointment>?





        })
        viewModel.getErrorObservable().observe(this, Observer<String> { message ->
           hideLoading()
            ViewUtils.showToast(activity!!, message, false)
        })
    }


}