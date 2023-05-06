package com.roksidark.core.util

import com.roksidark.core.util.Constant.USER_AGENT_CONTACT

//TODO
fun getUserAgent(): String {
    val appName = "MusicApplication"
    val appVersion = "1.0"
    return "$appName/$appVersion ( $USER_AGENT_CONTACT )"
}
