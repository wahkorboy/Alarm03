package com.wahkor.alarm03

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.wahkor.alarm03.model.CHANNEL_ID

class NotificationManagerCalling(var context: Context,var notification: Notification.Builder) {
    private val notificationManager=context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private lateinit var notificationChannel: NotificationChannel
    fun createChannel(notificationName:String){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel=
                NotificationChannel(CHANNEL_ID, notificationName,NotificationManager.IMPORTANCE_HIGH)
                notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    fun startNotify(){
        notificationManager.notify(0,notification.build())
    }


}