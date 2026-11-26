package com.example.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.engine.AngleMode
import com.example.ui.theme.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "taju_settings")

data class UserPreferences(
    val themeMode: ThemeMode = ThemeMode.DARK,
    val angleMode: AngleMode = AngleMode.DEG,
    val thousandsSeparator: Boolean = true,
    val hapticFeedback: Boolean = true,
    val memoryValue: Double = 0.0
)

class PreferencesRepository(private val context: Context) {

    private object Keys {
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val ANGLE_MODE = stringPreferencesKey("angle_mode")
        val THOUSANDS_SEPARATOR = booleanPreferencesKey("thousands_separator")
        val HAPTIC_FEEDBACK = booleanPreferencesKey("haptic_feedback")
        val MEMORY_VALUE = doublePreferencesKey("memory_value")
    }

    val preferencesFlow: Flow<UserPreferences> = context.dataStore.data.map { prefs ->
        val themeName = prefs[Keys.THEME_MODE] ?: ThemeMode.DARK.name
        val angleName = prefs[Keys.ANGLE_MODE] ?: AngleMode.DEG.name
        UserPreferences(
            themeMode = ThemeMode.entries.firstOrNull { it.name == themeName } ?: ThemeMode.DARK,
            angleMode = AngleMode.entries.firstOrNull { it.name == angleName } ?: AngleMode.DEG,
            thousandsSeparator = prefs[Keys.THOUSANDS_SEPARATOR] ?: true,
            hapticFeedback = prefs[Keys.HAPTIC_FEEDBACK] ?: true,
            memoryValue = prefs[Keys.MEMORY_VALUE] ?: 0.0
        )
    }

    suspend fun setThemeMode(mode: ThemeMode) {
        context.dataStore.edit { it[Keys.THEME_MODE] = mode.name }
    }

    suspend fun setAngleMode(mode: AngleMode) {
        context.dataStore.edit { it[Keys.ANGLE_MODE] = mode.name }
    }

    suspend fun setThousandsSeparator(enabled: Boolean) {
        context.dataStore.edit { it[Keys.THOUSANDS_SEPARATOR] = enabled }
    }

    suspend fun setHapticFeedback(enabled: Boolean) {
        context.dataStore.edit { it[Keys.HAPTIC_FEEDBACK] = enabled }
    }

    suspend fun setMemoryValue(value: Double) {
        context.dataStore.edit { it[Keys.MEMORY_VALUE] = value }
    }
}
