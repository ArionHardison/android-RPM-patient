package com.midokter.app.ui.activity.main.ui.online_appointments

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.midokter.app.R
import com.midokter.app.base.BaseFragment
import com.midokter.app.databinding.FragmentOnlineAppointmentsBinding
import com.midokter.app.ui.adapter.MenuAdapter
import com.midokter.app.ui.adapter.OnlineAppointmentListAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_online_appointments.*

class OnlineAppointmentsFragment : BaseFragment<FragmentOnlineAppointmentsBinding>() {

    val onlineAppmts: ArrayList<String> = ArrayList()

    override fun getLayoutId(): Int = R.layout.fragment_online_appointments

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {
        addMenus()
        // Creates a vertical Layout Manager
        rv_online_consultation.layoutManager = LinearLayoutManager(context)

        // You can use GridLayoutManager if you want multiple columns. Enter the number of columns as a parameter.
//        rv_animal_list.layoutManager = GridLayoutManager(this, 2)

        // Access the RecyclerView Adapter and load the data into it
        rv_online_consultation.adapter = context?.let { OnlineAppointmentListAdapter(onlineAppmts, it) }
    }

    private fun addMenus() {
        onlineAppmts.add("Dr.Bretto")
        onlineAppmts.add("Dr.John")
        onlineAppmts.add("Dr.Jenifer")
        onlineAppmts.add("Dr.Virundha")
    }
}