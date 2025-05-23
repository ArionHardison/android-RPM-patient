package com.telehealthmanager.app.ui.activity.main.ui.relative_management


import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.telehealthmanager.app.R
import com.telehealthmanager.app.base.BaseFragment
import com.telehealthmanager.app.data.Constant
import com.telehealthmanager.app.databinding.FragmentRelativeMgmtBinding
import com.telehealthmanager.app.repositary.model.RelativeList
import com.telehealthmanager.app.ui.activity.profile.ProfileActivity
import com.telehealthmanager.app.ui.adapter.RelativemanagerAdapter


/**
 * A simple [Fragment] subclass.
 */
class RelativeMgmtFragment : BaseFragment<FragmentRelativeMgmtBinding>(), RelativeMgmtNavigator, RelativemanagerAdapter.IRelativeListener {

    private lateinit var mViewModel: RelativeMgmtViewModel
    private lateinit var mDataBinding: FragmentRelativeMgmtBinding
    private var mAdapter: RelativemanagerAdapter? = null

    override fun getLayoutId(): Int = R.layout.fragment_relative_mgmt

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {
        mDataBinding = mViewDataBinding as FragmentRelativeMgmtBinding
        mViewModel = ViewModelProvider(this).get(RelativeMgmtViewModel::class.java)
        mDataBinding.viewmodel = mViewModel
        mViewModel.navigator = this
        initAdapter()
        mViewModel.initApi()
        observableLiveDate()

        mDataBinding.rvRelative.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && mDataBinding.addRelateLayout.visibility == View.VISIBLE) {
                    mDataBinding.addRelateLayout.visibility = View.GONE;
                } else if (dy < 0 && mDataBinding.addRelateLayout.visibility != View.VISIBLE) {
                    mDataBinding.addRelateLayout.visibility = View.VISIBLE;
                }
            }
        })
    }

    private fun observableLiveDate() {
        mViewModel.mRelativeResponse.observe(this, Observer {
            mViewModel.loadingProgress.value = false
            if (!it.relatives.isNullOrEmpty()) {
                mDataBinding.tvNotFound.visibility = View.GONE
                mViewModel.mRelativeList = it.relatives as MutableList<RelativeList>?
                mAdapter = RelativemanagerAdapter(mViewModel.mRelativeList!!, requireActivity(), this)
                mDataBinding.adapter = mAdapter
                mAdapter!!.notifyDataSetChanged()
            } else {
                mDataBinding.tvNotFound.visibility = View.VISIBLE
            }
        })

        mViewModel.loadingProgress.observe(this, Observer {
            if (it)
                showLoading()
            else
                hideLoading()
        })

        mViewModel.getErrorObservable().observe(this, Observer {
            mViewModel.loadingProgress.value = false
        })
    }

    private fun initAdapter() {
        mAdapter = RelativemanagerAdapter(mViewModel.mRelativeList!!, requireActivity(), this)
        mDataBinding.adapter = mAdapter
        mDataBinding.rvRelative.addItemDecoration(DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL))
        mDataBinding.rvRelative.layoutManager = LinearLayoutManager(requireActivity())
        mAdapter!!.notifyDataSetChanged()
    }

    override fun openAddRelative() {
        val intent = Intent(requireActivity(), ProfileActivity::class.java)
        intent.putExtra(Constant.IntentData.IS_VIEW_TYPE, "add_relative")
        startActivityForResult(intent, Constant.RELATIVE_ADD_UPDATED)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constant.RELATIVE_ADD_UPDATED) {
            if (resultCode != Activity.RESULT_CANCELED) {
                initAdapter()
                mViewModel.initApi()
            }
        }
    }

    override fun onItemClick(selectedItem: RelativeList) {
        val intent = Intent(requireActivity(), ProfileActivity::class.java)
        intent.putExtra(Constant.IntentData.IS_VIEW_TYPE, "edit_relative")
        intent.putExtra(Constant.IntentData.IS_RELATIVE_ID, selectedItem.id)
        startActivityForResult(intent, Constant.RELATIVE_ADD_UPDATED)
    }
}
