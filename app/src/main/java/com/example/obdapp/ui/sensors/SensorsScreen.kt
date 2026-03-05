package com.example.obdapp.ui.sensors

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ModernSensorsScreen(
    sensors: List<SensorData>, // SensorData(name, value: Float, unit: String, history: List<Float>)
    onRefresh: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Text(
            "Vehicle Sensors",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold),
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(sensors) { sensor ->
                SensorCardModern(sensor)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onRefresh,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
        ) {
            Text("Refresh Sensors", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun SensorCardModern(sensor: SensorData) {
    val animatedValue by animateFloatAsState(targetValue = sensor.value)

    val valueColor = when {
        animatedValue >= sensor.criticalThreshold -> Color.Red
        animatedValue >= sensor.warningThreshold -> Color(0xFFFFA000) // Amber
        else -> Color(0xFF4CAF50) // Green
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(sensor.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(sensor.unit, style = MaterialTheme.typography.bodySmall)
                }
                Text(
                    text = "%.1f".format(animatedValue),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp,
                    color = valueColor
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Mini sparkline graph for last 20 readings
            SparklineGraph(
                data = sensor.history,
                lineColor = valueColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
            )
        }
    }
}

@Composable
fun SparklineGraph(data: List<Float>, lineColor: Color, modifier: Modifier = Modifier) {
    if (data.isEmpty()) return

    Canvas(modifier = modifier) {
        val max = data.maxOrNull() ?: 1f
        val min = data.minOrNull() ?: 0f
        val points = data.mapIndexed { index, value ->
            val x = index * size.width / (data.size - 1)
            val y = size.height - ((value - min) / (max - min)) * size.height
            Offset(x, y)
        }

        for (i in 0 until points.size - 1) {
            drawLine(
                color = lineColor,
                start = points[i],
                end = points[i + 1],
                strokeWidth = 3f,
                cap = StrokeCap.Round
            )
        }
    }
}

// Example sensor data class
data class SensorData(
    val name: String,
    val value: Float,
    val unit: String,
    val history: List<Float> = emptyList(),
    val warningThreshold: Float = Float.MAX_VALUE,
    val criticalThreshold: Float = Float.MAX_VALUE
)
