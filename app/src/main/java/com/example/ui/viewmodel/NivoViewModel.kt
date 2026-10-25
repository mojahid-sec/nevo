package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.localization.AppLanguage
import com.example.data.model.CheckInRecord
import com.example.data.model.Goal
import com.example.data.repository.GoalRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class NivoViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = GoalRepository.getInstance(application)
    val pinManager = com.example.data.security.PinManager.getInstance(application)
    private val checkInMutex = Mutex()

    private val _isPinEnabled = MutableStateFlow(pinManager.isPinEnabled())
    val isPinEnabled: StateFlow<Boolean> = _isPinEnabled.asStateFlow()

    fun refreshPinStatus() {
        _isPinEnabled.value = pinManager.isPinEnabled()
    }

    val goals: StateFlow<List<Goal>> = repository.goals
    val checkIns: StateFlow<List<CheckInRecord>> = repository.checkIns
    val userName: StateFlow<String> = repository.userName
    val language: StateFlow<AppLanguage> = repository.selectedLanguage
    val currentTheme: StateFlow<com.example.ui.theme.NivoTheme> = repository.selectedTheme

    val selectedGoal: StateFlow<Goal?> = repository.goals.combine(repository.goals) { list, _ ->
        list.find { it.isSelected } ?: list.firstOrNull()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Current time ticker for precise 24-hour countdowns (updates every second)
    private val _currentTimeMillis = MutableStateFlow(System.currentTimeMillis())
    val currentTimeMillis: StateFlow<Long> = _currentTimeMillis.asStateFlow()

    val goalCompletedEvent = MutableStateFlow<Goal?>(null)

    init {
        // High-precision 1-second ticker for exact 24-hour timer countdown and automatic completions
        viewModelScope.launch {
            while (true) {
                val now = System.currentTimeMillis()
                _currentTimeMillis.value = now
                val autoCompleted = repository.checkAndCompleteAutomaticGoals(now)
                autoCompleted.firstOrNull()?.let { goal ->
                    goalCompletedEvent.value = goal
                }
                delay(1000L)
            }
        }
    }

    /**
     * Checks if selected goal is currently eligible for check-in based on exact 24 hours.
     */
    fun isGoalEligibleForCheckIn(goal: Goal?): Boolean {
        if (goal == null) return false
        return goal.isCheckInEligible(_currentTimeMillis.value)
    }

    /**
     * Returns remaining countdown in hours, minutes, seconds for manual check-in.
     */
    fun getCountdownForGoal(goal: Goal?): Triple<Long, Long, Long> {
        if (goal == null) return Triple(0L, 0L, 0L)
        val remainingMillis = goal.getRemainingCooldownMillis(_currentTimeMillis.value)
        val totalSeconds = remainingMillis / 1000L
        val hours = totalSeconds / 3600L
        val minutes = (totalSeconds % 3600L) / 60L
        val seconds = totalSeconds % 60L
        return Triple(hours, minutes, seconds)
    }

    /**
     * Returns remaining countdown in hours, minutes, seconds until next automatic 24h day completes.
     */
    fun getAutomaticCountdownForGoal(goal: Goal?): Triple<Long, Long, Long> {
        if (goal == null) return Triple(0L, 0L, 0L)
        val remainingMillis = goal.getRemainingMillisUntilNextAutoDay(_currentTimeMillis.value)
        val totalSeconds = remainingMillis / 1000L
        val hours = totalSeconds / 3600L
        val minutes = (totalSeconds % 3600L) / 60L
        val seconds = totalSeconds % 60L
        return Triple(hours, minutes, seconds)
    }

    /**
     * Executes check-in with mutex protection against duplicate taps.
     */
    fun checkInSelectedGoal(onGoalCompleted: ((Goal) -> Unit)? = null) {
        val currentGoal = selectedGoal.value ?: return
        viewModelScope.launch {
            checkInMutex.withLock {
                val now = System.currentTimeMillis()
                if (!currentGoal.isCheckInEligible(now)) {
                    return@launch
                }
                val resultGoal = repository.checkInGoal(currentGoal.id)
                if (resultGoal != null && resultGoal.isCompleted) {
                    goalCompletedEvent.value = resultGoal
                    onGoalCompleted?.invoke(resultGoal)
                }
            }
        }
    }

    fun createGoal(
        title: String,
        durationDays: Int,
        startDateMillis: Long,
        reason: String,
        trackingMode: com.example.data.model.TrackingMode = com.example.data.model.TrackingMode.MANUAL,
        onSuccess: (Goal) -> Unit
    ) {
        viewModelScope.launch {
            val newGoal = repository.createGoal(
                title = title,
                durationDays = durationDays,
                startDateMillis = startDateMillis,
                reason = reason,
                trackingMode = trackingMode
            )
            onSuccess(newGoal)
        }
    }

    fun updateGoal(
        id: String,
        title: String,
        durationDays: Int,
        startDateMillis: Long,
        reason: String,
        trackingMode: com.example.data.model.TrackingMode = com.example.data.model.TrackingMode.MANUAL,
        onSuccess: (Goal) -> Unit
    ) {
        viewModelScope.launch {
            val updated = repository.updateGoal(
                id = id,
                title = title,
                durationDays = durationDays,
                startDateMillis = startDateMillis,
                reason = reason,
                trackingMode = trackingMode
            )
            if (updated != null) {
                onSuccess(updated)
            }
        }
    }

    fun selectGoal(goalId: String) {
        viewModelScope.launch {
            repository.selectGoal(goalId)
        }
    }

    fun deleteGoal(goalId: String) {
        viewModelScope.launch {
            repository.deleteGoal(goalId)
        }
    }

    fun setLanguage(newLanguage: AppLanguage) {
        viewModelScope.launch {
            repository.updateLanguage(newLanguage)
        }
    }

    fun setTheme(newTheme: com.example.ui.theme.NivoTheme) {
        viewModelScope.launch {
            repository.updateTheme(newTheme)
        }
    }

    fun updateUserName(name: String) {
        viewModelScope.launch {
            repository.updateUserName(name)
        }
    }

    fun resetData() {
        viewModelScope.launch {
            repository.resetData()
        }
    }

    fun clearGoalCompletedEvent() {
        goalCompletedEvent.value = null
    }

    fun exportBackupJson(): String = repository.exportBackupJson()

    fun importBackupJson(json: String): Boolean = repository.importBackupJson(json)
}
