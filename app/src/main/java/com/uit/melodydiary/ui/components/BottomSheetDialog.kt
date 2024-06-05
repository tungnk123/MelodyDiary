package com.uit.melodydiary.ui.components

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
