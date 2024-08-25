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
import tech.kapacitor.android.notes.data.datastore.AuthDataStore
import tech.kapacitor.android.notes.data.datastore.defaultAuthState
import tech.kapacitor.android.notes.data.datastore.model.AuthState
import tech.kapacitor.android.notes.data.datastore.model.NullableAuthState
import javax.inject.Inject

@HiltViewModel
class UsersViewModel
@Inject
constructor(
    private val authDataStore: AuthDataStore
) : ViewModel() {
    var isLoading: Boolean by mutableStateOf(value = false)
        private set

    private val userDataStoreFlow: Flow<AuthState> =
        authDataStore.flow

    @Composable
    fun getUserDataStore(): State<AuthState> =
        userDataStoreFlow.collectAsStateWithLifecycle(
            initialValue = defaultAuthState(),
        )

    fun updateUserDataStore(user: NullableAuthState) {
        isLoading = true

        viewModelScope.launch {
            authDataStore.update(
                authState = user,
            )
        }

        isLoading = false
    }

    init {
        viewModelScope.launch {
            userDataStoreFlow.collect {
                isLoading = false
            }
        }
    }
}