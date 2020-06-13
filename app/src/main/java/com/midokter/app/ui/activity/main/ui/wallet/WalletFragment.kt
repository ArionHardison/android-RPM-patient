package com.midokter.app.ui.activity.main.ui.wallet

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.midokter.app.BaseApplication
import com.midokter.app.R
import com.midokter.app.base.BaseFragment
import com.midokter.app.data.PreferenceHelper
import com.midokter.app.data.PreferenceKey
import com.midokter.app.data.getValue
import com.midokter.app.data.setValue
import com.midokter.app.databinding.FragmentWalletBinding
import com.midokter.app.repositary.model.ArticleResponse
import com.midokter.app.repositary.model.ProfileResponse
import com.midokter.app.repositary.model.WalletResponse
import com.midokter.app.ui.adapter.HealthFeedAdapter
import com.midokter.app.utils.ViewUtils

class WalletFragment : BaseFragment<FragmentWalletBinding>(),WalletNavigator {

    private val preferenceHelper = PreferenceHelper(BaseApplication.baseApplication)
    override fun getLayoutId(): Int = R.layout.fragment_wallet
    lateinit var mViewDataBinding: FragmentWalletBinding
    val mViewModel = WalletViewModel()

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {
        this.mViewDataBinding = mViewDataBinding as FragmentWalletBinding
        mViewModel.navigator = this
        mViewDataBinding.viewmodel = mViewModel
        observeSuccessResponse()
        observeShowLoading()
        observeErrorResponse()
        mViewModel.balance.set(String.format("%s %s",preferenceHelper.getValue(PreferenceKey.CURRENCY,"$"),preferenceHelper.getValue(PreferenceKey.WALLET_BALANCE,"0").toString()))
        initApiCall()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear();
    }
    private fun observeErrorResponse() {
        mViewModel.getErrorObservable().observe(this, Observer<String> { message ->
            ViewUtils.showToast(context!!, message, false)
            mViewModel.loadingProgress.value=false
        })
    }

    private fun initApiCall() {
        mViewModel.getprofile()
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

    private fun observeSuccessResponse() {
        mViewModel.mProfileResponse.observe(this, Observer<ProfileResponse> {
            preferenceHelper.setValue(PreferenceKey.WALLET_BALANCE, it.patient.wallet_balance)
            mViewModel.loadingProgress.value=false
            mViewModel.balance.set(String.format("%s %s",preferenceHelper.getValue(PreferenceKey.CURRENCY,"$"),preferenceHelper.getValue(PreferenceKey.WALLET_BALANCE,"0").toString()))
        })
        mViewModel.mWalletResponse.observe(this, Observer<WalletResponse> {
            ViewUtils.showToast(context!!, getString(R.string.added_to_you_wallet,String.format("%s%s",preferenceHelper.getValue(PreferenceKey.CURRENCY,"$"),mViewModel.enteredMoney.get())), true)
            initApiCall()
        })
    }

    override fun onAddMoneyClicked() {
        if(mViewModel.enteredMoney.get().equals(""))
        {
            ViewUtils.showToast(context!!, getString(R.string.please_enter_amount), false)
        }
        else{
            mViewModel.addMoneyToWallet()
        }
    }
}