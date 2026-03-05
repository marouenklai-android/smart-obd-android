package com.example.obdapp.ui.dtc

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.obdapp.bluetoothmanager.BluetoothViewModel

@Composable
fun DtcScreenPro(
    onReadClick: () -> Unit,
    onClearClick: () -> Unit,
    viewModel: BluetoothViewModel = viewModel()
) {
    val dtcList by viewModel.dtcList.collectAsState()

    val AccentBlue = Color(0xFF007AFF)
    val AppBg = Color(0xFFF5F5F5)
    val textPrimary = Color(0xFF1A1A1A)
    val textSecondary = Color(0xFF555555)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBg)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        // --- Header ---
        Column {
            Text(
                text = "Diagnostic Trouble Codes",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = textPrimary
            )
            Text(
                text = "Know your vehicle’s issues in real time",
                fontSize = 14.sp,
                color = textSecondary
            )
        }

        // --- Info Card (Expandable) ---
        DtcInfoCard(AccentBlue, textPrimary, textSecondary)

        // --- Clear Button ---
        Button(
            onClick = onClearClick,
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFD32F2F),
                contentColor = Color.White
            )
        ) {
            Icon(Icons.Default.Delete, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Clear DTCs")
        }

        // --- DTC List ---
        if (dtcList.isNotEmpty()) {
            DetectedCodesHeader(dtcList.size)
            dtcList.forEach { dtc ->
                DtcCard(dtc) // your card from previous step
            }
        } else {
            NoCodesCard()
        }
    }
}
@Composable
fun NoCodesCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = Color(0xFF4CAF50),
                modifier = Modifier.size(24.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = "No codes detected",
                fontSize = 14.sp,
                color = Color(0xFF555555),
                fontWeight = FontWeight.Medium
            )
        }
    }
}
@Composable
fun DetectedCodesHeader(count: Int) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.List,
                contentDescription = null,
                tint = Color(0xFF007AFF),
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = "Detected Codes",
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = Color(0xFF007AFF)
            )
            Spacer(Modifier.width(6.dp))
            Text(
                text = "($count)",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }

        // ✅ Status phrase
        Text(
            text = "Your vehicle has $count issue${if(count > 1) "s" else ""}. Review below for details.",
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.padding(start = 28.dp, bottom = 6.dp)
        )
    }
}

@Composable
fun DtcInfoCard(accent: Color, textPrimary: Color, textSecondary: Color) {

    var expanded by remember { mutableStateOf(false) }

    val shortText =
        "Diagnostic trouble codes (DTCs) are alphanumeric codes output by your vehicle's computer when it detects a malfunction."

    val fullText = """
Diagnostic trouble codes (DTCs) are alphanumeric codes output by your vehicle's computer when it detects a malfunction.

Types:
• Stored DTC – confirmed malfunction saved in memory
• Pending DTC – detected in current drive cycle
• Permanent DTC – confirmed and cannot be erased

Clearing instructions:
1. Ignition ON, engine OFF
2. Connect scanner
3. Clear DTCs
4. Restart vehicle
""".trimIndent()

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp).animateContentSize()
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Info, contentDescription = null, tint = accent)
                Spacer(Modifier.width(8.dp))
                Text("What are DTCs?", fontWeight = FontWeight.SemiBold, color = textPrimary)
            }

            Spacer(Modifier.height(8.dp))

            Text(
                text = if (expanded) fullText else shortText,
                fontSize = 14.sp,
                color = textSecondary,
                maxLines = if (expanded) Int.MAX_VALUE else 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = if (expanded) "Read less" else "Read more",
                color = accent,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable { expanded = !expanded }
            )
        }
    }
}
@Composable
fun DtcCard(info: DtcInfo) {

    val severityColor = when (info.severity) {
        "HIGH" -> Color(0xFFD32F2F)
        "MEDIUM" -> Color(0xFFFFA000)
        "LOW" -> Color(0xFF4CAF50)
        else -> Color.Gray
    }

    val bgColor = severityColor.copy(alpha = 0.10f)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = severityColor
                )

                Spacer(Modifier.width(8.dp))

                Column {
                    Text(
                        text = info.code,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = severityColor
                    )

                    Text(
                        text = info.title,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp
                    )
                }
            }

            Spacer(Modifier.height(6.dp))

            Text(
                text = info.description,
                fontSize = 13.sp,
                color = Color.DarkGray
            )

            Spacer(Modifier.height(8.dp))

            SeverityChip(info.severity, severityColor)
        }
    }
}
@Composable
fun SeverityChip(severity: String, color: Color) {
    Box(
        modifier = Modifier
            .background(color.copy(alpha = 0.15f), RoundedCornerShape(50))
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Text(
            text = severity,
            color = color,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}