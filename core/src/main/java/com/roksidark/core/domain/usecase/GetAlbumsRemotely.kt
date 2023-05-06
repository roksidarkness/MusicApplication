package com.roksidark.core.domain.usecase

import com.roksidark.core.data.model.entity.AlbumItem
import com.roksidark.core.data.model.entity.SearchItem
import com.roksidark.core.domain.repository.RemoteRepository

class GetAlbumsRemotely(
    private val repository: RemoteRepository
) {
    suspend fun invoke(format: String,
                       searchText: String): AlbumItem? {
        return repository.getAlbums(format,
            searchText)
    }
}