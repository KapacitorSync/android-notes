package tech.kapacitor.android.notes.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import tech.kapacitor.android.notes.data.datastore.model.AuthState
import tech.kapacitor.android.notes.data.datastore.model.NullableAuthState
import java.io.IOException
import javax.inject.Inject

fun defaultAuthState(): AuthState = AuthState(
    username = "",
    token = "",

    users = listOf(
        "budchirp",
    ),
    tokens = listOf(
        listOf(
            "budchirp", "token"
        ),
    )
)

private val Context.authDataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")

class AuthDataStore @Inject
constructor(
    @ApplicationContext private val context: Context,
) {
    private val dataStore: DataStore<Preferences> = context.authDataStore

    private object PreferenceKeys {
        val USERNAME: Preferences.Key<String> = stringPreferencesKey(name = "username")
        val TOKEN: Preferences.Key<String> = stringPreferencesKey(name = "token")

        val USERS: Preferences.Key<String> = stringPreferencesKey(name = "users")
        val TOKENS: Preferences.Key<String> = stringPreferencesKey(name = "tokens")
    }

    suspend fun update(authState: NullableAuthState) {
        val existingAuthState: AuthState = flow.first()

        dataStore.edit { preferences: MutablePreferences ->
            preferences[PreferenceKeys.USERNAME] =
                authState.username ?: existingAuthState.username
            preferences[PreferenceKeys.TOKEN] =
                authState.token ?: existingAuthState.token

            preferences[PreferenceKeys.USERS] =
                if (authState.users != null) authState.users.joinToString(separator = ",") else existingAuthState.users.joinToString(
                    separator = ","
                )
            preferences[PreferenceKeys.TOKENS] =
                if (authState.tokens != null) authState.tokens.joinToString(separator = ",") { (username: String, token: String) -> "$username:${token}" } else existingAuthState.tokens.joinToString(
                    separator = ","
                ) { (username: String, token: String) ->
                    "$username:${token}"
                }
        }

    }

    val flow: Flow<AuthState> = dataStore.data.catch { exception: Throwable ->
        if (exception is IOException) {
            defaultAuthState()
        } else {
            throw exception
        }
    }.map { preferences: Preferences ->
        val tokens: MutableList<List<String>> = defaultAuthState().tokens.toMutableList()
        preferences[PreferenceKeys.TOKENS]?.split(",")?.forEach { tokenStr: String ->
            val (username: String, token: String) = tokenStr.split(":")

            tokens.add(listOf(username, token))
        }

        AuthState(
            username = preferences[PreferenceKeys.USERNAME] ?: defaultAuthState().username,
            token = preferences[PreferenceKeys.TOKEN] ?: defaultAuthState().token,

            users = preferences[PreferenceKeys.USERS]?.split(",")
                ?: defaultAuthState().users,
            tokens = tokens
        )
    }
}