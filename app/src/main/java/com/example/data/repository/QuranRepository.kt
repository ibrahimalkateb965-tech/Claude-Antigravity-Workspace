package com.example.data.repository

import com.example.data.local.QuranDao
import com.example.data.model.DailyLog
import com.example.data.model.Student
import com.example.data.model.WeeklyReport
import kotlinx.coroutines.flow.Flow

class QuranRepository(private val quranDao: QuranDao) {

    val allStudents: Flow<List<Student>> = quranDao.getAllStudents()

    fun getStudentById(id: Int): Flow<Student?> = quranDao.getStudentById(id)

    suspend fun insertStudent(student: Student): Long {
        return quranDao.insertStudent(student)
    }

    suspend fun updateStudent(student: Student) {
        quranDao.updateStudent(student)
    }

    suspend fun deleteStudent(student: Student) {
        quranDao.deleteStudent(student)
    }

    fun getWeeklyReportsForStudent(studentId: Int): Flow<List<WeeklyReport>> {
        return quranDao.getWeeklyReportsForStudent(studentId)
    }

    fun getWeeklyReportById(id: Int): Flow<WeeklyReport?> {
        return quranDao.getWeeklyReportById(id)
    }

    /**
     * Creates a weekly report for a student and automatically seeds it with the 6 standard study days:
     * Saturday, Sunday, Monday, Tuesday, Wednesday, Thursday
     */
    suspend fun createWeeklyReport(studentId: Int, weekName: String): Long {
        val report = WeeklyReport(studentId = studentId, weekName = weekName)
        val reportId = quranDao.insertWeeklyReport(report).toInt()
        
        val days = listOf("السبت", "الأحد", "الإثنين", "الثلاثاء", "الأربعاء", "الخميس")
        val logs = days.map { day ->
            DailyLog(
                weeklyReportId = reportId,
                dayName = day,
                newMemoStars = 3, // Default Excellent/⭐⭐⭐
                recentRevStars = 3,
                distantRevStars = 3
            )
        }
        quranDao.insertDailyLogs(logs)
        return reportId.toLong()
    }

    suspend fun updateWeeklyReport(report: WeeklyReport) {
        quranDao.updateWeeklyReport(report)
    }

    suspend fun deleteWeeklyReport(report: WeeklyReport) {
        quranDao.deleteWeeklyReport(report)
    }

    fun getDailyLogsForReport(weeklyReportId: Int): Flow<List<DailyLog>> {
        return quranDao.getDailyLogsForReport(weeklyReportId)
    }

    suspend fun updateDailyLog(log: DailyLog) {
        quranDao.updateDailyLog(log)
    }
}
