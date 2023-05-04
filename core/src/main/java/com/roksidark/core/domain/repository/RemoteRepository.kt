package com.roksidark.core.domain.repository

import com.roksidark.core.data.model.entity.SearchItem

interface RemoteRepository {

    suspend fun getArtists(format: String, searchText: String?): SearchItem

}
