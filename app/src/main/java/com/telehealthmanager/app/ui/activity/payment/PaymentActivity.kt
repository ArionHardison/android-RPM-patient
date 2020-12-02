package com.telehealthmanager.app.ui.activity.payment

import android.content.Intent
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.telehealthmanager.app.BaseApplication
import com.telehealthmanager.app.R
import com.telehealthmanager.app.base.BaseActivity
import com.telehealthmanager.app.data.Constant
import com.telehealthmanager.app.data.PreferenceHelper
import com.telehealthmanager.app.data.PreferenceKey
import com.telehealthmanager.app.data.getValue
import com.telehealthmanager.app.databinding.ActivityPaymentBinding
import com.telehealthmanager.app.repositary.model.CardList
import com.telehealthmanager.app.repositary.model.CategoryResponse
import com.telehealthmanager.app.repositary.model.MessageResponse
import com.telehealthmanager.app.ui.activity.addmoney.AddCardActivity
import com.telehealthmanager.app.ui.activity.addmoney.AddMoneyViewModel
import com.telehealthmanager.app.ui.activity.success.SuccessActivity
import com.telehealthmanager.app.ui.adapter.CardListAdapter
import com.telehealthmanager.app.ui.adapter.ICardItemClick
import com.telehealthmanager.app.utils.CustomBackClick
import com.telehealthmanager.app.utils.ViewUtils

class PaymentActivity : BaseActivity<ActivityPaymentBinding>(), PaymentNavigator, ICardItemClick, CustomBackClick {

    override fun getLayoutId(): Int = R.layout.activity_payment

    private val preferenceHelper = PreferenceHelper(BaseApplication.baseApplication)
    lateinit var category: CategoryResponse.Category
    lateinit var notes: String
    lateinit var fees: String

    private lateinit var mViewModel: PaymentViewModel
    private lateinit var mAddMoneyViewModel: AddMoneyViewModel
    private lateinit var mDataBinding: ActivityPaymentBinding
    private var mCardListAdapter: CardListAdapter? = null

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mDataBinding = mViewDataBinding as ActivityPaymentBinding
        mViewModel = ViewModelProviders.of(this).get(PaymentViewModel::class.java)
        mDataBinding.viewmodel = mViewModel
        mViewModel.navigator = this
        mAddMoneyViewModel = ViewModelProviders.of(this).get(AddMoneyViewModel::class.java)
        mDataBinding.addViewModel = mAddMoneyViewModel

        val data = intent.extras
        if (data != null) {
            category = intent.getSerializableExtra("category") as CategoryResponse.Category
            notes = intent.getStringExtra("notes") ?: ""
            fees = intent.getStringExtra("final_fees") ?: ""
            mDataBinding.tvConsultationFee.text = String.format("%s %s", preferenceHelper.getValue(PreferenceKey.CURRENCY, "$"), fees.toString())
        }

        initAdapter()
        observeShowLoading()
        observeSuccessResponse()
        observeErrorResponse()
        initApiCall()
        mViewModel.setOnClickListener(this@PaymentActivity)
        mDataBinding.payBtn.alpha = .5f
        mAddMoneyViewModel.mEnablePay.set(false)
    }

    override fun clickBackPress() {
        finish()
    }

    private fun initApiCall() {
        mAddMoneyViewModel.loadingProgress.value = true
        mAddMoneyViewModel.getCards()
    }

    private fun initAdapter() {
        mDataBinding.rvCardList.layoutManager = LinearLayoutManager(applicationContext)
        mDataBinding.rvCardList.addItemDecoration(DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL))
    }

    private fun observeShowLoading() {
        mAddMoneyViewModel.loadingProgress.observe(this, Observer {
            if (!it) {
                hideLoading()
            } else {
                showLoading()
            }
        })
    }

    private fun observeSuccessResponse() {
        mAddMoneyViewModel.mCardListResponse.observe(this, Observer {
            mAddMoneyViewModel.loadingProgress.value = false
            if (!it.isNullOrEmpty()) {
                mCardListAdapter = CardListAdapter(it, this@PaymentActivity, this, mAddMoneyViewModel)
                mDataBinding.rvCardList.adapter = mCardListAdapter
                mCardListAdapter!!.notifyDataSetChanged()
                alreadyAddedCards()
            } else {
                noCards()
            }
        })

        mViewModel.mChatRequestResponse.observe(this, Observer<MessageResponse> {
            mAddMoneyViewModel.loadingProgress.value = true
            if (it?.message != null && !it.message.equals("")) {
                ViewUtils.showToast(this@PaymentActivity, it.message, true)
                val intent = Intent(applicationContext, SuccessActivity::class.java)
                intent.putExtra("isFrom", "chat")
                intent.putExtra("title", getString(R.string.chat_thank_title, category.name))
                intent.putExtra("description", getString(R.string.chat_thank_desc))
                startActivity(intent);
                finishAffinity()
            }
        })

        mAddMoneyViewModel.mDeletedSuccess.observe(this, Observer {
            mAddMoneyViewModel.loadingProgress.value = true
            ViewUtils.showToast(this, it.message, true)
            initApiCall()
        })
    }

    private fun observeErrorResponse() {
        mViewModel.getErrorObservable().observe(this, Observer<String> { message ->
            ViewUtils.showToast(this, message, false)
            mAddMoneyViewModel.loadingProgress.value = false
        })
    }

    private fun noCards() {
        mDataBinding.rvCardList.visibility = View.GONE
        mDataBinding.addMoneyText.visibility = View.GONE
        mDataBinding.addCardButtom.visibility = View.VISIBLE
    }

    private fun alreadyAddedCards() {
        mDataBinding.rvCardList.visibility = View.VISIBLE
        mDataBinding.addMoneyText.visibility = View.VISIBLE
        mDataBinding.addCardButtom.visibility = View.GONE
    }

    override fun openAddCard() {
        val callIntent = Intent(this@PaymentActivity, AddCardActivity::class.java)
        startActivityForResult(callIntent, Constant.REQUEST_CODE_ADD_CARD)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_CANCELED) {
            if (requestCode == Constant.REQUEST_CODE_ADD_CARD) {
                initApiCall()
                mAddMoneyViewModel.mSelectedCard.set("")
            }
        }
    }

    override fun onItemClickCard(item: CardList) {
        mAddMoneyViewModel.mSelectedCard.set(item.card_id.toString())
        mDataBinding.payBtn.alpha = 1f
        mAddMoneyViewModel.mEnablePay.set(true)
    }

    override fun onItemDelete(item: CardList) {
        val hashMap: HashMap<String, Any> = HashMap()
        hashMap["card_id"] = mAddMoneyViewModel.mDeletedCard.get().toString()
        mViewModel.loadingProgress.value = true
        mAddMoneyViewModel.deleteCard(hashMap)
    }

    override fun paymentInitiate() {
        if (mAddMoneyViewModel.mSelectedCard.get().toString() == "") {
            ViewUtils.showToast(this, getString(R.string.please_select_card), false)
        } else {
            val hashMap = HashMap<String, Any>()
            hashMap["id"] = category.id
            hashMap["message"] = notes
            hashMap["amount"] = fees
            hashMap["pay_for"] = "CHAT"
            hashMap["promo_id"] = 1
            hashMap["card_id"] = mAddMoneyViewModel.mSelectedCard.get().toString()
            hashMap["speciality_id"] = category.id
            hashMap["payment_mode"] = "CARD"
            hashMap["use_wallet"] = false
            mAddMoneyViewModel.loadingProgress.value = true
            mViewModel.payForChatRequest(hashMap)
        }
    }
}
