package com.uit.melodydiary.ui.album

import MusicHelper
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import com.uit.melodydiary.R
import com.uit.melodydiary.model.Album
import com.uit.melodydiary.model.MusicSmall
import com.uit.melodydiary.ui.music.MusicList
import com.uit.melodydiary.ui.theme.mygreen

@OptIn(UnstableApi::class)
@Composable
fun AlbumDetailScreen(
    album: Album,
    musicList: List<MusicSmall>,
    onClose: () -> Unit,
    onAddMusic: (Uri) -> Unit,
    selectedMusicSmall: MusicSmall,
    onSelectedMusicChange: (MusicSmall) -> Unit,
) {
    val filePickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            onAddMusic(uri)
        }
    }
    val bitmap = BitmapFactory.decodeByteArray(
        album.logo,
        0,
        album.logo.size
    )
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(
                onClick = onClose,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null
                )
            }

            IconButton(onClick = {
                filePickerLauncher.launch("audio/*")

            }) {
                Icon(
                    imageVector = Icons.Filled.AddCircle,
                    contentDescription = null
                )
            }
        }

        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = null,
            modifier = Modifier
                .width(122.dp)
                .height(90.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = album.title,
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = "${musicList.size} đoạn nhạc",
            style = MaterialTheme.typography.titleMedium
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 20.dp,
                    vertical = 10.dp
                ),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    MusicHelper.playSequential()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
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
                    MusicHelper.playRandom { }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = mygreen
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