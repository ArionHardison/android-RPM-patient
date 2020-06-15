package com.midokter.app.ui.activity.success

import android.os.Handler
import androidx.databinding.ViewDataBinding
import com.midokter.app.R
import com.midokter.app.base.BaseActivity
import com.midokter.app.databinding.ActivitySuccessBinding
import com.midokter.app.ui.activity.main.MainActivity
import kotlinx.android.synthetic.main.activity_success.*

class SuccessActivity : BaseActivity<ActivitySuccessBinding>() {
    private var mDelayHandler: Handler? = null
    private val SPLASH_DELAY: Long = 3000 //3 seconds
    override fun getLayoutId(): Int = R.layout.activity_success

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mDelayHandler = Handler()

        //Navigate with delay
        mDelayHandler!!.postDelayed(mRunnable, SPLASH_DELAY)
        imageView21.setOnClickListener {
            openNewActivity(this@SuccessActivity, MainActivity::class.java, true)
            finishAffinity()
        }
    }
    internal val mRunnable: Runnable = Runnable {

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
}
