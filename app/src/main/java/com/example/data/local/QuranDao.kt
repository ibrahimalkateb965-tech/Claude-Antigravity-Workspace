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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeeklyReport(report: WeeklyReport): Long

    @Update
    suspend fun updateWeeklyReport(report: WeeklyReport)

    @Delete
    suspend fun deleteWeeklyReport(report: WeeklyReport)

    // --- DailyLog ---
    @Query("SELECT * FROM daily_logs WHERE weeklyReportId = :weeklyReportId ORDER BY id ASC")
    fun getDailyLogsForReport(weeklyReportId: Int): Flow<List<DailyLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyLog(log: DailyLog): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyLogs(logs: List<DailyLog>)

    @Update
    suspend fun updateDailyLog(log: DailyLog)

    @Query("DELETE FROM daily_logs WHERE weeklyReportId = :weeklyReportId")
    suspend fun deleteDailyLogsForReport(weeklyReportId: Int)
}
