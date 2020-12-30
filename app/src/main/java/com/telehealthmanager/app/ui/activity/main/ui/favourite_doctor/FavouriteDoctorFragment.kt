package com.telehealthmanager.app.ui.activity.main.ui.favourite_doctor


import android.content.Intent
import androidx.fragment.app.Fragment
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager

import com.telehealthmanager.app.R
import com.telehealthmanager.app.base.BaseFragment
import com.telehealthmanager.app.data.Constant
import com.telehealthmanager.app.databinding.FragmentFavouriteDoctorBinding
import com.telehealthmanager.app.repositary.WebApiConstants
import com.telehealthmanager.app.repositary.model.MainResponse
import com.telehealthmanager.app.ui.activity.searchDoctor.SearchDoctorDetailActivity
import com.telehealthmanager.app.ui.adapter.FavDoctorListAdapter
import com.telehealthmanager.app.ui.adapter.IFavDoctorsClick
import com.telehealthmanager.app.utils.ViewUtils
import java.io.Serializable
import java.util.HashMap

/**
 * A simple [Fragment] subclass.
 */
class FavouriteDoctorFragment : BaseFragment<FragmentFavouriteDoctorBinding>(), FavouriteDoctorNavigator, IFavDoctorsClick {

    private lateinit var viewModel: FavouriteDoctorViewModel
    private lateinit var mDataBinding: FragmentFavouriteDoctorBinding
    private var mAdapter: FavDoctorListAdapter? = null

    override fun getLayoutId(): Int = R.layout.fragment_favourite_doctor

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {
        mDataBinding = mViewDataBinding as FragmentFavouriteDoctorBinding
        viewModel = ViewModelProvider(this).get(FavouriteDoctorViewModel::class.java)
        mDataBinding.viewmodel = viewModel
        viewModel.navigator = this
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
        viewModel.mDoctorResponse.observe(this, {
            viewModel.mDoctorsList = it.favourite_Doctors as MutableList<MainResponse.Doctor>?
            if (!it.favourite_Doctors.isNullOrEmpty()) {
                mDataBinding.tvNotFound.visibility = View.GONE
                mAdapter!!.addItem(it.favourite_Doctors.toMutableList())
            } else {
                mDataBinding.tvNotFound.visibility = View.VISIBLE
            }
            hideLoading()
        })

        viewModel.getErrorObservable().observe(this, { message ->
            hideLoading()
            ViewUtils.showToast(requireActivity(), message, false)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != AppCompatActivity.RESULT_CANCELED) {
            if (requestCode == Constant.DOCTORS_ACTIVITY) {
                val isFavourite: String = data?.getStringExtra("is_favourite") as String
                if (isFavourite == "false") {
                    mAdapter!!.removePosition()
                    if (mAdapter!!.itemCount == 0) {
                        mDataBinding.tvNotFound.visibility = View.VISIBLE
                    }
                }
            }
        }
    }


    private fun initAdapter() {
        mDataBinding.rvFavDoctor.addItemDecoration(DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL))
        mAdapter = FavDoctorListAdapter(this@FavouriteDoctorFragment)
        mDataBinding.rvFavDoctor.adapter = mAdapter
    }

    override fun onItemClicked(item: MainResponse.Doctor) {
        val intent = Intent(requireContext(), SearchDoctorDetailActivity::class.java)
        intent.putExtra(WebApiConstants.IntentPass.FavDoctorProfile, item as Serializable)
        startActivityForResult(intent, Constant.DOCTORS_ACTIVITY);
    }

}
