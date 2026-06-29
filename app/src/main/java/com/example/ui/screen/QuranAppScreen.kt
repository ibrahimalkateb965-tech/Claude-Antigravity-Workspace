package com.example.ui.screen

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.DailyLog
import com.example.data.model.Student
import com.example.data.model.WeeklyReport
import com.example.ui.theme.*
import com.example.ui.viewmodel.QuranViewModel
import com.example.ui.viewmodel.Screen
import com.example.data.backup.AppPreferences
import com.example.data.notification.AlarmScheduler
import com.example.data.notification.NotificationHelper
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import com.example.R
import kotlinx.coroutines.launch

@Composable
fun QuranAppScreen(viewModel: QuranViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val currentLanguage by viewModel.appLanguage.collectAsStateWithLifecycle()

    // Keep the global singleton in sync for use in lambdas/callbacks
    AppLang.current = currentLanguage

    // Enforce RTL Layout Direction for Arabic, LTR for English
    val layoutDir = if (currentLanguage == "en") LayoutDirection.Ltr else LayoutDirection.Rtl

    CompositionLocalProvider(
        LocalLanguage provides currentLanguage,
        LocalLayoutDirection provides layoutDir
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            when (val screen = currentScreen) {
                is Screen.Splash -> SplashScreen(viewModel)
                is Screen.StudentsList -> StudentsListScreen(viewModel)
                is Screen.StudentProfile -> StudentProfileScreen(viewModel, screen.student)
                is Screen.ReportTracking -> ReportTrackingScreen(viewModel, screen.student, screen.report)
                is Screen.PeriodReport -> PeriodReportScreen(viewModel, screen.student)
                is Screen.Backups -> BackupScreen(viewModel)
                is Screen.Settings -> SettingsScreen(viewModel)
                is Screen.TodaySchedule -> TodayScheduleScreen(viewModel)
            }
        }
    }
}

// ==========================================
// 1. STUDENTS LIST SCREEN
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentsListScreen(viewModel: QuranViewModel) {
    val context = LocalContext.current
    val students by viewModel.students.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    
    var showAddDialog by remember { mutableStateOf(false) }
    var showGroupReportDialog by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Column(modifier = Modifier.padding(end = 16.dp)) {
                        Text(
                            text = "تيجان النور 📖".loc(),
                            fontWeight = FontWeight.Black,
                            fontSize = 25.sp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Text(
                            text = "متابعة تلاوة وحفظ القرآن الكريم للطلاب والناشئة".loc(),
                            fontSize = 13.5.sp,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.95f),
                            fontWeight = FontWeight.Bold,
                            lineHeight = 18.sp
                        )
                    }
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                actions = {
                    val isDarkModeOption by viewModel.isDarkMode.collectAsStateWithLifecycle()
                    val systemDark = isSystemInDarkTheme()
                    val isDark = isDarkModeOption ?: systemDark
                    
                    IconButton(
                        onClick = { viewModel.toggleTheme(!isDark) },
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.25f), CircleShape)
                    ) {
                        Text(
                            text = if (isDark) "☀️" else "🌙",
                            fontSize = 18.sp
                        )
                    }

                    IconButton(
                        onClick = { viewModel.navigateTo(Screen.TodaySchedule) },
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.25f), CircleShape)
                    ) {
                        Text(
                            text = "📅",
                            fontSize = 18.sp
                        )
                    }

                    IconButton(
                        onClick = { viewModel.navigateTo(Screen.Backups) },
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.25f), CircleShape)
                    ) {
                        Text(
                            text = "💾",
                            fontSize = 18.sp
                        )
                    }

                    IconButton(
                        onClick = {
                            if (students.isEmpty()) {
                                android.widget.Toast.makeText(context, "لا يوجد طلاب مسجلين لعرض التقارير!".loc(), android.widget.Toast.LENGTH_SHORT).show()
                            } else {
                                showGroupReportDialog = true
                            }
                        },
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.25f), CircleShape)
                    ) {
                        Text(
                            text = "📊",
                            fontSize = 18.sp
                        )
                    }

                    IconButton(
                        onClick = { viewModel.navigateTo(Screen.Settings) },
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.25f), CircleShape)
                    ) {
                        Text(
                            text = "⚙️",
                            fontSize = 18.sp
                        )
                    }

                    IconButton(
                        onClick = { showAddDialog = true },
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.25f), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "إضافة طالب".loc(),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Motivational Ayah Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Emerald80.copy(alpha = 0.35f)),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.8.dp, Emerald40.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "قالَ رَسُولُ اللَّهِ ﷺ:".loc(),
                        fontSize = 13.5.sp,
                        color = Emerald40,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "«خَيْرُكُمْ مَنْ تَعَلَّمَ القُرْآنَ وَعَلَّمَهُ»".loc(),
                        fontSize = 18.sp,
                        color = Emerald950,
                        fontWeight = FontWeight.Black,
                        textAlign = TextAlign.Center,
                        lineHeight = 24.sp
                    )
                }
            }
            
            // Statistics Counter
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.6.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.35f))
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("إجمالي الطلاب".loc(), fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f), fontWeight = FontWeight.Bold)
                        Text(
                            text = "${students.size}",
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.6.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.35f))
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("حلقات النشاط".loc(), fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f), fontWeight = FontWeight.Bold)
                        val groupsCount = students.map { it.groupName }.distinct().filter { it.isNotBlank() }.size
                        Text(
                            text = "$groupsCount",
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.updateSearchQuery(it) },
                placeholder = { Text("بحث عن طالب أو حلقة...".loc(), fontWeight = FontWeight.Medium) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("student_search_input"),
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "بحث".loc(), tint = MaterialTheme.colorScheme.primary) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.updateSearchQuery("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "مسح البحث".loc())
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.8f)
                ),
                textStyle = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    textDirection = TextDirection.Rtl,
                    textAlign = TextAlign.Right
                )
            )

            // Students List
            if (students.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("🌱", fontSize = 48.sp)
                        Text(
                            text = if (searchQuery.isEmpty()) "لا يوجد طلاب مسجلين بعد".loc() else "لم يتم العثور على نتائج للمطابقة".loc(),
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                            fontSize = 16.sp
                        )
                        Text(
                            text = if (searchQuery.isEmpty()) "اضغط على زر الإضافة (+) بالأعلى للبدء".loc() else "تأكد من كتابة الاسم بشكل صحيح".loc(),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            fontSize = 13.5.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(students, key = { it.id }) { student ->
                        StudentCard(
                            student = student,
                            onClick = { viewModel.navigateTo(Screen.StudentProfile(student)) },
                            onDelete = { viewModel.deleteStudent(student) }
                        )
                    }
                }
            }
        }
    }

    // Add Student Dialog
    if (showAddDialog) {
        AddStudentDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { name, group, teacher, notes, whatsapp, circleSessionDaysTimes, onResult ->
                viewModel.addStudent(name, group, teacher, notes, whatsapp, circleSessionDaysTimes) { error ->
                    if (error != null) {
                        onResult(error)
                    } else {
                        onResult(null)
                        showAddDialog = false
                    }
                }
            }
        )
    }

    if (showGroupReportDialog) {
        GroupReportDialog(
            students = students,
            onDismiss = { showGroupReportDialog = false }
        )
    }
}

@Composable
fun StudentCard(
    student: Student,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    var showConfirmDelete by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .testTag("student_card_${student.id}"),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.6.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.35f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(Emerald80.copy(alpha = 0.35f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("👤", fontSize = 20.sp)
                }
                
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        if (student.studentSequentialNumber > 0) {
                            Box(
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(4.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "#${student.studentSequentialNumber}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                        Text(
                            text = student.name,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    @OptIn(ExperimentalLayoutApi::class)
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (student.groupName.isNotBlank()) {
                            Box(
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Home,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Text(
                                        text = student.groupName,
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }

                        val displayTimeText = formatStudentCircleDaysTimesCompact(student.circleSessionDaysTimes)
                        if (displayTimeText != "غير محدد" && displayTimeText != "غير محدد".loc()) {
                            Box(
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text("⏰", fontSize = 11.sp)
                                    Text(
                                        text = displayTimeText,
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                }
                            }
                        }

                        if (student.teacherName.isNotBlank()) {
                            Box(
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text("👨‍🏫", fontSize = 11.sp)
                                    Text(
                                        text = "المعلم: ".loc() + student.teacherName,
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
            
            IconButton(
                onClick = { showConfirmDelete = true },
                modifier = Modifier.testTag("delete_student_btn_${student.id}")
            ) {
                Icon(Icons.Default.Delete, contentDescription = "حذف الطالب".loc(), tint = Color.LightGray)
            }
        }
    }

    if (showConfirmDelete) {
        AlertDialog(
            onDismissRequest = { showConfirmDelete = false },
            title = { Text("حذف ملف الطالب؟".loc(), fontWeight = FontWeight.Bold) },
            text = { Text("هل أنت متأكد من حذف الطالب".loc() + " (${student.name}) " + "وكامل سجلات متابعته بشكل نهائي؟ لا يمكن التراجع عن هذا الإجراء.".loc()) },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDelete()
                        showConfirmDelete = false
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("حذف نهائي".loc())
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDelete = false }) {
                    Text("إلغاء".loc())
                }
            }
        )
    }
}

@Composable
fun AddStudentDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, String, String?, String, (String?) -> Unit) -> Unit
) {
    val context = LocalContext.current
    var name by remember { mutableStateOf(TextFieldValue("")) }
    var group by remember { mutableStateOf(TextFieldValue("")) }
    var teacher by remember { mutableStateOf(TextFieldValue("")) }
    var notes by remember { mutableStateOf(TextFieldValue("")) }
    var whatsappNumber by remember { mutableStateOf(TextFieldValue("")) }
    val weekDays = listOf("السبت", "الأحد", "الاثنين", "الثلاثاء", "الأربعاء", "الخميس", "الجمعة")
    var selectedDays by remember { mutableStateOf(emptySet<String>()) }
    var dayTimeMap by remember { mutableStateOf(emptyMap<String, String>()) }
    
    var errorMsg by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "إضافة طالب جديد للحلقة ✨".loc(),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Right
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { 
                        name = it
                        if (it.text.isNotBlank()) errorMsg = ""
                    },
                    label = { Text("اسم الطالب الكامل *".loc()) },
                    isError = errorMsg.isNotEmpty(),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("add_student_name_field"),
                    textStyle = TextStyle(textDirection = TextDirection.Rtl, textAlign = TextAlign.Right)
                )
                if (errorMsg.isNotEmpty()) {
                    Text(errorMsg, color = MaterialTheme.colorScheme.error, fontSize = 10.sp)
                }
                
                OutlinedTextField(
                    value = group,
                    onValueChange = { group = it },
                    label = { Text("الصف / حلقة التحفيظ".loc()) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(textDirection = TextDirection.Rtl, textAlign = TextAlign.Right)
                )
                
                OutlinedTextField(
                    value = teacher,
                    onValueChange = { teacher = it },
                    label = { Text("اسم المعلم المربي".loc()) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(textDirection = TextDirection.Rtl, textAlign = TextAlign.Right)
                )
                
                OutlinedTextField(
                    value = whatsappNumber,
                    onValueChange = { whatsappNumber = it },
                    label = { Text("رقم الواتساب (اختياري، مثلاً +966501234567)".loc()) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("add_student_whatsapp_field"),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    textStyle = TextStyle(textDirection = TextDirection.Ltr, textAlign = TextAlign.Left)
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "مواعيد حلقة الطالب (اختر الأيام والأوقات) ⏰".loc(),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Right
                )
                
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    weekDays.forEach { day ->
                        val isEnabled = day in selectedDays
                        val time = dayTimeMap[day] ?: "18:30"
                        
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (isEnabled) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.15f)
                                    else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
                                )
                                .border(
                                    width = 1.dp,
                                    color = if (isEnabled) MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                                    else MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable {
                                    selectedDays = if (isEnabled) {
                                        selectedDays - day
                                    } else {
                                        selectedDays + day
                                    }
                                }
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Checkbox(
                                    checked = isEnabled,
                                    onCheckedChange = { checked ->
                                        selectedDays = if (checked == true) {
                                            selectedDays + day
                                        } else {
                                            selectedDays - day
                                        }
                                    }
                                )
                                Text(
                                    text = day.loc(),
                                    fontSize = 13.sp,
                                    fontWeight = if (isEnabled) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isEnabled) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.onSurface
                                )
                            }
                            
                            if (isEnabled) {
                                Button(
                                    onClick = {
                                        val parts = time.split(":")
                                        val currentHour = parts.getOrNull(0)?.toIntOrNull() ?: 18
                                        val currentMinute = parts.getOrNull(1)?.toIntOrNull() ?: 30
                                        
                                        android.app.TimePickerDialog(
                                            context,
                                            { _, hour, minute ->
                                                val formattedTime = String.format(java.util.Locale.US, "%02d:%02d", hour, minute)
                                                dayTimeMap = dayTimeMap + (day to formattedTime)
                                            },
                                            currentHour,
                                            currentMinute,
                                            false
                                        ).show()
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                                    ),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                                    modifier = Modifier.height(30.dp)
                                ) {
                                    Text(
                                        text = "${formatTime12h(time)} ⏰",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("ملحوظة".loc()) },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(textDirection = TextDirection.Content, textAlign = TextAlign.Start)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.text.isBlank()) {
                        errorMsg = "اسم الطالب مطلوب للرصد".loc()
                    } else {
                        // Serialize selected days and their custom times to "day=time;day=time" sorted chronologically
                        val daysTimesString = weekDays.filter { it in selectedDays }
                            .joinToString(";") { day ->
                                val time = dayTimeMap[day] ?: "18:30"
                                "$day=$time"
                            }
                        onConfirm(name.text, group.text, teacher.text, notes.text, whatsappNumber.text, daysTimesString) { result ->
                            if (result != null) {
                                errorMsg = result
                            } else {
                                errorMsg = ""
                            }
                        }
                    }
                },
                modifier = Modifier.testTag("save_student_confirm")
            ) {
                Text("إضافة وحفظ".loc())
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("إلغاء".loc())
            }
        }
    )
}

// ==========================================
// TODAY SCHEDULE SCREEN - مواعيد اليوم الحالي
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodayScheduleScreen(viewModel: QuranViewModel) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    // Get today's Arabic day name
    val todayName = remember {
        val sdf = java.text.SimpleDateFormat("EEEE", java.util.Locale("ar"))
        sdf.format(java.util.Date())
    }

    var todayStudents by remember { mutableStateOf<List<com.example.data.model.Student>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        isLoading = true
        todayStudents = viewModel.getStudentsScheduledOnDay(todayName)
        isLoading = false
    }

    // Group students by their session time for today
    val studentsByTime = remember(todayStudents) {
        todayStudents.groupBy { student ->
            student.circleSessionDaysTimes.split(";")
                .firstOrNull { it.split("=").getOrNull(0)?.trim() == todayName }
                ?.split("=")?.getOrNull(1) ?: "غير محدد".loc()
        }.toSortedMap()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "📅 جدول اليوم".loc(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )
                        Text(
                            "يوم $todayName - ${formatLongDate(System.currentTimeMillis())}",
                            fontSize = 11.5.sp,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateTo(Screen.StudentsList) }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "رجوع".loc())
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
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (todayStudents.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("🌙", fontSize = 52.sp)
                    Text(
                        text = "لا توجد جلسات مجدولة اليوم".loc(),
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 17.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Text(
                        text = if (AppLang.current == "en") "$todayName has no scheduled circles" else "يوم $todayName لا يحتوي على مواعيد حلقات",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Summary card
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f)
                        ),
                        border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f))
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp).fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    "إجمالي الطلاب اليوم".loc(),
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    "${todayStudents.size}" + " طالب".loc(),
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Black,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    "عدد الحلقات".loc(),
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    "${todayStudents.map { it.groupName }.distinct().filter { it.isNotBlank() }.size}" + " حلقة".loc(),
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Black,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }
                        }
                    }
                }

                // Students grouped by time
                studentsByTime.forEach { (time, students) ->
                    item {
                        // Time header
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 12.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "⏰ ${formatTime12h(time)}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Black,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                            HorizontalDivider(modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.outlineVariant)
                            Text(
                                "${students.size}" + " طالب".loc(),
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    items(students, key = { it.id }) { student ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { viewModel.navigateTo(Screen.StudentProfile(student)) },
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.2.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp).fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .background(Emerald80.copy(alpha = 0.3f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("👤", fontSize = 18.sp)
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        if (student.studentSequentialNumber > 0) {
                                            Text(
                                                "#${student.studentSequentialNumber}",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                        Text(
                                            student.name,
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 15.sp,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        modifier = Modifier.padding(top = 3.dp)
                                    ) {
                                        if (student.groupName.isNotBlank()) {
                                            Text(
                                                "🏫 ${student.groupName}",
                                                fontSize = 11.sp,
                                                color = MaterialTheme.colorScheme.primary,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                        if (student.teacherName.isNotBlank()) {
                                            Text(
                                                "👨‍🏫 ${student.teacherName}",
                                                fontSize = 11.sp,
                                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                            )
                                        }
                                    }
                                }
                                Icon(
                                    Icons.Default.ArrowBack,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}

// ==========================================
// 2. STUDENT PROFILE SCREEN
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentProfileScreen(viewModel: QuranViewModel, student: Student) {
    val reports by viewModel.weeklyReports.collectAsStateWithLifecycle()
    var showAddWeekDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    
    val activeStudentState by viewModel.selectedStudent.collectAsStateWithLifecycle()
    val activeStudent = activeStudentState ?: student

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ملف الطالب ومتابعته".loc(), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateTo(Screen.StudentsList) }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "رجوع".loc())
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAddWeekDialog = true },
                icon = { Icon(Icons.Default.DateRange, contentDescription = null) },
                text = { Text("إضافة أسبوع رصد".loc()) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.testTag("add_week_fab")
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Student Card Summary (Profile)
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                border = BorderStroke(1.8.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .background(MaterialTheme.colorScheme.primary, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = activeStudent.name.take(1),
                                color = Color.White,
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                        
                        Column {
                            Text(
                                text = activeStudent.name,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "🏫 الحلقة: ".loc() + activeStudent.groupName.ifBlank { "غير محددة".loc() } + "  |  👤 المعلم: ".loc() + activeStudent.teacherName.ifBlank { "غير محدد".loc() },
                                fontSize = 13.5.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),
                                fontWeight = FontWeight.Bold
                            )
                            if (activeStudent.notes.isNotBlank()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "📝 ملحوظة: ".loc() + activeStudent.notes,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = { viewModel.navigateTo(Screen.PeriodReport(activeStudent)) },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                modifier = Modifier.height(34.dp)
                            ) {
                                Icon(Icons.Default.Info, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("📊 تقرير فترة زمنية".loc(), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    
                    IconButton(
                        onClick = { showEditDialog = true },
                        modifier = Modifier.testTag("edit_student_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "تعديل البيانات".loc(),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            Text(
                text = "جداول ورصد المتابعة الأسبوعية 📅".loc(),
                fontWeight = FontWeight.Black,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onBackground
            )

            if (reports.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("📅", fontSize = 48.sp)
                        Text(
                            text = "لا توجد أسابيع رصد مسجلة للطالب".loc(),
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "انقر على زر 'إضافة أسبوع رصد' بالأسفل لبدء رصد الجداول".loc(),
                            color = Color.Gray.copy(alpha = 0.7f),
                            fontSize = 11.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(reports, key = { it.id }) { report ->
                        WeeklyReportItemCard(
                            report = report,
                            onClick = { viewModel.navigateTo(Screen.ReportTracking(activeStudent, report)) },
                            onDelete = { viewModel.deleteWeeklyReport(report) },
                            onUpdate = { updatedReport -> viewModel.updateWeeklyReport(updatedReport) }
                        )
                    }
                }
            }
        }
    }

    if (showAddWeekDialog) {
        var weekName by remember { mutableStateOf("") }
        // Simple default suggestion e.g. "الأسبوع الأول"
        LaunchedEffect(reports) {
            weekName = "الأسبوع ".loc() + (reports.size + 1)
        }
        
        AlertDialog(
            onDismissRequest = { showAddWeekDialog = false },
            title = { Text("إضافة أسبوع متابعة جديد".loc(), fontWeight = FontWeight.Bold) },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text("اكتب عنوان الأسبوع أو الفترة (مثال: الأسبوع الأول - محرم، أو رصد يونيو):".loc(), fontSize = 12.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = weekName,
                        onValueChange = { weekName = it },
                        label = { Text("عنوان أسبوع الرصد".loc()) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("add_week_name_field"),
                        textStyle = TextStyle(textDirection = TextDirection.Rtl, textAlign = TextAlign.Right)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (weekName.isNotBlank()) {
                            viewModel.addWeeklyReport(activeStudent.id, weekName)
                            showAddWeekDialog = false
                        }
                    },
                    modifier = Modifier.testTag("save_week_confirm")
                ) {
                    Text("إنشاء السجل".loc())
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddWeekDialog = false }) {
                    Text("إلغاء".loc())
                }
            }
        )
    }

    if (showEditDialog) {
        EditStudentDialog(
            student = activeStudent,
            onDismiss = { showEditDialog = false },
            onConfirm = { name, group, teacher, notes, whatsapp, circleSessionDaysTimes, onResult ->
                val updatedStudent = activeStudent.copy(
                    name = name,
                    groupName = group,
                    teacherName = teacher,
                    notes = notes,
                    whatsappNumber = whatsapp,
                    circleSessionDaysTimes = circleSessionDaysTimes
                )
                viewModel.updateStudent(updatedStudent) { error ->
                    if (error != null) {
                        onResult(error)
                    } else {
                        onResult(null)
                        showEditDialog = false
                    }
                }
            }
        )
    }
}

@Composable
fun WeeklyReportItemCard(
    report: WeeklyReport,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    onUpdate: (WeeklyReport) -> Unit
) {
    var showConfirmDelete by remember { mutableStateOf(false) }
    var showRenameDialog by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .testTag("week_report_card_${report.id}"),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.6.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.45f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(Sky80.copy(alpha = 0.45f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("📈", fontSize = 16.sp)
                }
                
                Column {
                    Text(
                        text = report.weekName,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 17.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (report.teacherFeedback.isNotBlank()) {
                        Text(
                            text = "رسالة التشجيع: ".loc() + report.teacherFeedback.take(30) + "...",
                            fontSize = 13.sp,
                            color = Emerald40,
                            fontWeight = FontWeight.ExtraBold
                        )
                    } else {
                        Text("اضغط للبدء في تدوين وحفظ رصد الأيام".loc(), fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f), fontWeight = FontWeight.Medium)
                    }
                }
            }
            
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { showRenameDialog = true }) {
                    Icon(Icons.Default.Edit, contentDescription = "تعديل اسم الأسبوع".loc(), tint = Color.LightGray)
                }
                IconButton(onClick = { showConfirmDelete = true }) {
                    Icon(Icons.Default.Delete, contentDescription = "حذف الأسبوع".loc(), tint = Color.LightGray)
                }
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "عرض السجل".loc(),
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }
    }

    if (showConfirmDelete) {
        AlertDialog(
            onDismissRequest = { showConfirmDelete = false },
            title = { Text("حذف الأسبوع؟".loc(), fontWeight = FontWeight.Bold) },
            text = { Text("هل أنت متأكد من حذف".loc() + " (${report.weekName}) " + "مع كافة عمليات الحفظ المسجلة فيه؟".loc()) },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDelete()
                        showConfirmDelete = false
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("حذف".loc())
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDelete = false }) {
                    Text("إلغاء".loc())
                }
            }
        )
    }

    if (showRenameDialog) {
        var newWeekName by remember { mutableStateOf(report.weekName) }
        AlertDialog(
            onDismissRequest = { showRenameDialog = false },
            title = {
                Text(
                    "تعديل اسم الأسبوع 📝".loc(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Right
                )
            },
            text = {
                OutlinedTextField(
                    value = newWeekName,
                    onValueChange = { newWeekName = it },
                    label = { Text("اسم أسبوع الرصد".loc()) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(textDirection = TextDirection.Rtl, textAlign = TextAlign.Right)
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newWeekName.isNotBlank()) {
                            onUpdate(report.copy(weekName = newWeekName))
                            showRenameDialog = false
                        }
                    }
                ) {
                    Text("حفظ".loc())
                }
            },
            dismissButton = {
                TextButton(onClick = { showRenameDialog = false }) {
                    Text("إلغاء".loc())
                }
            }
        )
    }
}

// ==========================================
// 3. REPORT TRACKING SCREEN (WEEKLY PROGRESS GRID)
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportTrackingScreen(
    viewModel: QuranViewModel,
    student: Student,
    report: WeeklyReport
) {
    val rawReport by viewModel.selectedReport.collectAsStateWithLifecycle()
    val dailyLogs by viewModel.dailyLogs.collectAsStateWithLifecycle()
    val context = LocalContext.current
    
    // We update the local state for feedback
    var feedbackText by remember { mutableStateOf("") }
    
    // Sync feedback text when loaded
    LaunchedEffect(rawReport) {
        rawReport?.let {
            feedbackText = it.teacherFeedback
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(student.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text(report.weekName, fontSize = 11.sp, color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f))
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateTo(Screen.StudentProfile(student)) }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "رجوع".loc())
                    }
                },
                actions = {
                    // WhatsApp-friendly share button
                    IconButton(
                        onClick = {
                            shareWeeklyReportDetails(context, student, report.weekName, dailyLogs, feedbackText)
                        },
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .background(Color.White.copy(alpha = 0.15f), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "مشاركة التقرير مع الوالدين".loc(),
                            tint = Color.White
                        )
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
            // Explanatory Note / Ayah banner
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)),
                    border = BorderStroke(1.6.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "💡 معلومات الاستخدام:".loc(),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "قم بإدخال السورة والآيات لكل يوم. اضغط على شارة التقييم (مثيل: الممتاز) لتبديل التقييم بالنجوم بكل سلاسة (⭐⭐⭐ -> ⭐⭐ -> ⭐ -> ❌ -> لم يرصد).",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Days Tracker Items
            if (dailyLogs.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "📖 لا توجد أيام مسجلة في هذا الأسبوع حتى الآن".loc(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "ابدأ بإضافة يوم جديد لتسجيل حفظ ومراجعة الطالب.".loc(),
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                        )
                    }
                }
            } else {
                items(dailyLogs, key = { it.id }) { log ->
                    DayRecordBox(
                        log = log,
                        onLogChange = { updatedLog ->
                            viewModel.updateDailyLog(updatedLog)
                        },
                        onDeleteClick = {
                            viewModel.deleteDailyLog(log)
                        },
                        checkDateAvailability = { date ->
                            viewModel.checkDateAvailability(
                                weeklyReportId = report.id,
                                studentId = student.id,
                                date = date,
                                excludeLogId = log.id
                            )
                        },
                        studentId = student.id,
                        onAbsenceToggled = { studentId ->
                            viewModel.recalculateDayNumbers(studentId)
                        }
                    )
                }
            }

            // Button to Add Day
            if (dailyLogs.size < 7) {
                item {
                    var showAddDatePicker by remember { mutableStateOf(false) }
                    var showNotesDialog by remember { mutableStateOf(false) }
                    var pendingDate by remember { mutableStateOf(0L) }
                    var addErrorMessage by remember { mutableStateOf<String?>(null) }
                    val coroutineScope = rememberCoroutineScope()

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        OutlinedButton(
                            onClick = { showAddDatePicker = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("add_day_button"),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary),
                            border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("إضافة يوم جديد للأسبوع".loc(), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }

                        if (addErrorMessage != null) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = addErrorMessage!!,
                                color = MaterialTheme.colorScheme.error,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    if (showAddDatePicker) {
                        QuranDatePickerDialog(
                            initialSelectedDateMillis = System.currentTimeMillis(),
                            onDateSelected = { selectedDate ->
                                coroutineScope.launch {
                                    val error = viewModel.checkDateAvailability(
                                        weeklyReportId = report.id,
                                        studentId = student.id,
                                        date = selectedDate
                                    )
                                    if (error != null) {
                                        addErrorMessage = error
                                    } else {
                                        addErrorMessage = null
                                        pendingDate = selectedDate
                                        showNotesDialog = true
                                    }
                                }
                            },
                            onDismiss = { showAddDatePicker = false }
                        )
                    }

                    // Notes dialog after date selection
                    if (showNotesDialog) {
                        var dayNotes by remember { mutableStateOf("") }
                        AlertDialog(
                            onDismissRequest = {
                                showNotesDialog = false
                                viewModel.addDayToWeek(report.id, student.id, pendingDate)
                            },
                            title = {
                                Text(
                                    "📝 ملاحظة لليوم (اختياري)".loc(),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Right
                                )
                            },
                            text = {
                                Column {
                                    Text(
                                        "يمكنك إضافة ملاحظة أو تعليق على هذا اليوم (ستظهر في سجل اليوم ويمكن تعديلها لاحقاً).".loc(),
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    OutlinedTextField(
                                        value = dayNotes,
                                        onValueChange = { dayNotes = it },
                                        placeholder = { Text("مثال: غاب الطالب عن الحلقة بعذر...".loc()) },
                                        modifier = Modifier.fillMaxWidth(),
                                        maxLines = 3,
                                        shape = RoundedCornerShape(10.dp),
                                        textStyle = TextStyle(textDirection = TextDirection.Content, textAlign = TextAlign.Start)
                                    )
                                }
                            },
                            confirmButton = {
                                Button(
                                    onClick = {
                                        showNotesDialog = false
                                        viewModel.addDayToWeek(report.id, student.id, pendingDate, dayNotes)
                                    }
                                ) {
                                    Text("إضافة اليوم".loc())
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = {
                                    showNotesDialog = false
                                    viewModel.addDayToWeek(report.id, student.id, pendingDate)
                                }) {
                                    Text("تخطي".loc())
                                }
                            }
                        )
                    }
                }
            }

            // Teacher Feedback Note Row
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f)),
                    border = BorderStroke(1.8.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.45f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "🌟 ملاحظة غراس المربي وتشجيعه للأسبوع:".loc(),
                            fontWeight = FontWeight.Black,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "تظهر هذه الرسالة في التقرير المشترك مع الوالدين لتحفيز البطل بكلمات طيبة.".loc(),
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = feedbackText,
                            onValueChange = {
                                feedbackText = it
                                rawReport?.copy(teacherFeedback = it)?.let { updated ->
                                    viewModel.updateWeeklyReport(updated)
                                }
                            },
                            placeholder = { Text("اكتب رسالة تشجيعية هنا (مثال: واصل تميزك يا بطل، فخورين بك!)".loc()) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("teacher_feedback_field"),
                            maxLines = 4,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = Color.LightGray
                            ),
                            textStyle = TextStyle(
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                textDirection = TextDirection.Content,
                                textAlign = TextAlign.Start
                            )
                        )
                    }
                }
            }
            
            // Helpful Quick Action Buttons at end
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = { 
                            shareWeeklyReportDetails(context, student, report.weekName, dailyLogs, feedbackText)
                        },
                        modifier = Modifier.weight(1f).testTag("share_summary_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("مشاركة التقرير كملف PDF 📄".loc(), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun DayRecordBox(
    log: DailyLog,
    onLogChange: (DailyLog) -> Unit,
    onDeleteClick: () -> Unit,
    checkDateAvailability: suspend (Long) -> String?,
    studentId: Int = 0,
    onAbsenceToggled: ((Int) -> Unit)? = null
) {
    val coroutineScope = rememberCoroutineScope()
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }

    var localNotes by remember { mutableStateOf(TextFieldValue(log.notes)) }
    LaunchedEffect(log.notes) {
        if (log.notes != localNotes.text) {
            localNotes = TextFieldValue(log.notes)
        }
    }

    LaunchedEffect(log.dayDate) {
        if (log.dayDate != 0L) {
            errorMessage = checkDateAvailability(log.dayDate)
        } else {
            errorMessage = null
        }
    }

    // Card border color changes if absent
    val cardBorderColor = if (log.isAbsent)
        Color(0xFFEF4444).copy(alpha = 0.6f)
    else
        MaterialTheme.colorScheme.primary.copy(alpha = 0.45f)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("day_record_${log.dayName}"),
        colors = CardDefaults.cardColors(
            containerColor = if (log.isAbsent)
                Color(0xFFEF4444).copy(alpha = 0.04f)
            else
                MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.8.dp, cardBorderColor),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // --- Header Row: Day name + date picker + day number badge + delete ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    // Day name + optional sequential number badge
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Surface(
                                color = if (log.isAbsent)
                                    Color(0xFFEF4444).copy(alpha = 0.15f)
                                else
                                    MaterialTheme.colorScheme.primaryContainer,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "يوم ".loc() + log.dayName.loc(),
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 15.sp,
                                    color = if (log.isAbsent) Color(0xFFDC2626)
                                            else MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                            // Sequential day number badge (only for present days)
                            if (!log.isAbsent && log.daySequentialNumber > 0) {
                                Surface(
                                    color = MaterialTheme.colorScheme.secondaryContainer,
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = "📌 #${log.daySequentialNumber}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.secondary,
                                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                                    )
                                }
                            }
                        }
                    }

                    Box(modifier = Modifier.width(160.dp)) {
                        OutlinedTextField(
                            value = if (log.dayDate == 0L) "" else formatLongDate(log.dayDate),
                            onValueChange = { },
                            readOnly = true,
                            isError = errorMessage != null,
                            supportingText = errorMessage?.let { { Text(it, color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold) } },
                            placeholder = { Text("اختر التاريخ 📅".loc(), fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                            singleLine = true,
                            textStyle = TextStyle(
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                textDirection = TextDirection.Rtl,
                                textAlign = TextAlign.Right
                            ),
                            modifier = Modifier.fillMaxWidth().testTag("day_date_input_${log.dayName}"),
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.DateRange,
                                    contentDescription = "اختر التاريخ".loc(),
                                    tint = if (errorMessage != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                                )
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.8f),
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .clickable { showDatePicker = true }
                        )
                    }
                }

                // Delete Day Option
                var showDeleteConfirm by remember { mutableStateOf(false) }
                IconButton(onClick = { showDeleteConfirm = true }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "حذف اليوم".loc(),
                        tint = MaterialTheme.colorScheme.error.copy(alpha = 0.8f)
                    )
                }

                if (showDeleteConfirm) {
                    AlertDialog(
                        onDismissRequest = { showDeleteConfirm = false },
                        title = { Text("حذف اليوم؟".loc(), fontWeight = FontWeight.Bold) },
                        text = { Text("هل أنت متأكد من حذف يوم".loc() + " (${log.dayName.loc()}) " + "وتاريخه وكل بيانات الحفظ والمراجعة المسجلة فيه؟".loc()) },
                        confirmButton = {
                            Button(
                                onClick = {
                                    showDeleteConfirm = false
                                    onDeleteClick()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                            ) {
                                Text("نعم، احذف".loc())
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showDeleteConfirm = false }) {
                                Text("إلغاء".loc())
                            }
                        }
                    )
                }
            }

            if (showDatePicker) {
                QuranDatePickerDialog(
                    initialSelectedDateMillis = if (log.dayDate == 0L) System.currentTimeMillis() else log.dayDate,
                    onDateSelected = { selectedDate ->
                        coroutineScope.launch {
                            val error = checkDateAvailability(selectedDate)
                            if (error != null) {
                                errorMessage = error
                            } else {
                                errorMessage = null
                                onLogChange(log.copy(dayDate = selectedDate))
                            }
                        }
                    },
                    onDismiss = { showDatePicker = false }
                )
            }

            // --- Absence Checkbox Row ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        if (log.isAbsent) Color(0xFFEF4444).copy(alpha = 0.08f)
                        else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    )
                    .border(
                        width = 1.2.dp,
                        color = if (log.isAbsent) Color(0xFFEF4444).copy(alpha = 0.5f)
                                else MaterialTheme.colorScheme.outline.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .clickable {
                        val newLog = log.copy(isAbsent = !log.isAbsent)
                        onLogChange(newLog)
                        if (studentId != 0) onAbsenceToggled?.invoke(studentId)
                    }
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Checkbox(
                    checked = log.isAbsent,
                    onCheckedChange = { checked ->
                        val newLog = log.copy(isAbsent = checked)
                        onLogChange(newLog)
                        if (studentId != 0) onAbsenceToggled?.invoke(studentId)
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color(0xFFEF4444),
                        uncheckedColor = MaterialTheme.colorScheme.outline
                    )
                )
                Text(
                    text = if (log.isAbsent) "🔴 الطالب غائب - لا يحسب في الإحصائيات".loc() else "تسجيل غياب".loc(),
                    fontSize = 13.sp,
                    fontWeight = if (log.isAbsent) FontWeight.Bold else FontWeight.Normal,
                    color = if (log.isAbsent) Color(0xFFDC2626) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }

            // --- Content: hidden if absent ---
            if (!log.isAbsent) {
                Spacer(modifier = Modifier.height(10.dp))

                // Columns layout for New Memorization, Recent, and Distant revision
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    // SECTION 1: الحفظ الجديد
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Emerald80.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                            .border(BorderStroke(1.2.dp, Emerald40.copy(alpha = 0.5f)), RoundedCornerShape(12.dp))
                            .padding(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("🌱 الْحِفْظُ الْجَدِيدُ".loc(), fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = Emerald40)
                            StarRatingCycleButton(
                                stars = log.newMemoStars,
                                onCycle = { nextStars -> onLogChange(log.copy(newMemoStars = nextStars)) }
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            SurahAutoCompleteTextField(value = log.newMemoSurahFrom, onValueChange = { onLogChange(log.copy(newMemoSurahFrom = it)) }, label = "من سورة".loc(), modifier = Modifier.weight(1f))
                            VerseDropdownTextField(value = log.newMemoVerseFrom, onValueChange = { onLogChange(log.copy(newMemoVerseFrom = it)) }, surahName = log.newMemoSurahFrom, label = "الآية".loc(), modifier = Modifier.width(62.dp), imeAction = ImeAction.Next)
                            SurahAutoCompleteTextField(value = log.newMemoSurahTo, onValueChange = { onLogChange(log.copy(newMemoSurahTo = it)) }, label = "إلى سورة".loc(), modifier = Modifier.weight(1f))
                            VerseDropdownTextField(value = log.newMemoVerseTo, onValueChange = { onLogChange(log.copy(newMemoVerseTo = it)) }, surahName = log.newMemoSurahTo, label = "الآية".loc(), modifier = Modifier.width(62.dp), imeAction = ImeAction.Next)
                        }
                    }

                    // SECTION 2: الماضي القريب
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Sky80.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                            .border(BorderStroke(1.2.dp, Sky40.copy(alpha = 0.5f)), RoundedCornerShape(12.dp))
                            .padding(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("💧 الْمَاضِي الْقَرِيبُ".loc(), fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = Sky40)
                            StarRatingCycleButton(
                                stars = log.recentRevStars,
                                onCycle = { nextStars -> onLogChange(log.copy(recentRevStars = nextStars)) }
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            SurahAutoCompleteTextField(value = log.recentRevSurahFrom, onValueChange = { onLogChange(log.copy(recentRevSurahFrom = it)) }, label = "من سورة".loc(), modifier = Modifier.weight(1f))
                            VerseDropdownTextField(value = log.recentRevVerseFrom, onValueChange = { onLogChange(log.copy(recentRevVerseFrom = it)) }, surahName = log.recentRevSurahFrom, label = "الآية".loc(), modifier = Modifier.width(62.dp), imeAction = ImeAction.Next)
                            SurahAutoCompleteTextField(value = log.recentRevSurahTo, onValueChange = { onLogChange(log.copy(recentRevSurahTo = it)) }, label = "إلى سورة".loc(), modifier = Modifier.weight(1f))
                            VerseDropdownTextField(value = log.recentRevVerseTo, onValueChange = { onLogChange(log.copy(recentRevVerseTo = it)) }, surahName = log.recentRevSurahTo, label = "الآية".loc(), modifier = Modifier.width(62.dp), imeAction = ImeAction.Next)
                        }
                    }

                    // SECTION 3: الماضي البعيد
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Amber80.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                            .border(BorderStroke(1.2.dp, Amber40.copy(alpha = 0.5f)), RoundedCornerShape(12.dp))
                            .padding(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("🔥 الْمَاضِي الْبَعِيدُ".loc(), fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = Amber40)
                            StarRatingCycleButton(
                                stars = log.distantRevStars,
                                onCycle = { nextStars -> onLogChange(log.copy(distantRevStars = nextStars)) }
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            SurahAutoCompleteTextField(value = log.distantRevSurahFrom, onValueChange = { onLogChange(log.copy(distantRevSurahFrom = it)) }, label = "من سورة".loc(), modifier = Modifier.weight(1f))
                            VerseDropdownTextField(value = log.distantRevVerseFrom, onValueChange = { onLogChange(log.copy(distantRevVerseFrom = it)) }, surahName = log.distantRevSurahFrom, label = "الآية".loc(), modifier = Modifier.width(62.dp), imeAction = ImeAction.Next)
                            SurahAutoCompleteTextField(value = log.distantRevSurahTo, onValueChange = { onLogChange(log.copy(distantRevSurahTo = it)) }, label = "إلى سورة".loc(), modifier = Modifier.weight(1f))
                            VerseDropdownTextField(value = log.distantRevVerseTo, onValueChange = { onLogChange(log.copy(distantRevVerseTo = it)) }, surahName = log.distantRevSurahTo, label = "الآية".loc(), modifier = Modifier.width(62.dp), imeAction = ImeAction.Done)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // SECTION 4: ملاحظات اليوم - fixed cursor direction (RTL Start)
                OutlinedTextField(
                    value = localNotes,
                    onValueChange = { newValue ->
                        localNotes = newValue
                        onLogChange(log.copy(notes = newValue.text))
                    },
                    placeholder = { Text("📝 أضف ملاحظة لليوم (اختياري)...".loc(), fontSize = 12.sp, fontWeight = FontWeight.Medium) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("day_notes_field_${log.dayName}"),
                    maxLines = 3,
                    textStyle = TextStyle(
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        textDirection = TextDirection.Content,
                        textAlign = TextAlign.Start
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(10.dp)
                )
            } else {
                // Absent day visual indicator
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFEF4444).copy(alpha = 0.06f), RoundedCornerShape(10.dp))
                        .border(1.dp, Color(0xFFEF4444).copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "📍 تم تسجيل غياب الطالب - لا يُحتسب في الأيام المرصودة والتقييم ونسبة الانتظام".loc(),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFDC2626),
                        textAlign = TextAlign.Center,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}

@Composable
fun StarRatingCycleButton(
    stars: Int,
    onCycle: (Int) -> Unit
) {
    // 0 = لم يرصد, 1 = جيد, 2 = جيد جدا, 3 = ممتاز, 4 = يحتاج مراجعة (❌)
    val textAndColor = when (stars) {
        3 -> Pair("امتياز ⭐⭐⭐".loc(), Emerald40)
        2 -> Pair("جيد جداً ⭐⭐".loc(), Sky40)
        1 -> Pair("جيد وطيب ⭐".loc(), Amber40)
        4 -> Pair("إعادة تسميع ❌".loc(), Color.Red)
        else -> Pair("لم يـرصد ⚪".loc(), Color.Gray)
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(textAndColor.second.copy(alpha = 0.15f))
            .border(BorderStroke(1.dp, textAndColor.second.copy(alpha = 0.5f)), RoundedCornerShape(8.dp))
            .clickable {
                val next = when (stars) {
                    3 -> 2
                    2 -> 1
                    1 -> 4
                    4 -> 0
                    else -> 3
                }
                onCycle(next)
            }
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = textAndColor.first,
            color = textAndColor.second,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

// Helper to format and share report as a beautiful PDF Document
private fun shareWeeklyReportDetails(
    context: Context,
    student: Student,
    weekName: String,
    logs: List<DailyLog>,
    notes: String
) {
    val pdfDocument = PdfDocument()
    // A4 Lanscape size in Postscript points (842 x 595)
    val pageInfo = PdfDocument.PageInfo.Builder(842, 595, 1).create()
    val page = pdfDocument.startPage(pageInfo)
    val canvas = page.canvas

    // --- Core Colors ---
    val colorPrimary = android.graphics.Color.parseColor("#059669") // Emerald primary
    val colorSlateDark = android.graphics.Color.parseColor("#1E293B") // Dark text
    val colorSlateLabel = android.graphics.Color.parseColor("#475569") // Light labels
    val colorBorder = android.graphics.Color.parseColor("#64748B") // Thicker/Darker Slate grid line border

    // 0. Draw Logo Watermark in center background
    try {
        val logoBitmap = BitmapFactory.decodeResource(context.resources, com.example.R.drawable.app_logo)
        if (logoBitmap != null) {
            val watermarkSize = 220f
            val scaledLogo = Bitmap.createScaledBitmap(logoBitmap, watermarkSize.toInt(), watermarkSize.toInt(), true)
            val watermarkPaint = Paint().apply {
                alpha = 30 // ~12% opacity for subtle watermark
                isAntiAlias = true
            }
            val centerX = (842f - watermarkSize) / 2f
            val centerY = 460f - (watermarkSize / 2f)
            canvas.drawBitmap(scaledLogo, centerX, centerY, watermarkPaint)
        }
    } catch (_: Exception) { /* Skip watermark if logo not found */ }

    // 1. Draw Green Title Header Banner
    val topBannerPaint = Paint().apply {
        color = colorPrimary
        style = Paint.Style.FILL
        isAntiAlias = true
    }
    val bannerRect = android.graphics.RectF(25f, 15f, 817f, 75f)
    canvas.drawRoundRect(bannerRect, 12f, 12f, topBannerPaint)

    // Outer framing border for aesthetic look
    val outerFramingBorder = Paint().apply {
        color = colorPrimary
        style = Paint.Style.STROKE
        strokeWidth = 1.8f
        isAntiAlias = true
    }
    canvas.drawRect(15f, 10f, 827f, 585f, outerFramingBorder)

    val bannerTitlePaint = Paint().apply {
        color = android.graphics.Color.WHITE
        textSize = 21f
        isAntiAlias = true
        typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
        textAlign = Paint.Align.CENTER
    }
    canvas.drawText("تيجان النور لِمُتَابَعَةِ تِلَاوَةِ وَحِفْظِ الْقُرْآنِ الْكَرِيمِ 📖".loc(), 421f, 50f, bannerTitlePaint)

    // 2. Draw Student Profile Card
    val infoBgPaint = Paint().apply {
        color = android.graphics.Color.parseColor("#F0FDF4") // Light sage Tint
        style = Paint.Style.FILL
    }
    val infoBorderPaint = Paint().apply {
        color = android.graphics.Color.parseColor("#059669") // Much higher contrast border
        style = Paint.Style.STROKE
        strokeWidth = 2.2f
        isAntiAlias = true
    }
    val infoRect = android.graphics.RectF(25f, 95f, 817f, 165f)
    canvas.drawRoundRect(infoRect, 10f, 10f, infoBgPaint)
    canvas.drawRoundRect(infoRect, 10f, 10f, infoBorderPaint)

    val infoLabelPaint = Paint().apply {
        color = colorSlateLabel
        textSize = 12f
        isAntiAlias = true
        typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
        textAlign = Paint.Align.RIGHT
    }
    val infoValPaint = Paint().apply {
        color = colorSlateDark
        textSize = 13.5f
        isAntiAlias = true
        typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
        textAlign = Paint.Align.RIGHT
    }

    // Right aligned info blocks
    canvas.drawText("👤 الطَّالِبُ/ـة:".loc(), 800f, 120f, infoLabelPaint)
    canvas.drawText(student.name, 800f, 145f, infoValPaint)

    canvas.drawText("🏫 الصَّفُّ/الْحَلَقَةُ:".loc(), 550f, 120f, infoLabelPaint)
    canvas.drawText(student.groupName.ifBlank { "غير محددة".loc() }, 550f, 145f, infoValPaint)

    canvas.drawText("👤 الْمُعَلِّمُ الْمُرَبِّي:".loc(), 320f, 120f, infoLabelPaint)
    canvas.drawText(student.teacherName.ifBlank { "غير محدد".loc() }, 320f, 145f, infoValPaint)

    canvas.drawText("📅 فَتْرَةُ الرَّصْدِ:".loc(), 120f, 120f, infoLabelPaint)
    canvas.drawText(weekName, 120f, 145f, infoValPaint)

    // 3. Draw Grid/Table
    val headerPaint = Paint().apply {
        color = colorPrimary
        style = Paint.Style.FILL
    }
    // Expanded height for higher contrast text
    canvas.drawRect(25f, 180f, 817f, 218f, headerPaint)

    val headerTextPaint = Paint().apply {
        color = android.graphics.Color.WHITE
        textSize = 12f
        isAntiAlias = true
        typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
        textAlign = Paint.Align.RIGHT
    }

    // Column positions (RTL representation)
    canvas.drawText("رقم اليوم".loc(), 812f, 204f, headerTextPaint)
    canvas.drawText("اليوم والتاريخ".loc(), 770f, 204f, headerTextPaint)
    canvas.drawText("🌱 الْحِفْظُ الْجَدِيدُ".loc(), 650f, 204f, headerTextPaint)
    canvas.drawText("💧 الْمَاضِي الْقَرِيبُ".loc(), 475f, 204f, headerTextPaint)
    canvas.drawText("🔥 الْمَاضِي الْبَعِيدُ".loc(), 300f, 204f, headerTextPaint)
    canvas.drawText("الملاحظات".loc(), 125f, 204f, headerTextPaint)

    val borderPaint = Paint().apply {
        color = colorBorder
        style = Paint.Style.STROKE
        strokeWidth = 1.8f
        isAntiAlias = true
    }

    var currentY = 218f
    val rowHeight = 52f

    logs.forEach { log ->
        val actualRowHeight = 52f
        val isEven = logs.indexOf(log) % 2 == 0
        
        // Reddish background for absent days, grey/white alternating for present days
        val rowBgPaint = Paint().apply {
            color = if (log.isAbsent) {
                android.graphics.Color.parseColor("#FEF2F2") // Light red
            } else if (isEven) {
                android.graphics.Color.parseColor("#F1F5F9")
            } else {
                android.graphics.Color.WHITE
            }
            style = Paint.Style.FILL
        }
        canvas.drawRect(25f, currentY, 817f, currentY + actualRowHeight, rowBgPaint)
        
        // Draw horizontal row bottom line
        canvas.drawLine(25f, currentY + actualRowHeight, 817f, currentY + actualRowHeight, borderPaint)

        // Draw Column 1: Sequential Day Number
        val seqPaint = Paint().apply {
            color = colorSlateDark
            textSize = 12f
            isAntiAlias = true
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            textAlign = Paint.Align.RIGHT
        }
        if (!log.isAbsent && log.daySequentialNumber > 0) {
            canvas.drawText("#${log.daySequentialNumber}", 810f, currentY + 31f, seqPaint)
        } else {
            canvas.drawText("—", 810f, currentY + 31f, seqPaint)
        }

        // Draw Column 2: Day Name & Date
        val boldDayPaint = Paint().apply {
            color = if (log.isAbsent) android.graphics.Color.parseColor("#DC2626") else colorSlateDark
            textSize = 12.5f
            isAntiAlias = true
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            textAlign = Paint.Align.RIGHT
        }
        val normalDatePaint = Paint().apply {
            color = colorSlateLabel
            textSize = 10.5f
            isAntiAlias = true
            textAlign = Paint.Align.RIGHT
        }
        canvas.drawText(log.dayName.loc(), 770f, currentY + 22f, boldDayPaint)
        if (log.dayDate != 0L) {
            canvas.drawText(formatLongDate(log.dayDate), 770f, currentY + 41f, normalDatePaint)
        } else {
            canvas.drawText("—", 770f, currentY + 41f, normalDatePaint)
        }

        // Draw Column 6: Notes
        val notePaint = Paint().apply {
            color = if (log.isAbsent) android.graphics.Color.parseColor("#DC2626") else android.graphics.Color.parseColor("#B45309")
            textSize = 10f
            isAntiAlias = true
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.ITALIC)
            textAlign = Paint.Align.RIGHT
        }
        val displayNote = if (log.notes.isNotBlank()) {
            if (log.notes.length > 20) log.notes.take(17) + "..." else log.notes
        } else if (log.isAbsent) {
            "غائب"
        } else {
            "—"
        }
        canvas.drawText(displayNote, 125f, currentY + 31f, notePaint)

        // Draw Cell Function
        fun drawCell(
            surahFrom: String, verseFrom: String,
            surahTo: String, verseTo: String,
            stars: Int,
            rightX: Float,
            colorHex: String
        ) {
            if (log.isAbsent) {
                val absentTextPaint = Paint().apply {
                    color = android.graphics.Color.parseColor("#EF4444")
                    textSize = 11.5f
                    isAntiAlias = true
                    typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
                    textAlign = Paint.Align.RIGHT
                }
                canvas.drawText("غائب 🔴", rightX, currentY + 31f, absentTextPaint)
                return
            }

            val hasContent = surahFrom.isNotBlank() || surahTo.isNotBlank()
            if (hasContent) {
                val fromText = if (surahFrom.isNotBlank()) "من $surahFrom" + (if (verseFrom.isNotBlank()) " ($verseFrom)" else "") else ""
                val toText = if (surahTo.isNotBlank()) "إلى $surahTo" + (if (verseTo.isNotBlank()) " ($verseTo)" else "") else ""
                val combinedText = "$fromText $toText".trim()

                val contentPaint = Paint().apply {
                    color = colorSlateDark
                    textSize = 11f
                    isAntiAlias = true
                    textAlign = Paint.Align.RIGHT
                }
                canvas.drawText(combinedText, rightX, currentY + 22f, contentPaint)

                val ratingText = when (stars) {
                    3 -> "امتيـاز ⭐⭐⭐"
                    2 -> "جيد جداً ⭐⭐"
                    1 -> "جيد وطيب ⭐"
                    4 -> "إعادة تسميع ❌"
                    else -> "لم يعقد"
                }
                val ratingPaint = Paint().apply {
                    color = android.graphics.Color.parseColor(colorHex)
                    textSize = 10.5f
                    isAntiAlias = true
                    typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
                    textAlign = Paint.Align.RIGHT
                }
                canvas.drawText(ratingText, rightX, currentY + 41f, ratingPaint)
            } else {
                val spacerPaint = Paint().apply {
                    color = android.graphics.Color.parseColor("#64748B")
                    textSize = 13.5f
                    isAntiAlias = true
                    textAlign = Paint.Align.RIGHT
                }
                canvas.drawText("—", rightX, currentY + 31f, spacerPaint)
            }
        }

        // C3: New Memory (Green)
        drawCell(
            log.newMemoSurahFrom, log.newMemoVerseFrom,
            log.newMemoSurahTo, log.newMemoVerseTo,
            log.newMemoStars,
            650f,
            "#059669"
        )

        // C4: Recent Rev (Blue)
        drawCell(
            log.recentRevSurahFrom, log.recentRevVerseFrom,
            log.recentRevSurahTo, log.recentRevVerseTo,
            log.recentRevStars,
            475f,
            "#0EA5E9"
        )

        // C5: Distant Rev (Amber)
        drawCell(
            log.distantRevSurahFrom, log.distantRevVerseFrom,
            log.distantRevSurahTo, log.distantRevVerseTo,
            log.distantRevStars,
            300f,
            "#D97706"
        )

        currentY += actualRowHeight
    }

    // Draw Vertical columns borders
    canvas.drawLine(25f, 180f, 25f, currentY, borderPaint)
    canvas.drawLine(135f, 180f, 135f, currentY, borderPaint)
    canvas.drawLine(310f, 180f, 310f, currentY, borderPaint)
    canvas.drawLine(485f, 180f, 485f, currentY, borderPaint)
    canvas.drawLine(660f, 180f, 660f, currentY, borderPaint)
    canvas.drawLine(780f, 180f, 780f, currentY, borderPaint)
    canvas.drawLine(817f, 180f, 817f, currentY, borderPaint)

    // 4. Draw Educator Comments at the bottom
    if (notes.isNotBlank()) {
        val commentY = currentY + 12f
        val commentBg = Paint().apply {
            color = android.graphics.Color.parseColor("#FFFBEB") // Very light Amber
            style = Paint.Style.FILL
        }
        val commentBorder = Paint().apply {
            color = android.graphics.Color.parseColor("#F59E0B") // Much deeper Amber outline
            style = Paint.Style.STROKE
            strokeWidth = 1.8f
            isAntiAlias = true
        }
        val rectNote = android.graphics.RectF(25f, commentY, 817f, commentY + 54f)
        canvas.drawRoundRect(rectNote, 8f, 8f, commentBg)
        canvas.drawRoundRect(rectNote, 8f, 8f, commentBorder)

        val noteLabelPaint = Paint().apply {
            color = android.graphics.Color.parseColor("#B45309")
            textSize = 12f
            isAntiAlias = true
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            textAlign = Paint.Align.RIGHT
        }
        val noteValPaint = Paint().apply {
            color = colorSlateDark
            textSize = 11.5f
            isAntiAlias = true
            textAlign = Paint.Align.RIGHT
        }
        canvas.drawText("💌 تَوْجِيهُ وَمُلَاحَظَاتُ الْمُرَبِّي لِلأُسْبُوعِ:".loc(), 805f, commentY + 20f, noteLabelPaint)
        
        val displayNote = if (notes.length > 105) notes.take(105) + "..." else notes
        canvas.drawText("\"$displayNote\"", 805f, commentY + 40f, noteValPaint)
    }

    // 5. Beautiful Quranic Slogan Footer
    val sloganPaint = Paint().apply {
        color = colorPrimary
        textSize = 12f
        isAntiAlias = true
        typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.ITALIC)
        textAlign = Paint.Align.CENTER
    }
    canvas.drawText("📖 «خَيْرُكُمْ مَنْ تَعَلَّمَ القُرْآنَ وَعَلَّمَهُ» - تم التصدير كملف PDF رقمي عبر تطبيق تيجان النور 📖".loc(), 421f, 575f, sloganPaint)

    pdfDocument.finishPage(page)

    // Save PDF and trigger secure Android File Sharing Share chooser.
    val cacheFile = File(context.cacheDir, "تقرير_متابعة_${student.name.replace(" ", "_")}.pdf")
    try {
        val fileOutputStream = FileOutputStream(cacheFile)
        pdfDocument.writeTo(fileOutputStream)
        pdfDocument.close()
        fileOutputStream.close()

        val authority = "${context.packageName}.fileprovider"
        val shareUri = FileProvider.getUriForFile(context, authority, cacheFile)

        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, shareUri)
            putExtra(Intent.EXTRA_SUBJECT, "تيجان النور: ".loc() + student.name)
            putExtra(Intent.EXTRA_TEXT, "السلام عليكم ورحمة الله وبركاته، نرسل لكم تقرير مستوى تسميع ومتابعة القرآن الكريم لـ الأسبوع ".loc() + " (${weekName}) " + "للطالب البطل: ".loc() + " ${student.name}. " + "نسأل الله أن يجعله من أهل القرآن.".loc())
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        val shareIntent = Intent.createChooser(intent, "تصدير ومشاركة تقرير PDF:".loc())
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(shareIntent)

    } catch (e: Exception) {
        e.printStackTrace()
    }
}

// ==========================================
// 4. BEAUTIFUL BRAND SPLASH SCREEN
// ==========================================
@Composable
fun SplashScreen(viewModel: QuranViewModel) {
    var startFadeIn by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        startFadeIn = true
    }
    
    // A beautiful animated transition for scale and alpha
    val scale by animateFloatAsState(
        targetValue = if (startFadeIn) 1f else 0.8f,
        animationSpec = tween(durationMillis = 1500, easing = LinearOutSlowInEasing),
        label = "scale"
    )
    val alpha by animateFloatAsState(
        targetValue = if (startFadeIn) 1f else 0f,
        animationSpec = tween(durationMillis = 1500),
        label = "alpha"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "halo_transition")
    val glowScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowScale"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0D1527), // Deep royal navy background matching the uploaded icon
                        Color(0xFF1E294B), // Rich royal navy middle
                        Color(0xFF090D1A)  // Dark deep navy slate
                    )
                )
            )
            .padding(24.dp)
            .testTag("splash_screen_container"),
        contentAlignment = Alignment.Center
    ) {
        // --- Navy Arabesque Geometric Mandala Backdrop ---
        androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
            val drawCenter = center
            drawCircle(
                color = Color(0xFF1E293B).copy(alpha = 0.15f),
                radius = 350f,
                center = drawCenter.copy(y = drawCenter.y - 100f),
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2f)
            )
            drawCircle(
                color = Color(0xFF1E293B).copy(alpha = 0.1f),
                radius = 450f,
                center = drawCenter.copy(y = drawCenter.y - 100f),
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.5f)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.offset(y = (-30).dp)
        ) {
            // Halo glow for the app icon center
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(260.dp)
                    .graphicsLayer(
                        scaleX = scale * glowScale,
                        scaleY = scale * glowScale,
                        alpha = alpha
                    )
            ) {
                // Glow aura container
                Box(
                    modifier = Modifier
                        .size(240.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFFFFFBEB).copy(alpha = 0.45f), // Golden light
                                    Color(0xFFF59E0B).copy(alpha = 0.15f), // Gold center
                                    Color.Transparent
                                )
                            )
                        )
                )

                // App Logo Image
                Card(
                    modifier = Modifier
                        .size(195.dp)
                        .border(
                            BorderStroke(4.dp, Brush.linearGradient(listOf(Color(0xFFF59E0B), Color(0xFFFFFBEB), Color(0xFFD97706)))),
                            RoundedCornerShape(32.dp)
                        ),
                    shape = RoundedCornerShape(32.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.app_logo),
                        contentDescription = "تيجان النور".loc(),
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Application Title Ribbon "تيجان النور" with glowing Gold / White Typography
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF059669).copy(alpha = 0.9f)),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.5.dp, Color(0xFFFDE68A).copy(alpha = 0.8f)),
                modifier = Modifier
                    .graphicsLayer(alpha = alpha, scaleX = scale, scaleY = scale)
            ) {
                Text(
                    text = "تيجان النور 📖".loc(),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 10.dp),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Description with soft emerald typography
            Text(
                text = "متابعة تلاوة وحفظ القرآن الكريم للطلاب والناشئة\n«مَن تَعَلَّمَ القُرْآنَ وَعَلَّمَهُ»".loc(),
                fontSize = 13.sp,
                color = Color(0xFFA7F3D0),
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp,
                modifier = Modifier
                    .graphicsLayer(alpha = alpha)
                    .padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Modern circular loading indicator
            CircularProgressIndicator(
                color = Color(0xFFF59E0B),
                strokeWidth = 3.dp,
                modifier = Modifier.size(28.dp)
            )
        }

        // Action exit button
        Button(
            onClick = { viewModel.navigateTo(Screen.StudentsList) },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF059669),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(50),
            border = BorderStroke(1.dp, Color(0xFFFDE68A)),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 36.dp)
                .height(48.dp)
                .width(180.dp)
                .graphicsLayer(alpha = alpha)
                .testTag("skip_splash_button")
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "الدخول للتطبيق".loc(),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    }
}

// 114 Quran Surahs list in Arabic
val quranSurahs = listOf(
    "الفاتحة", "البقرة", "آل عمران", "النساء", "المائدة", "الأنعام", "الأعراف", "الأنفال", "التوبة", "يونس",
    "هود", "يوسف", "الرعد", "إبراهيم", "الحجر", "النحل", "الإسراء", "الكهف", "مريم", "طه",
    "الأنبياء", "الحج", "المؤمنون", "النور", "الفرقان", "الشعراء", "النمل", "القصص", "العنكبوت", "الروم",
    "لقمان", "السجدة", "الأحزاب", "سبأ", "فاطر", "يس", "الصافات", "ص", "الزمر", "غافر",
    "فصلت", "الشورى", "الزخرف", "الدخان", "الجاثية", "الأحقاف", "محمد", "الفتح", "الحجرات", "ق",
    "الذاريات", "الطور", "النجم", "القمر", "الرحمن", "الواقعة", "الحديد", "المجادلة", "الحشر", "الممتحنة",
    "الصف", "الجمعة", "المنافقون", "التغابن", "الطلاق", "التحريم", "الملك", "القلم", "الحاقة", "المعارج",
    "نوح", "الجن", "المزمل", "المدثر", "القيامة", "الإنسان", "المرسلات", "النبأ", "النازعات", "عبس",
    "التكوير", "الانفطار", "المطففين", "الانشقاق", "البروج", "الطارق", "الأعلى", "الغاشية", "الفجر", "البلد",
    "الشمس", "الليل", "الضحى", "الشرح", "التين", "العلق", "القدر", "البينة", "الزلزلة", "العاديات",
    "القارعة", "التكاثر", "العصر", "الهمزة", "الفيل", "قريش", "الماعون", "الكوثر", "الكافرون", "النصر",
    "المسد", "الإخلاص", "الفلق", "الناس"
)

// Map of Surah name to its total number of verses
val surahVersesCount = mapOf(
    "الفاتحة" to 7, "البقرة" to 286, "آل عمران" to 200, "النساء" to 176, "المائدة" to 120,
    "الأنعام" to 165, "الأعراف" to 206, "الأنفال" to 75, "التوبة" to 129, "يونس" to 109,
    "هود" to 123, "يوسف" to 111, "الرعد" to 43, "إبراهيم" to 52, "الحجر" to 99,
    "النحل" to 128, "الإسراء" to 111, "الكهف" to 110, "مريم" to 98, "طه" to 135,
    "الأنبياء" to 112, "الحج" to 78, "المؤمنون" to 118, "النور" to 64, "الفرقان" to 77,
    "الشعراء" to 227, "النمل" to 93, "القصص" to 88, "العنكبوت" to 69, "الروم" to 60,
    "لقمان" to 34, "السجدة" to 30, "الأحزاب" to 73, "سبأ" to 54, "فاطر" to 45,
    "يس" to 83, "الصافات" to 182, "ص" to 88, "الزمر" to 75, "غافر" to 85,
    "فصلت" to 54, "الشورى" to 53, "الزخرف" to 89, "الدخان" to 59, "الجاثية" to 37,
    "الأحقاف" to 35, "محمد" to 38, "الفتح" to 29, "الحجرات" to 18, "ق" to 45,
    "الذاريات" to 60, "الطور" to 49, "النجم" to 62, "القمر" to 55, "الرحمن" to 78,
    "الواقعة" to 96, "الحديد" to 29, "المجادلة" to 22, "الحشر" to 24, "الممتحنة" to 13,
    "الصف" to 14, "الجمعة" to 11, "المنافقون" to 11, "التغابن" to 18, "الطلاق" to 12,
    "التحريم" to 12, "الملك" to 30, "القلم" to 52, "الحاقة" to 52, "المعارج" to 44,
    "نوح" to 28, "الجن" to 28, "المزمل" to 20, "المدثر" to 56, "القيامة" to 40,
    "الإنسان" to 31, "المرسلات" to 50, "النبأ" to 40, "النازعات" to 46, "عبس" to 42,
    "التكوير" to 29, "الانفطار" to 19, "المطففين" to 36, "الانشقاق" to 25, "البروج" to 22,
    "الطارق" to 17, "الأعلى" to 19, "الغاشية" to 26, "الفجر" to 30, "البلد" to 20,
    "الشمس" to 15, "الليل" to 21, "الضحى" to 11, "الشرح" to 8, "التين" to 8,
    "العلق" to 19, "القدر" to 5, "البينة" to 8, "الزلزلة" to 8, "العاديات" to 11,
    "القارعة" to 11, "التكاثر" to 8, "العصر" to 3, "الهمزة" to 9, "الفيل" to 5,
    "قريش" to 4, "الماعون" to 7, "الكوثر" to 3, "الكافرون" to 6, "النصر" to 3,
    "المسد" to 5, "الإخلاص" to 4, "الفلق" to 5, "الناس" to 6
)

// Helper function to get verse count by Surah name (with Arabic normalization)
fun getSurahVerseCount(surahName: String): Int? {
    val normalizedInput = surahName.trim()
        .replace("أ", "ا")
        .replace("إ", "ا")
        .replace("آ", "ا")
        .replace("ة", "ه")
    
    val matchedSurah = quranSurahs.firstOrNull { surah ->
        val normalizedSurah = surah.replace("أ", "ا")
                                   .replace("إ", "ا")
                                   .replace("آ", "ا")
                                   .replace("ة", "ه")
        normalizedSurah == normalizedInput
    }
    return matchedSurah?.let { surahVersesCount[it] }
}

// Custom dropdown text field for verse entry
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerseDropdownTextField(
    value: String,
    onValueChange: (String) -> Unit,
    surahName: String,
    label: String,
    modifier: Modifier = Modifier,
    imeAction: ImeAction = ImeAction.Next
) {
    val focusRequester = remember { FocusRequester() }
    var expanded by remember { mutableStateOf(false) }
    var localValue by remember { mutableStateOf(TextFieldValue(value)) }

    LaunchedEffect(value) {
        if (value != localValue.text) {
            localValue = TextFieldValue(value)
        }
    }
    
    val maxVerse = remember(surahName) { getSurahVerseCount(surahName) ?: 0 }
    
    val filteredVerses = remember(maxVerse, localValue) {
        if (maxVerse <= 0) {
            emptyList()
        } else {
            val all = (1..maxVerse).map { it.toString() }
            if (localValue.text.isBlank()) {
                all
            } else {
                all.filter { it.startsWith(localValue.text) || it.contains(localValue.text) }
            }
        }
    }
    
    ExposedDropdownMenuBox(
        expanded = expanded && filteredVerses.isNotEmpty(),
        onExpandedChange = { 
            expanded = it 
            focusRequester.requestFocus()
        },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = localValue,
            onValueChange = { newValue ->
                localValue = newValue
                if (newValue.text.isEmpty()) {
                    onValueChange(newValue.text)
                    expanded = true
                } else if (newValue.text.all { c -> c.isDigit() }) {
                    val num = newValue.text.toIntOrNull()
                    if (num != null) {
                        if (maxVerse <= 0 || num <= maxVerse || newValue.text.length < value.length) {
                            onValueChange(newValue.text)
                            expanded = true
                        }
                    }
                }
            },
            label = { Text(label, fontSize = 12.sp, fontWeight = FontWeight.Bold) },
            singleLine = true,
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryEditable)
                .focusRequester(focusRequester),
            textStyle = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                textDirection = TextDirection.Rtl,
                textAlign = TextAlign.Right
            ),
            shape = RoundedCornerShape(8.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = imeAction),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            )
        )
        
        if (filteredVerses.isNotEmpty()) {
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.heightIn(max = 200.dp).widthIn(min = 80.dp)
            ) {
                filteredVerses.forEach { verse ->
                    DropdownMenuItem(
                        text = { Text(verse, fontWeight = FontWeight.Bold) },
                        onClick = {
                            localValue = TextFieldValue(verse)
                            onValueChange(verse)
                            expanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
            }
        }
    }
}


// Auto-complete field for Surah names
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurahAutoCompleteTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }
    var expanded by remember { mutableStateOf(false) }
    var localText by remember { mutableStateOf(value) }
    
    LaunchedEffect(value) {
        if (value != localText) {
            localText = value
        }
    }

    val filteredSurahs = remember(localText) {
        if (localText.isBlank()) {
            quranSurahs
        } else {
            val normalizedValue = localText.replace("أ", "ا")
                                          .replace("إ", "ا")
                                          .replace("آ", "ا")
                                          .replace("ة", "ه")
            quranSurahs.filter { surah ->
                val normalizedSurah = surah.replace("أ", "ا")
                                           .replace("إ", "ا")
                                           .replace("آ", "ا")
                                           .replace("ة", "ه")
                normalizedSurah.contains(normalizedValue, ignoreCase = true)
            }
        }
    }

    ExposedDropdownMenuBox(
        expanded = expanded && filteredSurahs.isNotEmpty(),
        onExpandedChange = { 
            expanded = it 
            focusRequester.requestFocus()
        },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = localText,
            onValueChange = {
                localText = it
                onValueChange(it)
                expanded = true
            },
            label = { Text(label, fontSize = 12.sp, fontWeight = FontWeight.Bold) },
            singleLine = true,
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryEditable)
                .focusRequester(focusRequester),
            textStyle = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                textDirection = TextDirection.Rtl,
                textAlign = TextAlign.Right
            ),
            shape = RoundedCornerShape(8.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            )
        )

        ExposedDropdownMenu(
            expanded = expanded && filteredSurahs.isNotEmpty(),
            onDismissRequest = { expanded = false },
            modifier = Modifier.heightIn(max = 250.dp)
        ) {
            filteredSurahs.forEach { surah ->
                DropdownMenuItem(
                    text = { Text(surah, fontWeight = FontWeight.Bold) },
                    onClick = {
                        localText = surah
                        onValueChange(surah)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}

// Dialog to edit existing student data
@Composable
fun EditStudentDialog(
    student: Student,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, String, String?, String, (String?) -> Unit) -> Unit
) {
    val context = LocalContext.current
    var name by remember { mutableStateOf(TextFieldValue(student.name)) }
    var group by remember { mutableStateOf(TextFieldValue(student.groupName)) }
    var teacher by remember { mutableStateOf(TextFieldValue(student.teacherName)) }
    var notes by remember { mutableStateOf(TextFieldValue(student.notes)) }
    var whatsappNumber by remember { mutableStateOf(TextFieldValue(student.whatsappNumber ?: "")) }
    
    val weekDays = listOf("السبت", "الأحد", "الاثنين", "الثلاثاء", "الأربعاء", "الخميس", "الجمعة")
    
    val parsedMap = remember(student.circleSessionDaysTimes) {
        student.circleSessionDaysTimes.split(";")
            .filter { it.isNotBlank() }
            .associate {
                val parts = it.split("=")
                parts.getOrNull(0)?.trim().orEmpty() to parts.getOrNull(1)?.trim().orEmpty()
            }
            .filter { it.key.isNotBlank() && it.value.isNotBlank() }
    }
    
    var selectedDays by remember { mutableStateOf(parsedMap.keys) }
    var dayTimeMap by remember { mutableStateOf(parsedMap) }
    
    var errorMsg by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "تعديل بيانات الطالب 📝".loc(),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Right
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { 
                        name = it
                        if (it.text.isNotBlank()) errorMsg = ""
                    },
                    label = { Text("اسم الطالب الكامل *".loc()) },
                    isError = errorMsg.isNotEmpty(),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(textDirection = TextDirection.Rtl, textAlign = TextAlign.Right)
                )
                if (errorMsg.isNotEmpty()) {
                    Text(errorMsg, color = MaterialTheme.colorScheme.error, fontSize = 10.sp)
                }
                
                OutlinedTextField(
                    value = group,
                    onValueChange = { group = it },
                    label = { Text("الصف / حلقة التحفيظ".loc()) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(textDirection = TextDirection.Rtl, textAlign = TextAlign.Right)
                )
                
                OutlinedTextField(
                    value = teacher,
                    onValueChange = { teacher = it },
                    label = { Text("اسم المعلم المربي".loc()) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(textDirection = TextDirection.Rtl, textAlign = TextAlign.Right)
                )
                
                OutlinedTextField(
                    value = whatsappNumber,
                    onValueChange = { whatsappNumber = it },
                    label = { Text("رقم الواتساب (اختياري، مثلاً +966501234567)".loc()) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    textStyle = TextStyle(textDirection = TextDirection.Ltr, textAlign = TextAlign.Left)
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "مواعيد حلقة الطالب (اختر الأيام والأوقات) ⏰".loc(),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Right
                )
                
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    weekDays.forEach { day ->
                        val isEnabled = day in selectedDays
                        val time = dayTimeMap[day] ?: "18:30"
                        
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (isEnabled) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.15f)
                                    else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
                                )
                                .border(
                                    width = 1.dp,
                                    color = if (isEnabled) MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                                    else MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable {
                                    selectedDays = if (isEnabled) {
                                        selectedDays - day
                                    } else {
                                        selectedDays + day
                                    }
                                }
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Checkbox(
                                    checked = isEnabled,
                                    onCheckedChange = { checked ->
                                        selectedDays = if (checked == true) {
                                            selectedDays + day
                                        } else {
                                            selectedDays - day
                                        }
                                    }
                                )
                                Text(
                                    text = day.loc(),
                                    fontSize = 13.sp,
                                    fontWeight = if (isEnabled) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isEnabled) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.onSurface
                                )
                            }
                            
                            if (isEnabled) {
                                Button(
                                    onClick = {
                                        val parts = time.split(":")
                                        val currentHour = parts.getOrNull(0)?.toIntOrNull() ?: 18
                                        val currentMinute = parts.getOrNull(1)?.toIntOrNull() ?: 30
                                        
                                        android.app.TimePickerDialog(
                                            context,
                                            { _, hour, minute ->
                                                val formattedTime = String.format(java.util.Locale.US, "%02d:%02d", hour, minute)
                                                dayTimeMap = dayTimeMap + (day to formattedTime)
                                            },
                                            currentHour,
                                            currentMinute,
                                            false
                                        ).show()
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                                    ),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                                    modifier = Modifier.height(30.dp)
                                ) {
                                    Text(
                                        text = "${formatTime12h(time)} ⏰",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("ملحوظة".loc()) },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(textDirection = TextDirection.Rtl, textAlign = TextAlign.Right)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.text.isBlank()) {
                        errorMsg = "اسم الطالب مطلوب للرصد".loc()
                    } else {
                        // Serialize selected days and their custom times to "day=time;day=time" sorted chronologically
                        val daysTimesString = weekDays.filter { it in selectedDays }
                            .joinToString(";") { day ->
                                val time = dayTimeMap[day] ?: "18:30"
                                "$day=$time"
                            }
                        onConfirm(name.text, group.text, teacher.text, notes.text, whatsappNumber.text, daysTimesString) { result ->
                            if (result != null) {
                                errorMsg = result
                            } else {
                                errorMsg = ""
                            }
                        }
                    }
                }
            ) {
                Text("حفظ التعديلات".loc())
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("إلغاء".loc())
            }
        }
    )
}

fun formatTime12h(timeStr: String): String {
    if (timeStr.isBlank()) return ""
    return try {
        val parts = timeStr.split(":")
        if (parts.size != 2) return timeStr
        val hour = parts[0].toInt()
        val minute = parts[1].toInt()
        val isEn = AppLang.current == "en"
        val period = if (hour >= 12) {
            if (isEn) "PM" else "م"
        } else {
            if (isEn) "AM" else "ص"
        }
        val hour12 = when {
            hour == 0 -> 12
            hour > 12 -> hour - 12
            else -> hour
        }
        val locale = if (isEn) java.util.Locale.US else java.util.Locale("ar")
        String.format(locale, "%02d:%02d %s", hour12, minute, period)
    } catch (e: Exception) {
        timeStr
    }
}

fun formatStudentCircleDaysTimes(circleSessionDaysTimes: String): String {
    val dayTimeList = circleSessionDaysTimes.split(";")
        .filter { it.isNotBlank() }
        .map {
            val parts = it.split("=")
            val day = parts.getOrNull(0) ?: ""
            val time = parts.getOrNull(1) ?: ""
            day to formatTime12h(time)
        }
        .filter { it.first.isNotBlank() && it.second.isNotBlank() }
    
    if (dayTimeList.isEmpty()) {
        return "غير محدد".loc()
    }
    
    val isEn = AppLang.current == "en"
    val joinSep = if (isEn) ", " else "، "
    
    // Check if all times are the same
    val allSameTime = dayTimeList.map { it.second }.distinct().size == 1
    return if (allSameTime) {
        val weekDays = listOf("السبت", "الأحد", "الاثنين", "الثلاثاء", "الأربعاء", "الخميس", "الجمعة")
        val sortedDays = dayTimeList.map { it.first }.sortedBy { weekDays.indexOf(it) }
        val localizedDays = sortedDays.map { it.loc() }
        "${dayTimeList.first().second} (${localizedDays.joinToString(joinSep)})"
    } else {
        val weekDays = listOf("السبت", "الأحد", "الاثنين", "الثلاثاء", "الأربعاء", "الخميس", "الجمعة")
        val sortedDayTimeList = dayTimeList.sortedBy { weekDays.indexOf(it.first) }
        sortedDayTimeList.joinToString(" | ") { "${it.first.loc()}: ${it.second}" }
    }
}

fun formatStudentCircleDaysTimesCompact(circleSessionDaysTimes: String): String {
    val dayTimeList = circleSessionDaysTimes.split(";")
        .filter { it.isNotBlank() }
        .map {
            val parts = it.split("=")
            val day = parts.getOrNull(0) ?: ""
            val time = parts.getOrNull(1) ?: ""
            day to formatTime12h(time)
        }
        .filter { it.first.isNotBlank() && it.second.isNotBlank() }
    
    if (dayTimeList.isEmpty()) {
        return "غير محدد".loc()
    }
    
    val isEn = AppLang.current == "en"
    val weekDays = listOf("السبت", "الأحد", "الاثنين", "الثلاثاء", "الأربعاء", "الخميس", "الجمعة")
    val shortDaysMap = if (isEn) {
        mapOf(
            "السبت" to "Sat",
            "الأحد" to "Sun",
            "الاثنين" to "Mon",
            "الثلاثاء" to "Tue",
            "الأربعاء" to "Wed",
            "الخميس" to "Thu",
            "الجمعة" to "Fri"
        )
    } else {
        mapOf(
            "السبت" to "سبت",
            "الأحد" to "أحد",
            "الاثنين" to "اثنين",
            "الثلاثاء" to "ثلاثاء",
            "الأربعاء" to "أربعاء",
            "الخميس" to "خميس",
            "الجمعة" to "جمعة"
        )
    }
    
    val joinSep = if (isEn) ", " else "، "

    // Check if all times are the same
    val allSameTime = dayTimeList.map { it.second }.distinct().size == 1
    return if (allSameTime) {
        val sortedDays = dayTimeList.map { it.first }.sortedBy { weekDays.indexOf(it) }
        val shortDays = sortedDays.map { shortDaysMap[it] ?: it }
        "${dayTimeList.first().second} (${shortDays.joinToString(joinSep)})"
    } else {
        val sortedDayTimeList = dayTimeList.sortedBy { weekDays.indexOf(it.first) }
        sortedDayTimeList.joinToString(" | ") { "${shortDaysMap[it.first] ?: it.first}: ${it.second}" }
    }
}

fun wrapText(text: String, paint: Paint, maxWidth: Float): List<String> {
    if (text.isBlank()) return listOf("")
    val tokens = text.split(Regex("(?<=\\s)|(?<=\\s|\\|)|(?<=،)"))
    val lines = mutableListOf<String>()
    var currentLine = StringBuilder()
    
    for (token in tokens) {
        val testLine = currentLine.toString() + token
        val width = paint.measureText(testLine.trim())
        if (width <= maxWidth) {
            currentLine.append(token)
        } else {
            if (currentLine.isNotEmpty()) {
                lines.add(currentLine.toString().trim())
            }
            currentLine = StringBuilder(token)
        }
    }
    if (currentLine.isNotEmpty()) {
        lines.add(currentLine.toString().trim())
    }
    return lines
}

fun formatLongDate(dateMillis: Long): String {
    if (dateMillis == 0L) return "لم يُحدد تاريخ".loc()
    val isEn = AppLang.current == "en"
    val locale = if (isEn) java.util.Locale.US else java.util.Locale("ar")
    val sdf = java.text.SimpleDateFormat("dd/MM/yyyy", locale)
    return try {
        sdf.format(java.util.Date(dateMillis))
    } catch (e: Exception) {
        "لم يُحدد تاريخ".loc()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuranDatePickerDialog(
    initialSelectedDateMillis: Long?,
    onDateSelected: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialSelectedDateMillis ?: System.currentTimeMillis(),
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis <= System.currentTimeMillis()
            }
            override fun isSelectableYear(year: Int): Boolean {
                return year <= java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
            }
        }
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let {
                        onDateSelected(it)
                    }
                    onDismiss()
                }
            ) {
                Text("تأكيد")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("إلغاء".loc())
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PeriodReportScreen(
    viewModel: QuranViewModel,
    student: Student
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // 1. Selection State
    var isGroupReport by remember { mutableStateOf(false) } // false = Student, true = Group
    var startDate by remember { mutableStateOf(System.currentTimeMillis() - 30L * 24 * 60 * 60 * 1000) } // Default 1 month ago
    var endDate by remember { mutableStateOf(System.currentTimeMillis()) } // Default today

    var showStartPicker by remember { mutableStateOf(false) }
    var showEndPicker by remember { mutableStateOf(false) }

    // 2. Data State
    var logs by remember { mutableStateOf<List<DailyLog>>(emptyList()) }
    var allStudents by remember { mutableStateOf<List<Student>>(emptyList()) }
    var weeklyReports by remember { mutableStateOf<List<WeeklyReport>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    // Reload data when dates or scope changes
    LaunchedEffect(isGroupReport, startDate, endDate) {
        isLoading = true
        allStudents = viewModel.getAllStudentsList()
        weeklyReports = viewModel.getAllWeeklyReports()
        logs = if (isGroupReport) {
            viewModel.getDailyLogsForGroupInPeriod(student.groupName, startDate, endDate)
        } else {
            viewModel.getDailyLogsForStudentInPeriod(student.id, startDate, endDate)
        }
        isLoading = false
    }

    // 3. Stats Calculation (Preview) - Exclude absent days
    val presentLogs = logs.filter { !it.isAbsent }
    val totalLoggedDays = presentLogs.size
    val memoSessions = presentLogs.count { it.newMemoSurahFrom.isNotBlank() || it.newMemoSurahTo.isNotBlank() }
    val avgMemoStars = if (memoSessions > 0) {
        presentLogs.filter { it.newMemoSurahFrom.isNotBlank() && it.newMemoStars in 1..3 }.map { it.newMemoStars }.average()
    } else 0.0

    val daysDiff = ((endDate - startDate) / (1000L * 60 * 60 * 24)) + 1
    val attendanceRate = if (daysDiff > 0) {
        (totalLoggedDays.toFloat() / daysDiff.toFloat() * 100f).coerceAtMost(100f)
    } else 0f

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("تقارير الفترات الزمنية 📊", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateTo(Screen.StudentProfile(student)) }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "رجوع".loc())
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Configuration Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("⚙️ إعدادات التقرير:".loc(), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)

                    // Radio Button Scope
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(selected = !isGroupReport, onClick = { isGroupReport = false })
                            Text("طالب واحد".loc() + " (${student.name})", fontWeight = FontWeight.Medium)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(selected = isGroupReport, onClick = { isGroupReport = true })
                            Text("الحلقة كاملة".loc() + " (${student.groupName.ifBlank { "غير محددة".loc() }})", fontWeight = FontWeight.Medium)
                        }
                    }

                    // Date Selectors
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Start Date
                        OutlinedButton(
                            onClick = { showStartPicker = true },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("البداية:".loc() + " ${formatLongDate(startDate)}")
                        }

                        // End Date
                        OutlinedButton(
                            onClick = { showEndPicker = true },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("النهاية:".loc() + " ${formatLongDate(endDate)}")
                        }
                    }

                    // Quick buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val intervals = listOf(
                            "شهر" to 30L,
                            "3 أشهر" to 90L,
                            "6 أشهر" to 180L,
                            "سنة" to 365L
                        )
                        intervals.forEach { (label, days) ->
                            OutlinedButton(
                                onClick = {
                                    endDate = System.currentTimeMillis()
                                    startDate = System.currentTimeMillis() - days * 24 * 60 * 60 * 1000L
                                },
                                modifier = Modifier.weight(1f),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text(label.loc(), fontSize = 11.sp)
                            }
                        }
                    }
                }
            }

            // Stats Preview Card
            Card(
                modifier = Modifier.fillMaxWidth().weight(1f),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
            ) {
                if (isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    Column(
                        modifier = Modifier.padding(16.dp).fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text("📊 معاينة الإحصائيات للفترة:".loc(), fontWeight = FontWeight.Bold)

                        if (logs.isEmpty()) {
                            Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                                Text("لا توجد بيانات مسجلة لهذه الفترة الزمنية 📅".loc(), color = Color.Gray)
                            }
                        } else {
                            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                // 4 Cards of Stats
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    StatMiniCard(
                                        title = "الأيام المرصودة".loc(),
                                        value = "$totalLoggedDays " + " يوم".loc(),
                                        modifier = Modifier.weight(1f)
                                    )
                                    StatMiniCard(
                                        title = "جلسات الحفظ".loc(),
                                        value = "$memoSessions " + " جلسة".loc(),
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    StatMiniCard(
                                        title = "تقييم الحفظ".loc(),
                                        value = String.format("%.1f ⭐", avgMemoStars),
                                        modifier = Modifier.weight(1f)
                                    )
                                    StatMiniCard(
                                        title = "نسبة الانتظام".loc(),
                                        value = String.format("%.0f%%", attendanceRate),
                                        modifier = Modifier.weight(1f)
                                    )
                                }

                                if (isGroupReport) {
                                    Text(
                                        text = "* إحصائيات المعاينة أعلاه تشمل كامل الحلقة المكونة من ".loc() + "${logs.map { it.weeklyReportId }.distinct().size}" + " تقرير أسبوعي.".loc(),
                                        fontSize = 11.sp,
                                        color = Color.Gray
                                    )
                                }
                            }
                        }

                        // Export Button
                        Button(
                            onClick = {
                                val reportIdToStudentMap = weeklyReports.associate { it.id to allStudents.firstOrNull { s -> s.id == it.studentId } }.filterValues { it != null } as Map<Int, Student>
                                generatePeriodReportPdf(
                                    context = context,
                                    student = student,
                                    startDate = startDate,
                                    endDate = endDate,
                                    isGroupReport = isGroupReport,
                                    logs = logs,
                                    reportIdToStudentMap = reportIdToStudentMap
                                )
                            },
                            enabled = logs.isNotEmpty(),
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Icon(Icons.Default.Share, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("تصدير ومشاركة التقرير كملف PDF 📄".loc(), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }

    if (showStartPicker) {
        QuranDatePickerDialog(
            initialSelectedDateMillis = startDate,
            onDateSelected = { startDate = it },
            onDismiss = { showStartPicker = false }
        )
    }

    if (showEndPicker) {
        QuranDatePickerDialog(
            initialSelectedDateMillis = endDate,
            onDateSelected = { endDate = it },
            onDismiss = { showEndPicker = false }
        )
    }
}

@Composable
fun StatMiniCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(title, fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, fontSize = 18.sp, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary)
        }
    }
}

private fun generatePeriodReportPdf(
    context: Context,
    student: Student,
    startDate: Long,
    endDate: Long,
    isGroupReport: Boolean,
    logs: List<DailyLog>,
    reportIdToStudentMap: Map<Int, Student>
) {
    val pdfDocument = PdfDocument()
    val colorPrimary = android.graphics.Color.parseColor("#059669")
    val colorSlateDark = android.graphics.Color.parseColor("#1E293B")
    val colorSlateLabel = android.graphics.Color.parseColor("#475569")
    val colorBorder = android.graphics.Color.parseColor("#64748B")
    
    // Formatting Helpers
    val dateFormat = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale("ar"))
    val dateRangeStr = "${dateFormat.format(java.util.Date(startDate))} - ${dateFormat.format(java.util.Date(endDate))}"

    if (!isGroupReport) {
        // --- 1. SINGLE STUDENT REPORT (LANDSCAPE A4: 842 x 595) ---
        var pageNumber = 1
        var pageInfo = PdfDocument.PageInfo.Builder(842, 595, pageNumber).create()
        var page = pdfDocument.startPage(pageInfo)
        var canvas = page.canvas

        // Load logo bitmap for watermark
        val logoBitmap = try {
            BitmapFactory.decodeResource(context.resources, com.example.R.drawable.app_logo)
        } catch (_: Exception) { null }

        // Header drawing function
        fun drawHeader(c: android.graphics.Canvas, pNum: Int) {
            // Draw Logo Watermark
            if (logoBitmap != null) {
                try {
                    val watermarkSize = 220f
                    val scaledLogo = Bitmap.createScaledBitmap(logoBitmap, watermarkSize.toInt(), watermarkSize.toInt(), true)
                    val watermarkPaint = Paint().apply {
                        alpha = 30
                        isAntiAlias = true
                    }
                    c.drawBitmap(scaledLogo, (842f - watermarkSize) / 2f, 460f - (watermarkSize / 2f), watermarkPaint)
                } catch (_: Exception) {}
            }

            // Draw border
            val outerBorder = Paint().apply {
                color = colorPrimary
                style = Paint.Style.STROKE
                strokeWidth = 1.8f
                isAntiAlias = true
            }
            c.drawRect(15f, 10f, 827f, 585f, outerBorder)

            // Banner
            val bannerPaint = Paint().apply {
                color = colorPrimary
                style = Paint.Style.FILL
                isAntiAlias = true
            }
            c.drawRoundRect(android.graphics.RectF(25f, 15f, 817f, 75f), 12f, 12f, bannerPaint)

            // Title
            val titlePaint = Paint().apply {
                color = android.graphics.Color.WHITE
                textSize = 20f
                isAntiAlias = true
                typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
                textAlign = Paint.Align.CENTER
            }
            c.drawText("تيجان النور: تقرير متابعة الطالب للفترة الزمنية 📊".loc(), 421f, 48f, titlePaint)

            // Footer slogan
            val sloganPaint = Paint().apply {
                color = colorPrimary
                textSize = 10f
                isAntiAlias = true
                typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.ITALIC)
                textAlign = Paint.Align.CENTER
            }
            c.drawText("📖 «خَيْرُكُمْ مَنْ تَعَلَّمَ القُرْآنَ وَعَلَّمَهُ»".loc() + " - " + "تم التصدير عبر تطبيق تيجان النور | صفحة ".loc() + "$pNum 📖", 421f, 575f, sloganPaint)
        }

        drawHeader(canvas, pageNumber)

        // Draw Student metadata
        val infoLabelPaint = Paint().apply {
            color = colorSlateLabel
            textSize = 11f
            isAntiAlias = true
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            textAlign = Paint.Align.RIGHT
        }
        val infoValPaint = Paint().apply {
            color = colorSlateDark
            textSize = 13f
            isAntiAlias = true
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            textAlign = Paint.Align.RIGHT
        }

        canvas.drawText("👤 الطَّالِبُ الْبَطَلُ:".loc(), 800f, 105f, infoLabelPaint)
        canvas.drawText(student.name, 800f, 128f, infoValPaint)

        canvas.drawText("🏫 الْحَلَقَةُ:".loc(), 550f, 105f, infoLabelPaint)
        canvas.drawText(student.groupName.ifBlank { "غير محددة".loc() }, 550f, 128f, infoValPaint)

        canvas.drawText("👤 الْمُعَلِّمُ:".loc(), 350f, 105f, infoLabelPaint)
        canvas.drawText(student.teacherName.ifBlank { "غير محدد".loc() }, 350f, 128f, infoValPaint)

        canvas.drawText("📅 الْفَتْرَةُ الزَّمَنِيَّةُ:".loc(), 180f, 105f, infoLabelPaint)
        canvas.drawText(dateRangeStr, 180f, 128f, infoValPaint)

        // Draw Stats Section (Summary cards inside PDF)
        val cardBgPaint = Paint().apply {
            color = android.graphics.Color.parseColor("#F1F5F9")
            style = Paint.Style.FILL
            isAntiAlias = true
        }
        val cardBorderPaint = Paint().apply {
            color = android.graphics.Color.parseColor("#CBD5E1")
            style = Paint.Style.STROKE
            strokeWidth = 1f
            isAntiAlias = true
        }

        fun drawStatCard(c: android.graphics.Canvas, left: Float, top: Float, width: Float, height: Float, title: String, value: String) {
            val rect = android.graphics.RectF(left, top, left + width, top + height)
            c.drawRoundRect(rect, 8f, 8f, cardBgPaint)
            c.drawRoundRect(rect, 8f, 8f, cardBorderPaint)

            val tPaint = Paint().apply {
                color = colorSlateLabel
                textSize = 9.5f
                isAntiAlias = true
                textAlign = Paint.Align.CENTER
            }
            val vPaint = Paint().apply {
                color = colorPrimary
                textSize = 14f
                isAntiAlias = true
                typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
                textAlign = Paint.Align.CENTER
            }
            c.drawText(title, left + width/2, top + 24f, tPaint)
            c.drawText(value, left + width/2, top + 48f, vPaint)
        }

        // Stats Values - Exclude absent days
        val presentLogs = logs.filter { !it.isAbsent }
        val totalLoggedDays = presentLogs.size
        val memoSessions = presentLogs.count { it.newMemoSurahFrom.isNotBlank() || it.newMemoSurahTo.isNotBlank() }
        val avgMemoStars = if (memoSessions > 0) {
            presentLogs.filter { it.newMemoSurahFrom.isNotBlank() && it.newMemoStars in 1..3 }.map { it.newMemoStars }.average()
        } else 0.0
        val daysDiff = ((endDate - startDate) / (1000L * 60 * 60 * 24)) + 1
        val attendanceRate = if (daysDiff > 0) {
            (totalLoggedDays.toFloat() / daysDiff.toFloat() * 100f).coerceAtMost(100f)
        } else 0f

        drawStatCard(canvas, 640f, 150f, 160f, 60f, "الأيام المرصودة".loc(), "$totalLoggedDays " + "يوم".loc())
        drawStatCard(canvas, 440f, 150f, 160f, 60f, "نسبة الانتظام بالفترة".loc(), String.format("%.0f%%", attendanceRate))
        drawStatCard(canvas, 240f, 150f, 160f, 60f, "جلسات التسميع".loc(), "$memoSessions " + "جلسة".loc())
        drawStatCard(canvas, 40f, 150f, 160f, 60f, "متوسط تقييم الحفظ".loc(), String.format("%.1f / 3 ⭐", avgMemoStars))

        // Table Header
        val tblHeaderPaint = Paint().apply {
            color = colorPrimary
            style = Paint.Style.FILL
        }
        canvas.drawRect(25f, 230f, 817f, 260f, tblHeaderPaint)

        val tblHeaderTextPaint = Paint().apply {
            color = android.graphics.Color.WHITE
            textSize = 11f
            isAntiAlias = true
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            textAlign = Paint.Align.RIGHT
        }
        canvas.drawText("رقم اليوم".loc(), 812f, 249f, tblHeaderTextPaint)
        canvas.drawText("اليوم والتاريخ".loc(), 770f, 249f, tblHeaderTextPaint)
        canvas.drawText("🌱 الْحِفْظُ الْجَدِيدُ".loc(), 650f, 249f, tblHeaderTextPaint)
        canvas.drawText("💧 الْمَاضِي الْقَرِيبُ".loc(), 475f, 249f, tblHeaderTextPaint)
        canvas.drawText("🔥 الْمَاضِي الْبَعِيدُ".loc(), 300f, 249f, tblHeaderTextPaint)
        canvas.drawText("الملاحظات".loc(), 125f, 249f, tblHeaderTextPaint)

        val borderPaint = Paint().apply {
            color = colorBorder
            style = Paint.Style.STROKE
            strokeWidth = 1.2f
            isAntiAlias = true
        }

        var currentY = 260f
        val rowHeight = 44f

        fun drawCell(
            c: android.graphics.Canvas,
            logEntry: DailyLog,
            surahFrom: String, verseFrom: String,
            surahTo: String, verseTo: String,
            stars: Int,
            rightX: Float,
            colorHex: String,
            y: Float
        ) {
            if (logEntry.isAbsent) {
                val absentTextPaint = Paint().apply {
                    color = android.graphics.Color.parseColor("#EF4444")
                    textSize = 10f
                    isAntiAlias = true
                    typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
                    textAlign = Paint.Align.RIGHT
                }
                c.drawText("غائب 🔴", rightX - 10f, y + 26f, absentTextPaint)
                return
            }

            val hasContent = surahFrom.isNotBlank() || surahTo.isNotBlank()
            if (hasContent) {
                val fromText = if (surahFrom.isNotBlank()) "من $surahFrom" + (if (verseFrom.isNotBlank()) " ($verseFrom)" else "") else ""
                val toText = if (surahTo.isNotBlank()) "إلى $surahTo" + (if (verseTo.isNotBlank()) " ($verseTo)" else "") else ""
                val combinedText = "$fromText $toText".trim()

                val contentPaint = Paint().apply {
                    color = colorSlateDark
                    textSize = 10f
                    isAntiAlias = true
                    textAlign = Paint.Align.RIGHT
                }
                c.drawText(combinedText, rightX - 10f, y + 20f, contentPaint)

                // Stars
                val starLabelPaint = Paint().apply {
                    color = android.graphics.Color.parseColor(colorHex)
                    textSize = 9f
                    isAntiAlias = true
                    typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
                    textAlign = Paint.Align.RIGHT
                }
                val starsStr = when (stars) {
                    3 -> "الممتاز ⭐⭐⭐"
                    2 -> "الجيد جداً ⭐⭐"
                    1 -> "المقبول ⭐"
                    4 -> "لم يحفظ ❌"
                    else -> "لم يرصد"
                }
                c.drawText(starsStr.loc(), rightX - 10f, y + 36f, starLabelPaint)
            } else {
                val emptyPaint = Paint().apply {
                    color = android.graphics.Color.argb(120, 128, 128, 128)
                    textSize = 11f
                    isAntiAlias = true
                    textAlign = Paint.Align.RIGHT
                }
                c.drawText("—", rightX - 10f, y + 26f, emptyPaint)
            }
        }

        // Draw Logs Rows
        logs.forEach { log ->
            val actualRowHeight = 44f
            if (currentY + actualRowHeight > 540f) {
                // finish page and start new
                pdfDocument.finishPage(page)
                pageNumber++
                pageInfo = PdfDocument.PageInfo.Builder(842, 595, pageNumber).create()
                page = pdfDocument.startPage(pageInfo)
                canvas = page.canvas

                drawHeader(canvas, pageNumber)

                // Draw Table Header on new page
                canvas.drawRect(25f, 100f, 817f, 130f, tblHeaderPaint)
                canvas.drawText("رقم اليوم".loc(), 812f, 119f, tblHeaderTextPaint)
                canvas.drawText("اليوم والتاريخ".loc(), 770f, 119f, tblHeaderTextPaint)
                canvas.drawText("🌱 الْحِفْظُ الْجَدِيدُ".loc(), 650f, 119f, tblHeaderTextPaint)
                canvas.drawText("💧 الْمَاضِي الْقَرِيبُ".loc(), 475f, 119f, tblHeaderTextPaint)
                canvas.drawText("🔥 الْمَاضِي الْبَعِيدُ".loc(), 300f, 119f, tblHeaderTextPaint)
                canvas.drawText("الملاحظات".loc(), 125f, 119f, tblHeaderTextPaint)

                currentY = 130f
            }

            // Alternating backgrounds (Reddish if absent)
            val isEven = logs.indexOf(log) % 2 == 0
            val rowBgPaint = Paint().apply {
                color = if (log.isAbsent) {
                    android.graphics.Color.parseColor("#FEF2F2") // Light red
                } else if (isEven) {
                    android.graphics.Color.parseColor("#F8FAFC")
                } else {
                    android.graphics.Color.WHITE
                }
                style = Paint.Style.FILL
            }
            canvas.drawRect(25f, currentY, 817f, currentY + actualRowHeight, rowBgPaint)

            // Draw horizontal row bottom line
            canvas.drawLine(25f, currentY + actualRowHeight, 817f, currentY + actualRowHeight, borderPaint)

            // C1: Sequential Number
            val boldSeqPaint = Paint().apply {
                color = colorSlateDark
                textSize = 10.5f
                isAntiAlias = true
                typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
                textAlign = Paint.Align.RIGHT
            }
            if (!log.isAbsent && log.daySequentialNumber > 0) {
                canvas.drawText("#${log.daySequentialNumber}", 812f - 4f, currentY + 26f, boldSeqPaint)
            } else {
                canvas.drawText("—", 812f - 4f, currentY + 26f, boldSeqPaint)
            }

            // C2: Day & Date
            val boldDayPaint = Paint().apply {
                color = if (log.isAbsent) android.graphics.Color.parseColor("#DC2626") else colorSlateDark
                textSize = 10.5f
                isAntiAlias = true
                typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
                textAlign = Paint.Align.RIGHT
            }
            val normalDatePaint = Paint().apply {
                color = colorSlateLabel
                textSize = 9.5f
                isAntiAlias = true
                textAlign = Paint.Align.RIGHT
            }

            canvas.drawText(log.dayName.loc(), 770f - 4f, currentY + 18f, boldDayPaint)
            if (log.dayDate != 0L) {
                canvas.drawText(formatLongDate(log.dayDate), 770f - 4f, currentY + 34f, normalDatePaint)
            } else {
                canvas.drawText("—", 770f - 4f, currentY + 34f, normalDatePaint)
            }

            // C6: Notes
            val notePaint = Paint().apply {
                color = if (log.isAbsent) android.graphics.Color.parseColor("#DC2626") else android.graphics.Color.parseColor("#B45309")
                textSize = 9.5f
                isAntiAlias = true
                typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.ITALIC)
                textAlign = Paint.Align.RIGHT
            }
            val displayNote = if (log.notes.isNotBlank()) {
                if (log.notes.length > 20) log.notes.take(17) + "..." else log.notes
            } else if (log.isAbsent) {
                "غائب"
            } else {
                "—"
            }
            canvas.drawText(displayNote, 125f - 4f, currentY + 26f, notePaint)

            // C3: New Memo
            drawCell(canvas, log, log.newMemoSurahFrom, log.newMemoVerseFrom, log.newMemoSurahTo, log.newMemoVerseTo, log.newMemoStars, 650f, "#059669", currentY)
            // C4: Recent Rev
            drawCell(canvas, log, log.recentRevSurahFrom, log.recentRevVerseFrom, log.recentRevSurahTo, log.recentRevVerseTo, log.recentRevStars, 475f, "#0EA5E9", currentY)
            // C5: Distant Rev
            drawCell(canvas, log, log.distantRevSurahFrom, log.distantRevVerseFrom, log.distantRevSurahTo, log.distantRevVerseTo, log.distantRevStars, 300f, "#D97706", currentY)

            currentY += actualRowHeight
        }

        // Draw Table Outer borders
        val tblStartY = if (pageNumber == 1) 230f else 100f
        canvas.drawLine(25f, tblStartY, 25f, currentY, borderPaint)
        canvas.drawLine(135f, tblStartY, 135f, currentY, borderPaint)
        canvas.drawLine(310f, tblStartY, 310f, currentY, borderPaint)
        canvas.drawLine(485f, tblStartY, 485f, currentY, borderPaint)
        canvas.drawLine(660f, tblStartY, 660f, currentY, borderPaint)
        canvas.drawLine(780f, tblStartY, 780f, currentY, borderPaint)
        canvas.drawLine(817f, tblStartY, 817f, currentY, borderPaint)

        pdfDocument.finishPage(page)

    } else {
        // --- 2. WHOLE GROUP SUMMARY REPORT (PORTRAIT A4: 595 x 842) ---
        var pageNumber = 1
        var pageInfo = PdfDocument.PageInfo.Builder(595, 842, pageNumber).create()
        var page = pdfDocument.startPage(pageInfo)
        var canvas = page.canvas

        // Load logo bitmap for watermark (portrait)
        val logoBitmapPortrait = try {
            BitmapFactory.decodeResource(context.resources, com.example.R.drawable.app_logo)
        } catch (_: Exception) { null }

        fun drawPortraitHeader(c: android.graphics.Canvas, pNum: Int) {
            // Draw Logo Watermark (portrait)
            if (logoBitmapPortrait != null) {
                try {
                    val watermarkSize = 200f
                    val scaledLogo = Bitmap.createScaledBitmap(logoBitmapPortrait, watermarkSize.toInt(), watermarkSize.toInt(), true)
                    val watermarkPaint = Paint().apply {
                        alpha = 30
                        isAntiAlias = true
                    }
                    c.drawBitmap(scaledLogo, (595f - watermarkSize) / 2f, 700f - (watermarkSize / 2f), watermarkPaint)
                } catch (_: Exception) {}
            }

            val outerBorder = Paint().apply {
                color = colorPrimary
                style = Paint.Style.STROKE
                strokeWidth = 1.8f
                isAntiAlias = true
            }
            c.drawRect(15f, 10f, 580f, 832f, outerBorder)

            val bannerPaint = Paint().apply {
                color = colorPrimary
                style = Paint.Style.FILL
                isAntiAlias = true
            }
            c.drawRoundRect(android.graphics.RectF(25f, 15f, 570f, 75f), 12f, 12f, bannerPaint)

            val titlePaint = Paint().apply {
                color = android.graphics.Color.WHITE
                textSize = 16f
                isAntiAlias = true
                typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
                textAlign = Paint.Align.CENTER
            }
            c.drawText("تيجان النور: تقرير متابعة الحلقة للفترة الزمنية 📊".loc(), 297f, 48f, titlePaint)

            val sloganPaint = Paint().apply {
                color = colorPrimary
                textSize = 9.5f
                isAntiAlias = true
                typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.ITALIC)
                textAlign = Paint.Align.CENTER
            }
            c.drawText("📖 «خَيْرُكُمْ مَنْ تَعَلَّمَ القُرْآنَ وَعَلَّمَهُ»".loc() + " - " + "صفحة ".loc() + "$pNum 📖", 297f, 820f, sloganPaint)
        }

        drawPortraitHeader(canvas, pageNumber)

        val infoLabelPaint = Paint().apply {
            color = colorSlateLabel
            textSize = 10f
            isAntiAlias = true
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            textAlign = Paint.Align.RIGHT
        }
        val infoValPaint = Paint().apply {
            color = colorSlateDark
            textSize = 12f
            isAntiAlias = true
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            textAlign = Paint.Align.RIGHT
        }

        canvas.drawText("🏫 الحلقة:".loc(), 560f, 100f, infoLabelPaint)
        canvas.drawText(student.groupName.ifBlank { "غير محددة".loc() }, 560f, 120f, infoValPaint)

        canvas.drawText("👤 معلم الحلقة:".loc(), 380f, 100f, infoLabelPaint)
        canvas.drawText(student.teacherName.ifBlank { "غير محدد".loc() }, 380f, 120f, infoValPaint)

        canvas.drawText("📅 الفترة الزمنية:".loc(), 200f, 100f, infoLabelPaint)
        canvas.drawText(dateRangeStr, 200f, 120f, infoValPaint)

        // Summary Title
        val subTitlePaint = Paint().apply {
            color = colorPrimary
            textSize = 13f
            isAntiAlias = true
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            textAlign = Paint.Align.RIGHT
        }
        canvas.drawText("📈 ملخص إنجاز طلاب الحلقة في هذه الفترة:".loc(), 560f, 155f, subTitlePaint)

        // Group Logs by student
        val groupStudents = reportIdToStudentMap.values.distinctBy { it.id }.filter { it.groupName == student.groupName }

        val tblHeaderPaint = Paint().apply {
            color = colorPrimary
            style = Paint.Style.FILL
        }
        canvas.drawRect(25f, 175f, 570f, 205f, tblHeaderPaint)

        val tblHeaderTextPaint = Paint().apply {
            color = android.graphics.Color.WHITE
            textSize = 11f
            isAntiAlias = true
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            textAlign = Paint.Align.RIGHT
        }
        canvas.drawText("اسم الطالب".loc(), 560f, 194f, tblHeaderTextPaint)
        canvas.drawText("الأيام المرصودة".loc(), 400f, 194f, tblHeaderTextPaint)
        canvas.drawText("جلسات الحفظ".loc(), 290f, 194f, tblHeaderTextPaint)
        canvas.drawText("متوسط تقييم الحفظ".loc(), 190f, 194f, tblHeaderTextPaint)
        canvas.drawText("نسبة الانتظام".loc(), 90f, 194f, tblHeaderTextPaint)

        val borderPaint = Paint().apply {
            color = colorBorder
            style = Paint.Style.STROKE
            strokeWidth = 1f
            isAntiAlias = true
        }

        var currentY = 205f
        val rowHeight = 36f

        groupStudents.forEach { currentStudent ->
            if (currentY + rowHeight > 780f) {
                // finish page and start new
                pdfDocument.finishPage(page)
                pageNumber++
                pageInfo = PdfDocument.PageInfo.Builder(595, 842, pageNumber).create()
                page = pdfDocument.startPage(pageInfo)
                canvas = page.canvas

                drawPortraitHeader(canvas, pageNumber)

                canvas.drawRect(25f, 100f, 570f, 130f, tblHeaderPaint)
                canvas.drawText("اسم الطالب".loc(), 560f, 119f, tblHeaderTextPaint)
                canvas.drawText("الأيام المرصودة".loc(), 400f, 119f, tblHeaderTextPaint)
                canvas.drawText("جلسات الحفظ".loc(), 290f, 119f, tblHeaderTextPaint)
                canvas.drawText("متوسط تقييم الحفظ".loc(), 190f, 119f, tblHeaderTextPaint)
                canvas.drawText("نسبة الانتظام".loc(), 90f, 119f, tblHeaderTextPaint)

                currentY = 130f
            }

            // Get reports belonging to this student
            val studentReports = reportIdToStudentMap.filterValues { it.id == currentStudent.id }.keys
            val studentLogs = logs.filter { it.weeklyReportId in studentReports }
            val studentPresentLogs = studentLogs.filter { !it.isAbsent }

            val sLoggedDays = studentPresentLogs.size
            val sMemoCount = studentPresentLogs.count { it.newMemoSurahFrom.isNotBlank() }
            val sAvgStars = if (sMemoCount > 0) {
                studentPresentLogs.filter { it.newMemoSurahFrom.isNotBlank() && it.newMemoStars in 1..3 }.map { it.newMemoStars }.average()
            } else 0.0
            val sDaysDiff = ((endDate - startDate) / (1000L * 60 * 60 * 24)) + 1
            val sAttendance = if (sDaysDiff > 0) {
                (sLoggedDays.toFloat() / sDaysDiff.toFloat() * 100f).coerceAtMost(100f)
            } else 0f

            // Draw row lines
            val isEven = groupStudents.indexOf(currentStudent) % 2 == 0
            if (isEven) {
                val rowBgPaint = Paint().apply {
                    color = android.graphics.Color.parseColor("#F8FAFC")
                    style = Paint.Style.FILL
                }
                canvas.drawRect(25f, currentY, 570f, currentY + rowHeight, rowBgPaint)
            }

            canvas.drawLine(25f, currentY + rowHeight, 570f, currentY + rowHeight, borderPaint)

            val valPaint = Paint().apply {
                color = colorSlateDark
                textSize = 10.5f
                isAntiAlias = true
                textAlign = Paint.Align.RIGHT
            }
            val namePaint = Paint().apply {
                color = colorSlateDark
                textSize = 10.5f
                isAntiAlias = true
                typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
                textAlign = Paint.Align.RIGHT
            }

            canvas.drawText(currentStudent.name, 560f, currentY + 22f, namePaint)
            canvas.drawText("$sLoggedDays " + "يوم".loc(), 400f, currentY + 22f, valPaint)
            canvas.drawText("$sMemoCount " + "جلسة".loc(), 290f, currentY + 22f, valPaint)
            canvas.drawText(if (sMemoCount > 0) String.format("%.1f ⭐", sAvgStars) else "—", 190f, currentY + 22f, valPaint)
            canvas.drawText(String.format("%.0f%%", sAttendance), 90f, currentY + 22f, valPaint)

            currentY += rowHeight
        }

        // Draw Table Vertical lines
        val tblStartY = if (pageNumber == 1) 175f else 100f
        canvas.drawLine(25f, tblStartY, 25f, currentY, borderPaint)
        canvas.drawLine(415f, tblStartY, 415f, currentY, borderPaint)
        canvas.drawLine(305f, tblStartY, 305f, currentY, borderPaint)
        canvas.drawLine(205f, tblStartY, 205f, currentY, borderPaint)
        canvas.drawLine(105f, tblStartY, 105f, currentY, borderPaint)
        canvas.drawLine(570f, tblStartY, 570f, currentY, borderPaint)

        pdfDocument.finishPage(page)
    }

    // Save and Share PDF
    val cacheFile = File(context.cacheDir, "تقرير_الفترة_${student.groupName.replace(" ", "_")}.pdf")
    try {
        val fileOutputStream = FileOutputStream(cacheFile)
        pdfDocument.writeTo(fileOutputStream)
        pdfDocument.close()
        fileOutputStream.close()

        val authority = "${context.packageName}.fileprovider"
        val shareUri = FileProvider.getUriForFile(context, authority, cacheFile)

        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, shareUri)
            val shareSubject = "تيجان النور: تقرير الفترة".loc()
            val shareText = if (AppLang.current == "en") {
                "Assalamu Alaikum, we attach the tracking report of Quran recitation and memorization for the period for " +
                (if (isGroupReport) "circle (${student.groupName})" else "student: ${student.name}") +
                ". We pray to Allah for acceptance and success."
            } else {
                "السلام عليكم ورحمة الله وبركاته، نرفق لكم تقرير متابعة الفترة الزمنية لمستوى تلاوة وحفظ القرآن الكريم لـ " +
                (if (isGroupReport) "حلقة (${student.groupName})" else "الطالب: ${student.name}") +
                ". نسأل الله القبول والتوفيق."
            }
            putExtra(Intent.EXTRA_SUBJECT, shareSubject)
            putExtra(Intent.EXTRA_TEXT, shareText)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        val shareIntent = Intent.createChooser(intent, "تصدير ومشاركة تقرير الفترة:".loc())
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(shareIntent)

    } catch (e: Exception) {
        e.printStackTrace()
    }
}

// ==========================================
// 5. SETTINGS SCREEN
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: QuranViewModel) {
    val context = LocalContext.current
    val students by viewModel.students.collectAsStateWithLifecycle()
    val isDarkModeOption by viewModel.isDarkMode.collectAsStateWithLifecycle()
    val systemDark = isSystemInDarkTheme()
    val isDark = isDarkModeOption ?: systemDark

    val groups = remember(students) {
        students.map { it.groupName }.distinct().filter { it.isNotBlank() }
    }

    var generalNotificationsEnabled by remember {
        mutableStateOf(viewModel.appPrefs.notificationsEnabled)
    }
    var sessionReminderMinutes by remember {
        mutableStateOf(viewModel.appPrefs.sessionReminderMinutes.toString())
    }
    var dailyReminderTime by remember {
        mutableStateOf(viewModel.appPrefs.dailyReminderTime)
    }

    var preferenceChangeKey by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("إعدادات التطبيق ⚙️".loc(), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateTo(Screen.StudentsList) }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "رجوع".loc())
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
            // Theme settings Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    border = BorderStroke(1.6.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.35f))
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("🎨 المظهر وطابع التطبيق:".loc(), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, fontSize = 15.sp)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("الوضع الداكن (Dark Mode)".loc(), fontWeight = FontWeight.Medium)
                            Switch(
                                checked = isDark,
                                onCheckedChange = { viewModel.toggleTheme(it) }
                            )
                        }
                    }
                }
            }

            // Language settings Card
            item {
                val currentLanguage by viewModel.appLanguage.collectAsStateWithLifecycle()
                val activity = context as? android.app.Activity
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    border = BorderStroke(1.6.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.35f))
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("🌐 اللغة / Language:".loc(), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, fontSize = 15.sp)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Arabic option
                            val isArabic = currentLanguage == "ar"
                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        if (!isArabic) {
                                            viewModel.setLanguage("ar")
                                            activity?.recreate()
                                        }
                                    },
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isArabic) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                                ),
                                border = if (isArabic) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp).fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text("🇸🇦", fontSize = 24.sp)
                                    Text(
                                        "عربي".loc(),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = if (isArabic) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    if (isArabic) {
                                        Text("✓ محدد".loc(), fontSize = 11.sp, color = Color.White.copy(alpha = 0.85f))
                                    }
                                }
                            }

                            // English option
                            val isEnglish = currentLanguage == "en"
                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        if (!isEnglish) {
                                            viewModel.setLanguage("en")
                                            activity?.recreate()
                                        }
                                    },
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isEnglish) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                                ),
                                border = if (isEnglish) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp).fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text("🇬🇧", fontSize = 24.sp)
                                    Text(
                                        "English",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = if (isEnglish) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    if (isEnglish) {
                                        Text("✓ Selected", fontSize = 11.sp, color = Color.White.copy(alpha = 0.85f))
                                    }
                                }
                            }
                        }
                        Text(
                            "* سيتم إعادة تشغيل التطبيق تلقائياً عند تغيير اللغة".loc(),
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Notification configurations
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    border = BorderStroke(1.6.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.35f))
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("🔔 تنبيهات وإشعارات المتابعة:".loc(), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, fontSize = 15.sp)
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("تفعيل الإشعارات العامة".loc(), fontWeight = FontWeight.Medium)
                            Switch(
                                checked = generalNotificationsEnabled,
                                onCheckedChange = {
                                    generalNotificationsEnabled = it
                                    viewModel.appPrefs.notificationsEnabled = it
                                    viewModel.rescheduleAlarms()
                                }
                            )
                        }

                        if (generalNotificationsEnabled) {
                            HorizontalDivider()

                            // Minutes offset
                            OutlinedTextField(
                                value = sessionReminderMinutes,
                                onValueChange = { newValue ->
                                    if (newValue.all { it.isDigit() }) {
                                        sessionReminderMinutes = newValue
                                        val mins = newValue.toIntOrNull() ?: 30
                                        viewModel.appPrefs.sessionReminderMinutes = mins
                                        viewModel.rescheduleAlarms()
                                    }
                                },
                                label = { Text("وقت التنبيه قبل الحلقة (بالدقائق)".loc()) },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                textStyle = TextStyle(textDirection = TextDirection.Ltr, textAlign = TextAlign.Left),
                                modifier = Modifier.fillMaxWidth()
                            )

                            // Daily time selector
                            Box(modifier = Modifier.fillMaxWidth()) {
                                OutlinedTextField(
                                    value = formatTime12h(dailyReminderTime),
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("وقت إشعار التذكير اليومي".loc()) },
                                    modifier = Modifier.fillMaxWidth(),
                                    trailingIcon = { Text("⏰", modifier = Modifier.padding(end = 8.dp)) },
                                    textStyle = TextStyle(textDirection = TextDirection.Rtl, textAlign = TextAlign.Right)
                                )
                                Box(
                                    modifier = Modifier
                                        .matchParentSize()
                                        .clickable {
                                            val currentHour = dailyReminderTime.split(":")[0].toIntOrNull() ?: 8
                                            val currentMinute = dailyReminderTime.split(":")[1].toIntOrNull() ?: 0
                                            android.app.TimePickerDialog(
                                                context,
                                                { _, hour, minute ->
                                                    dailyReminderTime = String.format("%02d:%02d", hour, minute)
                                                    viewModel.appPrefs.dailyReminderTime = dailyReminderTime
                                                    viewModel.rescheduleAlarms()
                                                },
                                                currentHour,
                                                currentMinute,
                                                false
                                            ).show()
                                        }
                                )
                            }
                            
                            HorizontalDivider()
                            
                            // Per circle toggles
                            Text("تخصيص الإشعارات حسب الحلقات النشطة:".loc(), fontWeight = FontWeight.Bold, fontSize = 13.5.sp)
                            
                            if (groups.isEmpty()) {
                                Text("لا توجد حلقات مسجلة حالياً.".loc(), color = Color.Gray, fontSize = 12.sp)
                            } else {
                                key(preferenceChangeKey) {
                                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                        groups.forEach { groupName ->
                                            Card(
                                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)),
                                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                                    Text("حلقة: ".loc() + groupName, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                                    
                                                    Row(
                                                        modifier = Modifier.fillMaxWidth(),
                                                        horizontalArrangement = Arrangement.SpaceBetween,
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Text("تنبيه الحلقات ⏰".loc(), fontSize = 12.5.sp)
                                                        Checkbox(
                                                            checked = viewModel.appPrefs.isSessionEnabledForGroup(groupName),
                                                            onCheckedChange = { checked ->
                                                                viewModel.appPrefs.toggleSessionGroup(groupName, checked)
                                                                viewModel.rescheduleAlarms()
                                                                preferenceChangeKey++
                                                            }
                                                        )
                                                    }
                                                    
                                                    Row(
                                                        modifier = Modifier.fillMaxWidth(),
                                                        horizontalArrangement = Arrangement.SpaceBetween,
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Text("إشعارات التقارير 📋".loc(), fontSize = 12.5.sp)
                                                        Checkbox(
                                                            checked = viewModel.appPrefs.isReportEnabledForGroup(groupName),
                                                            onCheckedChange = { checked ->
                                                                viewModel.appPrefs.toggleReportGroup(groupName, checked)
                                                                viewModel.rescheduleAlarms()
                                                                preferenceChangeKey++
                                                            }
                                                        )
                                                    }

                                                    Row(
                                                        modifier = Modifier.fillMaxWidth(),
                                                        horizontalArrangement = Arrangement.SpaceBetween,
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Text("تنبيهات إضافية أخرى 🔔".loc(), fontSize = 12.5.sp)
                                                        Checkbox(
                                                            checked = viewModel.appPrefs.isOtherEnabledForGroup(groupName),
                                                            onCheckedChange = { checked ->
                                                                viewModel.appPrefs.toggleOtherGroup(groupName, checked)
                                                                viewModel.rescheduleAlarms()
                                                                preferenceChangeKey++
                                                            }
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
                }
            }

            // Quick actions & Backup Links Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    border = BorderStroke(1.6.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.35f))
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("🛠️ إجراءات سريعة وصيانة:".loc(), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, fontSize = 15.sp)
                        
                        Button(
                            onClick = {
                                NotificationHelper.sendNotification(
                                    context = context,
                                    id = 9999,
                                    title = "تنبيه تجريبي 🔔".loc(),
                                    text = "مرحباً بك! هذا تنبيه تجريبي من تطبيق تيجان النور للتأكد من عمل نظام الإشعارات.".loc()
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                        ) {
                            Text("إرسال إشعار تجريبي فوراً 🔔".loc(), fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = { viewModel.navigateTo(Screen.Backups) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Share, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("صفحة النسخ الاحتياطي واسترجاع البيانات 💾".loc(), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // App details description card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Emerald80.copy(alpha = 0.25f)),
                    border = BorderStroke(1.5.dp, Emerald40.copy(alpha = 0.4f))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text("تيجان النور 📖".loc(), fontWeight = FontWeight.Black, fontSize = 18.sp, color = Emerald950)
                        Text(
                            text = "نظام ريادي متكامل لمتابعة وتدوين مستوى تلاوة وحفظ القرآن الكريم للطلاب والناشئة، يدعم الرصد الأسبوعي والتقارير الدورية ومشاركة النتائج مع أولياء الأمور.".loc(),
                            fontSize = 13.sp,
                            color = Emerald950.copy(alpha = 0.8f),
                            textAlign = TextAlign.Center,
                            lineHeight = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "الإصدار الحالي: v5.0.0\nمطور بكل حب ومودة لخدمة كتاب الله عز وجل 🌱".loc(),
                            fontSize = 11.5.sp,
                            color = Emerald40,
                            fontWeight = FontWeight.ExtraBold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

// ==========================================
// 6. GROUP REPORT DIALOG & PDF GENERATOR
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupReportDialog(
    students: List<Student>,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val ALL_GROUPS_TEXT = "جميع الحلقات".loc()
    val groups = remember(students) {
        listOf(ALL_GROUPS_TEXT) + students.map { it.groupName }.distinct().filter { it.isNotBlank() }
    }
    
    var selectedGroup by remember {
        mutableStateOf(groups.firstOrNull() ?: "")
    }
    
    val filteredStudents = remember(students, selectedGroup) {
        if (selectedGroup == ALL_GROUPS_TEXT) {
            students.sortedWith(compareBy({ it.groupName }, { it.studentSequentialNumber }))
        } else {
            students.filter { it.groupName == selectedGroup }.sortedBy { it.studentSequentialNumber }
        }
    }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    "📊 تقرير طلاب الحلقة".loc(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Right,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                if (groups.size > 1) {
                    var expandedGroupMenu by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = expandedGroupMenu,
                        onExpandedChange = { expandedGroupMenu = it },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = selectedGroup,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("اختر الحلقة".loc()) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedGroupMenu) },
                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                            textStyle = TextStyle(textDirection = TextDirection.Rtl, textAlign = TextAlign.Right)
                        )
                        ExposedDropdownMenu(
                            expanded = expandedGroupMenu,
                            onDismissRequest = { expandedGroupMenu = false }
                        ) {
                            groups.forEach { group ->
                                DropdownMenuItem(
                                    text = { Text(group, fontWeight = FontWeight.Bold) },
                                    onClick = {
                                        selectedGroup = group
                                        expandedGroupMenu = false
                                    }
                                )
                            }
                        }
                    }
                } else {
                    Text(
                        text = "الحلقة: $selectedGroup",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 300.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (filteredStudents.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("لا يوجد طلاب في هذه الحلقة حالياً.".loc(), color = Color.Gray)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                            .clip(RoundedCornerShape(8.dp))
                    ) {
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.primary)
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text("#", modifier = Modifier.width(30.dp), color = Color.White, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, fontSize = 12.sp)
                                Text("الاسم", modifier = Modifier.weight(1.5f), color = Color.White, fontWeight = FontWeight.Bold, textAlign = TextAlign.Right, fontSize = 12.sp)
                                if (selectedGroup == ALL_GROUPS_TEXT) {
                                    Text("الحلقة", modifier = Modifier.weight(1f), color = Color.White, fontWeight = FontWeight.Bold, textAlign = TextAlign.Right, fontSize = 12.sp)
                                }
                                Text("الوقت", modifier = Modifier.weight(1f), color = Color.White, fontWeight = FontWeight.Bold, textAlign = TextAlign.Right, fontSize = 12.sp)
                                Text("الواتساب", modifier = Modifier.weight(1.2f), color = Color.White, fontWeight = FontWeight.Bold, textAlign = TextAlign.Right, fontSize = 12.sp)
                            }
                        }
                        
                        items(filteredStudents) { student ->
                            val isEven = filteredStudents.indexOf(student) % 2 == 0
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(if (isEven) Color(0xFFF8FAFC) else Color.White)
                                    .border(0.5.dp, Color.LightGray.copy(alpha = 0.5f))
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = student.studentSequentialNumber.toString(),
                                    modifier = Modifier.width(30.dp),
                                    textAlign = TextAlign.Center,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = student.name,
                                    modifier = Modifier.weight(1.5f),
                                    textAlign = TextAlign.Right,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                if (selectedGroup == ALL_GROUPS_TEXT) {
                                    Text(
                                        text = student.groupName,
                                        modifier = Modifier.weight(1f),
                                        textAlign = TextAlign.Right,
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                val timeDaysText = formatStudentCircleDaysTimes(student.circleSessionDaysTimes)
                                Text(
                                    text = timeDaysText,
                                    modifier = Modifier.weight(1f),
                                    textAlign = TextAlign.Right,
                                    fontSize = 11.sp
                                )
                                Text(
                                    text = student.whatsappNumber ?: "—",
                                    modifier = Modifier.weight(1.2f),
                                    textAlign = TextAlign.Right,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        val header = if (selectedGroup == ALL_GROUPS_TEXT) "الرقم التسلسلي\tالاسم\tاسم الحلقة\tوقت الحلقة\tرقم الواتساب\n" else "الرقم التسلسلي\tالاسم\tوقت الحلقة\tرقم الواتساب\n"
                        val rows = filteredStudents.joinToString("\n") { s ->
                            val timeDaysStr = formatStudentCircleDaysTimes(s.circleSessionDaysTimes)
                            if (selectedGroup == ALL_GROUPS_TEXT) {
                                "${s.studentSequentialNumber}\t${s.name}\t${s.groupName}\t${timeDaysStr}\t${s.whatsappNumber ?: "—"}"
                            } else {
                                "${s.studentSequentialNumber}\t${s.name}\t${timeDaysStr}\t${s.whatsappNumber ?: "—"}"
                            }
                        }
                        val textToCopy = header + rows
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                        val clip = android.content.ClipData.newPlainText("تقرير طلاب الحلقة", textToCopy)
                        clipboard.setPrimaryClip(clip)
                        android.widget.Toast.makeText(context, "تم نسخ الجدول للحافظة بنجاح!".loc(), android.widget.Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer, contentColor = MaterialTheme.colorScheme.onSecondaryContainer),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    Text("نسخ الجدول 📋".loc(), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                
                Button(
                    onClick = {
                        generateGroupListPdf(context, selectedGroup, filteredStudents, selectedGroup == ALL_GROUPS_TEXT)
                    },
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    Text("تصدير PDF 📄".loc(), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("إغلاق".loc(), fontWeight = FontWeight.Bold)
            }
        }
    )
}

private fun generateGroupListPdf(
    context: Context,
    groupName: String,
    students: List<Student>,
    showGroupNameColumn: Boolean = false
) {
    val pdfDocument = PdfDocument()
    val colorPrimary = android.graphics.Color.parseColor("#059669")
    val colorSlateDark = android.graphics.Color.parseColor("#1E293B")
    val colorSlateLabel = android.graphics.Color.parseColor("#475569")
    val colorBorder = android.graphics.Color.parseColor("#CBD5E1")
    
    var pageNumber = 1
    // A4 Landscape Dimensions: 842 x 595
    var pageInfo = PdfDocument.PageInfo.Builder(842, 595, pageNumber).create()
    var page = pdfDocument.startPage(pageInfo)
    var canvas = page.canvas

    val logoBitmap = try {
        BitmapFactory.decodeResource(context.resources, com.example.R.drawable.app_logo)
    } catch (_: Exception) { null }

    fun drawLandscapeHeader(c: android.graphics.Canvas, pNum: Int) {
        if (logoBitmap != null) {
            try {
                val watermarkSize = 250f
                val scaledLogo = Bitmap.createScaledBitmap(logoBitmap, watermarkSize.toInt(), watermarkSize.toInt(), true)
                val watermarkPaint = Paint().apply {
                    alpha = 25
                    isAntiAlias = true
                }
                c.drawBitmap(scaledLogo, (842f - watermarkSize) / 2f, 297.5f - (watermarkSize / 2f), watermarkPaint)
            } catch (_: Exception) {}
        }

        val outerBorder = Paint().apply {
            color = colorPrimary
            style = Paint.Style.STROKE
            strokeWidth = 1.8f
            isAntiAlias = true
        }
        c.drawRect(15f, 10f, 827f, 585f, outerBorder)

        val bannerPaint = Paint().apply {
            color = colorPrimary
            style = Paint.Style.FILL
            isAntiAlias = true
        }
        c.drawRoundRect(android.graphics.RectF(25f, 15f, 817f, 75f), 12f, 12f, bannerPaint)

        val titlePaint = Paint().apply {
            color = android.graphics.Color.WHITE
            textSize = 18f
            isAntiAlias = true
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            textAlign = Paint.Align.CENTER
        }
        c.drawText("تيجان النور: تقرير طلاب الحلقة 📊".loc(), 421f, 48f, titlePaint)

        val sloganPaint = Paint().apply {
            color = colorPrimary
            textSize = 9.5f
            isAntiAlias = true
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.ITALIC)
            textAlign = Paint.Align.CENTER
        }
        c.drawText("📖 «خَيْرُكُمْ مَنْ تَعَلَّمَ القُرْآنَ وَعَلَّمَهُ»".loc() + " - " + "صفحة ".loc() + "$pNum 📖", 421f, 570f, sloganPaint)
    }

    drawLandscapeHeader(canvas, pageNumber)

    val infoLabelPaint = Paint().apply {
        color = colorSlateLabel
        textSize = 10f
        isAntiAlias = true
        typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
        textAlign = Paint.Align.RIGHT
    }
    val infoValPaint = Paint().apply {
        color = colorSlateDark
        textSize = 12f
        isAntiAlias = true
        typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
        textAlign = Paint.Align.RIGHT
    }

    canvas.drawText("🏫 اسم الحلقة:".loc(), 800f, 100f, infoLabelPaint)
    canvas.drawText(groupName, 800f, 120f, infoValPaint)

    canvas.drawText("👥 عدد الطلاب:".loc(), 450f, 100f, infoLabelPaint)
    canvas.drawText("${students.size}" + " طالب".loc(), 450f, 120f, infoValPaint)

    val dateLocale = if (AppLang.current == "en") java.util.Locale.US else java.util.Locale("ar")
    val dateStr = java.text.SimpleDateFormat("dd/MM/yyyy", dateLocale).format(java.util.Date())
    canvas.drawText("📅 تاريخ التصدير:".loc(), 180f, 100f, infoLabelPaint)
    canvas.drawText(dateStr, 180f, 120f, infoValPaint)

    val tblHeaderPaint = Paint().apply {
        color = colorPrimary
        style = Paint.Style.FILL
    }
    // Table bounds: Left 25f to Right 817f
    canvas.drawRect(25f, 145f, 817f, 175f, tblHeaderPaint)

    val tblHeaderTextPaint = Paint().apply {
        color = android.graphics.Color.WHITE
        textSize = 11f
        isAntiAlias = true
        typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
        textAlign = Paint.Align.RIGHT
    }
    // Column widths: Right-aligned relative layout
    // Index (Right: 800f) | Name (Right: 700f) | WhatsApp (Right: 400f) | Times (Right: 220f)
    if (showGroupNameColumn) {
        canvas.drawText("الرقم".loc(), 805f, 164f, tblHeaderTextPaint)
        canvas.drawText("اسم الطالب الكامل".loc(), 755f, 164f, tblHeaderTextPaint)
        canvas.drawText("اسم الحلقة".loc(), 525f, 164f, tblHeaderTextPaint)
        canvas.drawText("الواتساب".loc(), 375f, 164f, tblHeaderTextPaint)
        canvas.drawText("مواعيد الحلقة".loc(), 225f, 164f, tblHeaderTextPaint)
    } else {
        canvas.drawText("الرقم التسلسلي".loc(), 805f, 164f, tblHeaderTextPaint)
        canvas.drawText("اسم الطالب الكامل".loc(), 700f, 164f, tblHeaderTextPaint)
        canvas.drawText("رقم التواصل (الواتساب)".loc(), 400f, 164f, tblHeaderTextPaint)
        canvas.drawText("مواعيد وأوقات الحلقة".loc(), 220f, 164f, tblHeaderTextPaint)
    }

    val borderPaint = Paint().apply {
        color = colorBorder
        style = Paint.Style.STROKE
        strokeWidth = 1f
        isAntiAlias = true
    }
    var currentY = 175f

    students.forEachIndexed { index, currentStudent ->
        val namePaint = Paint().apply {
            color = colorSlateDark
            textSize = 10.5f
            isAntiAlias = true
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            textAlign = Paint.Align.RIGHT
        }
        val valPaint = Paint().apply {
            color = colorSlateDark
            textSize = 10.5f
            isAntiAlias = true
            textAlign = Paint.Align.RIGHT
        }
        val seqPaint = Paint().apply {
            color = colorSlateDark
            textSize = 10.5f
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
        }

        val timeDaysStr = formatStudentCircleDaysTimes(currentStudent.circleSessionDaysTimes)
        
        val nameLines = if (showGroupNameColumn) wrapText(currentStudent.name, namePaint, 210f) else wrapText(currentStudent.name, namePaint, 270f)
        val groupNameLines = if (showGroupNameColumn) wrapText(currentStudent.groupName, valPaint, 130f) else emptyList()
        val timeLines = wrapText(timeDaysStr, valPaint, 190f)

        val maxLines = if (showGroupNameColumn) maxOf(nameLines.size, groupNameLines.size, timeLines.size, 1) else maxOf(nameLines.size, timeLines.size, 1)
        val actualRowHeight = 20f + maxLines * 16f

        if (currentY + actualRowHeight > 540f) {
            // Draw table vertical borders for current page before finishing
            val tStartY = if (pageNumber == 1) 145f else 100f
            canvas.drawLine(25f, tStartY, 25f, currentY, borderPaint)
            canvas.drawLine(817f, tStartY, 817f, currentY, borderPaint)
            if (showGroupNameColumn) {
                canvas.drawLine(767f, tStartY, 767f, currentY, borderPaint)
                canvas.drawLine(537f, tStartY, 537f, currentY, borderPaint)
                canvas.drawLine(387f, tStartY, 387f, currentY, borderPaint)
                canvas.drawLine(237f, tStartY, 237f, currentY, borderPaint)
            } else {
                canvas.drawLine(715f, tStartY, 715f, currentY, borderPaint)
                canvas.drawLine(420f, tStartY, 420f, currentY, borderPaint)
                canvas.drawLine(240f, tStartY, 240f, currentY, borderPaint)
            }

            pdfDocument.finishPage(page)
            pageNumber++
            pageInfo = PdfDocument.PageInfo.Builder(842, 595, pageNumber).create()
            page = pdfDocument.startPage(pageInfo)
            canvas = page.canvas

            drawLandscapeHeader(canvas, pageNumber)

            canvas.drawRect(25f, 100f, 817f, 130f, tblHeaderPaint)
            if (showGroupNameColumn) {
                canvas.drawText("الرقم".loc(), 805f, 119f, tblHeaderTextPaint)
                canvas.drawText("اسم الطالب الكامل".loc(), 755f, 119f, tblHeaderTextPaint)
                canvas.drawText("اسم الحلقة".loc(), 525f, 119f, tblHeaderTextPaint)
                canvas.drawText("الواتساب".loc(), 375f, 119f, tblHeaderTextPaint)
                canvas.drawText("مواعيد الحلقة".loc(), 225f, 119f, tblHeaderTextPaint)
            } else {
                canvas.drawText("الرقم التسلسلي".loc(), 805f, 119f, tblHeaderTextPaint)
                canvas.drawText("اسم الطالب الكامل".loc(), 700f, 119f, tblHeaderTextPaint)
                canvas.drawText("رقم التواصل (الواتساب)".loc(), 400f, 119f, tblHeaderTextPaint)
                canvas.drawText("مواعيد وأوقات الحلقة".loc(), 220f, 119f, tblHeaderTextPaint)
            }

            currentY = 130f
        }

        val isEven = index % 2 == 0
        if (isEven) {
            val rowBgPaint = Paint().apply {
                color = android.graphics.Color.parseColor("#F8FAFC")
                style = Paint.Style.FILL
            }
            canvas.drawRect(25f, currentY, 817f, currentY + actualRowHeight, rowBgPaint)
        }

        canvas.drawLine(25f, currentY + actualRowHeight, 817f, currentY + actualRowHeight, borderPaint)

        // Draw sequence number (Starts from 1)
        val seqNumber = (index + 1).toString()
        if (showGroupNameColumn) {
            canvas.drawText(seqNumber, 792f, currentY + 22f, seqPaint)
            nameLines.forEachIndexed { lineIdx, line ->
                canvas.drawText(line, 755f, currentY + 22f + (lineIdx * 16f), namePaint)
            }
            groupNameLines.forEachIndexed { lineIdx, line ->
                canvas.drawText(line, 525f, currentY + 22f + (lineIdx * 16f), valPaint)
            }
            canvas.drawText(currentStudent.whatsappNumber ?: "—", 375f, currentY + 22f, valPaint)
            timeLines.forEachIndexed { lineIdx, line ->
                canvas.drawText(line, 225f, currentY + 22f + (lineIdx * 16f), valPaint)
            }
        } else {
            canvas.drawText(seqNumber, 766f, currentY + 22f, seqPaint)
            nameLines.forEachIndexed { lineIdx, line ->
                canvas.drawText(line, 700f, currentY + 22f + (lineIdx * 16f), namePaint)
            }
            canvas.drawText(currentStudent.whatsappNumber ?: "—", 400f, currentY + 22f, valPaint)
            timeLines.forEachIndexed { lineIdx, line ->
                canvas.drawText(line, 220f, currentY + 22f + (lineIdx * 16f), valPaint)
            }
        }

        currentY += actualRowHeight
    }

    val tblStartY = if (pageNumber == 1) 145f else 100f
    canvas.drawLine(25f, tblStartY, 25f, currentY, borderPaint)
    canvas.drawLine(817f, tblStartY, 817f, currentY, borderPaint)
    if (showGroupNameColumn) {
        canvas.drawLine(767f, tblStartY, 767f, currentY, borderPaint)
        canvas.drawLine(537f, tblStartY, 537f, currentY, borderPaint)
        canvas.drawLine(387f, tblStartY, 387f, currentY, borderPaint)
        canvas.drawLine(237f, tblStartY, 237f, currentY, borderPaint)
    } else {
        canvas.drawLine(715f, tblStartY, 715f, currentY, borderPaint)
        canvas.drawLine(420f, tblStartY, 420f, currentY, borderPaint)
        canvas.drawLine(240f, tblStartY, 240f, currentY, borderPaint)
    }

    pdfDocument.finishPage(page)

    val cacheFile = File(context.cacheDir, "تقرير_طلاب_حلقة_${groupName.replace(" ", "_")}.pdf")
    try {
        val fileOutputStream = FileOutputStream(cacheFile)
        pdfDocument.writeTo(fileOutputStream)
        pdfDocument.close()
        fileOutputStream.close()

        val authority = "${context.packageName}.fileprovider"
        val shareUri = FileProvider.getUriForFile(context, authority, cacheFile)

        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, shareUri)
            val shareSubject = "تقرير طلاب حلقة: ".loc() + groupName
            val shareText = if (AppLang.current == "en") {
                "Assalamu Alaikum, we attach the report and schedule of circle ($groupName) students. We pray to Allah for acceptance and success."
            } else {
                "السلام عليكم ورحمة الله وبركاته، نرفق لكم تقرير وجدول طلاب حلقة ($groupName). نسأل الله لكم القبول والتوفيق."
            }
            putExtra(Intent.EXTRA_SUBJECT, shareSubject)
            putExtra(Intent.EXTRA_TEXT, shareText)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        val shareIntent = Intent.createChooser(intent, "تصدير ومشاركة التقرير:".loc())
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(shareIntent)

    } catch (e: Exception) {
        e.printStackTrace()
    }
}





