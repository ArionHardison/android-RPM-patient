package com.midokter.app.ui.activity.profile

import android.view.View
import android.widget.Button
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import com.midokter.app.R
import com.midokter.app.base.BaseActivity
import com.midokter.app.databinding.ActivityProfileBinding
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : BaseActivity<ActivityProfileBinding>() {

    override fun getLayoutId(): Int = R.layout.activity_profile

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        button11.setOnClickListener {
            cutomColorButton(button11)
            cutomWhiteColorButton(button12)
            cutomWhiteColorButton(button13)
            layout_profile_personal.visibility = View.VISIBLE
            layout_profile_medical.visibility = View.GONE
            layout_profile_lifestyle.visibility = View.GONE
        }
        button12.setOnClickListener {
            cutomColorButton(button12)
            cutomWhiteColorButton(button11)
            cutomWhiteColorButton(button13)
            layout_profile_personal.visibility = View.GONE
            layout_profile_medical.visibility = View.VISIBLE
            layout_profile_lifestyle.visibility = View.GONE
        }
        button13.setOnClickListener {
            cutomColorButton(button13)
            cutomWhiteColorButton(button11)
            cutomWhiteColorButton(button12)
            layout_profile_personal.visibility = View.GONE
            layout_profile_medical.visibility = View.GONE
            layout_profile_lifestyle.visibility = View.VISIBLE
        }
    }

    fun cutomColorButton(button: Button) {
        button.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.colorButton))
        button.setTextColor(ContextCompat.getColor(applicationContext, R.color.colorWhite))
    }

    fun cutomWhiteColorButton(button: Button) {
        button.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.colorWhite))
        button.setTextColor(ContextCompat.getColor(applicationContext, R.color.colorBlack))
    }
}
