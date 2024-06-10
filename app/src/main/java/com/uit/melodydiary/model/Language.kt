package com.uit.melodydiary.model

enum class Language(val code: String, val displayName: String) {
    ENGLISH("en", "English"),
    VIETNAMESE("vi", "Tiếng Việt");

    companion object {
        fun fromCode(code: String): Language {
            return values().find { it.code == code } ?: VIETNAMESE
        }
    }
}