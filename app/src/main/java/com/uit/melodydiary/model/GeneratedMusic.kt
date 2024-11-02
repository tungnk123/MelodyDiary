package com.uit.melodydiary.model

import com.google.gson.annotations.SerializedName

data class GeneratedMusic(
    @SerializedName("fileId") val fileId: String,
    @SerializedName("emotion") val emotion: String,
    @SerializedName("genre") val genre: String,
    @SerializedName("instrument") val instrument: String,
    @SerializedName("groupId") val groupId: String,
    @SerializedName("fileContentUrl") val fileContentUrl: String
)

data class MusicGroup(
    @SerializedName("groupId") val groupId: String,
    @SerializedName("musics") val musics: List<GeneratedMusic>
)

