package com.kysportsblogs.android.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
        primary = blue500,
        primaryVariant = blue700,
        onPrimary = Color.White,
        secondary = grey400,
        surface = grey800,
        background = grey800
)

private val LightColorPalette = lightColors(
        primary = blue500,
        primaryVariant = blue700,
        onPrimary = Color.White,
        secondary = grey400,
        secondaryVariant = grey200,
        onSecondary = Color.Black,
        error = amber800,
        onError = Color.Black
)

val Colors.primaryOnSurface: Color get() = if (isLight) primary else onSurface

val Colors.surfaceSecondary: Color
    get() = if (isLight) Color.Black.copy(0.05F) else Color.Black.copy(0.2F)

@Composable
fun KsbTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
            colors = colors,
            typography = typography,
            shapes = shapes,
            content = content
    )
}