package com.midokter.app.ui.activity.main.ui.wallet

import android.view.View
import androidx.databinding.ViewDataBinding
import com.midokter.app.BaseApplication
import com.midokter.app.R
import com.midokter.app.base.BaseFragment
import com.midokter.app.data.PreferenceHelper
import com.midokter.app.databinding.FragmentWalletBinding

class WalletFragment : BaseFragment<FragmentWalletBinding>() {

    private val preferenceHelper = PreferenceHelper(BaseApplication.baseApplication)
    override fun getLayoutId(): Int = R.layout.fragment_wallet

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {
    }
}