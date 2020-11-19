package com.telehealthmanager.app.ui.activity.main.ui.wallet

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.telehealthmanager.app.BaseApplication
import com.telehealthmanager.app.R
import com.telehealthmanager.app.base.BaseFragment
import com.telehealthmanager.app.data.*
import com.telehealthmanager.app.databinding.FragmentWalletBinding
import com.telehealthmanager.app.repositary.model.ProfileResponse
import com.telehealthmanager.app.ui.activity.addmoney.AddMoneyActivity
import com.telehealthmanager.app.utils.ViewUtils

class WalletFragment : BaseFragment<FragmentWalletBinding>(), WalletNavigator {

    private val preferenceHelper = PreferenceHelper(BaseApplication.baseApplication)
    private lateinit var mDataBinding: FragmentWalletBinding
    private val mViewModel = WalletViewModel()

    override fun getLayoutId(): Int = R.layout.fragment_wallet

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {
        mDataBinding = mViewDataBinding as FragmentWalletBinding
        mViewModel.navigator = this
        mDataBinding.viewmodel = mViewModel
        observeSuccessResponse()
        observeShowLoading()
        observeErrorResponse()
        mViewModel.balance.set(String.format("%s %s", preferenceHelper.getValue(PreferenceKey.CURRENCY, "$"), preferenceHelper.getValue(PreferenceKey.WALLET_BALANCE, "0").toString()))
        initApiCall()
    }

    private fun initApiCall() {
        mViewModel.getProfile()
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
            mViewModel.loadingProgress.value = false
            mViewModel.balance.set(String.format("%s %s", preferenceHelper.getValue(PreferenceKey.CURRENCY, "$"), preferenceHelper.getValue(PreferenceKey.WALLET_BALANCE, "0").toString()))
        })
    }

    private fun observeErrorResponse() {
        mViewModel.getErrorObservable().observe(this, Observer<String> { message ->
            ViewUtils.showToast(context!!, message, false)
            mViewModel.loadingProgress.value = false
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != AppCompatActivity.RESULT_CANCELED) {
            if (requestCode == Constant.REQUEST_CODE_ADD_MONEY) {
                mViewModel.enteredMoney.set("")
                mViewModel.loadingProgress.value = true
                initApiCall()
            }
        }
    }

    override fun onAddMoneyClicked() {
        if (mViewModel.enteredMoney.get().equals("")) {
            ViewUtils.showToast(context!!, getString(R.string.please_enter_amount), false)
        } else {
            val callIntent = Intent(activity, AddMoneyActivity::class.java)
            callIntent.putExtra(Constant.IntentData.WALLET_AMOUNT, mViewModel.enteredMoney.get().toString())
            startActivityForResult(callIntent, Constant.REQUEST_CODE_ADD_MONEY)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear();
    }
}