package com.telehealthmanager.app.ui.activity.profile

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.ui.AppBarConfiguration
import com.bumptech.glide.Glide
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.telehealthmanager.app.BaseApplication
import com.telehealthmanager.app.BuildConfig
import com.telehealthmanager.app.R
import com.telehealthmanager.app.base.BaseActivity
import com.telehealthmanager.app.data.PreferenceHelper
import com.telehealthmanager.app.data.PreferenceKey
import com.telehealthmanager.app.data.setValue
import com.telehealthmanager.app.databinding.ActivityProfileBinding
import com.telehealthmanager.app.repositary.model.ProfileResponse
import com.telehealthmanager.app.ui.activity.main.MainActivity
import com.telehealthmanager.app.utils.ValidationUtils
import com.telehealthmanager.app.utils.ViewUtils
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_profile.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

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

    fun pickDate() {
        val c = Calendar.getInstance()
        val mYear = c.get(Calendar.YEAR)
        val mMonth = c.get(Calendar.MONTH)
        val mDay = c.get(Calendar.DAY_OF_MONTH)
        val now = System.currentTimeMillis() - 1000
        val maxDate = System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 3)
        val datePickerDialog = DatePickerDialog(this@ProfileActivity,R.style.TransportCalenderThemeDialog,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                c.set(Calendar.YEAR, year)
                c.set(Calendar.MONTH, monthOfYear)
                c.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val sdf =
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                mDataBinding.layoutProfilePersonal.tvDob.setText(sdf.format(c.getTime()))
            }, mYear, mMonth, mDay)
        // datePickerDialog.datePicker.minDate = now
        datePickerDialog.datePicker.maxDate = now
        datePickerDialog.show()
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
        mDataBinding.layoutProfilePersonal.tvDob.setOnClickListener {
            pickDate()
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

        mDataBinding.toolbarSubmit.setOnClickListener {
            updatePatient()
        }

        mDataBinding.layoutProfilePersonal.imgProf.setOnClickListener {
            checkPermission()
        }
        mDataBinding.layoutProfileLifestyle.rgSmoking.setOnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = group.findViewById(checkedId)
            viewModel.smoking.set(radio.text.toString().toUpperCase())

        }
        mDataBinding.layoutProfileLifestyle.rgAlcohol.setOnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = group.findViewById(checkedId)
            viewModel.alcohol.set(radio.text.toString().toUpperCase())
        }
    }

    private fun validateEditPatient():Boolean{
        var isValid:Boolean=true
        if (viewModel.email.get().isNullOrBlank()) {
            ViewUtils.showToast(this@ProfileActivity, R.string.error_invalid_email_address, false)
            isValid=false
        }else if (!ValidationUtils.isValidEmail(viewModel.email.get()!!)){
            ViewUtils.showToast(this@ProfileActivity, R.string.error_invalid_email_address, false)
            isValid=false
        }else if(viewModel.number.get().isNullOrBlank()) {
            ViewUtils.showToast(this@ProfileActivity, R.string.error_invalid_mobile, false)
            isValid = false
        }else if(viewModel.firstName.get().isNullOrBlank()) {
            ViewUtils.showToast(this@ProfileActivity, R.string.error_invalid_first_name, false)
            isValid = false
        }else if(viewModel.lastName.get().isNullOrBlank()) {
            ViewUtils.showToast(this@ProfileActivity, R.string.error_invalid__last_name, false)
            isValid = false
        }
        return isValid
    }

    private fun updatePatient() {
        if(!validateEditPatient())
            return
        if(mCropImageUri?.path != null){
            val pictureFile = File(mCropImageUri?.path!!)
            if (pictureFile.exists()) {
                val requestFile = pictureFile.asRequestBody("*/*".toMediaTypeOrNull())
                val fileBody =
                    MultipartBody.Part.createFormData("profile_pic", pictureFile.name, requestFile)
                loadingObservable.value = true
                viewModel.editPatientWithImage(fileBody)
            }
        }else{
            loadingObservable.value = true
            viewModel.editPatient()
        }
    }

    private var mCropImageUri: Uri? = null
    private fun checkPermission() {
        Dexter.withActivity(this@ProfileActivity)
            .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    CropImage.startPickImageActivity(this@ProfileActivity)
                }

                override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) {
                    //close activity
                    token?.continuePermissionRequest()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    //close activity
                    ViewUtils.showToast(applicationContext, "Unable to perform this action", false)
                    //finish()
                }

            }).check()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_CANCELED) {
            if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE) {
                val imageUri = CropImage.getPickImageResultUri(this, data)

                // For API >= 23 we need to check specifically that we have permissions to read external storage.
                if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                    // request permissions and handle the result in onRequestPermissionsResult()
                    mCropImageUri = imageUri
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 0)
                    }
                } else {
                    startCropImageActivity(imageUri)
                }
            }

            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                val result = CropImage.getActivityResult(data)
                if (resultCode == Activity.RESULT_OK) {
                    mDataBinding.layoutProfilePersonal.imgProf.setImageURI(result.uri)
                    mCropImageUri = result.uri
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Toast.makeText(this, "Cropping failed: ", Toast.LENGTH_LONG).show()
                }
            }
        }
    }


    private fun startCropImageActivity(imageUri: Uri) {
        try {
            CropImage.activity(imageUri)
                .setFixAspectRatio(true)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setCropShape(CropImageView.CropShape.OVAL)
                .setMultiTouchEnabled(true)
                .start(this)
        }catch (ex:Exception){
            Log.e("CropImage",ex.message)
        }


    }


    private fun observeResponse() {

        viewModel.mEditpatientResponse.observe(this, androidx.lifecycle.Observer {
            //if (it.isStatus) {
            ViewUtils.showToast(
                this@ProfileActivity,
                getString(R.string.profile_successfully_edited),
                true
            )
            val newIntent = Intent(this,MainActivity::class.java)
            startActivity(newIntent)
            finishAffinity()
            //}
        })

        viewModel.mProfileResponse.observe(this, Observer<ProfileResponse> {
            loadingObservable.value = false

            if ( it.patient?.profile?.profile_pic!=null) {
                Glide.with(this)
                    .load(BuildConfig.BASE_IMAGE_URL+it.patient?.profile?.profile_pic)
                    .error(R.drawable.app_logo)
                    .placeholder(R.drawable.app_logo)
                    .into( profile_img)
            }
            preferenceHelper.setValue(PreferenceKey.WALLET_BALANCE, it.patient?.wallet_balance)
            viewModel.firstName.set(it.patient?.first_name?:"")
            viewModel.id.set(it.patient?.id)
            viewModel.lastName.set(it.patient?.last_name?:"")
            viewModel.number.set(it.patient?.phone?:"")
            viewModel.email.set(it.patient?.email?:"")
            viewModel.gender.set(it.patient?.profile?.gender?:"")
            viewModel.dob.set(it.patient.profile?.dob?:"")
            viewModel.bloodgroup.set(it.patient?.profile?.blood_group?:"")
            viewModel.marital.set(it.patient.profile?.merital_status?:"")
            viewModel.height.set(it.patient?.profile?.height?:"")
            viewModel.weight.set(it.patient?.profile?.weight?:"")
            viewModel.emgcontact.set(it.patient?.profile?.emergency_contact?:"")
            viewModel.location.set(it.patient?.profile?.address?:"")

            viewModel.allergies.set(it.patient.profile?.allergies?:"")
            viewModel.current_medications.set(it.patient.profile?.current_medications?:"")
            viewModel.past_medications.set(it.patient.profile?.past_medications?:"")
            viewModel.chronic_diseases.set(it.patient.profile?.chronic_diseases?:"")
            viewModel.injuries.set(it.patient.profile?.injuries?:"")
            viewModel.surgeries.set(it.patient.profile?.surgeries?:"")
if(it.patient.profile?.alcohol!=null && it.patient.profile?.alcohol.equals("YES"))
    mDataBinding.layoutProfileLifestyle.alcoholYes.isChecked=true
else if(it.patient.profile?.alcohol!=null && it.patient.profile?.alcohol.equals("NO"))
    mDataBinding.layoutProfileLifestyle.alcoholNo.isChecked=true
if(it.patient.profile?.smoking!=null && it.patient.profile?.smoking.equals("YES"))
    mDataBinding.layoutProfileLifestyle.smokingYes.isChecked=true
else if(it.patient.profile?.smoking!=null && it.patient.profile?.smoking.equals("NO"))
    mDataBinding.layoutProfileLifestyle.smokingNo.isChecked=true
            viewModel.smoking.set(it.patient.profile?.smoking?:"")
            viewModel.alcohol.set(it.patient.profile?.alcohol?:"")
            viewModel.activity.set(it.patient.profile?.activity?:"")
            viewModel.food.set(it.patient.profile?.food?:"")
            viewModel.occupation.set(it.patient.profile?.occupation?:"")




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
