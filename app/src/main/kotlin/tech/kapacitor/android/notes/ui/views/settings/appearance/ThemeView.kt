package tech.kapacitor.android.notes.ui.views.settings.appearance

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import tech.kapacitor.android.notes.R
import tech.kapacitor.android.notes.data.datastore.model.NullableSettingsState
import tech.kapacitor.android.notes.data.datastore.model.SettingsState
import tech.kapacitor.android.notes.ui.composables.ListItem
import tech.kapacitor.android.notes.ui.model.Theme
import tech.kapacitor.android.notes.viewmodel.SettingsViewModel

@Composable
fun ThemeView(settingsViewModel: SettingsViewModel = hiltViewModel()) {
    val context: Context = LocalContext.current

    val settingsState: SettingsState by settingsViewModel.getSettings()

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(Theme.entries) { theme: Theme ->
                ListItem(
                    title =
                    stringResource(
                        id =
                        context.resources.getIdentifier(
                            theme.type,
                            "string",
                            context.packageName,
                        ),
                    ),
                    onClick = {
                        settingsViewModel.updateSettings(
                            settings = NullableSettingsState(theme = theme),
                        )
                    },
                    firstItem = {
                        RadioButton(
                            selected = theme == settingsState.theme,
                            onClick = {
                                settingsViewModel.updateSettings(
                                    settings = NullableSettingsState(
                                        theme = theme,
                                    ),
                                )
                            },
                        )
                    },
                )
            }

            item {
                HorizontalDivider()
            }

            item {
                ListItem(
                    title = stringResource(id = R.string.appearance_theme_monochrome),
                    onClick = {
                        settingsViewModel.updateSettings(settings = NullableSettingsState(monochrome = !settingsState.monochrome))
                    },
                    lastItem = {
                        Switch(
                            checked = settingsState.monochrome,
                            onCheckedChange = { monochrome: Boolean ->
                                settingsViewModel.updateSettings(
                                    settings = NullableSettingsState(
                                        monochrome = monochrome
                                    )
                                )
                            })
                    })
            }
        }
    }
}