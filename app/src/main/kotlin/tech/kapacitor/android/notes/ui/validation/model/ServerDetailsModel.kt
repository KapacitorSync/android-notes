package tech.kapacitor.android.notes.ui.validation.model

import tech.kapacitor.android.notes.ui.validation.annotations.IsNumber
import tech.kapacitor.android.notes.ui.validation.annotations.NotEmpty

data class ServerDetailsModel(
    @NotEmpty val url: String = "",

    @NotEmpty @IsNumber val port: String = "3000"
)