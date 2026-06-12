package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.QuranDatabase
import com.example.data.model.DailyLog
import com.example.data.model.Student
import com.example.data.model.WeeklyReport
import com.example.data.repository.QuranRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class Screen {
    object Splash : Screen()
    object StudentsList : Screen()
    data class StudentProfile(val student: Student) : Screen()
    data class ReportTracking(val student: Student, val report: WeeklyReport) : Screen()
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

    // --- Theme State (null = follow system, true = dark, false = light) ---
    private val _isDarkMode = MutableStateFlow<Boolean?>(null)
    val isDarkMode: StateFlow<Boolean?> = _isDarkMode.asStateFlow()

    fun toggleTheme(isDark: Boolean) {
        _isDarkMode.value = isDark
    }

    init {
        val database = QuranDatabase.getDatabase(application)
        repository = QuranRepository(database.quranDao())
        
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
        }
    }

    // --- Search Actions ---
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    // --- Student Actions ---
    fun addStudent(name: String, groupName: String, teacherName: String, notes: String) {
        viewModelScope.launch {
            val student = Student(
                name = name,
                groupName = groupName,
                teacherName = teacherName,
                notes = notes
            )
            repository.insertStudent(student)
        }
    }

    fun updateStudent(student: Student) {
        viewModelScope.launch {
            repository.updateStudent(student)
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
}
