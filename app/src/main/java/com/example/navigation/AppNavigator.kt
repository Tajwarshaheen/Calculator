package com.example.navigation

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.R
import com.example.ui.components.TajuBottomNavigation
import com.example.ui.components.TajuNavigationRail
import com.example.ui.screens.AboutScreen
import com.example.ui.screens.CalculatorScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.ScientificScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.theme.LocalTajuColors
import com.example.ui.theme.TajuTealSecondary
import com.example.viewmodel.CalculatorState
import com.example.viewmodel.CalculatorViewModel
import kotlinx.coroutines.launch

@Composable
fun AppNavigator(
    state: CalculatorState,
    viewModel: CalculatorViewModel
) {
    var showSplash by remember { mutableStateOf(true) }
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val taju = LocalTajuColors.current
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(showSplash) {
        if (!showSplash) {
            runCatching { focusRequester.requestFocus() }
        }
    }

    val copyToClipboard: (String) -> Unit = { value ->
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(ClipData.newPlainText("Taju Calculator Result", value))
        scope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()
            snackbarHostState.showSnackbar(context.getString(R.string.copied_to_clipboard))
        }
    }

    val navigateToTab: (String) -> Unit = { route ->
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = if (taju.isDark) {
                        listOf(
                            TajuTealSecondary.copy(alpha = 0.28f),
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.background
                        )
                    } else {
                        listOf(
                            Color(0xFFE3ECF7),
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.background
                        )
                    }
                )
            )
            .focusRequester(focusRequester)
            .focusable()
            .onPreviewKeyEvent { keyEvent ->
                if (showSplash || keyEvent.type != KeyEventType.KeyDown) return@onPreviewKeyEvent false
                val isSci = currentRoute == AppRoutes.SCIENTIFIC
                val isCalc = currentRoute == AppRoutes.CALCULATOR || isSci
                if (!isCalc) return@onPreviewKeyEvent false

                when {
                    keyEvent.key == Key.Zero || keyEvent.key == Key.NumPad0 -> {
                        viewModel.onDigit("0"); true
                    }
                    keyEvent.key == Key.One || keyEvent.key == Key.NumPad1 -> {
                        viewModel.onDigit("1"); true
                    }
                    keyEvent.key == Key.Two || keyEvent.key == Key.NumPad2 -> {
                        viewModel.onDigit("2"); true
                    }
                    keyEvent.key == Key.Three || keyEvent.key == Key.NumPad3 -> {
                        viewModel.onDigit("3"); true
                    }
                    keyEvent.key == Key.Four || keyEvent.key == Key.NumPad4 -> {
                        viewModel.onDigit("4"); true
                    }
                    keyEvent.key == Key.Five && keyEvent.isShiftPressed -> {
                        viewModel.onPercentage(); true
                    }
                    keyEvent.key == Key.Five || keyEvent.key == Key.NumPad5 -> {
                        viewModel.onDigit("5"); true
                    }
                    keyEvent.key == Key.Six && keyEvent.isShiftPressed -> {
                        viewModel.onOperator("^"); true
                    }
                    keyEvent.key == Key.Six || keyEvent.key == Key.NumPad6 -> {
                        viewModel.onDigit("6"); true
                    }
                    keyEvent.key == Key.Seven || keyEvent.key == Key.NumPad7 -> {
                        viewModel.onDigit("7"); true
                    }
                    keyEvent.key == Key.Eight && keyEvent.isShiftPressed -> {
                        viewModel.onOperator("×"); true
                    }
                    keyEvent.key == Key.Eight || keyEvent.key == Key.NumPad8 -> {
                        viewModel.onDigit("8"); true
                    }
                    keyEvent.key == Key.Nine && keyEvent.isShiftPressed -> {
                        viewModel.onLeftParen(); true
                    }
                    keyEvent.key == Key.Nine || keyEvent.key == Key.NumPad9 -> {
                        viewModel.onDigit("9"); true
                    }
                    keyEvent.key == Key.Zero && keyEvent.isShiftPressed -> {
                        viewModel.onRightParen(); true
                    }
                    keyEvent.key == Key.Period || keyEvent.key == Key.NumPadDot -> {
                        viewModel.onDecimal(); true
                    }
                    keyEvent.key == Key.Plus || keyEvent.key == Key.NumPadAdd ||
                        (keyEvent.key == Key.Equals && keyEvent.isShiftPressed) -> {
                        viewModel.onOperator("+"); true
                    }
                    keyEvent.key == Key.Minus || keyEvent.key == Key.NumPadSubtract -> {
                        viewModel.onOperator("−"); true
                    }
                    keyEvent.key == Key.Multiply || keyEvent.key == Key.NumPadMultiply -> {
                        viewModel.onOperator("×"); true
                    }
                    keyEvent.key == Key.Slash || keyEvent.key == Key.NumPadDivide -> {
                        viewModel.onOperator("÷"); true
                    }
                    keyEvent.key == Key.Equals || keyEvent.key == Key.Enter || keyEvent.key == Key.NumPadEnter -> {
                        viewModel.onEquals(isScientific = isSci); true
                    }
                    keyEvent.key == Key.Backspace -> {
                        viewModel.onDelete(); true
                    }
                    keyEvent.key == Key.Escape || keyEvent.key == Key.Delete -> {
                        viewModel.onClearAll(); true
                    }
                    else -> false
                }
            }
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val useRail = maxWidth >= 680.dp

            Scaffold(
                containerColor = Color.Transparent,
                contentWindowInsets = WindowInsets(0, 0, 0, 0),
                snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                bottomBar = {
                    if (!useRail && !showSplash && currentRoute != AppRoutes.ABOUT) {
                        TajuBottomNavigation(
                            currentRoute = currentRoute,
                            onNavigate = navigateToTab
                        )
                    }
                }
            ) { innerPadding ->
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .windowInsetsPadding(WindowInsets.statusBars)
                        .padding(innerPadding)
                ) {
                    if (useRail && !showSplash) {
                        TajuNavigationRail(
                            currentRoute = currentRoute,
                            onNavigate = navigateToTab
                        )
                    }

                    NavHost(
                        navController = navController,
                        startDestination = AppRoutes.CALCULATOR,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                    ) {
                        composable(AppRoutes.CALCULATOR) {
                            CalculatorScreen(
                                state = state,
                                viewModel = viewModel,
                                onCopyResult = {
                                    copyToClipboard(viewModel.getCopyableResult())
                                }
                            )
                        }
                        composable(AppRoutes.SCIENTIFIC) {
                            ScientificScreen(
                                state = state,
                                viewModel = viewModel,
                                onCopyResult = {
                                    copyToClipboard(viewModel.getCopyableResult())
                                }
                            )
                        }
                        composable(AppRoutes.HISTORY) {
                            HistoryScreen(
                                state = state,
                                onSelectHistoryItem = { item ->
                                    viewModel.loadFromHistory(item)
                                    navigateToTab(
                                        if (item.isScientific) AppRoutes.SCIENTIFIC else AppRoutes.CALCULATOR
                                    )
                                },
                                onCopyResult = copyToClipboard,
                                onDeleteItem = viewModel::deleteHistoryItem,
                                onClearAll = viewModel::clearAllHistory
                            )
                        }
                        composable(AppRoutes.SETTINGS) {
                            SettingsScreen(
                                state = state,
                                onSetThemeMode = viewModel::setThemeMode,
                                onSetAngleMode = viewModel::setAngleMode,
                                onSetThousandsSeparator = viewModel::setThousandsSeparator,
                                onSetHapticFeedback = viewModel::setHapticFeedback,
                                onResetMemory = {
                                    viewModel.onMemoryClear()
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Memory register cleared")
                                    }
                                },
                                onClearHistory = {
                                    viewModel.clearAllHistory()
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Calculation history cleared")
                                    }
                                },
                                onNavigateToAbout = {
                                    navController.navigate(AppRoutes.ABOUT)
                                }
                            )
                        }
                        composable(AppRoutes.ABOUT) {
                            AboutScreen(
                                onBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = showSplash,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            SplashScreen(onFinished = { showSplash = false })
        }
    }
}
