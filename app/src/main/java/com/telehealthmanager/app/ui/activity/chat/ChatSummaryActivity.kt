package com.telehealthmanager.app.ui.activity.chat

import android.content.Intent
import android.graphics.Paint
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.appbar.AppBarLayout
import com.telehealthmanager.app.BaseApplication
import com.telehealthmanager.app.BuildConfig
import com.telehealthmanager.app.R
import com.telehealthmanager.app.base.BaseActivity
import com.telehealthmanager.app.data.Constant
import com.telehealthmanager.app.data.PreferenceHelper
import com.telehealthmanager.app.data.PreferenceKey
import com.telehealthmanager.app.data.getValue
import com.telehealthmanager.app.databinding.ActivityChatSummaryBinding
import com.telehealthmanager.app.repositary.model.*
import com.telehealthmanager.app.ui.activity.findDoctors.FindDoctorsViewModel
import com.telehealthmanager.app.ui.activity.payment.PaymentTypeActivity
import com.telehealthmanager.app.ui.activity.payment.PaymentViewModel
import com.telehealthmanager.app.ui.activity.success.SuccessActivity
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

    private lateinit var mDataBinding: ActivityChatSummaryBinding
    private lateinit var mViewModel: ChatSummaryViewModel
    private lateinit var viewModelFindDoctor: FindDoctorsViewModel
    private lateinit var paymentViewModel: PaymentViewModel

    lateinit var category: CategoryResponse.Category
    lateinit var notes: String
    lateinit var fees: String
    private var mAdapter: DoctorImageAdapter? = null
    private var docImage: ArrayList<String> = ArrayList()

    override fun getLayoutId(): Int = R.layout.activity_chat_summary

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mDataBinding = mViewDataBinding as ActivityChatSummaryBinding
        mViewModel = ViewModelProviders.of(this).get(ChatSummaryViewModel::class.java)
        mDataBinding.viewmodelSummary = mViewModel
        viewModelFindDoctor = ViewModelProviders.of(this).get(FindDoctorsViewModel::class.java)
        paymentViewModel = ViewModelProviders.of(this).get(PaymentViewModel::class.java)
        paymentViewModel.paymentType.set("wallet")
        paymentViewModel.paymentMode.set("Wallet")
        mDataBinding.contentChatSummary.paymentType.text = "Wallet"
        val data = intent.extras
        if (data != null) {
            category = intent.getSerializableExtra("category") as CategoryResponse.Category
            notes = intent.getStringExtra("notes") ?: ""
        }
        //initAdapter()
        observeResponse()
        observeError()
        initApiCal()
        initDocView()
        mDataBinding.contentChatSummary.textViewVerifiedText.text = getString(R.string.verified_specialists_online_now, category.name)
        mDataBinding.contentChatSummary.tvSummaryStrikePrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        if (category.discount == 0.00) {
            mDataBinding.contentChatSummary.tvSummaryPrice.text = String.format("%s %s", preferenceHelper.getValue(PreferenceKey.CURRENCY, "$"), category.fees.toString())
            mDataBinding.contentChatSummary.tvSummaryStrikePrice.visibility = View.GONE
            fees = category.fees.toString()
        } else {
            mDataBinding.contentChatSummary.tvSummaryStrikePrice.visibility = View.VISIBLE
            mDataBinding.contentChatSummary.tvSummaryPrice.text = String.format("%s %s", preferenceHelper.getValue(PreferenceKey.CURRENCY, "$"), category.offer_fees.toString())
            mDataBinding.contentChatSummary.tvSummaryStrikePrice.text = String.format("%s %s", preferenceHelper.getValue(PreferenceKey.CURRENCY, "$"), category.fees.toString())
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
                mViewModel.addPromoCode(hashMap)
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

        mDataBinding.contentChatSummary.changePayment.setOnClickListener {
            val callIntent = Intent(this@ChatSummaryActivity, PaymentTypeActivity::class.java)
            startActivityForResult(callIntent, Constant.PAYMENT_REQUEST_CODE)
        }

        mViewModel.setOnClickListener(this@ChatSummaryActivity)
        mViewModel.toolBarTile.value = getString(R.string.chat_summary)
    }


    override fun clickBackPress() {
        finish()
    }

    private fun payForChatRequest() {
        val hashMap = HashMap<String, Any>()
        hashMap["id"] = category.id
        hashMap["message"] = notes
        hashMap["amount"] = fees
        hashMap["pay_for"] = "CHAT"
        hashMap["promo_id"] = 1
        hashMap["speciality_id"] = category.id
        if (paymentViewModel.paymentType.get().toString() == "stripe") {
            hashMap["payment_mode"] = paymentViewModel.paymentType.get().toString()
            hashMap["card_id "] = paymentViewModel.mSelectedCard.get().toString()
            hashMap["use_wallet"] = false
        } else if (paymentViewModel.paymentType.get().toString() == "wallet") {
            hashMap["use_wallet"] = true
            hashMap["payment_mode"] = paymentViewModel.paymentType.get().toString()
        }
        loadingObservable.value = true
        paymentViewModel.payForChatRequest(hashMap)
    }

    private fun initApiCal() {
        loadingObservable.value = true
        viewModelFindDoctor.getDoctorByCategorys(category.id)
    }

    private fun observeResponse() {

        viewModelFindDoctor.mDoctorResponse.observe(this, Observer<DoctorListResponse> {
            loadingObservable.value = false
            viewModelFindDoctor.mDoctorList = it.Specialities.doctor_profile as MutableList<DoctorListResponse.specialities.DoctorProfile>?
            if (viewModelFindDoctor.mDoctorList!!.size > 0) {
                mDataBinding.contentChatSummary.tvSpecialistNotFound.visibility = View.GONE
                mDataBinding.contentChatSummary.textViewVerifiedText.visibility = View.VISIBLE
                mDataBinding.contentChatSummary.textViewOneOfThem.visibility = View.VISIBLE
                mDataBinding.buttonToProceed.alpha = 1f
                mDataBinding.buttonToProceed.isClickable = true
                initDocView()
            } else {
                mDataBinding.contentChatSummary.tvSpecialistNotFound.visibility = View.VISIBLE
                mDataBinding.contentChatSummary.textViewVerifiedText.visibility = View.GONE
                mDataBinding.contentChatSummary.textViewOneOfThem.visibility = View.GONE
                mDataBinding.buttonToProceed.alpha = .5f
                mDataBinding.buttonToProceed.isClickable = false
            }
            if (viewModelFindDoctor.mDoctorList!!.size > 5) {
                mDataBinding.contentChatSummary.tvDoctorCount.text = String.format("+%s", (viewModelFindDoctor.mDoctorList!!.size - 5))
                mDataBinding.contentChatSummary.tvDoctorCount.visibility = View.VISIBLE
            } else {
                mDataBinding.contentChatSummary.tvDoctorCount.visibility = View.GONE
            }
        })

        mViewModel.mChatPromoResponse.observe(this, Observer<ChatPromoSuccess> {
            loadingObservable.value = false
            if (it?.message != null && it.message != "") {
                ViewUtils.showToast(this@ChatSummaryActivity, it.message, true)
                mDataBinding.contentChatSummary.tvSummaryPrice.text = String.format("%s %s", preferenceHelper.getValue(PreferenceKey.CURRENCY, "$"), it.final_fees!!.toString())
                fees = it.final_fees.toString()
            }
        })

        paymentViewModel.mChatRequestResponse.observe(this, Observer<MessageResponse> {
            loadingObservable.value = false
            if (it?.message != null && !it.message.equals("")) {
                ViewUtils.showToast(this@ChatSummaryActivity, it.message, true)
                val intent = Intent(applicationContext, SuccessActivity::class.java)
                intent.putExtra("isFrom", "chat")
                intent.putExtra("title", getString(R.string.chat_thank_title, category.name))
                intent.putExtra("description", getString(R.string.chat_thank_desc))
                startActivity(intent);
                finishAffinity()
            }
        })
    }

    private fun observeError() {
        mViewModel.getErrorObservable().observe(this, Observer<String> { message ->
            loadingObservable.value = false
            ViewUtils.showToast(this@ChatSummaryActivity, message, false)
        })

        viewModelFindDoctor.getErrorObservable().observe(this, Observer<String> { message ->
            loadingObservable.value = false
            ViewUtils.showToast(this@ChatSummaryActivity, message, false)
        })

        paymentViewModel.getErrorObservable().observe(this, Observer<String> { message ->
            loadingObservable.value = false
            ViewUtils.showToast(this@ChatSummaryActivity, message, false)
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
                    BuildConfig.BASE_IMAGE_URL.plus(viewModelFindDoctor.mDoctorList!![i].profile_pic)
                )
            } else if (i == 1) {
                doctorView.doctor2.visibility = View.VISIBLE
                ViewUtils.setDocViewGlide(
                    this,
                    doctorView.doctor2,
                    BuildConfig.BASE_IMAGE_URL.plus(viewModelFindDoctor.mDoctorList!![i].profile_pic)
                )
            } else if (i == 2) {
                doctorView.doctor3.visibility = View.VISIBLE
                ViewUtils.setDocViewGlide(
                    this,
                    doctorView.doctor3,
                    BuildConfig.BASE_IMAGE_URL.plus(viewModelFindDoctor.mDoctorList!![i].profile_pic)
                )
            } else if (i == 3) {
                doctorView.doctor4.visibility = View.VISIBLE
                ViewUtils.setDocViewGlide(
                    this,
                    doctorView.doctor4,
                    BuildConfig.BASE_IMAGE_URL.plus(viewModelFindDoctor.mDoctorList!![i].profile_pic)
                )
            } else if (i == 4) {
                doctorView.doctor5.visibility = View.VISIBLE
                ViewUtils.setDocViewGlide(
                    this,
                    doctorView.doctor5,
                    BuildConfig.BASE_IMAGE_URL.plus(viewModelFindDoctor.mDoctorList!![i].profile_pic)
                )
                break
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_CANCELED) {
            if (requestCode == Constant.PAYMENT_REQUEST_CODE) {
                val isCheckCard = data?.getIntExtra("is_card", 0)
                if (isCheckCard == 0) {
                    paymentViewModel.paymentMode.set("Wallet")
                    paymentViewModel.paymentType.set("wallet")
                    paymentViewModel.mSelectedCard.set("")
                    mDataBinding.contentChatSummary.paymentType.text = "Wallet"
                } else {
                    val getCard = data?.getSerializableExtra("card") as CardList
                    paymentViewModel.paymentMode.set("XXXX-XXXX-XXXX-".plus(getCard.last_four))
                    paymentViewModel.paymentType.set("stripe")
                    paymentViewModel.mSelectedCard.set(getCard.card_id)
                    mDataBinding.contentChatSummary.paymentType.text = "XXXX-XXXX-XXXX-".plus(getCard.last_four)
                }
            }
        }
    }

    private fun addImage() {
        docImage.add("Image 1")
        docImage.add("Image 2")
        docImage.add("Image 3")
        docImage.add("Image 4")
        docImage.add("Image 5")
    }
}

