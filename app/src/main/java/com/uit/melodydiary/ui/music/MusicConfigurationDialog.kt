package com.uit.melodydiary.ui.music

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
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
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(
                shape = RoundedCornerShape(100.dp)
            )
            .background(
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
