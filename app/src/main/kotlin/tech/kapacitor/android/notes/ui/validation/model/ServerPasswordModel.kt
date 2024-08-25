package tech.kapacitor.android.notes.ui.validation.model

import tech.kapacitor.android.notes.ui.validation.annotations.NotEmpty

data class ServerPasswordModel(
    @NotEmpty val password: String = ""
)
