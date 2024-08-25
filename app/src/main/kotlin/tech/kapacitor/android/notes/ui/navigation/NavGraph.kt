package tech.kapacitor.android.notes.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.navigation
import tech.kapacitor.android.notes.data.datastore.model.AppState
import tech.kapacitor.android.notes.ui.composables.animatedComposableBuilder
import tech.kapacitor.android.notes.ui.composition.LocalNavController
import tech.kapacitor.android.notes.ui.views.HomeView
import tech.kapacitor.android.notes.ui.views.settings.GeneralSettingsView
import tech.kapacitor.android.notes.ui.views.settings.LanguagesView
import tech.kapacitor.android.notes.ui.views.settings.SettingsView
import tech.kapacitor.android.notes.ui.views.settings.about.AboutView
import tech.kapacitor.android.notes.ui.views.settings.about.LicensesView
import tech.kapacitor.android.notes.ui.views.settings.appearance.AppearanceView
import tech.kapacitor.android.notes.ui.views.settings.appearance.MaterialYouView
import tech.kapacitor.android.notes.ui.views.settings.appearance.ThemeView
import tech.kapacitor.android.notes.ui.views.user.AddUserView
import tech.kapacitor.android.notes.ui.views.user.CreateUserView
import tech.kapacitor.android.notes.ui.views.user.UsersView
import tech.kapacitor.android.notes.ui.views.welcome.WelcomeView
import tech.kapacitor.android.notes.ui.views.welcome.setup.ServerDetailsView
import tech.kapacitor.android.notes.ui.views.welcome.setup.ServerPasswordView
import tech.kapacitor.android.notes.viewmodel.AppViewModel

@Composable
fun AppNavGraph(appViewModel: AppViewModel = hiltViewModel<AppViewModel>()) {
    val navController: NavHostController = LocalNavController.current

    val appState: AppState by appViewModel.getAppDataStore()

    NavHost(
        navController = navController,
        startDestination = if (appState.isSetupDone) Route.Home.destination else Route.Welcome.destination,
    ) {
        val animatedComposable =
            animatedComposableBuilder()

        navigation(
            startDestination = Route.Welcome.destination,
            route = "_${Route.Welcome.destination}",
        ) {
            animatedComposable(
                Route.Welcome.destination,
                null,
                null,
            ) {
                WelcomeView()
            }

            animatedComposable(
                Route.ServerDetails.destination,
                null,
                null,
            ) {
                ServerDetailsView()
            }

            animatedComposable(
                Route.ServerPassword.destination,
                null,
                null,
            ) {
                ServerPasswordView(
                )
            }
        }

        navigation(
            startDestination = Route.Users.destination,
            route = "_${Route.Users.destination}",
        ) {
            animatedComposable(
                Route.Users.destination,
                null,
                null
            ) {
                UsersView()
            }

            animatedComposable(
                Route.AddUser.destination,
                null,
                null
            ) {
                AddUserView()
            }

            animatedComposable(
                Route.CreateUser.destination,
                null,
                null
            ) {
                CreateUserView()
            }
        }

        animatedComposable(
            Route.Home.destination,
            null,
            null,
        ) {
            HomeView()
        }

        navigation(
            startDestination = Route.Settings.destination,
            route = "_${Route.Settings.destination}",
        ) {
            animatedComposable(
                Route.Settings.destination,
                null,
                null,
            ) {
                SettingsView()
            }

            animatedComposable(
                Route.GeneralSettings.destination,
                null,
                null,
            ) {
                GeneralSettingsView()
            }

            animatedComposable(
                Route.Languages.destination,
                null,
                null,
            ) {
                LanguagesView()
            }

            navigation(
                startDestination = Route.Appearance.destination,
                route = "_${Route.Appearance.destination}",
            ) {
                animatedComposable(
                    Route.Appearance.destination,
                    null,
                    null,
                ) {
                    AppearanceView()
                }
                animatedComposable(
                    Route.Theme.destination,
                    null,
                    null,
                ) {
                    ThemeView()
                }
                animatedComposable(
                    Route.MaterialYou.destination,
                    null,
                    null,
                ) {
                    MaterialYouView()
                }
            }

            navigation(
                startDestination = Route.About.destination,
                route = "_${Route.About.destination}",
            ) {
                animatedComposable(
                    Route.About.destination,
                    null,
                    null,
                ) {
                    AboutView()
                }
                animatedComposable(
                    Route.Licenses.destination,
                    null,
                    null,
                ) {
                    LicensesView()
                }
            }
        }
    }
}