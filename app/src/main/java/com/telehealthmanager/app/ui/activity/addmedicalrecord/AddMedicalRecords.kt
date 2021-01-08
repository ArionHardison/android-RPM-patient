package com.telehealthmanager.app.ui.activity.addmedicalrecord

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.telehealthmanager.app.R
import com.telehealthmanager.app.base.BaseActivity
import com.telehealthmanager.app.data.Constant
import com.telehealthmanager.app.databinding.ActivityAddMedicalRecordBinding
import com.telehealthmanager.app.utils.CustomBackClick
import com.telehealthmanager.app.utils.ImagePickerActivity
import com.telehealthmanager.app.utils.ViewUtils
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
        viewModel = ViewModelProvider(this).get(DoctorMedicalRecordsViewModel::class.java)
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
        // Clearing older images from cache directory
        // don't call this line if you want to choose multiple images in the same activity
        // call this once the bitmap(s) usage is over
        ImagePickerActivity.clearCache(this)
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
            val intent = Intent()
            setResult(RESULT_OK, intent)
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
            when (requestCode) {
                Constant.REQUEST_IMAGE_PICK -> {
                    if (resultCode == Activity.RESULT_OK) {
                        val uri = data?.getParcelableExtra<Uri>("path")!!
                        mDataBinding.imgPrescription.setImageURI(uri)
                        mCropImageUri = uri
                    }
                }
            }
        }
    }

    private fun checkPermission() {
        Dexter.withContext(this@AddMedicalRecords)
            .withPermissions(
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
                ViewUtils.showToast(this@AddMedicalRecords, "Unable to perform this action", false)
            }.check()
    }

    private fun showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(this, object : ImagePickerActivity.PickerOptionListener {
            override fun onTakeCameraSelected() {
                launchCameraIntent()
            }

            override fun onChooseGallerySelected() {
                launchGalleryIntent()
            }
        })
    }

    private fun launchCameraIntent() {
        val intent = Intent(this@AddMedicalRecords, ImagePickerActivity::class.java)
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE)
        startActivityForResult(intent, Constant.REQUEST_IMAGE_PICK)
    }

    private fun launchGalleryIntent() {
        val intent = Intent(this@AddMedicalRecords, ImagePickerActivity::class.java)
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE)
        startActivityForResult(intent, Constant.REQUEST_IMAGE_PICK)
    }

    override fun onDocClicked(docID: String, docName: String) {
        viewModel.mSelectedDoctor.set(docID)
        viewModel.mDoctorName.set(docName)
        viewModel.mSelectedDoctor.notifyChange()
        viewModel.mDoctorName.notifyChange()
    }

}