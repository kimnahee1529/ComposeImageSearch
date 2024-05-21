package com.nhkim.composeimagesearch.data

import com.nhkim.composeimagesearch.data.network.KakaoDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface KakaoSearchService {
    @GET("v2/search/image")
    suspend fun getImages(
        @Query("query") query: String,
        @Query("sort") sort: String? = null,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null
    ): KakaoDTO<KakaoDTO.ImageDocument>

    @GET("v2/search/vclip")
    suspend fun getVideos(
        @Query("query") query: String,
        @Query("sort") sort: String? = null,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null
    ): KakaoDTO<KakaoDTO.VideoDocument>
}