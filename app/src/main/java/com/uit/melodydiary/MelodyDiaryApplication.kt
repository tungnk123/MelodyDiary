package com.uit.melodydiary

import android.app.Application
import com.uit.melodydiary.ui.profile.setLocale
import com.uit.melodydiary.utils.PreferenceUtils

class MelodyDiaryApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(applicationContext)

    }
}