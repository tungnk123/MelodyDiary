package com.example.melodydiary.network

import com.example.melodydiary.model.Diary
import retrofit2.http.GET

interface DiaryApiService {
    @GET("diary")
    suspend fun getDiarys(): List<Diary>
}