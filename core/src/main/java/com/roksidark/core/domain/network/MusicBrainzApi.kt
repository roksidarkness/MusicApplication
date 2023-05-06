package com.roksidark.core.domain.network

import com.roksidark.core.data.model.entity.AlbumItem
import com.roksidark.core.data.model.entity.SearchItem
import retrofit2.http.GET
import retrofit2.http.Query


interface MusicBrainzApi {

    @GET("artist")
    suspend fun getArtists(
        //TODO
        @Query("fmt") format: String = "json",
        @Query("query") query: String
    ): SearchItem?

    @GET("release")
    suspend fun getAlbums(
        @Query("fmt") format: String = "json",
        @Query("artist") query: String
    ): AlbumItem?
}