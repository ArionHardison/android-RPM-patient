package com.telehealthmanager.app.ui.twilio

import android.app.*
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.telehealthmanager.app.R

class TwilioCallService : Service(), CallHangUpListener {
    private val TAG = TwilioCallService::class.java.simpleName

    private val NOTIFICATION_CHANNEL_ID = "CALL CHANNEL"

    private var mNotificationManager: NotificationManager? = null
    private val notificationId = 11

    private var callRequest: ConnectedStatus? = null

    override fun onCreate() {
        Log.d(TAG, "onCreate: ")
        super.onCreate()
        mNotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        RoomManager.setHangUpListener(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        callRequest = intent?.getParcelableExtra(TwilloVideoActivity.CALL_REQUEST)
        startForeground(notificationId, getNotification())
        return START_NOT_STICKY
    }


    private fun getNotification(): Notification {
        createNotificationChannel()
        val intent = Intent(this, TwilloVideoActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        intent.putExtra(TwilloVideoActivity.CALL_CONNECTED_STATUS, callRequest)
        val contentIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.sym_action_call)
            .setColor(ContextCompat.getColor(this, R.color.colorGreen))
            .setContentTitle("Ongoing Call..")
            .setContentText("Tap to continue the call...")
            .setSound(null)
            .setUsesChronometer(true)
            .setContentIntent(contentIntent)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_ID,
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            }
            notificationChannel.setSound(null, null)
            mNotificationManager!!.createNotificationChannel(notificationChannel)
        }
    }

    override fun onCallEnd(callDuration: String) {
        stopSelf()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy: ")
        super.onDestroy()
    }
}