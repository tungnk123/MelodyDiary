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
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TextField
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.melodydiary.R
import com.example.melodydiary.model.Music
import com.example.melodydiary.ui.theme.MelodyDiaryTheme
import com.example.melodydiary.ui.theme.musicItemColor
import com.example.melodydiary.ui.theme.mygreen

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicScreen(
    modifier: Modifier = Modifier
) {
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

        DiaryTab(modifier = Modifier.padding(innerPadding))
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DiaryTab(
    modifier: Modifier = Modifier,
) {
    var selectedTabIndex by remember { mutableStateOf(0) }

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
                GenMusicTab()
            }
            1 -> {

            }
        }
    }
}


@Composable
fun GenMusicTab(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        GenMusicWrapper(
        )
        MusicList(
            musicList = mutableListOf(Music("Giai điệu 1"), Music("Giai điệu 2"))
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun MyDropDown(
    list: List<String>,
    modifier: Modifier = Modifier
) {
    val selectedItem = remember { mutableStateOf("Chọn") }
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = it
        },
        modifier = modifier.clip(RoundedCornerShape(8.dp))
    ) {
        TextField(
            value = selectedItem.value,
            onValueChange = {},
            readOnly = true,
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
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun GenMusicWrapper(
    modifier: Modifier = Modifier
) {
    val theLoaiList: List<String> = mutableListOf("Thể loại 1", "Thể loại 2", "Thể loại 3")
    val nhacCuList: List<String> = mutableListOf("Nhạc cụ 1", "Nhạc cụ 2", "Nhạc cụ 3")

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
            onClickChooseDiary = {}
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
                onClick = {},
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
                onClick = {},
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


@Composable
fun PickDiary(
    onClickChooseDiary: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth()
            .padding(20.dp)
            .height(80.dp)
            .clickable {
                onClickChooseDiary
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
                text = stringResource(R.string.title_chon_nhat_ky),
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Composable
fun MusicList(
    modifier: Modifier = Modifier,
    musicList: List<Music>
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
                MusicItem(musicItem = it, onClickPlay = {})
            }
        }
    }


}

@Composable
fun MusicItem(
    musicItem: Music,
    onClickPlay: () -> Unit,
    modifier: Modifier = Modifier
) {
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
                    Image(
                        painter = painterResource(R.drawable.ic_pause),
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                } else {
                    Image(
                        painter = painterResource(R.drawable.ic_play),
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                }

            }
            Spacer(modifier = Modifier.height(10.dp))
            CustomSeekBar(
                progress = 0f,
                onProgressChange = {}
            )


        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomSeekBar(
    progress: Float,
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

@Preview(showBackground = true)
@Composable
fun PreviewMusicItem() {
    MelodyDiaryTheme {
        MusicItem(
            musicItem = Music(title = "Giai điệu 1"),
            onClickPlay = {}
        )
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PreviewMusicScreen() {
    MelodyDiaryTheme {
        MusicScreen()
    }
}