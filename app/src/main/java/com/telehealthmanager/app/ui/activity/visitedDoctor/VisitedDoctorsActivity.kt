package com.telehealthmanager.app.ui.activity.visitedDoctor

import android.content.Intent
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.telehealthmanager.app.R
import com.telehealthmanager.app.base.BaseActivity
import com.telehealthmanager.app.databinding.ActivityVisitedDoctorsBinding
import com.telehealthmanager.app.repositary.WebApiConstants
import com.telehealthmanager.app.repositary.model.Appointment
import com.telehealthmanager.app.ui.adapter.VisitedDoctorsListAdapter
import com.telehealthmanager.app.utils.CustomBackClick
import com.telehealthmanager.app.utils.ViewUtils
import java.io.Serializable

class VisitedDoctorsActivity : BaseActivity<ActivityVisitedDoctorsBinding>(), VisitedDoctorsNavigator, CustomBackClick, VisitedDoctorsListAdapter.IAppointmentListener {
    override fun onlike() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onunlike() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSubmit() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun videoCallClick() {

    }

    val visitedDoctors: ArrayList<String> = ArrayList()
    private lateinit var viewModel: VisitedDoctorsViewModel
    private lateinit var mDataBinding: ActivityVisitedDoctorsBinding
    private var mAdapter: VisitedDoctorsListAdapter? = null

    override fun getLayoutId(): Int = R.layout.activity_visited_doctors

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mDataBinding = mViewDataBinding as ActivityVisitedDoctorsBinding
        viewModel = ViewModelProvider(this).get(VisitedDoctorsViewModel::class.java)
        mDataBinding.viewmodel = viewModel
        viewModel.navigator = this
        initApiCal()
        observeResponse()
        viewModel.setOnClickListener(this@VisitedDoctorsActivity)
        viewModel.toolBarTile.value = resources.getString(R.string.visted_doctor)
    }

    override fun clickBackPress() {
        finish()
    }

    private fun initApiCal() {
        loadingObservable.value = true
        viewModel.getListVisitedDoc()
    }

    private fun observeResponse() {
        viewModel.mVisitedAppointmentDoc.observe(this, Observer {
            loadingObservable.value = false
            if (it != null) {
                if (it.visited_doctors != null) {
                    if (!it.visited_doctors.appointments.isNullOrEmpty()) {
                        mDataBinding.tvNotFound.visibility = View.GONE
                        mAdapter = VisitedDoctorsListAdapter(it.visited_doctors.appointments, this@VisitedDoctorsActivity, this)
                        mDataBinding.rvVisitedDoctors.layoutManager = LinearLayoutManager(applicationContext)
                        mDataBinding.rvVisitedDoctors.addItemDecoration(DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL))
                        mDataBinding.adapter = mAdapter
                        mAdapter!!.notifyDataSetChanged()
                        loadingObservable.value = false
                    } else {
                        mDataBinding.tvNotFound.visibility = View.VISIBLE
                    }
                }
            }
        })

        viewModel.getErrorObservable().observe(this, Observer<String> { message ->
            loadingObservable.value = false
            ViewUtils.showToast(this@VisitedDoctorsActivity, message, false)
        })
    }

    override fun onItemClick(selectedItem: Appointment) {
        val intent = Intent(this@VisitedDoctorsActivity, VisitedDoctorsDetailActivity::class.java)
        intent.putExtra(WebApiConstants.IntentPass.iscancel, false)
        intent.putExtra(WebApiConstants.IntentPass.VisitedDoctor, selectedItem as Serializable)
        startActivity(intent);
    }
}
