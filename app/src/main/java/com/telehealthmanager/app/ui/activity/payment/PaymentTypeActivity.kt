package com.telehealthmanager.app.ui.activity.payment

import android.content.Intent
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.telehealthmanager.app.R
import com.telehealthmanager.app.base.BaseActivity
import com.telehealthmanager.app.data.Constant
import com.telehealthmanager.app.databinding.ActivityPaymentTypeBinding
import com.telehealthmanager.app.repositary.model.CardList
import com.telehealthmanager.app.ui.activity.addmoney.AddCardActivity
import com.telehealthmanager.app.ui.activity.addmoney.AddMoneyViewModel
import com.telehealthmanager.app.ui.adapter.AvailableCardsAdapter
import com.telehealthmanager.app.ui.adapter.ICardClick
import com.telehealthmanager.app.utils.CustomBackClick
import com.telehealthmanager.app.utils.ViewUtils
import java.io.Serializable

class PaymentTypeActivity : BaseActivity<ActivityPaymentTypeBinding>(), ICardClick, CustomBackClick {

    override fun getLayoutId(): Int = R.layout.activity_payment_type

    private lateinit var mAddMoneyViewModel: AddMoneyViewModel
    private lateinit var mDataBinding: ActivityPaymentTypeBinding
    private var mCardListAdapter: AvailableCardsAdapter? = null

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mDataBinding = mViewDataBinding as ActivityPaymentTypeBinding
        mAddMoneyViewModel = ViewModelProviders.of(this).get(AddMoneyViewModel::class.java)
        mDataBinding.viewmodel = mAddMoneyViewModel
        initAdapter()
        observeShowLoading()
        observeSuccessResponse()
        observeErrorResponse()
        initApiCall()
        mAddMoneyViewModel.setOnClickListener(this@PaymentTypeActivity)
        mAddMoneyViewModel.toolBarTile.value = getString(R.string.payment_view)

        mDataBinding.addCardButtom.setOnClickListener{
            val callIntent = Intent(this@PaymentTypeActivity, AddCardActivity::class.java)
            startActivityForResult(callIntent, Constant.REQUEST_CODE_ADD_CARD)
        }

        mDataBinding.walletLayout.setOnClickListener {
            val intent = Intent()
            intent.putExtra("is_card", 0)
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    private fun initAdapter() {
        mDataBinding.rvCardList.layoutManager = LinearLayoutManager(applicationContext)
        mDataBinding.rvCardList.addItemDecoration(DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL))
    }

    private fun initApiCall() {
        mAddMoneyViewModel.loadingProgress.value = true
        mAddMoneyViewModel.getCards()
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
                mCardListAdapter = AvailableCardsAdapter(it, this@PaymentTypeActivity, this)
                mDataBinding.rvCardList.adapter = mCardListAdapter
                mCardListAdapter!!.notifyDataSetChanged()
            }
        })

        mAddMoneyViewModel.mDeletedSuccess.observe(this, Observer {
            mAddMoneyViewModel.loadingProgress.value = false
            ViewUtils.showToast(this, it.message, true)
            initApiCall()
        })
    }


    private fun observeErrorResponse() {
        mAddMoneyViewModel.getErrorObservable().observe(this, Observer<String> { message ->
            ViewUtils.showToast(this, message, false)
            mAddMoneyViewModel.loadingProgress.value = false
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_CANCELED) {
            if (requestCode == Constant.REQUEST_CODE_ADD_CARD) {
                initApiCall()
            }
        }
    }

    override fun onItemClickCard(item: CardList) {
        val intent = Intent()
        intent.putExtra("card", item as Serializable)
        intent.putExtra("is_card", 1)
        setResult(RESULT_OK, intent)
        finish()
    }

    override fun onItemDelete(item: CardList) {

    }

    override fun clickBackPress() {
        val intent = Intent()
        setResult(RESULT_CANCELED, intent)
        finish()
    }
}