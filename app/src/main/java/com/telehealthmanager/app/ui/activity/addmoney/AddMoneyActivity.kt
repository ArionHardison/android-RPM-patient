package com.telehealthmanager.app.ui.activity.addmoney

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
import com.telehealthmanager.app.databinding.ActivityAddMoneyBinding
import com.telehealthmanager.app.repositary.model.CardList
import com.telehealthmanager.app.ui.adapter.CardListAdapter
import com.telehealthmanager.app.ui.adapter.ICardItemClick
import com.telehealthmanager.app.utils.CustomBackClick
import com.telehealthmanager.app.utils.ViewUtils

class AddMoneyActivity : BaseActivity<ActivityAddMoneyBinding>(), AddMoneyNavigator, CustomBackClick, ICardItemClick {

    private var TAG: String = "AddMoneyActivity"
    private val preferenceHelper = PreferenceHelper(BaseApplication.baseApplication)
    lateinit var mDataBinding: ActivityAddMoneyBinding
    lateinit var mViewModel: AddMoneyViewModel
    private var mCardListAdapter: CardListAdapter? = null

    override fun getLayoutId(): Int = R.layout.activity_add_money

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mDataBinding = mViewDataBinding as ActivityAddMoneyBinding
        mViewModel = ViewModelProviders.of(this).get(AddMoneyViewModel::class.java)
        mViewModel.navigator = this
        mDataBinding.viewmodel = mViewModel
        mViewModel.mWalletAmount.set(intent.getStringExtra(Constant.IntentData.WALLET_AMOUNT))
        mDataBinding.editTextAmount.text = preferenceHelper.getValue(PreferenceKey.CURRENCY, "$").toString().plus(" ").plus(mViewModel.mWalletAmount.get().toString())
        initAdapter()
        observeShowLoading()
        observeSuccessResponse()
        observeErrorResponse()
        initApiCall()
        mViewModel.setOnClickListener(this@AddMoneyActivity)
        mViewModel.toolBarTile.value = getString(R.string.add_money)
        mDataBinding.addMoneyBtn.alpha = .5f
        mViewModel.mEnablePay.set(false)
    }

    private fun initAdapter() {
        mDataBinding.rvCardList.layoutManager = LinearLayoutManager(applicationContext)
        mDataBinding.rvCardList.addItemDecoration(DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL))
    }

    override fun clickBackPress() {
        finish()
    }

    private fun initApiCall() {
        mViewModel.loadingProgress.value = true
        mViewModel.getCards()
    }

    private fun observeShowLoading() {
        mViewModel.loadingProgress.observe(this, Observer {
            if (!it) {
                hideLoading()
            } else {
                showLoading()
            }
        })
    }

    private fun observeSuccessResponse() {
        mViewModel.mCardListResponse.observe(this, Observer {
            mViewModel.loadingProgress.value = false
            if (!it.isNullOrEmpty()) {
                mCardListAdapter = CardListAdapter(it, this@AddMoneyActivity, this)
                mDataBinding.rvCardList.adapter = mCardListAdapter
                mCardListAdapter!!.notifyDataSetChanged()
                alreadyAddedCards()
            } else {
                noCards()
            }
        })

        mViewModel.mAddMoneySuccess.observe(this, Observer {
            mViewModel.loadingProgress.value = false
            ViewUtils.showToast(this, it.message, true)
            val intent = Intent()
            setResult(RESULT_OK, intent)
            finish()
        })
    }

    private fun noCards() {
        mDataBinding.layoutCardListEmpty.visibility = View.VISIBLE
        mDataBinding.addCardText.visibility = View.GONE
        mDataBinding.layoutCardList.visibility = View.GONE
    }

    private fun alreadyAddedCards() {
        mDataBinding.layoutCardListEmpty.visibility = View.GONE
        mDataBinding.addCardText.visibility = View.VISIBLE
        mDataBinding.layoutCardList.visibility = View.VISIBLE
    }

    private fun observeErrorResponse() {
        mViewModel.getErrorObservable().observe(this, Observer<String> { message ->
            ViewUtils.showToast(this, message, false)
            mViewModel.loadingProgress.value = false
        })
    }

    override fun openAddCard() {
        val callIntent = Intent(this@AddMoneyActivity, AddCardActivity::class.java)
        startActivityForResult(callIntent, Constant.REQUEST_CODE_ADD_CARD)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_CANCELED) {
            if (requestCode == Constant.REQUEST_CODE_ADD_CARD) {
                initApiCall()
                mViewModel.mSelectedCard.set("")
            }
        }
    }

    override fun onItemClickCard(item: CardList) {
        mViewModel.mSelectedCard.set(item.card_id.toString())
        mDataBinding.addMoneyBtn.alpha = 1f
        mViewModel.mEnablePay.set(true)
    }

    override fun addMoney() {
        val hashMap: HashMap<String, Any> = HashMap()
        hashMap["amount"] = mViewModel.mWalletAmount.get().toString()
        if (mViewModel.mSelectedCard.get().toString() == "") {
            ViewUtils.showToast(this, getString(R.string.please_select_card), false)
        } else {
            hashMap["card_id"] = mViewModel.mSelectedCard.get().toString()
            hashMap["payment_type"] = "stripe"
            hashMap["user_type"] = "patient"
            mViewModel.loadingProgress.value = true
            mViewModel.addMoneyWallet(hashMap)
        }
    }
}