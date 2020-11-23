package com.telehealthmanager.app.ui.activity.chat

import android.content.Intent
import android.graphics.Paint
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.telehealthmanager.app.BaseApplication
import com.telehealthmanager.app.BuildConfig
import com.telehealthmanager.app.R
import com.telehealthmanager.app.base.BaseActivity
import com.telehealthmanager.app.data.PreferenceHelper
import com.telehealthmanager.app.data.PreferenceKey
import com.telehealthmanager.app.data.getValue
import com.telehealthmanager.app.databinding.ActivityChatSummaryBinding
import com.telehealthmanager.app.repositary.model.CategoryResponse
import com.telehealthmanager.app.repositary.model.ChatPromoSuccess
import com.telehealthmanager.app.repositary.model.DoctorListResponse
import com.telehealthmanager.app.ui.activity.findDoctors.FindDoctorsViewModel
import com.telehealthmanager.app.ui.activity.payment.PaymentActivity
import com.telehealthmanager.app.ui.adapter.DoctorImageAdapter
import com.telehealthmanager.app.utils.CustomBackClick
import com.telehealthmanager.app.utils.ViewUtils
import kotlinx.android.synthetic.main.content_chat_summary.view.*
import kotlinx.android.synthetic.main.list_item_doctor_image.view.*
import java.lang.Math.abs
import java.util.*
import kotlin.collections.ArrayList


class ChatSummaryActivity : BaseActivity<ActivityChatSummaryBinding>(), ChatNavigator, CustomBackClick {
    private val preferenceHelper = PreferenceHelper(BaseApplication.baseApplication)

    override fun getLayoutId(): Int = R.layout.activity_chat_summary
    lateinit var category: CategoryResponse.Category
    lateinit var notes: String
    lateinit var fees: String
    private lateinit var viewModel: ChatViewModel
    private lateinit var viewModelFindDoctor: FindDoctorsViewModel
    private lateinit var viewModelSummary: ChatSummaryViewModel
    private lateinit var mDataBinding: ActivityChatSummaryBinding
    private var mAdapter: DoctorImageAdapter? = null

    private var docImage: ArrayList<String> = ArrayList()

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mDataBinding = mViewDataBinding as ActivityChatSummaryBinding
        viewModel = ViewModelProviders.of(this).get(ChatViewModel::class.java)
        mDataBinding.viewmodel = viewModel
        viewModelSummary = ViewModelProviders.of(this).get(ChatSummaryViewModel::class.java)
        mDataBinding.viewmodelSummary = viewModelSummary
        viewModelFindDoctor = ViewModelProviders.of(this).get(FindDoctorsViewModel::class.java)
        mDataBinding.viewModelFindDoctor = viewModelFindDoctor
        viewModel.navigator = this
        val data = intent.extras
        if (data != null) {
            category = intent.getSerializableExtra("category") as CategoryResponse.Category
            notes = intent.getStringExtra("notes") ?: ""
        }
        //initAdapter()
        observeResponse()
        initApiCal()
        initDocView()
        mDataBinding.contentChatSummary.textViewVerifiedText.text = getString(R.string.verified_specialists_online_now, category.name)
        mDataBinding.contentChatSummary.tvSummaryStrikePrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        if (category.discount == 0.00) {
            mDataBinding.contentChatSummary.tvSummaryPrice.text = String.format("%s %s", preferenceHelper.getValue(PreferenceKey.CURRENCY, "$"), category?.fees.toString())
            mDataBinding.contentChatSummary.tvSummaryStrikePrice.visibility = View.GONE
            fees = category.fees.toString()
        } else {
            mDataBinding.contentChatSummary.tvSummaryStrikePrice.visibility = View.VISIBLE
            mDataBinding.contentChatSummary.tvSummaryPrice.text = String.format("%s %s", preferenceHelper.getValue(PreferenceKey.CURRENCY, "$"), category?.discount.toString())
            mDataBinding.contentChatSummary.tvSummaryStrikePrice.text = String.format("%s %s", preferenceHelper.getValue(PreferenceKey.CURRENCY, "$"), category?.fees.toString())
            fees = category.offer_fees.toString()
        }

        mDataBinding.contentChatSummary.textViewApplyPromo.setOnClickListener {
            if (mDataBinding.contentChatSummary.editTextPromoValue.text.toString().equals("")) {
                ViewUtils.showToast(this@ChatSummaryActivity, getString(R.string.please_enter_promo_code), false)
            } else {
                val hashMap: HashMap<String, Any> = HashMap()
                hashMap["id"] = category.id
                hashMap["promocode"] = mDataBinding.contentChatSummary.editTextPromoValue.text.toString()
                loadingObservable.value = true
                viewModelSummary.addPromoCode(hashMap)
            }
        }

        mDataBinding.toolBar.appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (abs(verticalOffset) == appBarLayout.totalScrollRange) {
                mDataBinding.toolBar.scrollToolbarBar.visibility = View.GONE
                mDataBinding.toolBar.toolbarVisible.visibility = View.VISIBLE
            } else if (verticalOffset == 0) {
                mDataBinding.toolBar.scrollToolbarBar.visibility = View.VISIBLE
                mDataBinding.toolBar.toolbarVisible.visibility = View.GONE
            }
        })
        mDataBinding.buttonToProceed.setOnClickListener {
            payForChatRequest()
        }

        viewModel.setOnClickListener(this@ChatSummaryActivity)
        viewModel.toolBarTile.value = getString(R.string.chat_summary)
    }

    override fun clickBackPress() {
        finish()
    }

    private fun payForChatRequest() {
        val intent = Intent(this@ChatSummaryActivity, PaymentActivity::class.java)
        intent.putExtra("category", category)
        intent.putExtra("notes", notes)
        startActivity(intent)
    }

    private fun initApiCal() {
        loadingObservable.value = true
        viewModelFindDoctor.getDoctorByCategorys(category.id)
    }

    private fun observeResponse() {

        viewModelFindDoctor.mDoctorResponse.observe(this, Observer<DoctorListResponse> {
            viewModelFindDoctor.mDoctorList = it.Specialities.doctor_profile as MutableList<DoctorListResponse.specialities.DoctorProfile>?

            if (viewModelFindDoctor.mDoctorList!!.size > 0) {
                mDataBinding.contentChatSummary.tvSpecialistNotFound.visibility = View.GONE
                mDataBinding.contentChatSummary.textViewVerifiedText.visibility = View.VISIBLE
                mDataBinding.contentChatSummary.textViewOneOfThem.visibility = View.VISIBLE
                mDataBinding.buttonToProceed.alpha = 1f;
                mDataBinding.buttonToProceed.isClickable = true
                initDocView()
            } else {
                mDataBinding.contentChatSummary.tvSpecialistNotFound.visibility = View.VISIBLE
                mDataBinding.contentChatSummary.textViewVerifiedText.visibility = View.GONE
                mDataBinding.contentChatSummary.textViewOneOfThem.visibility = View.GONE
                mDataBinding.buttonToProceed.alpha = .5f;
                mDataBinding.buttonToProceed.isClickable = false;
            }
            if (viewModelFindDoctor.mDoctorList!!.size > 5) {
                mDataBinding.contentChatSummary.tvDoctorCount.text = String.format("+%s", (viewModelFindDoctor.mDoctorList!!.size - 5))
                mDataBinding.contentChatSummary.tvDoctorCount.visibility = View.VISIBLE
            } else {
                mDataBinding.contentChatSummary.tvDoctorCount.visibility = View.GONE
            }

            loadingObservable.value = false
        })

        viewModel.getErrorObservable().observe(this, Observer<String> { message ->
            loadingObservable.value = false
            ViewUtils.showToast(this@ChatSummaryActivity, message, false)
        })

        viewModelSummary.getErrorObservable().observe(this, Observer<String> { message ->
            loadingObservable.value = false
            ViewUtils.showToast(this@ChatSummaryActivity, message, false)
        })

        viewModelSummary.mChatPromoResponse.observe(this, Observer<ChatPromoSuccess> {
            loadingObservable.value = false
            if (it?.message != null && it.message != "") {
                ViewUtils.showToast(this@ChatSummaryActivity, it.message, true)
                mDataBinding.contentChatSummary.tvSummaryPrice.text = String.format("%s %s", preferenceHelper.getValue(PreferenceKey.CURRENCY, "$"), it.final_fees!!.toString())
                fees = it.final_fees.toString()
            }
        })
    }

    private fun initDocView() {
        val doctorView = mDataBinding.contentChatSummary.doctorRow
        for (i in viewModelFindDoctor.mDoctorList!!.indices) {
            if (i == 0) {
                doctorView.doctor1.visibility = View.VISIBLE
                ViewUtils.setDocViewGlide(
                    this,
                    doctorView.doctorRow.doctor1,
                    BuildConfig.BASE_IMAGE_URL.plus(viewModelFindDoctor.mDoctorList!![i]?.profile_pic ?: "")
                )
            } else if (i == 1) {
                doctorView.doctor2.visibility = View.VISIBLE
                ViewUtils.setDocViewGlide(
                    this,
                    doctorView.doctor2,
                    BuildConfig.BASE_IMAGE_URL.plus(viewModelFindDoctor.mDoctorList!![i]?.profile_pic ?: "")
                )
            } else if (i == 2) {
                doctorView.doctor3.visibility = View.VISIBLE
                ViewUtils.setDocViewGlide(
                    this,
                    doctorView.doctor3,
                    BuildConfig.BASE_IMAGE_URL.plus(viewModelFindDoctor.mDoctorList!![i]?.profile_pic ?: "")
                )
            } else if (i == 3) {
                doctorView.doctor4.visibility = View.VISIBLE
                ViewUtils.setDocViewGlide(
                    this,
                    doctorView.doctor4,
                    BuildConfig.BASE_IMAGE_URL.plus(viewModelFindDoctor.mDoctorList!![i]?.profile_pic ?: "")
                )
            } else if (i == 4) {
                doctorView.doctor5.visibility = View.VISIBLE
                ViewUtils.setDocViewGlide(
                    this,
                    doctorView.doctor5,
                    BuildConfig.BASE_IMAGE_URL.plus(viewModelFindDoctor.mDoctorList!![i]?.profile_pic ?: "")
                )
                break
            }
        }
    }

    private fun initAdapter() {
        docImage.clear()
        addImage()
        mAdapter = DoctorImageAdapter(viewModelFindDoctor.mDoctorList!!, docImage, this)
        val horizontalLayout = LinearLayoutManager(this@ChatSummaryActivity, LinearLayoutManager.HORIZONTAL, false)
        mDataBinding.contentChatSummary.rvSpecialist.layoutManager = horizontalLayout
        mDataBinding.contentChatSummary.rvSpecialist.adapter = mAdapter
        mAdapter!!.notifyDataSetChanged()
    }

    private fun addImage() {
        docImage.add("Image 1")
        docImage.add("Image 2")
        docImage.add("Image 3")
        docImage.add("Image 4")
        docImage.add("Image 5")
    }
}

