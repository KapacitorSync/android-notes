package tech.kapacitor.android.notes.data.remote.client

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json
import tech.kapacitor.android.notes.BuildConfig
import tech.kapacitor.android.notes.data.datastore.AuthDataStore
import tech.kapacitor.android.notes.data.datastore.SettingsDataStore
import tech.kapacitor.android.notes.data.datastore.model.SettingsState
import javax.inject.Inject

class KtorClient
@Inject
constructor(
    private val settingsDataStore: SettingsDataStore,
    private val authDataStore: AuthDataStore
) {
    suspend fun getClient(): HttpClient {
        val settingsState: SettingsState = settingsDataStore.flow.first()
        val user: tech.kapacitor.android.notes.data.datastore.model.AuthState =
            authDataStore.flow.first()

        val client: HttpClient = HttpClient(OkHttp) {
            defaultRequest {
                url {
                    protocol = URLProtocol.HTTP
                    host = settingsState.serverUrl
                    port = settingsState.serverPort
                }
            }

            install(plugin = Auth) {
                bearer {
                    loadTokens {
                        BearerTokens(accessToken = user.token, refreshToken = "")
                    }
                }
            }

            install(ContentNegotiation) {
                json(
                    json =
                    Json {
                        ignoreUnknownKeys = true
                    },
                )
            }

            if (BuildConfig.DEBUG) {
                install(Logging) {
                    logger = Logger.DEFAULT
                    level = LogLevel.HEADERS
                    sanitizeHeader { header: String -> header == HttpHeaders.Authorization }
                    sanitizeHeader { header: String -> header == "X-Server-Password" }
                    logger =
                        object : Logger {
                            override fun log(message: String) {
                                Log.i("KtorClient", message)
                            }
                        }
                }
            }
        }

        return client
    }
}