package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.OfflineBolt
import androidx.compose.material.icons.outlined.Science
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.LocalTajuColors
import com.example.ui.theme.OutfitFontFamily

@Composable
fun AboutScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onBack)

    val taju = LocalTajuColors.current

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
            // Top Bar with Back Arrow
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(48.dp)
                        .testTag("about_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Back to Settings",
                        tint = taju.goldAccent
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = stringResource(R.string.about_title),
                    style = MaterialTheme.typography.headlineMedium
                )
            }

            // Brand Crest Hero Card
            Surface(
                shape = RoundedCornerShape(26.dp),
                color = taju.glassSurfaceElevated,
                border = BorderStroke(1.dp, taju.goldBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_taju_crest_1795670479652),
                        contentDescription = stringResource(R.string.brand_title),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(96.dp)
                            .clip(CircleShape)
                            .border(2.dp, taju.goldAccent, CircleShape)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = stringResource(R.string.brand_title),
                        fontFamily = OutfitFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        letterSpacing = 2.2.sp,
                        color = taju.goldBright
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Version 1.0.0 • Native Android Edition",
                        style = MaterialTheme.typography.bodyMedium,
                        color = taju.mutedText
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Crafted for fast, reliable everyday and scientific calculations with a calm navy-and-gold glass interface and full offline privacy.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Capabilities Grid
            AboutHighlightCard(
                icon = Icons.Outlined.Calculate,
                title = "Deterministic Expression Parser",
                description = "Evaluates nested brackets, implicit multiplication, and mathematical operator precedence safely without dynamic script evaluation."
            )
            AboutHighlightCard(
                icon = Icons.Outlined.Science,
                title = "Full Scientific Suite",
                description = "Supports DEG/RAD trigonometry, inverse trig functions, logarithms, factorials, powers, roots, π, e, and persistent memory registers."
            )
            AboutHighlightCard(
                icon = Icons.Outlined.OfflineBolt,
                title = "100% Offline & Lightweight",
                description = "Instant zero-latency arithmetic and local Room history storage—no internet connection or account required."
            )
            AboutHighlightCard(
                icon = Icons.Outlined.Lock,
                title = "Private Local Storage",
                description = "Your calculation history, memory values, and theme preferences remain stored exclusively on your Android device."
            )

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun AboutHighlightCard(
    icon: ImageVector,
    title: String,
    description: String
) {
    val taju = LocalTajuColors.current
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = taju.glassSurface,
        border = BorderStroke(1.dp, taju.glassBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = taju.goldAccent.copy(alpha = 0.14f),
                modifier = Modifier.size(42.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = taju.goldAccent,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = taju.mutedText
                )
            }
        }
    }
}
