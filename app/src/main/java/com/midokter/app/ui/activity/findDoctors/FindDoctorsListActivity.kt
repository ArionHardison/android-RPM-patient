package com.midokter.app.ui.activity.findDoctors

import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.common.util.CollectionUtils
import com.midokter.app.BaseApplication
import com.midokter.app.R
import com.midokter.app.base.BaseActivity
import com.midokter.app.data.PreferenceHelper
import com.midokter.app.data.PreferenceKey
import com.midokter.app.data.setValue
import com.midokter.app.databinding.ActivityFindDoctorsListBinding
import com.midokter.app.repositary.WebApiConstants
import com.midokter.app.repositary.model.DoctorListResponse
import com.midokter.app.ui.activity.searchDoctor.SearchDoctorDetailActivity
import com.midokter.app.ui.adapter.FindDoctorListAdapter
import com.midokter.app.ui.adapter.IDoctorListener
import com.midokter.app.utils.ViewUtils
import kotlinx.android.synthetic.main.activity_find_doctors_list.*
import java.io.Serializable

class FindDoctorsListActivity : BaseActivity<ActivityFindDoctorsListBinding>(),FindDoctorsNavigator,IDoctorListener {

    val doctorList: ArrayList<String> = ArrayList()

    private lateinit var viewModel: FindDoctorsViewModel
    private lateinit var mDataBinding: ActivityFindDoctorsListBinding
    private var mAdapter: FindDoctorListAdapter? = null
    private lateinit var filterdialog: AlertDialog.Builder

    private val preferenceHelper = PreferenceHelper(BaseApplication.baseApplication)

    override fun getLayoutId(): Int = R.layout.activity_find_doctors_list

    override fun initView(mViewDataBinding: ViewDataBinding?) {

        mDataBinding = mViewDataBinding as ActivityFindDoctorsListBinding
        viewModel = ViewModelProviders.of(this).get(FindDoctorsViewModel::class.java)
        mDataBinding.viewmodel = viewModel
        viewModel.navigator = this

        initApiCal( intent.getIntExtra(WebApiConstants.IntentPass.ID,1))
        initAdapter()
        observeResponse()
        filterdialog = AlertDialog.Builder(this)
        //categoriesList()

        mDataBinding.imageView17.setOnClickListener {
            showFilterDialog(this@FindDoctorsListActivity)
        }

    }


    private fun initApiCal(id: Int) {
        loadingObservable.value = true
        viewModel.getDoctorByCategorys(id)

    }
    private fun observeResponse() {

        viewModel.mDoctorResponse.observe(this, Observer<DoctorListResponse> {


            viewModel.mDoctorslist = it.Specialities.doctor_profile as MutableList<DoctorListResponse.specialities.DoctorProfile>?
            if (viewModel.mDoctorslist!!.size > 0) {
                mDataBinding.tvNotFound.visibility = View.GONE
            } else {
                mDataBinding.tvNotFound.visibility = View.VISIBLE
            }
            mAdapter = FindDoctorListAdapter(viewModel.mDoctorslist!!,this@FindDoctorsListActivity,this)
            mDataBinding.adapter = mAdapter
            mAdapter!!.notifyDataSetChanged()
            loadingObservable.value = false




        })
        viewModel.getErrorObservable().observe(this, Observer<String> { message ->
            loadingObservable.value = false
            ViewUtils.showToast(this@FindDoctorsListActivity, message, false)
        })
    }

    fun showFilterDialog(context: Context) {
        val builder = android.app.AlertDialog.Builder(context)
        val inflater: LayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.dialog_filter, null)
        builder.setView(view)
        val tvYes = view.findViewById<TextView>(R.id.button16)
        val tvNo = view.findViewById<TextView>(R.id.button15)
        val radiogroup_avaliablity = view.findViewById<RadioGroup>(R.id.radiogroup_avaliablity)
        val radiogroup_gender = view.findViewById<RadioGroup>(R.id.radiogroup_gender)
        val radiogroup_consultion = view.findViewById<RadioGroup>(R.id.radiogroup_consultion)
        radiogroup_avaliablity.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener { group, checkedId ->
               // val radio: RadioButton = findViewById(checkedId)

            })
        radiogroup_gender.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener { group, checkedId ->
                //val radio: RadioButton = findViewById(checkedId)

            })
        radiogroup_consultion.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener { group, checkedId ->
               // val radio: RadioButton = findViewById(checkedId)

            })
        val ad: android.app.AlertDialog = builder.create()
        tvNo.setOnClickListener {

            ad.dismiss()
        }
        tvYes.setOnClickListener {

            ad.dismiss()
        }
        ad.show()
    }


    private fun initAdapter() {
        mAdapter = FindDoctorListAdapter( viewModel.mDoctorslist!!,this@FindDoctorsListActivity,this)
        mDataBinding.adapter = mAdapter
         mDataBinding.rvFinddoctorsList.addItemDecoration(
             DividerItemDecoration(
                 this,
                 DividerItemDecoration.VERTICAL
             )
         )
        mDataBinding.rvFinddoctorsList.layoutManager =LinearLayoutManager(applicationContext)
        mAdapter!!.notifyDataSetChanged()

        mDataBinding.editText9.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {


                if (s!!.length>0)
                    mDataBinding.adapter!!.filter.filter(s)
                else{
                   initAdapter()
                }

            }

        })
    }

    override fun onbookclick(selectedItem: DoctorListResponse.specialities.DoctorProfile) {
        preferenceHelper.setValue(PreferenceKey.SELECTED_DOC_ID, selectedItem.doctor_id.toString())
        if(!CollectionUtils.isEmpty(selectedItem.hospital)){
            preferenceHelper.setValue(PreferenceKey.SELECTED_DOC_NAME, selectedItem.hospital[0].first_name.plus(" ").plus(selectedItem.hospital[0].last_name))
            preferenceHelper.setValue(PreferenceKey.SELECTED_DOC_ADDRESS, selectedItem.hospital[0].clinic?.name.plus(" , ").plus(selectedItem.hospital[0].clinic?.address))
        }
        preferenceHelper.setValue(PreferenceKey.SELECTED_DOC_Special, selectedItem.speciality?.name?:"")
        preferenceHelper.setValue(PreferenceKey.SELECTED_DOC_IMAGE, selectedItem.profile_pic?:"")
         val intent = Intent(this@FindDoctorsListActivity, FindDoctorBookingActivity::class.java)
    startActivity(intent);
}

    override fun onitemclick(selectedItem: DoctorListResponse.specialities.DoctorProfile) {
        val intent = Intent(this@FindDoctorsListActivity, SearchDoctorDetailActivity::class.java)
        intent.putExtra(WebApiConstants.IntentPass.DoctorProfile,selectedItem as Serializable)
        startActivity(intent);

    }

    override fun oncallclick(phone: String) {

    }

    private fun categoriesList() {
        rv_finddoctors_list.layoutManager = LinearLayoutManager(applicationContext)
        //rv_finddoctors_list.adapter = applicationContext?.let { FindDoctorListAdapter(doctorList, it,F) }
        doctorList.add("Dr.Alvin")
        doctorList.add("Dr.Madison")
        doctorList.add("Dr.Joe")
        doctorList.add("Dr.Mellisa")
        doctorList.add("Dr.Glen")
    }
}
