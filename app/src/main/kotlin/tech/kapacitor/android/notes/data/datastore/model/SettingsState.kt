package tech.kapacitor.android.notes.data.datastore.model

import androidx.compose.ui.graphics.Color
import tech.kapacitor.android.notes.ui.model.Theme

data class SettingsState(
    val serverUrl: String,
    val serverPort: Int,
    val serverPassword: String,
    val theme: Theme,
    val monochrome: Boolean,
    val materialYou: Boolean,
    val keyColor: Color,
    val wallpaperColors: Boolean,
)

data class NullableSettingsState(
    val serverUrl: String? = null,
    val serverPort: Int? = null,
    val serverPassword: String? = null,
    val theme: Theme? = null,
    val monochrome: Boolean? = null,
    val materialYou: Boolean? = null,
    val keyColor: Color? = null,
    val wallpaperColors: Boolean? = null,
)