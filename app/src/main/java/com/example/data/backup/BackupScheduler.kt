package com.example.data.backup

import android.content.Context
import androidx.work.*
import java.util.Calendar
import java.util.concurrent.TimeUnit

class BackupScheduler(private val context: Context) {

    private val workManager: WorkManager by lazy { WorkManager.getInstance(context) }
    private val prefs: BackupPreferences by lazy { BackupPreferences(context) }

    /**
     * Schedule or reschedule the daily auto backup.
     * Calculates the initial delay to match the configured time (default: 03:00).
     */
    fun scheduleAutoBackup() {
        if (!prefs.autoBackupEnabled) {
            cancelAutoBackup()
            return
        }

        // Calculate delay until next backup time
        val now = Calendar.getInstance()
        val target = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, prefs.backupHour)
            set(Calendar.MINUTE, prefs.backupMinute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            // If target time already passed today, schedule for tomorrow
            if (before(now)) {
                add(Calendar.DAY_OF_MONTH, 1)
            }
        }

        val initialDelay = target.timeInMillis - now.timeInMillis

        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true) // Don't backup when battery is low
            .build()

        val backupRequest = PeriodicWorkRequestBuilder<BackupWorker>(
            repeatInterval = 24,
            repeatIntervalTimeUnit = TimeUnit.HOURS
        )
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                5, TimeUnit.MINUTES // Retry after 5 minutes on failure
            )
            .addTag("auto_backup")
            .build()

        workManager.enqueueUniquePeriodicWork(
            BackupWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE, // Replace existing schedule
            backupRequest
        )
    }

    /**
     * Cancel the auto backup schedule.
     */
    fun cancelAutoBackup() {
        workManager.cancelUniqueWork(BackupWorker.WORK_NAME)
    }

    /**
     * Toggle auto backup on/off and reschedule accordingly.
     */
    fun setAutoBackupEnabled(enabled: Boolean) {
        prefs.autoBackupEnabled = enabled
        if (enabled) {
            scheduleAutoBackup()
        } else {
            cancelAutoBackup()
        }
    }

    /**
     * Update backup time and reschedule.
     */
    fun setBackupTime(hour: Int, minute: Int) {
        prefs.backupHour = hour
        prefs.backupMinute = minute
        if (prefs.autoBackupEnabled) {
            scheduleAutoBackup()
        }
    }
}
