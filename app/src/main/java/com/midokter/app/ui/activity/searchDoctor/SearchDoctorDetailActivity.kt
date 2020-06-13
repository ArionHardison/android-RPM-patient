package com.midokter.app.ui.activity.searchDoctor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.midokter.app.BaseApplication
import com.midokter.app.BuildConfig
import com.midokter.app.R
import com.midokter.app.base.BaseActivity
import com.midokter.app.data.PreferenceHelper
import com.midokter.app.data.PreferenceKey
import com.midokter.app.data.getValue
import com.midokter.app.databinding.ActivitySearchDoctorDetailBinding
import com.midokter.app.repositary.WebApiConstants
import com.midokter.app.repositary.model.DoctorListResponse
import com.midokter.app.repositary.model.Hospital
import com.midokter.app.repositary.model.MainResponse
import com.midokter.app.repositary.model.Response
<<<<<<< HEAD
import com.midokter.app.ui.activity.findDoctors.FindDoctorBookingActivity
=======
import com.midokter.app.ui.activity.allservice.AllServiceActivity
>>>>>>> c1986475ef4ee487d8183365709549fbfc2e3c54
import com.midokter.app.ui.activity.patientDetail.PatientDetailsActivity
import com.midokter.app.ui.adapter.*
import com.midokter.app.utils.ViewUtils
import kotlinx.android.synthetic.main.activity_search_doctor_detail.*
import java.io.Serializable
import java.util.HashMap

class SearchDoctorDetailActivity : BaseActivity<ActivitySearchDoctorDetailBinding>(),
    SearchNavigator {


    private val preferenceHelper = PreferenceHelper(BaseApplication.baseApplication)
    private lateinit var viewModel: SearchViewModel
    private lateinit var mDataBinding: ActivitySearchDoctorDetailBinding

    private var mAdapterPhotos: Doctors_photoAdapter? = null
    private var mAdapterFeedback: Doctor_feedbackAdapter? = null
    private var mAdapterService: AllServiceAdapter? = null
    private var mAvailibilityAdapter: AvailabilityAdapter? = null
    override fun getLayoutId(): Int = R.layout.activity_search_doctor_detail

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mDataBinding = mViewDataBinding as ActivitySearchDoctorDetailBinding
        viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)
        mDataBinding.viewmodel = viewModel
        viewModel.navigator = this
        initIntentData()
<<<<<<< HEAD
        mDataBinding.toolbar2.setNavigationOnClickListener {
            finish()
=======
        observeResponse()
        button28.setOnClickListener {
            val intent = Intent(applicationContext, SearchDoctorScheduleActivity::class.java)
            startActivity(intent);
>>>>>>> c1986475ef4ee487d8183365709549fbfc2e3c54
        }
        mViewDataBinding.toolbarBackImg.setOnClickListener{
            finish()
        }
    }

    private fun initIntentData() {
        val details =
            intent.getSerializableExtra(WebApiConstants.IntentPass.DoctorProfile) as? DoctorListResponse.specialities.DoctorProfile
        val favDoctor =
            intent.getSerializableExtra(WebApiConstants.IntentPass.FavDoctorProfile) as? MainResponse.Doctor
        if (details != null) {
            viewModel.mDoctorProfile.value = details
            viewModel.id.set(viewModel.mDoctorProfile.value!!.id)
            ViewUtils.setImageViewGlide(
                this@SearchDoctorDetailActivity,
                mDataBinding.imageView25,
                BuildConfig.BASE_IMAGE_URL.plus(viewModel.mDoctorProfile.value!!.profile_pic)
            )
            viewModel.name.set(
                viewModel.mDoctorProfile.value!!.hospital[0]?.first_name.plus(" ").plus(
                    viewModel.mDoctorProfile.value!!.hospital[0]?.last_name
                )
            )
            viewModel.specialities.set(viewModel.mDoctorProfile.value!!.speciality?.name)
            viewModel.degree.set(viewModel.mDoctorProfile.value!!.certification)
            viewModel.branch.set(viewModel.mDoctorProfile.value!!.hospital[0]?.specialities_name)
            viewModel.percentage.set(
                viewModel.mDoctorProfile.value!!.hospital[0]?.feedback_percentage.plus(
                    "%"
                )
            )
            viewModel.experience.set(viewModel.mDoctorProfile.value!!.experience)
            viewModel.fee.set(viewModel.mDoctorProfile.value!!.fees)
            if (viewModel.mDoctorProfile.value!!.hospital[0]?.timing.size > 0) {
                viewModel.mor_time.set(
                    viewModel.mDoctorProfile.value!!.hospital[0]?.timing[0].start_time.plus(
                        " - "
                    ).plus(viewModel.mDoctorProfile.value!!.hospital[0]?.timing[0].end_time)
                )
                viewModel.eve_time.set(
                    viewModel.mDoctorProfile.value!!.hospital[0]?.timing[1].start_time.plus(
                        " - "
                    ).plus(viewModel.mDoctorProfile.value!!.hospital[0]?.timing[1].end_time)
                )
            }
            viewModel.clinic.set(viewModel.mDoctorProfile.value!!.hospital[0]?.clinic?.name)
            viewModel.clinic_address.set(viewModel.mDoctorProfile.value!!.hospital[0]?.clinic?.address)
            if ( viewModel.mDoctorProfile.value!!.hospital[0]?.clinic?.static_map!=null)
            ViewUtils.setImageViewGlide(this@SearchDoctorDetailActivity, mDataBinding.imageView27, viewModel.mDoctorProfile.value!!.hospital[0]?.clinic?.static_map)
            else
                mDataBinding.imageView27.visibility = View.GONE
            viewModel.mfeedbacklist =
                viewModel.mDoctorProfile.value!!.hospital[0]?.feedback as MutableList<Hospital.Feedback>?
            viewModel.mservcielist =
                viewModel.mDoctorProfile.value!!.hospital[0]?.doctor_service as MutableList<Hospital.DoctorService>?
            viewModel.mTiminglist =
                viewModel.mDoctorProfile.value!!.hospital[0]?.timing as MutableList<Hospital.Timing>?
            viewModel.mphotoslist =
                viewModel.mDoctorProfile.value!!.hospital[0]?.clinic.clinic_photo as MutableList<Hospital.Clinic.photos>?
            initAdapter()

        } else if (favDoctor != null) {
            viewModel.mfavDoctorProfile.value = favDoctor
            viewModel.id.set(viewModel.mfavDoctorProfile.value!!.id)
             ViewUtils.setImageViewGlide(this@SearchDoctorDetailActivity,  mDataBinding.imageView25, BuildConfig.BASE_IMAGE_URL.plus(viewModel.mfavDoctorProfile.value!!.hospital?.doctor_profile?.profile_pic))
            viewModel.name.set(
                viewModel.mfavDoctorProfile.value!!.hospital?.first_name.plus(" ").plus(
                    viewModel.mfavDoctorProfile.value!!.hospital?.last_name
                )
            )
            viewModel.specialities.set(viewModel.mfavDoctorProfile.value!!.hospital?.doctor_profile?.speciality?.name)
            viewModel.degree.set(viewModel.mfavDoctorProfile.value!!.hospital?.doctor_profile?.certification)
            viewModel.branch.set(viewModel.mfavDoctorProfile.value!!.hospital?.specialities_name)
            viewModel.percentage.set(
                viewModel.mfavDoctorProfile.value!!.hospital?.feedback_percentage.plus(
                    "%"
                )
            )
            viewModel.experience.set(viewModel.mfavDoctorProfile.value!!.hospital?.doctor_profile?.experience)
            viewModel.fee.set(viewModel.mfavDoctorProfile.value!!.hospital?.doctor_profile?.fees)
            if (viewModel.mfavDoctorProfile.value!!.hospital?.timing.size > 0) {
                viewModel.mor_time.set(
                    viewModel.mfavDoctorProfile.value!!.hospital?.timing[0].start_time.plus(
                        " - "
                    ).plus(viewModel.mfavDoctorProfile.value!!.hospital?.timing[0].end_time)
                )
                viewModel.eve_time.set(
                    viewModel.mfavDoctorProfile.value!!.hospital?.timing[1].start_time.plus(
                        " - "
                    ).plus(viewModel.mfavDoctorProfile.value!!.hospital?.timing[1].end_time)
                )
            }
            viewModel.clinic.set(viewModel.mfavDoctorProfile.value!!.hospital?.clinic?.name)
            viewModel.clinic_address.set(viewModel.mfavDoctorProfile.value!!.hospital?.clinic?.address)
             if ( viewModel.mfavDoctorProfile.value!!.hospital?.clinic?.static_map!=null)
            ViewUtils.setImageViewGlide(this@SearchDoctorDetailActivity, mDataBinding.imageView27, viewModel.mfavDoctorProfile.value!!.hospital?.clinic?.static_map)
                else
                mDataBinding.imageView27.visibility = View.GONE

            viewModel.mfeedbacklist =
                viewModel.mfavDoctorProfile.value!!.hospital?.feedback as MutableList<Hospital.Feedback>?
            viewModel.mservcielist =
                viewModel.mfavDoctorProfile.value!!.hospital?.doctor_service as MutableList<Hospital.DoctorService>?
            viewModel.mTiminglist =
                viewModel.mfavDoctorProfile.value!!.hospital?.timing as MutableList<Hospital.Timing>?
            viewModel.mphotoslist =
                viewModel.mfavDoctorProfile.value!!.hospital?.clinic.clinic_photo as MutableList<Hospital.Clinic.photos>?
            initAdapter()
        }
        if (viewModel.mTiminglist != null) {
            for (item in viewModel.mTiminglist!!){
                setAvailabilityVisibility(item.day)
            }
        }
    }

    fun setAvailabilityVisibility(day:String){
        when (day.toLowerCase()) {
            "all" -> {
                mDataBinding.textViewMon.setTextColor(ContextCompat.getColor(applicationContext, R.color.colorGreen))
                mDataBinding.textViewTue.setTextColor(ContextCompat.getColor(applicationContext, R.color.colorGreen))
                mDataBinding.textViewWed.setTextColor(ContextCompat.getColor(applicationContext, R.color.colorGreen))
                mDataBinding.textViewThur.setTextColor(ContextCompat.getColor(applicationContext, R.color.colorGreen))
                mDataBinding.textViewFri.setTextColor(ContextCompat.getColor(applicationContext, R.color.colorGreen))
                mDataBinding.textViewSat.setTextColor(ContextCompat.getColor(applicationContext, R.color.colorGreen))
                mDataBinding.textViewSun.setTextColor(ContextCompat.getColor(applicationContext, R.color.colorGreen))
            }
            "mon" -> mDataBinding.textViewMon.setTextColor(ContextCompat.getColor(applicationContext, R.color.colorGreen))
            "tue" -> mDataBinding.textViewTue.setTextColor(ContextCompat.getColor(applicationContext, R.color.colorGreen))
            "wed" -> mDataBinding.textViewWed.setTextColor(ContextCompat.getColor(applicationContext, R.color.colorGreen))
            "thu" -> mDataBinding.textViewThur.setTextColor(ContextCompat.getColor(applicationContext, R.color.colorGreen))
            "fri" -> mDataBinding.textViewFri.setTextColor(ContextCompat.getColor(applicationContext, R.color.colorGreen))
            "sat" -> mDataBinding.textViewSat.setTextColor(ContextCompat.getColor(applicationContext, R.color.colorGreen))
            "sun" -> mDataBinding.textViewSun.setTextColor(ContextCompat.getColor(applicationContext, R.color.colorGreen))
        }
    }

    private fun initAdapter() {
        if (viewModel.mfeedbacklist != null) {
            mAdapterFeedback =
                Doctor_feedbackAdapter(viewModel.mfeedbacklist!!, this@SearchDoctorDetailActivity)
            mDataBinding.feedbackadapter = mAdapterFeedback
            mDataBinding.recyclerView7.layoutManager = LinearLayoutManager(
                this@SearchDoctorDetailActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            mAdapterFeedback!!.notifyDataSetChanged()
            mDataBinding.recyclerView7.visibility= View.VISIBLE
        } else{
            mDataBinding.recyclerView7.visibility= View.GONE

        }
        if (viewModel.mservcielist != null) {
            mAdapterService =
                AllServiceAdapter(this@SearchDoctorDetailActivity,viewModel.mservcielist!! )
            mDataBinding.serviceadapter = mAdapterService
            mDataBinding.rvServices.layoutManager =
                LinearLayoutManager(this@SearchDoctorDetailActivity, LinearLayoutManager.VERTICAL, false)
            mAdapterService!!.notifyDataSetChanged()

            if(viewModel.mservcielist!!.size>4){
                mDataBinding.serviceViewAll.visibility= View.VISIBLE
            }
            else{
                mDataBinding.serviceViewAll.visibility= View.GONE
            }
        }

        if(viewModel.mTiminglist!=null){
            mAvailibilityAdapter =
                AvailabilityAdapter(viewModel.mTiminglist!!,this@SearchDoctorDetailActivity )
            mDataBinding.availibilityAdapter = mAvailibilityAdapter
            mDataBinding.rvAvailability.layoutManager =
                LinearLayoutManager(this@SearchDoctorDetailActivity, LinearLayoutManager.VERTICAL, false)
            mAvailibilityAdapter!!.notifyDataSetChanged()
        }

        if (viewModel.mphotoslist != null) {
            mAdapterPhotos = Doctors_photoAdapter(viewModel.mphotoslist!!, this@SearchDoctorDetailActivity)
            mDataBinding.photosadapter = mAdapterPhotos
            mDataBinding.recyclerView6.layoutManager = LinearLayoutManager(
                this@SearchDoctorDetailActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            mAdapterPhotos!!.notifyDataSetChanged()

            if(viewModel.mphotoslist!!.size>0){
                mDataBinding.recyclerView6.visibility= View.VISIBLE
                mDataBinding.textView119.visibility= View.VISIBLE
            }
            else{
                mDataBinding.recyclerView6.visibility= View.GONE
                mDataBinding.textView119.visibility= View.VISIBLE
            }
        } else{
            mDataBinding.recyclerView6.visibility= View.GONE
            mDataBinding.textView119.visibility= View.VISIBLE
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
        val intent = Intent(this@SearchDoctorDetailActivity, FindDoctorBookingActivity::class.java)
        startActivity(intent);
    }

    override fun ViewallClick() {
        val intent = Intent(this, AllServiceActivity::class.java)
        intent.putExtra(WebApiConstants.IntentPass.SERVICE_LIST,viewModel.mservcielist as Serializable)
        startActivity(intent)
    }
}
