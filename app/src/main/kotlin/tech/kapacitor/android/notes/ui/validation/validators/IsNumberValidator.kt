package tech.kapacitor.android.notes.ui.validation.validators

import tech.kapacitor.android.notes.ui.validation.ValidationError

object IsNumberValidator : IValidator {
    override fun validate(property: String, value: Any?): ValidationError? {
        if (value.toString().toIntOrNull() == null) {
            return ValidationError(
                property = property,
                message = "not_number"
            )
        }

        return null
    }
}