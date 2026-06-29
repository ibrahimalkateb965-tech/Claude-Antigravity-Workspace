package com.example.data.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import com.example.data.backup.AppPreferences
import com.example.data.model.Student
import java.util.Calendar

object AlarmScheduler {

    const val DAILY_REMINDER_REQUEST_CODE = 9999
    const val SESSION_ALARM_OFFSET = 10000

    fun scheduleAllAlarms(context: Context, students: List<Student>) {
        val appPrefs = AppPreferences(context)
        if (!appPrefs.notificationsEnabled) {
            cancelAllAlarms(context, students)
            return
        }

        scheduleDailyReminder(context, appPrefs.dailyReminderTime)
        scheduleSessionAlarms(context, students, appPrefs.sessionReminderMinutes)
    }

    fun scheduleDailyReminder(context: Context, timeStr: String) {
        val appPrefs = AppPreferences(context)
        if (!appPrefs.notificationsEnabled) return

        val parts = timeStr.split(":")
        if (parts.size != 2) return
        val hour = parts[0].toIntOrNull() ?: 8
        val minute = parts[1].toIntOrNull() ?: 0

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            action = NotificationReceiver.ACTION_DAILY_REMINDER
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            DAILY_REMINDER_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val triggerTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (before(Calendar.getInstance())) {
                add(Calendar.DAY_OF_MONTH, 1)
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime.timeInMillis,
                    pendingIntent
                )
            } else {
                alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime.timeInMillis,
                    pendingIntent
                )
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerTime.timeInMillis,
                pendingIntent
            )
        } else {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                triggerTime.timeInMillis,
                pendingIntent
            )
        }
    }

    fun scheduleSessionAlarms(context: Context, students: List<Student>, warningMinutes: Int) {
        val appPrefs = AppPreferences(context)
        if (!appPrefs.notificationsEnabled) return

        students.forEach { student ->
            scheduleSingleSessionAlarm(context, student, warningMinutes)
        }
    }

    fun scheduleSingleSessionAlarm(context: Context, student: Student, warningMinutes: Int) {
        val appPrefs = AppPreferences(context)
        if (!appPrefs.notificationsEnabled) {
            cancelSingleSessionAlarm(context, student.id)
            return
        }

        // Cancel all existing alarms for this student first to clean up
        cancelSingleSessionAlarm(context, student.id)

        if (!appPrefs.isSessionEnabledForGroup(student.groupName) || student.circleSessionDaysTimes.isBlank()) {
            return
        }

        val weekDays = listOf("السبت", "الأحد", "الاثنين", "الثلاثاء", "الأربعاء", "الخميس", "الجمعة")
        val dayTimeMap = student.circleSessionDaysTimes.split(";")
            .filter { it.isNotBlank() }
            .associate {
                val parts = it.split("=")
                parts[0] to parts[1]
            }

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        dayTimeMap.forEach { (arabicDay, timeStr) ->
            val dayIndex = weekDays.indexOf(arabicDay)
            if (dayIndex == -1) return@forEach
            val androidDayOfWeek = getAndroidDayOfWeek(arabicDay) ?: return@forEach

            val parts = timeStr.split(":")
            if (parts.size != 2) return@forEach
            val hour = parts[0].toIntOrNull() ?: return@forEach
            val minute = parts[1].toIntOrNull() ?: return@forEach

            val intent = Intent(context, NotificationReceiver::class.java).apply {
                action = NotificationReceiver.ACTION_SESSION_REMINDER
                putExtra("studentId", student.id)
                putExtra("studentName", student.name)
                putExtra("groupName", student.groupName)
                putExtra("sessionTime", timeStr)
            }
            
            // Unique Request Code for each student + day combination
            val requestCode = student.id + SESSION_ALARM_OFFSET + (dayIndex + 1) * 100000
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val triggerTime = Calendar.getInstance().apply {
                set(Calendar.DAY_OF_WEEK, androidDayOfWeek)
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
                add(Calendar.MINUTE, -warningMinutes)
                if (before(Calendar.getInstance())) {
                    add(Calendar.WEEK_OF_YEAR, 1)
                }
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        triggerTime.timeInMillis,
                        pendingIntent
                    )
                } else {
                    alarmManager.setAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        triggerTime.timeInMillis,
                        pendingIntent
                    )
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime.timeInMillis,
                    pendingIntent
                )
            } else {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime.timeInMillis,
                    pendingIntent
                )
            }
        }
    }

    private fun getAndroidDayOfWeek(arabicDay: String): Int? {
        return when (arabicDay) {
            "السبت" -> Calendar.SATURDAY
            "الأحد" -> Calendar.SUNDAY
            "الاثنين" -> Calendar.MONDAY
            "الثلاثاء" -> Calendar.TUESDAY
            "الأربعاء" -> Calendar.WEDNESDAY
            "الخميس" -> Calendar.THURSDAY
            "الجمعة" -> Calendar.FRIDAY
            else -> null
        }
    }

    fun cancelSingleSessionAlarm(context: Context, studentId: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            action = NotificationReceiver.ACTION_SESSION_REMINDER
        }
        for (dayIndex in 0..6) {
            val requestCode = studentId + SESSION_ALARM_OFFSET + (dayIndex + 1) * 100000
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
            )
            if (pendingIntent != null) {
                alarmManager.cancel(pendingIntent)
                pendingIntent.cancel()
            }
        }
    }

    fun cancelAllAlarms(context: Context, students: List<Student>) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Cancel daily summary alarm
        val dailyIntent = Intent(context, NotificationReceiver::class.java).apply {
            action = NotificationReceiver.ACTION_DAILY_REMINDER
        }
        val dailyPendingIntent = PendingIntent.getBroadcast(
            context,
            DAILY_REMINDER_REQUEST_CODE,
            dailyIntent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        if (dailyPendingIntent != null) {
            alarmManager.cancel(dailyPendingIntent)
            dailyPendingIntent.cancel()
        }

        // Cancel all session alarms
        students.forEach { student ->
            cancelSingleSessionAlarm(context, student.id)
        }
    }
}
