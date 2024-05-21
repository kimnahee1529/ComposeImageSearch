package com.nhkim.composeimagesearch.data

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object KakaoApi {
    private const val SEARCH_BASE_URL = "https://dapi.kakao.com/"

    const val apiKey = "87fc59267336d45275d00d39c3c853eb"

    private fun createOkHttpClient(): OkHttpClient {
        val intercepter = HttpLoggingInterceptor()
        val keyInterceptor = Interceptor { chain ->
            var request = chain.request()
            val url = request.newBuilder()
                .addHeader("Authorization", "KakaoAK $apiKey")
                .build()
            chain.proceed(url)
        }
        return OkHttpClient.Builder()
            .addNetworkInterceptor(intercepter)
            .addInterceptor(keyInterceptor)
            .build()
    }

    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory())
        .build()
    val kakaoRetrofit = Retrofit.Builder()
        .baseUrl(SEARCH_BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(createOkHttpClient())
        .build()

    val kakaoNetwork: KakaoSearchService = kakaoRetrofit.create(KakaoSearchService::class.java)
}