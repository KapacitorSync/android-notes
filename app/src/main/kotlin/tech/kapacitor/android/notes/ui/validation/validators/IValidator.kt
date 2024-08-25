package tech.kapacitor.android.notes.ui.validation.validators

import tech.kapacitor.android.notes.ui.validation.ValidationError

interface IValidator {
    fun validate(property: String, value: Any?): ValidationError?
}