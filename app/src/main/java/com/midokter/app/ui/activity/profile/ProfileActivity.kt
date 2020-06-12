package com.midokter.app.ui.activity.profile

import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.midokter.app.BaseApplication
import com.midokter.app.BuildConfig
import com.midokter.app.R
import com.midokter.app.base.BaseActivity
import com.midokter.app.data.PreferenceHelper
import com.midokter.app.data.PreferenceKey
import com.midokter.app.data.getValue
import com.midokter.app.data.setValue
import com.midokter.app.databinding.ActivityProfileBinding
import com.midokter.app.repositary.model.ProfileResponse
import com.midokter.app.utils.ViewUtils
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : BaseActivity<ActivityProfileBinding>(),ProfileNavigator {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private val preferenceHelper = PreferenceHelper(BaseApplication.baseApplication)
    private lateinit var viewModel: ProfileViewModel
    private lateinit var mDataBinding: ActivityProfileBinding
    private lateinit var profile_img: ImageView


    override fun getLayoutId(): Int = R.layout.activity_profile

    override fun initView(mViewDataBinding: ViewDataBinding?) {

        mDataBinding = mViewDataBinding as ActivityProfileBinding
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        mDataBinding.viewmodel = viewModel
        viewModel.navigator = this
        initUI()
        initApiCal()
        observeResponse()


    }
    private fun initApiCal() {
        loadingObservable.value = true
        viewModel.getprofile()
    }
    private fun initUI() {

        profile_img = findViewById(R.id.img_prof)
        setSupportActionBar( mDataBinding.toolbar9)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        mDataBinding.toolbar9.setNavigationOnClickListener(View.OnClickListener {

            finish()
        })

        button11.setOnClickListener {
            cutomColorButton(button11)
            cutomWhiteColorButton(button12)
            cutomWhiteColorButton(button13)
            layout_profile_personal.visibility = View.VISIBLE
            layout_profile_medical.visibility = View.GONE
            layout_profile_lifestyle.visibility = View.GONE
        }
        button12.setOnClickListener {
            cutomColorButton(button12)
            cutomWhiteColorButton(button11)
            cutomWhiteColorButton(button13)
            layout_profile_personal.visibility = View.GONE
            layout_profile_medical.visibility = View.VISIBLE
            layout_profile_lifestyle.visibility = View.GONE
        }
        button13.setOnClickListener {
            cutomColorButton(button13)
            cutomWhiteColorButton(button11)
            cutomWhiteColorButton(button12)
            layout_profile_personal.visibility = View.GONE
            layout_profile_medical.visibility = View.GONE
            layout_profile_lifestyle.visibility = View.VISIBLE
        }

    }

    private fun observeResponse() {

        viewModel.mProfileResponse.observe(this, Observer<ProfileResponse> {
            loadingObservable.value = false

            if ( it.patient?.profile?.profile_pic!=null) {
                Glide.with(this)
                    .load(BuildConfig.BASE_IMAGE_URL+it.patient?.profile?.profile_pic)
                    .error(R.drawable.app_logo)
                    .placeholder(R.drawable.app_logo)
                    .into( profile_img)
            }
            preferenceHelper.setValue(PreferenceKey.WALLET_BALANCE, it.patient.wallet_balance)
            viewModel.name.set(it.patient.first_name.plus(" ").plus(it.patient.last_name))
            viewModel.number.set(it.patient.phone)
            viewModel.email.set(it.patient.email as String?)
            viewModel.gender.set(it.patient.profile!!.gender as String?)
            viewModel.dob.set(it.patient.profile!!.dob as String?)
            viewModel.bloodgroup.set(it.patient.profile!!.blood_group as String?)
            viewModel.marital.set(it.patient.profile!!.merital_status as String?)
            viewModel.height.set(it.patient.profile.height as String?)
            viewModel.weight.set(it.patient.profile.weight as String?)
            viewModel.emgcontact.set(it.patient.profile.emergency_contact as String?)
            viewModel.location.set(it.patient.profile.address as String?)

            viewModel.allergies.set(it.patient.profile.allergies as String?)
            viewModel.current_medications.set(it.patient.profile.current_medications as String?)
            viewModel.past_medications.set(it.patient.profile.past_medications as String?)
            viewModel.chronic_diseases.set(it.patient.profile.chronic_diseases as String?)
            viewModel.injuries.set(it.patient.profile.injuries as String?)
            viewModel.surgeries.set(it.patient.profile.surgeries as String?)

            viewModel.smoking.set(it.patient.profile.smoking as String?)
            viewModel.alcohol.set(it.patient.profile.alcohol as String?)
            viewModel.activity.set(it.patient.profile.activity as String?)
            viewModel.food.set(it.patient.profile.food as String?)
            viewModel.occupation.set(it.patient.profile.occupation as String?)




        })
        viewModel.getErrorObservable().observe(this, Observer<String> { message ->
            loadingObservable.value = false
            ViewUtils.showToast(this@ProfileActivity, message, false)
        })
    }
    fun cutomColorButton(button: Button) {
        button.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.colorButton))
        button.setTextColor(ContextCompat.getColor(applicationContext, R.color.colorWhite))
    }

    fun cutomWhiteColorButton(button: Button) {
        button.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.colorWhite))
        button.setTextColor(ContextCompat.getColor(applicationContext, R.color.colorBlack))
    }
}
