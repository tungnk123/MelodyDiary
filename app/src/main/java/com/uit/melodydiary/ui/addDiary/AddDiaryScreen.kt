package com.uit.melodydiary.ui.addDiary


import MusicHelper
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
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
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
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
import coil.compose.AsyncImage
import com.makeappssimple.abhimanyu.composeemojipicker.ComposeEmojiPickerBottomSheetUI
import com.uit.melodydiary.MelodyDiaryApp
import com.uit.melodydiary.R
import com.uit.melodydiary.model.Diary
import com.uit.melodydiary.model.DiaryStyle
import com.uit.melodydiary.ui.components.FontSelectionContent
import com.uit.melodydiary.ui.components.ImageSelection
import com.uit.melodydiary.ui.components.PaletteSelection
import com.uit.melodydiary.ui.diary.DiaryViewModel
import com.uit.melodydiary.ui.music.MusicViewModel
import com.uit.melodydiary.ui.theme.MelodyDiaryTheme
import com.uit.melodydiary.ui.theme.mygreen
import com.uit.melodydiary.utils.DayOfWeekConverter
import com.uit.melodydiary.utils.byteArrayToString
import com.uit.melodydiary.utils.loadContentListFromFile
import com.uit.melodydiary.utils.plus
import com.uit.melodydiary.utils.saveContentListToFile
import com.uit.melodydiary.utils.stringToByteArray
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
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
    var content by remember {
        mutableStateOf("")
    }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var showBottomSheet by remember { mutableStateOf(true) }

    var logo by remember {
        mutableStateOf(R.drawable.ic_pick)
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
    var musicListString: List<String> = mutableListOf()
    var mood: String = "Fun"
    var showFontBottomSheet by remember { mutableStateOf(false) }
    var showIconBottomSheet by remember { mutableStateOf(false) }
    var showImageBottomSheet by remember { mutableStateOf(false) }
    var showPaletteBottomSheet by remember { mutableStateOf(false) }
    var selectedFontStyle by remember { mutableStateOf("Default") }
    var selectedFontSize by remember { mutableStateOf(16.sp) }
    var selectedColor by remember { mutableStateOf(Color.Black) }
    var selectedColorPalette by remember { mutableStateOf(mygreen) }
    var selectedEmoji by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var searchText by  remember { mutableStateOf("") }
    var isShrink by remember {
        mutableStateOf(false)
    }
    var contentList by remember {
        mutableStateOf(
            mutableListOf<Pair<String, ByteArray>>(
                Pair("text", stringToByteArray(""))
            )
        )
    }


    if (openDialog) {
        DatePickerDialog(
            onDismissRequest = {
                openDialog = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        openDialog = false
                        openTimeDialog = true
                    },
                    enabled = datePickerState.selectedDateMillis != null
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openDialog = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (openTimeDialog) {
        TimePickerDialog(
            onCancel = {
                openTimeDialog = false
            },
            onConfirm = {
                openTimeDialog = false
                val selectedDateMillis = datePickerState.selectedDateMillis ?: 0
                val selectedHour = timePickerState.hour
                val selectedMinute = timePickerState.minute
                datetime = Instant.ofEpochMilli(selectedDateMillis)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime()
                    .withHour(selectedHour)
                    .withMinute(selectedMinute)
            }
        ) {
            TimePicker(state = timePickerState)
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.background(selectedColorPalette),
                colors = TopAppBarDefaults.topAppBarColors().copy(
                    containerColor = selectedColorPalette
                ),
                navigationIcon = {

                    IconButton(
                        onClick = {
                            navController.popBackStack()
                            MusicHelper.pause()
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
                        text = stringResource(R.string.title_viet_nhat_ky),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                actions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
//                        IconButton(
//                            onClick = {
//                                Toast.makeText(context, "Feature is under construction", Toast.LENGTH_SHORT).show()
//                            }
//                        ) {
//                            Icon(
//                                imageVector = Icons.Default.MoreVert,
//                                contentDescription = null
//                            )
//                        }
                        Spacer(modifier = Modifier.width(5.dp))
                        Button(
                            onClick = {
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
                            }
                        ) {
                            Text(
                                text = "Save",
                                color = Color.White
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            ToolBar(
                onFontFormatClick = {
                    showFontBottomSheet = true
                },
                onPaletteClick = {
                    showPaletteBottomSheet = true
                },
                onIconClick = {
                    showIconBottomSheet = true
                },
                onImageClick = {
                    showImageBottomSheet = true
                }
            )
        }
    ) { paddingValue ->
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
                    value = title,
                    placeholder = "Title",
                    onValueChange = {
                        title = it
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
                        },
                        fontSize = selectedFontSize.value.sp + 10.sp,
                        color = selectedColor
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
                            },
                            fontSize = selectedFontSize,
                            color = selectedColor
                        )
                    )
                } else {
                    ImageContentWrapper(
                        imageByteArray = value,
                        onDeleteClick = {
                            val updatedContentList = contentList.toMutableList()
                            updatedContentList.removeAt(index)
                            contentList = updatedContentList
                        },
                        onShrinkClick = {
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
                },
                sheetState = sheetState,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.height(300.dp).fillMaxWidth(),
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
                                    mood = "Fun"
                                    showBottomSheet = false
                                    logo = R.drawable.ic_face
                                    scope.launch {
                                        MusicHelper.pause()
                                        musicViewModel.populateMusicList("fun")
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
                                    mood = "Cry"
                                    showBottomSheet = false
                                    logo = R.drawable.ic_cry
                                    scope.launch {
                                        MusicHelper.pause()
                                        musicViewModel.populateMusicList("sadnees, cry music")
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
                                    mood = "Sad"
                                    showBottomSheet = false
                                    logo = R.drawable.ic_neutral
                                    scope.launch {
                                        MusicHelper.pause()
                                        musicViewModel.populateMusicList("sad")
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
                                    mood = "Fear"
                                    showBottomSheet = false
                                    logo = R.drawable.ic_fear
                                    scope.launch {
                                        MusicHelper.pause()
                                        musicViewModel.populateMusicList("fear")
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
                                    mood = "Disgust"
                                    showBottomSheet = false
                                    logo = R.drawable.ic_disgust
                                    scope.launch {
                                        MusicHelper.pause()
                                        musicViewModel.populateMusicList("disgust")
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
                                    mood = "Angry"
                                    showBottomSheet = false
                                    logo = R.drawable.ic_angry
                                    scope.launch {
                                        MusicHelper.pause()
                                        musicViewModel.populateMusicList("angry")
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
                        TextButton(
                            onClick = {}
                        ) {
                            Text(
                                text = stringResource(R.string.btn_them_tam_trang),
                                color = Color.Blue
                            )
                        }
                        Image(
                            painter = painterResource(R.drawable.ic_groupface),
                            contentDescription = "Group face for new status",
                            modifier = Modifier.size(32.dp)
                        )
                    }

                }
            }
        }

        if (showFontBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showFontBottomSheet = false
                }
            ) {
                FontSelectionContent(
                    selectedFontStyle = selectedFontStyle,
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
            ModalBottomSheet(
                onDismissRequest = {
                    showIconBottomSheet = false
                }
            ) {
                ComposeEmojiPickerBottomSheetUI(
                    onEmojiClick = { emoji ->
                        showIconBottomSheet = false
                        selectedEmoji = emoji.character
                        val updatedContentList = contentList.toMutableList()
                        val currentText = byteArrayToString(updatedContentList[contentList.size - 1].second) + selectedEmoji
                        updatedContentList[contentList.size - 1] = "text" to stringToByteArray(currentText)
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
                },
                containerColor = Color.White

            ) {
                PaletteSelection(
                    color = selectedColorPalette,
                    onColorChange = {
                        selectedColorPalette = it
                    }
                )
            }
        }

        if (showImageBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showImageBottomSheet = !showImageBottomSheet
                }
            ) {
                ImageSelection(
                    selectedImageUri = selectedImageUri,
                    onImageSelected = { uri ->
                        selectedImageUri = uri
                        val pair: Pair<String, ByteArray> = Pair("image", context.contentResolver.openInputStream(
                            selectedImageUri!!
                        )?.readBytes()!!)
                        contentList.add(pair)
                        val temp: Pair<String, ByteArray> = Pair("text", stringToByteArray(""))
                        contentList.add(temp)
                        showImageBottomSheet = !showImageBottomSheet
                    }
                )
            }
        }
    }
}



@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun EditDiaryScreen(
    modifier: Modifier = Modifier,
    diaryId: Int,
    diaryViewModel: DiaryViewModel,
    musicViewModel: MusicViewModel,
    navController: NavHostController,
) {
    LaunchedEffect(diaryId) {
        diaryViewModel.getDiaryById(diaryId)
    }

    val diary by diaryViewModel.selectedDiary.collectAsState()
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var showBottomSheet by remember { mutableStateOf(true) }

    var logo by remember {
        mutableStateOf(diary?.logo ?: 0)
    }

    var datetime by remember {
        mutableStateOf(diary?.createdAt ?: LocalDateTime.now())
    }
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM")
    val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    var openDialog by remember { mutableStateOf(false) }
    var openTimeDialog by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val timePickerState = rememberTimePickerState()
    var musicListString: List<String> = mutableListOf()
    var mood: String = "Fun"

    var title by remember { mutableStateOf(diary?.title ?: "") }
    var content by remember { mutableStateOf(diary?.content ?: "") }

    val diaryStyle = diary!!.diaryStyle
    var isShrink by remember {
        mutableStateOf(false)
    }
    var contentList by remember {
        mutableStateOf(
            loadContentListFromFile(diary!!.contentFilePath)
        )
    }
    if (openDialog) {
        DatePickerDialog(
            onDismissRequest = {
                openDialog = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        openDialog = false
                        openTimeDialog = true
                    },
                    enabled = datePickerState.selectedDateMillis != null
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openDialog = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (openTimeDialog) {
        TimePickerDialog(
            onCancel = {
                openTimeDialog = false
            },
            onConfirm = {
                openTimeDialog = false
                val selectedDateMillis = datePickerState.selectedDateMillis ?: 0
                val selectedHour = timePickerState.hour
                val selectedMinute = timePickerState.minute
                datetime = Instant.ofEpochMilli(selectedDateMillis)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime()
                    .withHour(selectedHour)
                    .withMinute(selectedMinute)
            }
        ) {
            TimePicker(state = timePickerState)
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier,
                colors = TopAppBarDefaults.topAppBarColors().copy(
                    containerColor = diaryStyle.colorPalette
                ),
                navigationIcon = {

                    IconButton(
                        onClick = {
                            navController.popBackStack()
                            MusicHelper.pause()
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
                        text = stringResource(R.string.title_edit_nhat_ky),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                actions = {
                    Row(
                        modifier = Modifier.padding(end = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.width(5.dp))
                        Button(
                            onClick = {
                                val newDiary = diary?.let {
                                    Diary(
                                        diaryId = it.diaryId,
                                        title = title,
                                        content = content,
                                        mood = mood,
                                        logo = logo, // Thay thế R.drawable.logo bằng resource id thích hợp
                                        createdAt = datetime,
                                        diaryStyle = it.diaryStyle,
                                        contentFilePath = saveContentListToFile(context, contentList)

                                    )
                                }
                                diaryViewModel.updateDiary(newDiary!!)
                                navController.navigate(MelodyDiaryApp.DiaryScreen.name)
                                MusicHelper.pause()
                            }
                        ) {
                            Text(
                                text = "Save",
                                color = Color.White
                            )
                        }
                    }
                }
            )
        },

        ) { paddingValue ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(diaryStyle.colorPalette),
            contentPadding = paddingValue
        ) {
            diary?.let {

                item {
                    DateDetailInDiaryWithSelection(
                        date = it.createdAt.format(formatter),
                        time = it.createdAt.format(timeFormatter),
                        thu = DayOfWeekConverter.convertToThu(it.createdAt.dayOfWeek.toString()),
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
                        value = title,
                        placeholder = "Title",
                        onValueChange = {
                            title = it
                        },
                        enable = true,
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
                            fontSize = diaryStyle.fontSize.plus(10.sp)
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
                    } else {

                        ImageContentWrapper(
                            imageByteArray = value,
                            onDeleteClick = {
                                val updatedContentList = contentList.toMutableList()
                                updatedContentList.removeAt(index)
                                contentList = updatedContentList
                            },
                            onShrinkClick = {
                                isShrink = !isShrink
                            }, isShrink = isShrink
                        )
                    }
                }

            }
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.height(300.dp).fillMaxWidth(),
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
                                    mood = "Fun"
                                    showBottomSheet = false
                                    logo = R.drawable.ic_face
                                    scope.launch {
                                        MusicHelper.pause()
                                        musicViewModel.populateMusicList("fun")
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
                                    mood = "Cry"
                                    showBottomSheet = false
                                    logo = R.drawable.ic_cry
                                    scope.launch {
                                        MusicHelper.pause()
                                        musicViewModel.populateMusicList("sadnees, cry music")
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
                                    mood = "Sad"
                                    showBottomSheet = false
                                    logo = R.drawable.ic_neutral
                                    scope.launch {
                                        MusicHelper.pause()
                                        musicViewModel.populateMusicList("sad")
                                        MusicHelper.playSequential(onPlaybackCompleted = {})
                                    }
                                },
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.ic_neutral),
                                    contentDescription = null,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                        item {
                            IconButton(
                                onClick = {
                                    mood = "Fear"
                                    showBottomSheet = false
                                    logo = R.drawable.ic_fear
                                    scope.launch {
                                        MusicHelper.pause()
                                        musicViewModel.populateMusicList("fear")
                                        MusicHelper.playSequential(onPlaybackCompleted = {})
                                    }
                                },
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.ic_fear),
                                    contentDescription = null,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                        item {
                            IconButton(
                                onClick = {
                                    mood = "Disgust"
                                    showBottomSheet = false
                                    logo = R.drawable.ic_disgust
                                    scope.launch {
                                        MusicHelper.pause()
                                        musicViewModel.populateMusicList("disgust")
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
                                    mood = "Angry"
                                    showBottomSheet = false
                                    logo = R.drawable.ic_angry
                                    scope.launch {
                                        MusicHelper.pause()
                                        musicViewModel.populateMusicList("angry")
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
                        TextButton(
                            onClick = {}
                        ) {
                            Text(
                                text = stringResource(R.string.btn_them_tam_trang),
                                color = Color.Blue
                            )
                        }
                        Image(
                            painter = painterResource(R.drawable.ic_groupface),
                            contentDescription = "Group face for new status",
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ToolBar(
    modifier: Modifier = Modifier,
    onFontFormatClick: () -> Unit,
    onIconClick: () -> Unit,
    onImageClick: () -> Unit,
    onPaletteClick: () -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth().background(Color.White).padding(5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        IconButton(
            onClick = onFontFormatClick
        ) {
            Icon(
                painterResource(R.drawable.text_format_24px),
                contentDescription = null
            )
        }
        IconButton(
            onClick = onPaletteClick
        ) {
            Icon(
                painterResource(R.drawable.palette_24px),
                contentDescription = null
            )
        }
        IconButton(
            onClick = onIconClick
        ) {
            Icon(
                painterResource(R.drawable.add_reaction_24px),
                contentDescription = null
            )
        }
        IconButton(
            onClick = onImageClick
        ) {
            Icon(
                painterResource(R.drawable.photo_library_24px),
                contentDescription = null
            )
        }

    }
}
@Composable
fun BorderlessTextField(
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enable: Boolean,
    textStyle: TextStyle = MaterialTheme.typography.titleLarge
) {
    TextField(
        value = value,
        placeholder = {
            Text(
                text = placeholder,
                style = textStyle
            )
        },
        onValueChange = onValueChange,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent,
            cursorColor = Color.Black,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledTextColor = Color.Black,
            disabledIndicatorColor = Color.Transparent
        ),
        textStyle = textStyle,
        modifier = modifier.fillMaxWidth().border(
            width = 0.dp,
            color = Color.Transparent,
            shape = RoundedCornerShape(0.dp)
        ),
        enabled = enable
    )
}

@Composable
fun DiaryTextField(
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enable: Boolean,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium
) {
    TextField(
        value = value,
        placeholder = {
            Text(
                text = placeholder,
                style = textStyle
            )
        },
        onValueChange = onValueChange,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent,
            cursorColor = Color.Black,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledTextColor = Color.Black,
            disabledIndicatorColor = Color.Transparent
        ),
        textStyle = textStyle,
        modifier = modifier.fillMaxWidth(),
        enabled = enable
    )
}


@Composable
fun ImageContentWrapper(
    modifier: Modifier = Modifier,
    imageByteArray: ByteArray,
    isShrink: Boolean = false,
    onDeleteClick: () -> Unit,
    onShrinkClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val bitmap = BitmapFactory.decodeByteArray(
        imageByteArray, 0, imageByteArray.size
    )
    bitmap.asImageBitmap()
    Card(
        modifier = if (!isShrink) {
            modifier
                .fillMaxWidth()
                .padding(16.dp)
        } else {
            modifier
                .padding(16.dp)
                .size(200.dp)
        },
        shape = RoundedCornerShape(20.dp)
    ) {
        Box(
            modifier = Modifier.aspectRatio(1.0f)
                .fillMaxWidth()
        ) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
            )

            Column(
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                IconButton(
                    onClick = { expanded = true },
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(50))
                        .background(Color.LightGray)
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = null
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .padding(end = 8.dp)
                ) {
                    DropdownMenuItem(onClick = {
                        expanded = false
                        onDeleteClick()
                    }) {
                        Text(text = "Delete")
                    }
                    DropdownMenuItem(onClick = {
                        expanded = false
                        onShrinkClick()
                    }) {
                        Text(text = if (!isShrink) "Shrink" else "Expand")
                    }
                }
            }
        }
    }
}


@Composable
fun TimePickerDialog(
    title: String = "Select Time",
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    toggle: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onCancel,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp,
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min)
                .background(
                    shape = MaterialTheme.shapes.extraLarge,
                    color = MaterialTheme.colorScheme.surface
                ),
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    text = title,
                    style = MaterialTheme.typography.labelMedium
                )
                content()
                Row(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                ) {
                    toggle()
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(
                        onClick = onCancel
                    ) { Text("Cancel") }
                    TextButton(
                        onClick = onConfirm
                    ) { Text("OK") }
                }
            }
        }
    }
}
@Composable
fun DateDetailInDiaryWithSelection(
    date: String,
    time: String,
    thu: String,
    @DrawableRes statusLogoRes: Int,
    onPickEmoteClick: () -> Unit,
    onDateTimePickerClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(20.dp),
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
        Spacer(modifier = Modifier.width(5.dp))
        IconButton(
            onClick = onDateTimePickerClick,
            enabled = enabled
        ) {
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(
            onClick = onPickEmoteClick,
            enabled = enabled
        ) {
            Image(
                painter = painterResource(statusLogoRes),
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMusicScreen() {
    val diaryViewModel: DiaryViewModel = viewModel(factory = DiaryViewModel.Factory)
    MelodyDiaryTheme {
//        AddDiaryScreen(
//            diaryViewModel = diaryViewModel
//        )
    }
}