package com.example.obdapp.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.obdapp.ui.theme.Card

@Composable
fun UnitsScreen(
    navController: NavController,
    viewModel: UnitsViewModel = viewModel()
) {
    val selectedUnit by viewModel.unitType.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBg)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {

        // --- Header ---
        Text(
            text = "Units",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = textPrimary
        )

        Spacer(modifier = Modifier.height(24.dp))

        // --- Metric Option ---
        UnitItem(
            title = "Metric",
            description = "km/h, °C",
            selected = selectedUnit == UnitType.METRIC
        ) {
            viewModel.setUnit(UnitType.METRIC)
        }

        Spacer(modifier = Modifier.height(12.dp))

        // --- Imperial Option ---
        UnitItem(
            title = "Imperial",
            description = "mph, °F",
            selected = selectedUnit == UnitType.IMPERIAL
        ) {
            viewModel.setUnit(UnitType.IMPERIAL)
        }

        Spacer(modifier = Modifier.weight(1f))

        // --- Save Button ---
        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = AccentBlue, contentColor = Color.White),
            shape = RoundedCornerShape(14.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
        ) {
            Text("Save", fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun UnitItem(
    title: String,
    description: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) AccentBlue.copy(alpha = 0.1f) else Card
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(title, color = textPrimary, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                Text(description, color = textSecondary, fontSize = 12.sp)
            }

            if (selected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = AccentBlue,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}