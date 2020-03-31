package com.midokter.app.ui.activity.visitedDoctor

import android.content.Intent
import androidx.databinding.ViewDataBinding
import com.midokter.app.R
import com.midokter.app.base.BaseActivity
import com.midokter.app.databinding.ActivityVisitedDoctorsDetailBinding
import com.midokter.app.ui.activity.thankyou.ThankyouActivity
import kotlinx.android.synthetic.main.activity_visited_doctors_detail.*

class VisitedDoctorsDetailActivity : BaseActivity<ActivityVisitedDoctorsDetailBinding>() {

    override fun getLayoutId(): Int = R.layout.activity_visited_doctors_detail

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        button14.setOnClickListener {
            val intent = Intent(applicationContext, ThankyouActivity::class.java)
            startActivity(intent);
        }
    }
}
