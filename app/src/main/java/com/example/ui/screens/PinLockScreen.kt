package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.localization.AppLanguage
import com.example.data.localization.NivoStrings
import com.example.data.localization.toLocalizedDigits
import com.example.data.security.PinManager
import com.example.ui.components.NivoRibbonSymbol
import com.example.ui.theme.CardBackground
import com.example.ui.theme.CardElevated
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.OutlineGrey
import com.example.ui.theme.PrimaryRed
import com.example.ui.theme.PrimaryText
import com.example.ui.theme.PureBlack
import com.example.ui.theme.SecondaryText

enum class PinMode {
    AUTHENTICATE,
    SETUP,
    DISABLE
}

@Composable
fun PinLockScreen(
    mode: PinMode,
    pinManager: PinManager,
    language: AppLanguage,
    onSuccess: () -> Unit,
    onCancel: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var enteredPin by remember { mutableStateOf("") }
    var firstEnteredPin by remember { mutableStateOf("") }
    var isConfirmStep by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    fun handleDigit(digit: String) {
        if (enteredPin.length < 4) {
            val newPin = enteredPin + digit
            enteredPin = newPin
            errorMessage = null

            if (newPin.length == 4) {
                when (mode) {
                    PinMode.AUTHENTICATE -> {
                        if (pinManager.verifyPin(newPin)) {
                            onSuccess()
                        } else {
                            errorMessage = NivoStrings.errorIncorrectPin(language)
                            enteredPin = ""
                        }
                    }

                    PinMode.SETUP -> {
                        if (!isConfirmStep) {
                            firstEnteredPin = newPin
                            enteredPin = ""
                            isConfirmStep = true
                        } else {
                            if (newPin == firstEnteredPin) {
                                pinManager.setPin(newPin)
                                onSuccess()
                            } else {
                                errorMessage = NivoStrings.errorPinMismatch(language)
                                enteredPin = ""
                                firstEnteredPin = ""
                                isConfirmStep = false
                            }
                        }
                    }

                    PinMode.DISABLE -> {
                        if (pinManager.verifyPin(newPin)) {
                            pinManager.disablePin()
                            onSuccess()
                        } else {
                            errorMessage = NivoStrings.errorIncorrectPin(language)
                            enteredPin = ""
                        }
                    }
                }
            }
        }
    }

    fun handleBackspace() {
        if (enteredPin.isNotEmpty()) {
            enteredPin = enteredPin.dropLast(1)
            errorMessage = null
        }
    }

    val title = when (mode) {
        PinMode.AUTHENTICATE -> NivoStrings.enterPinTitle(language)
        PinMode.SETUP -> if (!isConfirmStep) NivoStrings.setPinTitle(language) else NivoStrings.confirmPinTitle(language)
        PinMode.DISABLE -> NivoStrings.disablePinTitle(language)
    }

    val subtitle = when (mode) {
        PinMode.AUTHENTICATE -> NivoStrings.enterPinSubtitle(language)
        PinMode.SETUP -> if (!isConfirmStep) NivoStrings.setPinSubtitle(language) else NivoStrings.confirmPinSubtitle(language)
        PinMode.DISABLE -> NivoStrings.disablePinSubtitle(language)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(PureBlack)
            .padding(horizontal = 24.dp, vertical = 20.dp)
            .testTag("pin_lock_screen"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize()
        ) {
            // Top Section: Cancel button if applicable
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                horizontalArrangement = Arrangement.End
            ) {
                if (onCancel != null && mode != PinMode.AUTHENTICATE) {
                    IconButton(
                        onClick = onCancel,
                        modifier = Modifier.size(48.dp).testTag("cancel_pin_button")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = NivoStrings.cancelButton(language),
                            tint = SecondaryText
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.height(48.dp))
                }
            }

            // Header: Symbol, Title, Subtitle
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                NivoRibbonSymbol(size = 64.dp)
                Spacer(modifier = Modifier.height(18.dp))
                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryText,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = subtitle,
                    fontSize = 13.sp,
                    color = SecondaryText,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(28.dp))

                // 4 PIN Dots
                Row(
                    horizontalArrangement = Arrangement.spacedBy(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (i in 0 until 4) {
                        val isFilled = i < enteredPin.length
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .clip(CircleShape)
                                .background(if (isFilled) PrimaryRed else CardElevated)
                                .border(
                                    width = 1.dp,
                                    color = if (isFilled) PrimaryRed else OutlineGrey,
                                    shape = CircleShape
                                )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Error Message
                AnimatedVisibility(visible = errorMessage != null) {
                    Text(
                        text = errorMessage ?: "",
                        color = PrimaryRed,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Numeric Keypad
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val digits = listOf(
                    listOf("1", "2", "3"),
                    listOf("4", "5", "6"),
                    listOf("7", "8", "9")
                )

                digits.forEach { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth(0.85f),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        row.forEach { digit ->
                            KeypadButton(
                                text = digit.toLocalizedDigits(language),
                                onClick = { handleDigit(digit) }
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(0.85f),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Empty spacer
                    Box(modifier = Modifier.size(70.dp))

                    // "0" Digit
                    KeypadButton(
                        text = "0".toLocalizedDigits(language),
                        onClick = { handleDigit("0") }
                    )

                    // Backspace button
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .clip(CircleShape)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = ripple(color = PrimaryText.copy(alpha = 0.2f)),
                                onClick = { handleBackspace() }
                            )
                            .testTag("pin_backspace_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Backspace,
                            contentDescription = "Backspace",
                            tint = SecondaryText,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun KeypadButton(
    text: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(70.dp)
            .clip(CircleShape)
            .background(CardBackground)
            .border(1.dp, OutlineGrey, CircleShape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = PrimaryRed.copy(alpha = 0.3f)),
                onClick = onClick
            )
            .testTag("keypad_button_$text"),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryText
        )
    }
}
