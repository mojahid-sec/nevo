package com.example.data.model

import java.util.UUID

enum class TrackingMode {
    MANUAL,
    AUTOMATIC;

    companion object {
        fun fromString(value: String?): TrackingMode {
            return when (value?.uppercase()) {
                "AUTOMATIC", "AUTO" -> AUTOMATIC
                else -> MANUAL
            }
        }
    }
}

data class Goal(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val durationDays: Int = 30, // 2 to 365
    val startDateMillis: Long = System.currentTimeMillis(),
    val reason: String = "",
    val completedDaysCount: Int = 0,
    val lastCheckInTimestampMillis: Long? = null,
    val isSelected: Boolean = false,
    val isCompleted: Boolean = false,
    val completedDateMillis: Long? = null,
    val trackingMode: TrackingMode = TrackingMode.MANUAL
) {
    // ------------------------------------------------------------------------
    // Circle 1: Manual Progress (Increases ONLY via valid manual Check-In)
    // ------------------------------------------------------------------------
    val manualCompletedDays: Int
        get() = completedDaysCount

    val manualRemainingDays: Int
        get() = (durationDays - completedDaysCount).coerceAtLeast(0)

    val manualProgressFraction: Float
        get() = if (durationDays > 0) {
            (completedDaysCount.toFloat() / durationDays.toFloat()).coerceIn(0f, 1f)
        } else 0f

    val manualProgressPercentage: Int
        get() = (manualProgressFraction * 100f).toInt()

    // ------------------------------------------------------------------------
    // Circle 2: Automatic Progress (Increases ONLY via elapsed 24-hour time)
    // ------------------------------------------------------------------------
    fun getAutomaticCompletedDays(nowMillis: Long = System.currentTimeMillis()): Int {
        val elapsedMillis = (nowMillis - startDateMillis).coerceAtLeast(0L)
        val full24HourDays = (elapsedMillis / EXACT_24_HOURS_MILLIS).toInt()
        return full24HourDays.coerceIn(0, durationDays)
    }

    fun getAutomaticRemainingDays(nowMillis: Long = System.currentTimeMillis()): Int {
        return (durationDays - getAutomaticCompletedDays(nowMillis)).coerceAtLeast(0)
    }

    fun getAutomaticProgressFraction(nowMillis: Long = System.currentTimeMillis()): Float {
        return if (durationDays > 0) {
            (getAutomaticCompletedDays(nowMillis).toFloat() / durationDays.toFloat()).coerceIn(0f, 1f)
        } else 0f
    }

    fun getAutomaticProgressPercentage(nowMillis: Long = System.currentTimeMillis()): Int {
        return (getAutomaticProgressFraction(nowMillis) * 100f).toInt()
    }

    fun isAutomaticCompleted(nowMillis: Long = System.currentTimeMillis()): Boolean {
        return getAutomaticCompletedDays(nowMillis) >= durationDays
    }

    fun getRemainingMillisUntilNextAutoDay(nowMillis: Long = System.currentTimeMillis()): Long {
        if (isAutomaticCompleted(nowMillis)) return 0L
        val elapsedMillis = (nowMillis - startDateMillis).coerceAtLeast(0L)
        val remainderMillis = elapsedMillis % EXACT_24_HOURS_MILLIS
        return (EXACT_24_HOURS_MILLIS - remainderMillis).coerceAtLeast(0L)
    }

    // ------------------------------------------------------------------------
    // Unified Single Progress Properties (Depends strictly on trackingMode)
    // ------------------------------------------------------------------------
    fun getEffectiveCompletedDays(nowMillis: Long = System.currentTimeMillis()): Int {
        return when (trackingMode) {
            TrackingMode.AUTOMATIC -> getAutomaticCompletedDays(nowMillis)
            TrackingMode.MANUAL -> completedDaysCount
        }
    }

    fun getEffectiveRemainingDays(nowMillis: Long = System.currentTimeMillis()): Int {
        return (durationDays - getEffectiveCompletedDays(nowMillis)).coerceAtLeast(0)
    }

    fun getEffectiveProgressFraction(nowMillis: Long = System.currentTimeMillis()): Float {
        return if (durationDays > 0) {
            (getEffectiveCompletedDays(nowMillis).toFloat() / durationDays.toFloat()).coerceIn(0f, 1f)
        } else 0f
    }

    fun getEffectiveProgressPercentage(nowMillis: Long = System.currentTimeMillis()): Int {
        return (getEffectiveProgressFraction(nowMillis) * 100f).toInt()
    }

    val remainingDays: Int
        get() = getEffectiveRemainingDays()

    val progressFraction: Float
        get() = getEffectiveProgressFraction()

    val progressPercentage: Int
        get() = getEffectiveProgressPercentage()

    val expectedCompletionDateMillis: Long
        get() = startDateMillis + (durationDays.toLong() * 24L * 60L * 60L * 1000L)

    companion object {
        const val EXACT_24_HOURS_MILLIS: Long = 24L * 60L * 60L * 1000L // 86,400,000 ms
    }

    /**
     * Checks if 24 hours have completely passed since the previous check-in.
     */
    fun isCheckInEligible(nowMillis: Long = System.currentTimeMillis()): Boolean {
        if (isCompleted) return false
        val lastTimestamp = lastCheckInTimestampMillis ?: return true
        return nowMillis >= (lastTimestamp + EXACT_24_HOURS_MILLIS)
    }

    /**
     * Returns remaining countdown milliseconds until next check-in is eligible.
     */
    fun getRemainingCooldownMillis(nowMillis: Long = System.currentTimeMillis()): Long {
        if (isCompleted) return 0L
        val lastTimestamp = lastCheckInTimestampMillis ?: return 0L
        val nextEligible = lastTimestamp + EXACT_24_HOURS_MILLIS
        return (nextEligible - nowMillis).coerceAtLeast(0L)
    }
}

data class CheckInRecord(
    val id: String = UUID.randomUUID().toString(),
    val goalId: String,
    val dateKey: String, // Format: YYYY-MM-DD
    val timestampMillis: Long = System.currentTimeMillis(),
    val note: String = ""
)

