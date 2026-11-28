package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.local.TajuDatabase
import com.example.data.repository.HistoryRepository
import com.example.data.repository.PreferencesRepository
import com.example.navigation.AppNavigator
import com.example.ui.theme.TajuCalculatorTheme
import com.example.viewmodel.CalculatorViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = TajuDatabase.getInstance(applicationContext)
        val historyRepository = HistoryRepository(database.historyDao())
        val preferencesRepository = PreferencesRepository(applicationContext)
        val factory = CalculatorViewModel.Factory(historyRepository, preferencesRepository)

        setContent {
            val viewModel: CalculatorViewModel = viewModel(factory = factory)
            val state by viewModel.uiState.collectAsStateWithLifecycle()

            TajuCalculatorTheme(themeMode = state.preferences.themeMode) {
                AppNavigator(
                    state = state,
                    viewModel = viewModel
                )
            }
        }
    }
}
