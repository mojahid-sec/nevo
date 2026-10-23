package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import com.example.ui.theme.NivoTheme
import com.example.ui.theme.getThemeColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.localization.AppLanguage
import com.example.data.localization.NivoStrings
import com.example.ui.components.NivoLogo
import com.example.ui.theme.CardBackground
import com.example.ui.theme.CardElevated
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.OutlineGrey
import com.example.ui.theme.PrimaryRed
import com.example.ui.theme.PrimaryText
import com.example.ui.theme.SecondaryText
import com.example.ui.viewmodel.NivoViewModel

@Composable
fun SettingsScreen(
    viewModel: NivoViewModel,
    onNavigateToAboutDeveloper: () -> Unit,
    onRequestSetPin: () -> Unit,
    onRequestDisablePin: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val userName by viewModel.userName.collectAsState()
    val language by viewModel.language.collectAsState()
    val currentTheme by viewModel.currentTheme.collectAsState()
    val isPinEnabled by viewModel.isPinEnabled.collectAsState()

    var showNameDialog by remember { mutableStateOf(false) }
    var tempName by remember { mutableStateOf(userName) }

    var showLanguageDialog by remember { mutableStateOf(false) }
    var showThemeDialog by remember { mutableStateOf(false) }
    var notificationsEnabled by remember { mutableStateOf(true) }
    var showResetDialog by remember { mutableStateOf(false) }
    var showExportDialog by remember { mutableStateOf(false) }
    var showAboutNivoDialog by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 14.dp)
            .testTag("settings_screen")
    ) {
        Text(
            text = NivoStrings.settingsTitle(language),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryText,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )
        Text(
            text = NivoStrings.settingsSubtitle(language),
            fontSize = 13.sp,
            color = SecondaryText,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        // 1. Personalization Section
        SettingsSectionHeader(NivoStrings.sectionPersonalization(language))
        SettingsCard {
            SettingsItemRow(
                icon = Icons.Filled.Person,
                title = NivoStrings.yourName(language),
                subtitle = userName,
                onClick = {
                    tempName = userName
                    showNameDialog = true
                }
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // 2. Language & 3. Appearance Sections
        SettingsSectionHeader(NivoStrings.sectionLanguage(language))
        SettingsCard {
            SettingsItemRow(
                icon = Icons.Filled.Language,
                title = NivoStrings.sectionLanguage(language),
                subtitle = "${language.displayName} (${language.nativeName})",
                onClick = { showLanguageDialog = true }
            )
            SettingsDivider()
            SettingsItemRow(
                icon = Icons.Filled.DarkMode,
                title = NivoStrings.sectionAppearance(language),
                subtitle = currentTheme.getLocalizedName(language),
                onClick = { showThemeDialog = true }
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // 4. Optional App Lock & 5. Notifications
        SettingsSectionHeader(NivoStrings.sectionSecurity(language))
        SettingsCard {
            SettingsToggleRow(
                icon = Icons.Filled.Lock,
                title = NivoStrings.appLockTitle(language),
                subtitle = if (isPinEnabled) {
                    if (language == AppLanguage.BANGLA) "৪-সংখ্যার পিন দিয়ে সুরক্ষিত" else "Protected with 4-digit PIN"
                } else {
                    NivoStrings.appLockSubtitle(language)
                },
                checked = isPinEnabled,
                onCheckedChange = { checked ->
                    if (checked) {
                        onRequestSetPin()
                    } else {
                        onRequestDisablePin()
                    }
                }
            )

            if (isPinEnabled) {
                SettingsDivider()
                SettingsItemRow(
                    icon = Icons.Filled.Key,
                    title = NivoStrings.changePin(language),
                    subtitle = if (language == AppLanguage.BANGLA) "নতুন ৪-সংখ্যার পিন সেট করুন" else "Update your 4-digit lock code",
                    onClick = onRequestSetPin
                )
            }

            SettingsDivider()
            SettingsToggleRow(
                icon = Icons.Filled.Notifications,
                title = NivoStrings.reminderTitle(language),
                subtitle = NivoStrings.reminderSubtitle(language),
                checked = notificationsEnabled,
                onCheckedChange = { notificationsEnabled = it }
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // 6. Privacy & 7. Data Management
        SettingsSectionHeader(NivoStrings.sectionData(language))
        SettingsCard {
            SettingsItemRow(
                icon = Icons.Filled.Security,
                title = NivoStrings.privacyTitle(language),
                subtitle = NivoStrings.privacySubtitle(language),
                onClick = { }
            )
            SettingsDivider()
            SettingsItemRow(
                icon = Icons.Filled.Storage,
                title = NivoStrings.exportDataTitle(language),
                subtitle = NivoStrings.exportDataDesc(language),
                onClick = { showExportDialog = true }
            )
            SettingsDivider()
            SettingsItemRow(
                icon = Icons.Filled.Storage,
                title = NivoStrings.dataManagementTitle(language),
                subtitle = NivoStrings.dataManagementSubtitle(language),
                onClick = { showResetDialog = true }
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // 8. About NIVO & 9. About Developer
        SettingsSectionHeader(NivoStrings.sectionAbout(language))
        SettingsCard {
            SettingsItemRow(
                icon = Icons.Filled.Info,
                title = NivoStrings.aboutNivo(language),
                subtitle = "Version 2.0 • Brand & Vision",
                onClick = { showAboutNivoDialog = true }
            )
            SettingsDivider()
            SettingsItemRow(
                icon = Icons.Filled.Person,
                title = NivoStrings.aboutDeveloper(language),
                subtitle = NivoStrings.devName(language),
                onClick = onNavigateToAboutDeveloper
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Centered Brand Identity layout in footer
        NivoLogo(
            iconSize = 56.dp,
            showTagline = true,
            language = language,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))
    }

    // Language Selection Dialog
    if (showLanguageDialog) {
        AlertDialog(
            onDismissRequest = { showLanguageDialog = false },
            containerColor = CardBackground,
            title = {
                Text(
                    NivoStrings.selectLanguageTitle(language),
                    color = PrimaryText,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    AppLanguage.values().forEach { langOption ->
                        val isSelected = language == langOption
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) CardElevated else CardBackground)
                                .clickable {
                                    viewModel.setLanguage(langOption)
                                    showLanguageDialog = false
                                }
                                .padding(horizontal = 14.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (isSelected) Icons.Filled.RadioButtonChecked else Icons.Filled.RadioButtonUnchecked,
                                contentDescription = null,
                                tint = if (isSelected) PrimaryRed else SecondaryText,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = langOption.nativeName,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) PrimaryRed else PrimaryText
                                )
                                Text(
                                    text = langOption.displayName,
                                    fontSize = 12.sp,
                                    color = SecondaryText
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showLanguageDialog = false }) {
                    Text(NivoStrings.doneButton(language), color = PrimaryRed)
                }
            }
        )
    }

    // 10 Premium Themes Selection Dialog
    if (showThemeDialog) {
        val themeScrollState = rememberScrollState()
        AlertDialog(
            onDismissRequest = { showThemeDialog = false },
            containerColor = CardBackground,
            title = {
                Text(
                    text = NivoStrings.selectThemeTitle(language),
                    color = PrimaryText,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(380.dp)
                        .verticalScroll(themeScrollState)
                ) {
                    NivoTheme.values().forEach { themeOption ->
                        val isSelected = currentTheme == themeOption
                        val palette = remember(themeOption) { getThemeColors(themeOption) }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) CardElevated else CardBackground)
                                .border(
                                    width = if (isSelected) 1.2.dp else 0.8.dp,
                                    color = if (isSelected) PrimaryRed else OutlineGrey.copy(alpha = 0.4f),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable {
                                    viewModel.setTheme(themeOption)
                                    showThemeDialog = false
                                }
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (isSelected) Icons.Filled.RadioButtonChecked else Icons.Filled.RadioButtonUnchecked,
                                contentDescription = null,
                                tint = if (isSelected) PrimaryRed else SecondaryText,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = themeOption.getLocalizedName(language),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) PrimaryRed else PrimaryText
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = themeOption.getLocalizedDesc(language),
                                    fontSize = 11.sp,
                                    color = SecondaryText,
                                    lineHeight = 14.sp
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                // 4-Color Swatch Preview (Background, Card, Primary Accent, Secondary Accent)
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    listOf(
                                        palette.background,
                                        palette.card,
                                        palette.primaryAccent,
                                        palette.secondaryAccent
                                    ).forEach { color ->
                                        Box(
                                            modifier = Modifier
                                                .size(14.dp)
                                                .clip(CircleShape)
                                                .background(color)
                                                .border(0.8.dp, OutlineGrey, CircleShape)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showThemeDialog = false }) {
                    Text(NivoStrings.doneButton(language), color = PrimaryRed, fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    // Edit Name Dialog
    if (showNameDialog) {
        AlertDialog(
            onDismissRequest = { showNameDialog = false },
            containerColor = CardBackground,
            title = { Text(NivoStrings.updateName(language), color = PrimaryText, fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    OutlinedTextField(
                        value = tempName,
                        onValueChange = { tempName = it },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = PrimaryText,
                            unfocusedTextColor = PrimaryText,
                            focusedBorderColor = PrimaryRed,
                            unfocusedBorderColor = OutlineGrey,
                            cursorColor = PrimaryRed
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.updateUserName(tempName)
                        showNameDialog = false
                    }
                ) {
                    Text(NivoStrings.saveGoalButton(language), color = PrimaryRed, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showNameDialog = false }) {
                    Text(NivoStrings.cancelButton(language), color = SecondaryText)
                }
            }
        )
    }

    // Export Data Dialog
    if (showExportDialog) {
        val backupJson = remember { viewModel.exportBackupJson() }
        AlertDialog(
            onDismissRequest = { showExportDialog = false },
            containerColor = CardBackground,
            title = { Text(NivoStrings.exportDataTitle(language), color = PrimaryText, fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text(
                        NivoStrings.exportDataDesc(language),
                        fontSize = 13.sp,
                        color = SecondaryText
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(DarkBackground)
                            .border(1.dp, OutlineGrey, RoundedCornerShape(10.dp))
                            .padding(8.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            text = backupJson,
                            fontSize = 11.sp,
                            color = PrimaryText
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("NIVO_BACKUP", backupJson)
                        clipboard.setPrimaryClip(clip)
                        Toast.makeText(context, NivoStrings.copiedSuccess(language), Toast.LENGTH_SHORT).show()
                        showExportDialog = false
                    }
                ) {
                    Text(NivoStrings.copyToClipboard(language), color = PrimaryRed, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showExportDialog = false }) {
                    Text(NivoStrings.cancelButton(language), color = SecondaryText)
                }
            }
        )
    }

    // Reset Data Dialog
    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            containerColor = CardBackground,
            title = { Text(NivoStrings.resetConfirmTitle(language), color = PrimaryText, fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    NivoStrings.resetConfirmMessage(language),
                    color = SecondaryText,
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.resetData()
                        showResetDialog = false
                    }
                ) {
                    Text(NivoStrings.deleteButton(language), color = PrimaryRed, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text(NivoStrings.cancelButton(language), color = SecondaryText)
                }
            }
        )
    }

    // About NIVO Dialog
    if (showAboutNivoDialog) {
        AlertDialog(
            onDismissRequest = { showAboutNivoDialog = false },
            containerColor = CardBackground,
            title = {
                Text(NivoStrings.aboutNivo(language), color = PrimaryText, fontWeight = FontWeight.Bold)
            },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    NivoLogo(iconSize = 52.dp, showTagline = true, language = language)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (language == AppLanguage.BANGLA) {
                            "নিভো হলো একটি ১০০% অফলাইন লক্ষ্য ও অভ্যাস ট্র্যাকার, যা প্রতিটি মানুষের মধ্যে অটল আত্মশৃঙ্খলা গড়ে তোলার লক্ষ্যে নির্মিত। কোনো সাবস্ক্রিপশন নেই, বিজ্ঞাপন নেই, ট্র্যাকিং নেই—শুধু আপনি এবং আপনার লক্ষ্য, প্রতিদিন এক ধাপ করে।"
                        } else {
                            "NIVO is an offline-first goal and habit tracker designed to help you build unbreakable discipline. No subscriptions, no ads, no trackers — just you and your goals, one day at a time."
                        },
                        color = SecondaryText,
                        fontSize = 13.sp,
                        lineHeight = 19.sp
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showAboutNivoDialog = false }) {
                    Text(NivoStrings.doneButton(language), color = PrimaryRed, fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}

@Composable
private fun SettingsSectionHeader(text: String) {
    Text(
        text = text.uppercase(),
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color = PrimaryRed,
        letterSpacing = 1.sp,
        modifier = Modifier.padding(start = 4.dp, bottom = 6.dp)
    )
}

@Composable
private fun SettingsCard(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CardBackground)
            .border(1.dp, OutlineGrey, RoundedCornerShape(16.dp))
            .padding(vertical = 4.dp)
    ) {
        Column {
            content()
        }
    }
}

@Composable
private fun SettingsItemRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = PrimaryRed,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = PrimaryText
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = SecondaryText
            )
        }
        Icon(
            imageVector = Icons.Filled.ChevronRight,
            contentDescription = null,
            tint = SecondaryText,
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
private fun SettingsToggleRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = PrimaryRed,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = PrimaryText
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = SecondaryText
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = PrimaryText,
                checkedTrackColor = PrimaryRed,
                uncheckedThumbColor = SecondaryText,
                uncheckedTrackColor = CardElevated
            )
        )
    }
}

@Composable
private fun SettingsDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 50.dp)
            .height(0.8.dp)
            .background(OutlineGrey.copy(alpha = 0.5f))
    )
}
