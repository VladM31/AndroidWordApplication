package can.lucky.of.core.domain.models.enums

import can.lucky.of.core.utils.titleCase

enum class Language(
    private val _shortName : String?
) {
    POLISH("pl"),
    ENGLISH("en"),
    GERMAN("de"),
//    FRENCH("fr"),
    UKRAINIAN("uk"),
//    RUSSIAN("ru"),
    UNDEFINED(null);

    val shortName: String
        get() = _shortName.toString()

    val fixedShortName: String
        get() =  if (UKRAINIAN == this) "ua" else shortName

    val titleCase = this.titleCase()

    companion object {
        private val mapShortName = entries.associateBy(Language::shortName)
        private val mapTitleCase = entries.associateBy(Language::titleCase)

        fun fromShortName(shortName: String): Language {
            return mapShortName[shortName] ?: UNDEFINED
        }

        fun fromTitleCase(titleCase: String): Language {
            return mapTitleCase[titleCase] ?: UNDEFINED
        }

        fun Language.toBcp47Tag(region: String? = null): String? = when (this) {
            POLISH -> buildTag("pl", region ?: "PL")
            ENGLISH -> buildTag("en", region)
            GERMAN -> buildTag("de", region ?: "DE")
            UKRAINIAN -> buildTag("ua", region ?: "UA")
            UNDEFINED -> null
        }

        private fun buildTag(lang: String, region: String?): String =
            if (region.isNullOrBlank()) lang else "$lang-$region"
    }

    val isDefined: Boolean
        get() = this != UNDEFINED
}