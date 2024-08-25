package tech.kapacitor.android.notes.ui.views.user

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import tech.kapacitor.android.notes.R
import tech.kapacitor.android.notes.data.datastore.model.AuthState
import tech.kapacitor.android.notes.data.datastore.model.NullableAuthState
import tech.kapacitor.android.notes.ui.composables.Card
import tech.kapacitor.android.notes.ui.composables.Icon
import tech.kapacitor.android.notes.ui.composition.LocalNavController
import tech.kapacitor.android.notes.ui.navigation.Route
import tech.kapacitor.android.notes.viewmodel.UsersViewModel

@Composable
fun UsersView(
    usersViewModel: UsersViewModel = hiltViewModel<UsersViewModel>(),
    compact: Boolean = false
) {
    val navController: NavHostController = LocalNavController.current

    val authState: AuthState by usersViewModel.getUserDataStore()

    Column(
        modifier = Modifier.then(
            if (!compact) {
                Modifier.fillMaxSize()
            } else Modifier.fillMaxWidth()
        )
    ) {
        LazyColumn(
            modifier = Modifier
                .then(
                    if (!compact) {
                        Modifier.weight(weight = 1f)
                    } else Modifier
                )
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(space = 8.dp),
        ) {
            if (authState.users.isNotEmpty()) {
                itemsIndexed(authState.users) { index: Int, username: String ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 16.dp,
                                end = 16.dp,
                                top = if (index == 0) 16.dp else 0.dp,
                                bottom = if (index == authState.users.size - 1) 16.dp else 0.dp
                            )
                    ) {
                        Card(
                            onClick = {
                                val token: String =
                                    authState.tokens.map { (_username: String, _token: String) ->
                                        if (username == _username) {
                                            _token
                                        } else {
                                            ""
                                        }
                                    }.first()

                                usersViewModel.updateUserDataStore(
                                    user = NullableAuthState(
                                        username = username,
                                        token = token,
                                    )
                                )
                            }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(height = 64.dp)
                                    .padding(all = 16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = username)

                                Box(
                                    modifier = Modifier
                                        .size(size = 32.dp)
                                ) {
                                    val isSelected: () -> Boolean = {
                                        authState.username == username
                                    }

                                    val animatedAlpha: Float by animateFloatAsState(targetValue = if (isSelected.invoke()) 1f else 0f)

                                    val containerColor: Color =
                                        MaterialTheme.colorScheme.primaryContainer
                                    val animatedContainerSize: Dp by animateDpAsState(targetValue = if (isSelected.invoke()) 32.dp else 0.dp)

                                    val animatedIconSize: Dp by animateDpAsState(targetValue = if (isSelected.invoke()) 24.dp else 0.dp)

                                    if (authState.username == username) {
                                        Box(
                                            modifier = Modifier
                                                .graphicsLayer {
                                                    alpha = animatedAlpha
                                                }
                                                .align(alignment = Alignment.Center)
                                                .size(size = animatedContainerSize)
                                                .clip(shape = CircleShape)
                                                .drawBehind {
                                                    drawCircle(color = containerColor)
                                                }
                                                .padding(all = 4.dp)
                                        ) {
                                            Icon(
                                                icon = Icons.Filled.Done,
                                                modifier = Modifier
                                                    .size(size = animatedIconSize)
                                                    .align(alignment = Alignment.Center)
                                                    .graphicsLayer {
                                                        alpha = animatedAlpha
                                                    },
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = 16.dp)
                    ) {
                        Text(text = stringResource(id = R.string.users_no_user_found))
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(space = 16.dp)
            ) {
                Button(modifier = Modifier.weight(weight = 1f), onClick = {
                    navController.navigate(route = Route.AddUser.destination)
                }) {
                    Text(text = stringResource(id = R.string.add_user))
                }

                Button(modifier = Modifier.weight(weight = 1f), onClick = {
                    navController.navigate(route = Route.CreateUser.destination)
                }) {
                    Text(text = stringResource(id = R.string.create_user))
                }
            }
        }
    }
}