package tech.kapacitor.android.notes.ui.views.settings.about

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import tech.kapacitor.android.notes.R
import tech.kapacitor.android.notes.ui.composables.Card
import tech.kapacitor.android.notes.ui.composables.Icon
import tech.kapacitor.android.notes.ui.composables.ListItem
import tech.kapacitor.android.notes.ui.composition.LocalNavController
import tech.kapacitor.android.notes.ui.navigation.Route
import tech.kapacitor.android.notes.ui.navigation.aboutRoutes

@Composable
fun AboutView() {
    val navController: NavHostController = LocalNavController.current
    
    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier =
            Modifier
                .fillMaxSize(),
        ) {
            item {
                Column(
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(space = 16.dp),
                ) {
                    AppCard()

                    DevCard()
                }
            }

            items(aboutRoutes) { route: Route ->
                ListItem(
                    title = stringResource(id = route.title),
                    description = stringResource(id = route.description),
                    onClick = { navController.navigate(route = route.destination) },
                    firstItem = {
                        Icon(
                            icon = route.icon,
                        )
                    },
                )
            }
        }
    }
}

@Composable
fun AppCard() {
    val context: Context = LocalContext.current

    val activityLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult(),
        ) {}

    Card(
        modifier =
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        Row(
            modifier = Modifier.padding(all = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = context.packageManager.getApplicationIcon(context.packageName),
                contentDescription = null,
                modifier =
                Modifier
                    .clip(shape = CircleShape)
                    .size(size = 64.dp)
                    .padding(end = 16.dp),
            )

            Column {
                Text(
                    text = stringResource(id = R.string.app_name),
                    style = MaterialTheme.typography.titleLarge,
                )
                Text(
                    text =
                    context.packageManager
                        .getPackageInfo(
                            context.packageName,
                            0,
                        )?.versionName ?: "1.0.0",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }

        HorizontalDivider()

        val appGithubUrl: String = stringResource(id = R.string.app_github_url)
        ListItem(
            title = stringResource(id = R.string.about_view_on, "Github"),
            description =
            stringResource(
                id = R.string.about_view_on_desc,
                "Github",
            ),
            onClick = {
                activityLauncher.launch(Intent(Intent.ACTION_VIEW, Uri.parse(appGithubUrl)))
            },
            firstItem = {
                Icon(icon = painterResource(id = R.drawable.ic_github))
            },
        )
    }
}

@Composable
fun DevCard() {
    val activityLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult(),
        ) {}

    Card(
        modifier =
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        Row(
            modifier = Modifier.padding(all = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(id = R.string.dev),
                style = MaterialTheme.typography.titleLarge,
            )
        }

        HorizontalDivider()

        val devWebsiteUrl: String = stringResource(id = R.string.dev_website_url)
        ListItem(
            title = stringResource(id = R.string.dev_name),
            description = "@" + stringResource(id = R.string.dev_username),
            onClick = {
                activityLauncher.launch(Intent(Intent.ACTION_VIEW, Uri.parse(devWebsiteUrl)))
            },
            firstItem = {
                Icon(icon = Icons.Default.Person)
            },
        )

        val devGithubUrl: String = stringResource(id = R.string.dev_github_url)
        ListItem(
            title = stringResource(id = R.string.about_follow_on, "Github"),
            description =
            stringResource(
                id = R.string.about_follow_on_desc,
                "Github",
            ),
            onClick = {
                activityLauncher.launch(Intent(Intent.ACTION_VIEW, Uri.parse(devGithubUrl)))
            },
            firstItem = {
                Icon(icon = painterResource(id = R.drawable.ic_github))
            },
        )

        val devXUrl: String = stringResource(id = R.string.dev_x_url)
        ListItem(
            title = stringResource(id = R.string.about_follow_on, "X"),
            description =
            stringResource(
                id = R.string.about_follow_on_desc,
                "X",
            ),
            onClick = {
                activityLauncher.launch(Intent(Intent.ACTION_VIEW, Uri.parse(devXUrl)))
            },
            firstItem = {
                Icon(icon = painterResource(id = R.drawable.ic_x))
            },
        )
    }
}