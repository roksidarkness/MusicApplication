package com.roksidark.core.data.repository

import com.roksidark.core.data.model.entity.SearchItem
import com.roksidark.core.domain.network.MusicBrainzApi
import com.roksidark.core.domain.repository.RemoteRepository

class RemoteRepositoryImpl(private val api: MusicBrainzApi): RemoteRepository {

    override suspend fun getArtists(format: String,
                                    searchText: String?): SearchItem {
        return api.getArtists(format, searchText)
    }
}