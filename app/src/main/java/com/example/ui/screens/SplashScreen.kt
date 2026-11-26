package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.OutfitFontFamily
import com.example.ui.theme.PlusJakartaSansFontFamily
import com.example.ui.theme.TajuGoldBright
import com.example.ui.theme.TajuGoldPrimary
import com.example.ui.theme.TajuMidnightBg
import com.example.ui.theme.TajuSlateMuted
import com.example.ui.theme.TajuTealSecondary
import com.example.ui.theme.TajuWhiteText
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onFinished: () -> Unit
) {
    val scaleAnim = remember { Animatable(0.84f) }
    val alphaAnim = remember { Animatable(0f) }

    val infiniteTransition = rememberInfiniteTransition(label = "splash_ring")
    val ringRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ring_rotation"
    )

    LaunchedEffect(Unit) {
        alphaAnim.animateTo(1f, animationSpec = tween(550, easing = FastOutSlowInEasing))
        scaleAnim.animateTo(1f, animationSpec = tween(650, easing = FastOutSlowInEasing))
        delay(850)
        onFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        TajuTealSecondary.copy(alpha = 0.65f),
                        TajuMidnightBg
                    ),
                    radius = 1200f
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(32.dp)
                .alpha(alphaAnim.value)
                .scale(scaleAnim.value)
        ) {
            Box(
                modifier = Modifier.size(164.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawArc(
                        brush = Brush.sweepGradient(
                            colors = listOf(
                                Color.Transparent,
                                TajuGoldPrimary,
                                TajuGoldBright,
                                Color.Transparent
                            )
                        ),
                        startAngle = ringRotation,
                        sweepAngle = 220f,
                        useCenter = false,
                        style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                    )
                    drawCircle(
                        color = TajuGoldPrimary.copy(alpha = 0.16f),
                        radius = size.minDimension / 2.2f,
                        center = Offset(size.width / 2f, size.height / 2f),
                        style = Stroke(width = 1.dp.toPx())
                    )
                }

                Image(
                    painter = painterResource(id = R.drawable.img_taju_crest_1795670479652),
                    contentDescription = stringResource(R.string.brand_title),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(126.dp)
                        .clip(CircleShape)
                        .border(1.5.dp, TajuGoldPrimary.copy(alpha = 0.65f), CircleShape)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = stringResource(R.string.brand_title),
                color = TajuWhiteText,
                fontFamily = OutfitFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                letterSpacing = 3.2.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.brand_subtitle),
                color = TajuGoldBright,
                fontFamily = PlusJakartaSansFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                letterSpacing = 2.8.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Offline Precision Engine",
                color = TajuSlateMuted,
                fontFamily = PlusJakartaSansFontFamily,
                fontSize = 12.sp
            )
        }
    }
}
