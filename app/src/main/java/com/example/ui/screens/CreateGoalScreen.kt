package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.localization.AppLanguage
import com.example.data.localization.NivoStrings
import com.example.data.localization.toLocalizedDigits
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
fun CreateGoalScreen(
    viewModel: NivoViewModel,
    goalIdToEdit: String? = null,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler {
        onNavigateBack()
    }

    val language by viewModel.language.collectAsState()
    val goals by viewModel.goals.collectAsState()
    val goalToEdit = remember(goalIdToEdit, goals) {
        goals.find { it.id == goalIdToEdit }
    }

    val isEditMode = goalToEdit != null

    var title by remember(goalToEdit) { mutableStateOf(goalToEdit?.title ?: "") }
    var durationInput by remember(goalToEdit) {
        mutableStateOf((goalToEdit?.durationDays ?: 30).toString())
    }
    var startDateMillis by remember(goalToEdit) {
        mutableLongStateOf(goalToEdit?.startDateMillis ?: System.currentTimeMillis())
    }
    var reason by remember(goalToEdit) { mutableStateOf(goalToEdit?.reason ?: "") }

    var titleError by remember { mutableStateOf<String?>(null) }
    var durationError by remember { mutableStateOf<String?>(null) }

    val dateFormat = remember { SimpleDateFormat("MMMM d, yyyy", Locale.US) }

    // Parse current duration
    val parsedDuration = durationInput.toIntOrNull()
    val isValidDuration = parsedDuration != null && parsedDuration in 2..365

    // Calculate expected completion date
    val expectedCompletionMillis = remember(startDateMillis, parsedDuration) {
        if (isValidDuration && parsedDuration != null) {
            startDateMillis + (parsedDuration.toLong() * 24L * 60L * 60L * 1000L)
        } else null
    }

    val sampleDurations = listOf(2, 3, 7, 15, 30, 90, 180, 365)
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .testTag("create_goal_screen")
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onNavigateBack,
                modifier = Modifier.testTag("create_goal_back_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = NivoStrings.cancelButton(language),
                    tint = PrimaryText
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isEditMode) NivoStrings.editGoal(language) else NivoStrings.newGoal(language),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryText
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 1. Goal Title Input
        Text(
            text = NivoStrings.goalTitleLabel(language),
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = PrimaryText
        )
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = title,
            onValueChange = {
                title = it
                if (it.isNotBlank()) titleError = null
            },
            placeholder = {
                Text(NivoStrings.goalTitlePlaceholder(language), color = SecondaryText)
            },
            isError = titleError != null,
            supportingText = {
                if (titleError != null) {
                    Text(text = titleError!!, color = PrimaryRed, fontSize = 12.sp)
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Next
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = PrimaryText,
                unfocusedTextColor = PrimaryText,
                focusedContainerColor = CardBackground,
                unfocusedContainerColor = CardBackground,
                focusedBorderColor = PrimaryRed,
                unfocusedBorderColor = OutlineGrey,
                cursorColor = PrimaryRed
            ),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("goal_title_input")
        )

        Spacer(modifier = Modifier.height(18.dp))

        // 2. Goal Duration (2 to 365 Days)
        Text(
            text = NivoStrings.goalDurationLabel(language),
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = PrimaryText
        )
        Spacer(modifier = Modifier.height(6.dp))

        // Stepper + Direct Number Input
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Decrement Button [-]
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(CardBackground)
                    .border(1.dp, OutlineGrey, RoundedCornerShape(12.dp))
                    .clickable {
                        val current = durationInput.toIntOrNull() ?: 30
                        val newVal = (current - 1).coerceIn(2, 365)
                        durationInput = newVal.toString()
                        durationError = null
                    }
                    .testTag("decrement_duration_button"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Remove,
                    contentDescription = "Decrement",
                    tint = PrimaryText
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Direct Numeric Input
            OutlinedTextField(
                value = durationInput,
                onValueChange = { input ->
                    // Allow only digits
                    val filtered = input.filter { it.isDigit() }
                    durationInput = filtered
                    val num = filtered.toIntOrNull()
                    durationError = when {
                        filtered.isEmpty() -> NivoStrings.errorDurationInvalid(language)
                        num == null -> NivoStrings.errorDurationInvalid(language)
                        num < 2 -> NivoStrings.errorDurationTooSmall(language)
                        num > 365 -> NivoStrings.errorDurationTooLarge(language)
                        else -> null
                    }
                },
                placeholder = { Text("30", color = SecondaryText) },
                singleLine = true,
                isError = durationError != null,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                textStyle = androidx.compose.ui.text.TextStyle(
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryText
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = PrimaryText,
                    unfocusedTextColor = PrimaryText,
                    focusedContainerColor = CardBackground,
                    unfocusedContainerColor = CardBackground,
                    focusedBorderColor = PrimaryRed,
                    unfocusedBorderColor = OutlineGrey,
                    cursorColor = PrimaryRed
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .weight(1f)
                    .testTag("duration_number_input")
            )

            Spacer(modifier = Modifier.width(10.dp))

            // Increment Button [+]
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(CardBackground)
                    .border(1.dp, OutlineGrey, RoundedCornerShape(12.dp))
                    .clickable {
                        val current = durationInput.toIntOrNull() ?: 30
                        val newVal = (current + 1).coerceIn(2, 365)
                        durationInput = newVal.toString()
                        durationError = null
                    }
                    .testTag("increment_duration_button"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Increment",
                    tint = PrimaryText
                )
            }
        }

        if (durationError != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = durationError!!,
                color = PrimaryRed,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Quick Preset Chips (e.g. 2, 3, 7, 15, 30, 90, 180, 365 days)
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(sampleDurations) { days ->
                val isSelected = parsedDuration == days
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isSelected) PrimaryRed else CardBackground)
                        .border(
                            1.dp,
                            if (isSelected) PrimaryRed else OutlineGrey,
                            RoundedCornerShape(10.dp)
                        )
                        .clickable {
                            durationInput = days.toString()
                            durationError = null
                        }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .testTag("preset_duration_${days}")
                ) {
                    Text(
                        text = "${days.toLocalizedDigits(language)} ${if (language == AppLanguage.BANGLA) "দিন" else "d"}",
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) PrimaryText else SecondaryText
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // 3. Expected Completion Date
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(CardBackground)
                .border(1.dp, OutlineGrey, RoundedCornerShape(14.dp))
                .padding(14.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.CalendarToday,
                        contentDescription = null,
                        tint = PrimaryRed,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${NivoStrings.startDateLabel(language)}: ${dateFormat.format(Date(startDateMillis))}",
                        fontSize = 13.sp,
                        color = SecondaryText
                    )
                }

                if (expectedCompletionMillis != null) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Flag,
                            contentDescription = null,
                            tint = PrimaryRed,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${NivoStrings.expectedCompletionLabel(language)}: ${dateFormat.format(Date(expectedCompletionMillis))}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = PrimaryText
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // 4. Optional Personal Reason
        Text(
            text = NivoStrings.personalReasonLabel(language),
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = PrimaryText
        )
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = reason,
            onValueChange = { reason = it },
            placeholder = {
                Text(NivoStrings.personalReasonPlaceholder(language), color = SecondaryText)
            },
            minLines = 3,
            maxLines = 4,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = PrimaryText,
                unfocusedTextColor = PrimaryText,
                focusedContainerColor = CardBackground,
                unfocusedContainerColor = CardBackground,
                focusedBorderColor = PrimaryRed,
                unfocusedBorderColor = OutlineGrey,
                cursorColor = PrimaryRed
            ),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("goal_reason_input")
        )

        Spacer(modifier = Modifier.height(28.dp))

        // Save / Update Button
        NivoButton(
            text = if (isEditMode) NivoStrings.updateGoalButton(language) else NivoStrings.saveGoalButton(language),
            onClick = {
                var hasError = false
                if (title.isBlank()) {
                    titleError = NivoStrings.errorTitleRequired(language)
                    hasError = true
                }
                val duration = durationInput.toIntOrNull()
                if (duration == null || duration !in 2..365) {
                    durationError = when {
                        duration == null -> NivoStrings.errorDurationInvalid(language)
                        duration < 2 -> NivoStrings.errorDurationTooSmall(language)
                        else -> NivoStrings.errorDurationTooLarge(language)
                    }
                    hasError = true
                }

                if (!hasError && duration != null) {
                    if (isEditMode && goalToEdit != null) {
                        viewModel.updateGoal(
                            id = goalToEdit.id,
                            title = title,
                            durationDays = duration,
                            startDateMillis = startDateMillis,
                            reason = reason
                        ) {
                            onNavigateBack()
                        }
                    } else {
                        viewModel.createGoal(
                            title = title,
                            durationDays = duration,
                            startDateMillis = startDateMillis,
                            reason = reason
                        ) {
                            onNavigateBack()
                        }
                    }
                }
            },
            icon = Icons.Filled.Check,
            testTag = "save_goal_button",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Cancel Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(CardBackground)
                .border(1.dp, OutlineGrey, RoundedCornerShape(14.dp))
                .clickable { onNavigateBack() }
                .testTag("cancel_goal_button"),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = NivoStrings.cancelButton(language),
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = SecondaryText
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
