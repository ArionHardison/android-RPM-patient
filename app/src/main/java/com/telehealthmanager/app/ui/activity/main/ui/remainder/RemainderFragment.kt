package com.telehealthmanager.app.ui.activity.main.ui.remainder

import android.content.Intent
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.telehealthmanager.app.R
import com.telehealthmanager.app.base.BaseFragment
import com.telehealthmanager.app.databinding.FragmentRemainderBinding
import com.telehealthmanager.app.repositary.model.ReminderResponse
import com.telehealthmanager.app.ui.activity.addreminder.AddReminderActivity
import com.telehealthmanager.app.ui.adapter.OnReminderClickListener
import com.telehealthmanager.app.ui.adapter.RemainderListAdapter
import com.telehealthmanager.app.utils.ViewUtils
import java.io.Serializable

class RemainderFragment : BaseFragment<FragmentRemainderBinding>(), RemainderNavigator,
    OnReminderClickListener {

    private lateinit var mDataBinding: FragmentRemainderBinding
    private var mViewModel = RemainderViewModel()
    private var mRemainderAdapter: RemainderListAdapter? = null

    override fun getLayoutId(): Int = R.layout.fragment_remainder;

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {
        mDataBinding = mViewDataBinding as FragmentRemainderBinding
        mViewModel = ViewModelProvider(this).get(RemainderViewModel::class.java)
        mDataBinding.viewmodel = mViewModel
        mViewModel.navigator = this
        initAdapter()
        observeSuccessResponse()
        observeErrorResponse()
        observeShowLoading()
    }

    private fun observeErrorResponse() {
        mViewModel.getErrorObservable().observe(this, { message ->
            mViewModel.loadingProgress.value = false
            ViewUtils.showToast(requireContext(), message, false)
        })
    }

    private fun initApiCall() {
        mViewModel.getReminders()
    }

    private fun observeShowLoading() {
        mViewModel.loadingProgress.observe(this, Observer {
            if (!it) {
                hideLoading()
            } else {
                showLoading()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        initApiCall()
    }

    private fun observeSuccessResponse() {
        mViewModel.mReminderResponse.observe(this, Observer {
            mViewModel.mReminders = it.reminder as MutableList<ReminderResponse.Reminder>?
            if (mViewModel.mReminders!!.size > 0) {
                mDataBinding.tvNotFound.visibility = View.GONE
            } else {
                mDataBinding.tvNotFound.visibility = View.VISIBLE
            }

            mRemainderAdapter = RemainderListAdapter(requireContext(), mViewModel.mReminders!!, this)
            mDataBinding.reminderAdapter = mRemainderAdapter
            mRemainderAdapter!!.notifyDataSetChanged()
            mViewModel.loadingProgress.value = false
        })
    }

    private fun initAdapter() {
        mDataBinding.rvRemainder.layoutManager = LinearLayoutManager(context)
        mDataBinding.rvRemainder.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        mRemainderAdapter = RemainderListAdapter(requireActivity(), mViewModel.mReminders!!, this)
        mDataBinding.reminderAdapter = mRemainderAdapter
        mRemainderAdapter!!.notifyDataSetChanged()
    }

    override fun onReminderClicked(item: ReminderResponse.Reminder) {
        startActivity(Intent(context, AddReminderActivity::class.java).putExtra("reminder", item as Serializable))
    }

    override fun onNewRemainderClicked() {
        openNewActivity(activity, AddReminderActivity::class.java, false)
    }
}