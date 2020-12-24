package com.telehealthmanager.app.ui.activity.searchDoctor

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.core.widget.NestedScrollView
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.telehealthmanager.app.R
import com.telehealthmanager.app.base.BaseActivity
import com.telehealthmanager.app.databinding.ActivitySearchDoctorBinding
import com.telehealthmanager.app.repositary.WebApiConstants
import com.telehealthmanager.app.ui.adapter.SearchDoctorsListAdapter
import com.telehealthmanager.app.utils.CustomBackClick
import com.telehealthmanager.app.utils.ViewUtils
import kotlinx.android.synthetic.main.content_search_doctor.*
import kotlinx.android.synthetic.main.toolbar_base.*
import kotlinx.android.synthetic.main.twillioi_video_activity.*
import java.util.*
import kotlin.math.abs


class SearchDoctorActivity : BaseActivity<ActivitySearchDoctorBinding>(), CustomBackClick {

    private val TAG = "SearchDoctorActivity"
    private lateinit var viewModel: SearchViewModel
    private lateinit var mDataBinding: ActivitySearchDoctorBinding
    private lateinit var mAdapter: SearchDoctorsListAdapter
    private var loading = true
    private var previousTotalItemCount = 0
    private var visibleThreshold = 5

    override fun getLayoutId(): Int = R.layout.activity_search_doctor

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mDataBinding = mViewDataBinding as ActivitySearchDoctorBinding
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        mDataBinding.viewmodel = viewModel
        mAdapter = SearchDoctorsListAdapter(this)
        mDataBinding.rvSerachDoctors.adapter = mAdapter
        setupScrollListener()
        mDataBinding.textView88.text = getString(R.string.search_result, "")
        mDataBinding.textView89.text = getString(R.string.result_found, "0")
        mDataBinding.editText13.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if (s!!.isNotEmpty()) {
                    mDataBinding.textView88.text = resources.getString(R.string.search_result, s.toString())
                    viewModel.searchText.set(true)
                    mAdapter.onSearchRequest()
                    val hashMap: HashMap<String, Any> = HashMap()
                    hashMap[WebApiConstants.Home.SEARCH] = s.toString()
                    viewModel.gethome(hashMap)
                } else {
                    mDataBinding.textView88.text = resources.getString(R.string.search_result, " ")
                    viewModel.searchText.set(false)
                    mAdapter.onSearchCleared()
                }
            }
        })

        initApiCal()
        observeResponse()
        viewModel.setOnClickListener(this@SearchDoctorActivity)
        viewModel.toolBarTile.value = resources.getString(R.string.search_doctor)

    }

    private fun setupScrollListener() {
        val layoutManager = mDataBinding.rvSerachDoctors.layoutManager as LinearLayoutManager
        mDataBinding.scrollable.setOnScrollChangeListener { v: NestedScrollView, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            if (viewModel.searchText.get() == true) {
                return@setOnScrollChangeListener
            } else if (mAdapter.itemCount == 0) {
                return@setOnScrollChangeListener
            }
            if (v.getChildAt(v.childCount - 1) != null) {
                if (scrollY >= v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight && scrollY > oldScrollY) {
                    val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                    val totalItemCount: Int = layoutManager.itemCount
                    if (loading && totalItemCount > previousTotalItemCount) {
                        loading = false
                        previousTotalItemCount = totalItemCount
                    }
                    if (!loading && lastVisibleItemPosition + visibleThreshold > totalItemCount && mAdapter.itemCount > visibleThreshold) { // This condition will useful when recyclerview has less than visibleThreshold items
                        mDataBinding.scrollable.fullScroll(View.FOCUS_DOWN)
                        val hashMap: HashMap<String, Any> = HashMap()
                        hashMap["page"] = mAdapter.getItem(mAdapter.itemCount - 1).id
                        loading = true
                        viewModel.loadingProgress.value = true
                        viewModel.gethome(hashMap)
                    }
                }
            }
        }

    }

    override fun clickBackPress() {
        finish()
    }

    private fun initApiCal() {
        viewModel.search.set("")
        val hashMap: HashMap<String, Any> = HashMap()
        hashMap["page"] = "0"
        viewModel.gethome(hashMap)
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

    private fun observeResponse() {

        viewModel.mDoctorResponse.observe(this, Observer {
            if (it.search_doctors.toMutableList().size > 0) {
                mDataBinding.tvNotFound.visibility = View.GONE
                mAdapter.addItems(it.search_doctors.toMutableList())
                mDataBinding.textView89.text = resources.getString(R.string.result_found, mAdapter.itemCount.toString())
            } else {
                if (mAdapter.itemCount > 0) {
                    mDataBinding.tvNotFound.visibility = View.GONE
                } else {
                    mDataBinding.tvNotFound.visibility = View.VISIBLE
                }
            }
            viewModel.listsize.postValue(mAdapter.itemCount.toString())
            loading = true
            loadingObservable.value = false
            viewModel.loadingProgress.value = false
        })

        viewModel.getErrorObservable().observe(this, Observer<String> { message ->
            loadingObservable.value = false
            viewModel.loadingProgress.value = false
            ViewUtils.showToast(this@SearchDoctorActivity, message, false)
        })

        viewModel.loadingProgress.observe(this, Observer {
            if (!it) {
                mDataBinding.loadingTxt.visibility = View.GONE
            } else {
                mDataBinding.loadingTxt.visibility = View.VISIBLE
            }
        })
    }
}
