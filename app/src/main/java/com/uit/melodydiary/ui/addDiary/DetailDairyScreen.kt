package com.uit.melodydiary.ui.addDiary

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.uit.melodydiary.MelodyDiaryApp
import com.uit.melodydiary.R
import com.uit.melodydiary.ui.addDiary.components.BorderlessTextField
import com.uit.melodydiary.ui.addDiary.components.DateDetailInDiaryWithSelection
import com.uit.melodydiary.ui.addDiary.components.DiaryTextField
import com.uit.melodydiary.ui.addDiary.components.ImageContentWrapper
import com.uit.melodydiary.ui.diary.DiaryViewModel
import com.uit.melodydiary.utils.DayOfWeekConverter
import com.uit.melodydiary.utils.byteArrayToString
import com.uit.melodydiary.utils.loadContentListFromFile
import com.uit.melodydiary.utils.plus
import java.time.format.DateTimeFormatter

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
    val diaryStyle = diary?.diaryStyle
    var isShrink by remember { mutableStateOf(false) }
    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier,
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
                        modifier = Modifier.padding(end = 20.dp),
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
        diary?.let {
            val diaryStyle = diary!!.diaryStyle
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .background(diaryStyle.colorPalette),
                contentPadding = paddingValue

            ) {

                item {
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
                }
                item {
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
                }

                itemsIndexed(loadContentListFromFile(it.contentFilePath)) { index, (type, value) ->
                    if (type == "text") {
                        DiaryTextField(
                            value = byteArrayToString(value),
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
                    else {
                        ImageContentWrapper(
                            imageByteArray = value,
                            onDeleteClick = {
                                Toast.makeText(context,"Please go to Edit mode to delete", Toast.LENGTH_LONG).show()
                            },
                            onShrinkClick = {
                                isShrink = !isShrink
                            }
                            , isShrink = isShrink
                        )
                    }
                }
            }
        }
    }
}


