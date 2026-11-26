package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.engine.AngleMode
import com.example.engine.CalcError
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.LocalTajuColors
import com.example.ui.theme.OutfitFontFamily
import com.example.viewmodel.CalculatorState

@Composable
fun CalculatorDisplay(
    state: CalculatorState,
    showAngleBadge: Boolean,
    onToggleAngleMode: () -> Unit,
    onCopyResult: () -> Unit,
    modifier: Modifier = Modifier
) {
    val taju = LocalTajuColors.current
    val exprScrollState = rememberScrollState()

    LaunchedEffect(state.expression) {
        exprScrollState.animateScrollTo(exprScrollState.maxValue)
    }

    val errorText = when (state.error) {
        CalcError.DIVIDE_BY_ZERO -> stringResource(R.string.error_divide_by_zero)
        CalcError.INVALID_EXPRESSION -> stringResource(R.string.error_invalid_expression)
        CalcError.MATH_DOMAIN -> stringResource(R.string.error_math_domain)
        CalcError.OVERFLOW -> stringResource(R.string.error_overflow)
        null -> null
    }

    val displayLength = state.formattedExpression.length
    val expressionFontSize = when {
        displayLength <= 10 -> 44.sp
        displayLength <= 15 -> 34.sp
        displayLength <= 22 -> 26.sp
        else -> 21.sp
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        color = Color.Transparent,
        border = BorderStroke(1.dp, taju.glassBorder),
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            taju.glassSurfaceElevated,
                            taju.glassSurface
                        )
                    )
                )
                .padding(horizontal = 18.dp, vertical = 14.dp)
        ) {
            // Top Status Row: Angle Mode Pill, Memory Pill, Copy Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (showAngleBadge) {
                        Surface(
                            onClick = onToggleAngleMode,
                            modifier = Modifier
                                .minimumInteractiveComponentSize()
                                .testTag("angle_mode_toggle"),
                            shape = RoundedCornerShape(10.dp),
                            color = taju.goldAccent.copy(alpha = 0.16f),
                            border = BorderStroke(1.dp, taju.goldBorder)
                        ) {
                            Text(
                                text = if (state.preferences.angleMode == AngleMode.DEG) "DEG" else "RAD",
                                color = taju.goldAccent,
                                fontFamily = OutfitFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    } else {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = taju.goldAccent.copy(alpha = 0.12f),
                            border = BorderStroke(1.dp, taju.goldBorder)
                        ) {
                            Text(
                                text = "TAJU",
                                color = taju.goldAccent,
                                fontFamily = OutfitFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                letterSpacing = 1.4.sp,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            )
                        }
                    }

                    AnimatedVisibility(
                        visible = state.hasMemory,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = taju.keyOperatorSurface.copy(alpha = 0.55f),
                            border = BorderStroke(1.dp, taju.keyOperatorBorder)
                        ) {
                            Text(
                                text = "M  ${state.formattedMemory}",
                                color = Color.White,
                                fontFamily = JetBrainsMonoFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 11.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            )
                        }
                    }
                }

                IconButton(
                    onClick = onCopyResult,
                    modifier = Modifier
                        .size(48.dp)
                        .testTag("copy_result_button")
                ) {
                    Icon(
                        imageVector = Icons.Rounded.ContentCopy,
                        contentDescription = "Copy result to clipboard",
                        tint = taju.goldAccent,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Previous expression line when evaluated
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(22.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                if (state.justEvaluated && state.lastEvaluatedExpression.isNotEmpty()) {
                    Text(
                        text = "${state.lastEvaluatedExpression} =",
                        color = taju.mutedText,
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Main Expression / Result Display
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(exprScrollState),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = state.formattedExpression,
                    color = if (state.justEvaluated) taju.goldBright else MaterialTheme.colorScheme.onSurface,
                    fontFamily = OutfitFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = expressionFontSize,
                    lineHeight = expressionFontSize * 1.15f,
                    textAlign = TextAlign.End,
                    maxLines = 1,
                    modifier = Modifier.testTag("calculator_display_text")
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Live Preview or Error Message
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                when {
                    errorText != null -> {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = taju.errorSurface
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.ErrorOutline,
                                    contentDescription = errorText,
                                    tint = taju.errorText,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = errorText,
                                    color = taju.errorText,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.testTag("calculator_error_text")
                                )
                            }
                        }
                    }

                    state.formattedPreview.isNotEmpty() -> {
                        Text(
                            text = "= ${state.formattedPreview}",
                            color = taju.goldAccent,
                            fontFamily = JetBrainsMonoFontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 20.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.testTag("calculator_preview_text")
                        )
                    }
                }
            }
        }
    }
}
