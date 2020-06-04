package com.midokter.app.ui.activity.visitedDoctor

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.midokter.app.R
import com.midokter.app.base.BaseActivity
import com.midokter.app.databinding.ActivityVisitedDoctorsBinding
import com.midokter.app.repositary.model.MainResponse
import com.midokter.app.ui.adapter.SearchDoctorsListAdapter
import com.midokter.app.ui.adapter.VisitedDoctorsListAdapter
import com.midokter.app.utils.ViewUtils
import kotlinx.android.synthetic.main.activity_visited_doctors.*
import java.util.HashMap

class VisitedDoctorsActivity : BaseActivity<ActivityVisitedDoctorsBinding>(),VisitedDoctorsNavigator {

    val visitedDoctors: ArrayList<String> = ArrayList()
    private lateinit var viewModel: VisitedDoctorsViewModel
    private lateinit var mDataBinding: ActivityVisitedDoctorsBinding
    private var mAdapter: VisitedDoctorsListAdapter? = null


    override fun getLayoutId(): Int = R.layout.activity_visited_doctors

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        //addVisitedDoctors()
        mDataBinding = mViewDataBinding as ActivityVisitedDoctorsBinding
        viewModel = ViewModelProviders.of(this).get(VisitedDoctorsViewModel::class.java)
        mDataBinding.viewmodel = viewModel
        viewModel.navigator = this
        initApiCal()
        initAdapter()
        observeResponse()

    }
    private fun initApiCal() {
        loadingObservable.value = true
        val hashMap: HashMap<String, Any> = HashMap()
        viewModel.gethome(hashMap)

    }
    private fun observeResponse() {

        viewModel.mDoctorResponse.observe(this, Observer<MainResponse> {


            viewModel.mDoctorslist = it.visited_doctors as MutableList<MainResponse.VisitedDoctor>?
            if (viewModel.mDoctorslist!!.size > 0) {
                mDataBinding.tvNotFound.visibility = View.GONE
            } else {
                mDataBinding.tvNotFound.visibility = View.VISIBLE
            }
            mAdapter = VisitedDoctorsListAdapter(viewModel.mDoctorslist!!,this@VisitedDoctorsActivity)
            mDataBinding.adapter = mAdapter
            mAdapter!!.notifyDataSetChanged()
            loadingObservable.value = false




        })
        viewModel.getErrorObservable().observe(this, Observer<String> { message ->
            loadingObservable.value = false
            ViewUtils.showToast(this@VisitedDoctorsActivity, message, false)
        })
    }

    private fun initAdapter() {
        mAdapter = VisitedDoctorsListAdapter( viewModel.mDoctorslist!!,this@VisitedDoctorsActivity)
        mDataBinding.adapter = mAdapter
        mDataBinding.rvVisitedDoctors.addItemDecoration(
            DividerItemDecoration(
                applicationContext,
                DividerItemDecoration.VERTICAL
            )
        )
        mDataBinding.rvVisitedDoctors.layoutManager =LinearLayoutManager(applicationContext)
        mAdapter!!.notifyDataSetChanged()


    }
    private fun addVisitedDoctors() {
        visitedDoctors.add("Dr.Bretto")
        visitedDoctors.add("Dr.John")
        visitedDoctors.add("Dr.Jenifer")
        visitedDoctors.add("Dr.Virundha")
        rv_visited_doctors.layoutManager = LinearLayoutManager(applicationContext)
        rv_visited_doctors.addItemDecoration(
            DividerItemDecoration(applicationContext,
                DividerItemDecoration.VERTICAL)
        )
        //rv_visited_doctors.adapter = applicationContext?.let { VisitedDoctorsListAdapter(visitedDoctors, it) }
    }
}
