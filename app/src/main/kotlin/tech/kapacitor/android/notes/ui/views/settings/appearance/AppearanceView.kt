package tech.kapacitor.android.notes.ui.views.settings.appearance

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import tech.kapacitor.android.notes.ui.composables.Icon
import tech.kapacitor.android.notes.ui.composables.ListItem
import tech.kapacitor.android.notes.ui.composition.LocalNavController
import tech.kapacitor.android.notes.ui.navigation.Route
import tech.kapacitor.android.notes.ui.navigation.appearanceRoutes

@Composable
fun AppearanceView() {
    val navController: NavHostController = LocalNavController.current

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(appearanceRoutes) { route: Route ->
                ListItem(
                    title = stringResource(id = route.title),
                    description =
                    stringResource(
                        id = route.description
                    ),
                    onClick = {
                        navController.navigate(route = route.destination)
                    },
                    firstItem = {
                        Icon(
                            icon = route.icon,
                        )
                    },
                )
            }
        }
    }
}