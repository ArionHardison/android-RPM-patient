package com.midokter.app.ui.activity.main.ui.appointment

import android.view.View
import androidx.databinding.ViewDataBinding
import com.midokter.app.R
import com.midokter.app.base.BaseFragment
import com.midokter.app.databinding.FragmentAppointmentBinding
import com.midokter.app.ui.adapter.MyPagerAdapter
import kotlinx.android.synthetic.main.fragment_appointment.*

class AppointmentFragment : BaseFragment<FragmentAppointmentBinding>() {


    override fun getLayoutId(): Int = R.layout.fragment_appointment

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {
        val fragmentAdapter = activity?.supportFragmentManager?.let { MyPagerAdapter(it) }
        viewpager_main.adapter = fragmentAdapter
        tabs_main.setupWithViewPager(viewpager_main)
    }
}