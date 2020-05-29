package com.midokter.app.ui.activity.main.ui.home

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.midokter.app.R
import com.midokter.app.base.BaseFragment
import com.midokter.app.databinding.FragmentHomeBinding
import com.midokter.app.repositary.model.HomeResponse
import com.midokter.app.ui.adapter.MenuAdapter
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    var menus: ArrayList<HomeResponse.Menu> = ArrayList()

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
        menus.add(HomeResponse.Menu(R.drawable.find_doctor,getString(R.string.find_doctors),getString(R.string.find_doctors)))
        menus.add(HomeResponse.Menu(R.drawable.ic_chat,getString(R.string.chat),getString(R.string.ask_question)))
        menus.add(HomeResponse.Menu(R.drawable._ic_search_doc,getString(R.string.search_doctor),getString(R.string.based_hospitals)))
        menus.add(HomeResponse.Menu(R.drawable.ic_visted_doc,getString(R.string.visted_doctor),getString(R.string.see_you_visted_doc)))
    }
}