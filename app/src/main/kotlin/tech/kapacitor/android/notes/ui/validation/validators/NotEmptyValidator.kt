package tech.kapacitor.android.notes.ui.validation.validators

import tech.kapacitor.android.notes.ui.validation.ValidationError

object NotEmptyValidator : IValidator {
    override fun validate(property: String, value: Any?): ValidationError? {
        if (value.toString().isEmpty()) {
            return ValidationError(
                property = property,
                message = "empty"
            )
        }

        return null
    }
}
