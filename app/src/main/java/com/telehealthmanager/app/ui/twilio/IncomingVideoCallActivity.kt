package com.telehealthmanager.app.ui.twilio

import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.telehealthmanager.app.BuildConfig
import com.telehealthmanager.app.R
import com.telehealthmanager.app.fcm.ReceiverPushResponse
import com.telehealthmanager.app.repositary.ApiInterface
import com.telehealthmanager.app.repositary.AppRepository
import com.telehealthmanager.app.repositary.model.VideoCallCancelResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlinx.android.synthetic.main.activity_incoming_call.*

class IncomingVideoCallActivity : AppCompatActivity(), View.OnClickListener {

    var chatPath: String? = null
    var name: String? = null
    var sender: String? = null
    var isVideo = "1"
    var ringtone: Ringtone? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_incoming_call)
        instance = this
        window.addFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                    WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        )

        imgEndCall.setOnClickListener(this)
        imgAcceptCall.setOnClickListener(this)

        val receiverPushResponse: ReceiverPushResponse = intent.getParcelableExtra("receiver_push") as ReceiverPushResponse
        name = receiverPushResponse.name
        sender = receiverPushResponse.sender_id
        chatPath = receiverPushResponse.room_id
        isVideo = "1"
        lblName.setText(name)
        if (isVideo.equals("1", ignoreCase = true)) {
            callType.setText("Incoming video call...")
        } else {
            callType.setText("Incoming audio call...")
        }
        playRingtone()
    }

    override fun onBackPressed() {}
    override fun onClick(v: View) {
        when (v.id) {
            R.id.imgEndCall -> {
                stopRingtone()
                cancelVideoCall()
            }
            R.id.imgAcceptCall -> {
                stopRingtone()
                val i = Intent(applicationContext, TwilloVideoActivity::class.java)
                i.putExtra("chat_path", chatPath)
                i.putExtra("is_video", isVideo)
                i.putExtra("sender", sender)
                startActivity(i)
                finish()
            }
        }
    }

    private fun playRingtone() {
        val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        if (notification != null) {
            ringtone = RingtoneManager.getRingtone(applicationContext, notification)
            ringtone!!.play()
        }
    }

    private val appRepository = AppRepository()
    private fun cancelVideoCall() {
        val call = appRepository.createApiClient(BuildConfig.BASE_URL, ApiInterface::class.java)
            .cancelVideoCall(chatPath)
        call!!.enqueue(object : Callback<VideoCallCancelResponse?> {
            override fun onResponse(
                call: Call<VideoCallCancelResponse?>,
                response: Response<VideoCallCancelResponse?>
            ) {
                finish()
            }

            override fun onFailure(call: Call<VideoCallCancelResponse?>, t: Throwable) {}
        })
    }

    private fun stopRingtone() {
        if (ringtone != null && ringtone!!.isPlaying) ringtone!!.stop()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun finish() {
        super.finish()
        instance = null
    }

    companion object {
        var instance: IncomingVideoCallActivity? = null
    }
}