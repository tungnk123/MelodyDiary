package com.example.melodydiary.ui.diary

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.melodydiary.MelodyDiaryApp
import com.example.melodydiary.R
import com.example.melodydiary.ui.theme.MelodyDiaryTheme

@Composable
fun DiaryScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(dimensionResource(R.dimen.padding_small))
    ) {
        Text(
            text = "Diary Screen",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDiaryScreen() {
    MelodyDiaryTheme {
        DiaryScreen()
    }
}