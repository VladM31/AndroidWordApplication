package com.generagames.happy.town.farm.wordandroid.valid

import java.net.IDN
import java.util.regex.Pattern


object EmailValidator : StringValidator {

    private const val MAX_LOCAL_PART_LENGTH = 64

    private val EMAIL_DOMAIN_PATTERN = Pattern.compile(
        "(?:[a-z\u0080-\uffff0-9!#$%&'*+/=?^_`{|}~]-*)*[a-z\u0080-\uffff0-9!#$%&'*+/=?^_`{|}~]++(?:\\.(?:[a-z\u0080-\uffff0-9!#$%&'*+/=?^_`{|}~]-*)*[a-z\u0080-\uffff0-9!#$%&'*+/=?^_`{|}~]++)*|\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\]|\\[IPv6:(?:(?:[0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|(?:[0-9a-fA-F]{1,4}:){1,7}:|(?:[0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|(?:[0-9a-fA-F]{1,4}:){1,5}(?::[0-9a-fA-F]{1,4}){1,2}|(?:[0-9a-fA-F]{1,4}:){1,4}(?::[0-9a-fA-F]{1,4}){1,3}|(?:[0-9a-fA-F]{1,4}:){1,3}(?::[0-9a-fA-F]{1,4}){1,4}|(?:[0-9a-fA-F]{1,4}:){1,2}(?::[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:(?:(?::[0-9a-fA-F]{1,4}){1,6})|:(?:(?::[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(?::[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|::(?:ffff(:0{1,4}){0,1}:){0,1}(?:(?:25[0-5]|(?:2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(?:25[0-5]|(?:2[0-4]|1{0,1}[0-9]){0,1}[0-9])|(?:[0-9a-fA-F]{1,4}:){1,4}:(?:(?:25[0-5]|(?:2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(?:25[0-5]|(?:2[0-4]|1{0,1}[0-9]){0,1}[0-9]))\\]",
        2
    )

    private const val ERROR_MESSAGE =  "Email is not valid"
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

    override fun valid(string: String): String? {
        return if (string.isNotEmpty() and string.isNotBlank()) {

            val splitPosition = string.lastIndexOf(MAX_LOCAL_PART_LENGTH.toChar())
            if (splitPosition < 0) {
                ERROR_MESSAGE
            } else {
                val localPart = string.substring(0, splitPosition)
                val domainPart = string.substring(splitPosition + 1)
                if (!this.isValidEmailLocalPart(localPart)) ERROR_MESSAGE else isValidDomainAddress(
                    domainPart
                )
            }
        } else {
            ERROR_MESSAGE
        }
    }


    private fun isValidDomainAddress(domain: String): String? {
        return if (domain.endsWith(".")) {
            ERROR_MESSAGE
        } else {
            val asciiString: String = try {
                IDN.toASCII(domain)
            } catch (var4: IllegalArgumentException) {
                return ERROR_MESSAGE
            }
            if (asciiString.length > 255) {
                ERROR_MESSAGE
            } else {
                val matcher = EMAIL_DOMAIN_PATTERN.matcher(domain)
                if (matcher.matches()) {
                    null
                }else{
                    ERROR_MESSAGE
                }
            }
        }
    }
}