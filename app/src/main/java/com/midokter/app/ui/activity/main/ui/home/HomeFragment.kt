package com.midokter.app.ui.activity.main.ui.home

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.midokter.app.R
import com.midokter.app.base.BaseFragment
import com.midokter.app.databinding.FragmentHomeBinding
import com.midokter.app.ui.adapter.MenuAdapter
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    val menus: ArrayList<String> = ArrayList()

    override fun getLayoutId(): Int = R.layout.fragment_home

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {
        addMenus()
        // Creates a vertical Layout Manager
        recyclerView.layoutManager = LinearLayoutManager(context)

        // You can use GridLayoutManager if you want multiple columns. Enter the number of columns as a parameter.
//        rv_animal_list.layoutManager = GridLayoutManager(this, 2)

        // Access the RecyclerView Adapter and load the data into it
        recyclerView.adapter = context?.let { MenuAdapter(menus, it) }
    }

    private fun addMenus() {
        menus.add("Find Doctors")
        menus.add("Chat")
        menus.add("Search Doctors")
        menus.add("Visited Doctors")
    }


}