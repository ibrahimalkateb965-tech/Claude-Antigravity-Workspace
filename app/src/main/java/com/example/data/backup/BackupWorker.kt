package com.example.data.backup

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.R
import com.example.ui.screen.AppLang
import com.example.ui.screen.loc

class BackupWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    companion object {
        const val WORK_NAME = "auto_backup_worker"
        const val CHANNEL_ID = "backup_notifications"
        const val NOTIFICATION_ID = 1001
    }

    override suspend fun doWork(): Result {
        val prefs = BackupPreferences(applicationContext)

        // Check if auto backup is enabled
        if (!prefs.autoBackupEnabled) {
            return Result.success()
        }

        val backupService = BackupService(applicationContext)

        // Attempt backup
        val result = backupService.exportBackup(exportedBy = "auto")

        // Update preferences
        prefs.lastAutoBackupTimestamp = System.currentTimeMillis()
        prefs.lastAutoBackupStatus = if (result.success) "success" else "failed"

        // Cleanup old backups
        if (result.success) {
            backupService.cleanupOldBackups(prefs.maxBackupCount)
        }

        // Send notification
        if (prefs.notificationsEnabled) {
            sendNotification(result)
        }

        return if (result.success) {
            Result.success()
        } else {
            // Retry once
            if (runAttemptCount < 1) {
                Result.retry()
            } else {
                Result.failure()
            }
        }
    }

    private fun sendNotification(result: ExportResult) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create notification channel for Android 8+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "النسخ الاحتياطية".loc(),
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "إشعارات النسخ الاحتياطية التلقائية".loc()
            }
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setAutoCancel(true)

        if (result.success) {
            builder.setContentTitle("✅ تم إنشاء نسخة احتياطية".loc())
            builder.setContentText("${result.fileName} (${formatFileSize(result.fileSize)})")
        } else {
            builder.setContentTitle("❌ فشل إنشاء النسخة الاحتياطية".loc())
            builder.setContentText((result.error ?: "خطأ غير معروف").loc())
            builder.priority = NotificationCompat.PRIORITY_HIGH
        }

        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    private fun formatFileSize(bytes: Long): String {
        return when {
            bytes < 1024 -> "$bytes B"
            bytes < 1024 * 1024 -> "${bytes / 1024} KB"
            else -> String.format("%.1f MB", bytes / (1024.0 * 1024.0))
        }
    }
}
