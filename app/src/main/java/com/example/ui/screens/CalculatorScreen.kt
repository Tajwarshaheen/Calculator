package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.HistoryEntity
import com.example.engine.NumberFormatter
import com.example.ui.components.BasicKeypad
import com.example.ui.components.CalculatorDisplay
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.LocalTajuColors
import com.example.ui.theme.OutfitFontFamily
import com.example.viewmodel.CalculatorState
import com.example.viewmodel.CalculatorViewModel

@Composable
fun CalculatorScreen(
    state: CalculatorState,
    viewModel: CalculatorViewModel,
    onCopyResult: () -> Unit,
    modifier: Modifier = Modifier
) {
    val taju = LocalTajuColors.current

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp, vertical = 8.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        val isWide = maxWidth >= 680.dp

        if (isWide) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1.35f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CalculatorDisplay(
                        state = state,
                        showAngleBadge = false,
                        onToggleAngleMode = viewModel::onToggleAngleMode,
                        onCopyResult = onCopyResult
                    )
                    BasicKeypad(
                        hapticEnabled = state.preferences.hapticFeedback,
                        hasMemory = state.hasMemory,
                        onDigit = viewModel::onDigit,
                        onDecimal = viewModel::onDecimal,
                        onOperator = viewModel::onOperator,
                        onPercentage = viewModel::onPercentage,
                        onLeftParen = viewModel::onLeftParen,
                        onRightParen = viewModel::onRightParen,
                        onNegate = viewModel::onNegate,
                        onDelete = viewModel::onDelete,
                        onClearAll = viewModel::onClearAll,
                        onEquals = { viewModel.onEquals(isScientific = false) },
                        onMemoryClear = viewModel::onMemoryClear,
                        onMemoryRecall = viewModel::onMemoryRecall,
                        onMemoryAdd = viewModel::onMemoryAdd,
                        onMemorySubtract = viewModel::onMemorySubtract,
                        modifier = Modifier.weight(1f)
                    )
                }

                // Supporting Recent History Pane for Tablets/Foldables
                Surface(
                    modifier = Modifier
                        .weight(0.75f)
                        .fillMaxHeight(),
                    shape = RoundedCornerShape(24.dp),
                    color = taju.glassSurface,
                    border = BorderStroke(1.dp, taju.glassBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Recent Calculations",
                            fontFamily = OutfitFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp,
                            color = taju.goldAccent
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        if (state.history.isEmpty()) {
                            Text(
                                text = "Completed calculations appear here for quick reuse.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = taju.mutedText
                            )
                        } else {
                            LazyColumn(
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                contentPadding = PaddingValues(bottom = 8.dp)
                            ) {
                                items(state.history.take(20), key = { it.id }) { item ->
                                    RecentHistoryChip(
                                        item = item,
                                        thousandsSeparator = state.preferences.thousandsSeparator,
                                        onClick = { viewModel.loadFromHistory(item) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .widthIn(max = 540.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CalculatorDisplay(
                    state = state,
                    showAngleBadge = false,
                    onToggleAngleMode = viewModel::onToggleAngleMode,
                    onCopyResult = onCopyResult
                )
                BasicKeypad(
                    hapticEnabled = state.preferences.hapticFeedback,
                    hasMemory = state.hasMemory,
                    onDigit = viewModel::onDigit,
                    onDecimal = viewModel::onDecimal,
                    onOperator = viewModel::onOperator,
                    onPercentage = viewModel::onPercentage,
                    onLeftParen = viewModel::onLeftParen,
                    onRightParen = viewModel::onRightParen,
                    onNegate = viewModel::onNegate,
                    onDelete = viewModel::onDelete,
                    onClearAll = viewModel::onClearAll,
                    onEquals = { viewModel.onEquals(isScientific = false) },
                    onMemoryClear = viewModel::onMemoryClear,
                    onMemoryRecall = viewModel::onMemoryRecall,
                    onMemoryAdd = viewModel::onMemoryAdd,
                    onMemorySubtract = viewModel::onMemorySubtract,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun RecentHistoryChip(
    item: HistoryEntity,
    thousandsSeparator: Boolean,
    onClick: () -> Unit
) {
    val taju = LocalTajuColors.current
    val expr = NumberFormatter.formatExpression(item.expression, thousandsSeparator)
    val res = NumberFormatter.formatResult(item.rawResult, thousandsSeparator)

    Surface(
        shape = RoundedCornerShape(14.dp),
        color = taju.glassSurfaceElevated,
        border = BorderStroke(1.dp, taju.glassBorder),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = expr,
                fontFamily = JetBrainsMonoFontFamily,
                fontSize = 12.sp,
                color = taju.mutedText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "= $res",
                fontFamily = OutfitFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 17.sp,
                color = taju.goldAccent,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
