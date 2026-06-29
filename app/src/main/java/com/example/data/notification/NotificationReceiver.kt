package com.example.data.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.data.backup.AppPreferences
import com.example.data.local.QuranDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class NotificationReceiver : BroadcastReceiver() {

    companion object {
        const val ACTION_DAILY_REMINDER = "com.example.ACTION_DAILY_REMINDER"
        const val ACTION_SESSION_REMINDER = "com.example.ACTION_SESSION_REMINDER"
        const val BOOT_ACTION = "android.intent.action.BOOT_COMPLETED"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val appPrefs = AppPreferences(context)
        if (!appPrefs.notificationsEnabled && intent.action != BOOT_ACTION) return

        val database = QuranDatabase.getDatabase(context)
        val dao = database.quranDao()

        when (intent.action) {
            BOOT_ACTION -> {
                // Reschedule all alarms on boot
                val pendingResult = goAsync()
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val students = dao.getAllStudentsList()
                        AlarmScheduler.scheduleAllAlarms(context, students)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    } finally {
                        pendingResult.finish()
                    }
                }
            }
            ACTION_DAILY_REMINDER -> {
                // Show daily reminder
                val pendingResult = goAsync()
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val students = dao.getAllStudentsList()
                        val calendar = java.util.Calendar.getInstance()
                        val arabicDayToday = when (calendar.get(java.util.Calendar.DAY_OF_WEEK)) {
                            java.util.Calendar.SATURDAY -> "السبت"
                            java.util.Calendar.SUNDAY -> "الأحد"
                            java.util.Calendar.MONDAY -> "الاثنين"
                            java.util.Calendar.TUESDAY -> "الثلاثاء"
                            java.util.Calendar.WEDNESDAY -> "الأربعاء"
                            java.util.Calendar.THURSDAY -> "الخميس"
                            java.util.Calendar.FRIDAY -> "الجمعة"
                            else -> ""
                        }
                        val activeSessionsCount = students.filter { student ->
                            student.circleSessionDaysTimes.split(";")
                                .filter { it.isNotBlank() }
                                .any { it.startsWith("$arabicDayToday=") }
                        }.size
                        if (activeSessionsCount > 0) {
                            val lang = appPrefs.appLanguage
                            if (lang == "en") {
                                NotificationHelper.sendNotification(
                                    context = context,
                                    id = DAILY_REMINDER_REQUEST_CODE_NOTIFICATION,
                                    title = "📖 Today's Quran Circle",
                                    text = "You have $activeSessionsCount scheduled circle(s) today. May Allah bless your efforts."
                                )
                            } else {
                                NotificationHelper.sendNotification(
                                    context = context,
                                    id = DAILY_REMINDER_REQUEST_CODE_NOTIFICATION,
                                    title = "📖 حلقة تحفيظ قرآن اليوم",
                                    text = "لديك اليوم $activeSessionsCount حلقة مجدولة للتحفيظ والمتابعة. بارك الله في جهودكم."
                                )
                            }
                        }
                        // Reschedule next exact alarms
                        AlarmScheduler.scheduleAllAlarms(context, students)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    } finally {
                        pendingResult.finish()
                    }
                }
            }
            ACTION_SESSION_REMINDER -> {
                val studentId = intent.getIntExtra("studentId", -1)
                val studentName = intent.getStringExtra("studentName") ?: return
                val groupName = intent.getStringExtra("groupName") ?: ""
                val sessionTime = intent.getStringExtra("sessionTime") ?: ""

                // Verify circle is still enabled
                if (groupName.isNotBlank() && !appPrefs.isSessionEnabledForGroup(groupName)) {
                    return
                }

                val pendingResult = goAsync()
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val lang = appPrefs.appLanguage
                        val timeFormatted = formatTime12h(sessionTime, lang)
                        if (lang == "en") {
                            NotificationHelper.sendNotification(
                                context = context,
                                id = studentId + AlarmScheduler.SESSION_ALARM_OFFSET,
                                title = "⏰ Circle time approaching for $studentName",
                                text = "The memorization circle ($groupName) starts at $timeFormatted. Keep up the great work!"
                            )
                        } else {
                            NotificationHelper.sendNotification(
                                context = context,
                                id = studentId + AlarmScheduler.SESSION_ALARM_OFFSET,
                                title = "⏰ اقترب موعد الحلقة للبطَل $studentName",
                                text = "تبدأ حلقة التحفيظ (حلقة $groupName) في تمام الساعة $timeFormatted. واصل تميزك!"
                            )
                        }
                        val students = dao.getAllStudentsList()
                        // Reschedule next exact alarms
                        AlarmScheduler.scheduleAllAlarms(context, students)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    } finally {
                        pendingResult.finish()
                    }
                }
            }
        }
    }

    private fun formatTime12h(timeStr: String, lang: String): String {
        if (timeStr.isBlank()) return "-"
        return try {
            val sdf24 = SimpleDateFormat("HH:mm", Locale.US)
            val locale = if (lang == "en") Locale.US else Locale("ar")
            val sdf12 = SimpleDateFormat("hh:mm a", locale)
            val date = sdf24.parse(timeStr)
            if (date != null) {
                val formatted = sdf12.format(date)
                if (lang != "en") {
                    formatted.replace("AM", "ص").replace("PM", "م")
                } else {
                    formatted
                }
            } else {
                timeStr
            }
        } catch (e: Exception) {
            timeStr
        }
    }

    private val DAILY_REMINDER_REQUEST_CODE_NOTIFICATION = 2002
}
