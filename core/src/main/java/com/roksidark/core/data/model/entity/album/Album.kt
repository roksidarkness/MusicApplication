package com.roksidark.core.data.model.entity.album

import com.google.gson.annotations.SerializedName

data class Album(
    val id: String?,
    val score: Int?,
    val title: String?,
    val status: String?,
    val disambiguation: String?,
    val packaging: String?,
    val date: String?,
    val country: String?,
    val barcode: String?,
    @SerializedName("track-count")
    val trackCount: Int?
)