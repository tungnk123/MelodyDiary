package com.uit.melodydiary.utils

import com.uit.melodydiary.model.Emotion
import com.uit.melodydiary.model.MusicSmall

object AppConstants {
    const val EMOTION_FUN = "Fun"
    const val EMOTION_CRY = "Cry"
    const val EMOTION_SAD = "Sad"
    const val EMOTION_FEAR = "Fear"
    const val EMOTION_DISGUST = "Disgust"
    const val EMOTION_ANGRY = "Angry"
    const val MUSIC_FUN = "Fun Music"
    const val MUSIC_CRY = "Cry Music"
    const val MUSIC_SAD = "Sad Music"
    const val MUSIC_FEAR = "Fear Music"
    const val MUSIC_DISGUST = "Disgust Music"
    const val MUSIC_ANGRY = "Angry Music"

    val demoList = listOf(
        MusicSmall(
            musicId = 1,
            title = "Fun 1",
            emotion = Emotion.Fun.emotion,
            url = "https://un-silent-backend-mobile.azurewebsites.net/api/v1/musics/file/1AAh9ZzPKChhojoVR9nHjcGHEKpcSswvq"
        ),
        MusicSmall(
            musicId = 2,
            title = "Cry 1",
            emotion = Emotion.Cry.emotion,
            url = "https://un-silent-backend-mobile.azurewebsites.net/api/v1/musics/file/15q1nFI6zpo5HrRRRN-6kkpYCjmCQkbPF"
        ),
        MusicSmall(
            musicId = 3,
            title = "Sad 1",
            emotion = Emotion.Sad.emotion,
            url = "https://un-silent-backend-mobile.azurewebsites.net/api/v1/musics/file/1AAh9ZzPKChhojoVR9nHjcGHEKpcSswvq"
        ),
        MusicSmall(
            musicId = 4,
            title = "Fear 1",
            emotion = Emotion.Fear.emotion,
            url = "https://un-silent-backend-mobile.azurewebsites.net/api/v1/musics/file/1AAh9ZzPKChhojoVR9nHjcGHEKpcSswvq"
        ),
        MusicSmall(
            musicId = 5,
            title = "Disgust1",
            emotion = Emotion.Disgust.emotion,
            url = "https://un-silent-backend-mobile.azurewebsites.net/api/v1/musics/file/1fvmAE2f3MDyYvFMQcBxSl6wZafAXTrV1"
        ),
        MusicSmall(
            musicId = 6,
            title = "Angry 1",
            emotion = Emotion.Angry.emotion,
            url = "https://un-silent-backend-mobile.azurewebsites.net/api/v1/musics/file/1AAh9ZzPKChhojoVR9nHjcGHEKpcSswvq"
        ),
    )

}