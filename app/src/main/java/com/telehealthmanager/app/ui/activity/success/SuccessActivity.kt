package com.telehealthmanager.app.ui.activity.success

import android.os.Handler
import android.os.Looper
import androidx.databinding.ViewDataBinding
import com.telehealthmanager.app.R
import com.telehealthmanager.app.base.BaseActivity
import com.telehealthmanager.app.databinding.ActivitySuccessBinding
import com.telehealthmanager.app.ui.activity.main.MainActivity
import kotlinx.android.synthetic.main.activity_success.*

class SuccessActivity : BaseActivity<ActivitySuccessBinding>() {
    private var mDelayHandler: Handler? = null

    override fun getLayoutId(): Int = R.layout.activity_success

    private lateinit var mDataBinding: ActivitySuccessBinding

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mDataBinding = mViewDataBinding as ActivitySuccessBinding
        mDelayHandler = Handler(Looper.myLooper()!!)
        val data = intent.extras
        if (data != null) {
            if (intent.getStringExtra("isFrom") != null && intent.getStringExtra("isFrom").toString().equals("chat")) {
                mDataBinding.textViewSuccessTitle.text = intent.getStringExtra("title")
                mDataBinding.textViewSuccessDesc.text = intent.getStringExtra("description")
            }
        }
        mDelayHandler!!.postDelayed(mRunnable, 3000)
        imageView21.setOnClickListener {
            openNewActivity(this@SuccessActivity, MainActivity::class.java, true)
            finishAffinity()
        }
    }

    private val mRunnable: Runnable = Runnable {
        if (!isFinishing) {
            openNewActivity(this@SuccessActivity, MainActivity::class.java, true)
            finishAffinity()
        }
    }

    public override fun onDestroy() {
        if (mDelayHandler != null) {
            mDelayHandler!!.removeCallbacks(mRunnable)
        }
        super.onDestroy()
    }

    override fun onBackPressed() {
    }
}
