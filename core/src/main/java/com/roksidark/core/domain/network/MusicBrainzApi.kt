package com.roksidark.core.domain.network

import com.roksidark.core.data.model.entity.SearchItem
import retrofit2.http.GET
import retrofit2.http.Query


interface MusicBrainzApi {

    @GET("/ws/2/artist")
    suspend fun getArtists(
        @Query("fmt") format: String,
        @Query("query") searchText: String?
    ): SearchItem
}