package com.example.data.local

import androidx.room.*
import com.example.data.model.DailyLog
import com.example.data.model.Student
import com.example.data.model.WeeklyReport
import kotlinx.coroutines.flow.Flow

@Dao
interface QuranDao {

    // --- Student ---
    @Query("SELECT * FROM students ORDER BY name ASC")
    fun getAllStudents(): Flow<List<Student>>

    @Query("SELECT * FROM students WHERE id = :id LIMIT 1")
    fun getStudentById(id: Int): Flow<Student?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudent(student: Student): Long

    @Update
    suspend fun updateStudent(student: Student)

    @Delete
    suspend fun deleteStudent(student: Student)

    // --- WeeklyReport ---
    @Query("SELECT * FROM weekly_reports WHERE studentId = :studentId ORDER BY id DESC")
    fun getWeeklyReportsForStudent(studentId: Int): Flow<List<WeeklyReport>>

    @Query("SELECT * FROM weekly_reports WHERE id = :id LIMIT 1")
    fun getWeeklyReportById(id: Int): Flow<WeeklyReport?>

    @Query("SELECT * FROM weekly_reports")
    suspend fun getAllWeeklyReports(): List<WeeklyReport>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeeklyReport(report: WeeklyReport): Long

    @Update
    suspend fun updateWeeklyReport(report: WeeklyReport)

    @Delete
    suspend fun deleteWeeklyReport(report: WeeklyReport)

    // --- DailyLog ---
    @Query("SELECT * FROM daily_logs WHERE weeklyReportId = :weeklyReportId AND isHidden = 0 ORDER BY id ASC")
    fun getDailyLogsForReport(weeklyReportId: Int): Flow<List<DailyLog>>

    @Query("SELECT COUNT(*) FROM daily_logs WHERE weeklyReportId = :weeklyReportId AND isHidden = 0")
    suspend fun getVisibleDayCount(weeklyReportId: Int): Int

    @Delete
    suspend fun deleteDailyLog(log: DailyLog)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyLog(log: DailyLog): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyLogs(logs: List<DailyLog>)

    @Update
    suspend fun updateDailyLog(log: DailyLog)

    @Query("DELETE FROM daily_logs WHERE weeklyReportId = :weeklyReportId")
    suspend fun deleteDailyLogsForReport(weeklyReportId: Int)

    // --- Date Duplicate Checks ---
    @Query("SELECT COUNT(*) FROM daily_logs WHERE weeklyReportId = :weeklyReportId AND dayDate = :date AND id != :excludeId AND dayDate != 0 AND isHidden = 0")
    suspend fun countLogsWithDateInWeek(weeklyReportId: Int, date: Long, excludeId: Int): Int

    @Query("""
        SELECT COUNT(*) FROM daily_logs dl 
        INNER JOIN weekly_reports wr ON dl.weeklyReportId = wr.id 
        WHERE wr.studentId = :studentId AND dl.dayDate = :date AND dl.id != :excludeId AND dl.dayDate != 0 AND dl.isHidden = 0
    """)
    suspend fun countLogsWithDateForStudent(studentId: Int, date: Long, excludeId: Int): Int

    @Query("""
        SELECT dl.* FROM daily_logs dl 
        INNER JOIN weekly_reports wr ON dl.weeklyReportId = wr.id 
        WHERE wr.studentId = :studentId AND dl.dayDate BETWEEN :startDate AND :endDate AND dl.isHidden = 0
        ORDER BY dl.dayDate ASC
    """)
    suspend fun getDailyLogsForStudentInPeriod(studentId: Int, startDate: Long, endDate: Long): List<DailyLog>

    @Query("""
        SELECT dl.* FROM daily_logs dl 
        INNER JOIN weekly_reports wr ON dl.weeklyReportId = wr.id
        INNER JOIN students s ON wr.studentId = s.id
        WHERE s.groupName = :groupName AND dl.dayDate BETWEEN :startDate AND :endDate AND dl.isHidden = 0
        ORDER BY s.name ASC, dl.dayDate ASC
    """)
    suspend fun getDailyLogsForGroupInPeriod(groupName: String, startDate: Long, endDate: Long): List<DailyLog>

    // --- Backup Queries ---
    @Query("SELECT * FROM students")
    suspend fun getAllStudentsList(): List<Student>

    @Query("SELECT * FROM daily_logs")
    suspend fun getAllDailyLogs(): List<DailyLog>

    @Query("DELETE FROM daily_logs")
    suspend fun deleteAllDailyLogs()

    @Query("DELETE FROM weekly_reports")
    suspend fun deleteAllWeeklyReports()

    @Query("DELETE FROM students")
    suspend fun deleteAllStudents()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudents(students: List<Student>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeeklyReports(reports: List<WeeklyReport>)

    @Query("SELECT MAX(studentSequentialNumber) FROM students WHERE groupName = :groupName")
    suspend fun getMaxSequentialNumberInGroup(groupName: String): Int?

    @Query("SELECT COUNT(*) FROM students WHERE groupName = :groupName AND whatsappNumber = :whatsappNumber AND id != :excludeId")
    suspend fun countStudentsWithWhatsappInGroup(groupName: String, whatsappNumber: String, excludeId: Int): Int

    @Query("SELECT * FROM students WHERE id = :id LIMIT 1")
    suspend fun getStudentByIdDirect(id: Int): Student?
}
