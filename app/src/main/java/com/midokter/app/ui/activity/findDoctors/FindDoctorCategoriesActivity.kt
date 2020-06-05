package com.midokter.app.ui.activity.findDoctors

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.midokter.app.BaseApplication
import com.midokter.app.R
import com.midokter.app.base.BaseActivity
import com.midokter.app.data.PreferenceHelper
import com.midokter.app.databinding.ActivityFindDoctorCategoriesBinding
import com.midokter.app.databinding.ActivityProfileBinding
import com.midokter.app.repositary.model.CategoryResponse
import com.midokter.app.repositary.model.ProfileResponse
import com.midokter.app.ui.activity.profile.ProfileViewModel
import com.midokter.app.ui.adapter.CategoriesListAdapter
import com.midokter.app.utils.ViewUtils
import kotlinx.android.synthetic.main.activity_find_doctor_categories.*
import kotlinx.android.synthetic.main.activity_visited_doctors.*

class FindDoctorCategoriesActivity : BaseActivity<ActivityFindDoctorCategoriesBinding>(),FindDoctorsNavigator {

    val categories: ArrayList<String> = ArrayList()

    private lateinit var appBarConfiguration: AppBarConfiguration
    private val preferenceHelper = PreferenceHelper(BaseApplication.baseApplication)
    private lateinit var viewModel: FindDoctorsViewModel
    private lateinit var mDataBinding: ActivityFindDoctorCategoriesBinding
    private var mCategoriesAdapter: CategoriesListAdapter? = null

    override fun getLayoutId(): Int = R.layout.activity_find_doctor_categories

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mDataBinding = mViewDataBinding as ActivityFindDoctorCategoriesBinding
        viewModel = ViewModelProviders.of(this).get(FindDoctorsViewModel::class.java)
        mDataBinding.viewmodel = viewModel
        viewModel.navigator = this

        initApiCal()
        initAdapter()
        observeResponse()

        mDataBinding.toolbar8.setNavigationOnClickListener {
            finish()
        }

    }
    private fun initApiCal() {
        loadingObservable.value = true
        viewModel.getCategorys()

    }
    private fun observeResponse() {

        viewModel.mCategoryResponse.observe(this, Observer<CategoryResponse> {


            viewModel.mCategoryslist = it.category as MutableList<CategoryResponse.Category>?
            if (viewModel.mCategoryslist!!.size > 0) {
                mDataBinding.tvNotFound.visibility = View.GONE
            } else {
                mDataBinding.tvNotFound.visibility = View.VISIBLE
            }
            mCategoriesAdapter = CategoriesListAdapter( viewModel.mCategoryslist!!,this)
            mDataBinding.adapter = mCategoriesAdapter
            mCategoriesAdapter!!.notifyDataSetChanged()
            loadingObservable.value = false




        })
        viewModel.getErrorObservable().observe(this, Observer<String> { message ->
            loadingObservable.value = false
            ViewUtils.showToast(this@FindDoctorCategoriesActivity, message, false)
        })
    }

    private fun initAdapter() {
        mCategoriesAdapter = CategoriesListAdapter( viewModel.mCategoryslist!!,this)
        mDataBinding.adapter = mCategoriesAdapter
       /* mDataBinding.rvCategories.addItemDecoration(
            DividerItemDecoration(
                applicationContext,
                DividerItemDecoration.VERTICAL
            )
        )*/
        mDataBinding.rvCategories.layoutManager =GridLayoutManager(applicationContext,2)
        mCategoriesAdapter!!.notifyDataSetChanged()

        mDataBinding.editText8.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.length>0)
                    mDataBinding.adapter!!.filter.filter(s)
                else{
                    mCategoriesAdapter = CategoriesListAdapter( viewModel.mCategoryslist!!, this@FindDoctorCategoriesActivity)
                    mDataBinding.adapter = mCategoriesAdapter
                    mDataBinding.adapter!!.notifyDataSetChanged()
                }

            }

        })
    }
    private fun categoriesList() {
        categories.add("Womens Health")
        categories.add("Skin & Hair")
        categories.add("Child Specialist")
        categories.add("General Physician")
        categories.add("Dental care")
        categories.add("Ear Nose Throat")
        categories.add("Ayurveda")
        categories.add("Bone & Join")
        categories.add("Sex Specialist")
        categories.add("Eye Specialist")
        categories.add("Digestive Issue")
        categories.add("Mental Wellness")
        categories.add("Physiotheraphy")
        categories.add("Diabetes Management")
        categories.add("Lungs & Breathing")
        categories.add("Heart")
    }
}
