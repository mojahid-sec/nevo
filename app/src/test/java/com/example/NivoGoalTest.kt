package com.example

import com.example.data.localization.AppLanguage
import com.example.data.localization.MotivationalQuotes
import com.example.data.localization.NivoStrings
import com.example.data.localization.toLocalizedDigits
import com.example.data.model.Goal
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class NivoGoalTest {

    @Test
    fun testGoalCalculationsAndBoundaries() {
        // Boundary: 2 days
        val minGoal = Goal(
            title = "Two Day Sprint",
            durationDays = 2,
            completedDaysCount = 1
        )
        assertEquals(2, minGoal.durationDays)
        assertEquals(1, minGoal.remainingDays)
        assertEquals(0.5f, minGoal.progressFraction, 0.001f)
        assertEquals(50, minGoal.progressPercentage)

        // Intermediate: 15 days
        val midGoal15 = Goal(
            title = "Two Week Challenge",
            durationDays = 15,
            completedDaysCount = 3
        )
        assertEquals(15, midGoal15.durationDays)
        assertEquals(12, midGoal15.remainingDays)
        assertEquals(0.2f, midGoal15.progressFraction, 0.001f)
        assertEquals(20, midGoal15.progressPercentage)

        // Intermediate: 180 days
        val midGoal180 = Goal(
            title = "Half Year Transformation",
            durationDays = 180,
            completedDaysCount = 90
        )
        assertEquals(180, midGoal180.durationDays)
        assertEquals(90, midGoal180.remainingDays)
        assertEquals(0.5f, midGoal180.progressFraction, 0.001f)
        assertEquals(50, midGoal180.progressPercentage)

        // Boundary: 365 days
        val maxGoal = Goal(
            title = "Full Year Discipline",
            durationDays = 365,
            completedDaysCount = 365
        )
        assertEquals(365, maxGoal.durationDays)
        assertEquals(0, maxGoal.remainingDays)
        assertEquals(1.0f, maxGoal.progressFraction, 0.001f)
        assertEquals(100, maxGoal.progressPercentage)
        assertTrue(maxGoal.remainingDays == 0)
    }

    @Test
    fun testExact24HourRule() {
        val checkInTime = 1_000_000_000_000L // 1 Trillion ms base epoch
        val exact24Hours = 24L * 60L * 60L * 1000L // 86,400,000 ms

        val goalWithCheckIn = Goal(
            title = "24 Hour Test Goal",
            durationDays = 30,
            completedDaysCount = 1,
            lastCheckInTimestampMillis = checkInTime
        )

        // At 10 hours after check-in: Ineligible
        val tenHoursLater = checkInTime + (10L * 60L * 60L * 1000L)
        assertFalse(goalWithCheckIn.isCheckInEligible(tenHoursLater))
        assertEquals(14L * 60L * 60L * 1000L, goalWithCheckIn.getRemainingCooldownMillis(tenHoursLater))

        // At 23 hours 59 minutes 59 seconds: Ineligible
        val almost24Hours = checkInTime + exact24Hours - 1000L
        assertFalse(goalWithCheckIn.isCheckInEligible(almost24Hours))
        assertEquals(1000L, goalWithCheckIn.getRemainingCooldownMillis(almost24Hours))

        // At exactly 24 hours: Eligible!
        val exactly24HoursLater = checkInTime + exact24Hours
        assertTrue(goalWithCheckIn.isCheckInEligible(exactly24HoursLater))
        assertEquals(0L, goalWithCheckIn.getRemainingCooldownMillis(exactly24HoursLater))

        // At 25 hours after check-in: Eligible!
        val twentyFiveHoursLater = checkInTime + (25L * 60L * 60L * 1000L)
        assertTrue(goalWithCheckIn.isCheckInEligible(twentyFiveHoursLater))
        assertEquals(0L, goalWithCheckIn.getRemainingCooldownMillis(twentyFiveHoursLater))
    }

    @Test
    fun testFirstCheckInAlwaysEligible() {
        val brandNewGoal = Goal(
            title = "Brand New Goal",
            durationDays = 30,
            completedDaysCount = 0,
            lastCheckInTimestampMillis = null // No check-in has happened yet
        )

        assertTrue(brandNewGoal.isCheckInEligible())
        assertEquals(0L, brandNewGoal.getRemainingCooldownMillis())
    }

    @Test
    fun testBanglaDigitLocalization() {
        assertEquals("০", 0.toLocalizedDigits(AppLanguage.BANGLA))
        assertEquals("১", 1.toLocalizedDigits(AppLanguage.BANGLA))
        assertEquals("২", 2.toLocalizedDigits(AppLanguage.BANGLA))
        assertEquals("১৫", 15.toLocalizedDigits(AppLanguage.BANGLA))
        assertEquals("১৮০", 180.toLocalizedDigits(AppLanguage.BANGLA))
        assertEquals("৩৬৫", 365.toLocalizedDigits(AppLanguage.BANGLA))

        // English remains Arabic digits
        assertEquals("365", 365.toLocalizedDigits(AppLanguage.ENGLISH))
    }

    @Test
    fun testMotivationalQuotesCount() {
        // Must have at least 50 unique quotes
        assertTrue("Expected at least 50 quotes, found: ${MotivationalQuotes.quotes.size}",
            MotivationalQuotes.quotes.size >= 50)
    }

    @Test
    fun testExpectedCompletionDateCalculation() {
        val start = 1_700_000_000_000L
        val goal30 = Goal(
            title = "30 Days",
            durationDays = 30,
            startDateMillis = start
        )

        val expected = start + (30L * 24L * 60L * 60L * 1000L)
        assertEquals(expected, goal30.expectedCompletionDateMillis)
    }

    @Test
    fun testAllTenThemesConfigured() {
        // Must contain exactly 10 themes
        val allThemes = com.example.ui.theme.NivoTheme.values()
        assertEquals(10, allThemes.size)

        allThemes.forEach { theme ->
            val colors = com.example.ui.theme.getThemeColors(theme)
            // Localized names must be non-empty
            assertTrue(theme.getLocalizedName(AppLanguage.ENGLISH).isNotBlank())
            assertTrue(theme.getLocalizedName(AppLanguage.BANGLA).isNotBlank())
            assertTrue(theme.getLocalizedDesc(AppLanguage.ENGLISH).isNotBlank())
            assertTrue(theme.getLocalizedDesc(AppLanguage.BANGLA).isNotBlank())

            // fromId resolution must roundtrip correctly
            assertEquals(theme, com.example.ui.theme.NivoTheme.fromId(theme.id))
        }

        // Pure white must be light theme
        assertTrue(com.example.ui.theme.getThemeColors(com.example.ui.theme.NivoTheme.PURE_WHITE).isLight)
        // Others must be dark themes
        assertFalse(com.example.ui.theme.getThemeColors(com.example.ui.theme.NivoTheme.CRIMSON).isLight)
    }
}
