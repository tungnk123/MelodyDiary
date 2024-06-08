package com.uit.melodydiary.ui.components

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.makeappssimple.abhimanyu.composeemojipicker.ComposeEmojiPickerBottomSheetUI
import com.makeappssimple.abhimanyu.composeemojipicker.ComposeEmojiPickerEmojiUI
import com.uit.melodydiary.ui.theme.mauDa1
import com.uit.melodydiary.ui.theme.mauDa2
import com.uit.melodydiary.ui.theme.mauDa3
import com.uit.melodydiary.ui.theme.mauDa4
import com.uit.melodydiary.ui.theme.mauDa5
import com.uit.melodydiary.ui.theme.mauDa6
import com.uit.melodydiary.ui.theme.mauDa7
import com.uit.melodydiary.ui.theme.mygreen

@Composable
fun FontSelectionContent(
    selectedFontStyle: String,
    onFontStyleChange: (String) -> Unit,
    selectedFontSize: TextUnit,
    onFontSizeChange: (TextUnit) -> Unit,
    selectedColor: Color,
    onColorChange: (Color) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Select Font Style",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        val fontStyles = listOf("Default", "Serif", "Sans-serif", "Monospace", "Cursive", "Fantasy")
        LazyRow(
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(fontStyles) { style ->
                Text(
                    text = style,
                    modifier = Modifier
                        .clickable {
                            onFontStyleChange(style)
                        }
                        .padding(8.dp),

                    style = TextStyle(
                        fontFamily = when (style) {
                            "Serif" -> FontFamily.Serif
                            "Sans-serif" -> FontFamily.SansSerif
                            "Monospace" -> FontFamily.Monospace
                            "Cursive" -> FontFamily.Cursive
                            "Fantasy" -> FontFamily.Default
                            else -> FontFamily.Default
                        }
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Select Font Size",
            style = MaterialTheme.typography.titleLarge
        )

        val fontSizes = listOf(12.sp, 14.sp, 16.sp, 18.sp, 20.sp, 25.sp, 30.sp)
        LazyRow(
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(fontSizes) {
                Text(
                    text = "${it.value}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onFontSizeChange(it)
                        }
                        .padding(8.dp)
                        ,
                    fontSize = it
                )
            }
        }

        Text(
            text = "Select Color ",
            style = MaterialTheme.typography.titleLarge
        )

        val colors = listOf(Color.Black, Color.White, Color.Red, Color.Blue, Color.Yellow, Color.Cyan, Color.Green, Color.Magenta)
        LazyRow(
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(colors) { color ->
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .size(40.dp)
                        .background(color, shape = RoundedCornerShape(50))
                        .clickable {
                            onColorChange(color)
                        }
                )
            }
        }
        Spacer(modifier = Modifier.height(30.dp))


    }
}

@Composable
fun PaletteSelection(
    color: Color,
    onColorChange: (Color) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Select Color ",
            style = MaterialTheme.typography.titleLarge
        )
        val colors = listOf(mauDa1, mauDa2, mauDa3, mauDa4, mygreen, mauDa5, mauDa6, mauDa7)

        LazyRow(
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(colors) { color ->
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .size(40.dp)
                        .background(color, shape = RoundedCornerShape(20.dp))
                        .clickable {
                            onColorChange(color)
                        }
                )
            }
        }
        Spacer(modifier = Modifier.height(30.dp))
    }

}


@Composable
fun ImageSelection(
    selectedImageUri: Uri?,
    onImageSelected: (Uri) -> Unit
) {
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            it?.let { uri -> onImageSelected(uri) }
        }
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Choose image from your library",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp)
        )

        Button(
            onClick = {
                photoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            },
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.White,
            )
        ) {
            Text("Browse image", color = Color.White)
        }

        Spacer(modifier = Modifier.height(50.dp))

    }
}


