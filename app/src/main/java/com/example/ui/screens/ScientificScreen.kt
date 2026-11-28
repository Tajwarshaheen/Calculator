package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ui.components.CalculatorDisplay
import com.example.ui.components.ScientificKeypad
import com.example.viewmodel.CalculatorState
import com.example.viewmodel.CalculatorViewModel

@Composable
fun ScientificScreen(
    state: CalculatorState,
    viewModel: CalculatorViewModel,
    onCopyResult: () -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp, vertical = 8.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .widthIn(max = 600.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CalculatorDisplay(
                state = state,
                showAngleBadge = true,
                onToggleAngleMode = viewModel::onToggleAngleMode,
                onCopyResult = onCopyResult
            )
            ScientificKeypad(
                hapticEnabled = state.preferences.hapticFeedback,
                hasMemory = state.hasMemory,
                angleMode = state.preferences.angleMode,
                isInverseTrig = state.isInverseTrig,
                onToggleAngleMode = viewModel::onToggleAngleMode,
                onToggleInverseTrig = viewModel::onToggleInverseTrig,
                onDigit = viewModel::onDigit,
                onDecimal = viewModel::onDecimal,
                onOperator = viewModel::onOperator,
                onPercentage = viewModel::onPercentage,
                onLeftParen = viewModel::onLeftParen,
                onRightParen = viewModel::onRightParen,
                onNegate = viewModel::onNegate,
                onScientificFunction = viewModel::onScientificFunction,
                onConstant = viewModel::onConstant,
                onPowerExponent = viewModel::onPowerExponent,
                onFactorial = viewModel::onFactorial,
                onDelete = viewModel::onDelete,
                onClearAll = viewModel::onClearAll,
                onEquals = { viewModel.onEquals(isScientific = true) },
                onMemoryClear = viewModel::onMemoryClear,
                onMemoryRecall = viewModel::onMemoryRecall,
                onMemoryAdd = viewModel::onMemoryAdd,
                onMemorySubtract = viewModel::onMemorySubtract,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
