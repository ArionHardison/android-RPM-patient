package com.telehealthmanager.app.ui.activity.addmoney

import android.content.Intent
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.braintreepayments.cardform.utils.CardType
import com.stripe.android.Stripe
import com.stripe.android.TokenCallback
import com.stripe.android.model.Card
import com.stripe.android.model.Token
import com.telehealthmanager.app.BaseApplication
import com.telehealthmanager.app.R
import com.telehealthmanager.app.base.BaseActivity
import com.telehealthmanager.app.data.PreferenceHelper
import com.telehealthmanager.app.data.PreferenceKey
import com.telehealthmanager.app.data.getValue
import com.telehealthmanager.app.databinding.ActivityAddCardBinding
import com.telehealthmanager.app.utils.CustomBackClick
import com.telehealthmanager.app.utils.ViewUtils


class AddCardActivity : BaseActivity<ActivityAddCardBinding>(), AddMoneyNavigator, CustomBackClick {

    private var TAG: String = "AddCardActivity"
    lateinit var mDataBinding: ActivityAddCardBinding
    lateinit var mViewModel: AddMoneyViewModel
    private val SUPPORTED_CARD_TYPES = arrayOf(
        CardType.VISA, CardType.MASTERCARD, CardType.DISCOVER,
        CardType.AMEX, CardType.DINERS_CLUB, CardType.JCB, CardType.MAESTRO, CardType.UNIONPAY
    )

    private val preferenceHelper = PreferenceHelper(BaseApplication.baseApplication)


    override fun getLayoutId(): Int = R.layout.activity_add_card

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mDataBinding = mViewDataBinding as ActivityAddCardBinding
        mViewModel = ViewModelProvider(this).get(AddMoneyViewModel::class.java)
        mViewModel.navigator = this
        mDataBinding.viewmodel = mViewModel

        observeShowLoading()
        observeSuccessResponse()
        mDataBinding.supportedCardTypes.setSupportedCardTypes(*SUPPORTED_CARD_TYPES)
        mDataBinding.cardForm.cardRequired(true)
            .expirationRequired(true)
            .cvvRequired(true)
            .postalCodeRequired(false)
            .mobileNumberRequired(false)
            .setup(this)

        mDataBinding.addCard.setOnClickListener {
            val stripe_Key: String = preferenceHelper.getValue(PreferenceKey.STRIPE_KEY, "") as String
            if (mDataBinding.cardForm.cardNumber == null || mDataBinding.cardForm.expirationMonth == null || mDataBinding.cardForm.expirationYear == null || mDataBinding.cardForm.cvv == null) {
                ViewUtils.showToast(this@AddCardActivity, getString(R.string.card_details_not_valid), false)
            } else {
                if (mDataBinding.cardForm.cardNumber == "" || mDataBinding.cardForm.expirationMonth == "" || mDataBinding.cardForm.expirationYear == "" || mDataBinding.cardForm.cvv == "") {
                    ViewUtils.showToast(this@AddCardActivity, getString(R.string.card_details_not_valid), false)
                } else if (stripe_Key == "") {
                    ViewUtils.showToast(this@AddCardActivity, getString(R.string.stripe_key_not_found), false)
                } else {
                    mViewModel.loadingProgress.value = true
                    val cardNumber: String = mDataBinding.cardForm.cardNumber
                    val month: Int = mDataBinding.cardForm.expirationMonth.toInt()
                    val year: Int = mDataBinding.cardForm.expirationYear.toInt()
                    val cvv: String = mDataBinding.cardForm.cvv
                    val card = Card(cardNumber, month, year, cvv)
                    try {
                        val stripe = Stripe(this@AddCardActivity, stripe_Key)
                        stripe.createToken(card, object : TokenCallback {
                            override fun onSuccess(token: Token) {
                                mViewModel.loadingProgress.value = false
                                val hashMap: HashMap<String, Any> = HashMap()
                                hashMap["stripe_token"] = token.id
                                hashMap["user_type"] = "patient"
                                hashMap["status"] = "active"
                                mViewModel.addCard(hashMap)
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

    override fun clickBackPress() {
        finish()
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
        mViewModel.mAddCardSuccess.observe(this, Observer {
            mViewModel.loadingProgress.value = false
            ViewUtils.showToast(this, it.message, true)
            val intent = Intent()
            setResult(RESULT_OK, intent)
            finish()
        })
    }

    override fun openAddCard() {

    }

}