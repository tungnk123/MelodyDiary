package com.uit.melodydiary.ui.addDiary.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.uit.melodydiary.R
import com.uit.melodydiary.model.Emotion
import com.uit.melodydiary.ui.music.MyDropDown

@Composable
fun MusicConfigurationDialog(
    onDismiss: () -> Unit,
    currentEmotion: String,
    onCurrentEmotionChange: (String) -> Unit,
    giaiDieuName: String,
) {
    val emotionList: MutableList<String> = mutableListOf(
        Emotion.Fun.emotion,
        Emotion.Cry.emotion,
        Emotion.Sad.emotion,
        Emotion.Fear.emotion,
        Emotion.Disgust.emotion,
        Emotion.Angry.emotion,
    )
    val selectedEmotion = remember {
        mutableStateOf(currentEmotion)
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
                    MyDropDown(
                        list = emotionList,
                        selectedItem = selectedEmotion.value,
                        onSelectedItemChange = { currentEmotion ->
                            selectedEmotion.value = currentEmotion
                            onCurrentEmotionChange.invoke(currentEmotion)
                        })
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
