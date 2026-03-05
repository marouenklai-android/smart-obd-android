package com.example.obdapp.ui.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Brightness6
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.obdapp.R
import com.example.obdapp.ui.theme.AccentGreen
import com.example.obdapp.ui.theme.AppBgDark
import com.example.obdapp.ui.theme.AppCardDark


val AccentBlue = Color(0xFF007AFF)
val AppBg = Color(0xFFF5F5F5) // Light gray background
val textPrimary = Color(0xFF1A1A1A)   // Dark text for titles
val textSecondary = Color(0xFF555555) // Gray for description
@Composable
fun SettingsScreen(
    selectedUnit: String = "Metric",
    selectedLanguage: String = "English",
    onUnitClick: () -> Unit,
    onLanguageClick: () -> Unit,
    onThemeClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBg) // light gray background
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {

        // --- Header ---
        Text(
            text = "Settings",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = textPrimary
        )
        Text(
            text = "Configure your preferences",
            fontSize = 14.sp,
            color = textSecondary
        )

        Spacer(Modifier.height(24.dp))

        // --- Units Setting ---
        SettingsRow(
            icon = Icons.Default.Speed,
            title = "Units",
            description = selectedUnit,
            onClick = onUnitClick
        )

        Spacer(Modifier.height(12.dp))

        // --- Language Setting ---
        SettingsRow(
            icon = Icons.Default.Language,
            title = "Language",
            description = selectedLanguage,
            onClick = onLanguageClick
        )

        Spacer(Modifier.height(12.dp))

        // --- Theme Setting ---
        SettingsRow(
            icon = Icons.Default.Brightness6,
            title = "Theme",
            description = "Light",
            onClick = onThemeClick
        )
    }
}

@Composable
fun SettingsRow(
    icon: ImageVector,
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White) // white card background
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = AccentBlue, // accent color for icons
                modifier = Modifier.size(24.dp)
            )
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, color = textPrimary, fontWeight = FontWeight.SemiBold)
                Text(description, color = textSecondary, fontSize = 12.sp)
            }
            Icon(
                imageVector = Icons.Default.ArrowForwardIos,
                contentDescription = null,
                tint = textSecondary,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}