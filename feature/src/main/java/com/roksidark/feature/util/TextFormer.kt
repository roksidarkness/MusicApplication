package com.roksidark.feature.util

import com.roksidark.core.data.model.entity.artist.Tag

fun getTagsText(tags: List<Tag>?): String {
    var text = ""
    tags?.forEach {
        text += buildString {
            append("#")
        } + it.name + buildString {
            append(" ")
        }
    }
    return text
}