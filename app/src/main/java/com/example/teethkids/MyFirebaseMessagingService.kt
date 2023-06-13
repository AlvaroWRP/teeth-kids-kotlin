package com.example.teethkids

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.FirebaseMessaging

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        if (remoteMessage.notification != null) {
            val title = remoteMessage.notification?.title
            val body = remoteMessage.notification?.body
            if (title != null && body != null) {
                if (isNotificationPermissionGranted()) {
                    createNotification(title, body)
                }
            }
        }
    }

    private fun createNotification(title: String, body: String) {
        val channelId = "default_channel_id"
        val channelName = "Default Channel"

        val notificationChannel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)

        val notificationId = (System.currentTimeMillis() % 10000).toInt()

        // Build the notification
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.baseline_notification_important_24)
            .setAutoCancel(true)

        // Display the notification
        val notificationManagerCompat = NotificationManagerCompat.from(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        notificationManagerCompat.notify(notificationId, notificationBuilder.build())
    }

    private fun isNotificationPermissionGranted(): Boolean {
        val notificationPermission = Manifest.permission.ACCESS_NOTIFICATION_POLICY
        val permissionStatus = ContextCompat.checkSelfPermission(this, notificationPermission)
        return permissionStatus == PackageManager.PERMISSION_GRANTED
    }
}


