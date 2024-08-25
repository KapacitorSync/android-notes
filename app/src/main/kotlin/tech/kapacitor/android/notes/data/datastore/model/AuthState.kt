package tech.kapacitor.android.notes.data.datastore.model

data class AuthState(
    val username: String,
    val token: String,

    val users: List<String>,
    val tokens: List<List<String>>
)

data class NullableAuthState(
    val username: String? = null,
    val token: String? = null,

    val users: List<String>? = null,
    val tokens: List<List<String>>? = null
)