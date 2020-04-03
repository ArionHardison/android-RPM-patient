package com.midokter.app.ui.activity.searchDoctor

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.midokter.app.R
import com.midokter.app.base.BaseActivity
import com.midokter.app.databinding.ActivitySearchDoctorBinding
import com.midokter.app.ui.adapter.SearchDoctorsListAdapter
import kotlinx.android.synthetic.main.content_search_doctor.*

class SearchDoctorActivity : BaseActivity<ActivitySearchDoctorBinding>() {

    val searchDoctors: ArrayList<String> = ArrayList()


    override fun getLayoutId(): Int = R.layout.activity_search_doctor

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        addSearchDoctors()
        // Creates a vertical Layout Manager
        rv_serach_doctors.layoutManager = LinearLayoutManager(applicationContext)

        // You can use GridLayoutManager if you want multiple columns. Enter the number of columns as a parameter.
//        rv_animal_list.layoutManager = GridLayoutManager(this, 2)

        // Access the RecyclerView Adapter and load the data into it
        rv_serach_doctors.addItemDecoration(
            DividerItemDecoration(applicationContext,
                DividerItemDecoration.VERTICAL))
        rv_serach_doctors.adapter = applicationContext?.let { SearchDoctorsListAdapter(searchDoctors, it) }
    }

    private fun addSearchDoctors() {
        searchDoctors.add("Dr.Bretto")
        searchDoctors.add("Dr.John")
        searchDoctors.add("Dr.Jenifer")
        searchDoctors.add("Dr.Virundha")
    }
}
