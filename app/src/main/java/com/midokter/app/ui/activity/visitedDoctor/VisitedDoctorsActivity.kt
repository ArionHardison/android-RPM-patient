package com.midokter.app.ui.activity.visitedDoctor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.midokter.app.R
import com.midokter.app.base.BaseActivity
import com.midokter.app.databinding.ActivityVisitedDoctorsBinding
import com.midokter.app.ui.adapter.OnlineAppointmentListAdapter
import com.midokter.app.ui.adapter.VisitedDoctorsListAdapter
import kotlinx.android.synthetic.main.activity_visited_doctors.*
import kotlinx.android.synthetic.main.fragment_online_consultation.*

class VisitedDoctorsActivity : BaseActivity<ActivityVisitedDoctorsBinding>() {

    val visitedDoctors: ArrayList<String> = ArrayList()

    override fun getLayoutId(): Int = R.layout.activity_visited_doctors

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        addVisitedDoctors()
        rv_visited_doctors.layoutManager = LinearLayoutManager(applicationContext)
        rv_visited_doctors.adapter = applicationContext?.let { VisitedDoctorsListAdapter(visitedDoctors, it) }
    }

    private fun addVisitedDoctors() {
        visitedDoctors.add("Dr.Bretto")
        visitedDoctors.add("Dr.John")
        visitedDoctors.add("Dr.Jenifer")
        visitedDoctors.add("Dr.Virundha")
    }
}
