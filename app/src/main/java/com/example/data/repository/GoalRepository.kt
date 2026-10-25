package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.data.localization.AppLanguage
import com.example.data.model.CheckInRecord
import com.example.data.model.Goal
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class GoalRepository(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("nivo_offline_prefs", Context.MODE_PRIVATE)

    private val _goals = MutableStateFlow<List<Goal>>(emptyList())
    val goals: StateFlow<List<Goal>> = _goals.asStateFlow()

    private val _checkIns = MutableStateFlow<List<CheckInRecord>>(emptyList())
    val checkIns: StateFlow<List<CheckInRecord>> = _checkIns.asStateFlow()

    private val _userName = MutableStateFlow("Mojahid")
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _selectedLanguage = MutableStateFlow(AppLanguage.ENGLISH)
    val selectedLanguage: StateFlow<AppLanguage> = _selectedLanguage.asStateFlow()

    private val _selectedTheme = MutableStateFlow(com.example.ui.theme.NivoTheme.CRIMSON)
    val selectedTheme: StateFlow<com.example.ui.theme.NivoTheme> = _selectedTheme.asStateFlow()

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    init {
        loadData()
    }

    private fun loadData() {
        val savedLangCode = prefs.getString("app_language", "en") ?: "en"
        _selectedLanguage.value = AppLanguage.fromCode(savedLangCode)

        val savedThemeId = prefs.getString("selected_theme", com.example.ui.theme.NivoTheme.CRIMSON.id) ?: com.example.ui.theme.NivoTheme.CRIMSON.id
        _selectedTheme.value = com.example.ui.theme.NivoTheme.fromId(savedThemeId)

        val savedName = prefs.getString("user_name", "Mojahid") ?: "Mojahid"
        _userName.value = savedName

        val goalsJson = prefs.getString("goals_json", null)
        val loadedGoals = mutableListOf<Goal>()

        if (!goalsJson.isNullOrEmpty()) {
            try {
                val array = JSONArray(goalsJson)
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    val lastTs = if (obj.has("lastCheckInTimestampMillis") && !obj.isNull("lastCheckInTimestampMillis")) {
                        obj.getLong("lastCheckInTimestampMillis")
                    } else null

                    val completedDate = if (obj.has("completedDateMillis") && !obj.isNull("completedDateMillis")) {
                        obj.getLong("completedDateMillis")
                    } else null

                    val trackingMode = com.example.data.model.TrackingMode.fromString(
                        obj.optString("trackingMode", "MANUAL")
                    )

                    loadedGoals.add(
                        Goal(
                            id = obj.getString("id"),
                            title = obj.getString("title"),
                            durationDays = obj.getInt("durationDays").coerceIn(2, 365),
                            startDateMillis = obj.getLong("startDateMillis"),
                            reason = obj.optString("reason", ""),
                            completedDaysCount = obj.getInt("completedDaysCount"),
                            lastCheckInTimestampMillis = lastTs,
                            isSelected = obj.getBoolean("isSelected"),
                            isCompleted = obj.getBoolean("isCompleted"),
                            completedDateMillis = completedDate,
                            trackingMode = trackingMode
                        )
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        if (loadedGoals.isEmpty()) {
            // First run: provide one pristine clean starter goal ready for check-in
            val now = System.currentTimeMillis()
            val starterGoal = Goal(
                title = "Daily Focus & Discipline",
                durationDays = 30,
                startDateMillis = now - (25L * 60 * 60 * 1000L), // started yesterday
                reason = "Build discipline, stay centered, and accomplish my vision.",
                completedDaysCount = 1,
                lastCheckInTimestampMillis = now - (25L * 60 * 60 * 1000L), // 25 hours ago -> eligible now!
                isSelected = true,
                isCompleted = false
            )
            loadedGoals.add(starterGoal)
            saveGoals(loadedGoals)

            val starterCheckIn = CheckInRecord(
                goalId = starterGoal.id,
                dateKey = dateFormat.format(Date(now - (25L * 60 * 60 * 1000L))),
                timestampMillis = now - (25L * 60 * 60 * 1000L),
                note = "First step taken. Ready for today."
            )
            saveCheckIns(listOf(starterCheckIn))
        } else {
            _goals.value = loadedGoals
            loadCheckIns()
        }
    }

    private fun loadCheckIns() {
        val checkInsJson = prefs.getString("checkins_json", null)
        val list = mutableListOf<CheckInRecord>()
        if (!checkInsJson.isNullOrEmpty()) {
            try {
                val array = JSONArray(checkInsJson)
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    list.add(
                        CheckInRecord(
                            id = obj.getString("id"),
                            goalId = obj.getString("goalId"),
                            dateKey = obj.getString("dateKey"),
                            timestampMillis = obj.getLong("timestampMillis"),
                            note = obj.optString("note", "")
                        )
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        _checkIns.value = list
    }

    private fun saveGoals(list: List<Goal>) {
        _goals.value = list
        val array = JSONArray()
        list.forEach { goal ->
            val obj = JSONObject().apply {
                put("id", goal.id)
                put("title", goal.title)
                put("durationDays", goal.durationDays)
                put("startDateMillis", goal.startDateMillis)
                put("reason", goal.reason)
                put("completedDaysCount", goal.completedDaysCount)
                if (goal.lastCheckInTimestampMillis != null) {
                    put("lastCheckInTimestampMillis", goal.lastCheckInTimestampMillis)
                }
                put("isSelected", goal.isSelected)
                put("isCompleted", goal.isCompleted)
                if (goal.completedDateMillis != null) {
                    put("completedDateMillis", goal.completedDateMillis)
                }
                put("trackingMode", goal.trackingMode.name)
            }
            array.put(obj)
        }
        prefs.edit().putString("goals_json", array.toString()).apply()
    }

    private fun saveCheckIns(list: List<CheckInRecord>) {
        _checkIns.value = list
        val array = JSONArray()
        list.forEach { record ->
            val obj = JSONObject().apply {
                put("id", record.id)
                put("goalId", record.goalId)
                put("dateKey", record.dateKey)
                put("timestampMillis", record.timestampMillis)
                put("note", record.note)
            }
            array.put(obj)
        }
        prefs.edit().putString("checkins_json", array.toString()).apply()
    }

    fun getTodayKey(): String = dateFormat.format(Date())

    /**
     * Checks in a goal adhering to the EXACT 24-HOUR rule.
     * Prevents check-in if 24 hours have not yet elapsed since lastCheckInTimestampMillis.
     */
    @Synchronized
    fun checkInGoal(goalId: String, note: String = ""): Goal? {
        val currentGoals = _goals.value
        val targetGoal = currentGoals.find { it.id == goalId } ?: return null
        val now = System.currentTimeMillis()

        // 24-Hour Enforcement: Reject check-in if ineligible
        if (!targetGoal.isCheckInEligible(now)) {
            return null
        }

        // Add check-in history record
        val newRecord = CheckInRecord(
            goalId = goalId,
            dateKey = dateFormat.format(Date(now)),
            timestampMillis = now,
            note = note
        )
        val updatedCheckIns = _checkIns.value + newRecord
        saveCheckIns(updatedCheckIns)

        // Increment completed days & update last check-in timestamp
        val newCount = targetGoal.completedDaysCount + 1
        val isGoalCompleted = newCount >= targetGoal.durationDays

        val updatedGoal = targetGoal.copy(
            completedDaysCount = newCount,
            lastCheckInTimestampMillis = now,
            isCompleted = isGoalCompleted,
            completedDateMillis = if (isGoalCompleted) now else null
        )

        val updatedGoals = currentGoals.map {
            if (it.id == goalId) updatedGoal else it
        }
        saveGoals(updatedGoals)

        return updatedGoal
    }

    fun createGoal(
        title: String,
        durationDays: Int,
        startDateMillis: Long,
        reason: String,
        trackingMode: com.example.data.model.TrackingMode = com.example.data.model.TrackingMode.MANUAL
    ): Goal {
        val validDuration = durationDays.coerceIn(2, 365)
        val currentGoals = _goals.value
        val shouldSelect = currentGoals.none { it.isSelected } || currentGoals.isEmpty()

        val newGoal = Goal(
            title = title.trim(),
            durationDays = validDuration,
            startDateMillis = startDateMillis,
            reason = reason.trim(),
            completedDaysCount = 0,
            lastCheckInTimestampMillis = null, // Ready for first check-in immediately!
            isSelected = shouldSelect,
            isCompleted = false,
            trackingMode = trackingMode
        )

        val updatedGoals = if (shouldSelect) {
            currentGoals.map { it.copy(isSelected = false) } + newGoal
        } else {
            currentGoals + newGoal
        }

        saveGoals(updatedGoals)
        return newGoal
    }

    fun updateGoal(
        id: String,
        title: String,
        durationDays: Int,
        startDateMillis: Long,
        reason: String,
        trackingMode: com.example.data.model.TrackingMode = com.example.data.model.TrackingMode.MANUAL
    ): Goal? {
        val validDuration = durationDays.coerceIn(2, 365)
        val currentGoals = _goals.value
        val existing = currentGoals.find { it.id == id } ?: return null

        val isCompletedNow = when (trackingMode) {
            com.example.data.model.TrackingMode.AUTOMATIC -> existing.isAutomaticCompleted()
            com.example.data.model.TrackingMode.MANUAL -> existing.completedDaysCount >= validDuration
        }
        val updated = existing.copy(
            title = title.trim(),
            durationDays = validDuration,
            startDateMillis = startDateMillis,
            reason = reason.trim(),
            isCompleted = isCompletedNow,
            completedDateMillis = if (isCompletedNow) (existing.completedDateMillis ?: System.currentTimeMillis()) else null,
            trackingMode = trackingMode
        )

        val updatedGoals = currentGoals.map { if (it.id == id) updated else it }
        saveGoals(updatedGoals)
        return updated
    }

    /**
     * Checks if any automatic tracking goals have elapsed their full target duration.
     * Automatically completes them and returns newly completed goals.
     */
    @Synchronized
    fun checkAndCompleteAutomaticGoals(nowMillis: Long = System.currentTimeMillis()): List<Goal> {
        val current = _goals.value
        var changed = false
        val newlyCompleted = mutableListOf<Goal>()

        val updatedList = current.map { goal ->
            if (!goal.isCompleted &&
                goal.trackingMode == com.example.data.model.TrackingMode.AUTOMATIC &&
                goal.isAutomaticCompleted(nowMillis)
            ) {
                changed = true
                val completed = goal.copy(
                    isCompleted = true,
                    completedDateMillis = nowMillis
                )
                newlyCompleted.add(completed)
                completed
            } else {
                goal
            }
        }

        if (changed) {
            saveGoals(updatedList)
        }
        return newlyCompleted
    }

    fun selectGoal(goalId: String) {
        val updatedGoals = _goals.value.map {
            it.copy(isSelected = (it.id == goalId))
        }
        saveGoals(updatedGoals)
    }

    fun deleteGoal(goalId: String) {
        val current = _goals.value.filter { it.id != goalId }
        val updated = if (current.isNotEmpty() && current.none { it.isSelected }) {
            current.mapIndexed { index, goal -> if (index == 0) goal.copy(isSelected = true) else goal }
        } else {
            current
        }
        saveGoals(updated)

        // Remove check-in records for this deleted goal
        val remainingCheckIns = _checkIns.value.filter { it.goalId != goalId }
        saveCheckIns(remainingCheckIns)
    }

    fun updateLanguage(language: AppLanguage) {
        _selectedLanguage.value = language
        prefs.edit().putString("app_language", language.code).apply()
    }

    fun updateTheme(theme: com.example.ui.theme.NivoTheme) {
        _selectedTheme.value = theme
        prefs.edit().putString("selected_theme", theme.id).apply()
    }

    fun updateUserName(name: String) {
        val trimmed = name.trim().ifEmpty { "Mojahid" }
        _userName.value = trimmed
        prefs.edit().putString("user_name", trimmed).apply()
    }

    fun resetData() {
        prefs.edit().clear().apply()
        _goals.value = emptyList()
        _checkIns.value = emptyList()
        _userName.value = "Mojahid"
        _selectedLanguage.value = AppLanguage.ENGLISH
        _selectedTheme.value = com.example.ui.theme.NivoTheme.CRIMSON
    }

    fun exportBackupJson(): String {
        val root = JSONObject()
        root.put("version", 2)
        root.put("userName", _userName.value)
        root.put("language", _selectedLanguage.value.code)
        root.put("selectedTheme", _selectedTheme.value.id)

        val goalsArray = JSONArray()
        _goals.value.forEach { goal ->
            val obj = JSONObject().apply {
                put("id", goal.id)
                put("title", goal.title)
                put("durationDays", goal.durationDays)
                put("startDateMillis", goal.startDateMillis)
                put("reason", goal.reason)
                put("completedDaysCount", goal.completedDaysCount)
                if (goal.lastCheckInTimestampMillis != null) {
                    put("lastCheckInTimestampMillis", goal.lastCheckInTimestampMillis)
                }
                put("isSelected", goal.isSelected)
                put("isCompleted", goal.isCompleted)
                if (goal.completedDateMillis != null) {
                    put("completedDateMillis", goal.completedDateMillis)
                }
                put("trackingMode", goal.trackingMode.name)
            }
            goalsArray.put(obj)
        }
        root.put("goals", goalsArray)

        val checkInsArray = JSONArray()
        _checkIns.value.forEach { checkIn ->
            val obj = JSONObject().apply {
                put("id", checkIn.id)
                put("goalId", checkIn.goalId)
                put("dateKey", checkIn.dateKey)
                put("timestampMillis", checkIn.timestampMillis)
                put("note", checkIn.note)
            }
            checkInsArray.put(obj)
        }
        root.put("checkIns", checkInsArray)

        return root.toString(2)
    }

    fun importBackupJson(jsonString: String): Boolean {
        return try {
            val root = JSONObject(jsonString)
            if (root.has("userName")) {
                updateUserName(root.getString("userName"))
            }
            if (root.has("language")) {
                updateLanguage(AppLanguage.fromCode(root.getString("language")))
            }
            if (root.has("selectedTheme")) {
                updateTheme(com.example.ui.theme.NivoTheme.fromId(root.getString("selectedTheme")))
            }

            if (root.has("goals")) {
                val array = root.getJSONArray("goals")
                val loaded = mutableListOf<Goal>()
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    val lastTs = if (obj.has("lastCheckInTimestampMillis") && !obj.isNull("lastCheckInTimestampMillis")) {
                        obj.getLong("lastCheckInTimestampMillis")
                    } else null

                    val completedDate = if (obj.has("completedDateMillis") && !obj.isNull("completedDateMillis")) {
                        obj.getLong("completedDateMillis")
                    } else null

                    val trackingMode = com.example.data.model.TrackingMode.fromString(
                        obj.optString("trackingMode", "MANUAL")
                    )

                    loaded.add(
                        Goal(
                            id = obj.getString("id"),
                            title = obj.getString("title"),
                            durationDays = obj.getInt("durationDays").coerceIn(2, 365),
                            startDateMillis = obj.getLong("startDateMillis"),
                            reason = obj.optString("reason", ""),
                            completedDaysCount = obj.getInt("completedDaysCount"),
                            lastCheckInTimestampMillis = lastTs,
                            isSelected = obj.getBoolean("isSelected"),
                            isCompleted = obj.getBoolean("isCompleted"),
                            completedDateMillis = completedDate,
                            trackingMode = trackingMode
                        )
                    )
                }
                saveGoals(loaded)
            }

            if (root.has("checkIns")) {
                val array = root.getJSONArray("checkIns")
                val loadedCheckIns = mutableListOf<CheckInRecord>()
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    loadedCheckIns.add(
                        CheckInRecord(
                            id = obj.getString("id"),
                            goalId = obj.getString("goalId"),
                            dateKey = obj.getString("dateKey"),
                            timestampMillis = obj.getLong("timestampMillis"),
                            note = obj.optString("note", "")
                        )
                    )
                }
                saveCheckIns(loadedCheckIns)
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: GoalRepository? = null

        fun getInstance(context: Context): GoalRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: GoalRepository(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
}
