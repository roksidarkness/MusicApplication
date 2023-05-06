package com.roksidark.feature

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roksidark.core.data.model.entity.Album
import com.roksidark.core.data.model.entity.Artist
import com.roksidark.core.data.model.entity.SearchItem
import com.roksidark.core.domain.usecase.ArtistUseCases
import com.roksidark.core.util.Constant.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val useCases: ArtistUseCases
) : ViewModel() {

    private var _items = MutableLiveData<List<Artist>>()
    val items: LiveData<List<Artist>> = _items

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var _itemAlbums = MutableLiveData<List<Album?>>()
    val itemAlbums: LiveData<List<Album?>> = _itemAlbums

    fun performSearch(request: String){
        viewModelScope.launch {
            try {
                if (request.isNotBlank()) {
                    _isLoading.value = true
                    //TODO
                    val data = useCases.getArtistsRemotely.invoke("json", request)
                    _items.value = data?.artists
                    _isLoading.value = false
                }else{
                    _items.value = emptyList()
                    _isLoading.value = false
                }
            } catch (error: Exception) {
                error.localizedMessage?.let {
                    Log.d(TAG, it)
                    _isLoading.value = false
                }
            }
        }
    }

    fun getDetails(id: String) {
        viewModelScope.launch {
            try {
                //TODO
                val data = useCases.getAlbumRemotely.invoke("json", id)
                _itemAlbums.value = data?.releases
                _isLoading.value = false
            } catch (error: Exception) {
                error.localizedMessage?.let {
                    Log.d(TAG, it)
                    _isLoading.value = false
                }
            }
        }

    }

}