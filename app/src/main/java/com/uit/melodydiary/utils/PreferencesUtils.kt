package com.uit.melodydiary.utils

import android.content.Context
import android.content.SharedPreferences

object PreferenceUtils {
    private const val PREFS_NAME = "melody_diary_prefs"
    private const val KEY_LANGUAGE = "key_language"

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun saveLanguage(context: Context, languageCode: String) {
        val prefs = getSharedPreferences(context)
        with(prefs.edit()) {
            putString(KEY_LANGUAGE, languageCode)
            apply()
        }
    }

    fun getLanguage(context: Context): String {
        val prefs = getSharedPreferences(context)
        return prefs.getString(KEY_LANGUAGE, "en") ?: "en"
    }
}
