package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import com.example.data.localization.NivoStrings
import com.example.data.localization.toLocalizedDigits
import com.example.data.model.Goal
import com.example.ui.components.NivoButton
import com.example.ui.theme.CardBackground
import com.example.ui.theme.CardElevated
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
fun GoalsScreen(
    viewModel: NivoViewModel,
    onCreateGoalClick: () -> Unit,
    onEditGoalClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val goals by viewModel.goals.collectAsState()
    val language by viewModel.language.collectAsState()
    var goalToDelete by remember { mutableStateOf<Goal?>(null) }

    val dateFormat = remember { SimpleDateFormat("MMM d, yyyy", Locale.US) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .testTag("goals_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 14.dp)
        ) {
            // Header with title and "+ New Goal" action button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = NivoStrings.myGoals(language),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryText
                    )
                    Text(
                        text = "${goals.size.toLocalizedDigits(language)} ${if (language == AppLanguage.BANGLA) "টি লক্ষ্য" else "Active Goals"}",
                        fontSize = 13.sp,
                        color = SecondaryText
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(PrimaryRed)
                        .clickable { onCreateGoalClick() }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                        .testTag("header_add_goal_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = NivoStrings.newGoal(language),
                            tint = PrimaryText,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = NivoStrings.newGoal(language),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryText
                        )
                    }
                }
            }

            if (goals.isEmpty()) {
                GoalsEmptyState(
                    language = language,
                    onCreateGoalClick = onCreateGoalClick
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(goals, key = { it.id }) { goal ->
                        GoalCard(
                            goal = goal,
                            language = language,
                            dateFormat = dateFormat,
                            onSelect = { viewModel.selectGoal(goal.id) },
                            onEdit = { onEditGoalClick(goal.id) },
                            onDelete = { goalToDelete = goal }
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(70.dp))
                    }
                }
            }
        }

        // Delete Confirmation Dialog
        if (goalToDelete != null) {
            val target = goalToDelete!!
            AlertDialog(
                onDismissRequest = { goalToDelete = null },
                containerColor = CardBackground,
                title = {
                    Text(
                        text = NivoStrings.deleteGoalConfirmTitle(language),
                        color = PrimaryText,
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Text(
                        text = NivoStrings.deleteGoalConfirmMessage(language, target.title),
                        color = SecondaryText,
                        fontSize = 14.sp
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.deleteGoal(target.id)
                            goalToDelete = null
                        }
                    ) {
                        Text(
                            NivoStrings.deleteButton(language),
                            color = PrimaryRed,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                dismissButton = {
                    TextButton(onClick = { goalToDelete = null }) {
                        Text(NivoStrings.cancelButton(language), color = SecondaryText)
                    }
                }
            )
        }
    }
}

@Composable
private fun GoalCard(
    goal: Goal,
    language: AppLanguage,
    dateFormat: SimpleDateFormat,
    onSelect: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isSelected = goal.isSelected

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) CardElevated else CardBackground)
            .border(
                width = if (isSelected) 1.5.dp else 1.dp,
                color = if (isSelected) PrimaryRed else OutlineGrey,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onSelect() }
            .padding(16.dp)
            .testTag("goal_item_card_${goal.id}")
    ) {
        Column {
            // Header Row: Active indicator + Title + Edit & Delete actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (isSelected) Icons.Filled.RadioButtonChecked else Icons.Filled.RadioButtonUnchecked,
                        contentDescription = if (isSelected) "Active Goal" else "Select Goal",
                        tint = if (isSelected) PrimaryRed else SecondaryText,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = goal.title,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryText,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Spacer(modifier = Modifier.height(3.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Tracking Mode Badge
                            val isAuto = goal.trackingMode == com.example.data.model.TrackingMode.AUTOMATIC
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (isAuto) PrimaryRed.copy(alpha = 0.15f) else OutlineGrey.copy(alpha = 0.2f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = if (isAuto) NivoStrings.autoModeShort(language) else NivoStrings.manualModeShort(language),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (isAuto) PrimaryRed else PrimaryText
                                )
                            }
                            if (isSelected) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "• ${NivoStrings.activeOnHome(language)}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = PrimaryRed
                                )
                            }
                        }
                    }
                }

                // Edit Button
                IconButton(
                    onClick = onEdit,
                    modifier = Modifier.size(32.dp).testTag("edit_goal_${goal.id}")
                ) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = NivoStrings.editGoal(language),
                        tint = SecondaryText,
                        modifier = Modifier.size(17.dp)
                    )
                }

                // Delete Button
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(32.dp).testTag("delete_goal_${goal.id}")
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = NivoStrings.deleteButton(language),
                        tint = SecondaryText,
                        modifier = Modifier.size(17.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Reason if available
            if (goal.reason.isNotBlank()) {
                Text(
                    text = "\"${goal.reason}\"",
                    fontSize = 12.sp,
                    color = SecondaryText,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Single Progress Overview (Mode-aware)
            val effectiveCompleted = goal.getEffectiveCompletedDays()
            val effectiveRemaining = goal.getEffectiveRemainingDays()
            val effectiveProgressFraction = goal.getEffectiveProgressFraction()

            // Single Progress Bar
            LinearProgressIndicator(
                progress = { effectiveProgressFraction },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = PrimaryRed,
                trackColor = OutlineGrey.copy(alpha = 0.5f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Single Metrics Row: Duration, Completed, Remaining, Target Date
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                GoalMetricItem(
                    label = NivoStrings.durationLabel(language),
                    value = NivoStrings.daysUnit(language, goal.durationDays)
                )
                GoalMetricItem(
                    label = NivoStrings.completedLabel(language),
                    value = "${effectiveCompleted.toLocalizedDigits(language)}/${goal.durationDays.toLocalizedDigits(language)}"
                )
                GoalMetricItem(
                    label = NivoStrings.remainingLabel(language),
                    value = "${effectiveRemaining.toLocalizedDigits(language)} ${if (language == AppLanguage.BANGLA) "দিন" else "d"}"
                )
                GoalMetricItem(
                    label = NivoStrings.expectedCompletionLabel(language),
                    value = dateFormat.format(Date(goal.expectedCompletionDateMillis))
                )
            }
        }
    }
}

@Composable
private fun GoalMetricItem(
    label: String,
    value: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryText
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 10.sp,
            color = SecondaryText
        )
    }
}

@Composable
private fun GoalsEmptyState(
    language: AppLanguage,
    onCreateGoalClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 60.dp, horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .clip(CircleShape)
                .background(CardBackground)
                .border(1.dp, OutlineGrey, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Flag,
                contentDescription = null,
                tint = PrimaryRed,
                modifier = Modifier.size(32.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = NivoStrings.noActiveGoal(language),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryText
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = NivoStrings.noActiveGoalDesc(language),
            fontSize = 13.sp,
            color = SecondaryText,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        NivoButton(
            text = NivoStrings.createFirstGoal(language),
            onClick = onCreateGoalClick,
            icon = Icons.Filled.Add,
            testTag = "empty_create_goal_button",
            modifier = Modifier.fillMaxWidth(0.85f)
        )
    }
}
