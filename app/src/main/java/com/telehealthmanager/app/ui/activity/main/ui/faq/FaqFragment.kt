package com.telehealthmanager.app.ui.activity.main.ui.faq


import android.view.View
import android.widget.ExpandableListAdapter
import android.widget.ExpandableListView
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.telehealthmanager.app.R
import com.telehealthmanager.app.base.BaseFragment
import com.telehealthmanager.app.databinding.FragmentFaqBinding
import com.telehealthmanager.app.ui.activity.main.ui.faq.ExpandableListDataPump.data
import com.telehealthmanager.app.ui.adapter.FaqAdapter


/**
 * A simple [Fragment] subclass.
 */
class FaqFragment : BaseFragment<FragmentFaqBinding>(), FaqNavigator {

    override fun getLayoutId(): Int = R.layout.fragment_faq
    private var mFaqAdapter: FaqAdapter? = null
    lateinit var mViewDataBinding: FragmentFaqBinding
    val mViewModel = FaqViewModel()


    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {
        this.mViewDataBinding = mViewDataBinding as FragmentFaqBinding
        mViewModel.navigator = this
        mViewDataBinding.viewmodel = mViewModel
        initAdapter()
    }

    private var expandableListView: ExpandableListView? = null
    private var adapter: ExpandableListAdapter? = null
    private var titleList: List<String>? = null

    private fun initAdapter() {
        expandableListView=mViewDataBinding.expendableList
        val listData = data
        titleList = ArrayList(listData.keys)
        adapter = FaqAdapter(context!!, titleList as ArrayList<String>, listData)
        expandableListView!!.setAdapter(adapter)
    }
}

object ExpandableListDataPump {
    val data: HashMap<String, List<String>>
        get() {
            val expandableListDetail =
                HashMap<String, List<String>>()
            val cricket: MutableList<String> = ArrayList()
            cricket.add("content description 1")
            val football: MutableList<String> = ArrayList()
            football.add("content description 2")
            val basketball: MutableList<String> = ArrayList()
            basketball.add("content description 3")
            expandableListDetail["Question 1"] = cricket
            expandableListDetail["Question 2"] = football
            expandableListDetail["Question 3"] = basketball
            return expandableListDetail
        }
}

