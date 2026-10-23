package com.example.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Calendar : Screen("calendar")
    object Goals : Screen("goals")
    object Settings : Screen("settings")
    object CreateGoal : Screen("create_goal")
    data class EditGoal(val goalId: String) : Screen("edit_goal/$goalId")
    data class GoalCompletion(val goalId: String) : Screen("goal_completion/$goalId")
    object AboutDeveloper : Screen("about_developer")
    object SetPin : Screen("set_pin")
    object DisablePin : Screen("disable_pin")
}

enum class BottomNavDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val screen: Screen,
    val testTag: String
) {
    HOME(
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        screen = Screen.Home,
        testTag = "nav_home"
    ),
    CALENDAR(
        selectedIcon = Icons.Filled.CalendarMonth,
        unselectedIcon = Icons.Outlined.CalendarMonth,
        screen = Screen.Calendar,
        testTag = "nav_calendar"
    ),
    GOALS(
        selectedIcon = Icons.Filled.CheckCircle,
        unselectedIcon = Icons.Outlined.CheckCircle,
        screen = Screen.Goals,
        testTag = "nav_goals"
    ),
    SETTINGS(
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings,
        screen = Screen.Settings,
        testTag = "nav_settings"
    )
}
