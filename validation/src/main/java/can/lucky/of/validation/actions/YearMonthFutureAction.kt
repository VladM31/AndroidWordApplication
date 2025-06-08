package can.lucky.of.validation.actions

import can.lucky.of.validation.models.ValidResult
import java.time.YearMonth
import java.time.format.DateTimeFormatter

internal object YearMonthFutureAction : ValidAction<String> {
    private val ERROR = ValidResult.invalid("Date is not valid");
    private val formatter = DateTimeFormatter.ofPattern("MM.yy")

    override fun validate(value: String): ValidResult {
        if (value.length != 5) {
            return ERROR
        }
        return value.runCatching {
            val yearMonth = YearMonth.parse(value, formatter)
            return if (YearMonth.now().isBefore(yearMonth)) {
                ValidResult.valid()
            } else {
                ERROR
            }
        }.getOrDefault(ERROR)
    }
}