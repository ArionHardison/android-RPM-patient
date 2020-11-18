package com.telehealthmanager.app.ui.activity.addmoney

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.telehealthmanager.app.R
import com.telehealthmanager.app.base.BaseActivity
import com.telehealthmanager.app.databinding.ActivityAddMoneyBinding

class AddMoneyActivity : BaseActivity<ActivityAddMoneyBinding>(), AddMoneyNavigator {

    lateinit var mDataBinding: ActivityAddMoneyBinding
    lateinit var mViewModel: AddMoneyViewModel

    override fun getLayoutId(): Int = R.layout.activity_add_money

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mDataBinding = mViewDataBinding as ActivityAddMoneyBinding
        mViewModel = ViewModelProviders.of(this).get(AddMoneyViewModel::class.java)
        mViewModel.navigator = this
        mDataBinding.viewmodel = mViewModel
    }

}