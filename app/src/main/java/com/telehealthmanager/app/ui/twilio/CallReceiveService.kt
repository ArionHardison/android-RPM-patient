package com.telehealthmanager.app.ui.twilio

import android.app.*
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.RingtoneManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.telehealthmanager.app.R
import com.telehealthmanager.app.fcm.ReceiverPushResponse
import com.telehealthmanager.app.ui.twilio.TwilloVideoActivity.Companion.RECEIVE_PUSH_INFO

class CallReceiveService : Service() {
    companion object {
        const val CALL_CANCEL_ACTION = "DECLINE_CALL"
        const val CALL_IS_ACCEPTED = "CALL_IS_ACCEPTED"
        private const val CHANNEL_ID_AND_NAME = "RECEIVE_CALL_CHANNEL"
        private const val NOTIFICATION_ID = 10
    }

    private var mNotificationManager: NotificationManager? = null
    private var mVibratePattern = longArrayOf(0, 400, 400, 400, 400, 400, 400, 400)

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        var data: ReceiverPushResponse? = null
        mNotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (intent != null) {
            data = intent.getParcelableExtra(RECEIVE_PUSH_INFO)
        }

        // After 45 seconds stop service in-case receive didn't take the call.
        Handler(Looper.myLooper()!!).postDelayed({ updateMissedCall() }, 45000)

        if (data != null) {
            startForeground(NOTIFICATION_ID, getNotification(data))
        } else {
            stopSelf()
        }
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun getNotification(data: ReceiverPushResponse): Notification {

        val mIntent1 = Intent(this, TwilloVideoActivity::class.java)
        mIntent1.putExtra(RECEIVE_PUSH_INFO, data)
        mIntent1.putExtra(CALL_IS_ACCEPTED, true)
        val receiveCallPendingIntent: PendingIntent = PendingIntent.getActivity(this, 1, mIntent1, PendingIntent.FLAG_UPDATE_CURRENT)

        val contentIntent = Intent(this, TwilloVideoActivity::class.java)
        contentIntent.putExtra(RECEIVE_PUSH_INFO, data)
        contentIntent.putExtra(CALL_IS_ACCEPTED, false)
        val contentPendingIntent: PendingIntent = PendingIntent.getActivity(this, 2, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val cancelCallAction = Intent(this, CallDeclineReceiver::class.java)
        cancelCallAction.putExtra(RECEIVE_PUSH_INFO, data)
        cancelCallAction.action = CALL_CANCEL_ACTION

        val declineCallPendingIntent = PendingIntent.getBroadcast(this, 0, cancelCallAction, PendingIntent.FLAG_UPDATE_CURRENT)

        val notification: Notification

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            createNotificationChannel()

        val callType = "Incoming Video Call"

        notification = NotificationCompat
            .Builder(this, CHANNEL_ID_AND_NAME)
            .setContentText(data.name)
            .setContentTitle(callType)
            .setContentText(data.name)
            .setSmallIcon(android.R.drawable.sym_action_call)
            .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
            //.setCategory(NotificationCompat.Ca)
            .setContentIntent(contentPendingIntent)
            .addAction(android.R.drawable.sym_action_call, "Answer", receiveCallPendingIntent)
            .addAction(R.drawable.twillio_ic_call_end, "Decline", declineCallPendingIntent)
            .setAutoCancel(true)
            .setSound(RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_RINGTONE))
            .setVibrate(mVibratePattern)
            .setFullScreenIntent(contentPendingIntent, true)
            .build()
        return notification
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val channel = NotificationChannel(CHANNEL_ID_AND_NAME, CHANNEL_ID_AND_NAME, NotificationManager.IMPORTANCE_HIGH)
        channel.description = "Call Notifications"
        channel.enableVibration(true)
        channel.enableLights(true)
        channel.lightColor = Color.GREEN
        channel.setSound(
            RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_RINGTONE),
            AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setLegacyStreamType(AudioManager.STREAM_RING)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE).build()
        )
        mNotificationManager!!.createNotificationChannel(channel)

    }

    private fun updateMissedCall() {
        stopSelf()
    }
}