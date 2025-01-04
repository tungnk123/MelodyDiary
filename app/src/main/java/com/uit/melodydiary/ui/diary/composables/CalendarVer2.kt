package com.uit.melodydiary.ui.diary.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import com.uit.melodydiary.model.Diary
import com.uit.melodydiary.ui.components.Day
import com.uit.melodydiary.ui.components.MonthHeader
import com.uit.melodydiary.ui.components.SimpleCalendarTitle
import com.uit.melodydiary.utils.hasDiaryOnDay
import com.uit.melodydiary.utils.rememberFirstMostVisibleMonth
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun CalendarVer2(
    adjacentMonths: Long = 500,
    diaryList: List<Diary>,
    onDateSelected: (LocalDate) -> Unit,
    selections: SnapshotStateList<LocalDate>,
) {
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(adjacentMonths) }
    val endMonth = remember { currentMonth.plusMonths(adjacentMonths) }
    val daysOfWeek = remember { daysOfWeek() }
    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = daysOfWeek.first(),
    )
    val coroutineScope = rememberCoroutineScope()
    val visibleMonth = rememberFirstMostVisibleMonth(
        state,
        viewportPercent = 90f
    )
    SimpleCalendarTitle(
        modifier = Modifier.padding(
            vertical = 10.dp,
            horizontal = 8.dp
        ),
        currentMonth = visibleMonth.yearMonth,
        goToPrevious = {
            coroutineScope.launch {
                state.animateScrollToMonth(state.firstVisibleMonth.yearMonth.previousMonth)
            }
        },
        goToNext = {
            coroutineScope.launch {
                state.animateScrollToMonth(state.firstVisibleMonth.yearMonth.nextMonth)
            }
        },
    )
    HorizontalCalendar(
        modifier = Modifier.testTag("Calendar"),
        state = state,
        dayContent = { day ->
            Day(
                day,
                isSelected = selections.contains(day.date),
                today = CalendarDay(
                    date = LocalDate.now(),
                    position = DayPosition.InDate
                ),
                hasDiary = hasDiaryOnDay(
                    diaryList,
                    day.date
                )
            ) { clicked ->
                onDateSelected(clicked.date)
            }
        },
        monthHeader = {
            MonthHeader(daysOfWeek = daysOfWeek)
        },
    )
}