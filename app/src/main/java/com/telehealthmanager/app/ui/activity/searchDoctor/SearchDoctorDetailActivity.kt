package com.telehealthmanager.app.ui.activity.searchDoctor

import android.content.Intent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
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
import com.telehealthmanager.app.repositary.model.Response
import com.telehealthmanager.app.ui.activity.allservice.AllServiceActivity
import com.telehealthmanager.app.ui.activity.findDoctors.FindDoctorBookingActivity
import com.telehealthmanager.app.ui.adapter.AllServiceAdapter
import com.telehealthmanager.app.ui.adapter.AvailabilityAdapter
import com.telehealthmanager.app.ui.adapter.Doctor_feedbackAdapter
import com.telehealthmanager.app.ui.adapter.Doctors_photoAdapter
import com.telehealthmanager.app.utils.ViewUtils
import java.io.Serializable
import java.util.*

class SearchDoctorDetailActivity : BaseActivity<ActivitySearchDoctorDetailBinding>(),
    SearchNavigator {

    private val preferenceHelper = PreferenceHelper(BaseApplication.baseApplication)
    private lateinit var viewModel: SearchViewModel
    private lateinit var mDataBinding: ActivitySearchDoctorDetailBinding

    private var mAdapterPhotos: Doctors_photoAdapter? = null
    private var mAdapterFeedback: Doctor_feedbackAdapter? = null
    private var mAdapterService: AllServiceAdapter? = null
    private var mAvailabilityAdapter: AvailabilityAdapter? = null
    override fun getLayoutId(): Int = R.layout.activity_search_doctor_detail

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mDataBinding = mViewDataBinding as ActivitySearchDoctorDetailBinding
        viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)
        mDataBinding.viewmodel = viewModel
        viewModel.navigator = this
        initIntentData()
        observeResponse()
        mDataBinding.toolbar2.setNavigationOnClickListener {
            finish()
        }
    }

    private fun initIntentData() {
        val details = intent.getSerializableExtra(WebApiConstants.IntentPass.DoctorProfile) as? DoctorListResponse.specialities.DoctorProfile
        val favDoctor = intent.getSerializableExtra(WebApiConstants.IntentPass.FavDoctorProfile) as? MainResponse.Doctor
        val searchDoctor = intent.getSerializableExtra(WebApiConstants.IntentPass.SearchDoctorProfile) as? Hospital
        if (details != null) {
            viewModel.mDoctorProfile.value = details
            viewModel.id.set(viewModel.mDoctorProfile.value!!.doctor_id)
            ViewUtils.setImageViewGlide(
                this@SearchDoctorDetailActivity,
                mDataBinding.imageView25,
                BuildConfig.BASE_IMAGE_URL.plus(viewModel.mDoctorProfile.value!!.profile_pic)
            )
            if (viewModel.mDoctorProfile.value!!.hospital.size > 0) {
                viewModel.profile_pic.set(viewModel.mDoctorProfile.value!!.profile_pic)
                viewModel.favourite.set(viewModel.mDoctorProfile.value!!.hospital[0].is_favourite.toString())
                viewModel.name.set(viewModel.mDoctorProfile.value!!.hospital[0]?.first_name.plus(" ").plus(viewModel.mDoctorProfile.value!!.hospital[0]?.last_name))
                viewModel.specialities.set(viewModel.mDoctorProfile.value!!.speciality?.name ?: "")
                viewModel.degree.set(viewModel.mDoctorProfile.value!!.certification.plus(" - "))
                viewModel.branch.set(viewModel.mDoctorProfile.value!!.hospital[0]?.specialities_name)
                viewModel.percentage.set(viewModel.mDoctorProfile.value!!.hospital[0]?.feedback_percentage ?: "0".plus("%"))
                viewModel.experience.set(viewModel.mDoctorProfile.value!!.experience ?: "0")
                viewModel.fee.set(preferenceHelper.getValue(PreferenceKey.CURRENCY, "$").toString().plus(viewModel.mDoctorProfile.value!!.fees ?: "0"))
                viewModel.clinic.set(viewModel.mDoctorProfile.value!!.hospital[0]?.clinic?.name)
                viewModel.clinic_address.set(viewModel.mDoctorProfile.value!!.hospital[0]?.clinic?.address)
                if (viewModel.mDoctorProfile.value!!.hospital[0]?.clinic?.static_map != null)
                    ViewUtils.setImageViewGlide(
                        this@SearchDoctorDetailActivity,
                        mDataBinding.imageView27,
                        viewModel.mDoctorProfile.value!!.hospital[0]?.clinic?.static_map
                    )
                else
                    mDataBinding.imageView27.visibility = View.GONE
                viewModel.mfeedbacklist = viewModel.mDoctorProfile.value!!.hospital[0]?.feedback as MutableList<Hospital.Feedback>?
                viewModel.mservcielist = viewModel.mDoctorProfile.value!!.hospital[0]?.doctor_service as MutableList<Hospital.DoctorService>?
                viewModel.mTiminglist = viewModel.mDoctorProfile.value!!.hospital[0]?.timing as MutableList<Hospital.Timing>?
                viewModel.mphotoslist = viewModel.mDoctorProfile.value!!.hospital[0]?.clinic?.clinic_photo as MutableList<Hospital.Clinic.photos>?
                initAdapter()
            }
        } else if (favDoctor != null) {
            viewModel.mfavDoctorProfile.value = favDoctor
            viewModel.id.set(viewModel.mfavDoctorProfile.value!!.id)
            ViewUtils.setImageViewGlide(
                this@SearchDoctorDetailActivity,
                mDataBinding.imageView25,
                BuildConfig.BASE_IMAGE_URL.plus(viewModel.mfavDoctorProfile.value!!.hospital?.doctor_profile?.profile_pic)
            )
            viewModel.profile_pic.set(viewModel.mfavDoctorProfile.value!!.hospital?.doctor_profile?.profile_pic)
            viewModel.name.set(viewModel.mfavDoctorProfile.value!!.hospital?.first_name.plus(" ").plus(viewModel.mfavDoctorProfile.value!!.hospital?.last_name))
            viewModel.favourite.set(viewModel.mfavDoctorProfile.value!!.hospital.is_favourite.toString())
            viewModel.specialities.set(viewModel.mfavDoctorProfile.value!!.hospital?.doctor_profile?.speciality?.name ?: "")
            viewModel.degree.set(viewModel.mfavDoctorProfile.value!!.hospital?.doctor_profile?.certification.plus(" - "))
            viewModel.branch.set(viewModel.mfavDoctorProfile.value!!.hospital?.specialities_name)
            viewModel.percentage.set(viewModel.mfavDoctorProfile.value!!.hospital?.feedback_percentage ?: "0".plus("%"))
            viewModel.experience.set(viewModel.mfavDoctorProfile.value!!.hospital?.doctor_profile?.experience ?: "0")
            viewModel.fee.set(preferenceHelper.getValue(PreferenceKey.CURRENCY, "$").toString().plus(viewModel.mfavDoctorProfile.value!!.hospital?.doctor_profile?.fees ?: "0"))
            viewModel.clinic.set(viewModel.mfavDoctorProfile.value!!.hospital?.clinic?.name)
            viewModel.clinic_address.set(viewModel.mfavDoctorProfile.value!!.hospital?.clinic?.address)
            if (viewModel.mfavDoctorProfile.value!!.hospital?.clinic?.static_map != null)
                ViewUtils.setImageViewGlide(
                    this@SearchDoctorDetailActivity,
                    mDataBinding.imageView27,
                    viewModel.mfavDoctorProfile.value!!.hospital?.clinic?.static_map
                )
            else
                mDataBinding.imageView27.visibility = View.GONE
            viewModel.mfeedbacklist = viewModel.mfavDoctorProfile.value!!.hospital?.feedback as MutableList<Hospital.Feedback>?
            viewModel.mservcielist = viewModel.mfavDoctorProfile.value!!.hospital?.doctor_service as MutableList<Hospital.DoctorService>?
            viewModel.mTiminglist = viewModel.mfavDoctorProfile.value!!.hospital?.timing as MutableList<Hospital.Timing>?
            viewModel.mphotoslist = viewModel.mfavDoctorProfile.value!!.hospital?.clinic.clinic_photo as MutableList<Hospital.Clinic.photos>?
            initAdapter()
        } else if (searchDoctor != null) {
            viewModel.msearchDoctorProfile.value = searchDoctor
            viewModel.id.set(viewModel.msearchDoctorProfile.value!!.id)

            ViewUtils.setImageViewGlide(
                this@SearchDoctorDetailActivity,
                mDataBinding.imageView25,
                BuildConfig.BASE_IMAGE_URL.plus(viewModel.msearchDoctorProfile.value!!.doctor_profile?.profile_pic)
            )
            viewModel.profile_pic.set(viewModel.msearchDoctorProfile.value!!.doctor_profile?.profile_pic)
            viewModel.name.set(viewModel.msearchDoctorProfile.value!!.first_name.plus(" ").plus(viewModel.msearchDoctorProfile.value!!.last_name))
            viewModel.favourite.set(viewModel.msearchDoctorProfile.value!!.is_favourite.toString())
            viewModel.specialities.set(viewModel.msearchDoctorProfile.value!!.doctor_profile?.speciality?.name ?: "")
            viewModel.degree.set(viewModel.msearchDoctorProfile.value!!.doctor_profile.certification.plus(" - "))
            viewModel.branch.set(viewModel.msearchDoctorProfile.value!!.specialities_name)
            viewModel.percentage.set(viewModel.msearchDoctorProfile.value!!.feedback_percentage ?: "0".plus("%"))
            viewModel.experience.set(viewModel.msearchDoctorProfile.value!!.doctor_profile?.experience ?: "0")
            viewModel.fee.set(preferenceHelper.getValue(PreferenceKey.CURRENCY, "$").toString().plus(viewModel.msearchDoctorProfile.value!!.doctor_profile?.fees ?: "0"))
            viewModel.clinic.set(viewModel.msearchDoctorProfile.value!!.clinic?.name)
            viewModel.clinic_address.set(viewModel.msearchDoctorProfile.value!!.clinic?.address)
            if (viewModel.msearchDoctorProfile.value!!.clinic?.static_map != null)
                ViewUtils.setImageViewGlide(
                    this@SearchDoctorDetailActivity,
                    mDataBinding.imageView27,
                    viewModel.msearchDoctorProfile.value!!.clinic?.static_map
                )
            else
                mDataBinding.imageView27.visibility = View.GONE

            viewModel.mfeedbacklist = viewModel.msearchDoctorProfile.value!!.feedback as MutableList<Hospital.Feedback>?
            viewModel.mservcielist = viewModel.msearchDoctorProfile.value!!.doctor_service as MutableList<Hospital.DoctorService>?
            viewModel.mTiminglist = viewModel.msearchDoctorProfile.value!!.timing as MutableList<Hospital.Timing>?
            viewModel.mphotoslist = viewModel.msearchDoctorProfile.value!!.clinic?.clinic_photo as MutableList<Hospital.Clinic.photos>?
            initAdapter()

        }
        if (viewModel.mTiminglist != null) {
            for (item in viewModel.mTiminglist!!) {
                setAvailabilityVisibility(item.day)
            }
        }
    }

    fun setAvailabilityVisibility(day: String) {
        when (day.toLowerCase()) {
            "all" -> {
                mDataBinding.textViewMon.setTextColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.colorGreen
                    )
                )
                mDataBinding.textViewTue.setTextColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.colorGreen
                    )
                )
                mDataBinding.textViewWed.setTextColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.colorGreen
                    )
                )
                mDataBinding.textViewThur.setTextColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.colorGreen
                    )
                )
                mDataBinding.textViewFri.setTextColor(ContextCompat.getColor(applicationContext, R.color.colorGreen))
                mDataBinding.textViewSat.setTextColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.colorGreen
                    )
                )
                mDataBinding.textViewSun.setTextColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.colorGreen
                    )
                )
            }
            "mon" -> mDataBinding.textViewMon.setTextColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.colorGreen
                )
            )
            "tue" -> mDataBinding.textViewTue.setTextColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.colorGreen
                )
            )
            "wed" -> mDataBinding.textViewWed.setTextColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.colorGreen
                )
            )
            "thu" -> mDataBinding.textViewThur.setTextColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.colorGreen
                )
            )
            "fri" -> mDataBinding.textViewFri.setTextColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.colorGreen
                )
            )
            "sat" -> mDataBinding.textViewSat.setTextColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.colorGreen
                )
            )
            "sun" -> mDataBinding.textViewSun.setTextColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.colorGreen
                )
            )
        }
    }

    private fun initAdapter() {
        isfavourite(viewModel.favourite.get().toString())
        if (!CollectionUtils.isEmpty(viewModel.mfeedbacklist)) {
            mAdapterFeedback =
                Doctor_feedbackAdapter(viewModel.mfeedbacklist!!, this@SearchDoctorDetailActivity)
            mDataBinding.feedbackadapter = mAdapterFeedback
            mDataBinding.recyclerView7.layoutManager = LinearLayoutManager(
                this@SearchDoctorDetailActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            mAdapterFeedback!!.notifyDataSetChanged()
            mDataBinding.recyclerView7.visibility = View.VISIBLE
        } else {
            mDataBinding.recyclerView7.visibility = View.GONE
            mDataBinding.textView120.visibility = View.GONE
        }
        if (!CollectionUtils.isEmpty(viewModel.mservcielist)) {
            mAdapterService =
                AllServiceAdapter(this@SearchDoctorDetailActivity, viewModel.mservcielist!!)
            mDataBinding.serviceadapter = mAdapterService
            mDataBinding.rvServices.layoutManager =
                LinearLayoutManager(
                    this@SearchDoctorDetailActivity,
                    LinearLayoutManager.VERTICAL,
                    false
                )
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

        if (!CollectionUtils.isEmpty(viewModel.mTiminglist)) {
            mAvailabilityAdapter =
                AvailabilityAdapter(viewModel.mTiminglist!!, this@SearchDoctorDetailActivity)
            mDataBinding.availibilityAdapter = mAvailabilityAdapter
            mDataBinding.rvAvailability.layoutManager =
                LinearLayoutManager(
                    this@SearchDoctorDetailActivity,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            mAvailabilityAdapter!!.notifyDataSetChanged()
            mDataBinding.textViewNotAvailable.visibility = View.GONE
        } else {
            mDataBinding.textViewNotAvailable.visibility = View.VISIBLE
        }

        if (!CollectionUtils.isEmpty(viewModel.mphotoslist)) {
            mAdapterPhotos =
                Doctors_photoAdapter(viewModel.mphotoslist!!, this@SearchDoctorDetailActivity)
            mDataBinding.photosadapter = mAdapterPhotos
            mDataBinding.recyclerView6.layoutManager = LinearLayoutManager(
                this@SearchDoctorDetailActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            mAdapterPhotos!!.notifyDataSetChanged()

            mDataBinding.recyclerView6.visibility = View.VISIBLE
            mDataBinding.textView119.visibility = View.VISIBLE

        } else {
            mDataBinding.recyclerView6.visibility = View.GONE
            mDataBinding.textView119.visibility = View.GONE
        }

    }

    fun isfavourite(data: String?) {
        viewModel.favourite.set(data)
        when (data) {

            "true" -> {
                mDataBinding.imageView26.setImageResource(R.drawable.ic_favourite)
            }
            "false" -> {
                mDataBinding.imageView26.setImageResource(R.drawable.ic_favout_solid)

            }
            else -> {
                mDataBinding.imageView26.setImageResource(R.drawable.ic_favout_solid)
            }
        }

    }

    private fun observeResponse() {
        viewModel.getErrorObservable().observe(this, Observer<String> { message ->
            hideLoading()
            ViewUtils.showToast(this@SearchDoctorDetailActivity, message, false)

        })
        viewModel.mFavResponse.observe(this, Observer<Response> {
            hideLoading()
            ViewUtils.showToast(this@SearchDoctorDetailActivity, it.message, true)
            isfavourite(it.is_favourite.toString())
        })

    }

    override fun onfavclick() {
        loadingObservable.value = true
        val hashMap: HashMap<String, Any> = HashMap()
        hashMap[WebApiConstants.Favourite.doctor_id] = viewModel.id.get().toString()
        hashMap[WebApiConstants.Favourite.patient_id] =
            preferenceHelper.getValue(PreferenceKey.PATIENT_ID, 1).toString()
        viewModel.addfav(hashMap)
    }

    override fun onshareclick() {

    }

    override fun Onbookclick() {
        preferenceHelper.setValue(PreferenceKey.SELECTED_DOC_ID, viewModel.id.get().toString())
        preferenceHelper.setValue(PreferenceKey.SELECTED_DOC_NAME, viewModel.name.get().toString())
        preferenceHelper.setValue(
            PreferenceKey.SELECTED_DOC_Special,
            viewModel.specialities.get().toString()
        )
        preferenceHelper.setValue(
            PreferenceKey.SELECTED_DOC_IMAGE,
            viewModel.profile_pic.get().toString()
        )
        preferenceHelper.setValue(
            PreferenceKey.SELECTED_DOC_ADDRESS,
            viewModel.clinic.get().plus(",").plus(viewModel.clinic_address.get().toString())
        )
        val intent = Intent(this@SearchDoctorDetailActivity, FindDoctorBookingActivity::class.java)
        startActivity(intent);
    }

    override fun ViewallClick() {
        val intent = Intent(this, AllServiceActivity::class.java)
        intent.putExtra(
            WebApiConstants.IntentPass.SERVICE_LIST,
            viewModel.mservcielist as Serializable
        )
        startActivity(intent)
    }
}
