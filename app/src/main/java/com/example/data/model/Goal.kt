package com.example.data.model

import java.util.UUID

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
    val completedDateMillis: Long? = null
) {
    val remainingDays: Int
        get() = (durationDays - completedDaysCount).coerceAtLeast(0)

    val progressFraction: Float
        get() = if (durationDays > 0) {
            (completedDaysCount.toFloat() / durationDays.toFloat()).coerceIn(0f, 1f)
        } else 0f

    val progressPercentage: Int
        get() = (progressFraction * 100f).toInt()

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
