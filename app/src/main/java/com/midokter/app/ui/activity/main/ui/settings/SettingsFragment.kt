package com.midokter.app.ui.activity.main.ui.settings


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding

import com.midokter.app.R
import com.midokter.app.base.BaseFragment
import com.midokter.app.databinding.FragmentSettingsBinding

/**
 * A simple [Fragment] subclass.
 */
class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_settings

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {
    }

}
