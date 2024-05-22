package com.nhkim.composeimagesearch.data

import com.nhkim.composeimagesearch.data.network.convert
import com.nhkim.composeimagesearch.domain.entity.KakaoEntity
import com.nhkim.composeimagesearch.domain.repository.KakaoRepository
import javax.inject.Inject


class KakaoRepositoryImpl @Inject constructor(
    private val kakaoSearchService: KakaoSearchService
): KakaoRepository {
    override suspend fun getImages(searchText: String, page: Int): List<KakaoEntity> {
        return kakaoSearchService.getImages(query = searchText, page = page).documents.map { it.convert() }
    }

    override suspend fun getVideos(searchText: String, page: Int): List<KakaoEntity> {
        return kakaoSearchService.getVideos(query = searchText, page = page).documents.map { it.convert() }
    }

}