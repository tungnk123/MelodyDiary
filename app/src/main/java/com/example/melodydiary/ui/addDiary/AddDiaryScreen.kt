package com.example.melodydiary.ui.addDiary

import android.os.Build
import android.widget.GridLayout
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
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
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.melodydiary.data.DiaryRepository
import com.example.melodydiary.model.Diary
import com.example.melodydiary.ui.diary.DiaryViewModel
import com.example.melodydiary.ui.theme.MelodyDiaryTheme
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun AddDiaryScreen(
    modifier: Modifier = Modifier,
    diaryViewModel: DiaryViewModel
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

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.padding(end = 20.dp),
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null
                    )
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
                            onClick = {}
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
                                    logo = R.drawable.ic_fear, // Thay thế R.drawable.logo bằng resource id thích hợp
                                    createdAt = LocalDateTime.now() // Sử dụng thời gian hiện tại
                                )
                                diaryViewModel.addDiary(newDiary)
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
                date = "3/10",
                time = "12:30",
                thu = "Thu 3",
                R.drawable.ic_pick,
                onPickEmoteClick = {
                    showBottomSheet = !showBottomSheet
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
                                onClick = {},
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
                                onClick = {},
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
                                onClick = {},
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
                                onClick = {},
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
                                onClick = {},
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
                                onClick = {},
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
                    Button(
                        onClick = {
                                  showBottomSheet = false
                        },
                        modifier = Modifier.width(150.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.btn_xac_nhan),
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White
                        )
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
                            contentDescription = "Group face for new status"
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
fun DateDetailInDiaryWithSelection(
    date: String,
    time: String,
    thu: String,
    @DrawableRes statusLogoRes: Int,
    onPickEmoteClick: () -> Unit,
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
            onClick = {}
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