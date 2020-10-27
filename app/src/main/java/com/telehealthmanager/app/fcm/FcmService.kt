package com.telehealthmanager.app.fcm

import android.annotation.SuppressLint
import android.provider.Settings
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.telehealthmanager.app.BaseApplication
import com.telehealthmanager.app.data.PreferenceKey


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
            sendNotification(remoteMessage.notification?.body.toString())
        else
            sendNotification(remoteMessage?.data?.get("message").toString())
    }

    private fun sendNotification(messageBody: String) {

       /* if(messageBody.contentEquals("New message received")){
            val intent = Intent(this, ChatListActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            val pendingIntent = PendingIntent.getActivity(this, 0 *//* Request code *//*, intent,
                    PendingIntent.FLAG_ONE_SHOT)

            val channelId = getString(R.string.app_name)
            val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val notificationBuilder = NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.drawable.ic_push)
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

            notificationManager.notify(0 *//* ID of notification *//*, notificationBuilder.build())

        }
        else{
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            val pendingIntent = PendingIntent.getActivity(this, 0 *//* Request code *//*, intent,
                    PendingIntent.FLAG_ONE_SHOT)

            val channelId = getString(R.string.app_name)
            val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val notificationBuilder = NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.drawable.ic_push)
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

            notificationManager.notify(0 *//* ID of notification *//*, notificationBuilder.build())

        }*/

    }
}