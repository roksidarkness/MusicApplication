package com.roksidark.feature

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roksidark.core.data.model.entity.album.Album
import com.roksidark.core.data.model.entity.artist.Artist
import com.roksidark.core.domain.usecase.ArtistUseCases
import com.roksidark.core.util.Constant.FORMAT
import com.roksidark.feature.util.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val useCases: ArtistUseCases
) : ViewModel() {

    private var _items = MutableLiveData<DataState<List<Artist>>>()
    val items: LiveData<DataState<List<Artist>>> = _items

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var _itemAlbums = MutableLiveData<DataState<List<Album?>>>()
    val itemAlbums: LiveData<DataState<List<Album?>>> = _itemAlbums

    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText.asStateFlow()

    fun updateSearchText(newSearchText: String) {
        _searchText.value = newSearchText
    }

    fun performSearch(request: String) {
        viewModelScope.launch {
            try {
                if (request.isNotBlank()) {
                    _isLoading.value = true
                    val data = useCases.getArtistsRemotely.invoke(FORMAT, request)
                    _items.value = DataState.Success(data?.artists ?: emptyList())
                    _isLoading.value = false
                } else {
                    _items.value = DataState.Success(emptyList())
                    _isLoading.value = false
                }
            } catch (error: Exception) {
                error.localizedMessage?.let {
                    val errorMessage = error.localizedMessage ?: "Unknown error occurred"
                    _items.value = DataState.Error(errorMessage)
                    _isLoading.value = false
                }
            }
        }
    }

    fun getDetails(id: String) {
        viewModelScope.launch {
            try {
                val data = useCases.getAlbumRemotely.invoke(FORMAT, id)
                _itemAlbums.value = DataState.Success(data?.releases ?: emptyList())
                _isLoading.value = false
            } catch (error: Exception) {
                error.localizedMessage?.let {
                    val errorMessage = error.localizedMessage ?: "Unknown error occurred"
                    _isLoading.value = false
                    _itemAlbums.value = DataState.Error(errorMessage)
                }
            }
        }
    }
}