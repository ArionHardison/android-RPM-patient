package com.midokter.app.ui.activity.main.ui.remainder

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.midokter.app.R
import com.midokter.app.base.BaseFragment
import com.midokter.app.databinding.FragmentRemainderBinding
import com.midokter.app.ui.adapter.FavDoctorListAdapter
import com.midokter.app.ui.adapter.RemainderListAdapter
import kotlinx.android.synthetic.main.fragment_favourite_doctor.*
import kotlinx.android.synthetic.main.fragment_remainder.*

class RemainderFragment : BaseFragment<FragmentRemainderBinding>() {

    val remainders: ArrayList<String> = ArrayList()

    override fun getLayoutId(): Int = R.layout.fragment_remainder;

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {
        addRemainder()
        // Creates a vertical Layout Manager
        rv_remainder.layoutManager = LinearLayoutManager(context)

        // You can use GridLayoutManager if you want multiple columns. Enter the number of columns as a parameter.
//        rv_animal_list.layoutManager = GridLayoutManager(this, 2)

        // Access the RecyclerView Adapter and load the data into it
        rv_remainder.adapter = context?.let { RemainderListAdapter(remainders, it) }
    }

    private fun addRemainder() {
        remainders.add("Fever Appointment")
        remainders.add("Personal Care Consultation")
    }

}