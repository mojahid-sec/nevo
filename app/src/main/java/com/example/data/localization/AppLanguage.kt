package com.example.data.localization

enum class AppLanguage(val code: String, val displayName: String, val nativeName: String) {
    ENGLISH("en", "English", "English"),
    BANGLA("bn", "Bangla", "বাংলা");

    companion object {
        fun fromCode(code: String): AppLanguage {
            return values().find { it.code.equals(code, ignoreCase = true) } ?: ENGLISH
        }
    }
}

/**
 * Converts English digits to Bangla digits if current language is Bangla
 */
fun Int.toLocalizedDigits(language: AppLanguage): String {
    return this.toString().toLocalizedDigits(language)
}

fun Long.toLocalizedDigits(language: AppLanguage): String {
    return this.toString().toLocalizedDigits(language)
}

fun String.toLocalizedDigits(language: AppLanguage): String {
    if (language != AppLanguage.BANGLA) return this
    val banglaDigits = charArrayOf('০', '১', '২', '৩', '৪', '৫', '৬', '৭', '৮', '৯')
    val sb = StringBuilder()
    for (char in this) {
        if (char in '0'..'9') {
            sb.append(banglaDigits[char - '0'])
        } else {
            sb.append(char)
        }
    }
    return sb.toString()
}
