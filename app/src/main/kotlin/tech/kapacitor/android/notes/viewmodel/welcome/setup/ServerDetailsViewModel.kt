package tech.kapacitor.android.notes.viewmodel.welcome.setup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import tech.kapacitor.android.notes.data.datastore.SettingsDataStore
import tech.kapacitor.android.notes.data.datastore.model.NullableSettingsState
import tech.kapacitor.android.notes.data.remote.error.APIErrorReason
import tech.kapacitor.android.notes.data.remote.model.ApiResult
import tech.kapacitor.android.notes.data.remote.model.ApiResult.Error
import tech.kapacitor.android.notes.data.remote.model.TestEndpointResponse
import tech.kapacitor.android.notes.data.remote.service.ApiTestService
import tech.kapacitor.android.notes.ui.validation.ValidateData
import tech.kapacitor.android.notes.ui.validation.ValidationError
import tech.kapacitor.android.notes.ui.validation.model.ServerDetailsModel
import tech.kapacitor.android.notes.viewmodel.welcome.setup.ServerDetailsSubmitReturn.Fail
import tech.kapacitor.android.notes.viewmodel.welcome.setup.ServerDetailsSubmitReturn.InvalidCredentials
import tech.kapacitor.android.notes.viewmodel.welcome.setup.ServerDetailsSubmitReturn.Success
import javax.inject.Inject

enum class ServerDetailsSubmitReturn {
    Success,
    Fail,
    InvalidCredentials
}

@HiltViewModel
class ServerDetailsViewModel @Inject constructor(
    private val settingsDataStore: SettingsDataStore,
    private val apiTestService: ApiTestService
) :
    ViewModel() {
    var isLoading: Boolean by mutableStateOf(value = false)
        private set

    var data: ServerDetailsModel by mutableStateOf(value = ServerDetailsModel())
        private set

    var validationError: ValidationError? by mutableStateOf(value = null)
        private set

    fun validateData(validationData: ServerDetailsModel = data): ValidationError? {
        validationError =
            ValidateData(model = ServerDetailsModel::class).validate(validationData)

        return validationError
    }

    fun updateData(newData: ServerDetailsModel) {
        validateData(
            validationData = newData
        )

        data = newData
    }

    suspend fun submit(): ServerDetailsSubmitReturn {
        if (validateData() != null) {
            return InvalidCredentials
        }

        try {
            val result: ApiResult<TestEndpointResponse> = runBlocking {
                isLoading = true

                apiTestService.test(
                    serverUrl = data.url,
                    serverPort = data.port.toInt()
                )
            }

            if (result is ApiResult.Success) {
                viewModelScope.launch {
                    settingsDataStore.update(
                        settingsState = NullableSettingsState(
                            serverUrl = data.url,
                            serverPort = data.port.toInt()
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