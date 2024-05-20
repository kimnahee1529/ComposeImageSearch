package com.nhkim.composeimagesearch.ui.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nhkim.composeimagesearch.domain.entity.KakaoEntity
import com.nhkim.composeimagesearch.domain.repository.KakaoRepository
import com.nhkim.composeimagesearch.data.datastore.SettingRepository
import com.nhkim.composeimagesearch.ui.model.ResultItem
import com.nhkim.composeimagesearch.ui.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val kakaoRepository: KakaoRepository,
    private val settingRepository: SettingRepository,
) : ViewModel() {

    //val _searchResult = MutableStateFlow<List<ResultItem>>(emptyList())
    val _searchResult: MutableStateFlow<UiState<List<ResultItem>>> =
        MutableStateFlow(UiState.Loading)
    val searchResult = _searchResult.asStateFlow()

    //val _page = MutableStateFlow<Int>(1) //이렇게도 쓰는 게 맞나????
    val _page: MutableStateFlow<Int> = MutableStateFlow(1)
    val page = _page.asStateFlow()

    //    val _bookmark = MutableStateFlow<List<ResultItem>>(emptyList())
    val _bookmark: MutableStateFlow<List<ResultItem>> = MutableStateFlow(emptyList())
    val bookmark = _bookmark.asStateFlow()

    init {
        //Data Store에 저장된 북마크 된 ResultItem 불러오기
        viewModelScope.launch {
            settingRepository.loadItems().collect {
                _bookmark.value = it
                Log.d("bookmark1 Search", "${it.size}개 : $it")
            }
        }
        viewModelScope.launch {
            bookmark.collect {
                setLike(it)
                Log.d("bookmark2 Search", "${it.size}개 : $it")
            }
        }
    }

    fun searchImage(query: String) {
        viewModelScope.launch {
            _searchResult.value = UiState.Loading
            try {
                val images = kakaoRepository.getImages(query, page.value).map { it.convert() }
                val updatedImages = images.map { image ->
                    image.copy(isLike = _bookmark.value.any { bookmark -> bookmark.url == image.url })
                }
                _searchResult.value = UiState.Success(updatedImages)
            } catch (e: Exception) {
                _searchResult.value = UiState.Error(e)
            }
        }
    }

    fun searchVideo(query: String) {
        viewModelScope.launch {
            _searchResult.value = UiState.Loading
            try {
                _searchResult.value = UiState.Success(
                    kakaoRepository.getVideos(query, page.value).map {
                        it.convert()
                    }
                )
            } catch (e: Exception) {
                _searchResult.value = UiState.Error(e)
            }
        }
    }

    fun clickButton(heart: ResultItem) {
        if (_searchResult.value is UiState.Success) {
            if (_bookmark.value.any { it.url == heart.url }) {
                _bookmark.value = _bookmark.value.filter { it.url != heart.url } //heart 객체와 완전히 동일한 객체가 아닌 url만 비교
            } //북마크 설정
            else {
                _bookmark.value += listOf(heart)
            }
            val list =
                ((_searchResult.value as UiState.Success<List<ResultItem>>).data.map { item ->
                    if (_bookmark.value.any { it.url == item.url }) { //하나라도 있는 경우
                        item.copy(isLike = true) //설정된 걸 보고 UI에 전달
                    } else {
                        item.copy(isLike = false)
                    }
                })
            viewModelScope.launch {
                settingRepository.saveItems(_bookmark.value)
            }
            Log.d("bookmark3", _bookmark.value.toString())
            _searchResult.value = UiState.Success(list)
        }
    }

    private fun setLike(items: List<ResultItem>) { //items는 북마크, _searchResult.value는 전체 검색 결과
        if (_searchResult.value is UiState.Success) {
            _searchResult.value = UiState.Success(
                (_searchResult.value as UiState.Success<List<ResultItem>>).data.map { item ->
                    if (items.any { it.url == item.url }) {
                        item.copy(isLike = true)
                    } else {
                        item.copy(isLike = false)
                    }
                })
        }
    }

    private fun KakaoEntity.convert() = ResultItem(
        type = type,
        title = this.title,
        dateTime = datetime,
        url = thumbnail
    )
}