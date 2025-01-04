package com.uit.melodydiary.ui.diary.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uit.melodydiary.model.Diary
import com.uit.melodydiary.utils.DayOfWeekConverter
import com.uit.melodydiary.utils.byteArrayToString
import com.uit.melodydiary.utils.loadContentListFromFile
import com.uit.melodydiary.utils.plus
import com.uit.melodydiary.utils.stringToByteArray
import java.time.format.DateTimeFormatter

@Composable
fun DiaryItem(
    item: Diary,
    modifier: Modifier = Modifier,
    onItemClick: () -> Unit,
) {
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM")
    val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    val diaryStyle = item.diaryStyle
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(
            topEnd = 20.dp,
            bottomStart = 20.dp,
            topStart = 5.dp,
            bottomEnd = 5.dp
        ),
        border = BorderStroke(
            1.dp,
            Color.DarkGray
        ),
        elevation = CardDefaults.cardElevation(
            2.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = diaryStyle.colorPalette,
        ),
        onClick = onItemClick
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.Transparent)
        ) {
            DateDetailInDiary(
                date = item.createdAt.format(formatter),
                time = item.createdAt.format(timeFormatter),
                thu = DayOfWeekConverter.convertToThu(item.createdAt.dayOfWeek.toString()),
                statusLogoRes = item.logo
            )
            Text(
                text = if (item.title.isNotEmpty()) item.title else "Title",
                style = TextStyle(
                    fontFamily = when (diaryStyle.fontStyle) {
                        "Serif" -> FontFamily.Serif
                        "Sans-serif" -> FontFamily.SansSerif
                        "Monospace" -> FontFamily.Monospace
                        "Cursive" -> FontFamily.Cursive
                        "Fantasy" -> FontFamily.Default
                        else -> FontFamily.Default
                    },
                    color = diaryStyle.color,
                    fontSize = diaryStyle.fontSize.value.sp + 10.sp
                ),
            )
            val textItem =
                loadContentListFromFile(item.contentFilePath).firstOrNull { it.first == "text" }
            val textContent: ByteArray = textItem?.second ?: stringToByteArray("")
            Text(
                text = byteArrayToString(textContent),
                style = TextStyle(
                    fontFamily = when (diaryStyle.fontStyle) {
                        "Serif" -> FontFamily.Serif
                        "Sans-serif" -> FontFamily.SansSerif
                        "Monospace" -> FontFamily.Monospace
                        "Cursive" -> FontFamily.Cursive
                        "Fantasy" -> FontFamily.Default
                        else -> FontFamily.Default
                    },
                    color = diaryStyle.color,
                    fontSize = diaryStyle.fontSize
                ),
            )
        }
    }
}