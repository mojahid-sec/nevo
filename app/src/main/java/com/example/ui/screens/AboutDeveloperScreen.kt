package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.localization.NivoStrings
import com.example.ui.theme.CardBackground
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkRed
import com.example.ui.theme.OutlineGrey
import com.example.ui.theme.PrimaryRed
import com.example.ui.theme.PrimaryText
import com.example.ui.theme.PureBlack
import com.example.ui.theme.SecondaryText
import com.example.ui.viewmodel.NivoViewModel

@Composable
fun AboutDeveloperScreen(
    viewModel: NivoViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler {
        onNavigateBack()
    }

    val language by viewModel.language.collectAsState()
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 14.dp)
            .testTag("about_developer_screen")
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onNavigateBack,
                modifier = Modifier.testTag("about_dev_back_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = NivoStrings.cancelButton(language),
                    tint = PrimaryText
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = NivoStrings.aboutDeveloper(language),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryText
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Reserved square image area displaying the developer's photograph faithfully
        // ContentScale.Fit inside dark frame, completely uncropped and undistorted.
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(20.dp))
                .background(PureBlack)
                .border(1.5.dp, OutlineGrey, RoundedCornerShape(20.dp))
                .padding(8.dp)
                .testTag("developer_photo_container"),
            contentAlignment = Alignment.Center
        ) {
            val context = LocalContext.current
            val hasPhoto = remember(context) {
                context.resources.getIdentifier("developer_photo", "drawable", context.packageName) != 0
            }

            if (hasPhoto) {
                Image(
                    painter = painterResource(id = R.drawable.developer_photo),
                    contentDescription = "Developer photograph - MD. MOJAHIDUR RAHMAN SAJID",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(14.dp))
                )
            } else {
                // Polished placeholder when photo is not supplied
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(CardBackground)
                            .border(1.dp, PrimaryRed, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "MR",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryRed
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "MD. MOJAHIDUR RAHMAN SAJID",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = PrimaryText
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Developer Details Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(CardBackground)
                .border(1.dp, OutlineGrey, RoundedCornerShape(16.dp))
                .padding(18.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                DeveloperDetailRow(
                    icon = Icons.Filled.Person,
                    label = NivoStrings.yourName(language),
                    value = NivoStrings.devName(language)
                )
                DeveloperDetailRow(
                    icon = Icons.Filled.Code,
                    label = NivoStrings.devDepartmentLabel(language),
                    value = NivoStrings.devDepartmentValue(language)
                )
                DeveloperDetailRow(
                    icon = Icons.Filled.AccountBalance,
                    label = NivoStrings.devInstitutionLabel(language),
                    value = NivoStrings.devInstitutionValue(language)
                )
                DeveloperDetailRow(
                    icon = Icons.Filled.LocationOn,
                    label = NivoStrings.devLocationLabel(language),
                    value = NivoStrings.devLocationValue(language)
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Purpose of NIVO Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(CardBackground)
                .border(1.dp, OutlineGrey, RoundedCornerShape(16.dp))
                .padding(18.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(PrimaryRed)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = NivoStrings.devPurposeTitle(language),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryText
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = NivoStrings.devPurposeText(language),
                    fontSize = 13.sp,
                    color = SecondaryText,
                    lineHeight = 20.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun DeveloperDetailRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(DarkRed.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = PrimaryRed,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = label,
                fontSize = 11.sp,
                color = SecondaryText
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = PrimaryText
            )
        }
    }
}
