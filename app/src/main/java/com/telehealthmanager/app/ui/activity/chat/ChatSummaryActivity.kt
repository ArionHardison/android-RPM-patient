package com.telehealthmanager.app.ui.activity.chat

import android.content.Intent
import android.graphics.Paint
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.telehealthmanager.app.BaseApplication
import com.telehealthmanager.app.R
import com.telehealthmanager.app.base.BaseActivity
import com.telehealthmanager.app.data.PreferenceHelper
import com.telehealthmanager.app.data.PreferenceKey
import com.telehealthmanager.app.data.getValue
import com.telehealthmanager.app.databinding.ActivityChatSummaryBinding
import com.telehealthmanager.app.repositary.model.CategoryResponse
import com.telehealthmanager.app.repositary.model.ChatPromoResponse
import com.telehealthmanager.app.repositary.model.DoctorListResponse
import com.telehealthmanager.app.repositary.model.MessageResponse
import com.telehealthmanager.app.ui.activity.findDoctors.FindDoctorsViewModel
import com.telehealthmanager.app.ui.activity.success.SuccessActivity
import com.telehealthmanager.app.ui.adapter.DoctorImageAdapter
import com.telehealthmanager.app.utils.ViewUtils
import kotlinx.android.synthetic.main.content_chat_summary.view.*
import java.util.HashMap


class ChatSummaryActivity : BaseActivity<ActivityChatSummaryBinding>(), ChatNavigator {
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
            notes = intent.getStringExtra("notes")?:""
        }
        initAdapter()
        observeResponse()
        initApiCal()
        mDataBinding.contentChatSummary.textViewVerifiedText.text =
            getString(R.string.verified_specialists_online_now, category.name)
        mDataBinding.contentChatSummary.tvSummaryStrikePrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG)
        if (category?.offer_fees == 0.00) {
            mDataBinding.contentChatSummary.tvSummaryPrice.text = String.format(
                "%s %s",
                preferenceHelper.getValue(PreferenceKey.CURRENCY, "$"),
                category?.fees.toString()
            )
            mDataBinding.contentChatSummary.tvSummaryStrikePrice.visibility = View.GONE
            fees=category?.fees.toString()
        } else {
            mDataBinding.contentChatSummary.tvSummaryStrikePrice.visibility = View.VISIBLE
            mDataBinding.contentChatSummary.tvSummaryPrice.text = String.format(
                "%s %s",
                preferenceHelper.getValue(PreferenceKey.CURRENCY, "$"),
                category?.offer_fees.toString()
            )
            mDataBinding.contentChatSummary.tvSummaryStrikePrice.text = String.format(
                "%s %s",
                preferenceHelper.getValue(PreferenceKey.CURRENCY, "$"),
                category?.fees.toString()
            )
            fees=category?.offer_fees.toString()
        }

        mDataBinding.contentChatSummary.textViewApplyPromo.setOnClickListener {
            if (mDataBinding.contentChatSummary.editTextPromoValue.text.toString().equals("")) {
                ViewUtils.showToast(
                    this@ChatSummaryActivity,
                    getString(R.string.please_enter_promo_code),
                    false
                )
            } else {


                val hashMap: HashMap<String, Any> = HashMap()
                hashMap["id"] = category.id
                hashMap["promocode"] =
                    mDataBinding.contentChatSummary.editTextPromoValue.text.toString()
                viewModelSummary.addPromoCode(hashMap)
            }
        }

        mDataBinding.buttonToProceed.setOnClickListener {
            payForChatRequest()
        }
    }
    private fun payForChatRequest(){

        val hashMap: HashMap<String, Any> = HashMap()
        hashMap["id"] = category.id
        hashMap["message"] =notes
        hashMap["Amount"] =fees
        hashMap["pay_for"] ="CHAT"
        hashMap["promo_id"] = 1
        hashMap["speciality_id"] = category.id
        hashMap["payment_mode"] = "CARD"
        hashMap["use_wallet"] = false
        viewModelSummary.payForChatRequest(hashMap)
    }

    private fun initApiCal() {
        loadingObservable.value = true
        viewModelFindDoctor.getDoctorByCategorys(category.id)
    }

    private fun observeResponse() {

        viewModelFindDoctor.mDoctorResponse.observe(this, Observer<DoctorListResponse> {
            viewModelFindDoctor.mDoctorList =
                it.Specialities.doctor_profile as MutableList<DoctorListResponse.specialities.DoctorProfile>?
            if (viewModelFindDoctor.mDoctorList!!.size > 0) {
                mDataBinding.contentChatSummary.tvSpecialistNotFound.visibility = View.GONE
                mDataBinding.contentChatSummary.textViewVerifiedText.visibility = View.VISIBLE
                mDataBinding.contentChatSummary.textViewOneOfThem.visibility = View.VISIBLE
                mDataBinding.buttonToProceed.setAlpha(1f);
                mDataBinding.buttonToProceed.setClickable(true)
            } else {
                mDataBinding.contentChatSummary.tvSpecialistNotFound.visibility = View.VISIBLE
                mDataBinding.contentChatSummary.textViewVerifiedText.visibility = View.GONE
                mDataBinding.contentChatSummary.textViewOneOfThem.visibility = View.GONE
                mDataBinding.buttonToProceed.setAlpha(.5f);
                mDataBinding.buttonToProceed.setClickable(false);
            }
            if (viewModelFindDoctor.mDoctorList!!.size > 5) {
                mDataBinding.contentChatSummary.tvDoctorCount.text =
                    String.format("+%s", (viewModelFindDoctor.mDoctorList!!.size - 5))
                mDataBinding.contentChatSummary.tvDoctorCount.visibility = View.VISIBLE
            } else {
                mDataBinding.contentChatSummary.tvDoctorCount.visibility = View.GONE
            }
            initAdapter()
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
        viewModelSummary.mChatPromoResponse.observe(this, Observer<ChatPromoResponse> {
            if (it != null && it.message != null && !it.message.equals("")) {
                ViewUtils.showToast(this@ChatSummaryActivity, it.message, true)
                mDataBinding.contentChatSummary.tvSummaryPrice.text = String.format(
                    "%s %s",
                    preferenceHelper.getValue(PreferenceKey.CURRENCY, "$"),
                    it?.finalFees.toString()
                )
                fees=it?.finalFees.toString()
            }
        })
        viewModelSummary.mChatRequestResponse.observe(this, Observer<MessageResponse> {
            if (it != null && it.message != null && !it.message.equals("")) {
                ViewUtils.showToast(this@ChatSummaryActivity, it.message, true)
                val intent = Intent(applicationContext, SuccessActivity::class.java)
                intent.putExtra("isFrom","chat")
                intent.putExtra("title",getString(R.string.chat_thank_title,category.name))
                intent.putExtra("description",getString(R.string.chat_thank_desc))
                startActivity(intent);
                finishAffinity()
            }
        })
    }

    private fun initAdapter() {
        mAdapter = DoctorImageAdapter(viewModelFindDoctor.mDoctorList!!, this)
        // Set Horizontal Layout Manager
        // for Recycler view

        // Set Horizontal Layout Manager
        // for Recycler view
        val horizontalLayout: LinearLayoutManager = LinearLayoutManager(
            this@ChatSummaryActivity,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        mDataBinding.contentChatSummary.rvSpecialist.setLayoutManager(horizontalLayout)
        mDataBinding.contentChatSummary.rvSpecialist.adapter = mAdapter
        mAdapter!!.notifyDataSetChanged()
    }
}

