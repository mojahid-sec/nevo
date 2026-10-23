package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.localization.AppLanguage
import com.example.data.localization.MotivationalQuotes
import com.example.data.localization.NivoStrings
import com.example.data.localization.toLocalizedDigits
import com.example.data.model.Goal
import com.example.ui.components.CircularProgressRing
import com.example.ui.components.MotivationalCard
import com.example.ui.components.NivoButton
import com.example.ui.components.NivoLogo
import com.example.ui.theme.CardBackground
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.OutlineGrey
import com.example.ui.theme.PrimaryRed
import com.example.ui.theme.PrimaryText
import com.example.ui.theme.SecondaryText
import com.example.ui.viewmodel.NivoViewModel

@Composable
fun HomeScreen(
    viewModel: NivoViewModel,
    onCreateGoalClick: () -> Unit,
    onNavigateToGoals: () -> Unit,
    onGoalCompleted: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedGoal by viewModel.selectedGoal.collectAsState()
    val allGoals by viewModel.goals.collectAsState()
    val userName by viewModel.userName.collectAsState()
    val language by viewModel.language.collectAsState()
    val completedEvent by viewModel.goalCompletedEvent.collectAsState()
    val currentTimeMillis by viewModel.currentTimeMillis.collectAsState()

    var showGoalSwitchDialog by remember { mutableStateOf(false) }

    // Trigger navigation on goal completion
    LaunchedEffect(completedEvent) {
        completedEvent?.let { goal ->
            onGoalCompleted(goal.id)
            viewModel.clearGoalCompletedEvent()
        }
    }

    // Exact 24-hour countdown calculation
    val isEligible = viewModel.isGoalEligibleForCheckIn(selectedGoal)
    val (hours, minutes, seconds) = viewModel.getCountdownForGoal(selectedGoal)

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Brand Identity Header
        NivoLogo(
            iconSize = 44.dp,
            showTagline = true,
            language = language,
            modifier = Modifier.padding(bottom = 6.dp)
        )

        // 1. Friendly Greeting (Language-aware, NO automatic time-of-day greeting)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = NivoStrings.greeting(language, userName),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryText,
                    modifier = Modifier.testTag("home_greeting_text")
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = NivoStrings.homeSubtitle(language),
                    fontSize = 13.sp,
                    color = SecondaryText
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        if (selectedGoal == null) {
            // Clean Empty State (no fake stats)
            HomeEmptyState(
                language = language,
                onCreateGoalClick = onCreateGoalClick
            )
        } else {
            val goal = selectedGoal!!

            // 2. Selected Goal Title Pill (With quick switcher if multiple goals exist)
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(CardBackground)
                    .border(1.dp, OutlineGrey, RoundedCornerShape(20.dp))
                    .clickable {
                        if (allGoals.size > 1) {
                            showGoalSwitchDialog = true
                        } else {
                            onNavigateToGoals()
                        }
                    }
                    .padding(horizontal = 14.dp, vertical = 7.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Flag,
                    contentDescription = null,
                    tint = PrimaryRed,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = goal.title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = PrimaryText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (allGoals.size > 1) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.Filled.SwapHoriz,
                        contentDescription = NivoStrings.switchGoalTitle(language),
                        tint = PrimaryRed,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            // Quick Goal Switcher Dialog
            if (showGoalSwitchDialog) {
                AlertDialog(
                    onDismissRequest = { showGoalSwitchDialog = false },
                    containerColor = CardBackground,
                    title = {
                        Text(
                            text = NivoStrings.switchGoalTitle(language),
                            color = PrimaryText,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    },
                    text = {
                        Column {
                            allGoals.forEach { g ->
                                val isCur = g.id == goal.id
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (isCur) OutlineGrey.copy(alpha = 0.2f) else CardBackground)
                                        .clickable {
                                            viewModel.selectGoal(g.id)
                                            showGoalSwitchDialog = false
                                        }
                                        .padding(horizontal = 12.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = if (isCur) Icons.Filled.RadioButtonChecked else Icons.Filled.RadioButtonUnchecked,
                                        contentDescription = null,
                                        tint = if (isCur) PrimaryRed else SecondaryText,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = g.title,
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 14.sp,
                                            color = if (isCur) PrimaryRed else PrimaryText
                                        )
                                        Text(
                                            text = "${NivoStrings.daysUnit(language, g.durationDays)} • ${NivoStrings.daysRemaining(language, g.remainingDays)}",
                                            fontSize = 12.sp,
                                            color = SecondaryText
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                            }
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = { showGoalSwitchDialog = false }) {
                            Text(NivoStrings.doneButton(language), color = PrimaryRed, fontWeight = FontWeight.Bold)
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 3 & 4. Circular Progress Indicator with Completed & Remaining Days
            CircularProgressRing(
                progressFraction = goal.progressFraction,
                completedDays = goal.completedDaysCount,
                remainingDays = goal.remainingDays,
                totalDays = goal.durationDays,
                size = 230.dp,
                strokeWidth = 16.dp,
                language = language
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 5. Prominent, full-width red Check In Button (Respects EXACT 24-HOUR rule)
            if (!isEligible) {
                NivoButton(
                    text = NivoStrings.checkedInForToday(language),
                    onClick = { /* Ineligible until 24 hours have passed */ },
                    enabled = false,
                    icon = Icons.Filled.Check,
                    testTag = "check_in_button",
                    containerColor = CardBackground,
                    contentColor = SecondaryText
                )
            } else {
                NivoButton(
                    text = NivoStrings.checkInButton(language),
                    onClick = {
                        viewModel.checkInSelectedGoal { completedGoal ->
                            onGoalCompleted(completedGoal.id)
                        }
                    },
                    enabled = true,
                    icon = Icons.Filled.Check,
                    testTag = "check_in_button",
                    containerColor = PrimaryRed,
                    contentColor = PrimaryText
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 6. Next Check-In Countdown (Exact 24-Hour Timer)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(CardBackground.copy(alpha = 0.7f))
                    .border(1.dp, OutlineGrey.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                    .padding(vertical = 12.dp, horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Schedule,
                    contentDescription = null,
                    tint = if (!isEligible) PrimaryRed else SecondaryText,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                val countdownText = if (!isEligible) {
                    NivoStrings.nextCheckInIn(language, hours, minutes, seconds)
                } else {
                    NivoStrings.checkInReady(language)
                }
                Text(
                    text = countdownText,
                    fontSize = 12.sp,
                    color = if (!isEligible) PrimaryText else PrimaryRed,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 7. Motivational Message (from the 50+ unique quote repository)
            val (quoteText, quoteAuthor) = remember(goal.id, goal.completedDaysCount, language) {
                MotivationalQuotes.getQuoteForGoal(goal.id, goal.completedDaysCount, language)
            }
            MotivationalCard(
                quote = quoteText,
                author = quoteAuthor
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun HomeEmptyState(
    language: AppLanguage,
    onCreateGoalClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp, horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(76.dp)
                .clip(CircleShape)
                .background(CardBackground)
                .border(1.dp, OutlineGrey, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Flag,
                contentDescription = null,
                tint = PrimaryRed,
                modifier = Modifier.size(36.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = NivoStrings.noActiveGoal(language),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryText,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = NivoStrings.noActiveGoalDesc(language),
            fontSize = 14.sp,
            color = SecondaryText,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(26.dp))

        NivoButton(
            text = NivoStrings.createFirstGoal(language),
            onClick = onCreateGoalClick,
            icon = Icons.Filled.Add,
            testTag = "create_first_goal_button",
            modifier = Modifier.fillMaxWidth()
        )
    }
}
