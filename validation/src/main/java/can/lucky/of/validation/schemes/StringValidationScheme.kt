package can.lucky.of.validation.schemes

import can.lucky.of.validation.actions.EmailAction
import can.lucky.of.validation.actions.IsMatchesAction
import can.lucky.of.validation.actions.NotBlankAction
import can.lucky.of.validation.actions.NotEmptyAction
import can.lucky.of.validation.actions.StringLengthAction
import can.lucky.of.validation.actions.YearMonthFutureAction

fun ValidationScheme<String>.length(min: Int? = null, max: Int? = null) : ValidationScheme<String> {
    if (min == null && max == null) {
        return this
    }

    return add(StringLengthAction(min, max))
}

fun ValidationScheme<String>.notBlank(canBeEmpty: Boolean = false) : ValidationScheme<String> {
    return add(NotBlankAction(canBeEmpty))
}

fun ValidationScheme<String>.notEmpty() : ValidationScheme<String> {
    return add(NotEmptyAction)
}

fun ValidationScheme<String>.email(isRequired: Boolean = true) : ValidationScheme<String> {
    return add(EmailAction(isRequired))
}

fun ValidationScheme<String>.isMatches(regex: Regex) : ValidationScheme<String> {
    return add(IsMatchesAction(regex))
}

fun ValidationScheme<String>.isPhoneNumber() : ValidationScheme<String> {
    return add(IsMatchesAction(
        Regex("\\d{10,15}", RegexOption.IGNORE_CASE),
        message = "Value should be a valid phone number"
    ))
}

fun ValidationScheme<String>.isYearMonthFuture() : ValidationScheme<String> {
    return add(YearMonthFutureAction)
}