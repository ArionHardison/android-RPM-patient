package com.telehealthmanager.app.ui.activity.allergies

import android.content.Intent
import android.content.res.ColorStateList
import android.text.Editable
import android.text.TextWatcher
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.telehealthmanager.app.R
import com.telehealthmanager.app.base.BaseActivity
import com.telehealthmanager.app.databinding.ActivityAddAllergiesBinding
import com.telehealthmanager.app.repositary.model.ProfileResponse
import com.telehealthmanager.app.utils.CustomBackClick
import com.telehealthmanager.app.utils.ViewUtils


class AddAllergiesActivity : BaseActivity<ActivityAddAllergiesBinding>(), AllergiesNavigator, CustomBackClick, AllergiesAdapter.IAllergiesListener {

    private lateinit var viewModel: AllergiesViewModel
    private lateinit var mDataBinding: ActivityAddAllergiesBinding
    private val allergiesList: ArrayList<ProfileResponse.Allergies> = ArrayList()
    private var mAdapter: AllergiesAdapter? = null

    override fun getLayoutId() = R.layout.activity_add_allergies

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mDataBinding = mViewDataBinding as ActivityAddAllergiesBinding
        viewModel = ViewModelProviders.of(this).get(AllergiesViewModel::class.java)
        mDataBinding.viewmodel = viewModel
        viewModel.navigator = this
        viewModel.setOnClickListener(this@AddAllergiesActivity)
        initAdapter()
        initApi()
        observableResponse()
        mDataBinding.doneBtn.setOnClickListener {
            if (mAdapter != null) {
                val stringBuilder: StringBuilder = java.lang.StringBuilder()
                for (index in allergiesList.indices) {
                    val tagName: String = allergiesList[index].name!!
                    if (index == 0) {
                        stringBuilder.append(tagName)
                    } else {
                        stringBuilder.append(", ").append(tagName)
                    }
                }
                if (stringBuilder.toString() != "") {
                    val intent = Intent()
                    intent.putExtra("select_allergies", stringBuilder.toString())
                    setResult(RESULT_OK, intent)
                    finish()
                } else {
                    ViewUtils.showToast(this@AddAllergiesActivity, getString(R.string.select_allergies), false)
                }
            } else {
                ViewUtils.showToast(this@AddAllergiesActivity, getString(R.string.select_allergies), false)
            }
        }
    }

    private fun initApi() {
        loadingObservable.value = true
        viewModel.getProfile()
    }

    private fun observableResponse() {
        viewModel.mProfileResponse.observe(this, Observer {
            loadingObservable.value = false
            if (!it.allergies.isNullOrEmpty()) {
                viewModel.mAllergiesList = it.allergies as MutableList<ProfileResponse.Allergies>?
                mAdapter = AllergiesAdapter(viewModel.mAllergiesList!!, applicationContext, this)
                mDataBinding.adapter = mAdapter
                mAdapter!!.notifyDataSetChanged()
            }
        })

        viewModel.getErrorObservable().observe(this, Observer<String> { message ->
            loadingObservable.value = false
            ViewUtils.showToast(this@AddAllergiesActivity, message, false)
        })
    }

    private fun initAdapter() {
        mAdapter = AllergiesAdapter(viewModel.mAllergiesList!!, applicationContext, this)
        mDataBinding.adapter = mAdapter
        mDataBinding.rvAllergies.addItemDecoration(DividerItemDecoration(applicationContext!!, DividerItemDecoration.VERTICAL))
        mDataBinding.rvAllergies.layoutManager = LinearLayoutManager(applicationContext!!)
        mAdapter!!.notifyDataSetChanged()
        mDataBinding.editText13.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.isNotEmpty() && mAdapter != null) {
                   // mDataBinding.adapter!!.filter.filter(s)
                    val filteredList: MutableList<ProfileResponse.Allergies> = mAdapter!!.items
                    val filteredItems: MutableList<ProfileResponse.Allergies> = arrayListOf()
                    for (index in filteredList.indices) {
                        val row=filteredList[index]
                        if (row.name!!.toLowerCase().contains(s.toString())) {
                            filteredItems.add(row)
                        }
                    }
                    runOnUiThread {
                        mDataBinding.adapter!!.setList(filteredItems)
                    }
                } else {
                    runOnUiThread {
                        mDataBinding.adapter!!.setList(viewModel.mAllergiesList!!)
                    }
                }
            }
        })

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

    override fun onIemClick(allergies: ProfileResponse.Allergies) {
        allergiesList.add(allergies)
        mAdapter!!.items.remove(allergies)
        mAdapter!!.notifyDataSetChanged()
        setSelectedAllergies()
    }

    private fun setSelectedAllergies() {
        val chipGroup: ChipGroup = findViewById(R.id.tag_group)
        chipGroup.removeAllViews()
        for (index in allergiesList.indices) {
            val tagName: String = allergiesList[index].name!!
            val chip: Chip = layoutInflater.inflate(R.layout.single_chip_layout, chipGroup, false) as Chip
            chip.chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorWhite))
            chip.chipStrokeColor = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorButton))
            chip.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorButton)))
            chip.chipCornerRadius = 10f
            chip.chipStrokeWidth = 1f
            chip.text = tagName
            chip.tag = index
            chip.setOnCloseIconClickListener {
                val chipTag: String = it.tag.toString()
                val chipText: String = chip.text.toString()
                val allergiesAdd = ProfileResponse.Allergies(chipTag.toInt(), chipText)
                chipGroup.removeView(chip)
                allergiesList.removeAt(chipTag.toInt())
                mAdapter!!.items.add(allergiesAdd)
                mDataBinding.adapter = mAdapter
                mAdapter!!.notifyDataSetChanged()
                viewModel.mAllergiesList= mAdapter!!.items
                setSelectedAllergies()
            }
            chipGroup.addView(chip)
        }
    }
}