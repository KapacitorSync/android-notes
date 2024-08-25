package tech.kapacitor.android.notes.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import tech.kapacitor.android.notes.data.datastore.model.AppState
import tech.kapacitor.android.notes.data.datastore.model.NullableAppState
import java.io.IOException
import javax.inject.Inject

private val Context.appStateDataStore: DataStore<Preferences> by preferencesDataStore(name = "app")

fun defaultAppState(): AppState = AppState(
    isSetupDone = false,
)

class AppDataStore @Inject
constructor(
    @ApplicationContext private val context: Context,
) {
    private val dataStore: DataStore<Preferences> = context.appStateDataStore

    private object PreferenceKeys {
        val IS_SETUP_DONE: Preferences.Key<Boolean> = booleanPreferencesKey(name = "is_setup_done")
    }

    suspend fun update(appState: NullableAppState) {
        val existingAppState: AppState = flow.first()

        dataStore.edit { preferences: MutablePreferences ->
            preferences[PreferenceKeys.IS_SETUP_DONE] =
                appState.isSetupDone ?: existingAppState.isSetupDone
        }
    }

    val flow: Flow<AppState> = dataStore.data.catch { exception: Throwable ->
        if (exception is IOException) {
            defaultAppState()
        } else {
            throw exception
        }
    }.map { preferences: Preferences ->
        AppState(
            isSetupDone = preferences[PreferenceKeys.IS_SETUP_DONE]
                ?: defaultAppState().isSetupDone
        )
    }
}