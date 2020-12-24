package com.telehealthmanager.app.ui.activity.register

import android.content.Intent
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.telehealthmanager.app.BaseApplication
import com.telehealthmanager.app.R
import com.telehealthmanager.app.base.BaseActivity
import com.telehealthmanager.app.data.Constant
import com.telehealthmanager.app.data.PreferenceHelper
import com.telehealthmanager.app.data.PreferenceKey
import com.telehealthmanager.app.data.setValue
import com.telehealthmanager.app.databinding.ActivityRegisterEmailBinding
import com.telehealthmanager.app.databinding.ActivityRegisterGenderBinding

class RegisterGenderActivity : BaseActivity<ActivityRegisterGenderBinding>() {
    private lateinit var viewModel: RegisterViewModel
    private lateinit var mDataBinding: ActivityRegisterGenderBinding
    private val preferenceHelper = PreferenceHelper(BaseApplication.baseApplication)


    override fun getLayoutId(): Int = R.layout.activity_register_gender

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mDataBinding = mViewDataBinding as ActivityRegisterGenderBinding
        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
        mDataBinding.viewmodel = viewModel

        mDataBinding.male.setOnClickListener {


            preferenceHelper.setValue(PreferenceKey.GENDER,Constant.Gender.MALE)
            val intent = Intent(applicationContext,RegisterFinalActivity::class.java)
            startActivity(intent);
        }
        mDataBinding.female.setOnClickListener {

            preferenceHelper.setValue(PreferenceKey.GENDER,Constant.Gender.FEMALE)
            val intent = Intent(applicationContext,RegisterFinalActivity::class.java)
            startActivity(intent);
        }
        mDataBinding.other.setOnClickListener {

            preferenceHelper.setValue(PreferenceKey.GENDER,Constant.Gender.OTHER)
            val intent = Intent(applicationContext,RegisterFinalActivity::class.java)
            startActivity(intent);
        }
        mDataBinding.backarrow.setOnClickListener {
           finish()
        }
    }
}
