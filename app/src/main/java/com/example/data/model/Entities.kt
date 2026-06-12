package com.example.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "students")
data class Student(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val groupName: String = "",
    val teacherName: String = "",
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "weekly_reports",
    foreignKeys = [
        ForeignKey(
            entity = Student::class,
            parentColumns = ["id"],
            childColumns = ["studentId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["studentId"])]
)
data class WeeklyReport(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val studentId: Int,
    val weekName: String, // e.g. "الأسبوع الأول - يناير"
    val teacherFeedback: String = "" // ملاحظة المربي وتشجيعه للأسبوع
)

@Entity(
    tableName = "daily_logs",
    foreignKeys = [
        ForeignKey(
            entity = WeeklyReport::class,
            parentColumns = ["id"],
            childColumns = ["weeklyReportId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["weeklyReportId"])]
)
data class DailyLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val weeklyReportId: Int,
    val dayName: String, // السبت، الأحد، الإثنين، الثلاثاء، الأربعاء، الخميس
    val dayDate: String = "", // التاريخ الخاص باليوم مثلاً "2026-06-12" أو "12 يونيو"
    
    // الحفظ الجديد
    val newMemoSurahFrom: String = "",
    val newMemoVerseFrom: String = "",
    val newMemoSurahTo: String = "",
    val newMemoVerseTo: String = "",
    val newMemoStars: Int = 3, // 0 = default empty/not attempted, 1 = ⭐, 2 = ⭐⭐, 3 = ⭐⭐⭐, 4 = ❌
    
    // الماضي القريب
    val recentRevSurahFrom: String = "",
    val recentRevVerseFrom: String = "",
    val recentRevSurahTo: String = "",
    val recentRevVerseTo: String = "",
    val recentRevStars: Int = 3,
    
    // الماضي البعيد
    val distantRevSurahFrom: String = "",
    val distantRevVerseFrom: String = "",
    val distantRevSurahTo: String = "",
    val distantRevVerseTo: String = "",
    val distantRevStars: Int = 3
)
