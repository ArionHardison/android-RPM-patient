package com.midokter.app.ui.activity.chat

import android.content.Intent
import androidx.databinding.ViewDataBinding
import com.midokter.app.R
import com.midokter.app.base.BaseActivity
import com.midokter.app.databinding.ActivityChatBinding
import kotlinx.android.synthetic.main.content_chat.*

class ChatActivity : BaseActivity<ActivityChatBinding>() {
    override fun getLayoutId(): Int = R.layout.activity_chat

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        button24.setOnClickListener {
            val intent = Intent(applicationContext, ChatProblemAreaActivity::class.java)
            startActivity(intent);
        }
        button25.setOnClickListener {
            val intent = Intent(applicationContext, ChatSummaryActivity::class.java)
            startActivity(intent);
        }
    }
}
