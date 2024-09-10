package com.uit.melodydiary

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.uit.melodydiary.ui.profile.setLocale
import com.uit.melodydiary.utils.PreferenceUtils
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appContainer = (applicationContext as MelodyDiaryApplication).container
        setContent {
            val scope = rememberCoroutineScope()

            LaunchedEffect(Unit) {
                scope.launch {
                    try {
                        appContainer.warmUpApiService.warmUp()
                    }
                    catch(e: Exception) {

                    }
                }
                scope.launch {

                }
            }

            MelodyDiaryApp()
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        val savedLanguage = PreferenceUtils.getLanguage(this)
        setLocale(newBase!!, savedLanguage)
    }
}


