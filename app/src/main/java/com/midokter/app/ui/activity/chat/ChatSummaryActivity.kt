package com.midokter.app.ui.activity.chat

import android.content.Intent
import androidx.databinding.ViewDataBinding
import com.midokter.app.R
import com.midokter.app.base.BaseActivity
import com.midokter.app.databinding.ActivityChatSummaryBinding
import com.midokter.app.ui.activity.payment.PaymentActivity
import kotlinx.android.synthetic.main.content_chat_summary.*

class ChatSummaryActivity : BaseActivity<ActivityChatSummaryBinding>() {

    override fun getLayoutId(): Int = R.layout.activity_chat_summary

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        button26.setOnClickListener {
            val intent = Intent(applicationContext, PaymentActivity::class.java)
            startActivity(intent);
        }
    }
}
