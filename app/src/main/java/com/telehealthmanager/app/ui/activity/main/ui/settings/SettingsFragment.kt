package com.telehealthmanager.app.ui.activity.main.ui.settings


import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.telehealthmanager.app.BaseApplication
import com.telehealthmanager.app.BuildConfig
import com.telehealthmanager.app.R
import com.telehealthmanager.app.base.BaseFragment
import com.telehealthmanager.app.data.PreferenceHelper
import com.telehealthmanager.app.data.clearAll
import com.telehealthmanager.app.databinding.FragmentSettingsBinding
import com.telehealthmanager.app.ui.activity.splash.SplashActivity

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
        viewModel = ViewModelProvider(this).get(SettingViewModel::class.java)
        mDataBinding.viewmodel = viewModel
        viewModel.navigator = this
        initObservableApi()
    }

    private fun initObservableApi() {
        viewModel.mLogoutResponse.observe(this, {
            hideLoading()
            preferenceHelper.clearAll()
            val intent = Intent(activity, SplashActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent);
            requireActivity().finish()
        })

        viewModel.getErrorObservable().observe(this, {
            hideLoading()
        })
    }

    override fun logoutClick() {
        showLoading()
        viewModel.logout(HashMap())
    }

    override fun ratingClick() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setData(Uri.parse(getString(R.string.playstore_url) + BuildConfig.APPLICATION_ID))
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(intent);
    }

    override fun aboutClick() {
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(BuildConfig.BASE_URL)
        startActivity(i)
    }
}
