package com.uit.melodydiary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
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
            }
            MelodyDiaryApp()
        }
    }
}


