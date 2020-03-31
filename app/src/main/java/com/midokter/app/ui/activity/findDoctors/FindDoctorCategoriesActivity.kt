package com.midokter.app.ui.activity.findDoctors

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.midokter.app.R
import com.midokter.app.base.BaseActivity
import com.midokter.app.databinding.ActivityFindDoctorCategoriesBinding
import com.midokter.app.ui.adapter.CategoriesListAdapter
import kotlinx.android.synthetic.main.activity_find_doctor_categories.*
import kotlinx.android.synthetic.main.activity_visited_doctors.*

class FindDoctorCategoriesActivity : BaseActivity<ActivityFindDoctorCategoriesBinding>() {

    val categories: ArrayList<String> = ArrayList()

    override fun getLayoutId(): Int = R.layout.activity_find_doctor_categories

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        categoriesList()
        rv_categories.layoutManager = GridLayoutManager(applicationContext,2)
        rv_categories.adapter = applicationContext?.let { CategoriesListAdapter(categories, it) }
    }

    private fun categoriesList() {
        categories.add("Womens Health")
        categories.add("Skin & Hair")
        categories.add("Child Specialist")
        categories.add("General Physician")
        categories.add("Dental care")
        categories.add("Ear Nose Throat")
        categories.add("Ayurveda")
        categories.add("Bone & Join")
        categories.add("Sex Specialist")
        categories.add("Eye Specialist")
        categories.add("Digestive Issue")
        categories.add("Mental Wellness")
        categories.add("Physiotheraphy")
        categories.add("Diabetes Management")
        categories.add("Lungs & Breathing")
        categories.add("Heart")
    }
}
