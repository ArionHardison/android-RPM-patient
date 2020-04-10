package com.midokter.app.ui.activity.main.ui.favourite_doctor


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager

import com.midokter.app.R
import com.midokter.app.base.BaseFragment
import com.midokter.app.databinding.FragmentFavouriteDoctorBinding
import com.midokter.app.ui.adapter.FavDoctorListAdapter
import com.midokter.app.ui.adapter.OnlineAppointmentListAdapter
import kotlinx.android.synthetic.main.fragment_favourite_doctor.*
import kotlinx.android.synthetic.main.fragment_online_consultation.*

/**
 * A simple [Fragment] subclass.
 */
class FavouriteDoctorFragment : BaseFragment<FragmentFavouriteDoctorBinding>() {

    val favDoctors: ArrayList<String> = ArrayList()

    override fun getLayoutId(): Int = R.layout.fragment_favourite_doctor

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {
        addFavDoctor()
        // Creates a vertical Layout Manager
        rv_fav_doctor.layoutManager = LinearLayoutManager(context)

        // You can use GridLayoutManager if you want multiple columns. Enter the number of columns as a parameter.
//        rv_animal_list.layoutManager = GridLayoutManager(this, 2)

        // Access the RecyclerView Adapter and load the data into it
        rv_fav_doctor.addItemDecoration(
            DividerItemDecoration(context,
                DividerItemDecoration.VERTICAL)
        )
        rv_fav_doctor.adapter = context?.let { FavDoctorListAdapter(favDoctors, it) }
    }

    private fun addFavDoctor() {
        favDoctors.add("Dr.Alvin")
        favDoctors.add("Dr.Richard")
        favDoctors.add("Dr.Glen Stwacy")
    }
}
