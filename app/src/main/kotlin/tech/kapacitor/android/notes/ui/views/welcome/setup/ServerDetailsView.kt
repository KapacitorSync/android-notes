package tech.kapacitor.android.notes.ui.views.welcome.setup

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import tech.kapacitor.android.notes.R
import tech.kapacitor.android.notes.ui.composables.Icon
import tech.kapacitor.android.notes.ui.composition.LocalNavController
import tech.kapacitor.android.notes.ui.navigation.Route
import tech.kapacitor.android.notes.ui.validation.ValidationError
import tech.kapacitor.android.notes.ui.validation.model.ServerDetailsModel
import tech.kapacitor.android.notes.viewmodel.welcome.setup.ServerDetailsSubmitReturn
import tech.kapacitor.android.notes.viewmodel.welcome.setup.ServerDetailsSubmitReturn.InvalidCredentials
import tech.kapacitor.android.notes.viewmodel.welcome.setup.ServerDetailsSubmitReturn.Success
import tech.kapacitor.android.notes.viewmodel.welcome.setup.ServerDetailsViewModel

@Composable
fun ServerDetailsView(
    serverDetailsViewModel: ServerDetailsViewModel = hiltViewModel<ServerDetailsViewModel>()
) {
    val context: Context = LocalContext.current
    val navController: NavHostController = LocalNavController.current

    val data: ServerDetailsModel = serverDetailsViewModel.data
    val validationError: ValidationError? = serverDetailsViewModel.validationError

    val coroutineScope: CoroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        if (serverDetailsViewModel.isLoading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(space = 16.dp),
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 16.dp),
                ) {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = data.url,
                        label = {
                            Text(text = stringResource(id = R.string.server_details_url))
                        },
                        leadingIcon = {
                            Icon(icon = Icons.Filled.Language)
                        },
                        singleLine = true,
                        isError = validationError?.property == "url",
                        onValueChange = { text: String ->
                            serverDetailsViewModel.updateData(newData = data.copy(url = text))
                        },
                    )

                    if (validationError != null && validationError.property == "url") Text(
                        text = stringResource(
                            id = context.resources.getIdentifier(
                                "input_${validationError.message}",
                                "string",
                                context.packageName
                            ), stringResource(
                                id = context.resources.getIdentifier(
                                    "server_details_${validationError.property}",
                                    "string",
                                    context.packageName
                                )
                            )
                        )
                    )
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                ) {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = data.port,
                        label = {
                            Text(text = stringResource(id = R.string.server_details_port))
                        },
                        leadingIcon = {
                            Icon(icon = Icons.Filled.Numbers)
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = validationError?.property == "port",
                        onValueChange = { text: String ->
                            serverDetailsViewModel.updateData(newData = data.copy(port = text))
                        },
                    )

                    if (validationError != null && validationError.property == "port") Text(
                        text = stringResource(
                            id = context.resources.getIdentifier(
                                "input_${validationError.message}",
                                "string",
                                context.packageName
                            ), stringResource(
                                id = context.resources.getIdentifier(
                                    "server_details_${validationError.property}",
                                    "string",
                                    context.packageName
                                )
                            )
                        )
                    )
                }

            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End
        ) {
            val serverFail: String = stringResource(id = R.string.server_fail)
            val invalidCredentials: String =
                stringResource(id = R.string.server_invalid_credentials)
            Button(onClick = {
                if (serverDetailsViewModel.validateData() == null) {
                    coroutineScope.launch {
                        val result: ServerDetailsSubmitReturn =
                            runBlocking { serverDetailsViewModel.submit() }

                        if (result == Success) {
                            navController.navigate(route = Route.ServerPassword.destination)
                        } else {
                            Toast.makeText(
                                context,
                                if (result == InvalidCredentials) invalidCredentials else serverFail,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }, enabled = validationError == null) {
                Text(text = stringResource(id = R.string.next))
            }
        }

    }
}