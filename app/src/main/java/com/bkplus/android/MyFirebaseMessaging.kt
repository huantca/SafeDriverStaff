package com.bkplus.android

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues
import android.content.Context
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.bkplus.android.ultis.Constants.channel_id
import com.bkplus.android.ultis.Constants.channel_name
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.harrison.myapplication.R

class MyFirebaseMessaging : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        Log.i(ContentValues.TAG, "Refreshed token: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        if (remoteMessage.data.isNotEmpty()) {
            Log.i(ContentValues.TAG, "Message data payload: " + remoteMessage.data)
        }

        if (remoteMessage.notification != null) {
            val notificationBuilder: NotificationCompat.Builder =
                NotificationCompat.Builder(this, channel_id)
                    .setContentTitle(remoteMessage.notification!!.title)
                    .setContentText(remoteMessage.notification!!.body)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setStyle(NotificationCompat.BigTextStyle())
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setSmallIcon(R.drawable.ic_launcher_foreground).setAutoCancel(true)
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(
                channel_id, channel_name, NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
            notificationManager.notify(0, notificationBuilder.build())
        }
    }
}