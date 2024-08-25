package tech.kapacitor.android.notes.viewmodel.welcome.setup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import tech.kapacitor.android.notes.data.datastore.AppDataStore
import tech.kapacitor.android.notes.data.datastore.SettingsDataStore
import tech.kapacitor.android.notes.data.datastore.model.NullableAppState
import tech.kapacitor.android.notes.data.datastore.model.NullableSettingsState
import tech.kapacitor.android.notes.data.remote.error.APIErrorReason
import tech.kapacitor.android.notes.data.remote.model.ApiResult
import tech.kapacitor.android.notes.data.remote.model.ApiResult.Error
import tech.kapacitor.android.notes.data.remote.model.GetVersionEndpointResponse
import tech.kapacitor.android.notes.data.remote.service.ApiTestService
import tech.kapacitor.android.notes.ui.validation.ValidateData
import tech.kapacitor.android.notes.ui.validation.ValidationError
import tech.kapacitor.android.notes.ui.validation.model.ServerPasswordModel
import tech.kapacitor.android.notes.viewmodel.welcome.setup.ServerPasswordSubmitReturn.Fail
import tech.kapacitor.android.notes.viewmodel.welcome.setup.ServerPasswordSubmitReturn.InvalidCredentials
import tech.kapacitor.android.notes.viewmodel.welcome.setup.ServerPasswordSubmitReturn.Success
import javax.inject.Inject

enum class ServerPasswordSubmitReturn {
    Success,
    Fail,
    InvalidCredentials
}

@HiltViewModel
class ServerPasswordViewModel @Inject constructor(
    private val settingsDataStore: SettingsDataStore,
    private val appDataStore: AppDataStore,
    private val apiTestService: ApiTestService
) :
    ViewModel() {
    var isLoading: Boolean by mutableStateOf(value = false)
        private set

    var data: ServerPasswordModel by mutableStateOf(value = ServerPasswordModel())
        private set

    var validationError: ValidationError? by mutableStateOf(value = null)
        private set

    fun validateData(validationData: ServerPasswordModel = data): ValidationError? {
        validationError =
            ValidateData(model = ServerPasswordModel::class).validate(validationData)

        return validationError
    }

    fun updateData(newData: ServerPasswordModel) {
        validateData(
            validationData = newData
        )

        data = newData
    }

    suspend fun submit(): ServerPasswordSubmitReturn {
        if (validateData() != null) {
            return InvalidCredentials
        }

        try {
            isLoading = true

            val result: ApiResult<GetVersionEndpointResponse> = runBlocking {
                apiTestService.getVersion(
                    serverPassword = data.password
                )
            }


            if (result is ApiResult.Success) {
                viewModelScope.launch {
                    settingsDataStore.update(
                        settingsState = NullableSettingsState(
                            serverPassword = data.password
                        )
                    )

                    appDataStore.update(
                        appState = NullableAppState(
                            isSetupDone = true
                        )
                    )
                }
            }

            isLoading = false

            return when (result) {
                is ApiResult.Success -> Success
                is Error -> when (result.reason) {
                    APIErrorReason.USER -> InvalidCredentials
                    APIErrorReason.SERVER -> Fail
                }

                is ApiResult.Fatal -> Fail
            }
        } catch (_: Exception) {
            return Fail
        }
    }
}