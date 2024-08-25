package tech.kapacitor.android.notes

import android.os.Bundle
import android.view.View
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.DrawerValue.Closed
import androidx.compose.material3.rememberDrawerState
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import tech.kapacitor.android.notes.ui.composables.AppLayout
import tech.kapacitor.android.notes.ui.composition.ProvideDrawerState
import tech.kapacitor.android.notes.ui.composition.ProvideNavController
import tech.kapacitor.android.notes.ui.navigation.AppNavGraph
import tech.kapacitor.android.notes.ui.theme.AppTheme
import tech.kapacitor.android.notes.viewmodel.AppViewModel
import tech.kapacitor.android.notes.viewmodel.SettingsViewModel

@AndroidEntryPoint
class AppActivity : AppCompatActivity() {
    private val appViewModel: AppViewModel by viewModels<AppViewModel>()
    private val settingsViewModel: SettingsViewModel by viewModels<SettingsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                appViewModel.isLoading || settingsViewModel.isLoading
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(
            window.decorView,
        ) { view: View, insets: WindowInsetsCompat ->
            view.setPadding(0, 0, 0, 0)
            insets
        }

        enableEdgeToEdge()

        setContent {
            AppTheme {
                ProvideNavController(navController = rememberNavController()) {
                    ProvideDrawerState(drawerState = rememberDrawerState(initialValue = Closed)) {
                        AppLayout {
                            AppNavGraph()
                        }
                    }
                }
            }
        }
    }
}