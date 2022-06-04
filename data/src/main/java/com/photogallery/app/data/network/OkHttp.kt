package com.photogallery.app.data.network

import com.photogallery.app.data.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

fun createOkHttpClient(): OkHttpClient {
    val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
    if (BuildConfig.DEBUG) {
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC).also {
            okHttpClientBuilder.addInterceptor(it)
        }
    }
    okHttpClientBuilder.retryOnConnectionFailure(true)

    return okHttpClientBuilder.build()
}
