package com.example.obdapp.ui.oboardingobd

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.obdapp.R
import com.example.obdapp.ui.engine.textPrimary
import com.example.obdapp.ui.theme.AppBgDark
import com.example.obdapp.ui.theme.CardBackground
val AccentBlue = Color(0xFF007AFF)
val AppBg = Color(0xFFF5F5F5) // Light gray background
val textPrimary = Color(0xFF1A1A1A)   // Dark text for titles
val textSecondary = Color(0xFF555555)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ObdOnboardingScreen(onStartClick: () -> Unit) {


    Scaffold(
        containerColor = AppBg,
        topBar = {
            TopAppBar(
                title = { Text("OBD Setup") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppBg,
                    titleContentColor = textPrimary,
                    navigationIconContentColor = textPrimary,
                    actionIconContentColor = textPrimary
                )
            )
        },
        bottomBar = {
            // Sticky button at the bottom
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(32.dp)
            ) {
                Button(
                    onClick = onStartClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AccentBlue)
                ) {
                    Text("Get Started", color = Color.White, fontSize = 16.sp)
                }
            }
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item {
                // Lottie animation
                LottieAnimation(
                    composition = rememberLottieComposition(
                        LottieCompositionSpec.RawRes(R.raw.connect_elm)
                    ).value,
                    iterations = LottieConstants.IterateForever,
                    modifier = Modifier.height(260.dp)
                )
            }

            item {
                Text(
                    text = "Connect Your OBD Device",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary
                )
            }

            item {
                Text(
                    text = "Follow these steps to start analyzing your vehicle",
                    fontSize = 14.sp,
                    color = textSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Spacer(Modifier.height(16.dp))
            }

            // Steps
            item { StepItem("Plug the OBD device into your car port") }
            item { StepItem("Enable Bluetooth and start the engine") }
            item { StepItem("Pair the OBD device from your phone settings using PIN 0000 or 1234") } // 👈 new important step
            item { StepItem("Tap Connect to view live vehicle data") }

            item { Spacer(Modifier.height(80.dp)) } // extra space so last step is visible above the button
        }
    }
}

