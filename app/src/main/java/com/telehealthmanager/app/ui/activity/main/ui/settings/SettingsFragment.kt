package com.telehealthmanager.app.ui.activity.main.ui.settings


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.ViewDataBinding

import com.telehealthmanager.app.R
import com.telehealthmanager.app.base.BaseFragment
import com.telehealthmanager.app.databinding.FragmentSettingsBinding

/**
 * A simple [Fragment] subclass.
 */
class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_settings

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {
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
