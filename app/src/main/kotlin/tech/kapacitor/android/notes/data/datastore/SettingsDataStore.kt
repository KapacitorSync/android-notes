package tech.kapacitor.android.notes.data.datastore

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import tech.kapacitor.android.notes.data.datastore.model.NullableSettingsState
import tech.kapacitor.android.notes.data.datastore.model.SettingsState
import tech.kapacitor.android.notes.ui.model.Theme
import tech.kapacitor.android.notes.ui.utils.fromColor
import tech.kapacitor.android.notes.ui.utils.fromHex
import java.io.IOException
import javax.inject.Inject

private val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

fun defaultSettingsState(): SettingsState =
    SettingsState(
        serverUrl = "",
        serverPort = 3000,
        serverPassword = "",
        theme = Theme.SYSTEM,
        monochrome = false,
        materialYou = false,
        keyColor = Color(color = 0xFF000000),
        wallpaperColors = false,
    )

class SettingsDataStore
@Inject
constructor(
    @ApplicationContext private val context: Context,
) {
    private val dataStore: DataStore<Preferences> = context.settingsDataStore

    private object PreferenceKeys {
        val SERVER_URL: Preferences.Key<String> =
            stringPreferencesKey(name = "server_url")
        val SERVER_PORT: Preferences.Key<Int> =
            intPreferencesKey(name = "server_port")
        val SERVER_PASSWORD: Preferences.Key<String> =
            stringPreferencesKey(name = "server_password")

        val THEME: Preferences.Key<String> = stringPreferencesKey(name = "theme")
        val MONOCHROME: Preferences.Key<Boolean> =
            booleanPreferencesKey(name = "black_theme")
        val MATERIAL_YOU: Preferences.Key<Boolean> =
            booleanPreferencesKey(name = "material_you")
        val KEY_COLOR: Preferences.Key<String> =
            stringPreferencesKey(name = "key_color")
        val WALLPAPER_COLORS: Preferences.Key<Boolean> =
            booleanPreferencesKey(name = "wallpaper_colors")
    }

    suspend fun update(settingsState: NullableSettingsState) {
        val existingSettingsState: SettingsState = flow.first()

        dataStore.edit { preferences: MutablePreferences ->
            preferences[PreferenceKeys.SERVER_URL] =
                settingsState.serverUrl ?: existingSettingsState.serverUrl
            preferences[PreferenceKeys.SERVER_PORT] =
                settingsState.serverPort ?: existingSettingsState.serverPort
            preferences[PreferenceKeys.SERVER_PASSWORD] =
                settingsState.serverPassword ?: existingSettingsState.serverPassword

            preferences[PreferenceKeys.THEME] =
                settingsState.theme?.name ?: existingSettingsState.theme.name
            preferences[PreferenceKeys.MONOCHROME] =
                settingsState.monochrome ?: existingSettingsState.monochrome
            preferences[PreferenceKeys.MATERIAL_YOU] =
                settingsState.materialYou ?: existingSettingsState.materialYou
            preferences[PreferenceKeys.KEY_COLOR] =
                Color.fromColor(color = settingsState.keyColor ?: existingSettingsState.keyColor)
            preferences[PreferenceKeys.WALLPAPER_COLORS] =
                settingsState.wallpaperColors ?: existingSettingsState.wallpaperColors
        }
    }

    val flow: Flow<SettingsState> =
        dataStore
            .data
            .catch { exception: Throwable ->
                if (exception is IOException) {
                    defaultSettingsState()
                } else {
                    throw exception
                }
            }.map { preferences: Preferences ->
                SettingsState(
                    serverUrl = preferences[PreferenceKeys.SERVER_URL]
                        ?: defaultSettingsState().serverUrl,
                    serverPort = preferences[PreferenceKeys.SERVER_PORT]
                        ?: defaultSettingsState().serverPort,
                    serverPassword = preferences[PreferenceKeys.SERVER_PASSWORD]
                        ?: defaultSettingsState().serverPassword,

                    theme = preferences[PreferenceKeys.THEME]?.let { type: String ->
                        Theme.valueOf(value = type)
                    }
                        ?: defaultSettingsState().theme,
                    monochrome = preferences[PreferenceKeys.MONOCHROME]
                        ?: defaultSettingsState().monochrome,
                    materialYou = preferences[PreferenceKeys.MATERIAL_YOU]
                        ?: defaultSettingsState().materialYou,
                    keyColor = Color.fromHex(
                        hexColor = preferences[PreferenceKeys.KEY_COLOR]
                            ?: Color.fromColor(defaultSettingsState().keyColor)
                    ),
                    wallpaperColors = preferences[PreferenceKeys.WALLPAPER_COLORS]
                        ?: defaultSettingsState().wallpaperColors,
                )
            }
}