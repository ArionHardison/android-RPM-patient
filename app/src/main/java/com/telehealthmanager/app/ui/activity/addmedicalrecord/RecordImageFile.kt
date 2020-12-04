package com.telehealthmanager.app.ui.activity.addmedicalrecord

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.telehealthmanager.app.R
import kotlinx.android.synthetic.main.activity_record_image.*

class RecordImageFile : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_image)
        val extras = intent.extras ?: return
        val url = extras.getString("file")
        Glide.with(this).load(url).apply(RequestOptions().placeholder(R.drawable.app_logo).error(R.drawable.app_logo).centerCrop().dontAnimate()).into(photo_view)
        back.setOnClickListener {
            onBackPressed()
        }
    }
}