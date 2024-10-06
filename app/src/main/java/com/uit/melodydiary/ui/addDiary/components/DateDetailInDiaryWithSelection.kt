package com.uit.melodydiary.ui.addDiary.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.IconButton
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun DateDetailInDiaryWithSelection(
    date: String,
    time: String,
    thu: String,
    @DrawableRes statusLogoRes: Int,
    onPickEmoteClick: () -> Unit,
    onDateTimePickerClick: () -> Unit,
    enabled: Boolean,
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
            onClick = onDateTimePickerClick,
            enabled = enabled
        ) {
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(
            onClick = onPickEmoteClick,
            enabled = enabled
        ) {
            Image(
                painter = painterResource(statusLogoRes),
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )
        }
        Spacer(modifier = Modifier.width(5.dp))
    }
}

