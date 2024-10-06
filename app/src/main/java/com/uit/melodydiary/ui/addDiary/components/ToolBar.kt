package com.uit.melodydiary.ui.addDiary.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.IconButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.uit.melodydiary.R

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