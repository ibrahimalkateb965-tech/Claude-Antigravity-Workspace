package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.ui.screen.QuranAppScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.QuranViewModel

class MainActivity : ComponentActivity() {
  private val viewModel: QuranViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        QuranAppScreen(viewModel = viewModel)
      }
    }
  }
}
