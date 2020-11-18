package com.telehealthmanager.app.ui.activity.addmoney

import android.util.Log
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.stripe.android.Stripe
import com.stripe.android.TokenCallback
import com.stripe.android.model.Card
import com.stripe.android.model.Token
import com.telehealthmanager.app.BuildConfig
import com.telehealthmanager.app.R
import com.telehealthmanager.app.base.BaseActivity
import com.telehealthmanager.app.databinding.ActivityAddCardBinding
import com.telehealthmanager.app.utils.CustomBackClick
import com.telehealthmanager.app.utils.ViewUtils

class AddCardActivity : BaseActivity<ActivityAddCardBinding>(), AddMoneyNavigator, CustomBackClick {

    lateinit var mDataBinding: ActivityAddCardBinding
    lateinit var mViewModel: AddMoneyViewModel
    private var TAG: String = "AddCardActivity"

    override fun getLayoutId(): Int = R.layout.activity_add_card

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mDataBinding = mViewDataBinding as ActivityAddCardBinding
        mViewModel = ViewModelProviders.of(this).get(AddMoneyViewModel::class.java)
        mViewModel.navigator = this
        mDataBinding.viewmodel = mViewModel

        observeShowLoading()

        mDataBinding.cardForm.cardRequired(true)
            .expirationRequired(true)
            .cvvRequired(true)
            .postalCodeRequired(false)
            .mobileNumberRequired(false)
            .setup(this)

        mDataBinding.addCard.setOnClickListener {
            if (mDataBinding.cardForm.cardNumber == null || mDataBinding.cardForm.expirationMonth == null || mDataBinding.cardForm.expirationYear == null || mDataBinding.cardForm.cvv == null) {
                ViewUtils.showToast(this@AddCardActivity, getString(R.string.card_details_not_valid), false)
            } else {
                if (mDataBinding.cardForm.cardNumber == "" || mDataBinding.cardForm.expirationMonth == "" || mDataBinding.cardForm.expirationYear == "" || mDataBinding.cardForm.cvv == "") {
                    ViewUtils.showToast(this@AddCardActivity, getString(R.string.card_details_not_valid), false)
                } else {
                    mViewModel.loadingProgress.value = true
                    val cardNumber: String = mDataBinding.cardForm.cardNumber
                    val month: Int = mDataBinding.cardForm.expirationMonth.toInt()
                    val year: Int = mDataBinding.cardForm.expirationYear.toInt()
                    val cvv: String = mDataBinding.cardForm.cvv
                    val card = Card(cardNumber, month, year, cvv)
                    try {
                        val stripe = Stripe(this@AddCardActivity, BuildConfig.STRIPE_KEY)
                        stripe.createToken(card, object : TokenCallback {
                            override fun onSuccess(token: Token) {
                                mViewModel.loadingProgress.value = false
                                Log.e(TAG + "createToken==>", "" + token.id)
                            }

                            override fun onError(error: Exception) {
                                mViewModel.loadingProgress.value = false
                                ViewUtils.showToast(this@AddCardActivity, getString(R.string.card_details_not_valid), false)
                            }
                        })
                    } catch (e: Exception) {
                        e.printStackTrace()
                        mViewModel.loadingProgress.value = false
                    }
                }
            }
        }
        mViewModel.setOnClickListener(this@AddCardActivity)
        mViewModel.toolBarTile.value = getString(R.string.add_card)
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

    override fun clickBackPress() {
        finish()
    }

}