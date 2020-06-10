package com.midokter.app.ui.activity.searchDoctor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.midokter.app.BuildConfig
import com.midokter.app.R
import com.midokter.app.base.BaseActivity
import com.midokter.app.databinding.ActivitySearchDoctorDetailBinding
import com.midokter.app.repositary.WebApiConstants
import com.midokter.app.repositary.model.DoctorListResponse
import com.midokter.app.ui.activity.patientDetail.PatientDetailsActivity
import com.midokter.app.ui.adapter.Doctor_feedbackAdapter
import com.midokter.app.ui.adapter.Doctors_photoAdapter
import com.midokter.app.ui.adapter.Doctors_serviceAdapter
import com.midokter.app.utils.ViewUtils
import kotlinx.android.synthetic.main.activity_search_doctor_detail.*

class SearchDoctorDetailActivity : BaseActivity<ActivitySearchDoctorDetailBinding>(),SearchNavigator {
    override fun ViewallClick() {

    }


    private lateinit var viewModel: SearchViewModel
    private lateinit var mDataBinding: ActivitySearchDoctorDetailBinding

    private var mAdapterPhotos: Doctors_photoAdapter? = null
    private var mAdapterFeedback: Doctor_feedbackAdapter? = null
    private var mAdapterService: Doctors_serviceAdapter? = null
    override fun getLayoutId(): Int = R.layout.activity_search_doctor_detail

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mDataBinding = mViewDataBinding as ActivitySearchDoctorDetailBinding
        viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)
        mDataBinding.viewmodel = viewModel
        viewModel.navigator = this
        initIntentData()
        button28.setOnClickListener {
            val intent = Intent(applicationContext, SearchDoctorScheduleActivity::class.java)
            startActivity(intent);
        }
    }

    private fun initIntentData() {
        val details = intent.getSerializableExtra(WebApiConstants.IntentPass.DoctorProfile) as? DoctorListResponse.specialities.DoctorProfile
        if (details!=null) {
            viewModel.mDoctorProfile.value = details
            ViewUtils.setImageViewGlide(this@SearchDoctorDetailActivity,  mDataBinding.imageView25, BuildConfig.BASE_IMAGE_URL.plus(viewModel.mDoctorProfile.value!!.profile_pic))
            viewModel.name.set(
                viewModel.mDoctorProfile.value!!.hospital[0]?.first_name.plus(" ").plus(
                    viewModel.mDoctorProfile.value!!.hospital[0]?.last_name
                )
            )
            viewModel.specialities.set(viewModel.mDoctorProfile.value!!.hospital[0]?.specialities_name)
            viewModel.degree.set(viewModel.mDoctorProfile.value!!.hospital[0]?.specialities_name)
            viewModel.branch.set(viewModel.mDoctorProfile.value!!.hospital[0]?.specialities_name)
            viewModel.percentage.set(
                viewModel.mDoctorProfile.value!!.hospital[0]?.feedback_percentage.plus(
                    "%"
                )
            )
            viewModel.fee.set(viewModel.mDoctorProfile.value!!.fees)
            if ( viewModel.mDoctorProfile.value!!.hospital[0]?.timing.size>0) {
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
            viewModel.mfeedbacklist = viewModel.mDoctorProfile.value!!.hospital[0]?.feedback as MutableList<DoctorListResponse.specialities.DoctorProfile.Hospital.Feedback>?
            viewModel.mservcielist = viewModel.mDoctorProfile.value!!.hospital[0]?.doctor_service as MutableList<DoctorListResponse.specialities.DoctorProfile.Hospital.DoctorService>?
initAdapter()

        }
        }
    private fun initAdapter() {

        mAdapterFeedback = Doctor_feedbackAdapter( viewModel.mfeedbacklist!!,this@SearchDoctorDetailActivity)
        mDataBinding.feedbackadapter = mAdapterFeedback
        mDataBinding.recyclerView7.layoutManager = LinearLayoutManager(this@SearchDoctorDetailActivity,LinearLayoutManager.HORIZONTAL ,false)
        mAdapterFeedback!!.notifyDataSetChanged()

        mAdapterService = Doctors_serviceAdapter( viewModel.mservcielist!!,this@SearchDoctorDetailActivity)
        mDataBinding.serviceadapter = mAdapterService
        mDataBinding.rvServices.layoutManager = LinearLayoutManager(this@SearchDoctorDetailActivity,LinearLayoutManager.HORIZONTAL ,false)
        mAdapterService!!.notifyDataSetChanged()


    }
    override fun onfavclick() {

    }

    override fun onshareclick() {
    }

    override fun Onbookclick() {

    }
}
