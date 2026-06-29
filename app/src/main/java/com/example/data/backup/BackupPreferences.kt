package com.example.data.backup

import android.content.Context
import android.content.SharedPreferences

class BackupPreferences(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("backup_preferences", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_AUTO_BACKUP_ENABLED = "auto_backup_enabled"
        private const val KEY_BACKUP_HOUR = "backup_hour"
        private const val KEY_BACKUP_MINUTE = "backup_minute"
        private const val KEY_MAX_BACKUP_COUNT = "max_backup_count"
        private const val KEY_NOTIFICATIONS_ENABLED = "notifications_enabled"
        private const val KEY_LAST_AUTO_BACKUP_TIMESTAMP = "last_auto_backup_timestamp"
        private const val KEY_LAST_AUTO_BACKUP_STATUS = "last_auto_backup_status"
    }

    var autoBackupEnabled: Boolean
        get() = prefs.getBoolean(KEY_AUTO_BACKUP_ENABLED, true)
        set(value) = prefs.edit().putBoolean(KEY_AUTO_BACKUP_ENABLED, value).apply()

    var backupHour: Int
        get() = prefs.getInt(KEY_BACKUP_HOUR, 3) // Default 03:00
        set(value) = prefs.edit().putInt(KEY_BACKUP_HOUR, value).apply()

    var backupMinute: Int
        get() = prefs.getInt(KEY_BACKUP_MINUTE, 0)
        set(value) = prefs.edit().putInt(KEY_BACKUP_MINUTE, value).apply()

    var maxBackupCount: Int
        get() = prefs.getInt(KEY_MAX_BACKUP_COUNT, 7)
        set(value) = prefs.edit().putInt(KEY_MAX_BACKUP_COUNT, value).apply()

    var notificationsEnabled: Boolean
        get() = prefs.getBoolean(KEY_NOTIFICATIONS_ENABLED, true)
        set(value) = prefs.edit().putBoolean(KEY_NOTIFICATIONS_ENABLED, value).apply()

    var lastAutoBackupTimestamp: Long
        get() = prefs.getLong(KEY_LAST_AUTO_BACKUP_TIMESTAMP, 0L)
        set(value) = prefs.edit().putLong(KEY_LAST_AUTO_BACKUP_TIMESTAMP, value).apply()

    var lastAutoBackupStatus: String
        get() = prefs.getString(KEY_LAST_AUTO_BACKUP_STATUS, "") ?: ""
        set(value) = prefs.edit().putString(KEY_LAST_AUTO_BACKUP_STATUS, value).apply()

    fun getBackupTimeFormatted(): String {
        return String.format("%02d:%02d", backupHour, backupMinute)
    }
}
