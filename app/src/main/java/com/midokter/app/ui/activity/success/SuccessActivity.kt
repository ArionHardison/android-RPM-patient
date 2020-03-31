package com.midokter.app.ui.activity.success

import androidx.databinding.ViewDataBinding
import com.midokter.app.R
import com.midokter.app.base.BaseActivity
import com.midokter.app.databinding.ActivitySuccessBinding
import kotlinx.android.synthetic.main.activity_success.*

class SuccessActivity : BaseActivity<ActivitySuccessBinding>() {
    override fun getLayoutId(): Int = R.layout.activity_success

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        imageView21.setOnClickListener {
            finishAffinity()
        }
    }
}
