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
import tech.kapacitor.android.notes.data.datastore.AppDataStore
import tech.kapacitor.android.notes.data.datastore.defaultAppState
import tech.kapacitor.android.notes.data.datastore.model.AppState
import tech.kapacitor.android.notes.data.datastore.model.NullableAppState
import javax.inject.Inject

@HiltViewModel
class AppViewModel
@Inject
constructor(
    private val appDataStore: AppDataStore
) : ViewModel() {
    var isLoading: Boolean by mutableStateOf(value = true)
        private set

    var isDrawerEnabled: Boolean by mutableStateOf(value = true)
        private set

    val appDataStoreFlow: Flow<AppState> = appDataStore.flow

    @Composable
    fun getAppDataStore(): State<AppState> =
        appDataStoreFlow.collectAsStateWithLifecycle(initialValue = defaultAppState())

    fun updateAppDataStore(app: NullableAppState) {
        isLoading = true

        viewModelScope.launch {
            appDataStore.update(
                appState = app,
            )

        }

        isLoading = false
    }

    fun setIsDrawerEnabled(enable: Boolean) {
        isDrawerEnabled = enable
    }

    init {
        viewModelScope.launch {
            appDataStoreFlow.collect { appState: AppState ->
                isLoading = false

                if (!appState.isSetupDone) {
                    setIsDrawerEnabled(enable = false)
                }
            }
        }
    }
}