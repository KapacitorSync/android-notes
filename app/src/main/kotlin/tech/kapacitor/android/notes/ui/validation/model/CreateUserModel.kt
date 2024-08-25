package tech.kapacitor.android.notes.ui.validation.model

import tech.kapacitor.android.notes.ui.validation.annotations.MaxLength
import tech.kapacitor.android.notes.ui.validation.annotations.MinLength
import tech.kapacitor.android.notes.ui.validation.annotations.NotEmpty

data class CreateUserModel(
    @NotEmpty @MinLength(length = 3) @MaxLength(length = 15) val username: String = "",

    @NotEmpty @MinLength(length = 8) @MaxLength(length = 4096) val password: String = "",
)
