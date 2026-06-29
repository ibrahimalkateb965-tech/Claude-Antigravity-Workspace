package com.example.data.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.R

object NotificationHelper {

    const val GENERAL_CHANNEL_ID = "general_app_notifications"
    const val BACKUP_CHANNEL_ID = "backup_notifications" // Kept for backward compatibility and BackupWorker

    fun createNotificationChannels(context: Context) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create General Channel (for student profile updates, daily reminder, session reminder)
            val generalChannel = NotificationChannel(
                GENERAL_CHANNEL_ID,
                "إشعارات تيجان النور العامة",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "إشعارات الحلقات، تحديث الطلاب، والتنبيهات اليومية"
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(generalChannel)

            // Create Backup Channel (already managed by BackupWorker, but here as fallback)
            val backupChannel = NotificationChannel(
                BACKUP_CHANNEL_ID,
                "النسخ الاحتياطية",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "إشعارات النسخ الاحتياطية التلقائية"
            }
            notificationManager.createNotificationChannel(backupChannel)
        }
    }

    fun sendNotification(context: Context, id: Int, title: String, text: String) {
        // Ensure channels are created
        createNotificationChannels(context)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val builder = NotificationCompat.Builder(context, GENERAL_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        notificationManager.notify(id, builder.build())
    }
}
