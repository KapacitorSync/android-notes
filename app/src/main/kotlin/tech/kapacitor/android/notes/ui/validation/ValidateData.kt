package tech.kapacitor.android.notes.ui.validation

import tech.kapacitor.android.notes.ui.validation.annotations.IsNumber
import tech.kapacitor.android.notes.ui.validation.annotations.NotEmpty
import tech.kapacitor.android.notes.ui.validation.validators.IsNumberValidator
import tech.kapacitor.android.notes.ui.validation.validators.NotEmptyValidator
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

class ValidateData<T : Any>(
    val model: KClass<T>
) {
    private val validatorMap =
        mapOf(
            NotEmpty::class to NotEmptyValidator::validate,
            IsNumber::class to IsNumberValidator::validate
        )

    fun validate(state: T): ValidationError? {
        val errors: MutableList<ValidationError> = mutableListOf()

        model.memberProperties.forEach { property: KProperty1<T, *> ->
            property.annotations.forEach { annotation: Annotation ->
                val error: ValidationError? = try {
                    validatorMap[annotation.annotationClass]?.invoke(
                        property.name,
                        property.get(state)
                    )
                } catch (_: Exception) {
                    null
                }

                if (error != null) {
                    errors.add(error)
                }
            }
        }

        return errors.firstOrNull()
    }
}