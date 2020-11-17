package com.telehealthmanager.app.ui.activity.allergies

import android.content.Intent
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.telehealthmanager.app.R
import com.telehealthmanager.app.base.BaseActivity
import com.telehealthmanager.app.databinding.ActivityAllergiesBinding
import com.telehealthmanager.app.utils.CustomBackClick

class AllergiesActivity : BaseActivity<ActivityAllergiesBinding>(), AllergiesNavigator, CustomBackClick {

    private lateinit var viewModel: AllergiesViewModel
    private lateinit var mDataBinding: ActivityAllergiesBinding
    private var REQUEST_ADD_ALLERGES: Int = 100

    override fun getLayoutId() = R.layout.activity_allergies

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mDataBinding = mViewDataBinding as ActivityAllergiesBinding
        viewModel = ViewModelProviders.of(this).get(AllergiesViewModel::class.java)
        mDataBinding.viewmodel = viewModel
        viewModel.navigator = this
        viewModel.setOnClickListener(this@AllergiesActivity)
        viewModel.toolBarTile.value = resources.getString(R.string.allergies)
    }

    override fun clickBackPress() {
        setResult(RESULT_CANCELED)
        finish()
    }

    override fun openAddAllergies() {
        val newIntent = Intent(this, AddAllergiesActivity::class.java)
        startActivityForResult(newIntent, REQUEST_ADD_ALLERGES)
    }

    override fun noAllergies() {
        val intent = Intent()
        setResult(RESULT_OK, intent)
        finish()
    }
}