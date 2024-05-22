package com.nhkim.composeimagesearch.domain.repository

import com.nhkim.composeimagesearch.domain.entity.KakaoEntity

interface KakaoRepository {
    suspend fun getImages(searchText: String, page: Int): List<KakaoEntity>
    suspend fun getVideos(searchText: String, page: Int): List<KakaoEntity>
}