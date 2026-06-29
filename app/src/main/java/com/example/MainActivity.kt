package com.example

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.data.backup.AppPreferences
import com.example.ui.screen.QuranAppScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.QuranViewModel
import java.util.Locale

class MainActivity : ComponentActivity() {
  private val viewModel: QuranViewModel by viewModels()

  override fun attachBaseContext(newBase: Context) {
    val prefs = AppPreferences(newBase)
    val lang = prefs.appLanguage
    val locale = Locale(lang)
    Locale.setDefault(locale)
    val config = Configuration(newBase.resources.configuration)
    config.setLocale(locale)
    val localizedContext = newBase.createConfigurationContext(config)
    super.attachBaseContext(localizedContext)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    // Request notification permission for Android 13+
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
      val requestPermissionLauncher = registerForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.RequestPermission()
      ) { _ -> }
      if (androidx.core.content.ContextCompat.checkSelfPermission(
          this,
          android.Manifest.permission.POST_NOTIFICATIONS
        ) != android.content.pm.PackageManager.PERMISSION_GRANTED
      ) {
        requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
      }
    }

    setContent {
      val isDarkModeOption by viewModel.isDarkMode.collectAsState()
      val systemDark = isSystemInDarkTheme()
      val darkTheme = isDarkModeOption ?: systemDark

      MyApplicationTheme(darkTheme = darkTheme) {
        QuranAppScreen(viewModel = viewModel)
      }
    }
  }
}
