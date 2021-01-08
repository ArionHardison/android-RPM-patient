package com.telehealthmanager.app.ui.activity.findDoctors

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.util.CollectionUtils
import com.telehealthmanager.app.BaseApplication
import com.telehealthmanager.app.R
import com.telehealthmanager.app.base.BaseActivity
import com.telehealthmanager.app.data.PreferenceHelper
import com.telehealthmanager.app.data.PreferenceKey
import com.telehealthmanager.app.data.setValue
import com.telehealthmanager.app.databinding.ActivityFindDoctorsListBinding
import com.telehealthmanager.app.repositary.WebApiConstants
import com.telehealthmanager.app.repositary.model.DoctorListResponse
import com.telehealthmanager.app.ui.activity.searchDoctor.SearchDoctorDetailActivity
import com.telehealthmanager.app.ui.adapter.FindDoctorsAdapter
import com.telehealthmanager.app.ui.adapter.FindFilterDoctorListAdapter
import com.telehealthmanager.app.ui.adapter.PassengersLoadStateAdapter
import com.telehealthmanager.app.utils.CustomBackClick
import com.telehealthmanager.app.utils.ViewCallBack
import com.telehealthmanager.app.utils.ViewUtils
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.Serializable


class FindDoctorsListActivity : BaseActivity<ActivityFindDoctorsListBinding>(),
    ViewCallBack.IDoctorListener, CustomBackClick {

    private val TAG = "FindDoctorsListActivity"
    private lateinit var viewModel: FindDoctorsViewModel
    private lateinit var mDataBinding: ActivityFindDoctorsListBinding
    private lateinit var filterdialog: AlertDialog.Builder
    private val preferenceHelper = PreferenceHelper(BaseApplication.baseApplication)
    private lateinit var mAdapter: FindFilterDoctorListAdapter
    private val mFindDoctorsAdapter: FindDoctorsAdapter = FindDoctorsAdapter(this)

    override fun getLayoutId(): Int = R.layout.activity_find_doctors_list

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mDataBinding = mViewDataBinding as ActivityFindDoctorsListBinding
        viewModel = ViewModelProvider(this).get(FindDoctorsViewModel::class.java)
        mDataBinding.viewmodel = viewModel
        viewModel.mCategoryId.set(intent.getIntExtra(WebApiConstants.IntentPass.ID, 1))
        mAdapter = FindFilterDoctorListAdapter(this@FindDoctorsListActivity, this)
        mDataBinding.rvFilterView.adapter = mAdapter
        mDataBinding.rvFilterView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        mDataBinding.rvFilterView.itemAnimator = DefaultItemAnimator()
        viewModel.setOnClickListener(this@FindDoctorsListActivity)
        mDataBinding.imageView17.setOnClickListener {
            showFilterDialog(this@FindDoctorsListActivity)
        }

        setupScrollListener()
        initApiCal()
        observeResponse()
        filterdialog = AlertDialog.Builder(this)

        mDataBinding.editText9.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.queryLiveData.postValue(s.toString())
                if (s!!.isNotEmpty()) {
                    val hashMap: HashMap<String, Any> = HashMap()
                    hashMap["search"] = viewModel.queryLiveData.value.toString()
                    viewModel.getDoctorByCategorys(viewModel.mCategoryId.get()!!.toInt(), hashMap)
                }
            }
        })

    }

    private fun setupScrollListener() {
        mFindDoctorsAdapter.registerAdapterDataObserver(adapterObserver)
        mDataBinding.rvFinddoctorsList.layoutManager = LinearLayoutManager(this)
        mDataBinding.rvFinddoctorsList.setHasFixedSize(true)

        mDataBinding.rvFinddoctorsList.adapter = mFindDoctorsAdapter.withLoadStateHeaderAndFooter(
            header = PassengersLoadStateAdapter { mFindDoctorsAdapter.retry() },
            footer = PassengersLoadStateAdapter { mFindDoctorsAdapter.retry() }
        )

        lifecycleScope.launch {
            viewModel.doctorsList.collectLatest { pagedData ->
                mFindDoctorsAdapter.submitData(pagedData)
            }
        }
    }

    private val adapterObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            val count = mFindDoctorsAdapter.itemCount
            if (itemCount == 0 && count == 0) {
                loadingObservable.value = false
                mDataBinding.rvFinddoctorsList.visibility = View.GONE
                mDataBinding.tvNotFound.visibility = View.VISIBLE
            } else {
                loadingObservable.value = false
                mDataBinding.rvFinddoctorsList.visibility = View.VISIBLE
                mDataBinding.tvNotFound.visibility = View.GONE
            }
        }
    }


    override fun clickBackPress() {
        finish()
    }

    private fun initApiCal() {
        loadingObservable.value = true
    }

    private fun observeResponse() {
        viewModel.mDoctorResponse.observe(this, {
            loadingObservable.value = false
            mDataBinding.rvFinddoctorsList.visibility = View.GONE
            mDataBinding.rvFilterView.visibility = View.VISIBLE
            viewModel.mDoctorList = it.Specialities.doctor_profile as MutableList<DoctorListResponse.specialities.DoctorProfile>?
            if (it.Specialities.doctor_profile.toMutableList().size > 0) {
                mAdapter.addItems(it.Specialities.doctor_profile.toMutableList())
                mDataBinding.tvNotFound.visibility = View.GONE
            } else {
                mDataBinding.rvFilterView.visibility = View.GONE
                mDataBinding.tvNotFound.visibility = View.VISIBLE
            }
        })

        viewModel.getErrorObservable().observe(this, { message ->
            loadingObservable.value = false
            ViewUtils.showToast(this@FindDoctorsListActivity, message, false)
        })
    }

    private fun showFilterDialog(context: Context) {
        val builder = android.app.AlertDialog.Builder(context)
        val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.dialog_filter, null)
        builder.setView(view)
        val tvApply = view.findViewById<TextView>(R.id.button16)
        val tvCancel = view.findViewById<TextView>(R.id.button15)
        val ad: android.app.AlertDialog = builder.create()
        tvApply.setOnClickListener {
            val hashMap: HashMap<String, Any> = HashMap()
            when {
                view.findViewById<RadioButton>(R.id.radioAvailabilityAnyDay).isChecked -> {
                    hashMap["availability_type"] = "today"
                }
                view.findViewById<RadioButton>(R.id.radioAvailabilityNext3).isChecked -> {
                    hashMap["availability_type"] = "3w"
                }
                view.findViewById<RadioButton>(R.id.radioAvailabilityWeek).isChecked -> {
                    hashMap["availability_type"] = "week"
                }
            }

            when {
                view.findViewById<RadioButton>(R.id.radioMale).isChecked -> {
                    hashMap["gender"] = "MALE"
                }
                view.findViewById<RadioButton>(R.id.radioFeMale).isChecked -> {
                    hashMap["gender"] = "FEMALE"
                }
            }

            when {
                view.findViewById<RadioButton>(R.id.radioOne).isChecked -> {
                    hashMap["fees"] = "1-10"
                }
                view.findViewById<RadioButton>(R.id.radioTwo).isChecked -> {
                    hashMap["fees"] = "20-30"
                }
                view.findViewById<RadioButton>(R.id.radioThree).isChecked -> {
                    hashMap["fees"] = "30"
                }
            }

            if (hashMap.isNotEmpty()) {
                loadingObservable.value = true
                viewModel.getDoctorFilterCategories(hashMap)
            } else {
                mDataBinding.rvFilterView.visibility = View.GONE
                val count = mFindDoctorsAdapter.itemCount
                if (count == 0) {
                    mDataBinding.rvFinddoctorsList.visibility = View.GONE
                    mDataBinding.tvNotFound.visibility = View.VISIBLE
                } else {
                    mDataBinding.rvFinddoctorsList.visibility = View.VISIBLE
                    mDataBinding.tvNotFound.visibility = View.GONE
                }
            }
            ad.dismiss()
        }
        tvCancel.setOnClickListener {
            ad.dismiss()
        }
        ad.show()
    }

    override fun onBookClick(selectedItem: DoctorListResponse.specialities.DoctorProfile) {
        preferenceHelper.setValue(PreferenceKey.SELECTED_DOC_ID, selectedItem.doctor_id.toString())
        if (!CollectionUtils.isEmpty(selectedItem.hospital)) {
            preferenceHelper.setValue(PreferenceKey.SELECTED_DOC_NAME, selectedItem.hospital[0].first_name.plus(" ").plus(selectedItem.hospital[0].last_name))
            preferenceHelper.setValue(
                PreferenceKey.SELECTED_DOC_ADDRESS, selectedItem.hospital[0].clinic?.name ?: "No Clinic"
                    .plus(" , ").plus(selectedItem.hospital[0].clinic?.address ?: "No address")
            )
        }
        preferenceHelper.setValue(PreferenceKey.SELECTED_DOC_Special, selectedItem.speciality?.name ?: "")
        preferenceHelper.setValue(PreferenceKey.SELECTED_DOC_SPECIALITY_ID, selectedItem.speciality?.id.toString())
        preferenceHelper.setValue(PreferenceKey.SELECTED_DOC_IMAGE, selectedItem.profile_pic ?: "")
        val intent = Intent(this@FindDoctorsListActivity, FindDoctorBookingActivity::class.java)
        intent.putExtra(WebApiConstants.IntentPass.DoctorProfile, selectedItem as Serializable)
        startActivity(intent);
    }

    override fun onItemClick(selectedItem: DoctorListResponse.specialities.DoctorProfile) {
        val intent = Intent(this@FindDoctorsListActivity, SearchDoctorDetailActivity::class.java)
        intent.putExtra(WebApiConstants.IntentPass.DoctorProfile, selectedItem as Serializable)
        startActivity(intent);
    }

    override fun onCallClick(phone: String) {
        val phoneNumber: String = phone
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$phoneNumber")
        startActivity(intent)
    }

}
