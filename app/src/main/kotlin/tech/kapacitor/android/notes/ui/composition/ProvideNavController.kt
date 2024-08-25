package tech.kapacitor.android.notes.ui.composition

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController

val LocalNavController: ProvidableCompositionLocal<NavHostController> =
    compositionLocalOf { error("Not provided") }

@Composable
fun ProvideNavController(navController: NavHostController, content: @Composable () -> Unit) {
    CompositionLocalProvider(
        value = LocalNavController provides navController,
    ) {
        content()
    }
}