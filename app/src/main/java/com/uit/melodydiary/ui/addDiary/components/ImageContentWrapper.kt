package com.uit.melodydiary.ui.addDiary.components

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp

@Composable
fun ImageContentWrapper(
    modifier: Modifier = Modifier,
    imageByteArray: ByteArray,
    isShrink: Boolean = false,
    onDeleteClick: () -> Unit,
    onShrinkClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val bitmap = BitmapFactory.decodeByteArray(
        imageByteArray, 0, imageByteArray.size
    )
    bitmap.asImageBitmap()
    Card(
        modifier = if (!isShrink) {
            modifier
                .fillMaxWidth()
                .padding(16.dp)
        } else {
            modifier
                .padding(16.dp)
                .size(200.dp)
        },
        shape = RoundedCornerShape(20.dp)
    ) {
        Box(
            modifier = Modifier.aspectRatio(1.0f)
                .fillMaxWidth()
        ) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
            )

            Column(
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                IconButton(
                    onClick = { expanded = true },
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(50))
                        .background(Color.LightGray)
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = null
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .padding(end = 8.dp)
                ) {
                    DropdownMenuItem(onClick = {
                        expanded = false
                        onDeleteClick()
                    }) {
                        Text(text = "Delete")
                    }
                    DropdownMenuItem(onClick = {
                        expanded = false
                        onShrinkClick()
                    }) {
                        Text(text = if (!isShrink) "Shrink" else "Expand")
                    }
                }
            }
        }
    }
}