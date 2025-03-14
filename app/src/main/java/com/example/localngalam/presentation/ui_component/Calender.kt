package com.example.localngalam.presentation.ui_component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localngalam.presentation.ui.theme.poppinsFont
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.*
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import com.example.localngalam.presentation.ui.theme.Blue2

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Calendar(
    modifier: Modifier,
    onDateSelected: (LocalDate?, LocalDate?, Long?) -> Unit
) {
    val today = remember { LocalDate.now() }
    val firstDayOfWeek = DayOfWeek.MONDAY
    val coroutineScope = rememberCoroutineScope()

    val state = rememberCalendarState(
        startMonth = YearMonth.now(),
        endMonth = YearMonth.now().plusMonths(12),
        firstDayOfWeek = firstDayOfWeek
    )

    var startDate by remember { mutableStateOf<LocalDate?>(null) }
    var endDate by remember { mutableStateOf<LocalDate?>(null) }

    Column(
        modifier = Modifier
            .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(size = 15.dp))
            .width(354.dp)
            .height(433.dp)
    ) {
        val currentMonth = state.firstVisibleMonth.yearMonth

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${currentMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${currentMonth.year}",
                fontSize = 15.sp,
                fontFamily = poppinsFont,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 20.dp, top = 20.dp, bottom = 20.dp)
            )

            Row {
                IconButton(
                    onClick = {
                        coroutineScope.launch {
                            val prevMonth = currentMonth.minusMonths(1)
                            if (prevMonth >= YearMonth.now()) {
                                state.scrollToMonth(prevMonth)
                            }
                        }
                    },
                    enabled = currentMonth > YearMonth.now()
                ) {
                    Text("<", fontSize = 20.sp, fontWeight = Bold)
                }

                IconButton(
                    onClick = {
                        coroutineScope.launch {
                            val nextMonth = currentMonth.plusMonths(1)
                            if (nextMonth <= YearMonth.now().plusMonths(12)) {
                                state.scrollToMonth(nextMonth)
                            }
                        }
                    }
                ) {
                    Text(">", fontSize = 20.sp, fontWeight = Bold)
                }
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .border(1.dp, Color.Black, RoundedCornerShape(15.dp))
                .padding(8.dp)
                .width(310.dp)
                .height(343.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DaysOfWeekTitle(firstDayOfWeek)

                HorizontalCalendar(
                    state = state,
                    dayContent = { day ->
                        DayCell(
                            day = day,
                            isToday = day.date == today,
                            isStartDate = day.date == startDate,
                            isEndDate = day.date == endDate,
                            isInRange = startDate != null && endDate != null && day.date in startDate!!..endDate!!,
                            currentMonth = state.firstVisibleMonth.yearMonth,
                            onClick = {
                                when {
                                    startDate == null -> startDate = day.date
                                    endDate == null && day.date.isAfter(startDate) -> endDate = day.date
                                    else -> {
                                        startDate = day.date
                                        endDate = null
                                    }
                                }

                                val daysBetween = if (startDate != null && endDate != null) {
                                    java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1
                                } else null
                                onDateSelected(startDate, endDate, daysBetween)
                            }
                        )
                    }
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DaysOfWeekTitle(firstDayOfWeek: DayOfWeek) {
    val daysOfWeek = DayOfWeek.values().rotateToFirst(firstDayOfWeek)
    Row(modifier = Modifier.fillMaxWidth()) {
        for (day in daysOfWeek) {
            Text(
                text = day.getDisplayName(TextStyle.NARROW, Locale.getDefault()),
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DayCell(
    day: CalendarDay,
    isToday: Boolean,
    isStartDate: Boolean,
    isEndDate: Boolean,
    isInRange: Boolean,
    currentMonth: YearMonth,
    onClick: () -> Unit
) {
    val isOutsideCurrentMonth = day.date.yearMonth != currentMonth
    val isBeforeToday = day.date.isBefore(LocalDate.now()) // ✅ Cek jika sebelum hari ini
    val backgroundColor = when {
        isOutsideCurrentMonth -> Color.Transparent
        isToday -> Color(0xFFA9A9A9)
        isStartDate || isEndDate || isInRange -> Blue2
        else -> Color(0xFFE6E6E6)
    }

    Box(
        modifier = Modifier
            .padding(horizontal = 1.dp, vertical = 7.dp)
            .fillMaxWidth()
            .aspectRatio(1f)
            .background(backgroundColor, shape = CircleShape)
            .clickable(enabled = !isOutsideCurrentMonth && !isBeforeToday) { // ✅ Cegah klik jika sebelum hari ini
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.date.dayOfMonth.toString(),
            fontSize = 12.sp,
            color = when {
                isOutsideCurrentMonth || isBeforeToday -> Color.LightGray // ✅ Buat lebih redup jika sebelum hari ini
                isStartDate || isEndDate || isInRange -> Color.White
                else -> Color.Black
            }
        )
    }
}


fun Array<DayOfWeek>.rotateToFirst(firstDay: DayOfWeek): List<DayOfWeek> {
    val index = indexOf(firstDay)
    return drop(index) + take(index)
}