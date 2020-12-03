package com.telehealthmanager.app.ui.activity.main.ui.favourite_doctor


import androidx.fragment.app.Fragment
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager

import com.telehealthmanager.app.R
import com.telehealthmanager.app.base.BaseFragment
import com.telehealthmanager.app.databinding.FragmentFavouriteDoctorBinding
import com.telehealthmanager.app.repositary.model.MainResponse
import com.telehealthmanager.app.ui.adapter.FavDoctorListAdapter
import com.telehealthmanager.app.utils.ViewUtils
import java.util.HashMap

/**
 * A simple [Fragment] subclass.
 */
class FavouriteDoctorFragment : BaseFragment<FragmentFavouriteDoctorBinding>(), FavouriteDoctorNavigator {

    private lateinit var viewModel: FavouriteDoctorViewModel
    private lateinit var mDataBinding: FragmentFavouriteDoctorBinding
    private var mAdapter: FavDoctorListAdapter? = null

    override fun getLayoutId(): Int = R.layout.fragment_favourite_doctor

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {
        mDataBinding = mViewDataBinding as FragmentFavouriteDoctorBinding
        viewModel = ViewModelProviders.of(this).get(FavouriteDoctorViewModel::class.java)
        mDataBinding.viewmodel = viewModel
        viewModel.navigator = this
        //addFavDoctor()
        initApiCal()
        initAdapter()
        observeResponse()
    }

    private fun initApiCal() {
        showLoading()
        val hashMap: HashMap<String, Any> = HashMap()
        viewModel.gethome(hashMap)
    }

    private fun observeResponse() {

        viewModel.mDoctorResponse.observe(this, Observer<MainResponse> {
            viewModel.mDoctorsList = it.favourite_Doctors as MutableList<MainResponse.Doctor>?
            if (viewModel.mDoctorsList!!.size > 0) {
                mDataBinding.tvNotFound.visibility = View.GONE
            } else {
                mDataBinding.tvNotFound.visibility = View.VISIBLE
            }
            mAdapter = FavDoctorListAdapter(viewModel.mDoctorsList!!, activity!!)
            mDataBinding.adapter = mAdapter
            mAdapter!!.notifyDataSetChanged()
            hideLoading()
        })

        viewModel.getErrorObservable().observe(this, Observer<String> { message ->
            hideLoading()
            ViewUtils.showToast(context!!, message, false)
        })
    }

    private fun initAdapter() {
        mAdapter = FavDoctorListAdapter(viewModel.mDoctorsList!!, activity!!)
        mDataBinding.adapter = mAdapter
        mDataBinding.rvFavDoctor.addItemDecoration(DividerItemDecoration(activity!!, DividerItemDecoration.VERTICAL))
        mDataBinding.rvFavDoctor.layoutManager = LinearLayoutManager(activity!!)
        mAdapter!!.notifyDataSetChanged()
    }

}
