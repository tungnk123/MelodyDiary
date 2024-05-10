package com.example.melodydiary.ui.music


import android.os.Build
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
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.melodydiary.R
import com.example.melodydiary.model.Album
import com.example.melodydiary.model.Diary
import com.example.melodydiary.model.MusicSmall
import com.example.melodydiary.ui.diary.DiaryViewModel
import com.example.melodydiary.ui.theme.MelodyDiaryTheme
import com.example.melodydiary.ui.theme.musicItemColor
import com.example.melodydiary.ui.theme.mygreen
import com.example.melodydiary.utils.togglePlayback
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

        DiaryTab(modifier = Modifier.padding(innerPadding), musicViewModel, diaryViewModel)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DiaryTab(
    modifier: Modifier = Modifier,
    musicViewModel: MusicViewModel,
    diaryViewModel: DiaryViewModel
) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    var musicList by remember {
        mutableStateOf(mutableListOf<MusicSmall>())
    }

    val diaryList = diaryViewModel.diaryList.collectAsState()
    val scope = rememberCoroutineScope()
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
                    text = stringResource(R.string.title_tao_nhac),
                    style = MaterialTheme.typography.labelLarge
                )
            }
            Tab(
                selected = selectedTabIndex == 1,
                onClick = { selectedTabIndex = 1 },
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
                        scope.launch {
                            val result = musicViewModel.fetchMusic("fun")
                            val size = musicList.size + 1
                            musicList = musicList.toMutableList()
                                .apply { add(MusicSmall(title = "Giai điệu $size", url = result)) }
                        }
                    },
                    onXuatBanClick = {

                    },
                    diaryList = diaryList.value.sortedByDescending { it.createdAt }
                )
            }
            1 -> {
                PlaylistTab(
                    musicViewModel = musicViewModel
                )
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GenMusicTab(
    modifier: Modifier = Modifier,
    musicList: List<MusicSmall>,
    onTaoNhacClick: () -> Unit,
    onXuatBanClick: () -> Unit,
    diaryList: List<Diary>
) {
    Column(
        modifier = modifier
    ) {
        GenMusicWrapper(
            onTaoNhacClick = onTaoNhacClick,
            onXuatBanClick = onXuatBanClick,
            diaryList = diaryList
        )
        MusicList(
            musicList = musicList
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun MyDropDown(
    list: List<String>,
    selectedItem: String = "Chọn",
    modifier: Modifier = Modifier
) {
    val selectedItem = remember { mutableStateOf("Chọn") }
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
            value = selectedItem.value,
            onValueChange = {
                selectedItem.value = it
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
                        selectedItem.value = item
                        expanded = false
                        if (item == "Tùy chọn") {
                            canWrite = true
                        }
                        else {
                            canWrite = false
                        }
                    }
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun GenMusicWrapper(
    modifier: Modifier = Modifier,
    onTaoNhacClick: () -> Unit,
    onXuatBanClick: () -> Unit,
    diaryList: List<Diary>
) {
    val theLoaiList: MutableList<String> = mutableListOf(
        "Nhạc pop",
        "Nhạc rock",
        "Nhạc jazz",
        "Nhạc đồng quê",
        "Nhạc điện tử",
        "Tùy chọn"
    )

    val nhacCuList: MutableList<String> = mutableListOf(
        "Guitar",
        "Piano",
        "Violin",
        "Trống",
        "Sáo",
        "Tùy chọn"
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
            list = diaryList
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
                list = theLoaiList
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
                list = nhacCuList
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
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = stringResource(R.string.title_xuat_ban),
                    color = Color.Black
                )
            }
        }

    }
}


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun PickDiary(
    onClickChooseDiary: () -> Unit,
    list: List<Diary>,
    modifier: Modifier = Modifier
) {
    val selectedDiary = remember {
        mutableStateOf(
            Diary(
                diaryId = 0,
                title = "Chọn",
                content = "Content",
                createdAt = LocalDateTime.now(),
                logo = R.drawable.ic_face,
                mood = "fun",
                imageIdList = listOf()

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
    musicList: List<MusicSmall>
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
    }
    else {
        LazyColumn {
            items(musicList) {
                MusicItem(musicItem = it,
                    onClickPlay = {
                        togglePlayback(it.url)
                    }, onClickPause = {

                    })
            }
        }
    }


}

@Composable
fun MusicItem(
    musicItem: MusicSmall,
    onClickPlay: () -> Unit,
    onClickPause: () -> Unit,
    modifier: Modifier = Modifier
) {
    var progress by remember {
        mutableFloatStateOf(0f)
    }
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp),
        shape = RoundedCornerShape(
            topEnd = 20.dp,
            bottomStart = 20.dp,
            topStart = 5.dp,
            bottomEnd = 5.dp
        ),
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
                if (musicItem.isPlay) {
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
                onAddAlbumClick
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
) {
    val albumList: List<Album> = mutableListOf(
        Album(
            title = "Minecraft",
            description = "Đây là playlist nhạc chơi game",
            logo = R.drawable.ic_minecraft
        ), Album(
            title = "Minecraft",
            description = "Đây là playlist nhạc chơi game",
            logo = R.drawable.ic_minecraft
        )
    )
    Column(
        modifier = modifier.fillMaxSize().padding(20.dp)
    ) {
        AddAlbum(
            onAddAlbumClick = {}
        )
        AlbumItemList(
            albumList = albumList
        )
    }
}

@Composable
fun AlbumItemList(
    albumList: List<Album>,
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
            items(albumList) {
                AlbumItem(
                    logo = it.logo,
                    title = it.title,
                    description = it.description,
                    onAlbumItemClick = {}
                )
            }
        }
    }

}

@Composable
fun AlbumItem(
    logo: Int,
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    onAlbumItemClick: () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth().padding(top = 10.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Image(
                painter = painterResource(logo),
                contentDescription = null,
                modifier = Modifier.width(122.dp).height(90.dp)
            )
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }

        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PreviewMusicScreen() {
    MelodyDiaryTheme {
        val musicViewModel: MusicViewModel = viewModel(factory = MusicViewModel.Factory)
        MusicScreen(musicViewModel = musicViewModel, navController = rememberNavController())
    }
}