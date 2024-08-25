package tech.kapacitor.android.notes.ui.composition

import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf

val LocalDrawerState: ProvidableCompositionLocal<DrawerState> =
    compositionLocalOf { error("Not provided") }

@Composable
fun ProvideDrawerState(drawerState: DrawerState, content: @Composable () -> Unit) {
    CompositionLocalProvider(
        value = LocalDrawerState provides drawerState,
    ) {
        content()
    }
}