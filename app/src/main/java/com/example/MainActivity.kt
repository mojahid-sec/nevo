package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.localization.NivoStrings
import com.example.ui.navigation.BottomNavDestination
import com.example.ui.navigation.Screen
import com.example.ui.screens.AboutDeveloperScreen
import com.example.ui.screens.CalendarScreen
import com.example.ui.screens.CreateGoalScreen
import com.example.ui.screens.GoalCompletionScreen
import com.example.ui.screens.GoalsScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.PinLockScreen
import com.example.ui.screens.PinMode
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkRed
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.PrimaryRed
import com.example.ui.theme.PrimaryText
import com.example.ui.theme.PureBlack
import com.example.ui.theme.SecondaryText
import com.example.ui.viewmodel.NivoViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: NivoViewModel = viewModel()
            val currentTheme by viewModel.currentTheme.collectAsState()
            MyApplicationTheme(theme = currentTheme) {
                NivoApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun NivoApp(viewModel: NivoViewModel = viewModel()) {
    var currentTab by remember { mutableStateOf(BottomNavDestination.HOME) }
    var currentSubScreen by remember { mutableStateOf<Screen?>(null) }
    val language by viewModel.language.collectAsState()
    val isPinEnabled by viewModel.isPinEnabled.collectAsState()

    var isUnlocked by remember { mutableStateOf(!viewModel.pinManager.isPinEnabled()) }

    // If PIN is enabled and app is locked, present the secure PinLockScreen
    if (isPinEnabled && !isUnlocked) {
        PinLockScreen(
            mode = PinMode.AUTHENTICATE,
            pinManager = viewModel.pinManager,
            language = language,
            onSuccess = { isUnlocked = true }
        )
        return
    }

    // Hardware back navigation handler
    BackHandler(enabled = currentSubScreen != null || currentTab != BottomNavDestination.HOME) {
        if (currentSubScreen != null) {
            currentSubScreen = null
        } else if (currentTab != BottomNavDestination.HOME) {
            currentTab = BottomNavDestination.HOME
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing),
        containerColor = DarkBackground,
        bottomBar = {
            if (currentSubScreen == null) {
                NavigationBar(
                    modifier = Modifier
                        .background(PureBlack)
                        .testTag("nivo_bottom_navigation"),
                    containerColor = PureBlack,
                    contentColor = PrimaryText,
                    tonalElevation = 0.dp
                ) {
                    BottomNavDestination.values().forEach { destination ->
                        val isSelected = currentTab == destination
                        val title = when (destination) {
                            BottomNavDestination.HOME -> NivoStrings.navHome(language)
                            BottomNavDestination.CALENDAR -> NivoStrings.navCalendar(language)
                            BottomNavDestination.GOALS -> NivoStrings.navGoals(language)
                            BottomNavDestination.SETTINGS -> NivoStrings.navSettings(language)
                        }

                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                currentTab = destination
                            },
                            icon = {
                                Icon(
                                    imageVector = if (isSelected) destination.selectedIcon else destination.unselectedIcon,
                                    contentDescription = title,
                                    tint = if (isSelected) PrimaryRed else SecondaryText
                                )
                            },
                            label = {
                                Text(
                                    text = title,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) PrimaryRed else SecondaryText
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = PrimaryRed,
                                unselectedIconColor = SecondaryText,
                                selectedTextColor = PrimaryRed,
                                unselectedTextColor = SecondaryText,
                                indicatorColor = DarkRed.copy(alpha = 0.25f)
                            ),
                            modifier = Modifier.testTag(destination.testTag)
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val sub = currentSubScreen) {
                is Screen.CreateGoal -> {
                    CreateGoalScreen(
                        viewModel = viewModel,
                        onNavigateBack = { currentSubScreen = null }
                    )
                }

                is Screen.EditGoal -> {
                    CreateGoalScreen(
                        viewModel = viewModel,
                        goalIdToEdit = sub.goalId,
                        onNavigateBack = { currentSubScreen = null }
                    )
                }

                is Screen.GoalCompletion -> {
                    GoalCompletionScreen(
                        goalId = sub.goalId,
                        viewModel = viewModel,
                        onReturnHome = {
                            currentSubScreen = null
                            currentTab = BottomNavDestination.HOME
                        }
                    )
                }

                is Screen.AboutDeveloper -> {
                    AboutDeveloperScreen(
                        viewModel = viewModel,
                        onNavigateBack = { currentSubScreen = null }
                    )
                }

                is Screen.SetPin -> {
                    PinLockScreen(
                        mode = PinMode.SETUP,
                        pinManager = viewModel.pinManager,
                        language = language,
                        onSuccess = {
                            viewModel.refreshPinStatus()
                            currentSubScreen = null
                        },
                        onCancel = { currentSubScreen = null }
                    )
                }

                is Screen.DisablePin -> {
                    PinLockScreen(
                        mode = PinMode.DISABLE,
                        pinManager = viewModel.pinManager,
                        language = language,
                        onSuccess = {
                            viewModel.refreshPinStatus()
                            currentSubScreen = null
                        },
                        onCancel = { currentSubScreen = null }
                    )
                }

                null -> {
                    AnimatedContent(
                        targetState = currentTab,
                        transitionSpec = { fadeIn() togetherWith fadeOut() },
                        label = "tab_transition"
                    ) { tab ->
                        when (tab) {
                            BottomNavDestination.HOME -> {
                                HomeScreen(
                                    viewModel = viewModel,
                                    onCreateGoalClick = { currentSubScreen = Screen.CreateGoal },
                                    onNavigateToGoals = { currentTab = BottomNavDestination.GOALS },
                                    onGoalCompleted = { goalId ->
                                        currentSubScreen = Screen.GoalCompletion(goalId)
                                    }
                                )
                            }

                            BottomNavDestination.CALENDAR -> {
                                CalendarScreen(
                                    viewModel = viewModel
                                )
                            }

                            BottomNavDestination.GOALS -> {
                                GoalsScreen(
                                    viewModel = viewModel,
                                    onCreateGoalClick = { currentSubScreen = Screen.CreateGoal },
                                    onEditGoalClick = { goalId ->
                                        currentSubScreen = Screen.EditGoal(goalId)
                                    }
                                )
                            }

                            BottomNavDestination.SETTINGS -> {
                                SettingsScreen(
                                    viewModel = viewModel,
                                    onNavigateToAboutDeveloper = {
                                        currentSubScreen = Screen.AboutDeveloper
                                    },
                                    onRequestSetPin = {
                                        currentSubScreen = Screen.SetPin
                                    },
                                    onRequestDisablePin = {
                                        currentSubScreen = Screen.DisablePin
                                    }
                                )
                            }
                        }
                    }
                }

                else -> {}
            }
        }
    }
}
