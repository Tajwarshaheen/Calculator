package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.local.HistoryEntity
import com.example.data.repository.HistoryRepository
import com.example.data.repository.PreferencesRepository
import com.example.data.repository.UserPreferences
import com.example.engine.AngleMode
import com.example.engine.CalcError
import com.example.engine.CalculatorEngine
import com.example.engine.EvalResult
import com.example.engine.NumberFormatter
import com.example.ui.theme.ThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CalculatorState(
    val expression: String = "",
    val previewResult: String = "",
    val lastEvaluatedExpression: String = "",
    val error: CalcError? = null,
    val justEvaluated: Boolean = false,
    val isInverseTrig: Boolean = false,
    val preferences: UserPreferences = UserPreferences(),
    val history: List<HistoryEntity> = emptyList()
) {
    val formattedExpression: String
        get() = if (expression.isEmpty()) {
            "0"
        } else {
            NumberFormatter.formatExpression(expression, preferences.thousandsSeparator)
        }

    val formattedPreview: String
        get() = if (previewResult.isEmpty()) {
            ""
        } else {
            NumberFormatter.formatResult(previewResult, preferences.thousandsSeparator)
        }

    val formattedMemory: String
        get() = NumberFormatter.formatDouble(preferences.memoryValue, preferences.thousandsSeparator)

    val hasMemory: Boolean
        get() = preferences.memoryValue != 0.0
}

private data class InternalCalcState(
    val expression: String = "",
    val previewResult: String = "",
    val lastEvaluatedExpression: String = "",
    val error: CalcError? = null,
    val justEvaluated: Boolean = false,
    val isInverseTrig: Boolean = false
)

class CalculatorViewModel(
    private val historyRepository: HistoryRepository,
    private val preferencesRepository: PreferencesRepository,
    private val engine: CalculatorEngine = CalculatorEngine()
) : ViewModel() {

    private val internalState = MutableStateFlow(InternalCalcState())

    val uiState: StateFlow<CalculatorState> = combine(
        internalState,
        preferencesRepository.preferencesFlow,
        historyRepository.allHistory
    ) { calc, prefs, history ->
        // Recompute live preview whenever angle mode or expression changes
        val livePreview = computeLivePreview(calc.expression, calc.justEvaluated, prefs.angleMode)
        CalculatorState(
            expression = calc.expression,
            previewResult = livePreview,
            lastEvaluatedExpression = calc.lastEvaluatedExpression,
            error = calc.error,
            justEvaluated = calc.justEvaluated,
            isInverseTrig = calc.isInverseTrig,
            preferences = prefs,
            history = history
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = CalculatorState()
    )

    private fun computeLivePreview(
        expression: String,
        justEvaluated: Boolean,
        angleMode: AngleMode
    ): String {
        if (expression.isEmpty() || justEvaluated) return ""
        val hasOperation = expression.any {
            it in charArrayOf('+', '−', '-', '×', '*', '÷', '/', '^', '%', '!', '(', 'π', 'e', '√')
        } || expression.contains("sin") || expression.contains("cos") ||
            expression.contains("tan") || expression.contains("log") || expression.contains("ln")
        if (!hasOperation) return ""
        return when (val result = engine.evaluate(expression, angleMode, autoCloseParentheses = true)) {
            is EvalResult.Success -> result.rawString
            is EvalResult.Error -> ""
        }
    }

    fun onDigit(digit: String) {
        internalState.update { state ->
            val base = if (state.justEvaluated) "" else state.expression
            val next = if (base == "0" && digit != "0") digit
            else if (base == "0" && digit == "0") "0"
            else base + digit
            state.copy(
                expression = next,
                lastEvaluatedExpression = if (state.justEvaluated) "" else state.lastEvaluatedExpression,
                error = null,
                justEvaluated = false
            )
        }
    }

    fun onDecimal() {
        internalState.update { state ->
            val base = if (state.justEvaluated) "" else state.expression
            if (base.isEmpty()) {
                return@update state.copy(
                    expression = "0.",
                    lastEvaluatedExpression = "",
                    error = null,
                    justEvaluated = false
                )
            }
            val lastChar = base.last()
            if (lastChar == ')' || lastChar == 'π' || lastChar == 'e' || lastChar == '%' || lastChar == '!') {
                return@update state.copy(
                    expression = base + "×0.",
                    error = null,
                    justEvaluated = false
                )
            }
            if (isBinaryOperator(lastChar) || lastChar == '(') {
                return@update state.copy(
                    expression = base + "0.",
                    error = null,
                    justEvaluated = false
                )
            }
            // Check current number token for an existing decimal point
            var idx = base.length - 1
            while (idx >= 0 && (base[idx].isDigit() || base[idx] == '.')) {
                if (base[idx] == '.') return@update state
                idx--
            }
            state.copy(
                expression = base + ".",
                error = null,
                justEvaluated = false
            )
        }
    }

    fun onOperator(op: String) {
        internalState.update { state ->
            val current = state.expression
            if (current.isEmpty()) {
                return@update if (op == "−" || op == "-") {
                    state.copy(expression = "−", error = null, justEvaluated = false)
                } else {
                    state
                }
            }
            val last = current.last()
            val next = when {
                last == '(' -> {
                    if (op == "−" || op == "-") current + "−" else current
                }
                isBinaryOperator(last) -> {
                    // Replace trailing binary operator with new one, unless it's solitary minus at start
                    if (current.length == 1 && (last == '−' || last == '-')) {
                        if (op == "−" || op == "-") current else current
                    } else {
                        current.dropLast(1) + op
                    }
                }
                else -> current + op
            }
            state.copy(
                expression = next,
                lastEvaluatedExpression = if (state.justEvaluated) "" else state.lastEvaluatedExpression,
                error = null,
                justEvaluated = false
            )
        }
    }

    fun onPercentage() {
        internalState.update { state ->
            val current = state.expression
            if (current.isEmpty()) return@update state
            val last = current.last()
            if (last.isDigit() || last == ')' || last == 'π' || last == 'e' || last == '!') {
                state.copy(
                    expression = current + "%",
                    error = null,
                    justEvaluated = false
                )
            } else {
                state
            }
        }
    }

    fun onLeftParen() {
        internalState.update { state ->
            val base = if (state.justEvaluated) "" else state.expression
            state.copy(
                expression = base + "(",
                lastEvaluatedExpression = if (state.justEvaluated) "" else state.lastEvaluatedExpression,
                error = null,
                justEvaluated = false
            )
        }
    }

    fun onRightParen() {
        internalState.update { state ->
            val current = state.expression
            val openCount = current.count { it == '(' }
            val closeCount = current.count { it == ')' }
            if (openCount <= closeCount || current.isEmpty()) return@update state
            val last = current.last()
            if (last == '(' || isBinaryOperator(last)) return@update state
            state.copy(
                expression = current + ")",
                error = null,
                justEvaluated = false
            )
        }
    }

    fun onNegate() {
        internalState.update { state ->
            val current = state.expression
            if (current.isEmpty() || current == "0") {
                return@update state.copy(expression = "−", error = null, justEvaluated = false)
            }
            if (current == "−" || current == "-") {
                return@update state.copy(expression = "", error = null, justEvaluated = false)
            }

            // If entire expression is a single negative number like -42
            if ((current.startsWith("−") || current.startsWith("-")) &&
                current.drop(1).all { it.isDigit() || it == '.' || it == 'E' }
            ) {
                return@update state.copy(
                    expression = current.drop(1),
                    error = null,
                    justEvaluated = false
                )
            }

            // If entire expression is a single positive number like 42
            if (current.all { it.isDigit() || it == '.' || it == 'E' }) {
                return@update state.copy(
                    expression = "−$current",
                    error = null,
                    justEvaluated = false
                )
            }

            // If ends with (-number) -> unwrap to number
            val wrappedNegRegex = Regex("""\(−([0-9.]+)\)$""")
            val match = wrappedNegRegex.find(current)
            if (match != null) {
                val number = match.groupValues[1]
                return@update state.copy(
                    expression = current.removeRange(match.range) + number,
                    error = null,
                    justEvaluated = false
                )
            }

            // If ends with (- -> remove (-
            if (current.endsWith("(−")) {
                return@update state.copy(
                    expression = current.dropLast(2),
                    error = null,
                    justEvaluated = false
                )
            }

            // If ends with a number token -> wrap in (-number)
            val trailingNumberRegex = Regex("""([0-9.]+)$""")
            val numMatch = trailingNumberRegex.find(current)
            if (numMatch != null) {
                val number = numMatch.groupValues[1]
                return@update state.copy(
                    expression = current.removeRange(numMatch.range) + "(−$number)",
                    error = null,
                    justEvaluated = false
                )
            }

            // Otherwise append (-
            state.copy(
                expression = current + "(−",
                error = null,
                justEvaluated = false
            )
        }
    }

    fun onScientificFunction(functionPrefix: String) {
        internalState.update { state ->
            val base = if (state.justEvaluated) "" else state.expression
            state.copy(
                expression = base + functionPrefix,
                lastEvaluatedExpression = if (state.justEvaluated) "" else state.lastEvaluatedExpression,
                error = null,
                justEvaluated = false
            )
        }
    }

    fun onConstant(constant: String) {
        internalState.update { state ->
            val base = if (state.justEvaluated) "" else state.expression
            state.copy(
                expression = base + constant,
                lastEvaluatedExpression = if (state.justEvaluated) "" else state.lastEvaluatedExpression,
                error = null,
                justEvaluated = false
            )
        }
    }

    fun onPowerExponent(exponent: String) {
        internalState.update { state ->
            val current = state.expression
            if (current.isEmpty()) return@update state
            val last = current.last()
            if (last.isDigit() || last == ')' || last == 'π' || last == 'e' || last == '%' || last == '!') {
                state.copy(
                    expression = current + "^$exponent",
                    error = null,
                    justEvaluated = false
                )
            } else {
                state
            }
        }
    }

    fun onFactorial() {
        internalState.update { state ->
            val current = state.expression
            if (current.isEmpty()) return@update state
            val last = current.last()
            if (last.isDigit() || last == ')' || last == 'π' || last == 'e') {
                state.copy(
                    expression = current + "!",
                    error = null,
                    justEvaluated = false
                )
            } else {
                state
            }
        }
    }

    fun onDelete() {
        internalState.update { state ->
            val current = state.expression
            if (current.isEmpty()) return@update state.copy(error = null)

            // Delete multi-character function tokens as a single unit
            val multiTokens = listOf("sin⁻¹(", "cos⁻¹(", "tan⁻¹(", "sin(", "cos(", "tan(", "log(", "ln(", "√(")
            val matchedToken = multiTokens.firstOrNull { current.endsWith(it) }
            val next = if (matchedToken != null) {
                current.dropLast(matchedToken.length)
            } else {
                current.dropLast(1)
            }

            state.copy(
                expression = next,
                error = null,
                justEvaluated = false
            )
        }
    }

    fun onClearAll() {
        internalState.value = InternalCalcState(isInverseTrig = internalState.value.isInverseTrig)
    }

    fun onToggleInverseTrig() {
        internalState.update { it.copy(isInverseTrig = !it.isInverseTrig) }
    }

    fun onToggleAngleMode() {
        val current = uiState.value.preferences.angleMode
        val next = if (current == AngleMode.DEG) AngleMode.RAD else AngleMode.DEG
        viewModelScope.launch {
            preferencesRepository.setAngleMode(next)
        }
    }

    fun onEquals(isScientific: Boolean) {
        val current = internalState.value
        val expr = current.expression
        if (expr.isEmpty()) return

        val prefs = uiState.value.preferences
        when (val result = engine.evaluate(expr, prefs.angleMode, autoCloseParentheses = true)) {
            is EvalResult.Success -> {
                val formattedRes = NumberFormatter.formatResult(result.rawString, prefs.thousandsSeparator)
                val formattedExpr = NumberFormatter.formatExpression(expr, prefs.thousandsSeparator)
                internalState.update {
                    it.copy(
                        expression = result.rawString,
                        lastEvaluatedExpression = formattedExpr,
                        error = null,
                        justEvaluated = true
                    )
                }
                if (expr != result.rawString) {
                    viewModelScope.launch {
                        historyRepository.insert(
                            HistoryEntity(
                                expression = expr,
                                result = formattedRes,
                                rawResult = result.rawString,
                                angleMode = prefs.angleMode.name,
                                isScientific = isScientific
                            )
                        )
                    }
                }
            }
            is EvalResult.Error -> {
                internalState.update { it.copy(error = result.error) }
            }
        }
    }

    // Memory Operations
    fun onMemoryClear() {
        viewModelScope.launch {
            preferencesRepository.setMemoryValue(0.0)
        }
    }

    fun onMemoryRecall() {
        val mem = uiState.value.preferences.memoryValue
        val memStr = NumberFormatter.formatDouble(mem, useThousandsSeparator = false)
            .replace("-", "−")
        internalState.update { state ->
            val next = if (state.expression.isEmpty() || state.justEvaluated) {
                memStr
            } else {
                val last = state.expression.last()
                if (isBinaryOperator(last) || last == '(') {
                    if (mem < 0) "${state.expression}($memStr)" else "${state.expression}$memStr"
                } else {
                    if (mem < 0) "${state.expression}×($memStr)" else "${state.expression}×$memStr"
                }
            }
            state.copy(
                expression = next,
                error = null,
                justEvaluated = false
            )
        }
    }

    fun onMemoryAdd() {
        modifyMemory(add = true)
    }

    fun onMemorySubtract() {
        modifyMemory(add = false)
    }

    private fun modifyMemory(add: Boolean) {
        val current = internalState.value
        val prefs = uiState.value.preferences
        val expr = current.expression
        if (expr.isEmpty()) return

        when (val result = engine.evaluate(expr, prefs.angleMode, autoCloseParentheses = true)) {
            is EvalResult.Success -> {
                val updated = if (add) prefs.memoryValue + result.value else prefs.memoryValue - result.value
                viewModelScope.launch {
                    preferencesRepository.setMemoryValue(updated)
                }
            }
            is EvalResult.Error -> {
                internalState.update { it.copy(error = result.error) }
            }
        }
    }

    fun loadFromHistory(item: HistoryEntity) {
        internalState.update {
            it.copy(
                expression = item.rawResult,
                lastEvaluatedExpression = NumberFormatter.formatExpression(
                    item.expression,
                    uiState.value.preferences.thousandsSeparator
                ),
                error = null,
                justEvaluated = true
            )
        }
    }

    fun deleteHistoryItem(id: Long) {
        viewModelScope.launch {
            historyRepository.deleteById(id)
        }
    }

    fun clearAllHistory() {
        viewModelScope.launch {
            historyRepository.clearAll()
        }
    }

    fun setThemeMode(mode: ThemeMode) {
        viewModelScope.launch {
            preferencesRepository.setThemeMode(mode)
        }
    }

    fun setAngleMode(mode: AngleMode) {
        viewModelScope.launch {
            preferencesRepository.setAngleMode(mode)
        }
    }

    fun setThousandsSeparator(enabled: Boolean) {
        viewModelScope.launch {
            preferencesRepository.setThousandsSeparator(enabled)
        }
    }

    fun setHapticFeedback(enabled: Boolean) {
        viewModelScope.launch {
            preferencesRepository.setHapticFeedback(enabled)
        }
    }

    fun getCopyableResult(): String {
        val state = uiState.value
        return when {
            state.previewResult.isNotEmpty() -> state.formattedPreview
            state.expression.isNotEmpty() -> state.formattedExpression
            else -> "0"
        }
    }

    private fun isBinaryOperator(ch: Char): Boolean {
        return ch == '+' || ch == '−' || ch == '-' || ch == '×' || ch == '*' || ch == '÷' || ch == '/' || ch == '^'
    }

    class Factory(
        private val historyRepository: HistoryRepository,
        private val preferencesRepository: PreferencesRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CalculatorViewModel(historyRepository, preferencesRepository) as T
        }
    }
}
