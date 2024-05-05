package com.example.melodydiary.ui.music


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.melodydiary.MelodyDiaryApp
import com.example.melodydiary.R
import com.example.melodydiary.model.Diary
import com.example.melodydiary.ui.diary.Calendar
import com.example.melodydiary.ui.diary.DiaryItem
import com.example.melodydiary.ui.diary.DiaryList
import com.example.melodydiary.ui.diary.DiaryScreen
import com.example.melodydiary.ui.diary.DiaryTab
import com.example.melodydiary.ui.diary.DiaryViewModel
import com.example.melodydiary.ui.theme.MelodyDiaryTheme
import java.util.Calendar

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicScreen(
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.title_music),
                        style = MaterialTheme.typography.titleLarge
                    )
                },

                )
        }
    ) { innerPadding ->

        DiaryTab(modifier = Modifier.padding(innerPadding))
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DiaryTab(
    modifier: Modifier = Modifier,
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
                    text = stringResource(R.string.title_tao_nhac),
                    style = MaterialTheme.typography.labelLarge
                )
            }
            Tab(
                selected = selectedTabIndex == 1,
                onClick = { selectedTabIndex = 1 },
                modifier = Modifier.background(Color.White)
            ) {
                Text(
                    text = stringResource(R.string.title_thu_vien_nhac),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }

        // Content for each tab
        when (selectedTabIndex) {
            0 -> {

            }
            1 -> {

            }
        }
    }
}

@Composable
fun GenMusicTab(
    modifier: Modifier = Modifier
) {

}


@Composable
fun TabContent(text: String) {
    Text(
        text = text,
        modifier = Modifier.padding(16.dp)
    )
}


@Preview(showBackground = true)
@Composable
fun PreviewMusicScreen() {
    MelodyDiaryTheme {
        MusicScreen()
    }
}