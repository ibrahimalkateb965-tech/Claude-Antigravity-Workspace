package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.QuranDatabase
import com.example.data.model.DailyLog
import com.example.data.model.Student
import com.example.data.model.WeeklyReport
import com.example.data.repository.QuranRepository
import com.example.data.backup.AppPreferences
import com.example.data.notification.AlarmScheduler
import com.example.ui.screen.loc
import com.example.data.notification.NotificationHelper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class Screen {
    object Splash : Screen()
    object StudentsList : Screen()
    data class StudentProfile(val student: Student) : Screen()
    data class ReportTracking(val student: Student, val report: WeeklyReport) : Screen()
    data class PeriodReport(val student: Student) : Screen()
    object Backups : Screen()
    object Settings : Screen()
    object TodaySchedule : Screen()
}

@OptIn(ExperimentalCoroutinesApi::class)
class QuranViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: QuranRepository
    
    // --- Navigation State ---
    private val _currentScreen = MutableStateFlow<Screen>(Screen.Splash)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    // --- Search Query ---
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val appPrefs = AppPreferences(application)

    // --- Theme State (null = follow system, true = dark, false = light) ---
    private val _isDarkMode = MutableStateFlow<Boolean?>(appPrefs.isDarkMode)
    val isDarkMode: StateFlow<Boolean?> = _isDarkMode.asStateFlow()

    fun toggleTheme(isDark: Boolean) {
        _isDarkMode.value = isDark
        appPrefs.isDarkMode = isDark
    }

    // --- Language State ("ar" = Arabic, "en" = English) ---
    private val _appLanguage = MutableStateFlow(appPrefs.appLanguage)
    val appLanguage: StateFlow<String> = _appLanguage.asStateFlow()

    fun setLanguage(lang: String) {
        appPrefs.appLanguage = lang
        _appLanguage.value = lang
        // Update global singleton for use in non-composable lambdas
        com.example.ui.screen.AppLang.current = lang
    }

    fun rescheduleAlarms() {
        viewModelScope.launch {
            try {
                val list = repository.allStudents.first()
                AlarmScheduler.scheduleAllAlarms(getApplication(), list)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    init {
        // Initialize AppLang singleton from preferences
        com.example.ui.screen.AppLang.current = appPrefs.appLanguage

        // Initialize Notification Channels
        NotificationHelper.createNotificationChannels(application)

        val database = QuranDatabase.getDatabase(application)
        repository = QuranRepository(database.quranDao())

        // Reschedule alarms in background to ensure sync on startup
        viewModelScope.launch {
            try {
                val list = repository.allStudents.first()
                AlarmScheduler.scheduleAllAlarms(application, list)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        
        // Auto-navigate from Splash screen to StudentsList after a beautiful transition delay
        viewModelScope.launch {
            kotlinx.coroutines.delay(3000)
            if (_currentScreen.value == Screen.Splash) {
                _currentScreen.value = Screen.StudentsList
            }
        }
    }

    // --- Students State ---
    val students: StateFlow<List<Student>> = combine(
        repository.allStudents,
        _searchQuery
    ) { studentList, query ->
        if (query.isBlank()) {
            studentList
        } else {
            studentList.filter { 
                it.name.contains(query, ignoreCase = true) || 
                it.groupName.contains(query, ignoreCase = true) 
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // --- Student Profile State ---
    private val _selectedStudentId = MutableStateFlow<Int?>(null)
    val selectedStudent: StateFlow<Student?> = _selectedStudentId.flatMapLatest { id ->
        if (id == null) flowOf(null)
        else repository.getStudentById(id)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    val weeklyReports: StateFlow<List<WeeklyReport>> = _selectedStudentId.flatMapLatest { studentId ->
        if (studentId == null) flowOf(emptyList())
        else repository.getWeeklyReportsForStudent(studentId)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // --- Weekly Report Detail State ---
    private val _selectedReportId = MutableStateFlow<Int?>(null)
    val selectedReport: StateFlow<WeeklyReport?> = _selectedReportId.flatMapLatest { id ->
        if (id == null) flowOf(null)
        else repository.getWeeklyReportById(id)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    val dailyLogs: StateFlow<List<DailyLog>> = _selectedReportId.flatMapLatest { reportId ->
        if (reportId == null) flowOf(emptyList())
        else repository.getDailyLogsForReport(reportId)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // --- Navigation Actions ---
    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
        when (screen) {
            is Screen.Splash -> {
                _selectedStudentId.value = null
                _selectedReportId.value = null
            }
            is Screen.StudentsList -> {
                _selectedStudentId.value = null
                _selectedReportId.value = null
            }
            is Screen.StudentProfile -> {
                _selectedStudentId.value = screen.student.id
                _selectedReportId.value = null
            }
            is Screen.ReportTracking -> {
                _selectedStudentId.value = screen.student.id
                _selectedReportId.value = screen.report.id
            }
            is Screen.PeriodReport -> {
                _selectedStudentId.value = screen.student.id
                _selectedReportId.value = null
            }
            is Screen.Backups -> {
                _selectedStudentId.value = null
                _selectedReportId.value = null
            }
            is Screen.Settings -> {
                _selectedStudentId.value = null
                _selectedReportId.value = null
            }
            is Screen.TodaySchedule -> {
                _selectedStudentId.value = null
                _selectedReportId.value = null
            }
        }
    }

    // --- Search Actions ---
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    // --- Student Actions ---
    fun addStudent(
        name: String,
        groupName: String,
        teacherName: String,
        notes: String,
        whatsappNumber: String?,
        circleSessionDaysTimes: String = "",
        onResult: (String?) -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                val cleanWhatsapp = whatsappNumber?.trim()?.takeIf { it.isNotBlank() }
                if (cleanWhatsapp != null) {
                    // Check format: must start with + followed by digits (length between 11 and 16 to support country codes)
                    if (!cleanWhatsapp.startsWith("+") || cleanWhatsapp.substring(1).any { !it.isDigit() } || cleanWhatsapp.length < 11 || cleanWhatsapp.length > 16) {
                        onResult("صيغة رقم الواتساب غير صحيحة! يجب أن يبدأ بـ + يليه رمز الدولة والأرقام (مثال: +966501234567)".loc())
                        return@launch
                    }
                    
                    // Duplicate check inside same group
                    val duplicates = repository.countStudentsWithWhatsappInGroup(groupName, cleanWhatsapp, 0)
                    if (duplicates > 0) {
                        onResult("رقم الواتساب هذا مسجل بالفعل لطالب آخر في نفس الحلقة!".loc())
                        return@launch
                    }
                }

                val student = Student(
                    name = name,
                    groupName = groupName,
                    teacherName = teacherName,
                    notes = notes,
                    whatsappNumber = cleanWhatsapp,
                    circleSessionDaysTimes = circleSessionDaysTimes.trim()
                )
                val insertedId = repository.insertStudent(student)
                
                // Re-sync all alarms
                val allList = repository.allStudents.first()
                AlarmScheduler.scheduleAllAlarms(getApplication(), allList)

                // Trigger report update notification if enabled for this circle
                if (appPrefs.isReportEnabledForGroup(groupName)) {
                    NotificationHelper.sendNotification(
                        context = getApplication(),
                        id = insertedId.toInt(),
                        title = "👤 إضافة طالب جديد للحلقة ✨".loc(),
                        text = "تم تسجيل الطالب البطل $name بنجاح في حلقة $groupName"
                    )
                }

                onResult(null)
            } catch (e: Exception) {
                e.printStackTrace()
                onResult("خطأ أثناء إضافة الطالب: ".loc() + "${e.localizedMessage}")
            }
        }
    }

    fun updateStudent(student: Student, onResult: (String?) -> Unit = {}) {
        viewModelScope.launch {
            try {
                val cleanWhatsapp = student.whatsappNumber?.trim()?.takeIf { it.isNotBlank() }
                if (cleanWhatsapp != null) {
                    if (!cleanWhatsapp.startsWith("+") || cleanWhatsapp.substring(1).any { !it.isDigit() } || cleanWhatsapp.length < 11 || cleanWhatsapp.length > 16) {
                        onResult("صيغة رقم الواتساب غير صحيحة! يجب أن يبدأ بـ + يليه رمز الدولة والأرقام (مثال: +966501234567)".loc())
                        return@launch
                    }
                    
                    val duplicates = repository.countStudentsWithWhatsappInGroup(student.groupName, cleanWhatsapp, student.id)
                    if (duplicates > 0) {
                        onResult("رقم الواتساب هذا مسجل بالفعل لطالب آخر في نفس الحلقة!".loc())
                        return@launch
                    }
                }

                val updatedStudent = student.copy(whatsappNumber = cleanWhatsapp)
                repository.updateStudent(updatedStudent)

                // Re-sync all alarms
                val allList = repository.allStudents.first()
                AlarmScheduler.scheduleAllAlarms(getApplication(), allList)

                // Trigger report update notification if enabled for this circle
                if (appPrefs.isReportEnabledForGroup(student.groupName)) {
                    NotificationHelper.sendNotification(
                        context = getApplication(),
                        id = student.id,
                        title = "✏️ تعديل بيانات طالب 📝".loc(),
                        text = "تم تحديث بيانات الطالب البطل ${student.name} بنجاح."
                    )
                }

                onResult(null)
            } catch (e: Exception) {
                e.printStackTrace()
                onResult("خطأ أثناء تحديث بيانات الطالب: ".loc() + "${e.localizedMessage}")
            }
        }
    }

    fun deleteStudent(student: Student) {
        viewModelScope.launch {
            // If deleting active student, back to home
            if (_selectedStudentId.value == student.id) {
                navigateTo(Screen.StudentsList)
            }
            repository.deleteStudent(student)
        }
    }

    // --- Report Actions ---
    fun addWeeklyReport(studentId: Int, weekName: String) {
        viewModelScope.launch {
            repository.createWeeklyReport(studentId, weekName)
        }
    }

    fun updateWeeklyReport(report: WeeklyReport) {
        viewModelScope.launch {
            repository.updateWeeklyReport(report)
        }
    }

    fun deleteWeeklyReport(report: WeeklyReport) {
        viewModelScope.launch {
            if (_selectedReportId.value == report.id) {
                val student = selectedStudent.value
                if (student != null) {
                    navigateTo(Screen.StudentProfile(student))
                } else {
                    navigateTo(Screen.StudentsList)
                }
            }
            repository.deleteWeeklyReport(report)
        }
    }

    // --- Logging Actions ---
    fun updateDailyLog(log: DailyLog) {
        viewModelScope.launch {
            repository.updateDailyLog(log)
        }
    }

    fun addDayToWeek(weeklyReportId: Int, studentId: Int, dayDate: Long, notes: String = "", onResult: (String?) -> Unit = {}) {
        viewModelScope.launch {
            val result = repository.addDayToWeek(weeklyReportId, studentId, dayDate, notes)
            onResult(result)
        }
    }

    fun deleteDailyLog(log: DailyLog) {
        viewModelScope.launch {
            repository.deleteDailyLog(log)
        }
    }

    suspend fun checkDateAvailability(
        weeklyReportId: Int,
        studentId: Int,
        date: Long,
        excludeLogId: Int = 0
    ): String? {
        return repository.checkDateAvailability(weeklyReportId, studentId, date, excludeLogId)
    }

    suspend fun getDailyLogsForStudentInPeriod(studentId: Int, startDate: Long, endDate: Long): List<DailyLog> {
        return repository.getDailyLogsForStudentInPeriod(studentId, startDate, endDate)
    }

    suspend fun getDailyLogsForGroupInPeriod(groupName: String, startDate: Long, endDate: Long): List<DailyLog> {
        return repository.getDailyLogsForGroupInPeriod(groupName, startDate, endDate)
    }

    suspend fun getAllStudentsList(): List<Student> {
        return repository.allStudents.first()
    }

    suspend fun getAllWeeklyReports(): List<WeeklyReport> {
        return repository.getAllWeeklyReports()
    }

    /**
     * Returns students that have a session scheduled on the given Arabic day name.
     * Groups them by session time.
     */
    suspend fun getStudentsScheduledOnDay(dayName: String): List<Student> {
        return repository.allStudents.first().filter { student ->
            if (student.circleSessionDaysTimes.isBlank()) return@filter false
            student.circleSessionDaysTimes.split(";").any { entry ->
                val parts = entry.split("=")
                parts.getOrNull(0)?.trim() == dayName
            }
        }.sortedBy { student ->
            // Sort by session time on that day
            student.circleSessionDaysTimes.split(";").firstOrNull { entry ->
                entry.split("=").getOrNull(0)?.trim() == dayName
            }?.split("=")?.getOrNull(1) ?: "99:99"
        }
    }

    /**
     * Triggers recalculation of day sequential numbers after toggling absence.
     */
    fun recalculateDayNumbers(studentId: Int) {
        viewModelScope.launch {
            repository.recalculateDayNumbers(studentId)
        }
    }
}
