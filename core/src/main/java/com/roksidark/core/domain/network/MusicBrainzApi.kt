package com.roksidark.core.domain.network

import com.roksidark.core.data.model.entity.AlbumItem
import com.roksidark.core.data.model.entity.SearchItem
import com.roksidark.core.util.Constant.FORMAT
import retrofit2.http.GET
import retrofit2.http.Query


interface MusicBrainzApi {

    @GET("artist")
    suspend fun getArtists(
        @Query("fmt") format: String,
        @Query("query") query: String
    ): SearchItem?

    @GET("release")
    suspend fun getAlbums(
        @Query("fmt") format: String,
        @Query("artist") query: String
    ): AlbumItem?
}