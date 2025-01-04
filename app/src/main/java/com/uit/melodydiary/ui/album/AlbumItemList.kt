package com.uit.melodydiary.ui.album

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.uit.melodydiary.R
import com.uit.melodydiary.model.Album

@Composable
fun AlbumItemList(
    albumList: List<Album>,
    onItemClick: (Album) -> Unit,
    modifier: Modifier = Modifier,
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
    }
    else {
        LazyColumn {
            items(albumList) { album ->
                AlbumItem(album = album,
                    onItemClick = { onItemClick(album) })
            }
        }


    }

}