package com.roksidark.core.data.model.entity.artist

import com.google.gson.annotations.SerializedName

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