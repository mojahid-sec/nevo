package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.localization.AppLanguage
import com.example.data.localization.NivoStrings
import com.example.data.localization.toLocalizedDigits
import com.example.ui.theme.DarkRed
import com.example.ui.theme.OutlineGrey
import com.example.ui.theme.PrimaryRed
import com.example.ui.theme.PrimaryText
import com.example.ui.theme.SecondaryText

@Composable
fun CircularProgressRing(
    progressFraction: Float,
    completedDays: Int,
    remainingDays: Int,
    totalDays: Int,
    modifier: Modifier = Modifier,
    size: Dp = 230.dp,
    strokeWidth: Dp = 16.dp,
    language: AppLanguage = AppLanguage.ENGLISH
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progressFraction.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 900, easing = FastOutSlowInEasing),
        label = "progress_animation"
    )

    val progressPercent = (animatedProgress * 100).toInt()
    val trackColor = OutlineGrey.copy(alpha = 0.5f)
    val darkRedColor = DarkRed
    val primaryRedColor = PrimaryRed

    Box(
        modifier = modifier
            .size(size)
            .testTag("circular_progress_ring"),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val strokePx = strokeWidth.toPx()
            val arcSize = Size(this.size.width - strokePx, this.size.height - strokePx)
            val topLeft = Offset(strokePx / 2f, strokePx / 2f)

            // Background Track
            drawArc(
                color = trackColor,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokePx, cap = StrokeCap.Round)
            )

            // Animated Foreground Arc with Theme Gradient
            if (animatedProgress > 0.005f) {
                drawArc(
                    brush = Brush.sweepGradient(
                        colors = listOf(darkRedColor, primaryRedColor, darkRedColor)
                    ),
                    startAngle = -90f,
                    sweepAngle = 360f * animatedProgress,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokePx, cap = StrokeCap.Round)
                )
            }
        }

        // Inside Content: Percentage and Days Breakdown (Localized)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${progressPercent.toLocalizedDigits(language)}%",
                fontSize = 42.sp,
                fontWeight = FontWeight.ExtraBold,
                color = PrimaryText,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = NivoStrings.ofDays(language, completedDays, totalDays),
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = PrimaryRed,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = NivoStrings.daysRemaining(language, remainingDays),
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = SecondaryText,
                textAlign = TextAlign.Center
            )
        }
    }
}
