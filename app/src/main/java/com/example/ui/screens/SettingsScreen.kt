package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.DeleteSweep
import androidx.compose.material.icons.outlined.FormatListNumbered
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Memory
import androidx.compose.material.icons.outlined.Straighten
import androidx.compose.material.icons.outlined.Vibration
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.engine.AngleMode
import com.example.ui.theme.LocalTajuColors
import com.example.ui.theme.OutfitFontFamily
import com.example.ui.theme.ThemeMode
import com.example.viewmodel.CalculatorState

@Composable
fun SettingsScreen(
    state: CalculatorState,
    onSetThemeMode: (ThemeMode) -> Unit,
    onSetAngleMode: (AngleMode) -> Unit,
    onSetThousandsSeparator: (Boolean) -> Unit,
    onSetHapticFeedback: (Boolean) -> Unit,
    onResetMemory: () -> Unit,
    onClearHistory: () -> Unit,
    onNavigateToAbout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val taju = LocalTajuColors.current
    var showClearHistoryConfirm by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 680.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                Text(
                    text = "Settings",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Customize TAJU CALCULATOR preferences",
                    style = MaterialTheme.typography.bodyMedium,
                    color = taju.mutedText
                )
            }

            // Theme Appearance Card
            Surface(
                shape = RoundedCornerShape(22.dp),
                color = taju.glassSurface,
                border = BorderStroke(1.dp, taju.glassBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.DarkMode,
                            contentDescription = "Appearance theme",
                            tint = taju.goldAccent,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Appearance Theme",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = "Switch between Luxury Navy Dark & Champagne Light",
                                style = MaterialTheme.typography.bodyMedium,
                                color = taju.mutedText
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ThemeOptionChip(
                            label = "Dark",
                            selected = state.preferences.themeMode == ThemeMode.DARK,
                            onClick = { onSetThemeMode(ThemeMode.DARK) },
                            testTag = "theme_dark",
                            modifier = Modifier.weight(1f)
                        )
                        ThemeOptionChip(
                            label = "Light",
                            selected = state.preferences.themeMode == ThemeMode.LIGHT,
                            onClick = { onSetThemeMode(ThemeMode.LIGHT) },
                            testTag = "theme_light",
                            modifier = Modifier.weight(1f)
                        )
                        ThemeOptionChip(
                            label = "System",
                            selected = state.preferences.themeMode == ThemeMode.SYSTEM,
                            onClick = { onSetThemeMode(ThemeMode.SYSTEM) },
                            testTag = "theme_system",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Angle Mode Card
            Surface(
                shape = RoundedCornerShape(22.dp),
                color = taju.glassSurface,
                border = BorderStroke(1.dp, taju.glassBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.Straighten,
                            contentDescription = "Trigonometric angle mode",
                            tint = taju.goldAccent,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Trigonometry Angle Unit",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = "Default unit for sin, cos, tan, and inverse functions",
                                style = MaterialTheme.typography.bodyMedium,
                                color = taju.mutedText
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ThemeOptionChip(
                            label = "Degrees (DEG)",
                            selected = state.preferences.angleMode == AngleMode.DEG,
                            onClick = { onSetAngleMode(AngleMode.DEG) },
                            testTag = "settings_angle_deg",
                            modifier = Modifier.weight(1f)
                        )
                        ThemeOptionChip(
                            label = "Radians (RAD)",
                            selected = state.preferences.angleMode == AngleMode.RAD,
                            onClick = { onSetAngleMode(AngleMode.RAD) },
                            testTag = "settings_angle_rad",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Formatting & Haptics Toggles
            Surface(
                shape = RoundedCornerShape(22.dp),
                color = taju.glassSurface,
                border = BorderStroke(1.dp, taju.glassBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    SettingsSwitchRow(
                        icon = Icons.Outlined.FormatListNumbered,
                        title = "Thousands Separator",
                        subtitle = if (state.preferences.thousandsSeparator) {
                            "Enabled — Preview: 1,234,567.89"
                        } else {
                            "Disabled — Preview: 1234567.89"
                        },
                        checked = state.preferences.thousandsSeparator,
                        onCheckedChange = onSetThousandsSeparator,
                        testTag = "switch_thousands_separator"
                    )

                    SettingsSwitchRow(
                        icon = Icons.Outlined.Vibration,
                        title = "Haptic Key Feedback",
                        subtitle = "Tactile vibration response on keypad press",
                        checked = state.preferences.hapticFeedback,
                        onCheckedChange = onSetHapticFeedback,
                        testTag = "switch_haptic_feedback"
                    )
                }
            }

            // Storage & Memory Actions
            Surface(
                shape = RoundedCornerShape(22.dp),
                color = taju.glassSurface,
                border = BorderStroke(1.dp, taju.glassBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    SettingsActionRow(
                        icon = Icons.Outlined.Memory,
                        title = "Reset Memory Register",
                        subtitle = if (state.hasMemory) {
                            "Current stored value: ${state.formattedMemory}"
                        } else {
                            "Memory register is empty (0)"
                        },
                        onClick = onResetMemory,
                        testTag = "action_reset_memory"
                    )
                    SettingsActionRow(
                        icon = Icons.Outlined.DeleteSweep,
                        title = "Clear Calculation History",
                        subtitle = "${state.history.size} item${if (state.history.size == 1) "" else "s"} saved locally",
                        onClick = { showClearHistoryConfirm = true },
                        testTag = "action_clear_history"
                    )
                }
            }

            // About TAJU Card
            Surface(
                shape = RoundedCornerShape(22.dp),
                color = taju.glassSurfaceElevated,
                border = BorderStroke(1.dp, taju.goldBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onNavigateToAbout)
                    .testTag("settings_about_card")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = "About TAJU CALCULATOR",
                            tint = taju.goldAccent,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "About TAJU CALCULATOR",
                                style = MaterialTheme.typography.titleMedium,
                                color = taju.goldAccent
                            )
                            Text(
                                text = "Version 1.0.0 • Offline Precision Engine",
                                style = MaterialTheme.typography.bodyMedium,
                                color = taju.mutedText
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                        contentDescription = "Open About TAJU",
                        tint = taju.goldAccent
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
        }
    }

    if (showClearHistoryConfirm) {
        AlertDialog(
            onDismissRequest = { showClearHistoryConfirm = false },
            containerColor = taju.glassSurfaceElevated,
            title = { Text(stringResource(R.string.clear_history_title)) },
            text = {
                Text(
                    text = stringResource(R.string.clear_history_message),
                    color = taju.mutedText
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showClearHistoryConfirm = false
                        onClearHistory()
                    }
                ) {
                    Text("Clear All", color = taju.errorText, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearHistoryConfirm = false }) {
                    Text("Cancel", color = taju.goldAccent)
                }
            }
        )
    }
}

@Composable
private fun ThemeOptionChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    testTag: String,
    modifier: Modifier = Modifier
) {
    val taju = LocalTajuColors.current
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        color = if (selected) taju.goldAccent.copy(alpha = 0.2f) else taju.glassSurfaceElevated,
        border = BorderStroke(
            width = 1.dp,
            color = if (selected) taju.goldAccent else taju.glassBorder
        ),
        modifier = modifier
            .height(48.dp)
            .testTag(testTag)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = label,
                fontFamily = OutfitFontFamily,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                fontSize = 13.sp,
                color = if (selected) taju.goldAccent else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun SettingsSwitchRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    testTag: String
) {
    val taju = LocalTajuColors.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = taju.goldAccent,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = title, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = taju.mutedText
                )
            }
        }
        Spacer(modifier = Modifier.width(10.dp))
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.testTag(testTag),
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                checkedTrackColor = taju.goldAccent,
                uncheckedThumbColor = taju.mutedText,
                uncheckedTrackColor = taju.glassSurfaceElevated
            )
        )
    }
}

@Composable
private fun SettingsActionRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    testTag: String
) {
    val taju = LocalTajuColors.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 12.dp)
            .testTag(testTag),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = taju.goldAccent,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = title, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = taju.mutedText
                )
            }
        }
        Icon(
            imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
            contentDescription = null,
            tint = taju.mutedText
        )
    }
}
