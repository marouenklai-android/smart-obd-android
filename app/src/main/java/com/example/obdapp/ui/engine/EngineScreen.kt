package com.example.obdapp.ui.engine

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.obdapp.ui.ModernMetricCard
import com.google.android.filament.Box


@Composable
fun ModernEngineDashboard(
    rpm: Float,
    speed: Float,
    fuel: Float,
    temp: Float,
    engineLoad: Float,
    alerts: List<String>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF121212), Color(0xFF1B1B1F))
                )
            ),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // ===== Top Circular Gauges =====
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                CircularGaugeModern(
                    label = "RPM",
                    value = rpm,
                    maxValue = 8000f,
                    unit = "rpm",
                    gradientColors = listOf(Color(0xFF00FFAA), Color(0xFF00BFA5))
                )
                CircularGaugeModern(
                    label = "Speed",
                    value = speed,
                    maxValue = 240f,
                    unit = "km/h",
                    gradientColors = listOf(Color(0xFF0099FF), Color(0xFF0066FF))
                )
            }
        }

        // ===== Secondary Metrics (Vertical Cards, 2 columns) =====
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    ModernMetricCard(
                        title = "Engine Load",
                        value = engineLoad,
                        unit = "%",
                        progress = engineLoad / 100f,
                        barColor = when {
                            engineLoad < 40f -> Color(0xFF4CAF50)
                            engineLoad < 70f -> Color(0xFFFFC107)
                            else -> Color(0xFFF44336)
                        },
                        hint = "How hard the engine is working"
                    )
                    ModernMetricCard(
                        title = "Fuel",
                        value = fuel,
                        unit = "%",
                        progress = fuel / 100f,
                        barColor = Color(0xFFFFAA00),
                        hint = "Fuel remaining in the tank"
                    )
                }

                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    ModernMetricCard(
                        title = "Temp",
                        value = temp,
                        unit = "°C",
                        progress = temp / 120f,
                        barColor = Color(0xFFFF4444),
                        hint = "Engine coolant temperature"
                    )
                }
            }
        }

        // ===== Alerts =====
        if (alerts.isNotEmpty()) {
            item {
                Text(
                    "Alerts",
                    color = Color.Red,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            items(alerts) { alert ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2A2A))
                ) {
                    Text(
                        text = alert,
                        color = Color.Red,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }
    }
}


@Composable
fun EngineScreenEnhanced(
    carModel: String,
    carYear: String,
    carEngine: String,
    carImage: Painter,
    rpm: Float,
    speed: Float,
    fuel: Float,
    temp: Float,
    engineLoad: Float,
    alerts: List<String>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
//            .background(
//                Brush.verticalGradient(
//                    colors = listOf(Color(0xFF121212), Color(0xFF1B1B1F))
//                )
//            )
        ,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {

        // ===== Car Info =====
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
                elevation = CardDefaults.cardElevation(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Image(
                        painter = carImage,
                        contentDescription = "Car Image",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .border(2.dp, Color.Gray.copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = carModel,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(Modifier.width(4.dp))
                            InfoIconInline("Car model name")
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = carYear,
                                fontSize = 16.sp,
                                color = Color.LightGray
                            )
                            Spacer(Modifier.width(4.dp))
                            InfoIconInline("Year of manufacture")
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = carEngine,
                                fontSize = 16.sp,
                                color = Color.LightGray
                            )
                            Spacer(Modifier.width(4.dp))
                            InfoIconInline("Engine type / displacement")
                        }
                    }
                }
            }
        }

        // ===== Top Circular Gauges =====
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                CircularGaugeModern(
                    label = "RPM",
                    value = rpm,
                    maxValue = 8000f,
                    unit = "rpm",
                    gradientColors = listOf(Color(0xFF00FFAA), Color(0xFF00BFA5))
                )
                CircularGaugeModern(
                    label = "Speed",
                    value = speed,
                    maxValue = 240f,
                    unit = "km/h",
                    gradientColors = listOf(Color(0xFF0099FF), Color(0xFF0066FF))
                )
            }
        }

        // ===== Metrics Cards (2-column) =====
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    ModernMetricCard(
                        title = "Engine Load",
                        value = engineLoad,
                        unit = "%",
                        progress = engineLoad / 100f,
                        barColor = when {
                            engineLoad < 40f -> Color(0xFF4CAF50)
                            engineLoad < 70f -> Color(0xFFFFC107)
                            else -> Color(0xFFF44336)
                        },
                        hint = "How hard the engine is working"
                    )
                    ModernMetricCard(
                        title = "Fuel",
                        value = fuel,
                        unit = "%",
                        progress = fuel / 100f,
                        barColor = Color(0xFFFFAA00),
                        hint = "Fuel remaining in the tank"
                    )
                }

                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    ModernMetricCard(
                        title = "Temp",
                        value = temp,
                        unit = "°C",
                        progress = temp / 120f,
                        barColor = Color(0xFFFF4444),
                        hint = "Engine coolant temperature"
                    )
                }
            }
        }

        // ===== Alerts =====
        if (alerts.isNotEmpty()) {
            item {
                Text(
                    "Alerts",
                    color = Color.Red,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            items(alerts) { alert ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2A2A))
                ) {
                    Text(
                        text = alert,
                        color = Color.Red,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }
    }
}


@Composable
fun InfoIconInline(hint: String) {
    var showHint by remember { mutableStateOf(false) }

    Box {
        Text(
            text = "ⓘ",
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier
                .clickable { showHint = !showHint }
                .padding(2.dp)
        )

        if (showHint) {
            Box(
                modifier = Modifier
                    .background(Color(0xCC2A2A2A), RoundedCornerShape(8.dp))
                    .padding(8.dp)
            ) {
                Text(
                    text = hint,
                    color = Color.White,
                    fontSize = 10.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun CircularGaugeModern(
    label: String,
    value: Float,
    maxValue: Float,
    unit: String,
    gradientColors: List<Color>,
    modifier: Modifier = Modifier,
    size: Dp = 140.dp
) {
    val sweepAngle = 270f
    val startAngle = 135f

    val animatedValue by animateFloatAsState(
        targetValue = value.coerceIn(0f, maxValue),
        animationSpec = tween(600)
    )

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Background Arc
            drawArc(
                color = Color(0xFF2A2A2A),
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = size.toPx() * 0.12f, cap = StrokeCap.Round)
            )

            // Foreground Arc with gradient
            drawArc(
                brush = Brush.sweepGradient(gradientColors),
                startAngle = startAngle,
                sweepAngle = sweepAngle * (animatedValue / maxValue),
                useCenter = false,
                style = Stroke(width = size.toPx() * 0.12f, cap = StrokeCap.Round)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "${animatedValue.toInt()} $unit",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Text(
                text = label,
                color = Color.Gray,
                fontSize = 12.sp
            )
        }
    }
}


@Composable
fun EngineScreen(
    carModel: String,
    carYear: String,
    carImage: Painter,
    rpm: Float,
    speed: Float,
    fuel: Float,
    temp: Float,
    engineLoad: Float,
    alerts: List<String>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF1B1B1F), Color(0xFF121212))
                )
            ),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // ===== Top Car Info =====
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Image(
                    painter = carImage,
                    contentDescription = "Car Image",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(2.dp, Color.Gray, RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
                Column {
                    Text(
                        text = carModel,
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = carYear,
                        color = Color.LightGray,
                        fontSize = 16.sp
                    )
                }
            }
        }

        // ===== Top Gauges (Circular) =====
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                CircularGauge(
                    label = "RPM",
                    value = rpm,
                    maxValue = 8000f,
                    unit = "rpm",
                    color = Color(0xFF00FFAA)
                )
                CircularGauge(
                    label = "Speed",
                    value = speed,
                    maxValue = 240f,
                    unit = "km/h",
                    color = Color(0xFF0099FF)
                )
            }
        }

        // ===== Metrics Cards (2 columns layout) =====
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    ModernMetricCard(
                        title = "RPM",
                        value = rpm,
                        unit = "rpm",
                        progress = rpm / 8000f,
                        barColor = Color(0xFF00FFAA),
                        hint = "Engine speed. Higher RPM means the engine is spinning faster."
                    )
                    ModernMetricCard(
                        title = "Fuel",
                        value = fuel,
                        unit = "%",
                        progress = fuel / 100f,
                        barColor = Color(0xFFFFAA00),
                        hint = "Fuel remaining in the tank."
                    )
                }

                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    ModernMetricCard(
                        title = "Engine Load",
                        value = engineLoad,
                        unit = "%",
                        progress = engineLoad / 100f,
                        barColor = when {
                            engineLoad < 40f -> Color(0xFF4CAF50)
                            engineLoad < 70f -> Color(0xFFFFC107)
                            else -> Color(0xFFF44336)
                        },
                        hint = "How hard the engine is working. High load at low RPM means heavy stress."
                    )
                    ModernMetricCard(
                        title = "Temp",
                        value = temp,
                        unit = "°C",
                        progress = temp / 120f, // assuming max temp 120°C
                        barColor = Color(0xFFFF4444),
                        hint = "Engine coolant temperature."
                    )
                }
            }
        }

        // ===== Alerts =====
        if (alerts.isNotEmpty()) {
            item {
                Text(
                    "Alerts",
                    color = Color.Red,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            items(alerts) { alert ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2A2A))
                ) {
                    Text(
                        text = alert,
                        color = Color.Red,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }
    }
}

//@Composable
//fun EngineScreen(
//    carModel: String,
//    carYear: String,
//    carImage: Painter,
//    rpm: Float,
//    speed: Float,
//    fuel: Float,
//    temp: Float,
//    alerts: List<String>
//) {
//    LazyColumn(
//        modifier = Modifier
//            .fillMaxSize()
////            .background(
////                Brush.verticalGradient(
////                    listOf(Color(0xFF1B1B1F), Color(0xFF121212))
////                )
////            )
//        ,
//        contentPadding = PaddingValues(16.dp),
//        verticalArrangement = Arrangement.spacedBy(16.dp)
//    ) {
//
//        // ===== Top Car Info =====
//        item {
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.spacedBy(16.dp)
//            ) {
//                Image(
//                    painter = carImage,
//                    contentDescription = "Car Image",
//                    modifier = Modifier
//                        .size(100.dp)
//                        .clip(RoundedCornerShape(12.dp))
//                        .border(2.dp, Color.Gray, RoundedCornerShape(12.dp)),
//                    contentScale = ContentScale.Crop
//                )
//                Column {
//                    Text(
//                        text = carModel,
//                        color = Color.White,
//                        fontSize = 20.sp,
//                        fontWeight = FontWeight.Bold
//                    )
//                    Text(
//                        text = carYear,
//                        color = Color.LightGray,
//                        fontSize = 16.sp
//                    )
//                }
//            }
//        }
//
//        // ===== Gauges =====
//        item {
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceEvenly
//            ) {
//                CircularGauge(
//                    label = "RPM",
//                    value = rpm,
//                    maxValue = 8000f,
//                    unit = "rpm",
//                    color = Color(0xFF00FFAA)
//                )
//                CircularGauge(
//                    label = "Speed",
//                    value = speed,
//                    maxValue = 240f,
//                    unit = "km/h",
//                    color = Color(0xFF0099FF)
//                )
//            }
//        }
//
//        // ===== Fuel & Temp =====
//        item {
//            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
//                LinearGauge(
//                    label = "Fuel",
//                    value = fuel,
//                    color = Color(0xFFFFAA00)
//                )
//                LinearGauge(
//                    label = "Temp",
//                    value = temp,
//                    color = Color(0xFFFF4444)
//                )
//                LinearGauge(
//                    label = "Fuel",
//                    value = fuel,
//                    color = Color(0xFFFFAA00)
//                )
//                LinearGauge(
//                    label = "Temp",
//                    value = temp,
//                    color = Color(0xFFFF4444)
//                )
//                LinearGauge(
//                    label = "Fuel",
//                    value = fuel,
//                    color = Color(0xFFFFAA00)
//                )
//                LinearGauge(
//                    label = "Temp",
//                    value = temp,
//                    color = Color(0xFFFF4444)
//                )
//            }
//        }
//
//        // ===== Alerts =====
//        if (alerts.isNotEmpty()) {
//            item {
//                Text(
//                    "Alerts",
//                    color = Color.Red,
//                    fontWeight = FontWeight.Bold,
//                    fontSize = 18.sp
//                )
//            }
//
//            items(alerts) { alert ->
//                Card(
//                    modifier = Modifier.fillMaxWidth(),
//                    shape = RoundedCornerShape(8.dp)
//                ) {
//                    Text(
//                        text = alert,
//                        color = Color.Red,
//                        modifier = Modifier.padding(12.dp)
//                    )
//                }
//            }
//        }
//    }
//}


//@Composable
//fun EngineScreen(
//    carModel: String,
//    carYear: String,
//    carImage: Painter,
//    rpm: Float,
//    speed: Float,
//    fuel: Float,
//    temp: Float,
//    alerts: List<String>
//) {
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Brush.verticalGradient(listOf(Color(0xFF1B1B1F), Color(0xFF121212))))
//            .padding(16.dp),
//        verticalArrangement = Arrangement.spacedBy(16.dp)
//    )
//    {
//        // ===== Top Car Info =====
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.spacedBy(16.dp)
//        ) {
//            Image(
//                painter = carImage,
//                contentDescription = "Car Image",
//                modifier = Modifier
//                    .size(100.dp)
//                    .clip(RoundedCornerShape(12.dp))
//                    .border(2.dp, Color.Gray, RoundedCornerShape(12.dp)),
//                contentScale = ContentScale.Crop
//            )
//            Column {
//                Text(text = carModel, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
//                Text(text = carYear, color = Color.LightGray, fontSize = 16.sp)
//            }
//        }
//
//        // ===== Gauges =====
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceEvenly
//        ) {
//            CircularGauge(
//                label = "RPM",
//                value = rpm,
//                maxValue = 8000f,
//                unit = "rpm",
//                color = Color(0xFF00FFAA)
//            )
//            CircularGauge(
//                label = "Speed",
//                value = speed,
//                maxValue = 240f,
//                unit = "km/h",
//                color = Color(0xFF0099FF)
//            )
//        }
//
////        // ===== Fuel & Temperature Bars =====
////        Column (
////            verticalArrangement = Arrangement.spacedBy(0.dp)
////        ) {
////            //LinearGauge(label = "Fuel", value = fuel, color = Color(0xFFFFAA00))
////            LinearGauge(label = "Temp", value = temp, color = Color(0xFFFF4444),modifier = Modifier.weight(1f))
////            //LinearGauge(label = "Fuel", value = fuel, color = Color(0xFFFFAA00),modifier = Modifier.weight(1f))
////
////        }
//
//        // ===== Alerts Section =====
//        if (alerts.isNotEmpty()) {
//            Column {
//                Text("Alerts", color = Color.Red, fontWeight = FontWeight.Bold, fontSize = 18.sp)
//                alerts.forEach { alert ->
//                    Card(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(vertical = 4.dp),
//                        //backgroundColor = Color(0xFF2A2A2E),
//                        shape = RoundedCornerShape(8.dp)
//                    ) {
//                        Text(
//                            text = alert,
//                            color = Color.Red,
//                            modifier = Modifier.padding(12.dp)
//                        )
//                    }
//                }
//            }
//        }
//        Column(
//            verticalArrangement = Arrangement.spacedBy(0.dp)
//        ) {
//            LinearGauge(label = "Fuel", value = fuel, color = Color(0xFFFFAA00))
//            LinearGauge(
//                label = "Temp",
//                value = temp,
//                color = Color(0xFFFF4444),
//                modifier = Modifier.weight(1f)
//            )
////            //LinearGauge(label = "Fuel", value = fuel, color = Color(0xFFFFAA00),modifier = Modifier.weight(1f))
////
//        }
//    }
//}

// ===== Gauge Composables =====
@Composable
fun CircularGauge(label: String, value: Float, maxValue: Float, unit: String, color: Color) {
    val animatedValue by animateFloatAsState(targetValue = value)
    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(140.dp)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawArc(
                color = Color.DarkGray,
                startAngle = 135f,
                sweepAngle = 270f,
                useCenter = false,
                style = Stroke(width = 12f, cap = StrokeCap.Round)
            )
            drawArc(
                color = color,
                startAngle = 135f,
                sweepAngle = 270f * (animatedValue / maxValue),
                useCenter = false,
                style = Stroke(width = 12f, cap = StrokeCap.Round)
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("${animatedValue.toInt()}", color = Color.White, fontWeight = FontWeight.Bold)
            Text(unit, color = Color.LightGray, fontSize = 12.sp)
            Text(label, color = Color.LightGray, fontSize = 14.sp)
        }
    }
}
@Composable
fun LinearGauge(
    label: String,
    value: Float,
    color: Color,
    modifier: Modifier = Modifier
) {
    val animatedValue by animateFloatAsState(
        targetValue = value.coerceIn(0f, 100f),
        label = "LinearGaugeAnimation"
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = label,
            color = Color.LightGray,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        LinearProgressIndicator(
            progress = { animatedValue / 100f },
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .clip(RoundedCornerShape(6.dp)),
            color = color,
            trackColor = Color.DarkGray,
            strokeCap = StrokeCap.Butt
        )
    }
}
