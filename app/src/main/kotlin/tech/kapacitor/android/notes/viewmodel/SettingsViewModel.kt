package tech.kapacitor.android.notes.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import tech.kapacitor.android.notes.data.datastore.SettingsDataStore
import tech.kapacitor.android.notes.data.datastore.defaultSettingsState
import tech.kapacitor.android.notes.data.datastore.model.NullableSettingsState
import tech.kapacitor.android.notes.data.datastore.model.SettingsState
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel
@Inject
constructor(
    private val settingsDataStore: SettingsDataStore,
) : ViewModel() {
    var isLoading: Boolean by mutableStateOf(value = true)
        private set

    private val settingsStateFlow: Flow<SettingsState> = settingsDataStore.flow

    @Composable
    fun getSettings(): State<SettingsState> =
        settingsStateFlow.collectAsStateWithLifecycle(
            initialValue =
            defaultSettingsState(),
        )

    fun updateSettings(settings: NullableSettingsState) {
        isLoading = true

        viewModelScope.launch {
            settingsDataStore.update(
                settingsState = settings,
            )
        }

        isLoading = false
    }

    init {
        viewModelScope.launch {
            settingsStateFlow.collect {
                isLoading = false
            }
        }
    }
}