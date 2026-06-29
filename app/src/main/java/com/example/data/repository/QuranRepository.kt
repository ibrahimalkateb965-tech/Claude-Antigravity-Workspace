package com.example.data.repository

import com.example.data.local.QuranDao
import com.example.data.model.DailyLog
import com.example.data.model.Student
import com.example.data.model.WeeklyReport
import com.example.ui.screen.loc
import kotlinx.coroutines.flow.Flow

class QuranRepository(private val quranDao: QuranDao) {

    val allStudents: Flow<List<Student>> = quranDao.getAllStudents()

    fun getStudentById(id: Int): Flow<Student?> = quranDao.getStudentById(id)

    suspend fun insertStudent(student: Student): Long {
        val maxSeq = quranDao.getMaxSequentialNumberInGroup(student.groupName) ?: 0
        val studentWithSeq = student.copy(
            studentSequentialNumber = maxSeq + 1,
            lastUpdated = System.currentTimeMillis()
        )
        return quranDao.insertStudent(studentWithSeq)
    }

    suspend fun updateStudent(student: Student) {
        val old = quranDao.getStudentByIdDirect(student.id)
        val studentWithUpdates = if (old != null && old.groupName != student.groupName) {
            val maxSeq = quranDao.getMaxSequentialNumberInGroup(student.groupName) ?: 0
            student.copy(
                studentSequentialNumber = maxSeq + 1,
                lastUpdated = System.currentTimeMillis()
            )
        } else {
            student.copy(lastUpdated = System.currentTimeMillis())
        }
        quranDao.updateStudent(studentWithUpdates)
    }

    suspend fun deleteStudent(student: Student) {
        quranDao.deleteStudent(student)
    }

    suspend fun countStudentsWithWhatsappInGroup(groupName: String, whatsappNumber: String, excludeId: Int): Int {
        return quranDao.countStudentsWithWhatsappInGroup(groupName, whatsappNumber, excludeId)
    }

    suspend fun getMaxSequentialNumberInGroup(groupName: String): Int? {
        return quranDao.getMaxSequentialNumberInGroup(groupName)
    }

    suspend fun getStudentByIdDirect(id: Int): Student? {
        return quranDao.getStudentByIdDirect(id)
    }

    fun getWeeklyReportsForStudent(studentId: Int): Flow<List<WeeklyReport>> {
        return quranDao.getWeeklyReportsForStudent(studentId)
    }

    fun getWeeklyReportById(id: Int): Flow<WeeklyReport?> {
        return quranDao.getWeeklyReportById(id)
    }

    /**
     * Creates a weekly report for a student.
     * The report starts empty (no seeded days).
     */
    suspend fun createWeeklyReport(studentId: Int, weekName: String): Long {
        val report = WeeklyReport(studentId = studentId, weekName = weekName)
        return quranDao.insertWeeklyReport(report)
    }

    suspend fun updateWeeklyReport(report: WeeklyReport) {
        quranDao.updateWeeklyReport(report)
    }

    suspend fun deleteWeeklyReport(report: WeeklyReport) {
        quranDao.deleteWeeklyReport(report)
    }

    suspend fun getAllWeeklyReports(): List<WeeklyReport> {
        return quranDao.getAllWeeklyReports()
    }

    fun getDailyLogsForReport(weeklyReportId: Int): Flow<List<DailyLog>> {
        return quranDao.getDailyLogsForReport(weeklyReportId)
    }

    suspend fun updateDailyLog(log: DailyLog) {
        quranDao.updateDailyLog(log)
    }

    suspend fun deleteDailyLog(log: DailyLog) {
        quranDao.deleteDailyLog(log)
        // Recalculate sequential numbers after deletion
        val studentId = getStudentIdForLog(log)
        if (studentId != null) recalculateDayNumbers(studentId)
    }

    /**
     * Recalculates sequential day numbers for all non-absent present days of a student,
     * ordered by dayDate. Absent days always get 0.
     */
    suspend fun recalculateDayNumbers(studentId: Int) {
        // Clear absent days sequential numbers
        quranDao.clearAbsentDaySequentialNumbers(studentId)
        // Get all present, visible logs sorted by date
        val logIds = quranDao.getVisiblePresentLogIdsSortedByDate(studentId)
        logIds.forEachIndexed { index, logId ->
            quranDao.updateDaySequentialNumber(logId, index + 1)
        }
    }

    /**
     * Looks up the studentId that owns a DailyLog (via weekly_reports join).
     */
    private suspend fun getStudentIdForLog(log: DailyLog): Int? {
        return quranDao.getAllWeeklyReports().firstOrNull { it.id == log.weeklyReportId }?.studentId
    }

    /**
     * Adds a new day log to a weekly report.
     * Infers the day name in Arabic from the timestamp.
     * Returns null on success, or an error message if limit reached or date duplicated.
     */
    suspend fun addDayToWeek(weeklyReportId: Int, studentId: Int, dayDate: Long, notes: String = ""): String? {
        val count = quranDao.getVisibleDayCount(weeklyReportId)
        if (count >= 7) {
            return "لا يمكن إضافة أكثر من 7 أيام في الأسبوع الواحد".loc()
        }

        val duplicateCheck = checkDateAvailability(weeklyReportId, studentId, dayDate)
        if (duplicateCheck != null) {
            return duplicateCheck
        }

        val sdf = java.text.SimpleDateFormat("EEEE", java.util.Locale("ar"))
        val dayName = try {
            sdf.format(java.util.Date(dayDate))
        } catch (e: Exception) {
            "اليوم".loc()
        }

        val log = DailyLog(
            weeklyReportId = weeklyReportId,
            dayName = dayName,
            dayDate = dayDate,
            newMemoStars = 3,
            recentRevStars = 3,
            distantRevStars = 3,
            notes = notes
        )
        quranDao.insertDailyLog(log)
        // Recalculate sequential numbers for this student
        recalculateDayNumbers(studentId)
        return null
    }

    /**
     * Checks if a date is available (not duplicated) for a given week and student.
     * Returns null if available, or an error message if duplicated.
     */
    suspend fun checkDateAvailability(
        weeklyReportId: Int,
        studentId: Int,
        date: Long,
        excludeLogId: Int = 0
    ): String? {
        if (date == 0L) return null // No date set, skip check

        val weekDuplicates = quranDao.countLogsWithDateInWeek(weeklyReportId, date, excludeLogId)
        if (weekDuplicates > 0) return "هذا التاريخ مسجل بالفعل في هذا الأسبوع".loc()

        val studentDuplicates = quranDao.countLogsWithDateForStudent(studentId, date, excludeLogId)
        if (studentDuplicates > 0) return "هذا التاريخ مسجل بالفعل في أسبوع آخر لنفس الطالب".loc()

        return null
    }

    suspend fun getDailyLogsForStudentInPeriod(studentId: Int, startDate: Long, endDate: Long): List<DailyLog> {
        return quranDao.getDailyLogsForStudentInPeriod(studentId, startDate, endDate)
    }

    suspend fun getDailyLogsForGroupInPeriod(groupName: String, startDate: Long, endDate: Long): List<DailyLog> {
        return quranDao.getDailyLogsForGroupInPeriod(groupName, startDate, endDate)
    }
}
