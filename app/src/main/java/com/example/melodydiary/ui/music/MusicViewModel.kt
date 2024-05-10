package com.example.melodydiary.ui.music

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.melodydiary.MelodyDiaryApplication
import com.example.melodydiary.data.MusicRepository
import com.example.melodydiary.model.GeneratedMusicDto
import com.example.melodydiary.model.GeneratedMusicInfo
import com.example.melodydiary.model.MusicInfoDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject


class MusicViewModel(
    private val musicRepository: MusicRepository
) : ViewModel() {

    suspend fun fetchMusic(lyric: String): String {
        try {
            val musicResponse = withContext(Dispatchers.IO) {
                musicRepository.getGeneratedMusicByLyric(lyric)
            }
            return musicResponse.value.fileContentUrl
        } catch (e: Exception) {
            Log.e("fetchMusic", "Error fetching music: ${e.message}", e)
            throw e
        }
    }



    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MelodyDiaryApplication)
                val musicRepository = application.container.musicRepository
                MusicViewModel(musicRepository)
            }
        }
    }
}
