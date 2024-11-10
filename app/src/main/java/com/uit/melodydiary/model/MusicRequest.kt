package com.uit.melodydiary.model

data class MusicRequest(
    val lyric: String,
    val genre: String = "",
    val instrument: String = "",
)
