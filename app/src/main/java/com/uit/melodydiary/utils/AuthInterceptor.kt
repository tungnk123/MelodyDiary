package com.uit.melodydiary.utils

import okhttp3.Interceptor

class AuthInterceptor(private val authToken: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()
            .addHeader("Authorization", authToken)

        val request = requestBuilder.build()
        return chain.proceed(request)
    }

}