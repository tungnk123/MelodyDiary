package com.uit.melodydiary.network

import com.uit.melodydiary.model.Diary
import retrofit2.http.GET

interface DiaryApiService {
    @GET("diary")
    suspend fun getDiarys(): List<Diary>
}