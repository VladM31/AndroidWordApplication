package can.lucky.of.validation.actions

import can.lucky.of.validation.models.ValidResult
import java.net.IDN
import java.util.regex.Pattern

internal class EmailAction(
    private val isRequired: Boolean = false
) : ValidAction<String> {


    private val EMAIL_DOMAIN_PATTERN = Pattern.compile(
        "(?:[a-z\u0080-\uffff0-9!#$%&'*+/=?^_`{|}~]-*)*[a-z\u0080-\uffff0-9!#$%&'*+/=?^_`{|}~]++(?:\\.(?:[a-z\u0080-\uffff0-9!#$%&'*+/=?^_`{|}~]-*)*[a-z\u0080-\uffff0-9!#$%&'*+/=?^_`{|}~]++)*|\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\]|\\[IPv6:(?:(?:[0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|(?:[0-9a-fA-F]{1,4}:){1,7}:|(?:[0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|(?:[0-9a-fA-F]{1,4}:){1,5}(?::[0-9a-fA-F]{1,4}){1,2}|(?:[0-9a-fA-F]{1,4}:){1,4}(?::[0-9a-fA-F]{1,4}){1,3}|(?:[0-9a-fA-F]{1,4}:){1,3}(?::[0-9a-fA-F]{1,4}){1,4}|(?:[0-9a-fA-F]{1,4}:){1,2}(?::[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:(?:(?::[0-9a-fA-F]{1,4}){1,6})|:(?:(?::[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(?::[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|::(?:ffff(:0{1,4}){0,1}:){0,1}(?:(?:25[0-5]|(?:2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(?:25[0-5]|(?:2[0-4]|1{0,1}[0-9]){0,1}[0-9])|(?:[0-9a-fA-F]{1,4}:){1,4}:(?:(?:25[0-5]|(?:2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(?:25[0-5]|(?:2[0-4]|1{0,1}[0-9]){0,1}[0-9]))\\]",
        2
    )

    private val ERROR_RESULT = ValidResult(false, "Email is not valid")

    private val LOCAL_PART_PATTERN: Pattern = Pattern.compile(
        "(?:[a-z0-9!#$%&'*+/=?^_`{|}~\u0080-\uffff-]+|\"(?:[a-z0-9!#$%&'*.(),<>\\[\\]:;  @+/=?^_`{|}~\u0080-\uffff-]|\\\\\\\\|\\\\\\\")+\")(?:\\.(?:[a-z0-9!#$%&'*+/=?^_`{|}~\u0080-\uffff-]+|\"(?:[a-z0-9!#$%&'*.(),<>\\[\\]:;  @+/=?^_`{|}~\u0080-\uffff-]|\\\\\\\\|\\\\\\\")+\"))*",
        2
    )

    private fun isValidEmailLocalPart(localPart: String): Boolean {
        return if (localPart.length > MAX_LOCAL_PART_LENGTH) {
            false
        } else {
            val matcher = LOCAL_PART_PATTERN.matcher(localPart)
            matcher.matches()
        }
    }

    override fun validate(value: String): ValidResult {
        if (value.isEmpty()) {
            return if (isRequired) {
                ERROR_RESULT
            }else{
                ValidResult.valid()
            }
        }

        if (value.isBlank()) return ERROR_RESULT

        val splitPosition = value.lastIndexOf(MAX_LOCAL_PART_LENGTH.toChar())
        return if (splitPosition < 0) {
            ERROR_RESULT
        } else {
            val localPart = value.substring(0, splitPosition)
            val domainPart = value.substring(splitPosition + 1)
            if (!this.isValidEmailLocalPart(localPart)) ERROR_RESULT else isValidDomainAddress(
                domainPart
            )
        }
    }

    private fun isValidDomainAddress(domain: String): ValidResult {
        return if (domain.endsWith(".")) {
            ERROR_RESULT
        } else {
            val asciiString: String = try {
                IDN.toASCII(domain)
            } catch (var4: IllegalArgumentException) {
                return ERROR_RESULT
            }
            if (asciiString.length > 255) {
                ERROR_RESULT
            } else {
                val matcher = EMAIL_DOMAIN_PATTERN.matcher(domain)
                if (matcher.matches()) {
                    ValidResult.valid()
                }else{
                    ERROR_RESULT
                }
            }
        }
    }

    companion object{
        private const val MAX_LOCAL_PART_LENGTH = 64
    }
}