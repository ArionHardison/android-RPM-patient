package com.telehealthmanager.app.ui.activity.addmedicalrecord

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.telehealthmanager.app.R
import com.telehealthmanager.app.base.BaseActivity
import com.telehealthmanager.app.databinding.ActivityAddMedicalRecordBinding
import com.telehealthmanager.app.utils.CustomBackClick
import com.telehealthmanager.app.utils.ViewUtils
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class AddMedicalRecords : BaseActivity<ActivityAddMedicalRecordBinding>(), DoctorMedicalRecordsNavigator, CustomBackClick, DoctorListFragment.ISelectDoctor {

    private lateinit var viewModel: DoctorMedicalRecordsViewModel
    private lateinit var mDataBinding: ActivityAddMedicalRecordBinding
    private var mCropImageUri: Uri? = null

    override fun getLayoutId(): Int = R.layout.activity_add_medical_record

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mDataBinding = mViewDataBinding as ActivityAddMedicalRecordBinding
        viewModel = ViewModelProviders.of(this).get(DoctorMedicalRecordsViewModel::class.java)
        mDataBinding.viewmodel = viewModel
        viewModel.navigator = this

        initObservable()

        mDataBinding.imgPrescription.setOnClickListener {
            checkPermission()
        }
        mDataBinding.etDocName.setOnClickListener {
            selectDoctor()
        }

        mDataBinding.btnSubmit.setOnClickListener {
            if (validate()) {
                val pictureFile = File(mCropImageUri?.path!!)
                if (pictureFile.exists()) {
                    val requestFile = pictureFile.asRequestBody("*/*".toMediaTypeOrNull())
                    val fileBody = MultipartBody.Part.createFormData("prescription_image", pictureFile.name, requestFile)
                    viewModel.loadingProgress.value = true
                    viewModel.addMedicalRecord(fileBody)
                }
            }
        }

        viewModel.toolBarTile.value = getString(R.string.add_medical_record)
        viewModel.setOnClickListener(this@AddMedicalRecords)
    }

    private fun selectDoctor() {
        val mDoctorListFragment = DoctorListFragment.newInstance()
        mDoctorListFragment.setClickListener(this)
        mDoctorListFragment.show(supportFragmentManager, mDoctorListFragment.tag)
    }

    private fun initObservable() {
        viewModel.loadingProgress.observe(this, Observer {
            if (it)
                showLoading()
            else
                hideLoading()
        })

        viewModel.mAddResponse.observe(this, Observer {
            viewModel.loadingProgress.value = false
            ViewUtils.showToast(this, getString(R.string.success_record), true)
            finish()
        })

        viewModel.getErrorObservable().observe(this, Observer<String> { message ->
            viewModel.loadingProgress.value = false
            ViewUtils.showToast(this, message, false)
        })
    }

    override fun clickBackPress() {
        finish()
    }

    private fun validate(): Boolean {
        when {
            viewModel.mTestTitle.get().equals("") -> {
                ViewUtils.showToast(applicationContext, "Please enter " + getString(R.string.test_taken_previous_prescription), false)
                return false
            }

            viewModel.mTestDescription.get().equals("") -> {
                ViewUtils.showToast(applicationContext, "Please enter " + getString(R.string.description_instruction_given), false)
                return false
            }

            viewModel.mDoctorName.get().equals("") -> {
                ViewUtils.showToast(applicationContext, "Please enter doctor name", false)
                return false
            }

            mCropImageUri?.path == null -> {
                ViewUtils.showToast(applicationContext, "Please upload medical record image", false)
                return false
            }

            else -> return true
        }
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
                    mDataBinding.imgPrescription.setImageURI(result.uri)
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
        } catch (ex: Exception) {
            Log.e("CropImage", "" + ex.message)
        }
    }

    private fun checkPermission() {
        Dexter.withActivity(this@AddMedicalRecords)
            .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    CropImage.startPickImageActivity(this@AddMedicalRecords)
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {
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

    override fun onDocClicked(docID: String, docName: String) {
        viewModel.mSelectedDoctor.set(docID)
        viewModel.mDoctorName.set(docName)
        viewModel.mSelectedDoctor.notifyChange()
        viewModel.mDoctorName.notifyChange()
    }

}