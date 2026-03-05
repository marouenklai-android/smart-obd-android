package com.example.obdapp.ui.diagnosticreport

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.obdapp.geminianalysis.model.ProDiagnosticReport
import com.example.obdapp.geminianalysis.model.RawObdData
import com.example.obdapp.geminianalysis.model.RiskAssessment
import com.example.obdapp.geminianalysis.model.SystemHealth



val AccentBlue = Color(0xFF007AFF)
val AppBg = Color(0xFFF5F5F5)
val TextPrimary = Color(0xFF1A1A1A)
val TextSecondary = Color(0xFF555555)
val CardBg = Color(0xFFFFFFFF)

@Composable
fun ProDiagnosticReportScreen(report: ProDiagnosticReport) {

    val verdictColor = when (report.verdict) {
        "NORMAL" -> Color(0xFF4CAF50)
        "WARNING" -> Color(0xFFFF9800)
        "CRITICAL" -> Color(0xFFF44336)
        else -> TextSecondary
    }

    Scaffold(
        containerColor = AppBg
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(AppBg)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // HEADER
            item {
                Column {
                    Text(
                        "PRO DIAGNOSTIC REPORT",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(Modifier.height(8.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Verdict badge
                        Box(
                            modifier = Modifier
                                .background(
                                    verdictColor.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                "Status: ${report.verdict}",
                                color = verdictColor,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }

                        // Confidence badge
                        Box(
                            modifier = Modifier
                                .background(
                                    AccentBlue.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                "Confidence: ${report.confidence}%",
                                color = AccentBlue,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }

                        // Scan Quality badge
                        Box(
                            modifier = Modifier
                                .background(AppBg, shape = RoundedCornerShape(8.dp))
                                .border(
                                    1.dp,
                                    TextSecondary.copy(alpha = 0.3f),
                                    RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                "Scan Quality: ${report.scanQuality ?: "N/A"}",
                                color = TextSecondary,
                                fontSize = 14.sp
                            )
                        }
                    }

                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Time: ${report.timestamp ?: "Unknown"}",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }
            }

            // Summary
            sectionTitle("Summary")
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(2.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBg)
                ) {
                    Text(
                        report.summary ?: "No summary available",
                        modifier = Modifier.padding(12.dp),
                        color = TextPrimary
                    )
                }
            }

            // Raw OBD Data
            sectionTitle("Raw OBD Data")
            item { RawDataExpandable(report.rawData) }

            // System Health
            sectionTitle("System Health")
            items(report.systemHealth.orEmpty().filterNotNull()) { system ->
                SystemHealthRow(system)
            }

            // Correlation Analysis
            sectionTitle("Correlation Analysis")
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(2.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBg)
                ) {
                    Text(
                        report.correlationAnalysis ?: "No correlation data",
                        modifier = Modifier.padding(12.dp),
                        color = TextPrimary
                    )
                }
            }

            // Risk Assessment
            sectionTitle("Risk Assessment")
            item { RiskAssessmentView(report.riskAssessment) }

            // Recommendations
            sectionTitle("Recommendations")
            items(report.recommendations.orEmpty()) { rec ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(2.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBg)
                ) {
                    Text("• $rec", modifier = Modifier.padding(12.dp), color = TextPrimary)
                }
            }

            // Bottom buttons
            item {
                Spacer(Modifier.height(16.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = { /* Save */ },
                        colors = ButtonDefaults.buttonColors(containerColor = AccentBlue)
                    ) { Text("Save", color = Color.White) }

                    Button(
                        onClick = { /* Export */ },
                        colors = ButtonDefaults.buttonColors(containerColor = AccentBlue)
                    ) { Text("Export PDF", color = Color.White) }
                }
            }
        }
    }
}

fun LazyListScope.sectionTitle(title: String) {
    item {
        Text(
            title,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = TextPrimary,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}

@Composable
fun RawDataExpandable(raw: RawObdData?) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = CardBg)
    ) {
        Column(Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Raw OBD Data", fontWeight = FontWeight.Bold, color = TextPrimary)
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = TextSecondary
                    )
                }
            }

            AnimatedVisibility(expanded) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Speed: ${raw?.speed}", color = TextSecondary)
                    Text("RPM: ${raw?.rpm}", color = TextSecondary)
                    Text("Coolant: ${raw?.coolantTemp}", color = TextSecondary)
                    Text("Voltage: ${raw?.voltage} ", color = TextSecondary)
                    Text("Intake Temp: ${raw?.intakeTemp}", color = TextSecondary)
                    Text("Throttle: ${raw?.throttlePosition}", color = TextSecondary)
                    Text(
                        "DTC Codes: ${raw?.dtcCodes?.ifEmpty { listOf("None") }?.joinToString()}",
                        color = TextSecondary
                    )
                }
            }
        }
    }
}
@Composable
fun SystemHealthRow(system: SystemHealth) {
    val color = when (system.status) {
        "HEALTHY", "CLEAR" -> Color(0xFF4CAF50)
        "WARNING" -> Color(0xFFFF9800)
        else -> Color(0xFFF44336)
    }

    var expanded by remember { mutableStateOf(false) }
    val icon = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = CardBg)
    ) {
        Column(Modifier.padding(12.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded } // toggle on click
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    //Icon(icon, tint = color, modifier = Modifier.size(18.dp))
                    Icon(
                        imageVector = icon,           // your vector
                        contentDescription = null,    // or a string like "System status icon"
                        tint = color,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(system.system, fontWeight = FontWeight.Bold, color = TextPrimary)
                }
                Text(system.status, color = color, fontWeight = FontWeight.Bold)
            }

            AnimatedVisibility(expanded) {
                Column(Modifier.padding(top = 8.dp)) {
                    Text(system.reason, fontSize = 13.sp, color = TextSecondary)
                    // Add more detailed info if needed
                }
            }
        }
    }
}
@Composable
fun RiskAssessmentView(risk: RiskAssessment?) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = CardBg)
    ) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("🟢 Short-term: ${risk?.shortTerm ?: "N/A"}", color = TextSecondary)
            Text("🟡 Medium-term: ${risk?.mediumTerm ?: "N/A"}", color = TextSecondary)
            Text("🔵 Long-term: ${risk?.longTerm ?: "N/A"}", color = TextSecondary)
        }
    }
}