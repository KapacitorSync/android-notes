package tech.kapacitor.android.notes.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import tech.kapacitor.android.notes.R
import tech.kapacitor.android.notes.ui.composition.LocalDrawerState
import tech.kapacitor.android.notes.ui.composition.LocalNavController
import tech.kapacitor.android.notes.ui.navigation.Route
import tech.kapacitor.android.notes.ui.navigation.drawerRoutes
import tech.kapacitor.android.notes.ui.utils.getSystemRoundedCorners
import tech.kapacitor.android.notes.ui.views.user.UsersBottomSheet
import tech.kapacitor.android.notes.viewmodel.AppViewModel

@Composable
fun Drawer(
    appViewModel: AppViewModel = hiltViewModel<AppViewModel>(),
    content: @Composable () -> Unit,
) {
    val isDrawerEnabled: Boolean = appViewModel.isDrawerEnabled
    if (isDrawerEnabled) {
        val windowSizeClass: WindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass

        val drawerState: DrawerState = LocalDrawerState.current
        when (windowSizeClass.windowWidthSizeClass) {
            WindowWidthSizeClass.COMPACT, WindowWidthSizeClass.MEDIUM ->
                ModalNavigationDrawer(
                    drawerContent = {
                        ModalDrawerSheet(
                            modifier =
                            Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(fraction = 0.85f)
                                .clip(
                                    shape =
                                    RoundedCornerShape(
                                        size = getSystemRoundedCorners(),
                                    ),
                                ),
                            drawerState = drawerState,
                        ) {
                            DrawerContent()
                        }
                    },
                    gesturesEnabled = true,
                    drawerState = drawerState,
                ) {
                    content()
                }

            WindowWidthSizeClass.EXPANDED ->
                PermanentNavigationDrawer(drawerContent = {
                    PermanentDrawerSheet(
                        modifier =
                        Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(fraction = 0.35f),
                        drawerContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                    ) {
                        DrawerContent()
                    }
                }) {
                    content()
                }
        }
    } else {
        content()
    }
}

@Composable
fun DrawerContent() {
    val navController: NavHostController = LocalNavController.current

    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val drawerState: DrawerState = LocalDrawerState.current

    val windowSizeClass: WindowWidthSizeClass =
        currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass

    var showUsersBottomSheet: Boolean by remember { mutableStateOf(value = false) }
    if (showUsersBottomSheet) {
        UsersBottomSheet(onDismissRequest = {
            showUsersBottomSheet = false
        })
    }

    LaunchedEffect(
        key1 = navController.currentBackStackEntryAsState().value?.destination?.route
            ?: navController.currentBackStackEntryAsState().value
    ) {
        if (windowSizeClass != WindowWidthSizeClass.EXPANDED) {
            coroutineScope.launch {
                drawerState.apply {
                    close()
                }
            }
        }

        showUsersBottomSheet = false
    }

    Row(
        modifier =
        Modifier
            .fillMaxWidth()
            .height(height = 64.dp)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.titleLarge,
        )

        if (windowSizeClass != WindowWidthSizeClass.EXPANDED) {
            IconButton(onClick = {
                coroutineScope.launch {
                    drawerState.apply {
                        close()
                    }
                }
            }) {
                Icon(
                    icon = Icons.Filled.Close,
                )
            }
        }
    }

    Column(
        modifier =
        Modifier
            .padding(vertical = 16.dp)
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Bottom,
    ) {
        val navBackStackEntry: NavBackStackEntry? by navController.currentBackStackEntryAsState()
        val currentDestination: String =
            navBackStackEntry?.destination?.route ?: Route.Home.destination

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column {
                drawerRoutes.map { route: Route ->
                    val selected: Boolean =
                        currentDestination.contains(route.destination, ignoreCase = true)

                    NavigationDrawerItem(
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                        selected = selected,
                        onClick = {
                            if (currentDestination != route.destination) {
                                navController.navigate(
                                    route = route.destination,
                                )
                            }
                        },
                        label = { Text(text = stringResource(id = route.title)) },
                        icon = {
                            Icon(icon = if (selected) route.icon else route.unselectedIcon)
                        },
                    )
                }
            }

            HorizontalDivider()

            NavigationDrawerItem(
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                label = {
                    Text(text = stringResource(id = R.string.users_change_user))
                },
                selected = true,
                icon = {
                    Icon(icon = Route.Users.icon)
                },
                onClick = {
                    if (windowSizeClass != WindowWidthSizeClass.EXPANDED) {
                        coroutineScope.launch {
                            drawerState.apply {
                                close()
                            }
                        }
                    }

                    showUsersBottomSheet = true
                },
            )
        }
    }
}