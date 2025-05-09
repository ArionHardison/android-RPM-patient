package com.telehealthmanager.app.ui.activity.main.ui.online_consultation

import android.content.Intent
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.telehealthmanager.app.R
import com.telehealthmanager.app.base.BaseFragment
import com.telehealthmanager.app.databinding.FragmentOnlineConsultationBinding
import com.telehealthmanager.app.repositary.model.chatmodel.Chat
import com.telehealthmanager.app.ui.activity.pubnub.PubnubChatActivity
import com.telehealthmanager.app.ui.adapter.ChatAdapter
import com.telehealthmanager.app.ui.adapter.IChatListener
import com.telehealthmanager.app.utils.ViewUtils
import java.io.Serializable

class OnlineConsultationFragment : BaseFragment<FragmentOnlineConsultationBinding>(), OnlineConsultationNavigator, IChatListener {

    private lateinit var mViewModel: OnlineConsultationViewModel
    private lateinit var mDataBinding: FragmentOnlineConsultationBinding
    private var mChatAdapter: ChatAdapter? = null
    private var chat: Chat? = null

    override fun getLayoutId(): Int = R.layout.fragment_online_consultation

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {
        mDataBinding = mViewDataBinding as FragmentOnlineConsultationBinding
        mViewModel = ViewModelProvider(this).get(OnlineConsultationViewModel::class.java)
        mDataBinding.viewmodel = mViewModel
        mViewModel.navigator = this
        observeSuccessResponse()
        observeErrorResponse()
        initApiCall()
    }

    override fun onChatClicked(item: Chat) {
        chat = item
        mViewModel.getChatStatus(item.chatRequestsId)
    }

    private fun initApiCall() {
        mViewModel.getChats()
    }

    private fun observeSuccessResponse() {
        mViewModel.mChatResponse.observe(this, Observer {
            if (mViewModel.mChatResponse.value!!.chats.isNotEmpty()) {
                mDataBinding.tvNotFound.visibility = View.GONE
            } else {
                mDataBinding.tvNotFound.visibility = View.VISIBLE
            }
            mChatAdapter = ChatAdapter(requireActivity(), mViewModel.mChatResponse.value!!.chats!!, this)
            mDataBinding.mChatAdapter = mChatAdapter
            mDataBinding.rvOnlineConsultation.addItemDecoration(DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL))
            mDataBinding.rvOnlineConsultation.layoutManager = LinearLayoutManager(activity)
            mChatAdapter!!.notifyDataSetChanged()
            mViewModel.loadingProgress.value = false
        })

        mViewModel.mChatStatusResponse.observe(this, Observer {
            if (it.checkStatus != null) {
                when {
                    it.checkStatus.status?.equals("ACCEPTED", true) -> {
                        val intent = Intent(requireActivity(), PubnubChatActivity::class.java)
                        intent.putExtra("chat_data", chat as Serializable)
                        startActivity(intent)
                    }
                    it.checkStatus.status?.equals("CANCELLED", true) -> {
                        ViewUtils.showToast(requireActivity(), "Doctor not accepted your request", false)
                    }
                    it.checkStatus.status?.equals("COMPLETED", true) -> {
                        ViewUtils.showToast(requireActivity(), "Chat time expired, Request again", false)
                    }
                }
            }
            mViewModel.loadingProgress.value = false
        })

        mViewModel.loadingProgress.observe(this, Observer {
            if (it)
                showLoading()
            else
                hideLoading()
        })
    }

    private fun observeErrorResponse() {
        mViewModel.getErrorObservable().observe(this, Observer<String> { message ->
            mViewModel.loadingProgress.value = false
            ViewUtils.showToast(requireActivity(), message, false)
        })
    }

}