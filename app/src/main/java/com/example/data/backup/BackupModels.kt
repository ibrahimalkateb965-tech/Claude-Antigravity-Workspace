package com.example.data.backup

import com.example.data.model.DailyLog
import com.example.data.model.Student
import com.example.data.model.WeeklyReport
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BackupFile(
    val metadata: BackupMetadata,
    val students: List<StudentBackup>,
    val weeklyReports: List<WeeklyReportBackup>,
    val dailyLogs: List<DailyLogBackup>
)

@JsonClass(generateAdapter = true)
data class BackupMetadata(
    val version: String = "1.0",
    val appVersion: String = "تيجان النور v2.0",
    val appBuild: Int = 1,
    val databaseSchema: String = "v4",
    val exportDate: String,
    val timestamp: Long,
    val recordCount: RecordCount,
    val fileSize: Long = 0,
    val exportedBy: String, // "manual" | "auto"
    val notes: String? = null
)

@JsonClass(generateAdapter = true)
data class RecordCount(
    val students: Int,
    val weeklyReports: Int,
    val dailyLogs: Int
)

// Backup-safe copies of entities (using simple types for JSON serialization)
@JsonClass(generateAdapter = true)
data class StudentBackup(
    val id: Int,
    val name: String,
    val groupName: String,
    val teacherName: String,
    val notes: String,
    val createdAt: Long,
    val whatsappNumber: String? = null,
    val studentSequentialNumber: Int = 0,
    val lastUpdated: Long = 0L,
    val circleSessionDaysTimes: String = ""
)

@JsonClass(generateAdapter = true)
data class WeeklyReportBackup(
    val id: Int,
    val studentId: Int,
    val weekName: String,
    val teacherFeedback: String
)

@JsonClass(generateAdapter = true)
data class DailyLogBackup(
    val id: Int,
    val weeklyReportId: Int,
    val dayName: String,
    val dayDate: Long,
    val isHidden: Boolean,
    val newMemoSurahFrom: String,
    val newMemoVerseFrom: String,
    val newMemoSurahTo: String,
    val newMemoVerseTo: String,
    val newMemoStars: Int,
    val recentRevSurahFrom: String,
    val recentRevVerseFrom: String,
    val recentRevSurahTo: String,
    val recentRevVerseTo: String,
    val recentRevStars: Int,
    val distantRevSurahFrom: String,
    val distantRevVerseFrom: String,
    val distantRevSurahTo: String,
    val distantRevVerseTo: String,
    val distantRevStars: Int,
    val notes: String
)

// Conversion extensions
fun Student.toBackup() = StudentBackup(
    id = id,
    name = name,
    groupName = groupName,
    teacherName = teacherName,
    notes = notes,
    createdAt = createdAt,
    whatsappNumber = whatsappNumber,
    studentSequentialNumber = studentSequentialNumber,
    lastUpdated = lastUpdated,
    circleSessionDaysTimes = circleSessionDaysTimes
)
fun WeeklyReport.toBackup() = WeeklyReportBackup(id, studentId, weekName, teacherFeedback)
fun DailyLog.toBackup() = DailyLogBackup(
    id, weeklyReportId, dayName, dayDate, isHidden,
    newMemoSurahFrom, newMemoVerseFrom, newMemoSurahTo, newMemoVerseTo, newMemoStars,
    recentRevSurahFrom, recentRevVerseFrom, recentRevSurahTo, recentRevVerseTo, recentRevStars,
    distantRevSurahFrom, distantRevVerseFrom, distantRevSurahTo, distantRevVerseTo, distantRevStars,
    notes
)

fun StudentBackup.toEntity() = Student(
    id = id,
    name = name,
    groupName = groupName,
    teacherName = teacherName,
    notes = notes,
    createdAt = createdAt,
    whatsappNumber = whatsappNumber,
    studentSequentialNumber = studentSequentialNumber,
    lastUpdated = if (lastUpdated == 0L) createdAt else lastUpdated,
    circleSessionDaysTimes = circleSessionDaysTimes
)
fun WeeklyReportBackup.toEntity() = WeeklyReport(id, studentId, weekName, teacherFeedback)
fun DailyLogBackup.toEntity() = DailyLog(
    id, weeklyReportId, dayName, dayDate, isHidden,
    newMemoSurahFrom, newMemoVerseFrom, newMemoSurahTo, newMemoVerseTo, newMemoStars,
    recentRevSurahFrom, recentRevVerseFrom, recentRevSurahTo, recentRevVerseTo, recentRevStars,
    distantRevSurahFrom, distantRevVerseFrom, distantRevSurahTo, distantRevVerseTo, distantRevStars,
    notes
)

// Result types
data class ExportResult(
    val success: Boolean,
    val fileName: String? = null,
    val filePath: String? = null,
    val recordCount: RecordCount? = null,
    val fileSize: Long = 0,
    val exportDate: String? = null,
    val error: String? = null
)

data class ImportResult(
    val success: Boolean,
    val recordsRestored: RecordCount? = null,
    val restoredDate: String? = null,
    val importedFromFile: String? = null,
    val error: String? = null
)

data class BackupValidation(
    val isValid: Boolean,
    val checks: Map<String, Boolean>,
    val errors: List<String>,
    val warnings: List<String>,
    val recordCount: RecordCount? = null,
    val exportDate: String? = null
)

data class BackupFileInfo(
    val fileName: String,
    val filePath: String,
    val fileSize: Long,
    val exportDate: String?,
    val timestamp: Long,
    val recordCount: RecordCount?,
    val exportedBy: String?
)

@JsonClass(generateAdapter = true)
data class BackupLogData(
    val backups: MutableList<BackupLogEntry> = mutableListOf()
)

@JsonClass(generateAdapter = true)
data class BackupLogEntry(
    val fileName: String,
    val timestamp: Long,
    val date: String,
    val type: String, // "auto" | "manual"
    val status: String, // "success" | "failed"
    val recordCount: RecordCount? = null,
    val fileSize: Long = 0,
    val error: String? = null,
    val notes: String? = null
)
