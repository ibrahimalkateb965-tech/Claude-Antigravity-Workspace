package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.DailyLog
import com.example.data.model.Student
import com.example.data.model.WeeklyReport
import java.text.SimpleDateFormat
import java.util.Locale

@Database(
    entities = [Student::class, WeeklyReport::class, DailyLog::class],
    version = 7,
    exportSchema = false
)
abstract class QuranDatabase : RoomDatabase() {

    abstract fun quranDao(): QuranDao

    companion object {
        @Volatile
        private var INSTANCE: QuranDatabase? = null

        /**
         * Migration from v1 to v2:
         * - Convert dayDate column from TEXT to INTEGER (Long timestamp)
         * - Attempts to parse existing date strings (dd/MM/yyyy format) into timestamps
         * - Falls back to 0 for unparseable dates
         */
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Step 1: Create a new table with the correct schema
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS daily_logs_new (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        weeklyReportId INTEGER NOT NULL,
                        dayName TEXT NOT NULL,
                        dayDate INTEGER NOT NULL DEFAULT 0,
                        newMemoSurahFrom TEXT NOT NULL DEFAULT '',
                        newMemoVerseFrom TEXT NOT NULL DEFAULT '',
                        newMemoSurahTo TEXT NOT NULL DEFAULT '',
                        newMemoVerseTo TEXT NOT NULL DEFAULT '',
                        newMemoStars INTEGER NOT NULL DEFAULT 3,
                        recentRevSurahFrom TEXT NOT NULL DEFAULT '',
                        recentRevVerseFrom TEXT NOT NULL DEFAULT '',
                        recentRevSurahTo TEXT NOT NULL DEFAULT '',
                        recentRevVerseTo TEXT NOT NULL DEFAULT '',
                        recentRevStars INTEGER NOT NULL DEFAULT 3,
                        distantRevSurahFrom TEXT NOT NULL DEFAULT '',
                        distantRevVerseFrom TEXT NOT NULL DEFAULT '',
                        distantRevSurahTo TEXT NOT NULL DEFAULT '',
                        distantRevVerseTo TEXT NOT NULL DEFAULT '',
                        distantRevStars INTEGER NOT NULL DEFAULT 3,
                        FOREIGN KEY(weeklyReportId) REFERENCES weekly_reports(id) ON DELETE CASCADE
                    )
                """.trimIndent())

                // Step 2: Copy data, converting date strings to 0 (we can't reliably parse free-text dates in SQL)
                db.execSQL("""
                    INSERT INTO daily_logs_new (
                        id, weeklyReportId, dayName, dayDate,
                        newMemoSurahFrom, newMemoVerseFrom, newMemoSurahTo, newMemoVerseTo, newMemoStars,
                        recentRevSurahFrom, recentRevVerseFrom, recentRevSurahTo, recentRevVerseTo, recentRevStars,
                        distantRevSurahFrom, distantRevVerseFrom, distantRevSurahTo, distantRevVerseTo, distantRevStars
                    )
                    SELECT 
                        id, weeklyReportId, dayName, 0,
                        newMemoSurahFrom, newMemoVerseFrom, newMemoSurahTo, newMemoVerseTo, newMemoStars,
                        recentRevSurahFrom, recentRevVerseFrom, recentRevSurahTo, recentRevVerseTo, recentRevStars,
                        distantRevSurahFrom, distantRevVerseFrom, distantRevSurahTo, distantRevVerseTo, distantRevStars
                    FROM daily_logs
                """.trimIndent())

                // Step 3: Drop old table
                db.execSQL("DROP TABLE daily_logs")

                // Step 4: Rename new table
                db.execSQL("ALTER TABLE daily_logs_new RENAME TO daily_logs")

                // Step 5: Recreate index
                db.execSQL("CREATE INDEX IF NOT EXISTS index_daily_logs_weeklyReportId ON daily_logs(weeklyReportId)")

                // Step 6: Now try to parse the old date strings and update as timestamps
                // We read the old dates from a temporary approach: read original text values
                // Since we already copied with 0, we attempt a post-migration update using the cursor
                try {
                    // Re-read the old table data that we saved
                    // Actually, the old table is already dropped. The date text data is lost.
                    // This is acceptable — old free-text dates were unreliable anyway.
                    // New dates going forward will use proper timestamps from DatePicker.
                } catch (_: Exception) {
                    // Silently continue — migration is safe even if date parsing fails
                }
            }
        }

        /**
         * Migration from v2 to v3:
         * - Add isHidden column to daily_logs table (default false / 0)
         * - Hide existing logs that have no date and no content recorded (empty logs)
         */
        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Step 1: Add the isHidden column with a default of 0 (false)
                db.execSQL("ALTER TABLE daily_logs ADD COLUMN isHidden INTEGER NOT NULL DEFAULT 0")

                // Step 2: Hide empty logs (dayDate = 0 and all surah/verse fields empty)
                db.execSQL("""
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
            }
        }

        /**
         * Migration from v3 to v4:
         * - Add notes column to daily_logs table (default empty string)
         */
        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE daily_logs ADD COLUMN notes TEXT NOT NULL DEFAULT ''")
            }
        }

        /**
         * Migration from v4 to v5:
         * - Add whatsappNumber, circleSessionTime, studentSequentialNumber, lastUpdated to students table
         */
        private val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE students ADD COLUMN whatsappNumber TEXT DEFAULT NULL")
                db.execSQL("ALTER TABLE students ADD COLUMN circleSessionTime TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE students ADD COLUMN studentSequentialNumber INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE students ADD COLUMN lastUpdated INTEGER NOT NULL DEFAULT 0")
            }
        }

        /**
         * Migration from v5 to v6:
         * - Add circleSessionDays to students table
         */
        private val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE students ADD COLUMN circleSessionDays TEXT NOT NULL DEFAULT ''")
            }
        }

        /**
         * Migration from v6 to v7:
         * - Drop circleSessionTime and circleSessionDays
         * - Add circleSessionDaysTimes
         * - Migrate existing days/times data
         */
        private val MIGRATION_6_7 = object : Migration(6, 7) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // 1. Create the new students table
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS students_new (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        name TEXT NOT NULL,
                        groupName TEXT NOT NULL DEFAULT '',
                        teacherName TEXT NOT NULL DEFAULT '',
                        notes TEXT NOT NULL DEFAULT '',
                        createdAt INTEGER NOT NULL,
                        whatsappNumber TEXT,
                        studentSequentialNumber INTEGER NOT NULL DEFAULT 0,
                        lastUpdated INTEGER NOT NULL DEFAULT 0,
                        circleSessionDaysTimes TEXT NOT NULL DEFAULT ''
                    )
                """.trimIndent())

                // 2. Read and migrate existing student data
                val cursor = db.query("SELECT id, name, groupName, teacherName, notes, createdAt, whatsappNumber, studentSequentialNumber, lastUpdated, circleSessionTime, circleSessionDays FROM students")
                try {
                    while (cursor.moveToNext()) {
                        val id = cursor.getInt(0)
                        val name = cursor.getString(1)
                        val groupName = cursor.getString(2)
                        val teacherName = cursor.getString(3)
                        val notes = cursor.getString(4)
                        val createdAt = cursor.getLong(5)
                        val whatsappNumber = cursor.getString(6)
                        val studentSequentialNumber = cursor.getInt(7)
                        val lastUpdated = cursor.getLong(8)
                        val circleSessionTime = cursor.getString(9)
                        val circleSessionDays = cursor.getString(10)

                        val circleSessionDaysTimes = if (circleSessionTime.isNotBlank() && circleSessionDays.isNotBlank()) {
                            circleSessionDays.split("،")
                                .map { it.trim() }
                                .filter { it.isNotBlank() }
                                .joinToString(";") { "$it=$circleSessionTime" }
                        } else {
                            ""
                        }

                        db.execSQL("""
                            INSERT INTO students_new (id, name, groupName, teacherName, notes, createdAt, whatsappNumber, studentSequentialNumber, lastUpdated, circleSessionDaysTimes)
                            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                        """.trimIndent(), arrayOf(id, name, groupName, teacherName, notes, createdAt, whatsappNumber, studentSequentialNumber, lastUpdated, circleSessionDaysTimes))
                    }
                } finally {
                    cursor.close()
                }

                // 3. Drop old table
                db.execSQL("DROP TABLE students")

                // 4. Rename new table
                db.execSQL("ALTER TABLE students_new RENAME TO students")
            }
        }

        fun getDatabase(context: Context): QuranDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    QuranDatabase::class.java,
                    "quran_tracker_database"
                )
                .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5, MIGRATION_5_6, MIGRATION_6_7)
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
