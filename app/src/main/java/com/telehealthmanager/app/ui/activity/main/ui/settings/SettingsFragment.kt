package com.telehealthmanager.app.ui.activity.main.ui.settings


import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.telehealthmanager.app.BaseApplication

import com.telehealthmanager.app.R
import com.telehealthmanager.app.base.BaseFragment
import com.telehealthmanager.app.data.PreferenceHelper
import com.telehealthmanager.app.data.clearAll
import com.telehealthmanager.app.databinding.FragmentMedicalRecordsBinding
import com.telehealthmanager.app.databinding.FragmentSettingsBinding
import com.telehealthmanager.app.repositary.model.Response
import com.telehealthmanager.app.ui.activity.main.MainViewModel
import com.telehealthmanager.app.ui.activity.main.ui.medical_records.MedicalRecordsNavigator
import com.telehealthmanager.app.ui.activity.main.ui.medical_records.MedicalRecordsViewModel
import com.telehealthmanager.app.ui.activity.splash.SplashActivity
import com.telehealthmanager.app.ui.adapter.MedicalRecordsListAdapter
import kotlinx.android.synthetic.main.fragment_medical_records.*
import kotlinx.android.synthetic.main.fragment_settings.*

/**
 * A simple [Fragment] subclass.
 */
class SettingsFragment : BaseFragment<FragmentSettingsBinding>(), SettingNavigator {

    private val preferenceHelper = PreferenceHelper(BaseApplication.baseApplication)
    private lateinit var viewModel: SettingViewModel
    private lateinit var mDataBinding: FragmentSettingsBinding

    override fun getLayoutId(): Int = R.layout.fragment_settings

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {
        mDataBinding = mViewDataBinding as FragmentSettingsBinding
        viewModel = ViewModelProviders.of(this).get(SettingViewModel::class.java)
        mDataBinding.viewmodel = viewModel
        viewModel.navigator = this
        initObservableApi()
    }

    private fun initObservableApi() {
        viewModel.mLogoutResponse.observe(this, Observer<Response> {
            hideLoading()
            preferenceHelper.clearAll()
            val intent = Intent(activity, SplashActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent);
            activity!!.finish()
        })

        viewModel.getErrorObservable().observe(this, Observer<String> { message ->
            hideLoading()
        })
    }

    override fun logoutClick() {
        showLoading()
        viewModel.logout(HashMap())
    }
}
