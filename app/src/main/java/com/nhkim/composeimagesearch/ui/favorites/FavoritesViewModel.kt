package com.nhkim.composeimagesearch.ui.favorites

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nhkim.composeimagesearch.data.datastore.SettingRepository
import com.nhkim.composeimagesearch.domain.repository.KakaoRepository
import com.nhkim.composeimagesearch.ui.model.ResultItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val settingRepository: SettingRepository
): ViewModel() {

    val _bookmark: MutableStateFlow<List<ResultItem>> = MutableStateFlow(emptyList())
    val bookmark = _bookmark.asStateFlow()

    init {
        viewModelScope.launch {
            settingRepository.loadItems().collect {
                _bookmark.value = it
                Log.d("bookmark1 Favorites", "${it.size}개 : $it")
            }
        }
    }

    fun removeItem(item: ResultItem) {
        viewModelScope.launch {
            settingRepository.removeItems(item)
        }
    }

}