package com.uit.melodydiary.ui.album

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.uit.melodydiary.R
import com.uit.melodydiary.model.Album
import com.uit.melodydiary.ui.theme.mygreen

@Composable
fun CreateAlbumScreen(
    onAlbumCreated: (Album) -> Unit,
    isCreateAlbumDialogOpen: Boolean,
    onCloseDialog: () -> Unit,
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    if (isCreateAlbumDialogOpen) {
        AlertDialog(onDismissRequest = {
            onCloseDialog()
            title = ""
            description = ""
            selectedImageUri = null
        },
            modifier = Modifier.padding(bottom = 10.dp),
            title = {
                Text(
                    "Create Album",
                    style = MaterialTheme.typography.titleLarge
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (title.isNotBlank() && description.isNotBlank() && selectedImageUri != null) {
                            val newAlbum = Album(
                                albumId = 0,
                                logo = context.contentResolver
                                    .openInputStream(
                                        selectedImageUri!!
                                    )
                                    ?.readBytes()!!,
                                title = title,
                                description = description
                            )
                            onAlbumCreated(newAlbum)
                        }
                        onCloseDialog()
                        title = ""
                        description = ""
                        selectedImageUri = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = mygreen
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        text = stringResource(R.string.btn_xac_nhan),
                        color = Color.Black
                    )
                }

            },
            dismissButton = {
                Button(
                    onClick = {
                        onCloseDialog()
                        title = ""
                        description = ""
                        selectedImageUri = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red
                    ),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.padding(bottom = 10.dp)
                ) {
                    Text(
                        text = stringResource(R.string.btn_close),
                        color = Color.White
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier.padding(vertical = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ImageSelector(selectedImageUri = selectedImageUri,
                        onImageSelected = { uri ->
                            selectedImageUri = uri
                        })

                    OutlinedTextField(value = title,
                        onValueChange = { title = it },
                        label = { Text("Title") })

                    OutlinedTextField(value = description,
                        onValueChange = { description = it },
                        label = { Text("Description") })

                }
            })
    }
}