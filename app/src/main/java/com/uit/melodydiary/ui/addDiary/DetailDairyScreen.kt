package com.uit.melodydiary.ui.addDiary

import MusicHelper
import android.os.Build
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.uit.melodydiary.MelodyDiaryApp
import com.uit.melodydiary.R
import com.uit.melodydiary.model.Diary
import com.uit.melodydiary.ui.diary.DiaryViewModel
import com.uit.melodydiary.ui.music.MusicViewModel
import com.uit.melodydiary.ui.theme.MelodyDiaryTheme
import com.uit.melodydiary.utils.DayOfWeekConverter
import com.uit.melodydiary.utils.plus
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonNull.content
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.coroutines.coroutineContext

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun DetailDiaryScreen(
    modifier: Modifier = Modifier,
    diaryId: Int,
    diaryViewModel: DiaryViewModel,
    navController: NavHostController
) {
    LaunchedEffect(diaryId) {
        diaryViewModel.getDiaryById(diaryId)
    }

    val diary by diaryViewModel.selectedDiary.collectAsState()
    var showBottomSheet by remember { mutableStateOf(true) }

    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM")
    val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    var openDialog by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }
    val context = LocalContext.current


    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.padding(end = 20.dp),
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                title = {
                    Text(
                        text = stringResource(R.string.title_see_nhat_ky),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                actions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { showMenu = !showMenu }
                        ) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = null
                            )
                        }
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                onClick = {
                                    showMenu = false
                                    // Implement delete action
                                    diaryViewModel.deleteDiaryById(diaryId)
                                    navController.popBackStack()
                                }
                            ) {
                                Text("Delete")
                            }
                            DropdownMenuItem(
                                onClick = {
                                    showMenu = false
                                    // Implement share action
                                    Toast.makeText(context, "Feature is under construction", Toast.LENGTH_SHORT).show()
                                }
                            ) {
                                Text("Share")
                            }
                        }
                        Spacer(modifier = Modifier.width(5.dp))
                        Button(
                            onClick = {
                                navController.navigate("${MelodyDiaryApp.EditDiaryScreen.name}/${diaryId}")
                            }
                        ) {
                            Text(
                                text = "Edit",
                                color = Color.White
                            )
                        }
                    }
                }
            )
        },
    ) { paddingValue ->
        Column(
            modifier = modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValue)
        ) {
            diary?.let {
                val diaryStyle = diary!!.diaryStyle
                DateDetailInDiaryWithSelection(
                    date = it.createdAt.format(formatter),
                    time = it.createdAt.format(timeFormatter),
                    thu = DayOfWeekConverter.convertToThu(it.createdAt.dayOfWeek.toString()),
                    it.logo,
                    onPickEmoteClick = {
                        showBottomSheet = !showBottomSheet
                    },
                    onDateTimePickerClick = {
                        openDialog = true
                    },
                    enabled = false
                )
                BorderlessTextField(
                    value = it.title,
                    placeholder = "Title",
                    onValueChange = {},
                    enable = false,
                    textStyle = TextStyle(
                        fontFamily = when (diaryStyle.fontStyle) {
                            "Serif" -> FontFamily.Serif
                            "Sans-serif" -> FontFamily.SansSerif
                            "Monospace" -> FontFamily.Monospace
                            "Cursive" -> FontFamily.Cursive
                            "Fantasy" -> FontFamily.Default
                            else -> FontFamily.Default
                        },
                        color = diaryStyle.color,
                        fontSize = diaryStyle.fontSize.plus(10.sp   )
                    )
                )
                DiaryTextField(
                    value = it.content,
                    placeholder = "Start to write now ...",
                    onValueChange = {},
                    enable = false,
                    textStyle = TextStyle(
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
                    )
                )
            }
        }
    }
}


