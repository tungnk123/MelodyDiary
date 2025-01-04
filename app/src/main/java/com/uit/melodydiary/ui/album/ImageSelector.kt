package com.uit.melodydiary.ui.album

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun ImageSelector(
    selectedImageUri: Uri?,
    onImageSelected: (Uri) -> Unit,
) {
    val photoPikcerLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia(),
            onResult = {
                onImageSelected(it!!)
            })
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            text = "Choose logo for album"
        )
        if (selectedImageUri == null) {
            Button(
                onClick = {
                    photoPikcerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                )
            ) {
                Text(
                    "Choose logo",
                    color = Color.White
                )
            }
        }
        else {
            AsyncImage(
                model = selectedImageUri,
                contentDescription = null,
                modifier = Modifier.size(100.dp),
            )
        }
    }
}