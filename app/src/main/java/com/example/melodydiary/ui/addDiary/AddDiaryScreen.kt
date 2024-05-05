package com.example.melodydiary.ui.addDiary


import android.app.TimePickerDialog
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
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
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.melodydiary.MelodyDiaryApp
import com.example.melodydiary.R
import com.example.melodydiary.model.Diary
import com.example.melodydiary.ui.diary.DiaryViewModel
import com.example.melodydiary.ui.theme.MelodyDiaryTheme
import com.example.melodydiary.utils.DayOfWeekConverter
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun AddDiaryScreen(
    modifier: Modifier = Modifier,
    diaryViewModel: DiaryViewModel,
    navController: NavHostController
) {

    var title by remember {
        mutableStateOf("")
    }
    var content by remember {
        mutableStateOf("")
    }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
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
    var datePickerState = rememberDatePickerState()
    var timePickerState = rememberTimePickerState()

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
                modifier = Modifier.padding(end = 20.dp, start = 10.dp),
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
                        text = stringResource(R.string.title_viet_nhat_ky),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                actions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {

                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = null
                            )
                        }
                        Spacer(modifier = Modifier.width(5.dp))
                        Button(
                            onClick = {
                                val newDiary = Diary(
                                    diaryId = 0,
                                    title = title,
                                    content = content,
                                    mood = "Happy",
                                    imageIdList = listOf("image1", "image2"),
                                    logo = logo, // Thay thế R.drawable.logo bằng resource id thích hợp
                                    createdAt = datetime
                                )
                                diaryViewModel.addDiary(newDiary)
                                navController.navigate(MelodyDiaryApp.DiaryScreen.name)
                            }
                        ) {
                            Text(
                                text = "Lưu",
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
                }
            )
            BorderlessTextField(
                value = title,
                placeholder = "Tiêu đề",
                onValueChange = {
                    title = it
                }
            )

            DiaryTextField(
                value = content,
                placeholder = "Bắt đầu viết ...",
                onValueChange = {
                    content = it
                }
            )

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
                                    showBottomSheet = false
                                    logo = R.drawable.ic_face
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
                                    showBottomSheet = false
                                    logo = R.drawable.ic_cry
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
                                    showBottomSheet = false
                                    logo = R.drawable.ic_neutral
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
                                    showBottomSheet = false
                                    logo = R.drawable.ic_fear
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
                                    showBottomSheet = false
                                    logo = R.drawable.ic_disgust
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
                                    showBottomSheet = false
                                    logo = R.drawable.ic_angry
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
fun BorderlessTextField(
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        placeholder = {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.titleLarge
            )
        },
        onValueChange = onValueChange,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent,
            cursorColor = Color.Black,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        textStyle = MaterialTheme.typography.titleLarge,
        modifier = modifier.fillMaxWidth().border(
            width = 0.dp,
            color = Color.Transparent,
            shape = RoundedCornerShape(0.dp)
        )
    )
}

@Composable
fun DiaryTextField(
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        placeholder = {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        onValueChange = onValueChange,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent,
            cursorColor = Color.Black,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        textStyle = MaterialTheme.typography.bodyMedium,
        modifier = modifier.fillMaxWidth().border(
            width = 0.dp,
            color = Color.Transparent,
            shape = RoundedCornerShape(0.dp)
        )
    )
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
            onClick = onDateTimePickerClick
        ) {
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(
            onClick = onPickEmoteClick,
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