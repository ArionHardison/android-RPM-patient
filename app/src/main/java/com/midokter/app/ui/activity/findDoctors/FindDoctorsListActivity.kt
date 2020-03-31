package com.midokter.app.ui.activity.findDoctors

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.midokter.app.R
import com.midokter.app.base.BaseActivity
import com.midokter.app.databinding.ActivityFindDoctorsListBinding
import com.midokter.app.ui.adapter.FindDoctorListAdapter
import kotlinx.android.synthetic.main.activity_find_doctor_categories.*
import kotlinx.android.synthetic.main.activity_find_doctors_list.*

class FindDoctorsListActivity : BaseActivity<ActivityFindDoctorsListBinding>() {

    val doctorList: ArrayList<String> = ArrayList()

    override fun getLayoutId(): Int = R.layout.activity_find_doctors_list

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        categoriesList()
        rv_finddoctors_list.layoutManager = LinearLayoutManager(applicationContext)
        rv_finddoctors_list.adapter = applicationContext?.let { FindDoctorListAdapter(doctorList, it) }
    }

    private fun categoriesList() {
        doctorList.add("Dr.Alvin")
        doctorList.add("Dr.Madison")
        doctorList.add("Dr.Joe")
        doctorList.add("Dr.Mellisa")
        doctorList.add("Dr.Glen")
    }
}
