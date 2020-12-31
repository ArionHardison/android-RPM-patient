package com.telehealthmanager.app.ui.activity.findDoctors

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.GridLayoutManager
import com.telehealthmanager.app.BaseApplication
import com.telehealthmanager.app.R
import com.telehealthmanager.app.base.BaseActivity
import com.telehealthmanager.app.data.PreferenceHelper
import com.telehealthmanager.app.databinding.ActivityFindDoctorCategoriesBinding
import com.telehealthmanager.app.repositary.WebApiConstants
import com.telehealthmanager.app.repositary.model.CategoryResponse
import com.telehealthmanager.app.ui.activity.searchDoctor.SearchDoctorActivity
import com.telehealthmanager.app.ui.adapter.CategoriesListAdapter
import com.telehealthmanager.app.utils.CustomBackClick
import com.telehealthmanager.app.utils.ViewUtils

class FindDoctorCategoriesActivity : BaseActivity<ActivityFindDoctorCategoriesBinding>(), FindDoctorsNavigator, CustomBackClick {

    private lateinit var viewModel: FindDoctorsViewModel
    private lateinit var mDataBinding: ActivityFindDoctorCategoriesBinding
    private var mCategoriesAdapter: CategoriesListAdapter? = null

    override fun getLayoutId(): Int = R.layout.activity_find_doctor_categories

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mDataBinding = mViewDataBinding as ActivityFindDoctorCategoriesBinding
        viewModel = ViewModelProvider(this).get(FindDoctorsViewModel::class.java)
        mDataBinding.viewmodel = viewModel
        viewModel.navigator = this
        initApiCal()
        initAdapter()
        observeResponse()
        viewModel.setOnClickListener(this@FindDoctorCategoriesActivity)
        viewModel.toolBarTile.value = getString(R.string.find_doctors)
    }

    override fun clickBackPress() {
        finish()
    }

    private fun initApiCal() {
        loadingObservable.value = true
        viewModel.getCategorys()
    }

    private fun observeResponse() {
        viewModel.mCategoryResponse.observe(this, {
            loadingObservable.value = false
            viewModel.mCategoryList = it.category as MutableList<CategoryResponse.Category>?
            if (viewModel.mCategoryList!!.size > 0) {
                mDataBinding.tvNotFound.visibility = View.GONE
            } else {
                mDataBinding.tvNotFound.visibility = View.VISIBLE
            }
            mCategoriesAdapter = CategoriesListAdapter(viewModel.mCategoryList!!, this)
            mDataBinding.adapter = mCategoriesAdapter
            mCategoriesAdapter!!.notifyDataSetChanged()
        })

        viewModel.getErrorObservable().observe(this, { message ->
            loadingObservable.value = false
            ViewUtils.showToast(this@FindDoctorCategoriesActivity, message, false)
        })
    }

    private fun initAdapter() {
        mCategoriesAdapter = CategoriesListAdapter(viewModel.mCategoryList!!, this)
        mDataBinding.adapter = mCategoriesAdapter
        mDataBinding.rvCategories.layoutManager = GridLayoutManager(applicationContext, 2)
        mCategoriesAdapter!!.notifyDataSetChanged()

        mDataBinding.editText8.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.isNotEmpty())
                    mDataBinding.adapter!!.filter.filter(s)
                else {
                    mCategoriesAdapter = CategoriesListAdapter(viewModel.mCategoryList!!, this@FindDoctorCategoriesActivity)
                    mDataBinding.adapter = mCategoriesAdapter
                    mDataBinding.adapter!!.notifyDataSetChanged()
                }
            }
        })
    }

    override fun openSearchDoctors() {
        val intent = Intent(applicationContext, SearchDoctorActivity::class.java)
        startActivity(intent);
    }
}
