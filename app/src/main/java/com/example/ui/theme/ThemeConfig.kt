package com.example.ui.theme

import androidx.compose.ui.graphics.Color
import com.example.data.localization.AppLanguage

/**
 * 10 Premium Themes for NIVO.
 * Pure Black, Dark Neomorphic, and High-Contrast palettes.
 * Green is strictly excluded from all palettes per brand guidelines.
 */
enum class NivoTheme(val id: String) {
    CRIMSON("crimson"),
    OCEAN("ocean"),
    VIOLET("violet"),
    AMBER("amber"),
    MONOCHROME("monochrome"),
    PURE_WHITE("pure_white"),
    ROSE("rose"),
    ARCTIC("arctic"),
    LUXURY_GOLD("luxury_gold"),
    SLATE("slate");

    fun getLocalizedName(language: AppLanguage): String {
        return when (this) {
            CRIMSON -> if (language == AppLanguage.BANGLA) "ক্রিমসন (মূল)" else "Crimson"
            OCEAN -> if (language == AppLanguage.BANGLA) "মহাসাগর (নীল)" else "Ocean"
            VIOLET -> if (language == AppLanguage.BANGLA) "ভায়োলেট (বেগুনি)" else "Violet"
            AMBER -> if (language == AppLanguage.BANGLA) "অ্যাম্বার (সোনালি)" else "Amber"
            MONOCHROME -> if (language == AppLanguage.BANGLA) "মনোক্রোম (কালো-সাদা)" else "Monochrome"
            PURE_WHITE -> if (language == AppLanguage.BANGLA) "শুভ্র সাদা (লাইট থিম)" else "Pure White"
            ROSE -> if (language == AppLanguage.BANGLA) "রোজ (গোলাপ লাল)" else "Rose"
            ARCTIC -> if (language == AppLanguage.BANGLA) "আর্কটিক (হিমবাহ সায়ান)" else "Arctic"
            LUXURY_GOLD -> if (language == AppLanguage.BANGLA) "লাক্সারি গোল্ড (স্বর্ণালি)" else "Luxury Gold"
            SLATE -> if (language == AppLanguage.BANGLA) "স্লেট (গ্রাফাইট টাইটেনিয়াম)" else "Slate"
        }
    }

    fun getLocalizedDesc(language: AppLanguage): String {
        return when (this) {
            CRIMSON -> if (language == AppLanguage.BANGLA) "মূল ওএলইডি মিনিমালিস্ট — বিশুদ্ধ কালো ও রক্তিম লাল" else "Original OLED Minimalist — pure black & vivid crimson red"
            OCEAN -> if (language == AppLanguage.BANGLA) "গভীর সমুদ্র ও নীল — শান্ত ও গভীর আত্মশৃঙ্খলা" else "Deep Navy & Azure — calming oceanic discipline"
            VIOLET -> if (language == AppLanguage.BANGLA) "মধ্যরাতের বেগুনি ও উজ্জ্বল আভা — গভীর মনোযোগ" else "Midnight Purple & Neon Violet — mystical focus"
            AMBER -> if (language == AppLanguage.BANGLA) "উষ্ণ চারকোল ও অ্যাম্বার সোনালি — অফুরন্ত আত্মবিশ্বাস" else "Warm Obsidian & Amber Gold — comforting energy"
            MONOCHROME -> if (language == AppLanguage.BANGLA) "বিশুদ্ধ কালো ও রূপালি — নিখুঁত মিনিমালিজম" else "Pitch Black & Silver — stealth minimalism"
            PURE_WHITE -> if (language == AppLanguage.BANGLA) "উজ্জ্বল সাদা ও গাঢ় টেক্সট — দিনের আলোর জন্য উপযোগী" else "Crisp Light & Scarlet — true high-contrast daytime clarity"
            ROSE -> if (language == AppLanguage.BANGLA) "গাঢ় কফি ও গোলাপ লাল — দৃঢ় অধ্যবসায়" else "Espresso & Vivid Rose — passionate perseverance"
            ARCTIC -> if (language == AppLanguage.BANGLA) "হিমশীতল অন্ধকার ও সায়ান — শীতল ও তীক্ষ্ণ একাগ্রতা" else "Glacial Abyss & Frost Cyan — cool razor-sharp clarity"
            LUXURY_GOLD -> if (language == AppLanguage.BANGLA) "অভিজাত কালো ও স্বর্ণালি — অনন্য রাজকীয় আভিজাত্য" else "Imperial Onyx & 24K Gold — royal timeless prestige"
            SLATE -> if (language == AppLanguage.BANGLA) "গ্রাফাইট ধূসর ও টাইটানিয়াম — আধুনিক মার্জিত শক্তি" else "Industrial Graphite & Titanium — modern refined strength"
        }
    }

    companion object {
        fun fromId(id: String): NivoTheme {
            return values().find { it.id.equals(id, ignoreCase = true) || it.name.equals(id, ignoreCase = true) }
                ?: CRIMSON
        }
    }
}

/**
 * Unified theme colors model for NIVO.
 */
data class NivoColors(
    val theme: NivoTheme,
    val background: Color,
    val card: Color,
    val cardElevated: Color,
    val primaryAccent: Color,
    val secondaryAccent: Color,
    val accent: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val outline: Color,
    val subtle: Color,
    val barBackground: Color,
    val isLight: Boolean = false
)

// 1. CRIMSON (Classic NIVO)
val CrimsonColors = NivoColors(
    theme = NivoTheme.CRIMSON,
    background = Color(0xFF121212),
    card = Color(0xFF1A1A1A),
    cardElevated = Color(0xFF242424),
    primaryAccent = Color(0xFFFF1744),
    secondaryAccent = Color(0xFFB71C1C),
    accent = Color(0xFFFF5252),
    textPrimary = Color(0xFFFFFFFF),
    textSecondary = Color(0xFFA0A0A0),
    outline = Color(0xFF333333),
    subtle = Color(0xFF4A4A4A),
    barBackground = Color(0xFF000000),
    isLight = false
)

// 2. OCEAN
val OceanColors = NivoColors(
    theme = NivoTheme.OCEAN,
    background = Color(0xFF0A1128),
    card = Color(0xFF101F42),
    cardElevated = Color(0xFF1C2E5E),
    primaryAccent = Color(0xFF0077FE),
    secondaryAccent = Color(0xFF004B99),
    accent = Color(0xFF00B4D8),
    textPrimary = Color(0xFFFFFFFF),
    textSecondary = Color(0xFF8EA8D6),
    outline = Color(0xFF23396D),
    subtle = Color(0xFF37539B),
    barBackground = Color(0xFF060A18),
    isLight = false
)

// 3. VIOLET
val VioletColors = NivoColors(
    theme = NivoTheme.VIOLET,
    background = Color(0xFF0D0614),
    card = Color(0xFF1A0D2A),
    cardElevated = Color(0xFF291542),
    primaryAccent = Color(0xFF9D4EDD),
    secondaryAccent = Color(0xFF5A189A),
    accent = Color(0xFFC77DFF),
    textPrimary = Color(0xFFFFFFFF),
    textSecondary = Color(0xFFB49EC4),
    outline = Color(0xFF3A1E5C),
    subtle = Color(0xFF4E2A7A),
    barBackground = Color(0xFF07030A),
    isLight = false
)

// 4. AMBER
val AmberColors = NivoColors(
    theme = NivoTheme.AMBER,
    background = Color(0xFF12100E),
    card = Color(0xFF1E1A16),
    cardElevated = Color(0xFF2E2721),
    primaryAccent = Color(0xFFFF9E00),
    secondaryAccent = Color(0xFFD47A00),
    accent = Color(0xFFFFB703),
    textPrimary = Color(0xFFFFF8F0),
    textSecondary = Color(0xFFBCAEA2),
    outline = Color(0xFF42372E),
    subtle = Color(0xFF594B3E),
    barBackground = Color(0xFF0A0908),
    isLight = false
)

// 5. MONOCHROME
val MonochromeColors = NivoColors(
    theme = NivoTheme.MONOCHROME,
    background = Color(0xFF000000),
    card = Color(0xFF161616),
    cardElevated = Color(0xFF242424),
    primaryAccent = Color(0xFFE0E0E0),
    secondaryAccent = Color(0xFF9E9E9E),
    accent = Color(0xFFFFFFFF),
    textPrimary = Color(0xFFFFFFFF),
    textSecondary = Color(0xFF888888),
    outline = Color(0xFF303030),
    subtle = Color(0xFF444444),
    barBackground = Color(0xFF000000),
    isLight = false
)

// 6. PURE WHITE (True Light Theme)
val PureWhiteColors = NivoColors(
    theme = NivoTheme.PURE_WHITE,
    background = Color(0xFFFFFFFF),
    card = Color(0xFFF5F5F7),
    cardElevated = Color(0xFFE8E8ED),
    primaryAccent = Color(0xFFD32F2F),
    secondaryAccent = Color(0xFF8A0000),
    accent = Color(0xFFE53935),
    textPrimary = Color(0xFF111111),
    textSecondary = Color(0xFF666666),
    outline = Color(0xFFD8D8E0),
    subtle = Color(0xFFB0B0B8),
    barBackground = Color(0xFFF5F5F7),
    isLight = true
)

// 7. ROSE
val RoseColors = NivoColors(
    theme = NivoTheme.ROSE,
    background = Color(0xFF14080E),
    card = Color(0xFF220F1A),
    cardElevated = Color(0xFF341728),
    primaryAccent = Color(0xFFFF2A6D),
    secondaryAccent = Color(0xFFAD1457),
    accent = Color(0xFFFF6584),
    textPrimary = Color(0xFFFFF0F5),
    textSecondary = Color(0xFFC49FAE),
    outline = Color(0xFF4D233C),
    subtle = Color(0xFF662F4F),
    barBackground = Color(0xFF0C0408),
    isLight = false
)

// 8. ARCTIC
val ArcticColors = NivoColors(
    theme = NivoTheme.ARCTIC,
    background = Color(0xFF0A1017),
    card = Color(0xFF131E2A),
    cardElevated = Color(0xFF1C2D3E),
    primaryAccent = Color(0xFF00E5FF),
    secondaryAccent = Color(0xFF0091EA),
    accent = Color(0xFF80D8FF),
    textPrimary = Color(0xFFFFFFFF),
    textSecondary = Color(0xFF8EAAC0),
    outline = Color(0xFF233B52),
    subtle = Color(0xFF345575),
    barBackground = Color(0xFF060A0F),
    isLight = false
)

// 9. LUXURY GOLD
val LuxuryGoldColors = NivoColors(
    theme = NivoTheme.LUXURY_GOLD,
    background = Color(0xFF0F0E0A),
    card = Color(0xFF1C1912),
    cardElevated = Color(0xFF2C271C),
    primaryAccent = Color(0xFFFFD700),
    secondaryAccent = Color(0xFFB8860B),
    accent = Color(0xFFFFE082),
    textPrimary = Color(0xFFFFFDF7),
    textSecondary = Color(0xFFBAAF98),
    outline = Color(0xFF453D2A),
    subtle = Color(0xFF5C5238),
    barBackground = Color(0xFF0A0906),
    isLight = false
)

// 10. SLATE
val SlateColors = NivoColors(
    theme = NivoTheme.SLATE,
    background = Color(0xFF101216),
    card = Color(0xFF1A1E24),
    cardElevated = Color(0xFF262B34),
    primaryAccent = Color(0xFF90A4AE),
    secondaryAccent = Color(0xFF546E7A),
    accent = Color(0xFFCFD8DC),
    textPrimary = Color(0xFFECEFF1),
    textSecondary = Color(0xFF90A4AE),
    outline = Color(0xFF37474F),
    subtle = Color(0xFF455A64),
    barBackground = Color(0xFF0A0B0E),
    isLight = false
)

/**
 * Returns the corresponding color scheme for the given [NivoTheme].
 */
fun getThemeColors(theme: NivoTheme): NivoColors {
    return when (theme) {
        NivoTheme.CRIMSON -> CrimsonColors
        NivoTheme.OCEAN -> OceanColors
        NivoTheme.VIOLET -> VioletColors
        NivoTheme.AMBER -> AmberColors
        NivoTheme.MONOCHROME -> MonochromeColors
        NivoTheme.PURE_WHITE -> PureWhiteColors
        NivoTheme.ROSE -> RoseColors
        NivoTheme.ARCTIC -> ArcticColors
        NivoTheme.LUXURY_GOLD -> LuxuryGoldColors
        NivoTheme.SLATE -> SlateColors
    }
}
