package com.uit.melodydiary.ui.diary

import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import com.uit.melodydiary.MelodyDiaryApp
import com.uit.melodydiary.R
import com.uit.melodydiary.model.Diary
import com.uit.melodydiary.ui.components.Day
import com.uit.melodydiary.ui.components.MonthHeader
import com.uit.melodydiary.ui.components.NoDiaryInfo
import com.uit.melodydiary.ui.components.SimpleCalendarTitle
import com.uit.melodydiary.ui.theme.MelodyDiaryTheme
import com.uit.melodydiary.ui.theme.mygreen
import com.uit.melodydiary.utils.DayOfWeekConverter
import com.uit.melodydiary.utils.byteArrayToString
import com.uit.melodydiary.utils.hasDiaryOnDay
import com.uit.melodydiary.utils.loadContentListFromFile
import com.uit.melodydiary.utils.plus
import com.uit.melodydiary.utils.rememberFirstMostVisibleMonth
import com.uit.melodydiary.utils.stringToByteArray
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiaryScreen(
    modifier: Modifier = Modifier,
    diaryViewModel: DiaryViewModel,
    navController: NavHostController
) {
    LaunchedEffect(Unit) {
        diaryViewModel.getDiaryFromDatabase()
    }
    val diaryList = diaryViewModel.diaryList.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.title_diary),
                        style = MaterialTheme.typography.titleLarge
                    )
                },

                )
        }
    ) { innerPadding ->

        DiaryTab(modifier = Modifier.padding(innerPadding),
            diaryList.value.sortedByDescending { it.createdAt }, diaryViewModel, navController = navController)

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DiaryTab(
    modifier: Modifier = Modifier,
    diaryList: List<Diary>,
    viewModel: DiaryViewModel,
    navController: NavHostController
) {
    var selectedTabIndex by remember { mutableStateOf(0) }

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier.height(48.dp),
            contentColor = Color.Green
        ) {
            Tab(
                selected = selectedTabIndex == 0,
                onClick = { selectedTabIndex = 0 },
                modifier = Modifier.background(Color.White)
            ) {
                Text(
                    text = stringResource(R.string.tab_name_danh_sach),
                    style = MaterialTheme.typography.labelLarge
                )
            }
            Tab(
                selected = selectedTabIndex == 1,
                onClick = { selectedTabIndex = 1 },
                modifier = Modifier.background(Color.White)
            ) {
                Text(
                    text = stringResource(R.string.tab_name_lich),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }

        // Content for each tab
        when (selectedTabIndex) {
            0 -> {
                if (diaryList.isNotEmpty()) {
                    DiaryList(
                        diaryList = diaryList,
                        onItemClick = {
                            navController.navigate("${MelodyDiaryApp.DetailDiaryScreen.name}/${it.diaryId}")
                        }
                    )
                }
                else {
                    NoDiaryInfo()
                }
            }
            1 -> {
                Calendar(diaryList = diaryList, diaryViewModel = viewModel, navController = navController)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DiaryList(
    diaryList: List<Diary>,
    modifier: Modifier = Modifier,
    onItemClick: (Diary) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp)
    ) {
        items(diaryList, key = { it.diaryId }) { item ->
            DiaryItem(
                item = item,
                modifier = Modifier.padding(vertical = 10.dp),
                onItemClick = {
                    onItemClick(item)
                }
            )
        }
    }
}

@Composable
fun TabContent(text: String) {
    Text(
        text = text,
        modifier = Modifier.padding(16.dp)
    )
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Calendar(
    modifier: Modifier = Modifier,
    diaryList: List<Diary>,
    diaryViewModel: DiaryViewModel,
    navController: NavHostController
) {
    var currentDateSelected by remember {
        mutableStateOf(LocalDate.now())
    }

    var diaryListAtDate by remember {
        mutableStateOf(listOf<Diary>())
    }
    val selections = remember { mutableStateListOf<LocalDate>() }
    LaunchedEffect(currentDateSelected) {
        diaryListAtDate = diaryViewModel.getDiaryAtDateFromDatabase(currentDateSelected.toString())
    }
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.scrollable(
            orientation = Orientation.Vertical,
            state = rememberScrollState()
        )
    ) {

        CalendarVer2(
            diaryList = diaryList, onDateSelected = { localDate ->
                currentDateSelected = localDate
                if (selections.size > 0) {
                    if (selections.contains(localDate)) {
                        selections.remove(localDate)
                    } else {
                        selections.clear()
                        selections.add(localDate)
                    }
                } else {
                    selections.add(localDate)
                }
                diaryListAtDate = diaryViewModel.getDiaryAtDateFromDatabase(localDate.toString())
            },
            selections = selections
        )
        Spacer(modifier = Modifier.height(5.dp))
        if (!hasDiaryOnDay(diaryList, currentDateSelected)) {
            NoDiaryInfo()
        } else {
            diaryListAtDate = diaryViewModel.getDiaryAtDateFromDatabase(currentDateSelected.toString())
            DiaryList(diaryList = diaryListAtDate,
                onItemClick = {
                    navController.navigate("${MelodyDiaryApp.DetailDiaryScreen.name}/${it.diaryId}")
                })
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarVer2(
    adjacentMonths: Long = 500,
    diaryList: List<Diary>,
    onDateSelected: (LocalDate) -> Unit,
    selections: SnapshotStateList<LocalDate>
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
    val visibleMonth = rememberFirstMostVisibleMonth(state, viewportPercent = 90f)
    SimpleCalendarTitle(
        modifier = Modifier.padding(vertical = 10.dp, horizontal = 8.dp),
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
                today = CalendarDay(date = LocalDate.now(), position = DayPosition.InDate),
                hasDiary = hasDiaryOnDay(diaryList, day.date)
            ) { clicked ->
                onDateSelected(clicked.date)
            }
        },
        monthHeader = {
            MonthHeader(daysOfWeek = daysOfWeek)
        },
    )

}



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DiaryItem(
    item: Diary,
    modifier: Modifier = Modifier,
    onItemClick: () -> Unit
) {
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM")
    val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    val diaryStyle = item.diaryStyle
    Card(
        modifier = modifier
            .fillMaxWidth()
        ,
        shape = RoundedCornerShape(
            topEnd = 20.dp,
            bottomStart = 20.dp,
            topStart = 5.dp,
            bottomEnd = 5.dp
        ),
        elevation = CardDefaults.cardElevation(
            2.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = diaryStyle.colorPalette,
        ),
        onClick = onItemClick
    ) {
        Column(
            modifier = Modifier.padding(16.dp).background(Color.Transparent)
        ) {
            DateDetailInDiary(
                date = item.createdAt.format(formatter),
                time = item.createdAt.format(timeFormatter),
                thu = DayOfWeekConverter.convertToThu(item.createdAt.dayOfWeek.toString()),
                statusLogoRes = item.logo
            )
            Text(
                text = if (!item.title.isNullOrEmpty()) item.title else "Title",
                style = TextStyle(
                    fontFamily = when (diaryStyle.fontStyle) {
                        "Serif" -> FontFamily.Serif
                        "Sans-serif" -> FontFamily.SansSerif
                        "Monospace" -> FontFamily.Monospace
                        "Cursive" -> FontFamily.Cursive
                        "Fantasy" -> FontFamily.Default
                        else -> FontFamily.Default
                    },
                    color = diaryStyle.color,
                    fontSize = diaryStyle.fontSize.value.sp + 10.sp
                ),
            )
            val textItem = loadContentListFromFile(item.contentFilePath).firstOrNull { it.first == "text" }
            val textContent: ByteArray = textItem?.second ?: stringToByteArray("")
            Text(
                text = byteArrayToString(textContent),
                style = TextStyle(
                    fontFamily = when (diaryStyle.fontStyle) {
                        "Serif" -> FontFamily.Serif
                        "Sans-serif" -> FontFamily.SansSerif
                        "Monospace" -> FontFamily.Monospace
                        "Cursive" -> FontFamily.Cursive
                        "Fantasy" -> FontFamily.Default
                        else -> FontFamily.Default
                    },
                    color = diaryStyle.color,
                    fontSize = diaryStyle.fontSize
                ),
            )
        }
    }
}


@Composable
fun DateDetailInDiary(
    date: String,
    time: String,
    thu: String,
    @DrawableRes statusLogoRes: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = date,
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(modifier = Modifier.width(5.dp))
        Box(
            modifier = Modifier.width(2.dp).height(32.dp).background(Color.Black)
        )
        Spacer(modifier = Modifier.width(5.dp))
        Column {
            Text(
                text = time
            )
            Text(
                text = thu
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Image(
            painter = painterResource(statusLogoRes),
            contentDescription = null,
            modifier = Modifier.size(32.dp)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewCalendar() {
    MelodyDiaryTheme {
//        Calendar()
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PreviewDiaryScreen() {
    MelodyDiaryTheme {
        val diaryViewModel: DiaryViewModel = viewModel(factory = DiaryViewModel.Factory)
        DiaryScreen(diaryViewModel = diaryViewModel , navController = rememberNavController())
    }
}