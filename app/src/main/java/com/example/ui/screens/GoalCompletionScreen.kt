package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.localization.AppLanguage
import com.example.data.localization.NivoStrings
import com.example.data.localization.toLocalizedDigits
import com.example.ui.components.CircularProgressRing
import com.example.ui.components.NivoButton
import com.example.ui.theme.CardBackground
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.OutlineGrey
import com.example.ui.theme.PrimaryRed
import com.example.ui.theme.PrimaryText
import com.example.ui.theme.SecondaryText
import com.example.ui.viewmodel.NivoViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun GoalCompletionScreen(
    goalId: String,
    viewModel: NivoViewModel,
    onReturnHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler {
        onReturnHome()
    }

    val goals by viewModel.goals.collectAsState()
    val language by viewModel.language.collectAsState()
    val goal = goals.find { it.id == goalId } ?: goals.firstOrNull()

    val locale = if (language == AppLanguage.BANGLA) Locale("bn", "BD") else Locale.US
    val dateFormat = remember(locale) { SimpleDateFormat("MMMM d, yyyy", locale) }

    // Subtle celebration scale animation
    val scaleAnim = remember { Animatable(0.85f) }
    LaunchedEffect(Unit) {
        scaleAnim.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 650, easing = FastOutSlowInEasing)
        )
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .verticalScroll(scrollState)
            .padding(horizontal = 24.dp, vertical = 20.dp)
            .testTag("goal_completion_screen"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Trophy badge
        Box(
            modifier = Modifier
                .scale(scaleAnim.value)
                .size(76.dp)
                .clip(CircleShape)
                .background(PrimaryRed.copy(alpha = 0.15f))
                .border(2.dp, PrimaryRed, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.EmojiEvents,
                contentDescription = "Trophy",
                tint = PrimaryRed,
                modifier = Modifier.size(40.dp)
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // "GOAL COMPLETED" / "লক্ষ্য অর্জিত হয়েছে!"
        Text(
            text = NivoStrings.goalCompletedTitle(language),
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            color = PrimaryRed,
            letterSpacing = 2.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.testTag("goal_completed_title")
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = goal?.title ?: "Milestone",
            fontSize = 17.sp,
            fontWeight = FontWeight.SemiBold,
            color = PrimaryText,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Completed Circular Progress Ring (100%)
        CircularProgressRing(
            progressFraction = 1f,
            completedDays = goal?.getEffectiveCompletedDays() ?: goal?.durationDays ?: 30,
            remainingDays = 0,
            totalDays = goal?.durationDays ?: 30,
            size = 210.dp,
            strokeWidth = 16.dp,
            language = language
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Encouraging Message Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(CardBackground)
                .border(1.dp, OutlineGrey, RoundedCornerShape(16.dp))
                .padding(18.dp)
        ) {
            Text(
                text = NivoStrings.goalCompletedMessage(language),
                fontSize = 14.sp,
                color = PrimaryText,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Stats Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(CardBackground)
                .border(1.dp, OutlineGrey, RoundedCornerShape(16.dp))
                .padding(18.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                StatRow(
                    label = if (goal?.trackingMode == com.example.data.model.TrackingMode.AUTOMATIC) NivoStrings.durationLabel(language) else NivoStrings.totalCheckInsLabel(language),
                    value = NivoStrings.daysUnit(language, goal?.getEffectiveCompletedDays() ?: goal?.durationDays ?: 0)
                )
                StatRow(
                    label = NivoStrings.startDateLabel(language),
                    value = goal?.startDateMillis?.let { dateFormat.format(Date(it)) } ?: "—"
                )
                StatRow(
                    label = NivoStrings.completionDateLabel(language),
                    value = (goal?.completedDateMillis ?: System.currentTimeMillis()).let {
                        dateFormat.format(Date(it))
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Button to return to Home screen
        NivoButton(
            text = NivoStrings.returnToHomeButton(language),
            onClick = onReturnHome,
            icon = Icons.Filled.Home,
            testTag = "return_to_home_button",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
private fun StatRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            color = SecondaryText
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = PrimaryText
        )
    }
}
