package com.example.obdapp.ui.language

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.obdapp.R
import com.example.obdapp.ui.theme.AccentGreen
import com.example.obdapp.ui.theme.AppBgDark
import com.example.obdapp.ui.theme.AppCardDark
import com.example.obdapp.ui.theme.BackgroundGradient
import com.example.obdapp.ui.theme.Bg
import com.example.obdapp.ui.theme.CardBackground
import com.example.obdapp.ui.theme.TextPrimaryDark
import com.example.obdapp.ui.theme.TextSecondaryDark

@Composable
fun LanguageScreen(
    viewModel: LanguageViewModel = viewModel(),
    onContinue: (Language) -> Unit
) {
    val selected = viewModel.selectedLanguage // LiveData or StateFlow

    val AccentBlue = Color(0xFF007AFF)
    val AppBg = Color(0xFFF5F5F5) // Light gray background
    val textPrimary = Color(0xFF1A1A1A)   // Dark text for titles
    val textSecondary = Color(0xFF555555) // Gray for description

    Scaffold(
        containerColor = AppBg
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppBg)
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.height(24.dp))

            LanguageLottieAnimation()

            Spacer(Modifier.height(20.dp))

            Text(
                text = "Choose your language",
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                color = textPrimary
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = "This helps us adapt the diagnostic data for you",
                fontSize = 14.sp,
                color = textSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(28.dp))

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                viewModel.languages.forEach { language ->
                    EnhancedLanguageItem(
                        language = language,
                        selected = language == selected,
                        onClick = { viewModel.selectLanguage(language) },
                        AccentBlue = AccentBlue,
                        textPrimary = textPrimary,
                        textSecondary = textSecondary,
                        AppBg = AppBg
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            Button(
                onClick = { selected?.let(onContinue) },
                enabled = selected != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AccentBlue
                )
            ) {
                Text(
                    text = "Continue",
                    fontWeight = FontWeight.SemiBold,
                    color = Color.DarkGray
                )
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
fun EnhancedLanguageItem(
    language: Language,
    selected: Boolean,
    onClick: () -> Unit,
    AccentBlue: Color,
    textPrimary: Color,
    textSecondary: Color,
    AppBg: Color
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (selected) CardBackground.copy(alpha = 0.5f) else CardBackground,
        label = ""
    )

    val indicatorWidth by animateDpAsState(
        targetValue = if (selected) 4.dp else 0.dp,
        label = ""
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (selected) 6.dp else 1.dp
        )
    ) {
        Row(
            modifier = Modifier.height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Blue selection indicator
            Box(
                modifier = Modifier
                    .width(indicatorWidth)
                    .fillMaxHeight()
                    .background(AccentBlue)
            )

            Row(
                modifier = Modifier
                    .padding(horizontal = 18.dp, vertical = 16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = language.flag,
                    fontSize = 26.sp
                )

                Spacer(Modifier.width(16.dp))

                Text(
                    text = language.name,
                    fontSize = 16.sp,
                    fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                    color = textPrimary,
                    modifier = Modifier.weight(1f)
                )

                AnimatedVisibility(
                    visible = selected,
                    enter = scaleIn() + fadeIn(),
                    exit = fadeOut()
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = AccentBlue
                    )
                }
            }
        }
    }
}


@Composable
fun LanguageLottieAnimation() {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.language)
    )

    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = Modifier.height(180.dp)
    )
}

