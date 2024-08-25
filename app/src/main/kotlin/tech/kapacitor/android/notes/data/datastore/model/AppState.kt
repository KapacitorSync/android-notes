package tech.kapacitor.android.notes.data.datastore.model

data class AppState(
    val isSetupDone: Boolean,
)

data class NullableAppState(
    val isSetupDone: Boolean? = null,
)
