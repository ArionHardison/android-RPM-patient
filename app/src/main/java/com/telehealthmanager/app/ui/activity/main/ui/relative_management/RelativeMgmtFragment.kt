package com.telehealthmanager.app.ui.activity.main.ui.relative_management


import android.content.Intent
import androidx.fragment.app.Fragment
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders

import com.telehealthmanager.app.R
import com.telehealthmanager.app.base.BaseFragment
import com.telehealthmanager.app.data.Constant
import com.telehealthmanager.app.databinding.FragmentOnlineConsultationBinding
import com.telehealthmanager.app.databinding.FragmentRelativeMgmtBinding
import com.telehealthmanager.app.repositary.model.chatmodel.Chat
import com.telehealthmanager.app.ui.activity.main.ui.online_consultation.OnlineConsultationViewModel
import com.telehealthmanager.app.ui.activity.profile.ProfileActivity
import com.telehealthmanager.app.ui.adapter.ChatAdapter

/**
 * A simple [Fragment] subclass.
 */
class RelativeMgmtFragment : BaseFragment<FragmentRelativeMgmtBinding>(), RelativeMgmtNavigator {
    private lateinit var mViewModel: RelativeMgmtViewModel
    private lateinit var mDataBinding: FragmentRelativeMgmtBinding

    override fun getLayoutId(): Int = R.layout.fragment_relative_mgmt

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {
        mDataBinding = mViewDataBinding as FragmentRelativeMgmtBinding
        mViewModel = ViewModelProviders.of(this).get(RelativeMgmtViewModel::class.java)
        mDataBinding.viewmodel = mViewModel
        mViewModel.navigator = this
    }

    override fun openAddRelative() {
        val intent = Intent(activity!!, ProfileActivity::class.java)
        intent.putExtra(Constant.IntentData.IS_VIEW_TYPE, "add_relative")
        startActivity(intent);
    }
}
