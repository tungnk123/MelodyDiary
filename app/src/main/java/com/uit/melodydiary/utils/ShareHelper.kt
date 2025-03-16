package com.uit.melodydiary.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.core.net.toUri
import com.uit.melodydiary.model.Diary
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

object ShareHelper {

    fun shareDiary(context: Context, diary: Diary) {
        CoroutineScope(Dispatchers.IO).launch {
            val contentList = loadContentListFromFile(diary.contentFilePath)
            val shareText = buildShareText(diary, contentList)
            Log.d("test_share", "Share text: $shareText")

            val imageUri = getFirstImageUri(context, contentList)
            val songUri = getSongUri(diary.songPath)
            Log.d("test_share", "Image uri: $imageUri, Song uri: $songUri")

            withContext(Dispatchers.Main) {
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    putExtra(Intent.EXTRA_TEXT, shareText)

                    when {
                        imageUri != null -> {
                            type = "image/*"
                            putExtra(Intent.EXTRA_STREAM, imageUri)
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        }

                        else -> {
                            type = "text/plain"
                        }
                    }
                }
                context.startActivity(Intent.createChooser(shareIntent, "Share Diary Entry"))
            }
        }
    }

    private fun buildShareText(diary: Diary, contentList: List<Pair<String, ByteArray>>): String {
        val textContent = contentList.filter { it.first == "text" }
            .joinToString("\n\n") { byteArrayToString(it.second) }

        return """
            📖 ${diary.title}
            📝 $textContent
            😊 Mood: ${diary.mood}
            📅 Date: ${diary.createdAt}
            🎵 Song: ${diary.songPath}
            
            #MelodyDiary
        """.trimIndent()
    }

    private suspend fun getFirstImageUri(
        context: Context,
        contentList: List<Pair<String, ByteArray>>
    ): Uri? {
        val imageByteArray = contentList.find { it.first == "image" }?.second
        return imageByteArray?.let { byteArrayToUri(context, it) }
    }

    private fun getSongUri(songPath: String): Uri? {
        return if (songPath.isNotEmpty()) songPath.toUri() else null
    }

    private suspend fun byteArrayToUri(context: Context, byteArray: ByteArray): Uri? {
        return withContext(Dispatchers.IO) {
            try {
                val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                val bytes = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 80, bytes)
                val path = MediaStore.Images.Media.insertImage(
                    context.contentResolver,
                    bitmap,
                    "Diary Image",
                    null
                )
                path?.toUri()
            } catch (e: Exception) {
                Log.e("test_share", "Error converting byte array to URI: $e")
                null
            }
        }
    }

    private fun byteArrayToString(byteArray: ByteArray): String {
        return byteArray.toString(Charsets.UTF_8)
    }
}
