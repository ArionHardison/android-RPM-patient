package com.telehealthmanager.app.ui.activity.searchDoctor

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.potyvideo.library.AndExoPlayerView
import com.telehealthmanager.app.R
import kotlinx.android.synthetic.main.activity_video_view.*

class VideoViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_view)
        val extras = intent.extras ?: return
        val url = extras.getString("url")
        andExoPlayerView!!.setSource(url)
        andExoPlayerView!!.setExoPlayerCallBack { }
    }
}