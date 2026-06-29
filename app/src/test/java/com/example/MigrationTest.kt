package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.QuranDatabase
import com.example.data.model.DailyLog
import com.example.data.model.Student
import com.example.data.model.WeeklyReport
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class MigrationTest {

    @Test
    fun testMigration2to3() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        
        // Build database in memory
        val db = Room.inMemoryDatabaseBuilder(context, QuranDatabase::class.java).build()
        val dao = db.quranDao()
        
        val studentId = dao.insertStudent(Student(name = "محمد")).toInt()
        val reportId = dao.insertWeeklyReport(WeeklyReport(studentId = studentId, weekName = "الأسبوع الأول")).toInt()
        
        val log1 = DailyLog(weeklyReportId = reportId, dayName = "السبت", dayDate = 0L)
        val log2 = DailyLog(weeklyReportId = reportId, dayName = "الأحد", dayDate = 1718524800000L) // has date
        val log3 = DailyLog(weeklyReportId = reportId, dayName = "الإثنين", dayDate = 0L, newMemoSurahFrom = "البقرة") // has content
        val log4 = DailyLog(weeklyReportId = reportId, dayName = "الثلاثاء", dayDate = 1718524800000L, newMemoSurahFrom = "آل عمران") // both
        
        dao.insertDailyLogs(listOf(log1, log2, log3, log4))
        
        // Execute the exact SQL query from MIGRATION_2_3 to simulate what it does on database update
        db.openHelper.writableDatabase.execSQL("""
            UPDATE daily_logs 
            SET isHidden = 1 
            WHERE dayDate = 0 
              AND (newMemoSurahFrom IS NULL OR newMemoSurahFrom = '') 
              AND (newMemoSurahTo IS NULL OR newMemoSurahTo = '')
              AND (newMemoVerseFrom IS NULL OR newMemoVerseFrom = '')
              AND (newMemoVerseTo IS NULL OR newMemoVerseTo = '')
              AND (recentRevSurahFrom IS NULL OR recentRevSurahFrom = '')
              AND (recentRevSurahTo IS NULL OR recentRevSurahTo = '')
              AND (recentRevVerseFrom IS NULL OR recentRevVerseFrom = '')
              AND (recentRevVerseTo IS NULL OR recentRevVerseTo = '')
              AND (distantRevSurahFrom IS NULL OR distantRevSurahFrom = '')
              AND (distantRevSurahTo IS NULL OR distantRevSurahTo = '')
              AND (distantRevVerseFrom IS NULL OR distantRevVerseFrom = '')
              AND (distantRevVerseTo IS NULL OR distantRevVerseTo = '')
        """.trimIndent())
        
        // Query visible logs
        val visibleLogs = dao.getDailyLogsForReport(reportId).first()
        
        // Only log2, log3, and log4 should be visible. log1 should be hidden!
        assertEquals(3, visibleLogs.size)
        assertTrue(visibleLogs.any { it.dayName == "الأحد" })
        assertTrue(visibleLogs.any { it.dayName == "الإثنين" })
        assertTrue(visibleLogs.any { it.dayName == "الثلاثاء" })
        assertFalse(visibleLogs.any { it.dayName == "السبت" })
        
        db.close()
    }
}
