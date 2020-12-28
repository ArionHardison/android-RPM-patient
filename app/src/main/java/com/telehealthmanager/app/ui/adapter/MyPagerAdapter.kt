package com.telehealthmanager.app.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.telehealthmanager.app.ui.activity.main.ui.appointment.PreviousAppointmentFragment
import com.telehealthmanager.app.ui.activity.main.ui.appointment.UpcomingAppointmentFragment

class MyPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> UpcomingAppointmentFragment()
            1 -> PreviousAppointmentFragment()
            else -> UpcomingAppointmentFragment()
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "Upcoming"
            1 -> "Previous"
            else -> {
                return ""
            }
        }
    }
}