package com.midokter.app.ui.activity.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.midokter.app.BaseApplication
import com.midokter.app.R
import com.midokter.app.base.BaseActivity
import com.midokter.app.data.Constant
import com.midokter.app.data.PreferenceHelper
import com.midokter.app.data.PreferenceKey
import com.midokter.app.data.setValue
import com.midokter.app.databinding.ActivityRegisterEmailBinding
import com.midokter.app.databinding.ActivityRegisterGenderBinding
import com.midokter.app.repositary.WebApiConstants
import kotlinx.android.synthetic.main.activity_register_email.*
import kotlinx.android.synthetic.main.activity_register_gender.*

class RegisterGenderActivity : BaseActivity<ActivityRegisterGenderBinding>() {
    private lateinit var viewModel: RegisterViewModel
    private lateinit var mDataBinding: ActivityRegisterGenderBinding
    private val preferenceHelper = PreferenceHelper(BaseApplication.baseApplication)


    override fun getLayoutId(): Int = R.layout.activity_register_gender

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mDataBinding = mViewDataBinding as ActivityRegisterGenderBinding
        viewModel = ViewModelProviders.of(this).get(RegisterViewModel::class.java)
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
