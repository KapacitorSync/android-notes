package tech.kapacitor.android.notes.viewmodel.user

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import tech.kapacitor.android.notes.data.datastore.AuthDataStore
import tech.kapacitor.android.notes.data.datastore.SettingsDataStore
import tech.kapacitor.android.notes.data.remote.service.ApiTestService
import tech.kapacitor.android.notes.ui.validation.ValidateData
import tech.kapacitor.android.notes.ui.validation.ValidationError
import tech.kapacitor.android.notes.ui.validation.model.ServerDetailsModel
import tech.kapacitor.android.notes.viewmodel.welcome.setup.ServerDetailsSubmitReturn
import tech.kapacitor.android.notes.viewmodel.welcome.setup.ServerDetailsSubmitReturn.InvalidCredentials
import javax.inject.Inject

class CreateUserViewModel @Inject constructor(
    private val authDataStore: AuthDataStore,
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
        } catch (_: Exception) {
        }
    }
}