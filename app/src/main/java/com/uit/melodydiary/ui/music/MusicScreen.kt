package com.uit.melodydiary.ui.music


import MusicHelper
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.uit.melodydiary.R
import com.uit.melodydiary.model.Album
import com.uit.melodydiary.model.Diary
import com.uit.melodydiary.model.MusicSmall
import com.uit.melodydiary.ui.diary.DiaryViewModel
import com.uit.melodydiary.ui.theme.MelodyDiaryTheme
import com.uit.melodydiary.ui.theme.musicItemColor
import com.uit.melodydiary.ui.theme.mygreen
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicScreen(
    modifier: Modifier = Modifier,
    musicViewModel: MusicViewModel,
    navController: NavController
) {
    val diaryViewModel: DiaryViewModel = viewModel(factory = DiaryViewModel.Factory)
    diaryViewModel.getDiaryFromDatabase()

    musicViewModel.getAllAlbum()
    val albumList = musicViewModel.albumList.collectAsState()
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

        DiaryTab(
            modifier = Modifier.padding(innerPadding),
            musicViewModel,
            diaryViewModel,
            albumList = albumList.value
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DiaryTab(
    modifier: Modifier = Modifier,
    musicViewModel: MusicViewModel,
    diaryViewModel: DiaryViewModel,
    albumList: List<Album>
) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    var musicList by remember { mutableStateOf(mutableListOf<MusicSmall>()) }
    var musicListFromDB by remember { mutableStateOf(listOf<MusicSmall>()) }
    val diaryList = diaryViewModel.diaryList.collectAsState()
    val scope = rememberCoroutineScope()
    var selectedAlbum by remember { mutableStateOf<Album?>(null) }
    var selectedGiaiDieu by remember {
        mutableStateOf("Choose")
    }
    var selectedNhacCu by remember {
        mutableStateOf("Choose")
    }
    var selectedMusicSmall by remember {
        mutableStateOf(
            MusicSmall(
                title = "Melody",
                albumId = 0
            )
        )
    }
    var isAlbumSelectionDialogVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current
    LaunchedEffect(isAlbumSelectionDialogVisible) {
        musicViewModel.getAllMusic()
    }
    Column(
        modifier = modifier.fillMaxSize()
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
                onClick = {
                    selectedTabIndex = 1
                    // Reset selectedAlbum when switching to PlaylistTab
                    selectedAlbum = null
                },
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
                GenMusicTab(
                    musicList = musicList,
                    onTaoNhacClick = {
                        if (musicViewModel.currentDiary.title == "Chọn") {
                            Toast.makeText(
                                context,
                                "Please select a diary to gen music!",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            scope.launch {
                                Log.i(
                                    "giai_dieu",
                                    musicViewModel.currentDiary.content + ":" + selectedGiaiDieu + ":" + selectedNhacCu
                                )
                                val genString =
                                    "Đây là một bài nhạc có giai điệu $selectedGiaiDieu, nhạc cụ $selectedNhacCu và có nội dung là $musicViewModel.currentDiary.content"
                                val result = musicViewModel.fetchMusic(genString)
                                val size = musicList.size + 1
                                musicList = musicList.toMutableList()
                                    .apply { add(MusicSmall(title = "Melody $size", url = result)) }
                            }
                        }
                    },
                    onXuatBanClick = {
                        if (selectedMusicSmall.title == "Melody") {
                            Toast.makeText(
                                context,
                                "Please select a music to expose!",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            isAlbumSelectionDialogVisible = true
                        }
                    },
                    diaryList = diaryList.value.sortedByDescending { it.createdAt },
                    selectedGiaiDieu = selectedGiaiDieu,
                    selectedNhacCu = selectedNhacCu,
                    onSelectedGiaiDieuChange = {
                        selectedGiaiDieu = it
                    },
                    onSelectedNhacCuChange = {
                        selectedNhacCu = it
                    },
                    musicViewModel = musicViewModel,
                    selectedMusicSmall = selectedMusicSmall,
                    onSelectedMusicChange = {
                        selectedMusicSmall = it
                    }
                )
            }

            1 -> {
                PlaylistTab(
                    modifier = Modifier.weight(1f),
                    musicViewModel = musicViewModel,
                    albumList = albumList,
                    showAlbumList = true,
                    selectedAlbum = selectedAlbum,
                    onAlbumSelected = { album -> selectedAlbum = album }
                )
            }
        }

        selectedAlbum?.let { album ->
            AlbumDetailScreen(
                album = album,
                onClose = { selectedAlbum = null },
                selectedMusicSmall = selectedMusicSmall,
                onSelectedMusicChange = {
                    selectedMusicSmall = it
                },
                musicList = musicViewModel.musicSmallList.filter { it.albumId == album.albumId }
            )
        }
        if (isAlbumSelectionDialogVisible) {
            AlbumSelectionDialog(
                albumList = albumList,
                onAlbumSelected = { album ->
                    selectedMusicSmall.albumId = album.albumId

                    musicViewModel.insertMusic(selectedMusicSmall)
                    Toast.makeText(context, "Exposed to ${album.title}", Toast.LENGTH_SHORT)
                        .show()
                    isAlbumSelectionDialogVisible = false
                },
                onDismissRequest = { isAlbumSelectionDialogVisible = false }
            )
        }
    }
}

@Composable
fun AlbumSelectionDialog(
    albumList: List<Album>,
    onAlbumSelected: (Album) -> Unit,
    onDismissRequest: () -> Unit
) {
    var selectedAlbum by remember { mutableStateOf<Album?>(null) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                text = stringResource(R.string.text_select_album),
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            LazyColumn {
                items(albumList) { album ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedAlbum = album }
                            .background(if (album == selectedAlbum) Color.Gray else Color.Transparent)
                            .padding(8.dp)
                    ) {
                        Text(
                            text = album.title,
                            fontWeight = if (album == selectedAlbum) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    selectedAlbum?.let { onAlbumSelected(it) }
                    onDismissRequest()
                },
                enabled = selectedAlbum != null,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = mygreen
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(text = stringResource(R.string.btn_xac_nhan), color = Color.Black)
            }
        },
        dismissButton = {
            Button(
                onClick = onDismissRequest,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Red
                ),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.padding(bottom = 10.dp)
            ) {
                Text(
                    text = stringResource(R.string.btn_close),
                    color = Color.White
                )
            }
        }
    )
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GenMusicTab(
    modifier: Modifier = Modifier,
    musicList: List<MusicSmall>,
    onTaoNhacClick: () -> Unit,
    onXuatBanClick: () -> Unit,
    diaryList: List<Diary>,
    selectedGiaiDieu: String,
    selectedNhacCu: String,
    onSelectedGiaiDieuChange: (String) -> Unit,
    onSelectedNhacCuChange: (String) -> Unit,
    selectedMusicSmall: MusicSmall,
    onSelectedMusicChange: (MusicSmall) -> Unit,
    musicViewModel: MusicViewModel
) {
    Column(
        modifier = modifier
    ) {
        GenMusicWrapper(
            onTaoNhacClick = onTaoNhacClick,
            onXuatBanClick = onXuatBanClick,
            diaryList = diaryList,
            selectedGiaiDieu = selectedGiaiDieu,
            selectedNhacCu = selectedNhacCu,
            onSelectedGiaiDieuChange = {
                onSelectedGiaiDieuChange(it)
            },
            onSelectedNhacCuChange = {
                onSelectedNhacCuChange(it)
            },
            musicViewModel = musicViewModel
        )
        MusicList(
            musicList = musicList,
            selectedItem = selectedMusicSmall,
            onSelectedItemChange = onSelectedMusicChange
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun MyDropDown(
    list: List<String>,
    selectedItem: String = "Choose",
    onSelectedItemChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var canWrite by remember {
        mutableStateOf(false)
    }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = it
        },
        modifier = modifier.clip(RoundedCornerShape(8.dp))
    ) {
        TextField(
            readOnly = !canWrite,
            value = selectedItem,
            onValueChange = {
                onSelectedItemChange(it)
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier.menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            list.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Text(text = item)
                    },
                    onClick = {
                        onSelectedItemChange(item)
                        expanded = false
                        if (item == "Other") {
                            canWrite = true
                        } else {
                            canWrite = false
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun GenMusicWrapper(
    modifier: Modifier = Modifier,
    onTaoNhacClick: () -> Unit,
    onXuatBanClick: () -> Unit,
    diaryList: List<Diary>,
    selectedGiaiDieu: String,
    selectedNhacCu: String,
    onSelectedGiaiDieuChange: (String) -> Unit,
    onSelectedNhacCuChange: (String) -> Unit,
    musicViewModel: MusicViewModel
) {
    val theLoaiList: MutableList<String> = mutableListOf(
        "Pop Music",
        "Rock Music",
        "Jazz Music",
        "Country Music",
        "Electronic Music",
        "Other"
    )

    val nhacCuList: MutableList<String> = mutableListOf(
        "Guitar",
        "Piano",
        "Violin",
        "Drums",
        "Flute",
        "Other"
    )
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(5.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(15.dp)
            )
    ) {
        PickDiary(
            onClickChooseDiary = {},
            list = diaryList,
            musicViewModel = musicViewModel
        )
        Row(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 5.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.title_the_loai),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.width(10.dp))
            MyDropDown(
                list = theLoaiList,
                selectedItem = selectedGiaiDieu,
                onSelectedItemChange = {
                    onSelectedGiaiDieuChange(it)
                }
            )
        }
        Row(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 5.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.title_nhac_cu),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.width(10.dp))
            MyDropDown(
                list = nhacCuList,
                selectedItem = selectedNhacCu,
                onSelectedItemChange = {
                    onSelectedNhacCuChange(it)
                }
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = onTaoNhacClick,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = stringResource(R.string.title_tao_nhac),
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.width(20.dp))
            Button(
                onClick = onXuatBanClick,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = mygreen
                ),
                shape = RoundedCornerShape(20.dp),
            ) {
                Text(
                    text = stringResource(R.string.title_xuat_ban),
                    color = Color.Black
                )
            }
        }

    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun PickDiary(
    onClickChooseDiary: () -> Unit,
    list: List<Diary>,
    musicViewModel: MusicViewModel,
    modifier: Modifier = Modifier
) {
    val selectedDiary = remember {
        mutableStateOf(
            Diary(
                diaryId = 0,
                title = "Other",
                content = "Content",
                createdAt = LocalDateTime.now(),
                logo = R.drawable.ic_face,
                mood = "fun",
                contentFilePath = ""
            )
        )
    }

    var expanded by remember { mutableStateOf(false) }
    var canExpandDropdown by remember { mutableStateOf(true) }

    canExpandDropdown = selectedDiary.value.diaryId == 0
    ExposedDropdownMenuBox(
        expanded = expanded && canExpandDropdown,
        onExpandedChange = {
            expanded = it
        },
        modifier = modifier.clip(RoundedCornerShape(8.dp))
            .padding(horizontal = 20.dp, vertical = 10.dp)
    ) {

        Card(
            modifier = modifier.fillMaxWidth()
                .height(80.dp)
                .menuAnchor(),
            shape = RoundedCornerShape(
                topEnd = 5.dp,
                bottomStart = 30.dp,
                topStart = 5.dp,
                bottomEnd = 30.dp
            ),
            elevation = CardDefaults.cardElevation(
                2.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = Color.White,
            ),
            border = BorderStroke(2.dp, Color.Blue)
        ) {
            if (selectedDiary.value.diaryId == 0) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painterResource(R.drawable.ic_choose),
                        contentDescription = null,
                        modifier = Modifier.size(50.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = stringResource(R.string.title_chon_nhat_ky),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            } else {
                DiaryMenuItemWithClose(diary = selectedDiary.value,
                    onCloseClick = {
                        selectedDiary.value = selectedDiary.value.copy(diaryId = 0)
                    })
            }
        }
        ExposedDropdownMenu(
            modifier = Modifier.fillMaxWidth(),
            expanded = expanded && canExpandDropdown,
            onDismissRequest = {
                expanded = false
            }
        ) {
            list.forEach { item ->
                DropdownMenuItem(
                    text = {
                        DiaryMenuItem(diary = item)
                    },
                    onClick = {
                        selectedDiary.value = item
                        musicViewModel.setSelectedDiary(selectedDiary.value)
                        expanded = false
                    }
                )
            }
        }
    }
}


@Composable
fun DiaryMenuItem(
    diary: Diary,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(diary.logo),
            contentDescription = diary.title,
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            diary.title
        )
    }
}

@Composable
fun DiaryMenuItemWithClose(
    diary: Diary,
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize().background(mygreen)
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(diary.logo),
                contentDescription = diary.title,
                modifier = Modifier.size(32.dp).padding(start = 10.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                diary.title,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = onCloseClick,
            ) {
                androidx.compose.material3.Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun MusicList(
    modifier: Modifier = Modifier,
    musicList: List<MusicSmall>,
    selectedItem: MusicSmall,
    onSelectedItemChange: (MusicSmall) -> Unit
) {
    if (musicList.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.text_danh_sach_giai_dieu_trong),
                style = MaterialTheme.typography.titleMedium
            )
        }
    } else {
        LazyColumn {
            items(musicList) {
                var isPlayState by remember {
                    mutableStateOf(it.isPlay)
                }

                MusicItem(musicItem = it,
                    onClickPlay = {
                        isPlayState = true
                        MusicHelper.togglePlayback(it) {
                            isPlayState = false
                        }
                    }, onClickPause = {
                        isPlayState = false
                    },
                    isPlay = isPlayState,
                    isSelected = selectedItem == it,
                    onItemSelected = {
                        onSelectedItemChange(it)
                    }
                )
            }
        }
    }


}

@Composable
fun MusicItem(
    musicItem: MusicSmall,
    isPlay: Boolean,
    onClickPlay: () -> Unit,
    onClickPause: () -> Unit,
    isSelected: Boolean,
    onItemSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    var progress by remember {
        mutableFloatStateOf(0f)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp),
        onClick = {
            onItemSelected()
        },
        shape = RoundedCornerShape(
            topEnd = 20.dp,
            bottomStart = 20.dp,
            topStart = 5.dp,
            bottomEnd = 5.dp
        ),
        border = if (isSelected) BorderStroke(2.dp, Color.Blue) else ButtonDefaults.outlinedBorder,
        elevation = CardDefaults.cardElevation(
            2.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = musicItemColor,
        )
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_music),
                    contentDescription = null,
                    modifier = Modifier.size(50.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Box(
                    modifier = Modifier.width(2.dp).height(32.dp).background(Color.Black)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = musicItem.title,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                if (isPlay) {
                    IconButton(
                        onClick = onClickPause
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_pause),
                            contentDescription = null,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                } else {
                    progress++
                    IconButton(
                        onClick = onClickPlay
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_play),
                            contentDescription = null,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }

            }
//            Spacer(modifier = Modifier.height(10.dp))

//            CustomSeekBar(
//                progress = progress,
//                maxProgress = musicItem.duration,
//                onProgressChange = {
//                    progress
//                }
//            )


        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomSeekBar(
    progress: Float,
    maxProgress: Float,
    onProgressChange: (Float) -> Unit
) {
    var sliderPosition by remember { mutableStateOf(progress) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(8.dp)
            .background(Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(sliderPosition)
                .height(8.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(4.dp)
                )
        )
        Slider(
            value = sliderPosition,
            onValueChange = {
                sliderPosition = it
                onProgressChange(it)
            },
            valueRange = 0f..maxProgress,
            modifier = Modifier.padding(0.dp),
            colors = SliderDefaults.colors(
                thumbColor = Color.Black,
                activeTrackColor = Color.Black,
                inactiveTrackColor = Color.LightGray,
            ),
            thumb = {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(Color.Black, RoundedCornerShape(percent = 50))
                        .align(Alignment.Center)

                )
            }
        )
    }
}

@Composable
fun AddAlbum(
    modifier: Modifier = Modifier,
    onAddAlbumClick: () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth()
            .height(80.dp)
            .clickable {
                onAddAlbumClick()
            },
        shape = RoundedCornerShape(
            topEnd = 5.dp,
            bottomStart = 30.dp,
            topStart = 5.dp,
            bottomEnd = 30.dp
        ),
        elevation = CardDefaults.cardElevation(
            2.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        border = BorderStroke(2.dp, Color.Blue)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painterResource(R.drawable.ic_add),
                contentDescription = null,
                modifier = Modifier.size(50.dp)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = stringResource(R.string.text_them_album),
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Composable
fun PlaylistTab(
    modifier: Modifier = Modifier,
    musicViewModel: MusicViewModel,
    albumList: List<Album>,
    showAlbumList: Boolean,
    selectedAlbum: Album? = null,
    onAlbumSelected: (Album) -> Unit
) {

    var isCreateAlbumDialogOpen by remember { mutableStateOf(false) }

    if (showAlbumList) {
        Column(
            modifier = modifier.fillMaxSize().padding(20.dp)
        ) {
            AddAlbum(
                onAddAlbumClick = {
                    isCreateAlbumDialogOpen = true
                }
            )
            AlbumItemList(
                albumList = albumList,
                onItemClick = { album -> onAlbumSelected(album) }
            )
            CreateAlbumScreen(
                onAlbumCreated = {
                    musicViewModel.insertAlbum(it)
                },
                isCreateAlbumDialogOpen = isCreateAlbumDialogOpen,
                onCloseDialog = {
                    isCreateAlbumDialogOpen = false
                }
            )
        }
    } else {
//        selectedAlbum?.let {
//            AlbumDetailScreen(
//                album = it,
//                onClose = { selectedAlbum = null },
//                selectedMusicSmall = selectedMusicSmall,
//                onSelectedMusicChange = {
//
//                }
//            )
//        }
    }
}

@Composable
fun AlbumItemList(
    albumList: List<Album>,
    onItemClick: (Album) -> Unit,
    modifier: Modifier = Modifier
) {

    if (albumList.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.text_danh_sach_album_trong),
                style = MaterialTheme.typography.titleMedium
            )
        }
    } else {
        LazyColumn {
            items(albumList) { album ->
                AlbumItem(
                    album = album,
                    onItemClick = { onItemClick(album) }
                )
            }
        }


    }

}

@Composable
fun AlbumItem(
    album: Album,
    modifier: Modifier = Modifier,
    onItemClick: (Album) -> Unit
) {
    val bitmap = BitmapFactory.decodeByteArray(
        album.logo, 0, album.logo.size
    )
    Card(
        modifier = modifier.fillMaxWidth().padding(top = 10.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        onClick = { onItemClick(album) }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.width(122.dp).height(90.dp),
                contentScale = ContentScale.Crop
            )

            Column {
                Text(
                    text = album.title,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = album.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }

        }
    }
}

@Composable
fun AlbumDetailScreen(
    album: Album,
    musicList: List<MusicSmall>,
    onClose: () -> Unit,
    selectedMusicSmall: MusicSmall,
    onSelectedMusicChange: (MusicSmall) -> Unit
) {
    val bitmap = BitmapFactory.decodeByteArray(
        album.logo, 0, album.logo.size
    )
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(
                onClick = onClose,
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null
                )
            }
        }

        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = null,
            modifier = Modifier.width(122.dp).height(90.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = album.title,
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = "${album.count} đoạn nhạc",
            style = MaterialTheme.typography.titleMedium
        )

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    Toast.makeText(context, "Feature is under construction", Toast.LENGTH_SHORT)
                        .show()
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = stringResource(R.string.btn_phat_tuan_tu),
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.width(20.dp))
            Button(
                onClick = {
                    Toast.makeText(context, "Feature is under construction", Toast.LENGTH_SHORT)
                        .show()
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = mygreen
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = stringResource(R.string.btn_phat_ngau_nhien),
                    color = Color.Black
                )
            }
        }

        MusicList(
            musicList = musicList,
            selectedItem = selectedMusicSmall,
            onSelectedItemChange = onSelectedMusicChange
        )
    }
}

@Composable
fun CreateAlbumScreen(
    onAlbumCreated: (Album) -> Unit,
    isCreateAlbumDialogOpen: Boolean,
    onCloseDialog: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    if (isCreateAlbumDialogOpen) {
        AlertDialog(
            onDismissRequest = {
                onCloseDialog()
                title = ""
                description = ""
                selectedImageUri = null
            },
            modifier = Modifier.padding(bottom = 10.dp),
            title = { Text("Create Album", style = MaterialTheme.typography.titleLarge) },
            confirmButton = {
                Button(
                    onClick = {
                        if (title.isNotBlank() && description.isNotBlank() && selectedImageUri != null) {
                            val newAlbum = Album(
                                albumId = 0,
                                logo = context.contentResolver.openInputStream(
                                    selectedImageUri!!
                                )?.readBytes()!!,
                                title = title,
                                description = description
                            )
                            onAlbumCreated(newAlbum)
                        }
                        onCloseDialog()
                        title = ""
                        description = ""
                        selectedImageUri = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = mygreen
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        text = stringResource(R.string.btn_xac_nhan),
                        color = Color.Black
                    )
                }

            },
            dismissButton = {
                Button(
                    onClick = {
                        onCloseDialog()
                        title = ""
                        description = ""
                        selectedImageUri = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Red
                    ),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.padding(bottom = 10.dp)
                ) {
                    Text(
                        text = stringResource(R.string.btn_close),
                        color = Color.White
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .padding(vertical = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ImageSelector(
                        selectedImageUri = selectedImageUri,
                        onImageSelected = { uri ->
                            selectedImageUri = uri
                        }
                    )

                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Title") }
                    )

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description") }
                    )

                }
            }
        )
    }
}


@Composable
fun ImageSelector(
    selectedImageUri: Uri?,
    onImageSelected: (Uri) -> Unit
) {
    val photoPikcerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            onImageSelected(it!!)
        }
    )
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            text = "Choose logo for album"
        )
        if (selectedImageUri == null) {
            Button(
                onClick = {
                    photoPikcerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                )
            ) {
                Text("Choose logo", color = Color.White)
            }
        } else {
            AsyncImage(
                model = selectedImageUri,
                contentDescription = null,
                modifier = Modifier.size(100.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMusicScreen() {
    MelodyDiaryTheme {
        val musicViewModel: MusicViewModel = viewModel(factory = MusicViewModel.Factory)
        MusicScreen(musicViewModel = musicViewModel, navController = rememberNavController())
    }
}