package com.example.melodydiary.ui.diary

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.melodydiary.R
import com.example.melodydiary.model.Diary
import com.example.melodydiary.ui.theme.MelodyDiaryTheme
import com.example.melodydiary.ui.theme.mygreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiaryScreen(
    modifier: Modifier = Modifier,
    diaryViewModel: DiaryViewModel
) {
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

        DiaryTab(modifier = Modifier.padding(innerPadding), diaryList.value)

    }
}

@Composable
fun DiaryTab(
    modifier: Modifier = Modifier,
    diaryList: List<Diary>
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
            // Add more tabs as needed
        }

        // Content for each tab
        when (selectedTabIndex) {
            0 -> {
                DiaryList(
                    diaryList = diaryList
                )
            }

            1 -> {
                TabContent(text = "Content for Tab 2")
            }
            // Add more content for additional tabs
        }
    }
}

@Composable
fun DiaryList(
    diaryList: List<Diary>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(diaryList, key = { it.diaryId }) { item ->
            DiaryItem(
                item = item
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


@Composable
fun DiaryItem(
    item: Diary,
    modifier: Modifier = Modifier
) {
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
            5.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = mygreen,
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp).background(Color.Transparent)
        ) {
            DateDetailInDiary(
                date = "03/10",
                time = "11:05",
                thu = "Thu 3",
                statusLogoRes = item.logo
            )
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleLarge,
            )

            Text(
                text = item.content,
                color = Color.Gray,
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
fun PreviewDiaryScreen() {
    MelodyDiaryTheme {
        val diaryViewModel: DiaryViewModel = viewModel(factory = DiaryViewModel.Factory)
        DiaryScreen(diaryViewModel = diaryViewModel)
    }
}