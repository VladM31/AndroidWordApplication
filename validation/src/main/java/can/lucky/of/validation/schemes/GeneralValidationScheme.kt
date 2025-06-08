package can.lucky.of.validation.schemes

import can.lucky.of.validation.actions.DifferentLanguageAction
import can.lucky.of.validation.actions.EqualsAction
import can.lucky.of.validation.models.DifferentLanguageable


fun <T> ValidationScheme<T>.isEquals(vararg values: T) : ValidationScheme<T> {
    if (values.isEmpty()) {
        return this
    }
    return add(EqualsAction(values.toList()))
}

fun <T> ValidationScheme<T>.isEquals(values: List<T>) : ValidationScheme<T> {
    if (values.isEmpty()) {
        return this
    }
    return add(EqualsAction(values))
}

fun <T> ValidationScheme<T>.isNotEquals(vararg values: T) : ValidationScheme<T> {
    return add(EqualsAction(values.toList(), false))
}

fun <T> ValidationScheme<T>.isNotEquals(values: List<T>) : ValidationScheme<T> {
    return add(EqualsAction(values, false))
}

fun <T : DifferentLanguageable> ValidationScheme<T>.isDifferentLanguage() : ValidationScheme<T> {
    return add(DifferentLanguageAction())
}