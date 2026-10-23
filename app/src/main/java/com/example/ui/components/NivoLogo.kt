package com.example.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.localization.AppLanguage
import com.example.data.localization.NivoStrings
import com.example.ui.theme.PrimaryText
import com.example.ui.theme.SecondaryText

@Composable
fun NivoLogo(
    modifier: Modifier = Modifier,
    iconSize: Dp = 68.dp,
    showTagline: Boolean = true,
    language: AppLanguage = AppLanguage.ENGLISH
) {
    Column(
        modifier = modifier
            .padding(vertical = 12.dp)
            .testTag("nivo_brand_logo"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 1. Stylized Red Ribbon N Symbol Centered at Top
        NivoRibbonSymbol(size = iconSize)

        Spacer(modifier = Modifier.height(10.dp))

        // 2. NIVO Wordmark
        Text(
            text = "N I V O",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryText,
            letterSpacing = 4.sp,
            textAlign = TextAlign.Center
        )

        // 3. Tagline: "One Day at a Time."
        if (showTagline) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = NivoStrings.appTagline(language),
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal,
                color = SecondaryText,
                textAlign = TextAlign.Center
            )
        }
    }
}
