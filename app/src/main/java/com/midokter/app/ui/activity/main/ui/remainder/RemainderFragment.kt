package com.midokter.app.ui.activity.main.ui.remainder

import android.content.Intent
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.midokter.app.R
import com.midokter.app.base.BaseFragment
import com.midokter.app.databinding.FragmentRemainderBinding
import com.midokter.app.repositary.model.AddRemainderResponse
import com.midokter.app.repositary.model.ArticleResponse
import com.midokter.app.repositary.model.ReminderResponse
import com.midokter.app.ui.activity.addreminder.AddReminderActivity
import com.midokter.app.ui.adapter.HealthFeedAdapter
import com.midokter.app.ui.adapter.OnReminderClickListener
import com.midokter.app.ui.adapter.RemainderListAdapter
import com.midokter.app.utils.ViewUtils
import com.midokter.doctor.ui.activity.healthfeeddetails.HealthFeedDetailsActivity
import kotlinx.android.synthetic.main.fragment_remainder.*
import java.io.Serializable

class RemainderFragment : BaseFragment<FragmentRemainderBinding>(),RemainderNavigator,
    OnReminderClickListener {

    lateinit var mViewDataBinding: FragmentRemainderBinding
   var mViewModel =RemainderViewModel()
    override fun getLayoutId(): Int = R.layout.fragment_remainder;
    private var mReminderdapter: RemainderListAdapter? = null

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {
        this.mViewDataBinding = mViewDataBinding as FragmentRemainderBinding
        mViewModel = ViewModelProviders.of(this).get(RemainderViewModel::class.java)
        mViewDataBinding.viewmodel = mViewModel
        mViewModel.navigator = this
        initAdapter()
        observeSuccessResponse()
        observeErrorResponse()
        observeShowLoading()

    }

    private fun observeErrorResponse() {
        mViewModel.getErrorObservable().observe(this, Observer<String> { message ->
            mViewModel.loadingProgress.value = false
            ViewUtils.showToast(context!!, message, false)
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
                mViewDataBinding.tvNotFound.visibility = View.GONE
            } else {
                mViewDataBinding.tvNotFound.visibility = View.VISIBLE
            }

            mReminderdapter = RemainderListAdapter(context!!, mViewModel.mReminders!!,this)
            mViewDataBinding.reminderAdapter = mReminderdapter
            mReminderdapter!!.notifyDataSetChanged()
            mViewModel.loadingProgress.value = false
        })
    }


    private fun initAdapter() {

        mViewDataBinding.rvRemainder.layoutManager = LinearLayoutManager(context)
        mViewDataBinding.rvRemainder.addItemDecoration(
            DividerItemDecoration(context,
                DividerItemDecoration.VERTICAL)
        )
        mReminderdapter = RemainderListAdapter(context!!, mViewModel.mReminders!!, this)
        mViewDataBinding.reminderAdapter = mReminderdapter
        mReminderdapter!!.notifyDataSetChanged()
    }


    override fun onReminderClicked(item: ReminderResponse.Reminder) {
        startActivity(Intent(context, AddReminderActivity::class.java).putExtra("reminder",item as Serializable))
    }

    override fun onNewRemainderClicked() {
        openNewActivity(activity, AddReminderActivity::class.java,false)
    }
}