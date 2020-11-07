package com.telehealthmanager.app.fcm

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.telehealthmanager.app.BaseApplication
import com.telehealthmanager.app.R
import com.telehealthmanager.app.data.PreferenceKey
import com.telehealthmanager.app.ui.activity.main.MainActivity
import com.telehealthmanager.app.ui.twilio.IncomingVideoCallActivity
import org.json.JSONObject
import java.util.*


class FcmService : FirebaseMessagingService() {
    private val tagName = "FCMService"

    @SuppressLint("HardwareIds")
    override fun onNewToken(token: String?) {
        super.onNewToken(token)
        Log.d(tagName, "onNewToken()")
        Log.d(tagName, "FireBaseRegToken: " + token!!)
        Log.e("FCMToken", "----$token")
        val deviceId = Settings.Secure.getString(
            contentResolver,
            Settings.Secure.ANDROID_ID
        )
        BaseApplication.getCustomPreference!!.edit().putString(PreferenceKey.DEVICE_TOKEN, token).apply()
        BaseApplication.getCustomPreference!!.edit().putString(PreferenceKey.DEVICE_ID, deviceId).apply()
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)
        Log.d(tagName, "msg: $remoteMessage")
        if (remoteMessage?.notification != null)
            sendNotification(remoteMessage.notification?.body.toString(), remoteMessage)
        else
            sendNotification(remoteMessage?.data?.get("message").toString(), remoteMessage)
    }

    private fun sendNotification(messageBody: String, remoteMessage: RemoteMessage?) {
        val notificationMap: Map<String, String?> = remoteMessage!!.getData()
        Log.d(tagName, "notificationMap: $notificationMap")
        if (notificationMap.containsKey("message")) {
            if (Objects.requireNonNull(notificationMap["message"]) == "video_call") {
                val params: Map<String?, String?> = remoteMessage.getData()
                val json = JSONObject(params)
                val pushResponse = Gson().fromJson(
                    json.toString(),
                    ReceiverPushResponse::class.java
                )
                val contentIntent = Intent(this, IncomingVideoCallActivity::class.java)
                contentIntent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT or Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_NEW_TASK)
                contentIntent.putExtra("receiver_push", pushResponse)
                showNotification("Incoming Video Call",contentIntent)
                openCallActivity(pushResponse)
            } else if (Objects.requireNonNull(notificationMap["message"]) == "audio_call") {
                wakeUpScreen()
                val params: Map<String?, String?> = remoteMessage.getData()
                val json = JSONObject(params)
                val pushResponse = Gson().fromJson(
                    json.toString(),
                    ReceiverPushResponse::class.java
                )
                val contentIntent = Intent(this, IncomingVideoCallActivity::class.java)
                contentIntent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT or Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_NEW_TASK)
                contentIntent.putExtra("receiver_push", pushResponse)
                showNotification("Incoming Audio Call",contentIntent)
                openCallActivity(pushResponse)
            } else {
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                showNotification(messageBody, intent)
            }
        }

    }

    private fun showNotification(messageBody: String, contentIntent: Intent){
        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, contentIntent, PendingIntent.FLAG_ONE_SHOT)
        val channelId = getString(R.string.app_name)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.app_logo)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }

    private fun wakeUpScreen() {
        val pm = this.getSystemService(POWER_SERVICE) as PowerManager
        val isScreenOn = pm.isScreenOn
        Log.e("screen on......", "" + isScreenOn)
        if (!isScreenOn) {
            @SuppressLint("InvalidWakeLockTag") val wl = pm.newWakeLock(
                PowerManager.FULL_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP or PowerManager.ON_AFTER_RELEASE,
                "MyLock"
            )
            wl.acquire(10000)
            @SuppressLint("InvalidWakeLockTag") val wl_cpu =
                pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyCpuLock")
            wl_cpu.acquire(10000)
        }
    }

    private fun openCallActivity(pushResponse: ReceiverPushResponse?) {
        val contentIntent = Intent(this, IncomingVideoCallActivity::class.java)
        contentIntent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT or Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_NEW_TASK)
        contentIntent.putExtra("receiver_push", pushResponse)
        startActivity(contentIntent)
    }
}