package tech.kapacitor.android.notes.ui.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

fun Color.Companion.fromHex(hexColor: String): Color {
    val color: String = hexColor.replace(oldValue = "#", newValue = "")
    return Color(
        android.graphics.Color.parseColor(
            "#${
                when (color.length) {
                    6 -> "FF$color"
                    8 -> color
                    else -> throw IllegalArgumentException("Invalid hex color: $hexColor")
                }
            }"
        )
    )
}

fun Color.Companion.fromColor(color: Color): String =
    String.format(format = "#%08X", color.toArgb())