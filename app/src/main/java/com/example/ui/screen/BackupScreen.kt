package com.example.ui.screen

import android.app.TimePickerDialog
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.backup.*
import com.example.ui.viewmodel.QuranViewModel
import com.example.ui.viewmodel.Screen
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackupScreen(viewModel: QuranViewModel) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val backupService = remember { BackupService(context) }
    val backupPrefs = remember { BackupPreferences(context) }
    val backupScheduler = remember { BackupScheduler(context) }

    // State
    var isExporting by remember { mutableStateOf(false) }
    var isImporting by remember { mutableStateOf(false) }
    var backupList by remember { mutableStateOf<List<BackupFileInfo>>(emptyList()) }
    var backupLog by remember { mutableStateOf<List<BackupLogEntry>>(emptyList()) }
    var lastExportResult by remember { mutableStateOf<ExportResult?>(null) }
    var lastImportResult by remember { mutableStateOf<ImportResult?>(null) }
    var showDeleteConfirm by remember { mutableStateOf<BackupFileInfo?>(null) }
    var showImportConfirm by remember { mutableStateOf(false) }
    var pendingImportUri by remember { mutableStateOf<android.net.Uri?>(null) }
    var validationResult by remember { mutableStateOf<BackupValidation?>(null) }

    // Auto-backup settings
    var autoBackupEnabled by remember { mutableStateOf(backupPrefs.autoBackupEnabled) }
    var maxBackupCount by remember { mutableStateOf(backupPrefs.maxBackupCount) }
    var backupTime by remember { mutableStateOf(backupPrefs.getBackupTimeFormatted()) }

    // Load data
    LaunchedEffect(Unit) {
        backupList = backupService.listBackups()
        backupLog = backupService.getBackupLog()
    }

    // File picker launcher for import
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            coroutineScope.launch {
                // Read and validate first
                val jsonString = context.contentResolver.openInputStream(uri)?.bufferedReader()?.use { it.readText() }
                if (jsonString != null) {
                    validationResult = backupService.validateBackup(jsonString)
                    pendingImportUri = uri
                    showImportConfirm = true
                } else {
                    Toast.makeText(context, "❌ لا يمكن قراءة الملف المحدد", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("💾 النسخ الاحتياطية", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateTo(Screen.StudentsList) }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "رجوع")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // =============================================
            // 1. QUICK ACTIONS: Export & Import
            // =============================================
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            "⚡ إجراءات سريعة:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Export Button
                            Button(
                                onClick = {
                                    coroutineScope.launch {
                                        isExporting = true
                                        lastExportResult = backupService.exportBackup(exportedBy = "manual")
                                        isExporting = false
                                        // Refresh list
                                        backupList = backupService.listBackups()
                                        backupLog = backupService.getBackupLog()
                                    }
                                },
                                enabled = !isExporting && !isImporting,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(52.dp)
                                    .testTag("export_backup_button"),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                if (isExporting) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(18.dp),
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        strokeWidth = 2.dp
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("جاري التصدير...", fontWeight = FontWeight.Bold)
                                } else {
                                    Text("📤 تصدير نسخة", fontWeight = FontWeight.Bold)
                                }
                            }

                            // Import Button
                            OutlinedButton(
                                onClick = {
                                    filePickerLauncher.launch(arrayOf("application/json"))
                                },
                                enabled = !isExporting && !isImporting,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(52.dp)
                                    .testTag("import_backup_button"),
                                border = androidx.compose.foundation.BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("📥 استيراد نسخة", fontWeight = FontWeight.Bold)
                            }
                        }

                        // Show last result
                        AnimatedVisibility(visible = lastExportResult != null) {
                            lastExportResult?.let { result ->
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (result.success)
                                            Color(0xFF059669).copy(alpha = 0.1f)
                                        else
                                            MaterialTheme.colorScheme.error.copy(alpha = 0.1f)
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            if (result.success) "✅" else "❌",
                                            fontSize = 20.sp
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column {
                                            Text(
                                                if (result.success) "تم التصدير بنجاح!" else "فشل التصدير",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 14.sp,
                                                color = if (result.success) Color(0xFF059669) else MaterialTheme.colorScheme.error
                                            )
                                            if (result.success) {
                                                Text(
                                                    "📁 ${result.fileName} (${formatFileSize(result.fileSize)})",
                                                    fontSize = 11.sp,
                                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                                )
                                                Text(
                                                    "📊 ${result.recordCount?.students ?: 0} طالب | ${result.recordCount?.weeklyReports ?: 0} تقرير | ${result.recordCount?.dailyLogs ?: 0} سجل",
                                                    fontSize = 11.sp,
                                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                                )
                                            } else {
                                                Text(
                                                    result.error ?: "خطأ غير معروف",
                                                    fontSize = 11.sp,
                                                    color = MaterialTheme.colorScheme.error
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        AnimatedVisibility(visible = lastImportResult != null) {
                            lastImportResult?.let { result ->
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (result.success)
                                            Color(0xFF059669).copy(alpha = 0.1f)
                                        else
                                            MaterialTheme.colorScheme.error.copy(alpha = 0.1f)
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            if (result.success) "✅" else "❌",
                                            fontSize = 20.sp
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column {
                                            Text(
                                                if (result.success) "تم الاستيراد بنجاح!" else "فشل الاستيراد",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 14.sp,
                                                color = if (result.success) Color(0xFF059669) else MaterialTheme.colorScheme.error
                                            )
                                            if (result.success) {
                                                Text(
                                                    "📊 ${result.recordsRestored?.students ?: 0} طالب | ${result.recordsRestored?.weeklyReports ?: 0} تقرير | ${result.recordsRestored?.dailyLogs ?: 0} سجل",
                                                    fontSize = 11.sp,
                                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                                )
                                                Text(
                                                    "📅 النسخة بتاريخ: ${result.restoredDate ?: "غير معروف"}",
                                                    fontSize = 11.sp,
                                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                                )
                                            } else {
                                                Text(
                                                    result.error ?: "خطأ غير معروف",
                                                    fontSize = 11.sp,
                                                    color = MaterialTheme.colorScheme.error
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // =============================================
            // 2. AUTO BACKUP SETTINGS
            // =============================================
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            "⚙️ إعدادات النسخ التلقائي:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.primary
                        )

                        // Auto backup toggle
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    "النسخ التلقائي اليومي",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Text(
                                    "إنشاء نسخة احتياطية تلقائياً كل يوم",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            }
                            Switch(
                                checked = autoBackupEnabled,
                                onCheckedChange = {
                                    autoBackupEnabled = it
                                    backupScheduler.setAutoBackupEnabled(it)
                                },
                                modifier = Modifier.testTag("auto_backup_switch")
                            )
                        }

                        AnimatedVisibility(visible = autoBackupEnabled) {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                // Backup time
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable {
                                            TimePickerDialog(
                                                context,
                                                { _, hour, minute ->
                                                    backupScheduler.setBackupTime(hour, minute)
                                                    backupTime = String.format("%02d:%02d", hour, minute)
                                                },
                                                backupPrefs.backupHour,
                                                backupPrefs.backupMinute,
                                                true
                                            ).show()
                                        }
                                        .padding(vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("⏰ وقت النسخ", fontWeight = FontWeight.Medium, fontSize = 14.sp)
                                    Text(
                                        backupTime,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontSize = 16.sp
                                    )
                                }

                                // Max backup count
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("📦 الحد الأقصى للنسخ", fontWeight = FontWeight.Medium, fontSize = 14.sp)
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        IconButton(
                                            onClick = {
                                                if (maxBackupCount > 3) {
                                                    maxBackupCount--
                                                    backupPrefs.maxBackupCount = maxBackupCount
                                                }
                                            },
                                            modifier = Modifier.size(32.dp)
                                        ) {
                                            Text("−", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                        }
                                        Text(
                                            "$maxBackupCount",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        IconButton(
                                            onClick = {
                                                if (maxBackupCount < 30) {
                                                    maxBackupCount++
                                                    backupPrefs.maxBackupCount = maxBackupCount
                                                }
                                            },
                                            modifier = Modifier.size(32.dp)
                                        ) {
                                            Icon(Icons.Default.Add, contentDescription = "زيادة")
                                        }
                                    }
                                }

                                // Last auto backup status
                                if (backupPrefs.lastAutoBackupTimestamp > 0) {
                                    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("ar"))
                                    val statusText = if (backupPrefs.lastAutoBackupStatus == "success") "✅ نجح" else "❌ فشل"
                                    Text(
                                        "آخر نسخة تلقائية: $statusText — ${dateFormat.format(Date(backupPrefs.lastAutoBackupTimestamp))}",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // =============================================
            // 3. SAVED BACKUPS LIST
            // =============================================
            item {
                Text(
                    "📁 النسخ المحفوظة (${backupList.size}):",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            if (backupList.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("📭", fontSize = 36.sp)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    "لا توجد نسخ احتياطية محفوظة بعد",
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                )
                                Text(
                                    "اضغط 'تصدير نسخة' لإنشاء أول نسخة احتياطية",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                                )
                            }
                        }
                    }
                }
            } else {
                items(backupList) { backup ->
                    BackupFileCard(
                        backup = backup,
                        onRestore = {
                            pendingImportUri = android.net.Uri.parse(backup.filePath)
                            coroutineScope.launch {
                                val jsonString = context.contentResolver.openInputStream(pendingImportUri!!)?.bufferedReader()?.use { it.readText() }
                                if (jsonString != null) {
                                    validationResult = backupService.validateBackup(jsonString)
                                    showImportConfirm = true
                                }
                            }
                        },
                        onDelete = { showDeleteConfirm = backup }
                    )
                }
            }

            // =============================================
            // 4. BACKUP LOG (Recent Activity)
            // =============================================
            if (backupLog.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "📋 سجل العمليات الأخيرة:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                items(backupLog.take(10)) { entry ->
                    BackupLogCard(entry)
                }
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }

    // =============================================
    // IMPORT CONFIRMATION DIALOG
    // =============================================
    if (showImportConfirm && pendingImportUri != null) {
        AlertDialog(
            onDismissRequest = {
                showImportConfirm = false
                pendingImportUri = null
                validationResult = null
            },
            icon = { Text("⚠️", fontSize = 32.sp) },
            title = {
                Text(
                    "تأكيد الاستيراد",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        "⚠️ تنبيه مهم: عملية الاستيراد ستحذف جميع البيانات الحالية وتستبدلها ببيانات النسخة الاحتياطية!",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 13.sp
                    )

                    validationResult?.let { validation ->
                        HorizontalDivider()
                        Text(
                            "📊 فحص الملف:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )

                        validation.checks.forEach { (check, passed) ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    if (passed) "✅" else "❌",
                                    fontSize = 12.sp
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    getCheckDisplayName(check),
                                    fontSize = 12.sp
                                )
                            }
                        }

                        if (validation.recordCount != null) {
                            HorizontalDivider()
                            Text(
                                "محتوى النسخة: ${validation.recordCount.students} طالب | ${validation.recordCount.weeklyReports} تقرير | ${validation.recordCount.dailyLogs} سجل",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        if (validation.warnings.isNotEmpty()) {
                            Text(
                                "⚠️ تحذيرات: ${validation.warnings.joinToString(", ")}",
                                fontSize = 11.sp,
                                color = Color(0xFFD97706)
                            )
                        }

                        if (validation.errors.isNotEmpty()) {
                            Text(
                                "❌ أخطاء: ${validation.errors.joinToString(", ")}",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            },
            confirmButton = {
                val canImport = validationResult?.isValid ?: false
                Button(
                    onClick = {
                        coroutineScope.launch {
                            showImportConfirm = false
                            isImporting = true
                            lastImportResult = backupService.importBackup(pendingImportUri!!)
                            isImporting = false
                            pendingImportUri = null
                            validationResult = null
                            // Refresh
                            backupList = backupService.listBackups()
                            backupLog = backupService.getBackupLog()
                        }
                    },
                    enabled = canImport,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("استيراد واستبدال البيانات", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showImportConfirm = false
                    pendingImportUri = null
                    validationResult = null
                }) {
                    Text("إلغاء")
                }
            }
        )
    }

    // =============================================
    // DELETE CONFIRMATION DIALOG
    // =============================================
    if (showDeleteConfirm != null) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = null },
            title = { Text("حذف النسخة الاحتياطية؟", fontWeight = FontWeight.Bold) },
            text = {
                Text("هل أنت متأكد من حذف النسخة \"${showDeleteConfirm!!.fileName}\"؟ لا يمكن التراجع عن هذا الإجراء.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        backupService.deleteBackup(showDeleteConfirm!!.fileName)
                        showDeleteConfirm = null
                        coroutineScope.launch {
                            backupList = backupService.listBackups()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("حذف", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = null }) {
                    Text("إلغاء")
                }
            }
        )
    }
}

// =============================================
// BACKUP FILE CARD
// =============================================
@Composable
fun BackupFileCard(
    backup: BackupFileInfo,
    onRestore: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        backup.fileName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            "📦 ${formatFileSize(backup.fileSize)}",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                        if (backup.exportDate != null) {
                            Text(
                                "📅 ${formatExportDate(backup.exportDate)}",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                        if (backup.exportedBy != null) {
                            Text(
                                if (backup.exportedBy == "auto") "🤖 تلقائي" else "👤 يدوي",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    }
                    if (backup.recordCount != null) {
                        Text(
                            "${backup.recordCount.students} طالب | ${backup.recordCount.weeklyReports} تقرير | ${backup.recordCount.dailyLogs} سجل",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    IconButton(
                        onClick = onRestore,
                        modifier = Modifier
                            .size(36.dp)
                            .background(
                                Color(0xFF059669).copy(alpha = 0.1f),
                                CircleShape
                            )
                    ) {
                        Icon(
                            Icons.Default.Refresh,
                            contentDescription = "استعادة",
                            tint = Color(0xFF059669),
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier
                            .size(36.dp)
                            .background(
                                MaterialTheme.colorScheme.error.copy(alpha = 0.1f),
                                CircleShape
                            )
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "حذف",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

// =============================================
// BACKUP LOG CARD
// =============================================
@Composable
fun BackupLogCard(entry: BackupLogEntry) {
    val statusColor = when (entry.status) {
        "success" -> Color(0xFF059669)
        "failed" -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
    }
    val statusIcon = when (entry.status) {
        "success" -> "✅"
        "failed" -> "❌"
        else -> "⏳"
    }
    val typeIcon = when (entry.type) {
        "auto" -> "🤖"
        "manual" -> "👤"
        "import" -> "📥"
        else -> "📋"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("$statusIcon $typeIcon", fontSize = 16.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    when (entry.type) {
                        "auto" -> "نسخة تلقائية"
                        "manual" -> "نسخة يدوية"
                        "import" -> "استيراد نسخة"
                        else -> entry.type
                    },
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = statusColor
                )
                Text(
                    formatExportDate(entry.date),
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
            if (entry.recordCount != null) {
                Text(
                    "${entry.recordCount.students}👤 ${entry.recordCount.weeklyReports}📋 ${entry.recordCount.dailyLogs}📝",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
        }
    }
}

// =============================================
// HELPER FUNCTIONS
// =============================================
private fun formatFileSize(bytes: Long): String {
    return when {
        bytes < 1024 -> "$bytes B"
        bytes < 1024 * 1024 -> "${bytes / 1024} KB"
        else -> String.format("%.1f MB", bytes / (1024.0 * 1024.0))
    }
}

private fun formatExportDate(dateString: String?): String {
    if (dateString.isNullOrBlank()) return "غير معروف"
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
        val date = inputFormat.parse(dateString)
        val outputFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("ar"))
        outputFormat.format(date!!)
    } catch (e: Exception) {
        dateString
    }
}

private fun getCheckDisplayName(check: String): String {
    return when (check) {
        "fileReadable" -> "الملف قابل للقراءة"
        "isValidJSON" -> "صيغة JSON صحيحة"
        "hasMetadata" -> "بيانات وصفية موجودة"
        "versionCompatible" -> "إصدار متوافق"
        "hasAllTables" -> "جميع الجداول موجودة"
        "dataIntegrity" -> "سلامة البيانات"
        "relationsValid" -> "العلاقات صحيحة"
        else -> check
    }
}
