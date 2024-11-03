package com.uit.melodydiary.ui.addDiary


import MusicHelper
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.IconButton
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.makeappssimple.abhimanyu.composeemojipicker.ComposeEmojiPickerBottomSheetUI
import com.uit.melodydiary.MelodyDiaryApp
import com.uit.melodydiary.R
import com.uit.melodydiary.model.Diary
import com.uit.melodydiary.model.DiaryStyle
import com.uit.melodydiary.model.Emotion
import com.uit.melodydiary.ui.addDiary.components.BorderlessTextField
import com.uit.melodydiary.ui.addDiary.components.DateDetailInDiaryWithSelection
import com.uit.melodydiary.ui.addDiary.components.DiaryTextField
import com.uit.melodydiary.ui.addDiary.components.ImageContentWrapper
import com.uit.melodydiary.ui.addDiary.components.MusicConfigurationDialog
import com.uit.melodydiary.ui.addDiary.components.TimePickerDialog
import com.uit.melodydiary.ui.addDiary.components.ToolBar
import com.uit.melodydiary.ui.components.FontSelectionContent
import com.uit.melodydiary.ui.components.ImageSelection
import com.uit.melodydiary.ui.components.PaletteSelection
import com.uit.melodydiary.ui.diary.DiaryViewModel
import com.uit.melodydiary.ui.music.MusicConfigurationTab
import com.uit.melodydiary.ui.music.MusicViewModel
import com.uit.melodydiary.ui.theme.mygreen
import com.uit.melodydiary.utils.AppConstants
import com.uit.melodydiary.utils.DayOfWeekConverter
import com.uit.melodydiary.utils.byteArrayToString
import com.uit.melodydiary.utils.plus
import com.uit.melodydiary.utils.saveContentListToFile
import com.uit.melodydiary.utils.stringToByteArray
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDiaryScreen(
    modifier: Modifier = Modifier,
    diaryViewModel: DiaryViewModel,
    musicViewModel: MusicViewModel,
    navController: NavHostController,
) {

    var title by remember {
        mutableStateOf("")
    }
    val content by remember {
        mutableStateOf("")
    }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var showBottomSheet by remember { mutableStateOf(true) }

    var logo by remember {
        androidx.compose.runtime.mutableIntStateOf(R.drawable.ic_pick)
    }

    var datetime by remember {
        mutableStateOf(LocalDateTime.now())
    }
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM")
    val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    var openDialog by remember { mutableStateOf(false) }
    var openTimeDialog by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val timePickerState = rememberTimePickerState()
    var mood by remember {
        mutableStateOf(AppConstants.EMOTION_FUN)
    }
    var showFontBottomSheet by remember { mutableStateOf(false) }
    var showIconBottomSheet by remember { mutableStateOf(false) }
    var showImageBottomSheet by remember { mutableStateOf(false) }
    var showPaletteBottomSheet by remember { mutableStateOf(false) }
    var showMusicConfigurationDialog by remember { mutableStateOf(false) }
    var selectedFontStyle by remember { mutableStateOf("Default") }
    var selectedFontSize by remember { mutableStateOf(16.sp) }
    var selectedColor by remember { mutableStateOf(Color.Black) }
    var selectedColorPalette by remember { mutableStateOf(mygreen) }
    var selectedEmoji by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var searchText by remember { mutableStateOf("") }
    var isShrink by remember {
        mutableStateOf(false)
    }
    var contentList by remember {
        mutableStateOf(
            mutableListOf(
                Pair("text", stringToByteArray(""))
            )
        )
    }
    var isPlayingMusic by remember {
        mutableStateOf(true)
    }

    LaunchedEffect(Unit) {
        AppConstants.demoList.forEach { item ->
            musicViewModel.insertMusic(item)
        }
    }

    if (openDialog) {
        DatePickerDialog(onDismissRequest = {
            openDialog = false
        }, confirmButton = {
            TextButton(
                onClick = {
                    openDialog = false
                    openTimeDialog = true
                }, enabled = datePickerState.selectedDateMillis != null
            ) {
                Text("OK")
            }
        }, dismissButton = {
            TextButton(onClick = {
                openDialog = false
            }) {
                Text("Cancel")
            }
        }) {
            DatePicker(state = datePickerState)
        }
    }

    if (openTimeDialog) {
        TimePickerDialog(onCancel = {
            openTimeDialog = false
        }, onConfirm = {
            openTimeDialog = false
            val selectedDateMillis = datePickerState.selectedDateMillis ?: 0
            val selectedHour = timePickerState.hour
            val selectedMinute = timePickerState.minute
            datetime = Instant.ofEpochMilli(selectedDateMillis).atZone(ZoneId.systemDefault())
                .toLocalDateTime().withHour(selectedHour).withMinute(selectedMinute)
        }) {
            TimePicker(state = timePickerState)
        }
    }

    Scaffold(modifier = modifier, topBar = {
        CenterAlignedTopAppBar(modifier = Modifier.background(selectedColorPalette),
            colors = TopAppBarDefaults.topAppBarColors().copy(
                containerColor = selectedColorPalette
            ),
            navigationIcon = {

                IconButton(onClick = {
                    navController.popBackStack()
                    MusicHelper.pause()
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = null
                    )
                }
            },
            title = {
                Text(
                    text = stringResource(R.string.title_viet_nhat_ky),
                    style = MaterialTheme.typography.titleLarge
                )
            },
            actions = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(end = 16.dp)
                ) {
                    Spacer(modifier = Modifier.width(5.dp))
                    Button(onClick = {
                        val newDiary = Diary(
                            diaryId = 0,
                            title = title,
                            content = content,
                            mood = mood,
                            logo = logo,
                            createdAt = datetime,
                            diaryStyle = DiaryStyle(
                                fontStyle = selectedFontStyle,
                                color = selectedColor,
                                fontSize = selectedFontSize,
                                colorPalette = selectedColorPalette
                            ),
                            contentFilePath = saveContentListToFile(context, contentList)
                        )
                        diaryViewModel.addDiary(newDiary)
                        navController.navigate(MelodyDiaryApp.DiaryScreen.name)
                        MusicHelper.pause()
                    }) {
                        Text(
                            text = stringResource(R.string.msg_save_btn), color = Color.White
                        )
                    }
                }
            })
    }, bottomBar = {
        Column {
            MusicConfigurationTab(onShowMusicConfigurationDialog = {
                showMusicConfigurationDialog = !showMusicConfigurationDialog
            }, onPlayPauseClick = {
                if (!isPlayingMusic) {
                    MusicHelper.currentSong?.let { MusicHelper.togglePlayback(it) { } }
                } else {
                    MusicHelper.pause()
                }
                isPlayingMusic = !isPlayingMusic

            }, onPlayPreviousClick = {
                MusicHelper.pause()
                MusicHelper.previous { }
            }, onPlayNextClick = {
                MusicHelper.pause()
                MusicHelper.next { }
            }, isPlaying = isPlayingMusic
            )
            Spacer(modifier = Modifier.height(3.dp))

            ToolBar(onFontFormatClick = {
                showFontBottomSheet = true
            }, onPaletteClick = {
                showPaletteBottomSheet = true
            }, onIconClick = {
                showIconBottomSheet = true
            }, onImageClick = {
                showImageBottomSheet = true
            })
        }

    }) { paddingValue ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(selectedColorPalette),
            contentPadding = paddingValue

        ) {
            item {
                DateDetailInDiaryWithSelection(
                    date = datetime.format(formatter),
                    time = datetime.format(timeFormatter),
                    thu = DayOfWeekConverter.convertToThu(datetime.dayOfWeek.toString()),
                    logo,
                    onPickEmoteClick = {
                        showBottomSheet = !showBottomSheet
                    },
                    onDateTimePickerClick = {
                        openDialog = true
                    },
                    enabled = true
                )
            }

            item {
                BorderlessTextField(
                    value = title, placeholder = "Title", onValueChange = {
                        title = it
                    }, enable = true, textStyle = TextStyle(
                        fontFamily = when (selectedFontStyle) {
                            "Serif" -> FontFamily.Serif
                            "Sans-serif" -> FontFamily.SansSerif
                            "Monospace" -> FontFamily.Monospace
                            "Cursive" -> FontFamily.Cursive
                            "Fantasy" -> FontFamily.Default
                            else -> FontFamily.Default
                        }, fontSize = selectedFontSize.value.sp + 10.sp, color = selectedColor
                    )
                )
            }

            itemsIndexed(contentList) { index, (type, value) ->
                if (type == "text") {
                    DiaryTextField(
                        value = byteArrayToString(value),
                        placeholder = "Start to write now ...",
                        onValueChange = {
                            val updatedContentList = contentList.toMutableList()
                            updatedContentList[index] = "text" to stringToByteArray(it)
                            contentList = updatedContentList
                        },
                        enable = true,
                        textStyle = TextStyle(
                            fontFamily = when (selectedFontStyle) {
                                "Serif" -> FontFamily.Serif
                                "Sans-serif" -> FontFamily.SansSerif
                                "Monospace" -> FontFamily.Monospace
                                "Cursive" -> FontFamily.Cursive
                                "Fantasy" -> FontFamily.Default
                                else -> FontFamily.Default
                            }, fontSize = selectedFontSize, color = selectedColor
                        )
                    )
                } else {
                    ImageContentWrapper(imageByteArray = value, onDeleteClick = {
                        val updatedContentList = contentList.toMutableList()
                        updatedContentList.removeAt(index)
                        contentList = updatedContentList
                    }, onShrinkClick = {
                        isShrink = !isShrink
                    }, isShrink = isShrink
                    )
                }
            }


        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                }, sheetState = sheetState, modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .height(300.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = stringResource(R.string.ban_hom_nay_the_nao),
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                    ) {
                        item {
                            IconButton(
                                onClick = {
                                    mood = AppConstants.EMOTION_FUN
                                    showBottomSheet = false
                                    logo = R.drawable.ic_face
                                    scope.launch {
                                        MusicHelper.pause()
                                        MusicHelper.clearAllMusic()
                                        musicViewModel.populateMusicList(Emotion.Fun.emotion)
                                        MusicHelper.playSequential(onPlaybackCompleted = {})
                                    }
                                },
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.ic_face),
                                    contentDescription = null,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }
                        item {
                            IconButton(
                                onClick = {
                                    mood = AppConstants.EMOTION_CRY
                                    showBottomSheet = false
                                    logo = R.drawable.ic_cry
                                    scope.launch {
                                        MusicHelper.pause()
                                        MusicHelper.clearAllMusic()
                                        musicViewModel.populateMusicList(Emotion.Cry.emotion)
                                        MusicHelper.playSequential(onPlaybackCompleted = {})
                                    }
                                },
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.ic_cry),
                                    contentDescription = null,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                        item {
                            IconButton(
                                onClick = {
                                    mood = AppConstants.EMOTION_SAD
                                    showBottomSheet = false
                                    logo = R.drawable.ic_neutral
                                    scope.launch {
                                        MusicHelper.pause()
                                        MusicHelper.clearAllMusic()
                                        musicViewModel.populateMusicList(AppConstants.EMOTION_SAD)
                                        MusicHelper.playSequential(onPlaybackCompleted = {})
                                    }
                                },
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.ic_neutral),
                                    contentDescription = null,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                        }
                        item {
                            IconButton(
                                onClick = {
                                    mood = AppConstants.EMOTION_FEAR
                                    showBottomSheet = false
                                    logo = R.drawable.ic_fear
                                    scope.launch {
                                        MusicHelper.pause()
                                        MusicHelper.clearAllMusic()
                                        musicViewModel.populateMusicList(AppConstants.EMOTION_FEAR)
                                        MusicHelper.playSequential(onPlaybackCompleted = {})
                                    }
                                },
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.ic_fear),
                                    contentDescription = null,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                        }
                        item {
                            IconButton(
                                onClick = {
                                    mood = AppConstants.EMOTION_DISGUST
                                    showBottomSheet = false
                                    logo = R.drawable.ic_disgust
                                    scope.launch {
                                        MusicHelper.pause()
                                        MusicHelper.clearAllMusic()
                                        musicViewModel.populateMusicList(AppConstants.EMOTION_DISGUST)
                                        MusicHelper.playSequential(onPlaybackCompleted = {})
                                    }
                                },
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.ic_disgust),
                                    contentDescription = null,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                        item {
                            IconButton(
                                onClick = {
                                    mood = AppConstants.EMOTION_ANGRY
                                    showBottomSheet = false
                                    logo = R.drawable.ic_angry
                                    scope.launch {
                                        MusicHelper.pause()
                                        MusicHelper.clearAllMusic()
                                        musicViewModel.populateMusicList(AppConstants.EMOTION_ANGRY)
                                        MusicHelper.playSequential(onPlaybackCompleted = {})
                                    }
                                },
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.ic_angry),
                                    contentDescription = null,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }

                    }
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(onClick = {
                            Toast.makeText(
                                context, "Feature is under construction", Toast.LENGTH_SHORT
                            ).show()
                        }) {
                            Text(
                                text = stringResource(R.string.btn_them_tam_trang),
                                color = Color.Blue
                            )
                        }
                        Image(
                            painter = painterResource(R.drawable.ic_groupface),
                            contentDescription = stringResource(R.string.msg_new_status),
                            modifier = Modifier.size(32.dp)
                        )
                    }

                }
            }
        }

        if (showFontBottomSheet) {
            ModalBottomSheet(onDismissRequest = {
                showFontBottomSheet = false
            }) {
                FontSelectionContent(selectedFontStyle = selectedFontStyle,
                    onFontStyleChange = { style ->
                        selectedFontStyle = style
                    },
                    selectedFontSize = selectedFontSize,
                    onFontSizeChange = { size ->
                        selectedFontSize = size
                    },
                    selectedColor = selectedColor,
                    onColorChange = { color ->
                        selectedColor = color
                    }

                )
            }
        }

        if (showIconBottomSheet) {
            ModalBottomSheet(onDismissRequest = {
                showIconBottomSheet = false
            }) {
                ComposeEmojiPickerBottomSheetUI(
                    onEmojiClick = { emoji ->
                        showIconBottomSheet = false
                        selectedEmoji = emoji.character
                        val updatedContentList = contentList.toMutableList()
                        val currentText =
                            byteArrayToString(updatedContentList[contentList.size - 1].second) + selectedEmoji
                        updatedContentList[contentList.size - 1] =
                            "text" to stringToByteArray(currentText)
                        contentList = updatedContentList
                    },
                    searchText = searchText,
                    updateSearchText = { updatedSearchText ->
                        searchText = updatedSearchText
                    },
                )
            }
        }

        if (showPaletteBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showPaletteBottomSheet = false
                }, containerColor = Color.White

            ) {
                PaletteSelection(color = selectedColorPalette, onColorChange = {
                    selectedColorPalette = it
                })
            }
        }

        if (showImageBottomSheet) {
            ModalBottomSheet(onDismissRequest = {
                showImageBottomSheet = !showImageBottomSheet
            }) {
                ImageSelection(selectedImageUri = selectedImageUri, onImageSelected = { uri ->
                    selectedImageUri = uri
                    val pair: Pair<String, ByteArray> = Pair(
                        "image", context.contentResolver.openInputStream(
                            selectedImageUri!!
                        )?.readBytes()!!
                    )
                    contentList.add(pair)
                    val temp: Pair<String, ByteArray> = Pair("text", stringToByteArray(""))
                    contentList.add(temp)
                    showImageBottomSheet = !showImageBottomSheet
                })
            }
        }

        if (showMusicConfigurationDialog) {
            MusicHelper.currentSong?.let {
                MusicConfigurationDialog(
                    onDismiss = {
                        showMusicConfigurationDialog = !showMusicConfigurationDialog
                    }, giaiDieuName = it.title, currentEmotion = mood + " music"
                )
            }
        }
    }
}