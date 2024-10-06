package com.uit.melodydiary.model

import com.google.gson.annotations.SerializedName

data class MusicResponse(
    val value: MusicInfoDto,
    val valueType: String,
    val status: Int,
    val isSuccess: Boolean,
    val successMessage: String,
    val correlationId: String,
    val errors: List<Any>,
    val validationErrors: List<Any>
)

data class MusicInfoDto(
    val totalNumberOfMusics: Int,
    val totalDurationInSeconds: Int,
    val musicList: List<Music>
)

data class Music(
    val id: Int,
    val name: String,
    @SerializedName("colorValue")
    val color: String,
    val description: String,
    val lyrics: String,
    val durationInSeconds: Int,
    val fileContentUrl: String,
    val fileName: String?, // Nullable field
    val coverImageContentUrl: String?, // Nullable field
    val isFavourite: Boolean,
    val userId: String
)

data class GeneratedMusicDto(
    @SerializedName("value")
    val value: GeneratedMusicInfo,
    @SerializedName("valueType")
    val valueType: String,
    val status: Int,
    val isSuccess: Boolean,
    val successMessage: String,
    val correlationId: String,
    val errors: List<Any>,
    val validationErrors: List<Any>
)

data class GeneratedMusicInfo(
    @SerializedName("fileId")
    val fileId: String,
    val emotion: String,
    @SerializedName("durationInSeconds")
    val duration: Int,
    @SerializedName("fileContentUrl")
    val fileContentUrl: String
)

fun GeneratedMusicDto.toMusicSmall(): MusicSmall {
    return MusicSmall(
        musicId = 0,
        title = "Giai điệu ${this.value.fileId}",
        url = this.value.fileContentUrl,
        duration = this.value.duration.toFloat(),
    )
}
