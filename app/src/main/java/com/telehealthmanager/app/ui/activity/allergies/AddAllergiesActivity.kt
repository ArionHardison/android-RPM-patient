package com.telehealthmanager.app.ui.activity.allergies

import android.content.Intent
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.telehealthmanager.app.R
import com.telehealthmanager.app.base.BaseActivity
import com.telehealthmanager.app.databinding.ActivityAddAllergiesBinding
import com.telehealthmanager.app.utils.CustomBackClick

class AddAllergiesActivity : BaseActivity<ActivityAddAllergiesBinding>(), AllergiesNavigator, CustomBackClick {

    private lateinit var viewModel: AllergiesViewModel
    private lateinit var mDataBinding: ActivityAddAllergiesBinding
    val allerigiesList: ArrayList<String> = ArrayList()

    override fun getLayoutId() = R.layout.activity_add_allergies

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mDataBinding = mViewDataBinding as ActivityAddAllergiesBinding
        viewModel = ViewModelProviders.of(this).get(AllergiesViewModel::class.java)
        mDataBinding.viewmodel = viewModel
        viewModel.navigator = this
        viewModel.setOnClickListener(this@AddAllergiesActivity)
        addedAllergies()
    }

    override fun clickBackPress() {
        setResult(RESULT_CANCELED)
        finish()
    }

    override fun openAddAllergies() {

    }

    override fun noAllergies() {
        val intent = Intent()
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun addedAllergies() {
        allerigiesList.add("Soy")
        allerigiesList.add("Nuts")
        allerigiesList.add("Egg")
        allerigiesList.add("Fish")
        allerigiesList.add("Sea Food")
        allerigiesList.add("Mushroom")
        allerigiesList.add("Other")
        mDataBinding.rvAllergies.layoutManager = LinearLayoutManager(applicationContext)
        mDataBinding.rvAllergies.addItemDecoration(
            DividerItemDecoration(
                applicationContext,
                DividerItemDecoration.VERTICAL
            )
        )
        mDataBinding.rvAllergies.adapter = applicationContext?.let { AllergiesAdapter(allerigiesList, it) }
    }
}