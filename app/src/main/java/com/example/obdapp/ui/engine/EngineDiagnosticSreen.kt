package com.example.obdapp.ui.engine

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.obdapp.bluetoothmanager.BluetoothViewModel

val AccentBlue = Color(0xFF007AFF)
val AppBg = Color(0xFFF5F5F5)
val textPrimary = Color(0xFF1A1A1A)
val textSecondary = Color(0xFF555555)
val Card = Color(0xFFFFFFFF)
// --- Engine Diagnostics Screen ---
@Composable
fun EngineDiagnosticsScreenLight(viewModel: BluetoothViewModel = viewModel()) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBg)
            .padding(16.dp)
    ) {
        // Make the content scrollable
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()) // <-- scrollable
        ) {
            EngineScreenHeaderLight()
            EngineChartSectionLight(viewModel)
            EngineSensorsSectionLight(viewModel)
            EngineStatsSectionLight()
        }
    }
}
@Composable
fun EngineStatsSectionLight() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Card, RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        StatRowLight("Torque", "650 Nm")
        StatRowLight("Power", "405 kW")
        StatRowLight("Coolant Temperature", "205 °F")
        StatRowLight("Oil Pressure", "28 psi")
        StatRowLight("Top Speed", "188 mph")
    }
}

@Composable
fun StatRowLight(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = textPrimary)
        Text(value, fontWeight = FontWeight.Medium, color = AccentBlue)
    }
    Spacer(Modifier.height(8.dp))
}
@Composable
fun EngineChartSectionLight(viewModel: BluetoothViewModel = viewModel()) {
    val speed by viewModel.speedNoUnit.collectAsState()
    val rpm by viewModel.rpmNoUnit.collectAsState()

    Column(modifier = Modifier.padding(10.dp)) {
        if (speed != "--" && rpm != "--") {
            EngineChartProLivePolishedLight(speed = speed.toInt().toFloat(), rpm = rpm.toInt().toFloat())
        }
    }
}

@Composable
fun EngineSensorsSectionLight(viewModel: BluetoothViewModel = viewModel()) {

    val engineTemp by viewModel.engineTempNoUnit.collectAsState()
    val voltage by viewModel.voltageNoUnit.collectAsState()
    val intakeTemp by viewModel.intakeTemUnit.collectAsState()
    val throttle by viewModel.throttlePositionNoUnit.collectAsState()
    println("hello test engineTemp${engineTemp} voltage${voltage} intakeTemp${intakeTemp} throttle${throttle}")

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

        if (engineTemp != "--") {
            SingleSensorGraph(
                title = "Engine Temperature",
                value = engineTemp.toFloat(),
                maxValue = 120f,
                color = Color.Red,
                unit = "°C"
            )
        }
//
//        if (voltage != "--") {
//            SingleSensorGraph(
//                title = "Battery Voltage",
//                value = voltage.toFloat(),
//                maxValue = 16f,
//                color = AccentBlue,
//                unit = "V"
//            )
//        }

        if (intakeTemp != "--") {
            SingleSensorGraph(
                title = "Intake Air Temperature",
                value = intakeTemp.toFloat(),
                maxValue = 100f,
                color = Color(0xFFFF9500),
                unit = "°C"
            )
        }

        if (throttle != "--") {
            SingleSensorGraph(
                title = "Throttle Position",
                value = throttle.toFloat(),
                maxValue = 100f,
                color = Color(0xFF4CAF50),
                unit = "%"
            )
        }
    }
}
@Composable
fun SingleSensorGraph(
    title: String,
    value: Float,
    maxValue: Float,
    color: Color,
    unit: String,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(180.dp)
) {
    val maxPoints = 8
    val history = remember { mutableStateListOf<Float>() }

    LaunchedEffect(value) {
        if (history.size >= maxPoints) history.removeAt(0)
        history.add(value)
    }

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Card),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Box(Modifier.fillMaxSize().padding(16.dp)) {

            Text(title, fontWeight = FontWeight.SemiBold, color = textPrimary)

            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 28.dp)
            ) {
                if (history.size < 2) return@Canvas
                val stepX = size.width / (maxPoints - 1)

                fun mapY(v: Float) =
                    size.height - (v / maxValue) * size.height

                val path = Path()
                history.forEachIndexed { i, v ->
                    val x = i * stepX
                    val y = mapY(v)
                    if (i == 0) path.moveTo(x, y)
                    else path.lineTo(x, y)
                }

                drawPath(
                    path = path,
                    color = color,
                    style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
                )
                //drawPath(path, color, Stroke(4.dp.toPx(), cap = StrokeCap.Round))
            }

            Text(
                "$value $unit",
                modifier = Modifier.align(Alignment.BottomEnd),
                color = color,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
@Composable
fun EngineChartProLivePolishedLight(
    speed: Float,
    rpm: Float,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(280.dp)
) {
    val maxPoints = 8
    val speedHistory = remember { mutableStateListOf<Float>() }
    val rpmHistory = remember { mutableStateListOf<Float>() }

    LaunchedEffect(speed) {
        if (speedHistory.size >= maxPoints) speedHistory.removeAt(0)
        if (rpmHistory.size >= maxPoints) rpmHistory.removeAt(0)
        speedHistory.add(speed)
        rpmHistory.add(rpm)
    }

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Card),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {

            // --- Title ---
            Text(
                "Engine Performance",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = textPrimary,
                modifier = Modifier.align(Alignment.TopStart)
            )

            Canvas(modifier = Modifier
                .fillMaxSize()
                .padding(top = 32.dp, start = 48.dp, end = 16.dp) // right padding reduced
            ) {
                if (speedHistory.size < 2) return@Canvas
                val stepX = (size.width - 48.dp.toPx() - 16.dp.toPx()) / (maxPoints - 1)
                val speedMax = 240f
                val rpmMax = 8000f

                fun mapY(v: Float, max: Float) = size.height - (v / max) * size.height

                val labelPaint = android.graphics.Paint().apply {
                    color = android.graphics.Color.DKGRAY
                    textSize = 28f
                    textAlign = android.graphics.Paint.Align.RIGHT
                }

                val ySteps = 5
                repeat(ySteps + 1) { i ->
                    val ySpeed = mapY(speedMax / ySteps * i, speedMax)
                    val yRpm = mapY(rpmMax / ySteps * i, rpmMax)

                    // --- Left Y-axis: speed (normal order) ---
                    drawContext.canvas.nativeCanvas.drawText(
                        "${(speedMax / ySteps * i).toInt()} km/h",
                        -8f,
                        ySpeed,
                        labelPaint
                    )

                    // --- Right Y-axis: RPM (normal order) ---
                    drawContext.canvas.nativeCanvas.drawText(
                        "${(rpmMax / ySteps * i).toInt()} RPM",
                        size.width - 8f,
                        yRpm,
                        labelPaint
                    )

                    // Optional horizontal grid
                    drawLine(Color(0x22007AFF), Offset(0f, ySpeed), Offset(size.width, ySpeed), 1.dp.toPx())
                    drawLine(Color(0x22FF9500), Offset(0f, yRpm), Offset(size.width, yRpm), 1.dp.toPx())
                }

                fun buildPath(data: List<Float>, mapper: (Float) -> Float): Path {
                    val path = Path()
                    data.forEachIndexed { i, v ->
                        val x = i * stepX
                        val y = mapper(v)
                        if (i == 0) path.moveTo(x, y)
                        else {
                            val prevX = (i - 1) * stepX
                            val prevY = mapper(data[i - 1])
                            val cx = (prevX + x) / 2
                            path.cubicTo(cx, prevY, cx, y, x, y)
                        }
                    }
                    return path
                }

                // --- Draw curves ---
                drawPath(buildPath(speedHistory) { mapY(it, speedMax) }, color = AccentBlue, style = Stroke(4.dp.toPx(), cap = StrokeCap.Round))
                drawPath(buildPath(rpmHistory) { mapY(it, rpmMax) }, color = Color(0xFFFF9500), style = Stroke(4.dp.toPx(), cap = StrokeCap.Round))
            }

            // --- Legends at bottom with padding ---
            Spacer(Modifier.height(24.dp))
            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp) // add spacing between legend dots
            ) {
                LegendDot(AccentBlue, "Speed (km/h)")
                LegendDot(Color(0xFFFF9500), "RPM")
            }
        }
    }
}

@Composable
fun LegendDot(color: Color, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(end = 8.dp) // add right padding around each legend
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(color, CircleShape)
        )
        Spacer(Modifier.width(6.dp))
        Text(text, color = textPrimary, fontSize = 12.sp)
    }
}

@Composable
fun EngineScreenHeaderLight() {
    Column {
        Text("Engine", color = textPrimary, fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
        Text("Diagnostics", color = textSecondary, fontSize = 14.sp)
        Spacer(Modifier.height(8.dp))
        Text("Full load diagram F82 with S55 engine", color = textSecondary, fontSize = 12.sp)
    }
}
