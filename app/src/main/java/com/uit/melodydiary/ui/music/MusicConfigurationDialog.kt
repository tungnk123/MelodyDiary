package com.uit.melodydiary.ui.music

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.uit.melodydiary.R

@Composable
fun MusicConfigurationDialog(
    onDismiss: () -> Unit,
    sliderPosition: Float,
    giaiDieuName: String,
    onValueChange: (Float) -> Unit,
    onPlayPreviousClick: () -> Unit,
    onPlayPauseClick: () -> Unit,
    onPlayNextClick: () -> Unit
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
                Slider(
                    value = sliderPosition,
                    onValueChange = onValueChange,
                    valueRange = 0f..100f,
                    modifier = Modifier.fillMaxWidth(),
                    colors = SliderDefaults.colors(
                        thumbColor = Color.Black,
                        activeTrackColor = Color.Black,
                        inactiveTrackColor = Color.LightGray,
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))
                // Control Buttons
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onPlayPreviousClick) {
                        Icon(
                            painter = painterResource(R.drawable.ic_skip_previous),
                            contentDescription = "Previous"
                        )
                    }
                    IconButton(onClick = onPlayPauseClick) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow, contentDescription = "Play/Pause"
                        )
                    }
                    IconButton(onClick = onPlayNextClick) {
                        Icon(
                            painter = painterResource(R.drawable.ic_skip_next),
                            contentDescription = "Previous"
                        )
                    }
                }
            }
        }
    }
}
