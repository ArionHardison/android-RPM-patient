package com.telehealthmanager.app.ui.activity.profile

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.text.InputType
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.telehealthmanager.app.BaseApplication
import com.telehealthmanager.app.BuildConfig
import com.telehealthmanager.app.R
import com.telehealthmanager.app.base.BaseActivity
import com.telehealthmanager.app.data.*
import com.telehealthmanager.app.databinding.ActivityProfileBinding
import com.telehealthmanager.app.ui.activity.allergies.AllergiesActivity
import com.telehealthmanager.app.ui.activity.main.MainActivity
import com.telehealthmanager.app.utils.*
import com.telehealthmanager.app.utils.ImagePickerActivity.PickerOptionListener
import com.telehealthmanager.app.utils.ViewUtils.selectGender
import com.telehealthmanager.app.utils.ViewUtils.selectMarital
import kotlinx.android.synthetic.main.activity_profile.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class ProfileActivity : BaseActivity<ActivityProfileBinding>(), ProfileNavigator, CustomBackClick {

    private val preferenceHelper = PreferenceHelper(BaseApplication.baseApplication)
    private lateinit var viewModel: ProfileViewModel
    private lateinit var mDataBinding: ActivityProfileBinding
    private lateinit var profileImg: ImageView
    private var viewType: String = ""
    private var relativeManagementID: Int = 0

    override fun getLayoutId(): Int = R.layout.activity_profile

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mDataBinding = mViewDataBinding as ActivityProfileBinding
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        mDataBinding.viewmodel = viewModel
        viewModel.navigator = this
        viewType = intent.getStringExtra(Constant.IntentData.IS_VIEW_TYPE) as String
        relativeManagementID = intent.getIntExtra(Constant.IntentData.IS_RELATIVE_ID, 0) as Int
        viewModel.patientId.set(preferenceHelper.getValue(PreferenceKey.PATIENT_ID, 1).toString())
        Places.initialize(this@ProfileActivity, resources.getString(R.string.google_map_api))
        viewModel.setOnClickListener(this@ProfileActivity)
        mDataBinding.titleText2.text = getString(R.string.your_profile)
        initUI()
        initApiCal()
        observeResponse()
        // Clearing older images from cache directory
        // don't call this line if you want to choose multiple images in the same activity
        // call this once the bitmap(s) usage is over
        ImagePickerActivity.clearCache(this)
    }

    override fun clickBackPress() {
        finish()
    }

    private fun initApiCal() {
        viewModel.viewType.set(viewType)
        when (viewType) {
            "home" -> {
                loadingObservable.value = true
                viewModel.getProfile()
                viewModel.updateText.set("Done")
            }
            "edit_relative" -> {
                loadingObservable.value = true
                viewModel.updateText.set("Updated")
                viewModel.relativeId.set(relativeManagementID.toString())
                viewModel.getRelateProfile()
            }
            "add_relative" -> {
                viewModel.updateText.set("Add")
                mDataBinding.layoutProfilePersonal.edPhoneNumber.inputType = InputType.TYPE_CLASS_NUMBER
            }
            else -> {
                viewModel.updateText.set("Done")
                loadingObservable.value = true
                viewModel.getProfile()
            }
        }
    }

    private fun pickDate() {
        val c = Calendar.getInstance()
        val mYear = c.get(Calendar.YEAR)
        val mMonth = c.get(Calendar.MONTH)
        val mDay = c.get(Calendar.DAY_OF_MONTH)
        val now = System.currentTimeMillis() - 1000
        val maxDate = System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 3)
        val datePickerDialog =
            DatePickerDialog(
                this@ProfileActivity, R.style.CalandrTheamDialog,
                { view, year, monthOfYear, dayOfMonth ->
                    c.set(Calendar.YEAR, year)
                    c.set(Calendar.MONTH, monthOfYear)
                    c.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    val sdf =
                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    mDataBinding.layoutProfilePersonal.tvDob.setText(sdf.format(c.getTime()))
                }, mYear, mMonth, mDay
            )
        // datePickerDialog.datePicker.minDate = now
        datePickerDialog.datePicker.maxDate = now
        datePickerDialog.show()
    }

    private fun initUI() {
        profileImg = findViewById(R.id.img_prof)

        button11.setOnClickListener {
            visiblePersonal()
        }

        mDataBinding.layoutProfilePersonal.tvDob.setOnClickListener {
            pickDate()
        }

        button12.setOnClickListener {
            visibleMedical()
        }

        button13.setOnClickListener {
            visibleLifestyle()
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

    private fun visiblePersonal() {
        customColorButton(button11)
        customWhiteColorButton(button12)
        customWhiteColorButton(button13)
        layout_profile_personal.visibility = View.VISIBLE
        layout_profile_medical.visibility = View.GONE
        layout_profile_lifestyle.visibility = View.GONE
    }

    private fun visibleMedical() {
        customColorButton(button12)
        customWhiteColorButton(button11)
        customWhiteColorButton(button13)
        layout_profile_personal.visibility = View.GONE
        layout_profile_medical.visibility = View.VISIBLE
        layout_profile_lifestyle.visibility = View.GONE
    }

    private fun visibleLifestyle() {
        customColorButton(button13)
        customWhiteColorButton(button11)
        customWhiteColorButton(button12)
        layout_profile_personal.visibility = View.GONE
        layout_profile_medical.visibility = View.GONE
        layout_profile_lifestyle.visibility = View.VISIBLE
    }

    private fun validPersonal(): Boolean {
        var isValid = true
        if (viewModel.firstName.get().isNullOrBlank()) {
            ViewUtils.showToast(this@ProfileActivity, R.string.error_invalid_first_name, false)
            isValid = false
        } else if (viewModel.lastName.get().isNullOrBlank()) {
            ViewUtils.showToast(this@ProfileActivity, R.string.error_invalid__last_name, false)
            isValid = false
        } else if (viewModel.number.get().isNullOrBlank()) {
            ViewUtils.showToast(this@ProfileActivity, R.string.error_invalid_mobile, false)
            isValid = false
        } else if (viewModel.email.get().isNullOrBlank()) {
            ViewUtils.showToast(this@ProfileActivity, R.string.error_invalid_email_address, false)
            isValid = false
        } else if (!ValidationUtils.isValidEmail(viewModel.email.get()!!)) {
            ViewUtils.showToast(this@ProfileActivity, R.string.error_invalid_email_address, false)
            isValid = false
        } else if (viewModel.gender.get().isNullOrBlank()) {
            ViewUtils.showToast(this@ProfileActivity, R.string.error_invalid_gender, false)
            isValid = false
        } else if (viewModel.dob.get().isNullOrBlank()) {
            ViewUtils.showToast(this@ProfileActivity, R.string.error_invalid_dob, false)
            isValid = false
        } else if (viewModel.bloodgroup.get().isNullOrBlank()) {
            ViewUtils.showToast(this@ProfileActivity, R.string.error_invalid__blood_group, false)
            isValid = false
        } else if (viewModel.marital.get().isNullOrBlank()) {
            ViewUtils.showToast(this@ProfileActivity, R.string.error_invalid__marital, false)
            isValid = false
        } else if (viewModel.height.get().isNullOrBlank()) {
            ViewUtils.showToast(this@ProfileActivity, R.string.error_invalid__height, false)
            isValid = false
        } else if (viewModel.weight.get().isNullOrBlank()) {
            ViewUtils.showToast(this@ProfileActivity, R.string.error_invalid__weight, false)
            isValid = false
        } else if (viewModel.emgcontact.get().isNullOrBlank()) {
            ViewUtils.showToast(this@ProfileActivity, R.string.error_invalid_mobile_, false)
            isValid = false
        } else if (viewModel.location.get().isNullOrBlank()) {
            ViewUtils.showToast(this@ProfileActivity, R.string.please_enter_location, false)
            isValid = false
        }
        return isValid
    }

    private fun validMedical(): Boolean {
        var isValid = true
        if (viewModel.allergies.get().isNullOrBlank()) {
            ViewUtils.showToast(this@ProfileActivity, R.string.error_invalid__allergies, false)
            isValid = false
        } else if (viewModel.current_medications.get().isNullOrBlank()) {
            ViewUtils.showToast(this@ProfileActivity, R.string.error_invalid__current_medications, false)
            isValid = false
        } else if (viewModel.past_medications.get().isNullOrBlank()) {
            ViewUtils.showToast(this@ProfileActivity, R.string.error_invalid__past_medications, false)
            isValid = false
        } else if (viewModel.chronic_diseases.get().isNullOrBlank()) {
            ViewUtils.showToast(this@ProfileActivity, R.string.error_invalid__chronic_diseases, false)
            isValid = false
        } else if (viewModel.injuries.get().isNullOrBlank()) {
            ViewUtils.showToast(this@ProfileActivity, R.string.error_invalid_injuries, false)
            isValid = false
        } else if (viewModel.surgeries.get().isNullOrBlank()) {
            ViewUtils.showToast(this@ProfileActivity, R.string.error_invalid_surgeries, false)
            isValid = false
        }
        return isValid
    }

    private fun validLifestyle(): Boolean {
        var isValid = true
        if (viewModel.smoking.get().isNullOrBlank()) {
            ViewUtils.showToast(this@ProfileActivity, R.string.error_invalid__smoking, false)
            isValid = false
        } else if (viewModel.alcohol.get().isNullOrBlank()) {
            ViewUtils.showToast(this@ProfileActivity, R.string.error_invalid__alcohol, false)
            isValid = false
        } else if (viewModel.activity.get().isNullOrBlank()) {
            ViewUtils.showToast(this@ProfileActivity, R.string.error_invalid__activity, false)
            isValid = false
        } else if (viewModel.food.get().isNullOrBlank()) {
            ViewUtils.showToast(this@ProfileActivity, R.string.error_invalid__food, false)
            isValid = false
        } else if (viewModel.occupation.get().isNullOrBlank()) {
            ViewUtils.showToast(this@ProfileActivity, R.string.error_invalid_occupation, false)
            isValid = false
        }
        return isValid
    }

    private fun updatePatient() {
        if (!validPersonal()) {
            visiblePersonal()
            return
        } else if (!validMedical()) {
            visibleMedical()
            return
        } else if (!validLifestyle()) {
            visibleLifestyle()
            return
        }

        if (mCropImageUri?.path != null) {
            val pictureFile = File(mCropImageUri?.path!!)
            if (pictureFile.exists()) {
                val requestFile = pictureFile.asRequestBody("*/*".toMediaTypeOrNull())
                val fileBody = MultipartBody.Part.createFormData("profile_pic", pictureFile.name, requestFile)
                loadingObservable.value = true
                when (viewType) {
                    "home" -> {
                        viewModel.updatePatientWithImage(fileBody)
                    }
                    "edit_relative" -> {
                        viewModel.updateRelativeWithImage(fileBody)
                    }
                    "add_relative" -> {
                        viewModel.addRelativeWithImage(fileBody)
                    }
                    else -> {
                        viewModel.updatePatientWithImage(fileBody)
                    }
                }
            }
        } else {
            loadingObservable.value = true
            when (viewType) {
                "home" -> {
                    viewModel.updatePatient()
                }
                "edit_relative" -> {
                    viewModel.updateRelative()
                }
                "add_relative" -> {
                    viewModel.addRelative()
                }
                else -> {
                    viewModel.updatePatient()
                }
            }

        }
    }

    private var mCropImageUri: Uri? = null
    private fun checkPermission() {
        Dexter.withContext(this).withPermissions(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        ).withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                report?.let {
                    if (report.areAllPermissionsGranted()) {
                        showImagePickerOptions()
                    }
                }
            }

            override fun onPermissionRationaleShouldBeShown(
                permissions: List<PermissionRequest?>?,
                token: PermissionToken?
            ) {
                token?.continuePermissionRequest()
            }
        }).withErrorListener {
            ViewUtils.showToast(applicationContext, "Unable to perform this action", false)
        }.check()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_CANCELED) {
            when (requestCode) {
                Constant.REQUEST_AUTOCOMPLETE -> {
                    val place: Place = Autocomplete.getPlaceFromIntent(data!!)
                    viewModel.location.set(place.name.toString().plus(", ").plus(place.address.toString()))
                }

                Constant.REQUEST_CODE_ALLERGIES -> {
                    val allergies = data!!.getStringExtra("select_allergies") as String
                    viewModel.allergies.set(allergies)
                }

                Constant.REQUEST_IMAGE_PICK -> {
                    val uri = data?.getParcelableExtra<Uri>("path")!!
                    Glide.with(applicationContext)
                        .load(uri)
                        .thumbnail(0.5f)
                        .error(R.drawable.user_placeholder)
                        .placeholder(R.drawable.user_placeholder)
                        .into(mDataBinding.layoutProfilePersonal.imgProf)
                    mCropImageUri = uri
                }
            }
        }
    }


    private fun showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(this, object : PickerOptionListener {
            override fun onTakeCameraSelected() {
                launchCameraIntent()
            }

            override fun onChooseGallerySelected() {
                launchGalleryIntent()
            }
        })
    }

    private fun launchCameraIntent() {
        val intent = Intent(this@ProfileActivity, ImagePickerActivity::class.java)
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE)
        startActivityForResult(intent, Constant.REQUEST_IMAGE_PICK)
    }

    private fun launchGalleryIntent() {
        val intent = Intent(this@ProfileActivity, ImagePickerActivity::class.java)
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE)
        startActivityForResult(intent, Constant.REQUEST_IMAGE_PICK)
    }


    private fun observeResponse() {

        viewModel.mEditPatientResponse.observe(this, {
            loadingObservable.value = false
            ViewUtils.showToast(this@ProfileActivity, getString(R.string.profile_successfully_edited), true)
            val newIntent = Intent(this, MainActivity::class.java)
            startActivity(newIntent)
            finishAffinity()
        })

        viewModel.mProfileResponse.observe(this, {
            loadingObservable.value = false
            if (it.patient?.profile?.profile_pic != null) {
                ViewUtils.setDocViewGlide(this, profileImg, BuildConfig.BASE_IMAGE_URL + it.patient.profile.profile_pic)
            }
            preferenceHelper.setValue(PreferenceKey.WALLET_BALANCE, it.patient?.wallet_balance)
            viewModel.firstName.set(it.patient?.first_name ?: "")
            viewModel.id.set(it.patient?.id)
            viewModel.lastName.set(it.patient?.last_name ?: "")
            viewModel.number.set(it.patient?.phone ?: "")
            viewModel.email.set(it.patient?.email ?: "")
            viewModel.gender.set(it.patient?.profile?.gender ?: "")
            viewModel.dob.set(it.patient.profile?.dob ?: "")
            viewModel.bloodgroup.set(it.patient?.profile?.blood_group ?: "")
            viewModel.marital.set(it.patient.profile?.merital_status ?: "")
            viewModel.height.set(it.patient?.profile?.height ?: "")
            viewModel.weight.set(it.patient?.profile?.weight ?: "")
            viewModel.emgcontact.set(it.patient?.profile?.emergency_contact ?: "")
            viewModel.location.set(it.patient?.profile?.address ?: "")
            viewModel.allergies.set(it.patient.profile?.allergies ?: "")
            viewModel.current_medications.set(it.patient.profile?.current_medications ?: "")
            viewModel.past_medications.set(it.patient.profile?.past_medications ?: "")
            viewModel.chronic_diseases.set(it.patient.profile?.chronic_diseases ?: "")
            viewModel.injuries.set(it.patient.profile?.injuries ?: "")
            viewModel.surgeries.set(it.patient.profile?.surgeries ?: "")
            if (it.patient.profile?.alcohol != null && it.patient.profile?.alcohol.equals("YES"))
                mDataBinding.layoutProfileLifestyle.alcoholYes.isChecked = true
            else if (it.patient.profile?.alcohol != null && it.patient.profile?.alcohol.equals("NO"))
                mDataBinding.layoutProfileLifestyle.alcoholNo.isChecked = true
            if (it.patient.profile?.smoking != null && it.patient.profile?.smoking.equals("YES"))
                mDataBinding.layoutProfileLifestyle.smokingYes.isChecked = true
            else if (it.patient.profile?.smoking != null && it.patient.profile?.smoking.equals("NO"))
                mDataBinding.layoutProfileLifestyle.smokingNo.isChecked = true
            viewModel.smoking.set(it.patient.profile?.smoking ?: "")
            viewModel.alcohol.set(it.patient.profile?.alcohol ?: "")
            viewModel.activity.set(it.patient.profile?.activity ?: "")
            viewModel.food.set(it.patient.profile?.food ?: "")
            viewModel.occupation.set(it.patient.profile?.occupation ?: "")
        })

        viewModel.mAddRelativeResponse.observe(this, Observer {
            loadingObservable.value = false
            ViewUtils.showToast(this@ProfileActivity, it.message, true)
            val intent = Intent()
            setResult(RESULT_OK, intent)
            finish()
        })

        viewModel.mRelativeResponse.observe(this, {
            loadingObservable.value = false
            if (it.relative_detail?.profile?.profile_pic != null) {
                Glide.with(this)
                    .load(BuildConfig.BASE_IMAGE_URL + it.relative_detail?.profile?.profile_pic)
                    .error(R.drawable.user_placeholder)
                    .placeholder(R.drawable.user_placeholder)
                    .into(profileImg)
            }
            mDataBinding.titleText2.text = it.relative_detail?.first_name.plus(" ").plus(it.relative_detail?.last_name)
            viewModel.firstName.set(it.relative_detail?.first_name ?: "")
            viewModel.id.set(it.relative_detail?.id)
            viewModel.lastName.set(it.relative_detail?.last_name ?: "")
            viewModel.number.set(it.relative_detail?.phone ?: "")
            viewModel.email.set(it.relative_detail?.email ?: "")
            viewModel.gender.set(it.relative_detail?.profile?.gender ?: "")
            viewModel.dob.set(it.relative_detail?.profile?.dob ?: "")
            viewModel.bloodgroup.set(it.relative_detail?.profile?.blood_group ?: "")
            viewModel.marital.set(it.relative_detail?.profile?.merital_status ?: "")
            viewModel.height.set(it.relative_detail?.profile?.height ?: "")
            viewModel.weight.set(it.relative_detail?.profile?.weight ?: "")
            viewModel.emgcontact.set(it.relative_detail?.profile?.emergency_contact ?: "")
            viewModel.location.set(it.relative_detail?.profile?.address ?: "")
            viewModel.allergies.set(it.relative_detail?.profile?.allergies ?: "")
            viewModel.current_medications.set(it.relative_detail?.profile?.current_medications ?: "")
            viewModel.past_medications.set(it.relative_detail?.profile?.past_medications ?: "")
            viewModel.chronic_diseases.set(it.relative_detail?.profile?.chronic_diseases ?: "")
            viewModel.injuries.set(it.relative_detail?.profile?.injuries ?: "")
            viewModel.surgeries.set(it.relative_detail?.profile?.surgeries ?: "")
            if (it.relative_detail?.profile?.alcohol != null && it.relative_detail?.profile?.alcohol == "YES")
                mDataBinding.layoutProfileLifestyle.alcoholYes.isChecked = true
            else if (it.relative_detail?.profile?.alcohol != null && it.relative_detail?.profile?.alcohol == "NO")
                mDataBinding.layoutProfileLifestyle.alcoholNo.isChecked = true
            if (it.relative_detail?.profile?.smoking != null && it.relative_detail?.profile?.smoking == "YES")
                mDataBinding.layoutProfileLifestyle.smokingYes.isChecked = true
            else if (it.relative_detail?.profile?.smoking != null && it.relative_detail?.profile?.smoking == "NO")
                mDataBinding.layoutProfileLifestyle.smokingNo.isChecked = true
            viewModel.smoking.set(it.relative_detail?.profile?.smoking ?: "")
            viewModel.alcohol.set(it.relative_detail?.profile?.alcohol ?: "")
            viewModel.activity.set(it.relative_detail?.profile?.activity ?: "")
            viewModel.food.set(it.relative_detail?.profile?.food ?: "")
            viewModel.occupation.set(it.relative_detail?.profile?.occupation ?: "")
        })

        viewModel.getErrorObservable().observe(this, Observer<String> { message ->
            loadingObservable.value = false
            ViewUtils.showToast(this@ProfileActivity, message, false)
        })
    }

    private fun customColorButton(button: Button) {
        button.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.colorSecondary))
        button.setTextColor(ContextCompat.getColor(applicationContext, R.color.colorWhite))
    }

    private fun customWhiteColorButton(button: Button) {
        button.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.colorWhite))
        button.setTextColor(ContextCompat.getColor(applicationContext, R.color.colorBlack))
    }

    override fun onClickAllergies() {
        val newIntent = Intent(this, AllergiesActivity::class.java)
        startActivityForResult(newIntent, Constant.REQUEST_CODE_ALLERGIES)
    }

    override fun onClickLocation() {
        val fields: List<Place.Field> = listOf(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.NAME, Place.Field.ADDRESS)
        val intent: Intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(this)
        startActivityForResult(intent, Constant.REQUEST_AUTOCOMPLETE)
    }

    override fun onClickGender() {
        selectGender(this, "Select Gender", object : ViewCallBack.IItemClick {
            override fun alertItemClick(strItem: String) {
                viewModel.gender.set(strItem)
            }
        })
    }

    override fun onClickMartial() {
        selectMarital(this, "Select Marital", object : ViewCallBack.IItemClick {
            override fun alertItemClick(strItem: String) {
                viewModel.marital.set(strItem)
            }
        })
    }
}
