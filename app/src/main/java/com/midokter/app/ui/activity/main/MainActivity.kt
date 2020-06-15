package com.midokter.app.ui.activity.main

import android.content.Intent
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.midokter.app.BaseApplication
import com.midokter.app.BuildConfig
import com.midokter.app.R
import com.midokter.app.base.BaseActivity
import com.midokter.app.data.*
import com.midokter.app.databinding.ActivityMainBinding
import com.midokter.app.databinding.ActivityOtpBinding
import com.midokter.app.repositary.model.ProfileResponse
import com.midokter.app.repositary.model.Response
import com.midokter.app.ui.activity.otp.OTPViewModel
import com.midokter.app.ui.activity.profile.ProfileActivity
import com.midokter.app.ui.activity.register.RegisterEmailActivity
import com.midokter.app.ui.activity.register.RegisterNameActivity
import com.midokter.app.ui.activity.splash.SplashActivity
import com.midokter.app.utils.ViewUtils

class MainActivity : BaseActivity<ActivityMainBinding>(),MainNavigator {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val preferenceHelper = PreferenceHelper(BaseApplication.baseApplication)
    private lateinit var viewModel: MainViewModel
    private lateinit var mDataBinding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var headerview: View
    private lateinit var name: TextView
    private lateinit var profile_completed: TextView
    private lateinit var profile_img: ImageView

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mDataBinding = mViewDataBinding as ActivityMainBinding
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        mDataBinding.viewmodel = viewModel
        viewModel.navigator = this

        preferenceHelper.setValue(PreferenceKey.CURRENCY, "$")
        initUI()
        initApiCal()
        observeResponse()


    }

    private fun initApiCal() {
        loadingObservable.value = true
        viewModel.getprofile()
    }
    private fun initUI() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

         drawerLayout = findViewById(R.id.drawer_layout)
         navView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        headerview = navView.getHeaderView(0)
        navView.itemIconTintList = null
        navView.getHeaderView(0).setOnClickListener() {
            val intent = Intent(applicationContext, ProfileActivity::class.java)
            startActivity(intent);
        }
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_appointments, R.id.nav_online_consultation,
                R.id.nav_fav_doctor, R.id.nav_medical_records, R.id.nav_remainder,R.id.nav_wallet, R.id.nav_articles
                , R.id.nav_relative_mgmt, R.id.nav_faq, R.id.nav_settings
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        name = headerview.findViewById(R.id.name)
        profile_completed = headerview.findViewById(R.id.profile_completed)
        profile_img = headerview.findViewById(R.id.profile_img)
        name.setText(preferenceHelper.getValue(PreferenceKey.FIRST_NAME,"Test").toString().plus(" ").plus(preferenceHelper.getValue(PreferenceKey.LAST_NAME,"Test").toString()))
        profile_completed.setText( getString(R.string.profile_completed).plus(" ").plus(preferenceHelper.getValue(PreferenceKey.PROFILE_PER,"Test").toString() ))



    }

    private fun observeResponse() {
        /*viewModel.mhomeResponse.observe(this, Observer<HomeResponse> {
            loadingObservable.value = false
            preferenceHelper.setValue(PreferenceKey.CURRENCY, "$")
            viewModel.total.set(it.total_appoinments.toString())
            viewModel.booked.set(it.booked_count.toString())
            viewModel.cancelled.set(it.cancelled_count.toString())
            viewModel.newpatient.set(it.new_patient_count.toString())
            viewModel.repeat.set(it.Repeat_patients.toString())
            viewModel.revenue.set(preferenceHelper.getValue(PreferenceKey.CURRENCY,"$").toString().plus(it.revenue.toString()))
            viewModel.paid.set(preferenceHelper.getValue(PreferenceKey.CURRENCY,"$").toString().plus(it.paid.toString()))
            viewModel.pending.set(preferenceHelper.getValue(PreferenceKey.CURRENCY,"$").toString().plus(it.pending.toString()))


        })*/
        viewModel.mProfileResponse.observe(this, Observer<ProfileResponse> {
            loadingObservable.value = false

            viewModel.name.set(it.patient.first_name.plus(" ").plus(it.patient.last_name))
            viewModel.profilepercentage.set(it.profile_complete)
            if (it.patient.profile?.profile_pic !=null)
            viewModel.imageurl.set(it.patient.profile?.profile_pic as String?)

          /*  viewModel.imageurl.set(BuildConfig.BASE_IMAGE_URL+it.doctor.doctor_profile.profile_pic)
            if ( it.doctor.doctor_profile.profile_pic!=null) {
                Glide.with(this)
                    .load(viewModel.imageurl.get())
                    .error(R.drawable.app_logo)
                    .placeholder(R.drawable.app_logo)
                    .into(mDataBinding.profileIv)
            }*/
            preferenceHelper.setValue(PreferenceKey.PATIENT_ID, it.patient.id)
            preferenceHelper.setValue(PreferenceKey.FIRST_NAME, it.patient.first_name)
            preferenceHelper.setValue(PreferenceKey.LAST_NAME, it.patient.last_name)
            preferenceHelper.setValue(PreferenceKey.PROFILE_PER, it.profile_complete)
            preferenceHelper.setValue(PreferenceKey.PHONE, it.patient.phone)
            preferenceHelper.setValue(PreferenceKey.EMAIL, it.patient.email)
            preferenceHelper.setValue(PreferenceKey.PROFILE_IMAGE, it.patient.profile?.profile_pic)
            preferenceHelper.setValue(PreferenceKey.WALLET_BALANCE, it.patient.wallet_balance)

            name.setText(preferenceHelper.getValue(PreferenceKey.FIRST_NAME,"Test").toString().plus(" ").plus(preferenceHelper.getValue(PreferenceKey.LAST_NAME,"Test").toString()))
            profile_completed.setText( getString(R.string.profile_completed).plus(" ").plus(preferenceHelper.getValue(PreferenceKey.PROFILE_PER,"Test").toString() ))


        })

        viewModel.mLogoutResponse.observe(this, Observer<Response> {

            hideLoading()
            preferenceHelper.clearAll()
            val intent = Intent(this@MainActivity, SplashActivity::class.java)
            startActivity(intent);
            finish()



        })
        viewModel.getErrorObservable().observe(this, Observer<String> { message ->
            loadingObservable.value = false
            ViewUtils.showToast(this@MainActivity, message, false)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                preferenceHelper.clearAll()
                val intent = Intent(this@MainActivity, SplashActivity::class.java)
                startActivity(intent);
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
