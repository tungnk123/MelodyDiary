package com.uit.melodydiary.ui.diary.composables

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.uit.melodydiary.MelodyDiaryApp
import com.uit.melodydiary.model.Diary
import com.uit.melodydiary.ui.components.NoDiaryInfo
import com.uit.melodydiary.ui.diary.DiaryList
import com.uit.melodydiary.ui.diary.DiaryViewModel
import com.uit.melodydiary.utils.hasDiaryOnDay
import java.time.LocalDate

@Composable
fun Calendar(
    modifier: Modifier = Modifier,
    diaryList: List<Diary>,
    diaryViewModel: DiaryViewModel,
    navController: NavHostController,
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
        modifier = modifier.scrollable(
            orientation = Orientation.Vertical,
            state = rememberScrollState()
        )
    ) {

        CalendarVer2(
            diaryList = diaryList,
            onDateSelected = { localDate ->
                currentDateSelected = localDate
                if (selections.size > 0) {
                    if (selections.contains(localDate)) {
                        selections.remove(localDate)
                    }
                    else {
                        selections.clear()
                        selections.add(localDate)
                    }
                }
                else {
                    selections.add(localDate)
                }
                diaryListAtDate = diaryViewModel.getDiaryAtDateFromDatabase(localDate.toString())
            },
            selections = selections
        )
        Spacer(modifier = Modifier.height(5.dp))
        if (!hasDiaryOnDay(
                diaryList,
                currentDateSelected
            )
        ) {
            NoDiaryInfo()
        }
        else {
            diaryListAtDate =
                diaryViewModel.getDiaryAtDateFromDatabase(currentDateSelected.toString())
            DiaryList(diaryList = diaryListAtDate,
                onItemClick = {
                    navController.navigate("${MelodyDiaryApp.DetailDiaryScreen.name}/${it.diaryId}")
                })
        }
    }
}