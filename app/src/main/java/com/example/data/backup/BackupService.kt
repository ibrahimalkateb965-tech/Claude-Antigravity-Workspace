package com.example.data.backup

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.room.withTransaction
import com.example.data.local.QuranDatabase
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class BackupService(private val context: Context) {

    private val database: QuranDatabase by lazy { QuranDatabase.getDatabase(context) }
    private val dao by lazy { database.quranDao() }
    private val moshi: Moshi by lazy {
        Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
    }

    companion object {
        const val BACKUP_VERSION = "1.0"
        const val APP_VERSION = "تيجان النور v2.0"
        const val DB_SCHEMA_VERSION = "v7"
        const val BACKUP_FILE_PREFIX = "tijan-alnoor-backup-"
        const val BACKUP_FILE_EXTENSION = ".json"
    }

    // ==========================================
    // EXPORT
    // ==========================================
    suspend fun exportBackup(exportedBy: String = "manual", notes: String? = null): ExportResult {
        return try {
            // 1. Collect all data
            val students = dao.getAllStudentsList()
            val weeklyReports = dao.getAllWeeklyReports()
            val dailyLogs = dao.getAllDailyLogs()

            val timestamp = System.currentTimeMillis()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
            dateFormat.timeZone = TimeZone.getTimeZone("UTC")
            val exportDate = dateFormat.format(Date(timestamp))

            val recordCount = RecordCount(
                students = students.size,
                weeklyReports = weeklyReports.size,
                dailyLogs = dailyLogs.size
            )

            // 2. Build backup file
            val backupFile = BackupFile(
                metadata = BackupMetadata(
                    version = BACKUP_VERSION,
                    appVersion = APP_VERSION,
                    databaseSchema = DB_SCHEMA_VERSION,
                    exportDate = exportDate,
                    timestamp = timestamp,
                    recordCount = recordCount,
                    exportedBy = exportedBy,
                    notes = notes
                ),
                students = students.map { it.toBackup() },
                weeklyReports = weeklyReports.map { it.toBackup() },
                dailyLogs = dailyLogs.map { it.toBackup() }
            )

            // 3. Serialize to JSON
            val adapter = moshi.adapter(BackupFile::class.java).indent("  ")
            val jsonString = adapter.toJson(backupFile)
            val jsonBytes = jsonString.toByteArray(Charsets.UTF_8)

            // 4. Save to Downloads
            val fileName = "$BACKUP_FILE_PREFIX$timestamp$BACKUP_FILE_EXTENSION"
            val filePath = saveToDownloads(fileName, jsonBytes)

            // 5. Log the backup
            logBackup(BackupLogEntry(
                fileName = fileName,
                timestamp = timestamp,
                date = exportDate,
                type = exportedBy,
                status = "success",
                recordCount = recordCount,
                fileSize = jsonBytes.size.toLong(),
                notes = notes
            ))

            ExportResult(
                success = true,
                fileName = fileName,
                filePath = filePath,
                recordCount = recordCount,
                fileSize = jsonBytes.size.toLong(),
                exportDate = exportDate
            )
        } catch (e: Exception) {
            // Log failure
            try {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
                dateFormat.timeZone = TimeZone.getTimeZone("UTC")
                logBackup(BackupLogEntry(
                    fileName = "",
                    timestamp = System.currentTimeMillis(),
                    date = dateFormat.format(Date()),
                    type = exportedBy,
                    status = "failed",
                    error = e.message
                ))
            } catch (_: Exception) {}

            ExportResult(
                success = false,
                error = "فشل التصدير: ${e.message}"
            )
        }
    }

    // ==========================================
    // IMPORT
    // ==========================================
    suspend fun importBackup(uri: Uri): ImportResult {
        return try {
            // 1. Read file
            val jsonString = context.contentResolver.openInputStream(uri)?.bufferedReader()?.use {
                it.readText()
            } ?: return ImportResult(success = false, error = "لا يمكن قراءة الملف المحدد")

            // 2. Validate
            val validation = validateBackup(jsonString)
            if (!validation.isValid) {
                return ImportResult(
                    success = false,
                    error = "الملف غير صالح:\n${validation.errors.joinToString("\n")}"
                )
            }

            // 3. Parse
            val adapter = moshi.adapter(BackupFile::class.java)
            val backupFile = adapter.fromJson(jsonString)
                ?: return ImportResult(success = false, error = "فشل تحليل محتوى الملف")

            // 4. Import inside a transaction (with rollback on failure)
            database.withTransaction {
                // Clear existing data (order matters for foreign keys)
                dao.deleteAllDailyLogs()
                dao.deleteAllWeeklyReports()
                dao.deleteAllStudents()

                // Insert new data (order matters for foreign keys)
                dao.insertStudents(backupFile.students.map { it.toEntity() })
                dao.insertWeeklyReports(backupFile.weeklyReports.map { it.toEntity() })
                dao.insertDailyLogs(backupFile.dailyLogs.map { it.toEntity() })
            }

            val recordCount = RecordCount(
                students = backupFile.students.size,
                weeklyReports = backupFile.weeklyReports.size,
                dailyLogs = backupFile.dailyLogs.size
            )

            // 5. Log import
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
            dateFormat.timeZone = TimeZone.getTimeZone("UTC")
            logBackup(BackupLogEntry(
                fileName = uri.lastPathSegment ?: "unknown",
                timestamp = System.currentTimeMillis(),
                date = dateFormat.format(Date()),
                type = "import",
                status = "success",
                recordCount = recordCount
            ))

            ImportResult(
                success = true,
                recordsRestored = recordCount,
                restoredDate = backupFile.metadata.exportDate,
                importedFromFile = uri.lastPathSegment
            )
        } catch (e: Exception) {
            ImportResult(
                success = false,
                error = "فشل الاستيراد: ${e.message}"
            )
        }
    }

    // ==========================================
    // VALIDATION
    // ==========================================
    fun validateBackup(jsonString: String): BackupValidation {
        val checks = mutableMapOf<String, Boolean>()
        val errors = mutableListOf<String>()
        val warnings = mutableListOf<String>()

        // 1. File readable
        checks["fileReadable"] = jsonString.isNotBlank()
        if (!checks["fileReadable"]!!) {
            errors.add("الملف فارغ أو لا يمكن قراءته")
            return BackupValidation(false, checks, errors, warnings)
        }

        // 2. Valid JSON
        val adapter = moshi.adapter(BackupFile::class.java)
        val backupFile: BackupFile?
        try {
            backupFile = adapter.fromJson(jsonString)
            checks["isValidJSON"] = backupFile != null
        } catch (e: Exception) {
            checks["isValidJSON"] = false
            errors.add("الملف ليس بصيغة JSON صحيحة: ${e.message}")
            return BackupValidation(false, checks, errors, warnings)
        }

        if (backupFile == null) {
            errors.add("فشل تحليل محتوى الملف")
            return BackupValidation(false, checks, errors, warnings)
        }

        // 3. Has metadata
        checks["hasMetadata"] = backupFile.metadata.version.isNotBlank() &&
                backupFile.metadata.exportDate.isNotBlank()
        if (!checks["hasMetadata"]!!) {
            errors.add("الملف لا يحتوي على بيانات وصفية (metadata) صحيحة")
        }

        // 4. Version compatible
        val fileVersion = backupFile.metadata.version.toDoubleOrNull() ?: 0.0
        val currentVersion = BACKUP_VERSION.toDoubleOrNull() ?: 1.0
        checks["versionCompatible"] = fileVersion <= currentVersion
        if (!checks["versionCompatible"]!!) {
            errors.add("الملف من إصدار أحدث ($fileVersion) ولا يمكن استيراده بالإصدار الحالي ($currentVersion)")
        }
        if (fileVersion < currentVersion) {
            warnings.add("الملف من إصدار أقدم ($fileVersion). قد تكون بعض البيانات غير متوافقة.")
        }

        // 5. Has all tables
        checks["hasAllTables"] = true // JSON structure guarantees this via data class

        // 6. Data integrity (basic checks)
        val hasValidStudents = backupFile.students.all { it.name.isNotBlank() }
        val hasValidReports = backupFile.weeklyReports.all { it.weekName.isNotBlank() }
        checks["dataIntegrity"] = hasValidStudents && hasValidReports
        if (!hasValidStudents) errors.add("بعض بيانات الطلاب تالفة (أسماء فارغة)")
        if (!hasValidReports) errors.add("بعض بيانات التقارير تالفة (أسماء أسابيع فارغة)")

        // 7. Relations valid
        val studentIds = backupFile.students.map { it.id }.toSet()
        val reportIds = backupFile.weeklyReports.map { it.id }.toSet()
        val orphanReports = backupFile.weeklyReports.count { it.studentId !in studentIds }
        val orphanLogs = backupFile.dailyLogs.count { it.weeklyReportId !in reportIds }
        checks["relationsValid"] = orphanReports == 0 && orphanLogs == 0
        if (orphanReports > 0) warnings.add("$orphanReports تقرير أسبوعي مرتبط بطالب غير موجود")
        if (orphanLogs > 0) warnings.add("$orphanLogs سجل يومي مرتبط بتقرير غير موجود")

        val isValid = checks.values.all { it }
        return BackupValidation(
            isValid = isValid || (errors.isEmpty()), // Valid if no hard errors
            checks = checks,
            errors = errors,
            warnings = warnings,
            recordCount = backupFile.metadata.recordCount,
            exportDate = backupFile.metadata.exportDate
        )
    }

    // ==========================================
    // LIST BACKUPS
    // ==========================================
    fun listBackups(): List<BackupFileInfo> {
        val backups = mutableListOf<BackupFileInfo>()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Android 10+ use MediaStore
            val projection = arrayOf(
                MediaStore.Downloads.DISPLAY_NAME,
                MediaStore.Downloads.SIZE,
                MediaStore.Downloads.DATE_MODIFIED,
                MediaStore.Downloads._ID
            )
            val selection = "${MediaStore.Downloads.DISPLAY_NAME} LIKE ?"
            val selectionArgs = arrayOf("$BACKUP_FILE_PREFIX%$BACKUP_FILE_EXTENSION")
            val sortOrder = "${MediaStore.Downloads.DATE_MODIFIED} DESC"

            context.contentResolver.query(
                MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL),
                projection, selection, selectionArgs, sortOrder
            )?.use { cursor ->
                val nameIndex = cursor.getColumnIndexOrThrow(MediaStore.Downloads.DISPLAY_NAME)
                val sizeIndex = cursor.getColumnIndexOrThrow(MediaStore.Downloads.SIZE)
                val dateIndex = cursor.getColumnIndexOrThrow(MediaStore.Downloads.DATE_MODIFIED)
                val idIndex = cursor.getColumnIndexOrThrow(MediaStore.Downloads._ID)

                while (cursor.moveToNext()) {
                    val name = cursor.getString(nameIndex)
                    val size = cursor.getLong(sizeIndex)
                    val dateModified = cursor.getLong(dateIndex) * 1000 // Convert seconds to millis
                    val id = cursor.getLong(idIndex)

                    // Try to extract metadata by reading the file
                    val fileUri = Uri.withAppendedPath(
                        MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL),
                        id.toString()
                    )
                    val metadata = tryReadMetadata(fileUri)

                    backups.add(BackupFileInfo(
                        fileName = name,
                        filePath = fileUri.toString(),
                        fileSize = size,
                        exportDate = metadata?.exportDate,
                        timestamp = metadata?.timestamp ?: dateModified,
                        recordCount = metadata?.recordCount,
                        exportedBy = metadata?.exportedBy
                    ))
                }
            }
        } else {
            // Android 9 and below
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val files = downloadsDir.listFiles { _, name ->
                name.startsWith(BACKUP_FILE_PREFIX) && name.endsWith(BACKUP_FILE_EXTENSION)
            }
            files?.sortByDescending { it.lastModified() }
            files?.forEach { file ->
                val metadata = tryReadMetadata(file)
                backups.add(BackupFileInfo(
                    fileName = file.name,
                    filePath = file.absolutePath,
                    fileSize = file.length(),
                    exportDate = metadata?.exportDate,
                    timestamp = metadata?.timestamp ?: file.lastModified(),
                    recordCount = metadata?.recordCount,
                    exportedBy = metadata?.exportedBy
                ))
            }
        }

        return backups
    }

    // ==========================================
    // DELETE BACKUP
    // ==========================================
    fun deleteBackup(fileName: String): Boolean {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val selection = "${MediaStore.Downloads.DISPLAY_NAME} = ?"
                val selectionArgs = arrayOf(fileName)
                val deleted = context.contentResolver.delete(
                    MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL),
                    selection, selectionArgs
                )
                deleted > 0
            } else {
                val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                val file = File(downloadsDir, fileName)
                file.delete()
            }
        } catch (e: Exception) {
            false
        }
    }

    // ==========================================
    // CLEANUP OLD BACKUPS
    // ==========================================
    fun cleanupOldBackups(maxCount: Int = 7) {
        val backups = listBackups()
        if (backups.size > maxCount) {
            // Keep the newest maxCount, delete the rest
            val toDelete = backups.drop(maxCount)
            toDelete.forEach { deleteBackup(it.fileName) }
        }
    }

    // ==========================================
    // HELPER: Save to Downloads
    // ==========================================
    private fun saveToDownloads(fileName: String, data: ByteArray): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Android 10+ use MediaStore
            val values = ContentValues().apply {
                put(MediaStore.Downloads.DISPLAY_NAME, fileName)
                put(MediaStore.Downloads.MIME_TYPE, "application/json")
                put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }
            val uri = context.contentResolver.insert(
                MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY),
                values
            ) ?: throw Exception("فشل إنشاء الملف في مجلد التنزيلات")

            context.contentResolver.openOutputStream(uri)?.use { it.write(data) }
                ?: throw Exception("فشل كتابة البيانات")

            uri.toString()
        } else {
            // Android 9 and below
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            if (!downloadsDir.exists()) downloadsDir.mkdirs()
            val file = File(downloadsDir, fileName)
            file.writeBytes(data)
            file.absolutePath
        }
    }

    // ==========================================
    // HELPER: Read metadata from backup file
    // ==========================================
    private fun tryReadMetadata(uri: Uri): BackupMetadata? {
        return try {
            val jsonString = context.contentResolver.openInputStream(uri)?.bufferedReader()?.use {
                it.readText()
            } ?: return null
            val adapter = moshi.adapter(BackupFile::class.java)
            adapter.fromJson(jsonString)?.metadata
        } catch (e: Exception) {
            null
        }
    }

    private fun tryReadMetadata(file: File): BackupMetadata? {
        return try {
            val jsonString = file.readText(Charsets.UTF_8)
            val adapter = moshi.adapter(BackupFile::class.java)
            adapter.fromJson(jsonString)?.metadata
        } catch (e: Exception) {
            null
        }
    }

    // ==========================================
    // BACKUP LOG
    // ==========================================
    private fun logBackup(entry: BackupLogEntry) {
        try {
            val logFile = File(context.filesDir, "backup-log.json")
            val logAdapter = moshi.adapter(BackupLogData::class.java)

            val logData = if (logFile.exists()) {
                try {
                    logAdapter.fromJson(logFile.readText()) ?: BackupLogData()
                } catch (e: Exception) {
                    BackupLogData()
                }
            } else {
                BackupLogData()
            }

            logData.backups.add(0, entry)
            // Keep only last 50 entries
            while (logData.backups.size > 50) {
                logData.backups.removeAt(logData.backups.size - 1)
            }

            logFile.writeText(logAdapter.indent("  ").toJson(logData))
        } catch (_: Exception) {
            // Silent fail for logging
        }
    }

    fun getBackupLog(): List<BackupLogEntry> {
        return try {
            val logFile = File(context.filesDir, "backup-log.json")
            if (!logFile.exists()) return emptyList()
            val logAdapter = moshi.adapter(BackupLogData::class.java)
            logAdapter.fromJson(logFile.readText())?.backups ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
}
