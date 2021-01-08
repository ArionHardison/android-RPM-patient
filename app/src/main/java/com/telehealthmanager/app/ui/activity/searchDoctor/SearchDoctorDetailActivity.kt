package com.telehealthmanager.app.ui.activity.searchDoctor

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.common.util.CollectionUtils
import com.telehealthmanager.app.BaseApplication
import com.telehealthmanager.app.BuildConfig
import com.telehealthmanager.app.R
import com.telehealthmanager.app.base.BaseActivity
import com.telehealthmanager.app.data.PreferenceHelper
import com.telehealthmanager.app.data.PreferenceKey
import com.telehealthmanager.app.data.getValue
import com.telehealthmanager.app.data.setValue
import com.telehealthmanager.app.databinding.ActivitySearchDoctorDetailBinding
import com.telehealthmanager.app.repositary.WebApiConstants
import com.telehealthmanager.app.repositary.model.DoctorListResponse
import com.telehealthmanager.app.repositary.model.Hospital
import com.telehealthmanager.app.repositary.model.MainResponse
import com.telehealthmanager.app.ui.activity.allservice.AllServiceActivity
import com.telehealthmanager.app.ui.activity.findDoctors.FindDoctorBookingActivity
import com.telehealthmanager.app.ui.adapter.AllServiceAdapter
import com.telehealthmanager.app.ui.adapter.AvailabilityAdapter
import com.telehealthmanager.app.ui.adapter.Doctor_feedbackAdapter
import com.telehealthmanager.app.ui.adapter.Doctors_photoAdapter
import com.telehealthmanager.app.utils.CustomBackClick
import com.telehealthmanager.app.utils.ViewUtils
import java.io.Serializable
import java.util.*

class SearchDoctorDetailActivity : BaseActivity<ActivitySearchDoctorDetailBinding>(),
    SearchNavigator, CustomBackClick {

    private val preferenceHelper = PreferenceHelper(BaseApplication.baseApplication)
    private lateinit var viewModel: SearchViewModel
    private lateinit var mDataBinding: ActivitySearchDoctorDetailBinding

    private var mAdapterPhotos: Doctors_photoAdapter? = null
    private var mAdapterFeedback: Doctor_feedbackAdapter? = null
    private var mAdapterService: AllServiceAdapter? = null
    private var mAvailabilityAdapter: AvailabilityAdapter? = null
    private var detailsType: String = ""

    override fun getLayoutId(): Int = R.layout.activity_search_doctor_detail

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mDataBinding = mViewDataBinding as ActivitySearchDoctorDetailBinding
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        mDataBinding.viewmodel = viewModel
        viewModel.navigator = this
        initIntentData()
        observeResponse()
        viewModel.setOnClickListener(this@SearchDoctorDetailActivity)
        viewModel.toolBarTile.value = ""
        mDataBinding.imageView27.setOnClickListener {
            if (!viewModel.clinicAddress.get().toString().equals("")) {
                goToMapLocation(viewModel.clinicAddress.get().toString())
            } else {
                ViewUtils.showToast(this@SearchDoctorDetailActivity, getString(R.string.no_address_found), false)
            }
        }
    }

    override fun clickBackPress() {
        onBackPressed()
    }

    private fun initIntentData() {
        mDataBinding.isVideoVisible = false
        val details = intent.getSerializableExtra(WebApiConstants.IntentPass.DoctorProfile) as? DoctorListResponse.specialities.DoctorProfile
        val favDoctor = intent.getSerializableExtra(WebApiConstants.IntentPass.FavDoctorProfile) as? MainResponse.Doctor
        val searchDoctor = intent.getSerializableExtra(WebApiConstants.IntentPass.SearchDoctorProfile) as? Hospital
        when {
            details != null -> {
                detailsType = WebApiConstants.IntentPass.DoctorProfile
                viewModel.mDoctorProfile.value = details
                details.let { it ->
                    viewModel.id.set(it.doctor_id)
                    viewModel.profilePic.set(it.profile_pic)
                    viewModel.experience.set(it.experience.emptyToNull() ?: "0")
                    viewModel.fee.set(preferenceHelper.getValue(PreferenceKey.CURRENCY, "$").toString().plus(it.fees ?: "0"))
                    ViewUtils.setDocViewGlide(this@SearchDoctorDetailActivity, mDataBinding.imageView25, BuildConfig.BASE_IMAGE_URL.plus(it.profile_pic.emptyToNull() ?: ""))

                    it.profile_video.let { video ->
                        mDataBinding.isVideoVisible = true
                        viewModel.profileVideo.set(video)
                    }

                    val specialist: StringBuilder = StringBuilder()
                    specialist.append(it.certification.emptyToNull() ?: "")
                    it.speciality?.let { speciality ->
                        if (specialist.toString() != "") {
                            specialist.append(" - ").append(speciality.name.emptyToNull() ?: "")
                        } else {
                            specialist.append(speciality.name.emptyToNull() ?: "")
                        }
                        viewModel.specialities.set(speciality.name.emptyToNull() ?: "")
                        viewModel.specialitiesID.set(speciality.id.toString())
                        viewModel.degreeSpecialities.set(specialist.toString())
                    }

                    if (it.hospital.isNotEmpty()) {
                        it.hospital[0].let { hospital ->
                            viewModel.percentage.set((hospital.feedback_percentage.emptyToNull() ?: "0").plus("%"))
                            viewModel.favourite.set(hospital.is_favourite.emptyToNull() ?: "false")
                            viewModel.name.set(hospital.first_name ?: "".plus(" ").plus(hospital.last_name ?: ""))
                            viewModel.mfeedbacklist = hospital.feedback as MutableList<Hospital.Feedback>?
                            viewModel.mservcielist = hospital.doctor_service as MutableList<Hospital.DoctorService>?
                            viewModel.mTimingList = hospital.timing as MutableList<Hospital.Timing>?
                        }

                        mDataBinding.locationView.visibility = View.GONE
                        it.hospital[0].clinic?.let { clinic ->
                            viewModel.clinic.set(clinic.name ?: "No clinic")
                            viewModel.clinicAddress.set(clinic.address ?: "No address")

                            val staticMap = clinic.address ?: ""
                            if (staticMap != "") {
                                mDataBinding.locationView.visibility = View.VISIBLE
                                ViewUtils.setMapViewGlide(this@SearchDoctorDetailActivity, mDataBinding.imageView27, clinic.static_map)
                            } else {
                                mDataBinding.locationView.visibility = View.GONE
                            }

                            if (!clinic.clinic_photo.isNullOrEmpty()) {
                                viewModel.mphotoslist = clinic.clinic_photo as MutableList<Hospital.Clinic.photos>?
                            }
                        }
                    }
                }
                initAdapter()
            }
            favDoctor != null -> {
                detailsType = WebApiConstants.IntentPass.FavDoctorProfile

                viewModel.mFavDoctorProfile.value = favDoctor
                favDoctor.hospital?.let {
                    viewModel.id.set(it.id)
                    viewModel.name.set(it.first_name ?: "".plus(" ").plus(it.last_name ?: ""))
                    viewModel.favourite.set(it.is_favourite.emptyToNull() ?: "false")
                    viewModel.percentage.set((it.feedback_percentage.emptyToNull() ?: "0").plus("%"))
                    viewModel.mfeedbacklist = it.feedback as MutableList<Hospital.Feedback>?
                    viewModel.mservcielist = it.doctor_service as MutableList<Hospital.DoctorService>?
                    viewModel.mTimingList = it.timing as MutableList<Hospital.Timing>?

                    it.doctor_profile.let { profile ->
                        ViewUtils.setDocViewGlide(this@SearchDoctorDetailActivity, mDataBinding.imageView25, BuildConfig.BASE_IMAGE_URL.plus(profile.profile_pic.emptyToNull() ?: ""))
                        viewModel.profilePic.set(profile.profile_pic.emptyToNull() ?: "")
                        viewModel.experience.set(profile.experience.emptyToNull() ?: "0")
                        viewModel.fee.set(preferenceHelper.getValue(PreferenceKey.CURRENCY, "$").toString().plus(profile.fees ?: "0"))

                        profile.profile_video.let { video ->
                            mDataBinding.isVideoVisible = true
                            viewModel.profileVideo.set(video)
                        }

                        val specialist: StringBuilder = StringBuilder()
                        specialist.append(profile.certification ?: "")
                        profile.speciality?.let { speciality ->
                            if (specialist.toString() != "") {
                                specialist.append(" - ").append(speciality.name ?: "")
                            } else {
                                specialist.append(speciality.name ?: "")
                            }
                            viewModel.specialities.set(speciality.name ?: "")
                            viewModel.specialitiesID.set(speciality.id.toString())
                            viewModel.degreeSpecialities.set(specialist.toString())
                        }
                    }
                    mDataBinding.locationView.visibility = View.GONE
                    it.clinic?.let { clinic ->
                        viewModel.clinic.set(clinic.name ?: "No clinic")
                        viewModel.clinicAddress.set(clinic.address ?: "No address")

                        val staticMap = clinic.address ?: ""
                        if (staticMap != "") {
                            mDataBinding.locationView.visibility = View.VISIBLE
                            ViewUtils.setMapViewGlide(this@SearchDoctorDetailActivity, mDataBinding.imageView27, clinic.static_map)
                        } else {
                            mDataBinding.locationView.visibility = View.GONE
                        }

                        if (!clinic.clinic_photo.isNullOrEmpty()) {
                            viewModel.mphotoslist = clinic.clinic_photo as MutableList<Hospital.Clinic.photos>?
                        }
                    }
                }
                initAdapter()

            }
            searchDoctor != null -> {
                detailsType = WebApiConstants.IntentPass.SearchDoctorProfile
                viewModel.mSearchDoctorProfile.value = searchDoctor
                searchDoctor.let {
                    viewModel.id.set(it.id)
                    viewModel.name.set(it.first_name ?: "".plus(" ").plus(it.last_name ?: ""))
                    viewModel.favourite.set(it.is_favourite.emptyToNull() ?: "false")
                    viewModel.percentage.set((it.feedback_percentage.emptyToNull() ?: "0").plus("%"))
                    viewModel.mfeedbacklist = it.feedback as MutableList<Hospital.Feedback>?
                    viewModel.mservcielist = it.doctor_service as MutableList<Hospital.DoctorService>?
                    viewModel.mTimingList = it.timing as MutableList<Hospital.Timing>?

                    it.doctor_profile.let { profile ->
                        ViewUtils.setDocViewGlide(this@SearchDoctorDetailActivity, mDataBinding.imageView25, BuildConfig.BASE_IMAGE_URL.plus(profile.profile_pic.emptyToNull() ?: ""))
                        viewModel.profilePic.set(profile.profile_pic.emptyToNull() ?: "")
                        viewModel.experience.set(profile.experience.emptyToNull() ?: "0")
                        viewModel.fee.set(preferenceHelper.getValue(PreferenceKey.CURRENCY, "$").toString().plus(profile.fees ?: "0"))

                        profile.profile_video.let { video ->
                            mDataBinding.isVideoVisible = true
                            viewModel.profileVideo.set(video)
                        }

                        val specialist: StringBuilder = StringBuilder()
                        specialist.append(profile.certification ?: "")
                        profile.speciality?.let { speciality ->
                            if (specialist.toString() != "") {
                                specialist.append(" - ").append(speciality.name ?: "")
                            } else {
                                specialist.append(speciality.name ?: "")
                            }
                            viewModel.specialities.set(speciality.name ?: "")
                            viewModel.specialitiesID.set(speciality.id.toString())
                            viewModel.degreeSpecialities.set(specialist.toString())
                        }
                    }

                    mDataBinding.locationView.visibility = View.GONE
                    it.clinic?.let { clinic ->
                        viewModel.clinic.set(clinic.name ?: "No clinic")
                        viewModel.clinicAddress.set(clinic.address ?: "No address")

                        val staticMap = clinic.address ?: ""
                        if (staticMap != "") {
                            mDataBinding.locationView.visibility = View.VISIBLE
                            ViewUtils.setMapViewGlide(this@SearchDoctorDetailActivity, mDataBinding.imageView27, clinic.static_map)
                        } else {
                            mDataBinding.locationView.visibility = View.GONE
                        }

                        if (!clinic.clinic_photo.isNullOrEmpty()) {
                            viewModel.mphotoslist = clinic.clinic_photo as MutableList<Hospital.Clinic.photos>?
                        }
                    }

                }
                initAdapter()
            }
        }

        if (viewModel.mTimingList != null) {
            for (item in viewModel.mTimingList!!) {
                setAvailabilityVisibility(item.day)
            }
        }
    }

    private val TAG = "SearchDoctorDetailActiv"
    private fun setAvailabilityVisibility(day: String) {
        val currentDay = ViewUtils.getCurrentDate("EEE").toLowerCase(Locale.ROOT)
        Log.d(TAG, "setAvailabilityVisibility: $currentDay $day")
        when (currentDay.toLowerCase(Locale.ROOT)) {
            /*"all" -> {
                mDataBinding.textViewMon.setTextColor(ContextCompat.getColor(applicationContext, R.color.colorGreen))
                mDataBinding.textViewTue.setTextColor(ContextCompat.getColor(applicationContext, R.color.colorGreen))
                mDataBinding.textViewWed.setTextColor(ContextCompat.getColor(applicationContext, R.color.colorGreen))
                mDataBinding.textViewThur.setTextColor(ContextCompat.getColor(applicationContext, R.color.colorGreen))
                mDataBinding.textViewFri.setTextColor(ContextCompat.getColor(applicationContext, R.color.colorGreen))
                mDataBinding.textViewSat.setTextColor(ContextCompat.getColor(applicationContext, R.color.colorGreen))
                mDataBinding.textViewSun.setTextColor(ContextCompat.getColor(applicationContext, R.color.colorGreen))
            }*/
            "mon" -> /*if ("mon" == currentDay)*/mDataBinding.textViewMon.setTextColor(ContextCompat.getColor(applicationContext, R.color.colorRed))
            "tue" -> /*if ("tue" == currentDay)*/mDataBinding.textViewTue.setTextColor(ContextCompat.getColor(applicationContext, R.color.colorRed))
            "wed" -> /*if ("wed" == currentDay)*/mDataBinding.textViewWed.setTextColor(ContextCompat.getColor(applicationContext, R.color.colorRed))
            "thu" -> /*if ("thu" == currentDay)*/mDataBinding.textViewThur.setTextColor(ContextCompat.getColor(applicationContext, R.color.colorRed))
            "fri" -> /*if ("fri" == currentDay)*/mDataBinding.textViewFri.setTextColor(ContextCompat.getColor(applicationContext, R.color.colorRed))
            "sat" -> /*if ("sat" == currentDay)*/mDataBinding.textViewSat.setTextColor(ContextCompat.getColor(applicationContext, R.color.colorRed))
            "sun" -> /*if ("sun" == currentDay)*/mDataBinding.textViewSun.setTextColor(ContextCompat.getColor(applicationContext, R.color.colorRed))
        }
    }

    private fun initAdapter() {
        isFavourite(viewModel.favourite.get().toString())

        if (!CollectionUtils.isEmpty(viewModel.mfeedbacklist)) {
            mAdapterFeedback = Doctor_feedbackAdapter(viewModel.mfeedbacklist!!, this@SearchDoctorDetailActivity)
            mDataBinding.feedbackadapter = mAdapterFeedback
            mDataBinding.recyclerView7.layoutManager = LinearLayoutManager(this@SearchDoctorDetailActivity, LinearLayoutManager.HORIZONTAL, false)
            mAdapterFeedback!!.notifyDataSetChanged()
            mDataBinding.recyclerView7.visibility = View.VISIBLE
        } else {
            mDataBinding.recyclerView7.visibility = View.GONE
            mDataBinding.textView120.visibility = View.GONE
        }

        if (!CollectionUtils.isEmpty(viewModel.mservcielist)) {
            mAdapterService = AllServiceAdapter(this@SearchDoctorDetailActivity, viewModel.mservcielist!!)
            mDataBinding.serviceadapter = mAdapterService
            mDataBinding.rvServices.layoutManager = LinearLayoutManager(this@SearchDoctorDetailActivity, LinearLayoutManager.VERTICAL, false)
            mAdapterService!!.notifyDataSetChanged()
            if (viewModel.mservcielist!!.size > 4) {
                mDataBinding.serviceViewAll.visibility = View.VISIBLE
            } else {
                mDataBinding.serviceViewAll.visibility = View.GONE
            }
        } else {
            mDataBinding.rvServices.visibility = View.GONE
            mDataBinding.textView111.visibility = View.GONE
            mDataBinding.divider13.visibility = View.GONE
        }

        if (!CollectionUtils.isEmpty(viewModel.mTimingList)) {
            mAvailabilityAdapter = AvailabilityAdapter(viewModel.mTimingList!!, this@SearchDoctorDetailActivity)
            mDataBinding.availibilityAdapter = mAvailabilityAdapter
            mDataBinding.rvAvailability.layoutManager = LinearLayoutManager(this@SearchDoctorDetailActivity, LinearLayoutManager.VERTICAL, false)
            mAvailabilityAdapter!!.notifyDataSetChanged()
            mDataBinding.textViewNotAvailable.visibility = View.GONE
        } else {
            mDataBinding.textViewNotAvailable.visibility = View.VISIBLE
        }

        if (!CollectionUtils.isEmpty(viewModel.mphotoslist)) {
            mAdapterPhotos = Doctors_photoAdapter(viewModel.mphotoslist!!, this@SearchDoctorDetailActivity)
            mDataBinding.photosadapter = mAdapterPhotos
            mDataBinding.recyclerView6.layoutManager = LinearLayoutManager(this@SearchDoctorDetailActivity, LinearLayoutManager.HORIZONTAL, false)
            mAdapterPhotos!!.notifyDataSetChanged()
            mDataBinding.recyclerView6.visibility = View.VISIBLE
            mDataBinding.textView119.visibility = View.VISIBLE
        } else {
            mDataBinding.recyclerView6.visibility = View.GONE
            mDataBinding.textView119.visibility = View.GONE
        }

    }

    private fun isFavourite(data: String?) {
        viewModel.favourite.set(data)
        when (data) {
            "true" -> mDataBinding.imageView26.setImageResource(R.drawable.ic_favourite)
            "false" -> mDataBinding.imageView26.setImageResource(R.drawable.ic_favout_solid)
            else -> mDataBinding.imageView26.setImageResource(R.drawable.ic_favout_solid)
        }
    }

    private fun observeResponse() {
        viewModel.getErrorObservable().observe(this, { message ->
            hideLoading()
            ViewUtils.showToast(this@SearchDoctorDetailActivity, message, false)
        })

        viewModel.mFavResponse.observe(this, {
            hideLoading()
            ViewUtils.showToast(this@SearchDoctorDetailActivity, it.message, true)
            isFavourite(it.is_favourite.toString())
        })
    }

    private fun goToMapLocation(pinAddress: String) {
        val gmmIntentUri = Uri.parse("geo:0,0?q=$pinAddress")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        startActivity(mapIntent)
    }


    override fun onFavClick() {
        loadingObservable.value = true
        val hashMap: HashMap<String, Any> = HashMap()
        hashMap[WebApiConstants.Favourite.doctor_id] = viewModel.id.get().toString()
        hashMap[WebApiConstants.Favourite.patient_id] = preferenceHelper.getValue(PreferenceKey.PATIENT_ID, 1).toString()
        viewModel.addFav(hashMap)
    }

    override fun onShareClick() {

    }

    override fun onBookClick() {
        preferenceHelper.setValue(PreferenceKey.SELECTED_DOC_ID, viewModel.id.get().toString())
        preferenceHelper.setValue(PreferenceKey.SELECTED_DOC_NAME, viewModel.name.get().toString())
        preferenceHelper.setValue(PreferenceKey.SELECTED_DOC_Special, viewModel.specialities.get().toString())
        preferenceHelper.setValue(PreferenceKey.SELECTED_DOC_IMAGE, viewModel.profilePic.get().toString())
        preferenceHelper.setValue(PreferenceKey.SELECTED_DOC_SPECIALITY_ID, viewModel.specialitiesID.get().toString())
        preferenceHelper.setValue(PreferenceKey.SELECTED_DOC_ADDRESS, viewModel.clinic.get().plus(",").plus(viewModel.clinicAddress.get().toString()))
        val intent = Intent(this@SearchDoctorDetailActivity, FindDoctorBookingActivity::class.java)
        when (detailsType) {
            WebApiConstants.IntentPass.DoctorProfile -> intent.putExtra(WebApiConstants.IntentPass.DoctorProfile, viewModel.mDoctorProfile.value as Serializable)
            WebApiConstants.IntentPass.FavDoctorProfile -> intent.putExtra(WebApiConstants.IntentPass.FavDoctorProfile, viewModel.mFavDoctorProfile.value as Serializable)
            WebApiConstants.IntentPass.SearchDoctorProfile -> intent.putExtra(WebApiConstants.IntentPass.SearchDoctorProfile, viewModel.mSearchDoctorProfile.value as Serializable)
        }
        startActivity(intent);
    }

    override fun viewAllClick() {
        val intent = Intent(this, AllServiceActivity::class.java)
        intent.putExtra(WebApiConstants.IntentPass.SERVICE_LIST, viewModel.mservcielist as Serializable)
        startActivity(intent)
    }

    override fun viewVideoClick() {
        val i = Intent(applicationContext, VideoViewActivity::class.java)
        i.putExtra("url", BuildConfig.BASE_IMAGE_URL + viewModel.profileVideo.get().toString())
        startActivity(i)
    }

    override fun viewInfoClick() {

    }

    override fun viewShareClick() {

    }

    override fun onBackPressed() {
        val intent = Intent()
        intent.putExtra("is_favourite", viewModel.favourite.get().toString())
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun String?.emptyToNull(): String? {
        return if (this == null || this.isEmpty()) null else this
    }
}
