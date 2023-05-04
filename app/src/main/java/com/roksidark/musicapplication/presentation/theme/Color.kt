package com.roksidark.musicapplication.presentation.theme

import androidx.compose.ui.graphics.Color

data class Colors(
    val primaryColor: Color,
    val secondaryBackgroundColor: Color,
    val headerTextColor: Color,
    val subtitleTextColor: Color,
    val primaryTextColor: Color,
    val backgroundColor: Color
)

val lightPalette = Colors(
    primaryColor = Color.White,
    secondaryBackgroundColor = Color(0XFFD1C2F8),
    headerTextColor = Color(0xFF505B5E),
    subtitleTextColor = Color(0xFF98ACB3),
    primaryTextColor = Color(0xFF345159),
    backgroundColor = Color(0XFFF2EEFE)
)