package com.uit.melodydiary.ui.diary

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.uit.melodydiary.MelodyDiaryApp
import com.uit.melodydiary.R
import com.uit.melodydiary.model.Diary
import com.uit.melodydiary.ui.components.NoDiaryInfo
import com.uit.melodydiary.ui.diary.composables.Calendar
import com.uit.melodydiary.ui.diary.composables.DiaryItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiaryScreen(
    modifier: Modifier = Modifier,
    diaryViewModel: DiaryViewModel,
    navController: NavHostController,
) {
    LaunchedEffect(Unit) {
        diaryViewModel.getDiaryFromDatabase()
    }
    val diaryList = diaryViewModel.diaryList.collectAsState()
    Scaffold(
        topBar = {
            Box {
                Image(
                    painter = painterResource(id = R.drawable.img_background_home),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    contentScale = ContentScale.Crop
                )
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.title_diary),
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    ),
                    actions = {
                        Image(
                            painter = painterResource(id = R.drawable.ic_present),
                            modifier = Modifier.size(32.dp),
                            contentDescription = null
                        )
                        Spacer(modifier = modifier.width(8.dp))
                        Spacer(modifier = modifier.width(16.dp))
                        Image(
                            painter = painterResource(id = R.drawable.baseline_search_24),
                            modifier = Modifier.size(32.dp),
                            contentDescription = null
                        )
                        Spacer(modifier = modifier.width(16.dp))
                    }
                )
            }
        },
    ) { innerPadding ->
        DiaryTab(
            modifier = Modifier
                .padding(innerPadding)
                .background(Color.White),
            diaryList = diaryList.value.sortedByDescending { it.createdAt },
            viewModel = diaryViewModel,
            navController = navController
        )

    }
}

@Composable
fun DiaryTab(
    modifier: Modifier = Modifier,
    diaryList: List<Diary>,
    viewModel: DiaryViewModel,
    navController: NavHostController,
) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier.height(48.dp),
        ) {
            Tab(
                selected = selectedTabIndex == 0,
                onClick = { selectedTabIndex = 0 },
                modifier = Modifier.background(Color.White)
            ) {
                Text(
                    text = stringResource(R.string.tab_name_danh_sach),
                    style = if (selectedTabIndex == 0) MaterialTheme.typography.labelLarge else MaterialTheme.typography.labelMedium
                )
            }
            Tab(
                selected = selectedTabIndex == 1,
                onClick = { selectedTabIndex = 1 },
                modifier = Modifier.background(Color.White)
            ) {
                Text(
                    text = stringResource(R.string.tab_name_lich),
                    style = if (selectedTabIndex == 1) MaterialTheme.typography.labelLarge else MaterialTheme.typography.labelMedium
                )
            }
        }

        when (selectedTabIndex) {
            0 -> {
                if (diaryList.isNotEmpty()) {
                    DiaryList(diaryList = diaryList,
                        onItemClick = {
                            navController.navigate("${MelodyDiaryApp.DetailDiaryScreen.name}/${it.diaryId}")
                        })
                }
                else {
                    NoDiaryInfo()
                }
            }

            1 -> {
                Calendar(
                    diaryList = diaryList,
                    diaryViewModel = viewModel,
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun DiaryList(
    diaryList: List<Diary>,
    modifier: Modifier = Modifier,
    onItemClick: (Diary) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp)
    ) {
        items(diaryList,
            key = { it.diaryId }) { item ->
            DiaryItem(
                item = item,
                onItemClick = {
                    onItemClick(item)
                },
                modifier = Modifier.padding(vertical = 10.dp)
            )
        }
    }
}
