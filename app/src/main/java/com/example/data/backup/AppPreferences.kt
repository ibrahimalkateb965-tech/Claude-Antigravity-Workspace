package com.example.data.backup

import android.content.Context
import android.content.SharedPreferences

class AppPreferences(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_DARK_MODE = "dark_mode"
        private const val KEY_NOTIFICATIONS_ENABLED = "general_notifications_enabled"
        private const val KEY_SESSION_REMINDER_MINS = "session_reminder_minutes"
        private const val KEY_DAILY_REMINDER_TIME = "daily_reminder_time"
        private const val KEY_DISABLED_SESSION_GROUPS = "disabled_session_groups"
        private const val KEY_DISABLED_REPORT_GROUPS = "disabled_report_groups"
        private const val KEY_DISABLED_OTHER_GROUPS = "disabled_other_groups"
        private const val KEY_APP_LANGUAGE = "app_language"
    }

    // "ar" = Arabic (default), "en" = English
    var appLanguage: String
        get() = prefs.getString(KEY_APP_LANGUAGE, "ar") ?: "ar"
        set(value) = prefs.edit().putString(KEY_APP_LANGUAGE, value).apply()


    // null = system default, true = dark mode, false = light mode
    var isDarkMode: Boolean?
        get() {
            return if (!prefs.contains(KEY_DARK_MODE)) null
            else prefs.getBoolean(KEY_DARK_MODE, false)
        }
        set(value) {
            val editor = prefs.edit()
            if (value == null) {
                editor.remove(KEY_DARK_MODE)
            } else {
                editor.putBoolean(KEY_DARK_MODE, value)
            }
            editor.apply()
        }

    var notificationsEnabled: Boolean
        get() = prefs.getBoolean(KEY_NOTIFICATIONS_ENABLED, true)
        set(value) = prefs.edit().putBoolean(KEY_NOTIFICATIONS_ENABLED, value).apply()

    var sessionReminderMinutes: Int
        get() = prefs.getInt(KEY_SESSION_REMINDER_MINS, 30)
        set(value) = prefs.edit().putInt(KEY_SESSION_REMINDER_MINS, value).apply()

    var dailyReminderTime: String
        get() = prefs.getString(KEY_DAILY_REMINDER_TIME, "08:00") ?: "08:00"
        set(value) = prefs.edit().putString(KEY_DAILY_REMINDER_TIME, value).apply()

    var disabledSessionGroups: Set<String>
        get() = prefs.getStringSet(KEY_DISABLED_SESSION_GROUPS, emptySet()) ?: emptySet()
        set(value) = prefs.edit().putStringSet(KEY_DISABLED_SESSION_GROUPS, value).apply()

    var disabledReportGroups: Set<String>
        get() = prefs.getStringSet(KEY_DISABLED_REPORT_GROUPS, emptySet()) ?: emptySet()
        set(value) = prefs.edit().putStringSet(KEY_DISABLED_REPORT_GROUPS, value).apply()

    var disabledOtherGroups: Set<String>
        get() = prefs.getStringSet(KEY_DISABLED_OTHER_GROUPS, emptySet()) ?: emptySet()
        set(value) = prefs.edit().putStringSet(KEY_DISABLED_OTHER_GROUPS, value).apply()

    fun isSessionEnabledForGroup(groupName: String): Boolean {
        if (groupName.isBlank()) return true
        return !disabledSessionGroups.contains(groupName)
    }

    fun isReportEnabledForGroup(groupName: String): Boolean {
        if (groupName.isBlank()) return true
        return !disabledReportGroups.contains(groupName)
    }

    fun isOtherEnabledForGroup(groupName: String): Boolean {
        if (groupName.isBlank()) return true
        return !disabledOtherGroups.contains(groupName)
    }

    fun toggleSessionGroup(groupName: String, enabled: Boolean) {
        val current = disabledSessionGroups.toMutableSet()
        if (enabled) {
            current.remove(groupName)
        } else {
            current.add(groupName)
        }
        disabledSessionGroups = current
    }

    fun toggleReportGroup(groupName: String, enabled: Boolean) {
        val current = disabledReportGroups.toMutableSet()
        if (enabled) {
            current.remove(groupName)
        } else {
            current.add(groupName)
        }
        disabledReportGroups = current
    }

    fun toggleOtherGroup(groupName: String, enabled: Boolean) {
        val current = disabledOtherGroups.toMutableSet()
        if (enabled) {
            current.remove(groupName)
        } else {
            current.add(groupName)
        }
        disabledOtherGroups = current
    }
}
