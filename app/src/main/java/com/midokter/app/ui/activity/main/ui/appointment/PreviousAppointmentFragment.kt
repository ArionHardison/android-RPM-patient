package com.midokter.app.ui.activity.main.ui.appointment


import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.midokter.app.R
import com.midokter.app.base.BaseFragment
import com.midokter.app.databinding.FragmentPreviousAppointmentBinding
import com.midokter.app.ui.adapter.PreviousAppointmentsListAdapter
import kotlinx.android.synthetic.main.fragment_previous_appointment.*

/**
 * A simple [Fragment] subclass.
 */
class PreviousAppointmentFragment : BaseFragment<FragmentPreviousAppointmentBinding>() {

    val previousAppmts: ArrayList<String> = ArrayList()


    override fun getLayoutId(): Int = R.layout.fragment_previous_appointment

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {
        addMenus()
        // Creates a vertical Layout Manager
        rv_previous_appointments.layoutManager = LinearLayoutManager(context)

        // You can use GridLayoutManager if you want multiple columns. Enter the number of columns as a parameter.
//        rv_animal_list.layoutManager = GridLayoutManager(this, 2)

        // Access the RecyclerView Adapter and load the data into it
        rv_previous_appointments.adapter = context?.let { PreviousAppointmentsListAdapter(previousAppmts, it) }
    }

    private fun addMenus() {
        previousAppmts.add("Dr.Bretto")
        previousAppmts.add("Dr.John")
        previousAppmts.add("Dr.Jenifer")
        previousAppmts.add("Dr.Virundha")
    }
}
