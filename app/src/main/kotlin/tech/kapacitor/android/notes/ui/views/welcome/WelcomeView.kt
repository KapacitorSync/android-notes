package tech.kapacitor.android.notes.ui.views.welcome

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import tech.kapacitor.android.notes.R
import tech.kapacitor.android.notes.ui.composition.LocalNavController
import tech.kapacitor.android.notes.ui.navigation.Route

@Composable
fun WelcomeView() {
    val navController: NavHostController = LocalNavController.current

    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier =
            Modifier
                .fillMaxSize()
                .padding(all = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
        ) {
            Button(onClick = {
                navController.navigate(route = Route.ServerDetails.destination)
            }) {
                Text(text = stringResource(id = R.string.welcome_lets_get_started))
            }
        }
    }
}