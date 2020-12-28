package com.telehealthmanager.app.ui.activity.searchDoctor

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.telehealthmanager.app.R
import com.telehealthmanager.app.base.BaseActivity
import com.telehealthmanager.app.data.Constant
import com.telehealthmanager.app.databinding.ActivitySearchDoctorBinding
import com.telehealthmanager.app.repositary.WebApiConstants
import com.telehealthmanager.app.repositary.model.Hospital
import com.telehealthmanager.app.ui.adapter.DoctorsAdapter
import com.telehealthmanager.app.ui.adapter.IDoctorsClick
import com.telehealthmanager.app.ui.adapter.PassengersLoadStateAdapter
import com.telehealthmanager.app.utils.CustomBackClick
import kotlinx.android.synthetic.main.content_search_doctor.*
import kotlinx.android.synthetic.main.toolbar_base.*
import kotlinx.android.synthetic.main.twillioi_video_activity.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.Serializable
import java.util.*
import kotlin.math.abs


class SearchDoctorActivity : BaseActivity<ActivitySearchDoctorBinding>(), CustomBackClick, IDoctorsClick {

    private val TAG = "SearchDoctorActivity"
    private lateinit var viewModel: SearchViewModel
    private lateinit var mDataBinding: ActivitySearchDoctorBinding
    private val doctorsAdapter: DoctorsAdapter = DoctorsAdapter(this)
    private val searchAdapter: DoctorsAdapter = DoctorsAdapter(this)

    override fun getLayoutId(): Int = R.layout.activity_search_doctor

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mDataBinding = mViewDataBinding as ActivitySearchDoctorBinding
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        mDataBinding.viewmodel = viewModel
        mDataBinding.textView88.text = getString(R.string.search_result, "")
        mDataBinding.textView89.text = getString(R.string.result_found, "0")
        initPagination()
        viewModel.setOnClickListener(this@SearchDoctorActivity)
        viewModel.toolBarTile.value = resources.getString(R.string.search_doctor)
        mDataBinding.toolBar.appBar.addOnOffsetChangedListener(OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (abs(verticalOffset) == appBarLayout.totalScrollRange) {
                mDataBinding.toolBar.scrollToolbarBar.visibility = View.GONE
                mDataBinding.toolBar.toolbarVisible.visibility = View.VISIBLE
            } else if (verticalOffset == 0) {
                mDataBinding.toolBar.scrollToolbarBar.visibility = View.VISIBLE
                mDataBinding.toolBar.toolbarVisible.visibility = View.GONE
            }
        })

    }


    override fun clickBackPress() {
        finish()
    }

    private fun initPagination() {

        mDataBinding.rvDoctors.layoutManager = LinearLayoutManager(this)
        mDataBinding.rvDoctors.setHasFixedSize(true)

        mDataBinding.rvSerachDoctors.layoutManager = LinearLayoutManager(this)
        mDataBinding.rvSerachDoctors.setHasFixedSize(true)
        mDataBinding.editText13.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.isNotEmpty()) {
                    mDataBinding.rvDoctors.visibility = View.GONE
                    mDataBinding.rvSerachDoctors.visibility = View.VISIBLE
                    viewModel.searchDoctorsCount = 0
                    viewModel.searchRepo(s.toString())
                } else {
                    mDataBinding.rvSerachDoctors.visibility = View.GONE
                    mDataBinding.rvDoctors.visibility = View.VISIBLE
                    viewModel.searchDoctorsCount = 0
                    viewModel.searchRepo(s.toString())
                }
            }
        })

        mDataBinding.rvDoctors.adapter = doctorsAdapter.withLoadStateHeaderAndFooter(
            header = PassengersLoadStateAdapter { doctorsAdapter.retry() },
            footer = PassengersLoadStateAdapter { doctorsAdapter.retry() }
        )

        mDataBinding.rvSerachDoctors.adapter = searchAdapter.withLoadStateHeaderAndFooter(
            header = PassengersLoadStateAdapter { searchAdapter.retry() },
            footer = PassengersLoadStateAdapter { searchAdapter.retry() }
        )

        viewModel.getData.observe(this) {
            searchAdapter.submitData(lifecycle, it)
        }

        lifecycleScope.launch {
            viewModel.doctorsList.collectLatest { pagedData ->
                doctorsAdapter.submitData(pagedData)
            }
        }
        viewModel.counnt.observe(this, {
            mDataBinding.textView89.text = getString(R.string.result_found, it.toString())
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_CANCELED) {
            if (requestCode == Constant.DOCTORS_ACTIVITY) {
                val isFavourite: String = data?.getStringExtra("is_favourite") as String
                if (viewModel.queryLiveData.value.toString() == "") {
                    doctorsAdapter.changeFavState(isFavourite)
                } else {
                    searchAdapter.changeFavState(isFavourite)
                }
            }
        }
    }

    override fun onItemClicked(item: Hospital) {
        val intent = Intent(this@SearchDoctorActivity, SearchDoctorDetailActivity::class.java)
        intent.putExtra(WebApiConstants.IntentPass.SearchDoctorProfile, item as Serializable)
        startActivityForResult(intent, Constant.DOCTORS_ACTIVITY);
    }
}
