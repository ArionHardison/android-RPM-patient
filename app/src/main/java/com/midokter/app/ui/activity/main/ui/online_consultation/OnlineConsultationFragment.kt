package com.midokter.app.ui.activity.main.ui.online_consultation

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.midokter.app.R
import com.midokter.app.base.BaseFragment
import com.midokter.app.databinding.FragmentOnlineConsultationBinding
import com.midokter.app.ui.adapter.ChatAdapter
import com.midokter.app.utils.ViewUtils

class OnlineConsultationFragment : BaseFragment<FragmentOnlineConsultationBinding>(),OnlineConsultationNavigator {

    private lateinit var mViewModel: OnlineConsultationViewModel
    private lateinit var mDataBinding: FragmentOnlineConsultationBinding
    override fun getLayoutId(): Int = R.layout.fragment_online_consultation
    private var mChatAdapter: ChatAdapter? = null

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {

        mDataBinding = mViewDataBinding as FragmentOnlineConsultationBinding
        mViewModel = ViewModelProviders.of(this).get(OnlineConsultationViewModel::class.java)
        mDataBinding.viewmodel = mViewModel
        mViewModel.navigator = this
        observeSuccessResponse()
        observeErrorResponse()
        initApiCall()
    }

    private fun initApiCall() {
        mViewModel.getChats()
    }

    private fun observeSuccessResponse() {
        mViewModel.mChatResponse.observe(this, Observer {
            if (mViewModel.mChatResponse.value!!.chats.size > 0) {
                mDataBinding.tvNotFound.visibility = View.GONE
            } else {
                mDataBinding.tvNotFound.visibility = View.VISIBLE
            }

            mChatAdapter = ChatAdapter(activity!!, mViewModel.mChatResponse.value!!.chats!!)
            mDataBinding.mChatAdapter = mChatAdapter

            mDataBinding.rvOnlineConsultation.addItemDecoration(
                DividerItemDecoration(
                    activity!!,
                    DividerItemDecoration.VERTICAL
                )
            )
            mDataBinding.rvOnlineConsultation.layoutManager = LinearLayoutManager(activity)
            mChatAdapter!!.notifyDataSetChanged()
            mViewModel.loadingProgress.value = false
        })
    }
    private fun observeErrorResponse() {
        mViewModel.getErrorObservable().observe(this, Observer<String> { message ->
            mViewModel.loadingProgress.value = false
            ViewUtils.showToast(activity!!, message, false)
        })
    }

}