package com.roksidark.core.domain.usecase

import com.roksidark.core.data.model.entity.SearchItem
import com.roksidark.core.domain.repository.RemoteRepository

class GetArtistsRemotely(
    private val repository: RemoteRepository
) {
    suspend fun invoke(format: String,
                       searchText: String?): SearchItem {
        return repository.getArtists(format,
            searchText)
    }
}