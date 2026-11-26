package com.example.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.LocalTajuColors
import com.example.ui.theme.OutfitFontFamily
import com.example.ui.theme.TajuGoldBright
import com.example.ui.theme.TajuGoldDeep
import com.example.ui.theme.TajuGoldPrimary
import com.example.ui.theme.TajuMidnightBg
import com.example.ui.theme.TajuWhiteText

enum class CalcButtonRole {
    DIGIT,
    OPERATOR,
    SCIENTIFIC,
    ACTION,
    EQUALS,
    MEMORY
}

@Composable
fun CalculatorButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    role: CalcButtonRole = CalcButtonRole.DIGIT,
    testTag: String = "btn_$label",
    contentDesc: String = label,
    hapticEnabled: Boolean = true,
    isActive: Boolean = false,
    fontSize: TextUnit = when (role) {
        CalcButtonRole.DIGIT -> 24.sp
        CalcButtonRole.OPERATOR -> 25.sp
        CalcButtonRole.EQUALS -> 28.sp
        CalcButtonRole.ACTION -> 18.sp
        CalcButtonRole.SCIENTIFIC -> 15.sp
        CalcButtonRole.MEMORY -> 13.sp
    },
    shape: Shape = RoundedCornerShape(20.dp)
) {
    val taju = LocalTajuColors.current
    val haptic = LocalHapticFeedback.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.93f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        ),
        label = "button_press_scale"
    )

    val backgroundBrush = when (role) {
        CalcButtonRole.EQUALS -> Brush.linearGradient(
            colors = listOf(TajuGoldBright, TajuGoldPrimary, TajuGoldDeep)
        )
        CalcButtonRole.OPERATOR -> Brush.verticalGradient(
            colors = listOf(
                taju.keyOperatorSurface.copy(alpha = 0.95f),
                taju.keyOperatorSurface.copy(alpha = 0.78f)
            )
        )
        CalcButtonRole.ACTION -> Brush.verticalGradient(
            colors = listOf(
                taju.keyActionSurface,
                taju.keyActionSurface.copy(alpha = 0.85f)
            )
        )
        CalcButtonRole.SCIENTIFIC -> if (isActive) {
            Brush.verticalGradient(
                colors = listOf(
                    taju.goldAccent.copy(alpha = 0.30f),
                    taju.goldAccent.copy(alpha = 0.16f)
                )
            )
        } else {
            Brush.verticalGradient(
                colors = listOf(
                    taju.keyFunctionSurface,
                    taju.keyFunctionSurface.copy(alpha = 0.85f)
                )
            )
        }
        CalcButtonRole.MEMORY -> if (isActive) {
            Brush.verticalGradient(
                colors = listOf(
                    taju.goldAccent.copy(alpha = 0.25f),
                    taju.goldAccent.copy(alpha = 0.12f)
                )
            )
        } else {
            Brush.verticalGradient(
                colors = listOf(
                    taju.glassSurfaceElevated,
                    taju.glassSurface
                )
            )
        }
        CalcButtonRole.DIGIT -> Brush.verticalGradient(
            colors = listOf(
                taju.keyDigitSurface,
                taju.keyDigitSurface.copy(alpha = 0.88f)
            )
        )
    }

    val borderColor = when {
        role == CalcButtonRole.EQUALS -> TajuGoldBright.copy(alpha = 0.75f)
        isActive -> taju.goldAccent.copy(alpha = 0.75f)
        role == CalcButtonRole.OPERATOR -> taju.keyOperatorBorder
        role == CalcButtonRole.ACTION -> taju.goldBorder
        else -> taju.keyDigitBorder
    }

    val textColor = when (role) {
        CalcButtonRole.EQUALS -> TajuMidnightBg
        CalcButtonRole.OPERATOR -> TajuWhiteText
        CalcButtonRole.ACTION -> taju.keyActionText
        CalcButtonRole.SCIENTIFIC -> if (isActive) taju.goldBright else MaterialTheme.colorScheme.onSurface
        CalcButtonRole.MEMORY -> if (isActive) taju.goldAccent else taju.mutedText
        CalcButtonRole.DIGIT -> MaterialTheme.colorScheme.onSurface
    }

    Surface(
        onClick = {
            if (hapticEnabled) {
                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
            }
            onClick()
        },
        modifier = modifier
            .defaultMinSize(minWidth = 48.dp, minHeight = 48.dp)
            .minimumInteractiveComponentSize()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .testTag(testTag)
            .semantics { contentDescription = contentDesc },
        shape = shape,
        color = Color.Transparent,
        border = BorderStroke(1.dp, borderColor),
        shadowElevation = if (role == CalcButtonRole.EQUALS) 6.dp else 1.dp,
        interactionSource = interactionSource
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(shape)
                .background(backgroundBrush),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                color = textColor,
                fontFamily = OutfitFontFamily,
                fontSize = fontSize,
                fontWeight = when (role) {
                    CalcButtonRole.EQUALS -> FontWeight.Bold
                    CalcButtonRole.OPERATOR, CalcButtonRole.ACTION -> FontWeight.SemiBold
                    CalcButtonRole.SCIENTIFIC, CalcButtonRole.MEMORY -> FontWeight.Medium
                    CalcButtonRole.DIGIT -> FontWeight.Medium
                },
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
