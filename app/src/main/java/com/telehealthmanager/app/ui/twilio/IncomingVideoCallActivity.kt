package com.telehealthmanager.app.ui.twilio

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.telehealthmanager.app.BuildConfig
import com.telehealthmanager.app.R
import com.telehealthmanager.app.fcm.ReceiverPushResponse
import com.telehealthmanager.app.repositary.ApiInterface
import com.telehealthmanager.app.repositary.AppRepository
import com.telehealthmanager.app.repositary.model.VideoCallCancelResponse
import kotlinx.android.synthetic.main.activity_incoming_call.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class IncomingVideoCallActivity : AppCompatActivity(), View.OnClickListener {
    private val CAMERA_MIC_PERMISSION_REQUEST_CODE = 1
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
                if (!checkPermissionForCameraAndMicrophone()) {
                    requestPermissionForCameraAndMicrophone()
                } else {
                    openTwillioCall()
                }
            }
        }
    }


    private fun checkPermissionForCameraAndMicrophone(): Boolean {
        val resultCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        val resultMic = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
        return resultCamera == PackageManager.PERMISSION_GRANTED && resultMic == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissionForCameraAndMicrophone() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)
            || ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.RECORD_AUDIO
            )
        ) {
            Toast.makeText(this, R.string.permissions_needed, Toast.LENGTH_LONG).show()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO),
                CAMERA_MIC_PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == CAMERA_MIC_PERMISSION_REQUEST_CODE) {
            var cameraAndMicPermissionGranted = true
            for (grantResult in grantResults) {
                cameraAndMicPermissionGranted =
                    cameraAndMicPermissionGranted and (grantResult == PackageManager.PERMISSION_GRANTED)
            }
            if (cameraAndMicPermissionGranted) {
                openTwillioCall()
            } else {
                Toast.makeText(this, R.string.permissions_needed, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun openTwillioCall() {
        stopRingtone()
        // Clear all notification
        val nMgr: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nMgr.cancelAll()
        val i = Intent(applicationContext, TwilloVideoActivity::class.java)
        i.putExtra("chat_path", chatPath)
        i.putExtra("is_video", isVideo)
        i.putExtra("sender", sender)
        i.putExtra("is_request", false)
        startActivity(i)
        finish()
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