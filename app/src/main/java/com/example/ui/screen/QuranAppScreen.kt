package com.example.ui.screen

import android.content.Context
import android.content.Intent
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
import androidx.compose.ui.text.style.TextAlign
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
import androidx.compose.animation.core.*
import androidx.compose.ui.graphics.graphicsLayer

@Composable
fun QuranAppScreen(viewModel: QuranViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    
    // Enforce RTL Layout Direction for Islamic Arabic Application
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            when (val screen = currentScreen) {
                is Screen.Splash -> SplashScreen(viewModel)
                is Screen.StudentsList -> StudentsListScreen(viewModel)
                is Screen.StudentProfile -> StudentProfileScreen(viewModel, screen.student)
                is Screen.ReportTracking -> ReportTrackingScreen(viewModel, screen.student, screen.report)
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
    val students by viewModel.students.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    
    var showAddDialog by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Column(modifier = Modifier.padding(end = 16.dp)) {
                        Text(
                            text = "سِجِلّ تَاجُ الْوَقَارِ 📖",
                            fontWeight = FontWeight.Black,
                            fontSize = 25.sp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Text(
                            text = "متابعة تلاوة وحفظ القرآن الكريم للطلاب والناشئة",
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
                        onClick = { showAddDialog = true },
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.25f), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "إضافة طالب",
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
                        text = "قالَ رَسُولُ اللَّهِ ﷺ:",
                        fontSize = 13.5.sp,
                        color = Emerald40,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "«خَيْرُكُمْ مَنْ تَعَلَّمَ القُرْآنَ وَعَلَّمَهُ»",
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
                        Text("إجمالي الطلاب", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f), fontWeight = FontWeight.Bold)
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
                        Text("حلقات النشاط", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f), fontWeight = FontWeight.Bold)
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
                placeholder = { Text("بحث عن طالب أو حلقة...", fontWeight = FontWeight.Medium) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("student_search_input"),
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "بحث", tint = MaterialTheme.colorScheme.primary) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.updateSearchQuery("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "مسح البحث")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.8f)
                ),
                textStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
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
                            text = if (searchQuery.isEmpty()) "لا يوجد طلاب مسجلين بعد" else "لم يتم العثور على نتائج للمطابقة",
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                            fontSize = 16.sp
                        )
                        Text(
                            text = if (searchQuery.isEmpty()) "اضغط على زر الإضافة (+) بالأعلى للبدء" else "تأكد من كتابة الاسم بشكل صحيح",
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
            onConfirm = { name, group, teacher, notes ->
                viewModel.addStudent(name, group, teacher, notes)
                showAddDialog = false
            }
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
                    Text(
                        text = student.name,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (student.groupName.isNotBlank()) {
                            AssistChip(
                                onClick = {},
                                label = { Text(student.groupName, fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                                leadingIcon = { Icon(Icons.Default.Home, contentDescription = null, modifier = Modifier.size(14.dp)) },
                                colors = AssistChipDefaults.assistChipColors(labelColor = MaterialTheme.colorScheme.primary)
                            )
                        }
                        if (student.teacherName.isNotBlank()) {
                            Text(
                                text = "المعلم: ${student.teacherName}",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
            
            IconButton(
                onClick = { showConfirmDelete = true },
                modifier = Modifier.testTag("delete_student_btn_${student.id}")
            ) {
                Icon(Icons.Default.Delete, contentDescription = "حذف الطالب", tint = Color.LightGray)
            }
        }
    }

    if (showConfirmDelete) {
        AlertDialog(
            onDismissRequest = { showConfirmDelete = false },
            title = { Text("حذف ملف الطالب؟", fontWeight = FontWeight.Bold) },
            text = { Text("هل أنت متأكد من حذف الطالب (${student.name}) وكامل سجلات متابعته بشكل نهائي؟ لا يمكن التراجع عن هذا الإجراء.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDelete()
                        showConfirmDelete = false
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("حذف نهائي")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDelete = false }) {
                    Text("إلغاء")
                }
            }
        )
    }
}

@Composable
fun AddStudentDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var group by remember { mutableStateOf("") }
    var teacher by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    
    var errorMsg by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "إضافة طالب جديد للحلقة ✨",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Right
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { 
                        name = it
                        if (it.isNotBlank()) errorMsg = ""
                    },
                    label = { Text("اسم الطالب الكامل *") },
                    isError = errorMsg.isNotEmpty(),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("add_student_name_field")
                )
                if (errorMsg.isNotEmpty()) {
                    Text(errorMsg, color = MaterialTheme.colorScheme.error, fontSize = 10.sp)
                }
                
                OutlinedTextField(
                    value = group,
                    onValueChange = { group = it },
                    label = { Text("الصف / حلقة التحفيظ") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                
                OutlinedTextField(
                    value = teacher,
                    onValueChange = { teacher = it },
                    label = { Text("اسم المعلم المربي") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("ملاحظات إضافية (أهداف الحفظ، إلخ)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isBlank()) {
                        errorMsg = "اسم الطالب مطلوب للرصد"
                    } else {
                        onConfirm(name, group, teacher, notes)
                    }
                },
                modifier = Modifier.testTag("save_student_confirm")
            ) {
                Text("إضافة وحفظ")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("إلغاء")
            }
        }
    )
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
                title = { Text("ملف الطالب ومتابعته", fontWeight = FontWeight.Bold) },
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
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAddWeekDialog = true },
                icon = { Icon(Icons.Default.DateRange, contentDescription = null) },
                text = { Text("إضافة أسبوع رصد") },
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
                                text = "🏫 الحلقة: ${activeStudent.groupName.ifBlank { "غير محددة" }}  |  👤 المعلم: ${activeStudent.teacherName.ifBlank { "غير محدد" }}",
                                fontSize = 13.5.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),
                                fontWeight = FontWeight.Bold
                            )
                            if (activeStudent.notes.isNotBlank()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "📝 أهداف التحفيظ: ${activeStudent.notes}",
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                    
                    IconButton(
                        onClick = { showEditDialog = true },
                        modifier = Modifier.testTag("edit_student_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "تعديل البيانات",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            Text(
                text = "جداول ورصد المتابعة الأسبوعية 📅",
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
                            text = "لا توجد أسابيع رصد مسجلة للطالب",
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "انقر على زر 'إضافة أسبوع رصد' بالأسفل لبدء رصد الجداول",
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
            weekName = "الأسبوع ${reports.size + 1}"
        }
        
        AlertDialog(
            onDismissRequest = { showAddWeekDialog = false },
            title = { Text("إضافة أسبوع متابعة جديد", fontWeight = FontWeight.Bold) },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text("اكتب عنوان الأسبوع أو الفترة (مثال: الأسبوع الأول - محرم، أو رصد يونيو):", fontSize = 12.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = weekName,
                        onValueChange = { weekName = it },
                        label = { Text("عنوان أسبوع الرصد") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("add_week_name_field")
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
                    Text("إنشاء السجل")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddWeekDialog = false }) {
                    Text("إلغاء")
                }
            }
        )
    }

    if (showEditDialog) {
        EditStudentDialog(
            student = activeStudent,
            onDismiss = { showEditDialog = false },
            onConfirm = { name, group, teacher, notes ->
                val updatedStudent = activeStudent.copy(
                    name = name,
                    groupName = group,
                    teacherName = teacher,
                    notes = notes
                )
                viewModel.updateStudent(updatedStudent)
                showEditDialog = false
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
                            text = "رسالة التشجيع: ${report.teacherFeedback.take(30)}...",
                            fontSize = 13.sp,
                            color = Emerald40,
                            fontWeight = FontWeight.ExtraBold
                        )
                    } else {
                        Text("اضغط للبدء في تدوين وحفظ رصد الأيام", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f), fontWeight = FontWeight.Medium)
                    }
                }
            }
            
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { showRenameDialog = true }) {
                    Icon(Icons.Default.Edit, contentDescription = "تعديل اسم الأسبوع", tint = Color.LightGray)
                }
                IconButton(onClick = { showConfirmDelete = true }) {
                    Icon(Icons.Default.Delete, contentDescription = "حذف الأسبوع", tint = Color.LightGray)
                }
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "عرض السجل",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }
    }

    if (showConfirmDelete) {
        AlertDialog(
            onDismissRequest = { showConfirmDelete = false },
            title = { Text("حذف الأسبوع؟", fontWeight = FontWeight.Bold) },
            text = { Text("هل أنت متأكد من حذف (${report.weekName}) مع كافة عمليات الحفظ المسجلة فيه؟") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDelete()
                        showConfirmDelete = false
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("حذف")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDelete = false }) {
                    Text("إلغاء")
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
                    "تعديل اسم الأسبوع 📝",
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
                    label = { Text("اسم أسبوع الرصد") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
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
                    Text("حفظ")
                }
            },
            dismissButton = {
                TextButton(onClick = { showRenameDialog = false }) {
                    Text("إلغاء")
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
                        Icon(Icons.Default.ArrowBack, contentDescription = "رجوع")
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
                            contentDescription = "مشاركة التقرير مع الوالدين",
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
                            text = "💡 معلومات الاستخدام:",
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
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            } else {
                items(dailyLogs, key = { it.id }) { log ->
                    DayRecordBox(
                        log = log,
                        onLogChange = { updatedLog ->
                            viewModel.updateDailyLog(updatedLog)
                        }
                    )
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
                            text = "🌟 ملاحظة غراس المربي وتشجيعه للأسبوع:",
                            fontWeight = FontWeight.Black,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "تظهر هذه الرسالة في التقرير المشترك مع الوالدين لتحفيز البطل بكلمات طيبة.",
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
                            placeholder = { Text("اكتب رسالة تشجيعية هنا (مثال: واصل تميزك يا بطل، فخورين بك!)") },
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
                            textStyle = TextStyle(fontSize = 13.5.sp, fontWeight = FontWeight.SemiBold)
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
                        Text("مشاركة التقرير كملف PDF 📄", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun DayRecordBox(
    log: DailyLog,
    onLogChange: (DailyLog) -> Unit
) {
    // Elegant container representing a single student record-day
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("day_record_${log.dayName}"),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.8.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.45f)),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Day Name Blue Headline Ribbon with adjustable date field
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "يوم ${log.dayName}",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }

                    OutlinedTextField(
                        value = log.dayDate,
                        onValueChange = { onLogChange(log.copy(dayDate = it)) },
                        placeholder = { Text("التاريخ (مثال: ١٢ يونيو)", fontSize = 13.sp, fontWeight = FontWeight.Bold) },
                        singleLine = true,
                        textStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold),
                        modifier = Modifier
                            .width(180.dp)
                            .testTag("day_date_input_${log.dayName}"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.8f),
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                }

                // Quick assist button to fill with current date
                TextButton(
                    onClick = {
                        try {
                            val sdf = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale("ar"))
                            val dateStr = sdf.format(java.util.Date())
                            onLogChange(log.copy(dayDate = dateStr))
                        } catch (e: Exception) {
                            onLogChange(log.copy(dayDate = "اليوم"))
                        }
                    },
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                    modifier = Modifier.height(34.dp)
                ) {
                    Icon(Icons.Default.DateRange, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.secondary)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("تعيين اليوم", fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.secondary)
                }
            }

            // Columns layout for New Memorization, Recent, and Distant revision
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
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
                        Text("🌱 الْحِفْظُ الْجَدِيدُ", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = Emerald40)
                        StarRatingCycleButton(
                            stars = log.newMemoStars,
                            onCycle = { nextStars ->
                                onLogChange(log.copy(newMemoStars = nextStars))
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SurahAutoCompleteTextField(
                            value = log.newMemoSurahFrom,
                            onValueChange = { onLogChange(log.copy(newMemoSurahFrom = it)) },
                            label = "من سورة",
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = log.newMemoVerseFrom,
                            onValueChange = { onLogChange(log.copy(newMemoVerseFrom = it)) },
                            label = { Text("الآية", fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                            singleLine = true,
                            modifier = Modifier.width(62.dp),
                            textStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold),
                            shape = RoundedCornerShape(8.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next)
                        )
                        SurahAutoCompleteTextField(
                            value = log.newMemoSurahTo,
                            onValueChange = { onLogChange(log.copy(newMemoSurahTo = it)) },
                            label = "إلى سورة",
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = log.newMemoVerseTo,
                            onValueChange = { onLogChange(log.copy(newMemoVerseTo = it)) },
                            label = { Text("الآية", fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                            singleLine = true,
                            modifier = Modifier.width(62.dp),
                            textStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold),
                            shape = RoundedCornerShape(8.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next)
                        )
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
                        Text("💧 الْمَاضِي الْقَرِيبُ", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = Sky40)
                        StarRatingCycleButton(
                            stars = log.recentRevStars,
                            onCycle = { nextStars ->
                                onLogChange(log.copy(recentRevStars = nextStars))
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SurahAutoCompleteTextField(
                            value = log.recentRevSurahFrom,
                            onValueChange = { onLogChange(log.copy(recentRevSurahFrom = it)) },
                            label = "من سورة",
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = log.recentRevVerseFrom,
                            onValueChange = { onLogChange(log.copy(recentRevVerseFrom = it)) },
                            label = { Text("الآية", fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                            singleLine = true,
                            modifier = Modifier.width(62.dp),
                            textStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold),
                            shape = RoundedCornerShape(8.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next)
                        )
                        SurahAutoCompleteTextField(
                            value = log.recentRevSurahTo,
                            onValueChange = { onLogChange(log.copy(recentRevSurahTo = it)) },
                            label = "إلى سورة",
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = log.recentRevVerseTo,
                            onValueChange = { onLogChange(log.copy(recentRevVerseTo = it)) },
                            label = { Text("الآية", fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                            singleLine = true,
                            modifier = Modifier.width(62.dp),
                            textStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold),
                            shape = RoundedCornerShape(8.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next)
                        )
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
                        Text("🔥 الْمَاضِي الْبَعِيدُ", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = Amber40)
                        StarRatingCycleButton(
                            stars = log.distantRevStars,
                            onCycle = { nextStars ->
                                onLogChange(log.copy(distantRevStars = nextStars))
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SurahAutoCompleteTextField(
                            value = log.distantRevSurahFrom,
                            onValueChange = { onLogChange(log.copy(distantRevSurahFrom = it)) },
                            label = "من سورة",
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = log.distantRevVerseFrom,
                            onValueChange = { onLogChange(log.copy(distantRevVerseFrom = it)) },
                            label = { Text("الآية", fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                            singleLine = true,
                            modifier = Modifier.width(62.dp),
                            textStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold),
                            shape = RoundedCornerShape(8.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next)
                        )
                        SurahAutoCompleteTextField(
                            value = log.distantRevSurahTo,
                            onValueChange = { onLogChange(log.copy(distantRevSurahTo = it)) },
                            label = "إلى سورة",
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = log.distantRevVerseTo,
                            onValueChange = { onLogChange(log.copy(distantRevVerseTo = it)) },
                            label = { Text("الآية", fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                            singleLine = true,
                            modifier = Modifier.width(62.dp),
                            textStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold),
                            shape = RoundedCornerShape(8.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done)
                        )
                    }
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
        3 -> Pair("امتياز ⭐⭐⭐", Emerald40)
        2 -> Pair("جيد جداً ⭐⭐", Sky40)
        1 -> Pair("جيد وطيب ⭐", Amber40)
        4 -> Pair("إعادة تسميع ❌", Color.Red)
        else -> Pair("لم يـرصد ⚪", Color.Gray)
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
    canvas.drawText("سِجِلّ تَاجُ الْوَقَارِ لِمُتَابَعَةِ تِلَاوَةِ وَحِفْظِ الْقُرْآنِ الْكَرِيمِ 📖", 421f, 50f, bannerTitlePaint)

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
    canvas.drawText("👤 الطَّالِبُ/ـة:", 800f, 120f, infoLabelPaint)
    canvas.drawText(student.name, 800f, 145f, infoValPaint)

    canvas.drawText("🏫 الصَّفُّ/الْحَلَقَةُ:", 550f, 120f, infoLabelPaint)
    canvas.drawText(student.groupName.ifBlank { "غير محددة" }, 550f, 145f, infoValPaint)

    canvas.drawText("👤 الْمُعَلِّمُ الْمُرَبِّي:", 320f, 120f, infoLabelPaint)
    canvas.drawText(student.teacherName.ifBlank { "غير محدد" }, 320f, 145f, infoValPaint)

    canvas.drawText("📅 فَتْرَةُ الرَّصْدِ:", 120f, 120f, infoLabelPaint)
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
        textSize = 13f
        isAntiAlias = true
        typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
        textAlign = Paint.Align.RIGHT
    }

    // Column positions (RTL representation)
    canvas.drawText("اليوم والتاريخ", 810f, 204f, headerTextPaint)
    canvas.drawText("🌱 الْحِفْظُ الْجَدِيدُ", 630f, 204f, headerTextPaint)
    canvas.drawText("💧 الْمَاضِي الْقَرِيبُ", 420f, 204f, headerTextPaint)
    canvas.drawText("🔥 الْمَاضِي الْبَعِيدُ", 210f, 204f, headerTextPaint)

    val borderPaint = Paint().apply {
        color = colorBorder
        style = Paint.Style.STROKE
        strokeWidth = 1.8f
        isAntiAlias = true
    }

    var currentY = 218f
    val rowHeight = 52f

    logs.forEach { log ->
        val isEven = logs.indexOf(log) % 2 == 0
        val rowBgPaint = Paint().apply {
            color = if (isEven) android.graphics.Color.parseColor("#F1F5F9") else android.graphics.Color.WHITE
            style = Paint.Style.FILL
        }
        canvas.drawRect(25f, currentY, 817f, currentY + rowHeight, rowBgPaint)
        
        // Draw horizontal row bottom line
        canvas.drawLine(25f, currentY + rowHeight, 817f, currentY + rowHeight, borderPaint)

        // Draw Column 1: Day Name & Date
        val boldDayPaint = Paint().apply {
            color = colorSlateDark
            textSize = 13.5f
            isAntiAlias = true
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            textAlign = Paint.Align.RIGHT
        }
        val normalDatePaint = Paint().apply {
            color = colorSlateLabel
            textSize = 11f
            isAntiAlias = true
            textAlign = Paint.Align.RIGHT
        }
        canvas.drawText(log.dayName, 810f, currentY + 22f, boldDayPaint)
        if (log.dayDate.isNotBlank()) {
            canvas.drawText(log.dayDate, 810f, currentY + 41f, normalDatePaint)
        } else {
            canvas.drawText("—", 810f, currentY + 41f, normalDatePaint)
        }

        // Draw Cell Function
        fun drawCell(
            surahFrom: String, verseFrom: String,
            surahTo: String, verseTo: String,
            stars: Int,
            rightX: Float,
            colorHex: String
        ) {
            val hasContent = surahFrom.isNotBlank() || surahTo.isNotBlank()
            if (hasContent) {
                val fromText = if (surahFrom.isNotBlank()) "من $surahFrom" + (if (verseFrom.isNotBlank()) " ($verseFrom)" else "") else ""
                val toText = if (surahTo.isNotBlank()) "إلى $surahTo" + (if (verseTo.isNotBlank()) " ($verseTo)" else "") else ""
                val combinedText = "$fromText $toText".trim()

                val contentPaint = Paint().apply {
                    color = colorSlateDark
                    textSize = 11.5f
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
                    textSize = 11f
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

        // C2: New Memory (Green)
        drawCell(
            log.newMemoSurahFrom, log.newMemoVerseFrom,
            log.newMemoSurahTo, log.newMemoVerseTo,
            log.newMemoStars,
            630f,
            "#059669"
        )

        // C3: Recent Rev (Blue)
        drawCell(
            log.recentRevSurahFrom, log.recentRevVerseFrom,
            log.recentRevSurahTo, log.recentRevVerseTo,
            log.recentRevStars,
            420f,
            "#0EA5E9"
        )

        // C4: Distant Rev (Amber)
        drawCell(
            log.distantRevSurahFrom, log.distantRevVerseFrom,
            log.distantRevSurahTo, log.distantRevVerseTo,
            log.distantRevStars,
            210f,
            "#D97706"
        )

        currentY += rowHeight
    }

    // Draw Vertical columns borders
    canvas.drawLine(25f, 180f, 25f, currentY, borderPaint)
    canvas.drawLine(220f, 180f, 220f, currentY, borderPaint)
    canvas.drawLine(430f, 180f, 430f, currentY, borderPaint)
    canvas.drawLine(640f, 180f, 640f, currentY, borderPaint)
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
        canvas.drawText("💌 تَوْجِيهُ وَمُلَاحَظَاتُ الْمُرَبِّي لِلأُسْبُوعِ:", 805f, commentY + 20f, noteLabelPaint)
        
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
    canvas.drawText("📖 «خَيْرُكُمْ مَنْ تَعَلَّمَ القُرْآنَ وَعَلَّمَهُ» - تم التصدير كملف PDF رقمي عبر تطبيق تاج الوقار 📖", 421f, 575f, sloganPaint)

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
            putExtra(Intent.EXTRA_SUBJECT, "سجل تاج الوقار: ${student.name}")
            putExtra(Intent.EXTRA_TEXT, "السلام عليكم ورحمة الله وبركاته، نرسل لكم تقرير مستوى تسميع ومتابعة القرآن الكريم لـ الأسبوع (${weekName}) للطالب البطل: ${student.name}. نسأل الله أن يجعله من أهل القرآن.")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        val shareIntent = Intent.createChooser(intent, "تصدير ومشاركة تقرير PDF:")
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
                    .size(240.dp)
                    .graphicsLayer(
                        scaleX = scale * glowScale,
                        scaleY = scale * glowScale,
                        alpha = alpha
                    )
            ) {
                // Glow aura container
                Box(
                    modifier = Modifier
                        .size(220.dp)
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

                // Beautifully designed programmatical replica of the requested APP ICON
                Card(
                    modifier = Modifier
                        .size(175.dp)
                        .border(
                            BorderStroke(4.dp, Brush.linearGradient(listOf(Color(0xFFF59E0B), Color(0xFFFFFBEB), Color(0xFFD97706)))),
                            RoundedCornerShape(32.dp)
                        ),
                    shape = RoundedCornerShape(32.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF721C24) // Deep burgundy leather Quran background
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0xFF881B28),
                                        Color(0xFF5B121C)
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        // Inner elegant border
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(10.dp)
                                .border(BorderStroke(1.dp, Color(0xFFD97706).copy(alpha = 0.6f)), RoundedCornerShape(22.dp))
                        )

                        // Central Gold Medallion Halo containing the Holy Quran Book
                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFD97706).copy(alpha = 0.2f))
                                .border(BorderStroke(1.dp, Color(0xFFF59E0B).copy(alpha = 0.6f)), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            // Custom drawn open book on Canvas (guaranteed 100% build size & speed without any heavy downloads)
                            androidx.compose.foundation.Canvas(modifier = Modifier.size(44.dp)) {
                                val bookPath = androidx.compose.ui.graphics.Path().apply {
                                    // Left page curve
                                    moveTo(size.width * 0.1f, size.height * 0.35f)
                                    quadraticTo(size.width * 0.3f, size.height * 0.2f, size.width * 0.48f, size.height * 0.3f)
                                    lineTo(size.width * 0.48f, size.height * 0.85f)
                                    quadraticTo(size.width * 0.3f, size.height * 0.75f, size.width * 0.1f, size.height * 0.9f)
                                    close()
                                    
                                    // Right page curve
                                    moveTo(size.width * 0.9f, size.height * 0.35f)
                                    quadraticTo(size.width * 0.7f, size.height * 0.2f, size.width * 0.52f, size.height * 0.3f)
                                    lineTo(size.width * 0.52f, size.height * 0.85f)
                                    quadraticTo(size.width * 0.7f, size.height * 0.75f, size.width * 0.9f, size.height * 0.9f)
                                    close()
                                }
                                drawPath(
                                    path = bookPath,
                                    color = Color(0xFFFFFBEB),
                                    style = androidx.compose.ui.graphics.drawscope.Fill
                                )
                                // Draw book spine
                                drawLine(
                                    color = Color(0xFFD97706),
                                    start = androidx.compose.ui.geometry.Offset(size.width * 0.5f, size.height * 0.30f),
                                    end = androidx.compose.ui.geometry.Offset(size.width * 0.5f, size.height * 0.88f),
                                    strokeWidth = 3f
                                )
                            }
                        }

                        // Sparkles around
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            modifier = Modifier
                                .size(14.dp)
                                .align(Alignment.TopEnd)
                                .offset(x = (-22).dp, y = 22.dp),
                            tint = Color(0xFFF59E0B)
                        )
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            modifier = Modifier
                                .size(10.dp)
                                .align(Alignment.BottomStart)
                                .offset(x = 22.dp, y = (-22).dp),
                            tint = Color(0xFFFDE68A)
                        )

                        // Luxurious Fountain Pen overlaying the book, laid elegantly at an angle
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            contentAlignment = Alignment.BottomEnd
                        ) {
                            Row(
                                modifier = Modifier
                                    .graphicsLayer {
                                        rotationZ = -30f
                                    }
                                    .offset(x = 4.dp, y = (-4).dp)
                                    .background(
                                        Brush.horizontalGradient(
                                            colors = listOf(
                                                Color(0xFF450A0A), // Burgundy body
                                                Color(0xFFF59E0B)  // Gold trim nib
                                            )
                                        ),
                                        shape = RoundedCornerShape(4.dp)
                                    )
                                    .border(BorderStroke(1.dp, Color(0xFFFFFBEB)), RoundedCornerShape(4.dp))
                                    .padding(horizontal = 8.dp, vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Create, // Fountain pen symbol
                                    contentDescription = null,
                                    modifier = Modifier.size(15.dp),
                                    tint = Color(0xFFFFFBEB)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Application Title Ribbon "سجل تاج الوقار" with glowing Gold / White Typography
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF059669).copy(alpha = 0.9f)),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.5.dp, Color(0xFFFDE68A).copy(alpha = 0.8f)),
                modifier = Modifier
                    .graphicsLayer(alpha = alpha, scaleX = scale, scaleY = scale)
            ) {
                Text(
                    text = "سِجِلّ تَاجُ الْوَقَارِ 📖",
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
                text = "متابعة تلاوة وحفظ القرآن الكريم للطلاب والناشئة\n«مَن تَعَلَّمَ القُرْآنَ وَعَلَّمَهُ»",
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
                text = "الدخول للتطبيق",
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

// Auto-complete field for Surah names
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurahAutoCompleteTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val filteredSurahs = remember(value) {
        if (value.isBlank()) {
            quranSurahs
        } else {
            val normalizedValue = value.replace("أ", "ا")
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
        onExpandedChange = { expanded = it },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {
                onValueChange(it)
                expanded = true
            },
            label = { Text(label, fontSize = 12.sp, fontWeight = FontWeight.Bold) },
            singleLine = true,
            modifier = Modifier.menuAnchor(),
            textStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold),
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
    onConfirm: (String, String, String, String) -> Unit
) {
    var name by remember { mutableStateOf(student.name) }
    var group by remember { mutableStateOf(student.groupName) }
    var teacher by remember { mutableStateOf(student.teacherName) }
    var notes by remember { mutableStateOf(student.notes) }
    
    var errorMsg by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "تعديل بيانات الطالب 📝",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Right
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { 
                        name = it
                        if (it.isNotBlank()) errorMsg = ""
                    },
                    label = { Text("اسم الطالب الكامل *") },
                    isError = errorMsg.isNotEmpty(),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                if (errorMsg.isNotEmpty()) {
                    Text(errorMsg, color = MaterialTheme.colorScheme.error, fontSize = 10.sp)
                }
                
                OutlinedTextField(
                    value = group,
                    onValueChange = { group = it },
                    label = { Text("الصف / حلقة التحفيظ") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                
                OutlinedTextField(
                    value = teacher,
                    onValueChange = { teacher = it },
                    label = { Text("اسم المعلم المربي") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("ملاحظات إضافية (أهداف الحفظ، إلخ)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isBlank()) {
                        errorMsg = "اسم الطالب مطلوب للرصد"
                    } else {
                        onConfirm(name, group, teacher, notes)
                    }
                }
            ) {
                Text("حفظ التعديلات")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("إلغاء")
            }
        }
    )
}

