package com.uit.melodydiary.ui.music

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.uit.melodydiary.R

@Composable
fun MusicConfigurationTab(
    isPlaying: Boolean,
    onShowMusicConfigurationDialog: () -> Unit,
    onPlayPreviousClick: () -> Unit,
    onPlayPauseClick: () -> Unit,
    onPlayNextClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth().padding(16.dp).clip(
            shape = RoundedCornerShape(100.dp)
        ).background(
            color = Color.White
        )
    ) {

        IconButton(onClick = onShowMusicConfigurationDialog) {
            Icon(
                painter = painterResource(R.drawable.ic_setting),
                contentDescription = "Show music configuration tab",
            )
        }

        IconButton(onClick = onPlayPreviousClick) {
            Icon(
                painter = painterResource(R.drawable.ic_skip_previous),
                contentDescription = "Previous"
            )
        }
        IconButton(onClick = onPlayPauseClick) {
            Image(
                painter = painterResource(if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play),
                contentDescription = "Play/Pause"
            )
        }
        IconButton(onClick = onPlayNextClick) {
            Icon(
                painter = painterResource(R.drawable.ic_skip_next), contentDescription = "Previous"
            )
        }
    }
}

@Composable
fun MusicConfigurationDialog(
    onDismiss: () -> Unit,
    giaiDieuName: String,
) {
    val theLoaiList: MutableList<String> = mutableListOf(
        "Fun Music", "Cry Music", "Sad Music", "Fear Music", "Disgust Music", "Angry Music"
    )
    val selectedGiaiDieu = remember {
        mutableStateOf("")
    }
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Music Configuration",
                        style = MaterialTheme.typography.titleLarge,
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(
                        onClick = onDismiss,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                        )
                    }
                }

                Row(
                    modifier = Modifier.padding(vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.title_the_loai),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    MyDropDown(list = theLoaiList,
                        selectedItem = selectedGiaiDieu.value,
                        onSelectedItemChange = { selectedGiaiDieu.value = it })
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = giaiDieuName,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}
