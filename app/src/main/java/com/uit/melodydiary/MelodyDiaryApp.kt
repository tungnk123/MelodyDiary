package com.uit.melodydiary

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.uit.melodydiary.ui.addDiary.AddDiaryScreen
import com.uit.melodydiary.ui.addDiary.DetailDiaryScreen
import com.uit.melodydiary.ui.addDiary.EditDiaryScreen
import com.uit.melodydiary.ui.diary.DiaryScreen
import com.uit.melodydiary.ui.diary.DiaryViewModel
import com.uit.melodydiary.ui.music.MusicScreen
import com.uit.melodydiary.ui.music.MusicViewModel
import com.uit.melodydiary.ui.profile.BackupScreen
import com.uit.melodydiary.ui.profile.ProfileScreen
import com.uit.melodydiary.ui.report.ReportScreen
import com.uit.melodydiary.ui.theme.MelodyDiaryTheme


enum class MelodyDiaryApp(@StringRes val title: Int) {
    DiaryScreen(title = R.string.diary_route),
    MusicScreen(title = R.string.music_route),
    ReportScreen(title = R.string.profile_route),
    ProfileScreen(title = R.string.profile_route),
    AddDiaryScreen(title = R.string.add_diary_route),
    DetailDiaryScreen(title = R.string.detail_diary_route),
    EditDiaryScreen(title = R.string.edit_diary_route),
    BackUpScreen(title = R.string.backup_route)
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MelodyDiaryApp(
    navController: NavHostController = rememberNavController()
) {

    val diaryViewModel: DiaryViewModel = viewModel(factory = DiaryViewModel.Factory)
    val musicViewModel: MusicViewModel = viewModel(factory = MusicViewModel.Factory)
    var isHomeScreen by remember {
        mutableStateOf(false)
    }
    isHomeScreen =
        navController.currentBackStackEntryAsState().value?.destination?.route == MelodyDiaryApp.DiaryScreen.name
    var isMusicScreen by remember {
        mutableStateOf(false)
    }
    isMusicScreen =
        navController.currentBackStackEntryAsState().value?.destination?.route == MelodyDiaryApp.MusicScreen.name
    var isReportScreen by remember {
        mutableStateOf(false)
    }
    isReportScreen =
        navController.currentBackStackEntryAsState().value?.destination?.route == MelodyDiaryApp.ReportScreen.name
    var isProfileScreen by remember {
        mutableStateOf(false)
    }
    isProfileScreen =
        navController.currentBackStackEntryAsState().value?.destination?.route == MelodyDiaryApp.ProfileScreen.name

    MelodyDiaryTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Scaffold(
                bottomBar = {
                    if (isHomeScreen || isMusicScreen || isProfileScreen || isReportScreen) {
                        BottomNavigationWithFab(
                            navController = navController
                        )
                    }
                },
                floatingActionButtonPosition = FabPosition.Center,
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = MelodyDiaryApp.DiaryScreen.name,
                    modifier = Modifier.padding(innerPadding)
                ) {

                    composable(
                        route = MelodyDiaryApp.DiaryScreen.name
                    ) {
                        DiaryScreen(diaryViewModel = diaryViewModel, navController = navController)
                    }

                    composable(route = MelodyDiaryApp.MusicScreen.name) {
                        MusicScreen(musicViewModel = musicViewModel, navController = navController)
                    }

                    composable(route = MelodyDiaryApp.AddDiaryScreen.name) {
                        AddDiaryScreen(
                            diaryViewModel = diaryViewModel,
                            musicViewModel = musicViewModel,
                            navController = navController
                        )
                    }

                    composable(
                        route = "${MelodyDiaryApp.DetailDiaryScreen.name}/{diaryId}",
                        arguments = listOf(navArgument("diaryId") {
                            type = NavType.StringType
                        })
                    ) { navBackStackEntry ->
                        val diaryId = navBackStackEntry.arguments?.getString("diaryId")
                        diaryId?.let {
                            DetailDiaryScreen(
                                diaryId = diaryId.toInt(),
                                navController = navController,
                                diaryViewModel = diaryViewModel
                            )
                        }

                    }

                    composable(
                        route = "${MelodyDiaryApp.EditDiaryScreen.name}/{diaryId}",
                        arguments = listOf(navArgument("diaryId") {
                            type = NavType.StringType
                        })
                    ) { navBackStackEntry ->
                        val diaryId = navBackStackEntry.arguments?.getString("diaryId")
                        diaryId?.let {
                            EditDiaryScreen(
                                diaryId = diaryId.toInt(),
                                navController = navController,
                                diaryViewModel = diaryViewModel,
                                musicViewModel = musicViewModel
                            )
                        }

                    }
                    composable(route = MelodyDiaryApp.ReportScreen.name) {
                        ReportScreen(
                            diaryViewModel = diaryViewModel, navController = navController
                        )
                    }
                    composable(route = MelodyDiaryApp.ProfileScreen.name) {
                        ProfileScreen(
                            navController = navController
                        )
                    }

                    composable(route = MelodyDiaryApp.BackUpScreen.name) {
                        BackupScreen(
                            navController = navController
                        )
                    }

                }
            }
        }
    }
}

@Composable
fun BottomNavigationWithFab(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    var selectedNavBarItem by remember {
        mutableStateOf(MelodyDiaryApp.DiaryScreen.name)
    }
    NavigationBar(
        containerColor = Color.White,
        modifier = modifier
    ) {
//        val isSelected
        NavigationBarItem(
            label = {
                Text(
                    text = stringResource(R.string.bottom_navigation_diary)
                )
            },
            icon = {
                Icon(
                    painter = painterResource(R.drawable.ic_diary),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            },
            selected = selectedNavBarItem == MelodyDiaryApp.DiaryScreen.name,
            onClick = {
                selectedNavBarItem = MelodyDiaryApp.DiaryScreen.name
                navController.navigate(MelodyDiaryApp.DiaryScreen.name)

            }
        )
        NavigationBarItem(
            label = {
                Text(
                    text = stringResource(R.string.bottom_navigation_music)
                )
            },
            icon = {
                Icon(
                    painter = painterResource(R.drawable.library_music),
                    contentDescription = null
                )
            },
            selected = selectedNavBarItem == MelodyDiaryApp.MusicScreen.name,
            onClick = {
                selectedNavBarItem = MelodyDiaryApp.MusicScreen.name
                navController.navigate(MelodyDiaryApp.MusicScreen.name)
            }
        )

        DiamondFab(
            onClick = {
                selectedNavBarItem = MelodyDiaryApp.AddDiaryScreen.name
                navController.navigate(MelodyDiaryApp.AddDiaryScreen.name)
            }
        )
        NavigationBarItem(
            label = {
                Text(
                    text = stringResource(R.string.bottom_navigation_report)
                )
            },
            icon = {
                Icon(
                    painter = painterResource(R.drawable.bar_chart_24px),
                    contentDescription = null
                )
            },
            selected = selectedNavBarItem == MelodyDiaryApp.ReportScreen.name,
            onClick = {
                selectedNavBarItem = MelodyDiaryApp.ReportScreen.name
                navController.navigate(MelodyDiaryApp.ReportScreen.name)
            }
        )
        NavigationBarItem(
            label = {
                Text(
                    text = stringResource(R.string.bottom_navigation_profile)
                )
            },
            icon = {
                Icon(
                    painter = painterResource(R.drawable.person),
                    contentDescription = null
                )
            },
            selected = selectedNavBarItem == MelodyDiaryApp.ProfileScreen.name,
            onClick = {
                selectedNavBarItem = MelodyDiaryApp.ProfileScreen.name
                navController.navigate(MelodyDiaryApp.ProfileScreen.name)
            }
        )
    }
}

@Composable
fun DiamondFab(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.height(100.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        FloatingActionButton(
            onClick = onClick,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .graphicsLayer {
                    rotationZ = 45f
                },
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier
                    .graphicsLayer {
                        rotationZ = -45f
                    }

            )
        }
    }
}

@Preview
@Composable
fun DiamondFabPreview() {
    DiamondFab(onClick = {}, Modifier.padding(bottom = 30.dp))
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F0EE)
@Composable
fun PreviewNavBar(
) {
    MelodyDiaryTheme {
        BottomNavigationWithFab(Modifier.padding(16.dp), rememberNavController())
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PreviewApp() {
    MelodyDiaryTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val diaryViewModel: DiaryViewModel = viewModel(factory = DiaryViewModel.Factory)
            DiaryScreen(diaryViewModel = diaryViewModel, navController = rememberNavController())
        }
    }
}
