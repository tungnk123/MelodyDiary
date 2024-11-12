package com.uit.melodydiary

import MusicHelper
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.OptIn
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.media3.common.util.UnstableApi
import com.uit.melodydiary.ui.profile.setLocale
import com.uit.melodydiary.utils.PreferenceUtils
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @OptIn(UnstableApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appContainer = (applicationContext as MelodyDiaryApplication).container

        setContent {
            val scope = rememberCoroutineScope()
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                scope.launch {
                    try {
                        appContainer.warmUpApiService.warmUp()
                        MusicHelper.initializeExoPlayer(context)
                    }
                    catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            MelodyDiaryApp()
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        val savedLanguage = PreferenceUtils.getLanguage(this)
        setLocale(
            newBase!!,
            savedLanguage
        )
    }
}


