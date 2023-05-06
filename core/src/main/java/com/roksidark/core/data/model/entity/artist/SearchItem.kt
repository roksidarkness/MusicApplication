package com.roksidark.core.data.model.entity

import com.google.gson.annotations.SerializedName
//TODO
data class SearchItem (
    val created: String,
    val artists: List<Artist>?
)

data class Artist(
    val id: String?,
    val type: String?,
    val score: Int?,
    val name: String?,
    val country: String?,
    val area: Area?,
    @SerializedName("begin-area")
    val beginArea: Area?,
    @SerializedName("end-area")
    val endArea: Area?,
    @SerializedName("life-span")
    val lifeSpan: LifeSpan?,
    val tags: List<Tag>?
)

data class Area(
    val type: String?,
    val name: String?
)

data class Tag(
    val name: String?
)

data class LifeSpan(
    val begin: String?,
    val end: String?,
)