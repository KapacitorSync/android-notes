package tech.kapacitor.android.notes.ui.utils

import android.content.Context
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import tech.kapacitor.android.notes.data.datastore.model.SettingsState
import tech.kapacitor.android.notes.ui.model.Theme

@Composable
fun isDark(settingsState: SettingsState): Boolean {
    return when (settingsState.theme) {
        Theme.SYSTEM -> isSystemInDarkTheme()
        Theme.DARK -> true
        Theme.LIGHT -> false
    }
}

@Composable
fun getSystemRoundedCorners(): Dp {
    val context: Context = LocalContext.current

    val resId: Int =
        context.resources.getIdentifier("rounded_corner_radius", "dimen", "android")

    return if (resId > 0) {
        maxOf(dimensionResource(id = resId), 32.dp)
    } else {
        32.dp
    }
}