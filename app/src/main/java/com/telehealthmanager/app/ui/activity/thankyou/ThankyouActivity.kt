package com.telehealthmanager.app.ui.activity.thankyou

import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.databinding.ViewDataBinding
import com.telehealthmanager.app.R
import com.telehealthmanager.app.base.BaseActivity
import com.telehealthmanager.app.databinding.ActivityThankyouBinding

class ThankyouActivity : BaseActivity<ActivityThankyouBinding>() {

    private var mDelayHandler: Handler? = null
    private val SPLASH_DELAY: Long = 3000 //3 seconds
    override fun getLayoutId(): Int = R.layout.activity_thankyou

    override fun initView(mViewDataBinding: ViewDataBinding?) {

        mDelayHandler = Handler(Looper.myLooper()!!)

        //Navigate with delay
        mDelayHandler!!.postDelayed(mRunnable, SPLASH_DELAY)
    }
    internal val mRunnable: Runnable = Runnable {

        if (!isFinishing) {
            val intent = Intent()
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    public override fun onDestroy() {

        if (mDelayHandler != null) {
            mDelayHandler!!.removeCallbacks(mRunnable)
        }

        super.onDestroy()
    }
}
