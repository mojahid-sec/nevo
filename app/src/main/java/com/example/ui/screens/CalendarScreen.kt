package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.localization.AppLanguage
import com.example.data.localization.NivoStrings
import com.example.data.localization.toLocalizedDigits
import com.example.data.model.CheckInRecord
import com.example.ui.theme.CardBackground
import com.example.ui.theme.CardElevated
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.OutlineGrey
import com.example.ui.theme.PrimaryRed
import com.example.ui.theme.PrimaryText
import com.example.ui.theme.SecondaryText
import com.example.ui.theme.SubtleGrey
import com.example.ui.viewmodel.NivoViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun CalendarScreen(
    viewModel: NivoViewModel,
    modifier: Modifier = Modifier
) {
    val checkIns by viewModel.checkIns.collectAsState()
    val selectedGoal by viewModel.selectedGoal.collectAsState()
    val language by viewModel.language.collectAsState()

    var displayedCal by remember { mutableStateOf(Calendar.getInstance()) }
    var selectedDayKey by remember {
        mutableStateOf(SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Calendar.getInstance().time))
    }

    val locale = if (language == AppLanguage.BANGLA) Locale("bn", "BD") else Locale.US
    val monthFormat = remember(locale) { SimpleDateFormat("MMMM yyyy", locale) }
    val dayFormat = remember { SimpleDateFormat("yyyy-MM-dd", Locale.US) }
    val timeFormat = remember(locale) { SimpleDateFormat("hh:mm a", locale) }
    val fullDateFormat = remember(locale) { SimpleDateFormat("EEEE, MMMM d, yyyy", locale) }

    val todayCalendar = remember { Calendar.getInstance() }
    val todayKey = remember { dayFormat.format(todayCalendar.time) }

    val year = displayedCal.get(Calendar.YEAR)
    val month = displayedCal.get(Calendar.MONTH)

    val monthStartCal = remember(year, month) {
        Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, 1)
        }
    }

    val daysInMonth = monthStartCal.getActualMaximum(Calendar.DAY_OF_MONTH)
    val firstDayOfWeek = monthStartCal.get(Calendar.DAY_OF_WEEK) // 1 = Sunday
    val offset = firstDayOfWeek - 1

    val checkInsThisMonth = remember(checkIns, year, month) {
        checkIns.filter { record ->
            val recCal = Calendar.getInstance().apply { timeInMillis = record.timestampMillis }
            recCal.get(Calendar.YEAR) == year && recCal.get(Calendar.MONTH) == month
        }
    }

    val selectedRecord = remember(checkIns, selectedDayKey) {
        checkIns.find { it.dateKey == selectedDayKey }
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 14.dp)
            .testTag("calendar_screen"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Screen Header
        Text(
            text = NivoStrings.progressCalendar(language),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryText,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )
        Text(
            text = NivoStrings.calendarSubtitle(language),
            fontSize = 13.sp,
            color = SecondaryText,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Month Selector Header Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(CardBackground)
                .border(1.dp, OutlineGrey, RoundedCornerShape(16.dp))
                .padding(14.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = {
                            val prev = (displayedCal.clone() as Calendar).apply {
                                add(Calendar.MONTH, -1)
                            }
                            displayedCal = prev
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Previous Month",
                            tint = PrimaryText
                        )
                    }

                    Text(
                        text = monthFormat.format(displayedCal.time),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryText
                    )

                    IconButton(
                        onClick = {
                            val next = (displayedCal.clone() as Calendar).apply {
                                add(Calendar.MONTH, 1)
                            }
                            displayedCal = next
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Next Month",
                            tint = PrimaryText
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Weekday Labels
                val weekdays = if (language == AppLanguage.BANGLA) {
                    listOf("রবি", "সোম", "মঙ্গল", "বুধ", "বৃহঃ", "শুক্র", "শনি")
                } else {
                    listOf("S", "M", "T", "W", "T", "F", "S")
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    weekdays.forEach { day ->
                        Text(
                            text = day,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = SecondaryText,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Calendar Grid
                val totalSlots = offset + daysInMonth
                val totalRows = (totalSlots + 6) / 7

                for (rowIndex in 0 until totalRows) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        for (colIndex in 0 until 7) {
                            val slot = rowIndex * 7 + colIndex
                            val dayNumber = slot - offset + 1

                            if (dayNumber in 1..daysInMonth) {
                                val dateCal = Calendar.getInstance().apply {
                                    set(Calendar.YEAR, year)
                                    set(Calendar.MONTH, month)
                                    set(Calendar.DAY_OF_MONTH, dayNumber)
                                }
                                val dateKey = dayFormat.format(dateCal.time)
                                val isSelected = dateKey == selectedDayKey
                                val isToday = dateKey == todayKey
                                val isCheckedIn = checkIns.any { it.dateKey == dateKey }
                                val isPastDay = dateCal.before(todayCalendar) && !isToday

                                DayCell(
                                    day = dayNumber,
                                    language = language,
                                    isSelected = isSelected,
                                    isToday = isToday,
                                    isCheckedIn = isCheckedIn,
                                    isPastDay = isPastDay,
                                    onClick = { selectedDayKey = dateKey },
                                    modifier = Modifier.weight(1f)
                                )
                            } else {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Selected Day Details Card
        val parsedSelectedCal = remember(selectedDayKey) {
            Calendar.getInstance().apply {
                try {
                    val date = dayFormat.parse(selectedDayKey)
                    if (date != null) time = date
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(CardBackground)
                .border(1.dp, OutlineGrey, RoundedCornerShape(16.dp))
                .padding(16.dp)
                .testTag("selected_day_details_card")
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = fullDateFormat.format(parsedSelectedCal.time),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryText
                    )

                    // Status Badge
                    if (selectedRecord != null) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(PrimaryRed.copy(alpha = 0.2f))
                                .border(1.dp, PrimaryRed, RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = NivoStrings.completedStatus(language),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = PrimaryRed
                            )
                        }
                    } else if (selectedDayKey == todayKey) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(CardElevated)
                                .border(1.dp, OutlineGrey, RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = NivoStrings.todayStatus(language),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Normal,
                                color = PrimaryText
                            )
                        }
                    } else if (parsedSelectedCal.before(todayCalendar)) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(CardElevated)
                                .border(1.dp, OutlineGrey, RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = NivoStrings.missedStatus(language),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Normal,
                                color = SecondaryText
                            )
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(CardElevated)
                                .border(1.dp, OutlineGrey, RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = NivoStrings.upcomingStatus(language),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Normal,
                                color = SecondaryText
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                if (selectedRecord != null) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Schedule,
                            contentDescription = null,
                            tint = PrimaryRed,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (language == AppLanguage.BANGLA) {
                                "চেক-ইন সম্পন্ন: ${timeFormat.format(selectedRecord.timestampMillis)}"
                            } else {
                                "Checked in at ${timeFormat.format(selectedRecord.timestampMillis)}"
                            },
                            fontSize = 13.sp,
                            color = PrimaryText
                        )
                    }

                    if (selectedRecord.note.isNotBlank()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "\"${selectedRecord.note}\"",
                            fontSize = 12.sp,
                            color = SecondaryText
                        )
                    }
                } else {
                    Text(
                        text = if (selectedDayKey == todayKey) {
                            NivoStrings.checkInReady(language)
                        } else if (parsedSelectedCal.before(todayCalendar)) {
                            if (language == AppLanguage.BANGLA) "এই দিনে কোনো চেক-ইন রেকর্ড করা হয়নি।" else "No check-in was logged for this day."
                        } else {
                            if (language == AppLanguage.BANGLA) "আসন্ন দিন, আপনার লক্ষ্যের অপেক্ষায়।" else "Future milestone awaiting your commitment."
                        },
                        fontSize = 12.sp,
                        color = SecondaryText
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Monthly Summary
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(CardBackground)
                .border(1.dp, OutlineGrey, RoundedCornerShape(16.dp))
                .padding(16.dp)
                .testTag("monthly_summary_card")
        ) {
            Column {
                Text(
                    text = NivoStrings.monthlySummary(language),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryText
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    SummaryMetric(
                        label = NivoStrings.totalCheckInsLabel(language),
                        value = "${checkInsThisMonth.size.toLocalizedDigits(language)} ${if (language == AppLanguage.BANGLA) "দিন" else "Days"}",
                        modifier = Modifier.weight(1f)
                    )
                    SummaryMetric(
                        label = NivoStrings.consistencyRate(language),
                        value = "${((checkInsThisMonth.size.toFloat() / daysInMonth) * 100).toInt().toLocalizedDigits(language)}%",
                        modifier = Modifier.weight(1f)
                    )
                    SummaryMetric(
                        label = NivoStrings.expectedCompletionLabel(language),
                        value = selectedGoal?.title?.take(10) ?: "—",
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun DayCell(
    day: Int,
    language: AppLanguage,
    isSelected: Boolean,
    isToday: Boolean,
    isCheckedIn: Boolean,
    isPastDay: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .padding(2.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(
                when {
                    isSelected -> CardElevated
                    isToday -> CardBackground
                    else -> DarkBackground.copy(alpha = 0.5f)
                }
            )
            .border(
                width = if (isSelected) 1.5.dp else if (isToday) 1.dp else 0.dp,
                color = if (isSelected) PrimaryRed else if (isToday) OutlineGrey else androidx.compose.ui.graphics.Color.Transparent,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = day.toLocalizedDigits(language),
                fontSize = 11.sp,
                fontWeight = if (isToday || isSelected) FontWeight.Bold else FontWeight.Normal,
                color = when {
                    isCheckedIn -> PrimaryText
                    isPastDay -> SubtleGrey
                    else -> SecondaryText
                }
            )

            Spacer(modifier = Modifier.height(2.dp))

            if (isCheckedIn) {
                Box(
                    modifier = Modifier
                        .size(4.dp)
                        .clip(CircleShape)
                        .background(PrimaryRed)
                )
            } else if (isPastDay) {
                Box(
                    modifier = Modifier
                        .size(3.dp)
                        .clip(CircleShape)
                        .background(SubtleGrey)
                )
            } else {
                Spacer(modifier = Modifier.size(4.dp))
            }
        }
    }
}

@Composable
private fun SummaryMetric(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryRed
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            color = SecondaryText,
            textAlign = TextAlign.Center
        )
    }
}
