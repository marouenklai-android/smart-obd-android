package com.example.obdapp.ui.engine

import android.app.Application
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BatteryFull
import androidx.compose.material.icons.filled.BluetoothSearching
import androidx.compose.material.icons.filled.DeviceThermostat
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlin.math.cos
import kotlin.math.sin
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.obdapp.R
import com.example.obdapp.bluetoothmanager.BluetoothConnectionState
import com.example.obdapp.bluetoothmanager.BluetoothViewModel
import com.example.obdapp.geminianalysis.ObdData
import com.example.obdapp.geminianalysis.buildPromptLLMReport
import com.example.obdapp.geminianalysis.viewmodel.LLMViewModel
import com.example.obdapp.ui.navigation.Route
import com.example.obdapp.ui.theme.AppBgDark
import com.example.obdapp.ui.theme.Bg
import com.example.obdapp.ui.theme.Card
import com.example.obdapp.ui.theme.LabelColor
import com.example.obdapp.ui.theme.Rpm
import kotlin.String


@Composable
fun CarDashboardScreen(navController: NavController,
                       viewModel: BluetoothViewModel = viewModel(),
                       onUnitClick: () -> Unit?,
                       viewModelChat: LLMViewModel = hiltViewModel(),
                       onDiagnosticClick: () -> Unit?,
                       ) {

    LaunchedEffect(Unit) {
        viewModelChat.navigateToReport.collect {
            navController.navigate("report")
        }
    }
    Box(
        modifier = Modifier
            .background(color = Bg)

    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState()) // 👈 make scrollable

        ) {

            val connectionState by viewModel.connectionState.collectAsState()


            DashboardScreenHeader()
            when(connectionState){
                BluetoothConnectionState.Connected -> CarInfoHeader(
                    carName = "Car",
                        //"KIA Rio",
                    year =  "",
                    vin = "KMHDU46D09U123456",
                    //isConnected = true,
                    connectionState = ConnectionState.CONNECTED,
                    suggestion = "Welcome! Vehicle connected successfully",
                    onConnectClick = { navController.navigate(Route.OnboardingObdScreen.name) },
                    onDisconnectClick = { viewModel.disconnect() }
                )
                else -> EmptyConnectionState {
                    onUnitClick.invoke()
                }

            }


            val result by viewModelChat.response.collectAsState()

            Spacer(Modifier.height(24.dp))

            DiagnosticButton{
                onDiagnosticClick.invoke()
                val result = buildPromptLLMReport(
                    ObdData(
                        viewModel.speedNoUnit.value,
                        viewModel.rpmNoUnit.value,
                        viewModel.engineTempNoUnit.value,
                        voltage = viewModel.voltage.value,
                        intakeTemp = viewModel.intakeTem.value,
                        throttlePosition = viewModel.throttlePosition.value,
                        dtcCodes = viewModel.dtcList.value
                    )
                )
                println("result: $result")
                viewModelChat.sendPrompt(result)
                viewModelChat.triggerDiagnosis()



            }
            //DiagnosticResultCard(result = result)
            Spacer(Modifier.height(24.dp))
            IndicatorsRow(viewModel)

            CarLottieAnimation(modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                    .offset(y = -20.dp))
            StatusGrid(viewModel=viewModel)
            Spacer(Modifier.height(24.dp))

        }

    }
}
@Composable
fun ModeToggle(isExpertMode: Boolean, onModeChange: (Boolean) -> Unit) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center
    ) {

        Button(
            onClick = { onModeChange(false) },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (!isExpertMode) Color(0xFF4CAF50) else Color.LightGray
            )
        ) {
            Text("Simple Mode")
        }

        Spacer(modifier = Modifier.width(12.dp))

        Button(
            onClick = { onModeChange(true) },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isExpertMode) Color(0xFF2196F3) else Color.LightGray
            )
        ) {
            Text("Expert Mode")
        }
    }
}

@Composable
fun DiagnosticResultCard(result: String) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Settings,
                        //Icons.Default.Dashboard,
                    contentDescription = null,
                    tint = Color(0xFF007AFF),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "AI Diagnostic Result",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = Color(0xFF007AFF)
                )
            }
            Spacer(Modifier.height(8.dp))

            // The result content
            Text(
                text = result,
                fontSize = 14.sp,
                color = Color(0xFF1A1A1A),
                lineHeight = 20.sp
            )
        }
    }
}


@Composable
fun SmartDiagnosisOverlay(onDismiss: () -> Unit) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.95f))
            .zIndex(10f), // higher z-index to cover bottom bar
        contentAlignment = Alignment.Center
    ) {

        // Fill the Box height to center the Column properly
        Column(
            modifier = Modifier
                .fillMaxHeight()  // fill parent vertically
                .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.Center, // center vertically
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val composition by rememberLottieComposition(
                LottieCompositionSpec.RawRes(R.raw.sparkles_loop_loader_ai)
            )

            LottieAnimation(
                composition = composition,
                iterations = LottieConstants.IterateForever,
                modifier = Modifier.size(220.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Running Smart Diagnosis...",
                color = AppBg,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Analyzing vehicle data",
                color = AppBg.copy(alpha = 0.8f),
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppBg,
                    contentColor = AccentBlue
                )
            ) {
                Text("Cancel")
            }
        }
    }
}
@Composable
fun EmptyConnectionState(onConnectClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.BluetoothSearching,
            contentDescription = null,
            tint = Color(0xFFB0BEC5),
            modifier = Modifier.size(96.dp)
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = "No vehicle connected",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF212121)
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Connect your OBD device to see live vehicle data",
            fontSize = 14.sp,
            color = Color(0xFF666666),
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = onConnectClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007AFF)),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text("Connect", color = Color.White)
        }
    }
}

// --- Header Section ---
@Composable
fun DashboardScreenHeader() {
    Column {
        Text(
            "Dashboard",
            color = Color(0xFF212121),
                //AppBgDark,
                //Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            "Smart vehicle insights",
            color = LabelColor,
            fontSize = 14.sp
        )
        Spacer(Modifier.height(8.dp))

    }
}
@Composable
fun CarInfoHeader(
    carName: String,
    year: String,
    vin: String,
    connectionState: ConnectionState,
    suggestion: String,
    onConnectClick: () -> Unit,
    onDisconnectClick: () -> Unit
) {
    val (statusText, statusColor) = when (connectionState) {
        ConnectionState.CONNECTED -> "Connected" to Color(0xFF00C853)
        ConnectionState.CONNECTING -> "Connecting..." to Color(0xFFFFC107)
        ConnectionState.ERROR -> "Error" to Color(0xFFF44336)
        ConnectionState.DISCONNECTED -> "Disconnected" to Color(0xFFB0BEC5)
    }

    val buttonText = when (connectionState) {
        ConnectionState.CONNECTED -> "Disconnect"
        ConnectionState.CONNECTING -> "Connecting..."
        else -> "Connect"
    }

    val buttonColor = when (connectionState) {
        ConnectionState.CONNECTED -> Color(0xFFE0E0E0)
        ConnectionState.CONNECTING -> Color(0xFFFFC107)
        ConnectionState.ERROR -> Color(0xFFF44336)
        ConnectionState.DISCONNECTED -> Color(0xFF007AFF)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // ───── Top Row: Car info + Status chip ─────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.DirectionsCar,
                            contentDescription = "Car",
                            tint = AccentBlue,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = "CAR",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF212121)
                        )
                    }

                    Text(
                        text = "VIN: $vin",
                        fontSize = 12.sp,
                        color = Color(0xFF666666)
                    )
                }

                StatusChip(text = statusText, color = statusColor)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Divider(color = Color(0xFFE0E0E0))

            Spacer(modifier = Modifier.height(12.dp))

            // ───── Info row (suggestion) ─────
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = statusColor
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = suggestion,
                    fontSize = 14.sp,
                    color = Color(0xFF666666)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ───── Connect / Disconnect Button ─────
            Button(
                onClick = {
                    if (connectionState == ConnectionState.CONNECTED) {
                        onDisconnectClick()
                    } else {
                        onConnectClick()
                    }
                },
                enabled = connectionState != ConnectionState.CONNECTING,
                colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (connectionState == ConnectionState.CONNECTING) {
                    CircularProgressIndicator(
                        color = Color.Black,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                }
                Text(
                    text = buttonText,
                    color = if (connectionState == ConnectionState.CONNECTED) Color(0xFF212121) else Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
@Composable
fun StatusChip(text: String, color: Color) {
    Box(
        modifier = Modifier
            .background(color.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            color = color,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
enum class ConnectionState {
    DISCONNECTED,
    CONNECTING,
    CONNECTED,
    ERROR
}




@Composable
fun ProNeedleGauge(
    title: String,
    value: Int,
    unit: String,
    maxValue: Int,
    modifier: Modifier = Modifier.size(120.dp)
) {
    val safeValue = value.coerceIn(0, maxValue)

    val angle by animateFloatAsState(
        targetValue = (safeValue / maxValue.toFloat()) * 270f - 135f,
        animationSpec = tween(700, easing = FastOutSlowInEasing),
        label = "needleAnim"
    )

    //Column(horizontalAlignment = Alignment.CenterHorizontally) {
        //Text(title, color = Color.White, fontSize = 14.sp)

        Box(contentAlignment = Alignment.Center, modifier = modifier) {

            Canvas(modifier = Modifier.fillMaxSize()) {
                val center = size.center
                val radius = size.minDimension / 2.2f

                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF0E2A1F),
                            Color(0xFF081611)
                        )
                    ),
                    radius = radius + 20,
                    center = center
                )
                drawCircle(
                    color = Color.White.copy(alpha = 0.05f),
                    radius = radius + 20,
                    style = Stroke(2f)
                )

                // ===== Color zones arc =====
//                drawArc(
//                    color = Color(0xFF4CAF50),
//                    startAngle = 135f,
//                    sweepAngle = 180f,
//                    useCenter = false,
//                    style = Stroke(12f, cap = StrokeCap.Round)
//                )
//                drawArc(
//                    color = Color(0xFFFFC107),
//                    startAngle = 315f,
//                    sweepAngle = 45f,
//                    useCenter = false,
//                    style = Stroke(12f, cap = StrokeCap.Round)
//                )
//                drawArc(
//                    color = Color(0xFFF44336),
//                    startAngle = 360f,
//                    sweepAngle = 45f,
//                    useCenter = false,
//                    style = Stroke(12f, cap = StrokeCap.Round)
//                )

                // ===== Tick marks =====
                for (i in 0..5) {
                    val valueLabel = (i * maxValue / 5).toString()
                    val labelAngle = Math.toRadians((135 + i * 54).toDouble())
                    val labelRadius = radius - 30

                    val x = center.x + cos(labelAngle).toFloat() * labelRadius
                    val y = center.y + sin(labelAngle).toFloat() * labelRadius

                    drawContext.canvas.nativeCanvas.drawText(
                        valueLabel,
                        x,
                        y,
                        android.graphics.Paint().apply {
                            color = android.graphics.Color.GRAY
                            textSize = 28f
                            textAlign = android.graphics.Paint.Align.CENTER
                        }
                    )
                }


                // ===== Needle glow =====
                rotate(angle, pivot = center) {
                    drawLine(
                        color = Color.Red.copy(alpha = 0.3f),
                        start = center,
                        end = Offset(center.x, center.y - radius),
                        strokeWidth = 12f,
                        cap = StrokeCap.Round
                    )

                    // Main needle
                    drawLine(
                        color = Color.Red,
                        start = center,
                        end = Offset(center.x, center.y - radius),
                        strokeWidth = 6f,
                        cap = StrokeCap.Round
                    )
                }

                // ===== Center circle =====
                drawCircle(
                    color = Color.White,
                    radius = 10f,
                    center = center
                )
            }

            // ===== Value text =====
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "$safeValue",
                    color = Color.White,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(unit, color = Color.Gray, fontSize = 12.sp)
            }
        }
    //}
}



@Composable
fun IndicatorsRow(viewModel: BluetoothViewModel = viewModel()) {



        val speedNoUnit by viewModel.speedNoUnit.collectAsState()
        val rpmNoUnit by viewModel.rpmNoUnit.collectAsState()

    val rpm by viewModel.rpm.collectAsState()
        val engineTemp by viewModel.engineTemp.collectAsState()
        var progress: Float = 0f

        if(speedNoUnit != "--")
            progress =speedNoUnit.toInt()/ 240f

        if(speedNoUnit != "--" && rpmNoUnit != "--") {


            CarDashboardTopModern(speedNoUnit.toInt(),rpmNoUnit.toFloat(),50f)

        }else{CarDashboardTopModern(0.toInt(),0.toFloat(),50f)}





}



@Composable
fun CarDashboardTopModern(
    speed: Int,
    rpm: Float,
    fuelPercent: Float
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(horizontal = 16.dp), // 👈 same start & end padding
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

//        ProNeedleGauge(
//            "Speed",
//            speed,
//            "km/h",
//            240,
//            modifier = Modifier.weight(1f)
//        )
        SpeedGaugePro(
            speed = speed.toFloat(),
            modifier = Modifier
                .weight(1f)
                .fillMaxSize())

//        RpmGaugePro(
//            rpm = rpm,
//            modifier = Modifier
//                .weight(1f).fillMaxSize()
//
//            //.height(200.dp)
//        )

        RpmGaugePro(
            rpm = rpm,
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()

                //.height(200.dp)
        )
    }

//    Card(
//        modifier = Modifier.fillMaxWidth().height(200.dp),
//        shape = RoundedCornerShape(24.dp),
//        colors = CardDefaults.cardColors(
//            containerColor = Color.Transparent
//        )
//    ) {
//        Column(
//            modifier = Modifier.padding(bottom = 0.dp),
//            verticalArrangement = Arrangement.spacedBy(0.dp) // NO spacing
//
//        ) {
//
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(150.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                //Box(modifier = Modifier.height(120.dp)) {
//                    ProNeedleGauge("Speed", speed, "km/h", 240)
//
//                //}
//
//
//                RpmGaugePro(
//                        rpm = rpm,
//                        modifier = Modifier
//                            .height(120.dp)
//                            .fillMaxWidth()
//                    )
//
//
//            }
////            CarLottieAnimation(
////                modifier = Modifier
////                    .fillMaxWidth()
////                    .height(160.dp)
////                    //.offset(y = (-90).dp)
////                    .offset(y = -90.dp)
//                // 👈 control how much space it takes
//                // 👈 removes top gap visually
//            //)
//
//
//        }
//    }
}

@Composable
fun CarLottieAnimation(modifier: Modifier = Modifier) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(com.example.obdapp.R.raw.green_moving_car)
    )

    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = modifier
    )
}


@Composable
fun RpmGaugePro(
    rpm: Float,
    maxRpm: Float = 8000f,
    redLine: Float = 6500f,
    modifier: Modifier = Modifier
        .size(260.dp)
        .fillMaxSize()
) {
    val animatedRpm by animateFloatAsState(
        targetValue = rpm.coerceIn(0f, maxRpm),
        animationSpec = tween(700, easing = FastOutSlowInEasing)
    )

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = modifier) {

            val radius = size.minDimension / 2
            val center = Offset(size.width / 2, size.height / 2)

            val startAngle = 135f
            val sweepAngle = 270f

            val strokeWidth = 14.dp.toPx()

            // Background arc
            drawArc(
                color = AppBgDark,
                    //Color(0xFF0E2A1F),
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(strokeWidth, cap = StrokeCap.Round),
                size = Size(radius * 2, radius * 2),
                topLeft = Offset(center.x - radius, center.y - radius)
            )

            // Redline zone
            val redStart = startAngle + (redLine / maxRpm) * sweepAngle
            val redSweep = sweepAngle - (redLine / maxRpm) * sweepAngle

            drawArc(
                color = Color.Red,
                startAngle = redStart,
                sweepAngle = redSweep,
                useCenter = false,
                style = Stroke(strokeWidth, cap = StrokeCap.Round),
                size = Size(radius * 2, radius * 2),
                topLeft = Offset(center.x - radius, center.y - radius)
            )

            // Progress arc
            val progressSweep = (animatedRpm / maxRpm) * sweepAngle

//            Brush.sweepGradient(
//                Color(0xFF102840),
//
//                listOf(Color(0xFF00E5FF), Color(0xFF00FF99))
//            ),
            drawArc(
                brush = Brush.sweepGradient(
                    listOf(
                        Color(0xFF00E5FF),
                        Color(0xFF00FF99),
                        Color(0xFFFFC107),
                        Color(0xFFFF3D00)
                    )                ),
                startAngle = startAngle,
                sweepAngle = progressSweep,
                useCenter = false,
                style = Stroke(strokeWidth, cap = StrokeCap.Round),
                size = Size(radius * 2, radius * 2),
                topLeft = Offset(center.x - radius, center.y - radius)
            )

            // Tick marks
            val totalTicks = 40
            for (i in 0..totalTicks) {
                val angle = Math.toRadians((startAngle + i * (sweepAngle / totalTicks)).toDouble())

                val tickStart = Offset(
                    center.x + (radius * 0.85f) * cos(angle).toFloat(),
                    center.y + (radius * 0.85f) * sin(angle).toFloat()
                )

                val tickEnd = Offset(
                    center.x + radius * cos(angle).toFloat(),
                    center.y + radius * sin(angle).toFloat()
                )

                drawLine(
                    color = Color.Gray,
                    start = tickStart,
                    end = tickEnd,
                    strokeWidth = if (i % 5 == 0) 4f else 2f
                )
            }

            // Numbers (0–8)
            for (i in 0..8) {
                val angle = Math.toRadians((startAngle + i * (sweepAngle / 8)).toDouble())
                val textRadius = radius * 0.65f

                drawContext.canvas.nativeCanvas.apply {
                    drawText(
                        "$i",
                        center.x + textRadius * cos(angle).toFloat(),
                        center.y + textRadius * sin(angle).toFloat(),
                        android.graphics.Paint().apply {
                            color = android.graphics.Color.BLACK
                            textSize = 32f
                            textAlign = android.graphics.Paint.Align.CENTER
                        }
                    )
                }
            }

            // Needle
            val needleAngle = Math.toRadians((startAngle + progressSweep).toDouble())
            val needleLength = radius * 0.75f

            val needleEnd = Offset(
                center.x + needleLength * cos(needleAngle).toFloat(),
                center.y + needleLength * sin(needleAngle).toFloat()
            )

            drawLine(
                color =
                    //com.example.obdapp.ui.theme.Speed,
                    Rpm,
                    //AppBgDark,
                    //Color.Red,
                start = center,
                end = needleEnd,
                strokeWidth = 6.dp.toPx(),
                cap = StrokeCap.Round
            )

            // Center circle
            drawCircle(Color.White, radius = 10.dp.toPx(), center = center)
        }
        Box(
            modifier = Modifier
                .size(80.dp) // fixed size to prevent overflow
                .align(Alignment.Center) // align in parent gauge
                .background(Color(0x88000000), CircleShape),
            contentAlignment = Alignment.Center // center the Column inside
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "${animatedRpm.toInt()}",
                    color = Color.White,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "RPM",
                    color = Color.White,
                    fontSize = 12.sp
                )
            }
        }
    }
}
@Composable
fun SpeedGaugePro(
    speed: Float,
    maxSpeed: Float = 240f,
    modifier: Modifier = Modifier.size(260.dp)
) {
    val animatedSpeed by animateFloatAsState(
        targetValue = speed.coerceIn(0f, maxSpeed),
        animationSpec = tween(700, easing = FastOutSlowInEasing),
        label = "speedAnim"
    )

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = modifier) {

            val radius = size.minDimension / 2
            val center = Offset(size.width / 2, size.height / 2)

            val startAngle = 135f
            val sweepAngle = 270f
            val strokeWidth = 14.dp.toPx()

            // Background arc
            drawArc(
                color =
                    AppBgDark,
                    //Color(0xFF102840),
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(strokeWidth, cap = StrokeCap.Round),
                size = Size(radius * 2, radius * 2),
                topLeft = Offset(center.x - radius, center.y - radius)
            )

            // Progress arc
            val progressSweep = (animatedSpeed / maxSpeed) * sweepAngle

            drawArc(
                brush = Brush.sweepGradient(
                    listOf(Color(0xFF00E5FF), Color(0xFF007AFF))
                ),
                startAngle = startAngle,
                sweepAngle = progressSweep,
                useCenter = false,
                style = Stroke(strokeWidth, cap = StrokeCap.Round),
                size = Size(radius * 2, radius * 2),
                topLeft = Offset(center.x - radius, center.y - radius)
            )

            // Tick marks
            val totalTicks = 40
            for (i in 0..totalTicks) {
                val angle = Math.toRadians((startAngle + i * (sweepAngle / totalTicks)).toDouble())

                val tickStart = Offset(
                    center.x + (radius * 0.85f) * cos(angle).toFloat(),
                    center.y + (radius * 0.85f) * sin(angle).toFloat()
                )

                val tickEnd = Offset(
                    center.x + radius * cos(angle).toFloat(),
                    center.y + radius * sin(angle).toFloat()
                )

                drawLine(
                    color = Color.Gray,
                    start = tickStart,
                    end = tickEnd,
                    strokeWidth = if (i % 5 == 0) 4f else 2f
                )
            }

            // Numbers (0, 40, 80, 120, 160, 200, 240)
            val steps = 6
            for (i in 0..steps) {
                val value = (i * maxSpeed / steps).toInt()
                val angle = Math.toRadians((startAngle + i * (sweepAngle / steps)).toDouble())
                val textRadius = radius * 0.65f

                drawContext.canvas.nativeCanvas.drawText(
                    "$value",
                    center.x + textRadius * cos(angle).toFloat(),
                    center.y + textRadius * sin(angle).toFloat(),
                    android.graphics.Paint().apply {
                        color = android.graphics.Color.BLACK
                        textSize = 32f
                        textAlign = android.graphics.Paint.Align.CENTER
                        isAntiAlias = true
                    }
                )
            }

            // Needle
            val needleAngle = Math.toRadians((startAngle + progressSweep).toDouble())
            val needleLength = radius * 0.75f

            val needleEnd = Offset(
                center.x + needleLength * cos(needleAngle).toFloat(),
                center.y + needleLength * sin(needleAngle).toFloat()
            )

            drawLine(
                color = Color(0xFF007AFF),
                    //AppBgDark,
                    //Color.Cyan,
                start = center,
                end = needleEnd,
                strokeWidth = 6.dp.toPx(),
                cap = StrokeCap.Round
            )

            // Center circle
            drawCircle(Color.White, radius = 10.dp.toPx(), center = center)
        }

        Box(
            modifier = Modifier
                .size(80.dp) // fixed square size
                .background(Color(0x88000000), CircleShape),
            contentAlignment = Alignment.Center // center text inside
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "${animatedSpeed.toInt()}",
                    color = Color.White,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "km/h",
                    color = Color.White,
                    fontSize = 12.sp
                )
            }
        }
    }
}




@Composable
fun StatusGrid(viewModel: BluetoothViewModel = viewModel(
    factory = ViewModelProvider.AndroidViewModelFactory(LocalContext.current.applicationContext as Application)
)) {
    val speed by viewModel.speed.collectAsState()
    val rpm by viewModel.rpm.collectAsState()
    val engineTemp by viewModel.engineTemp.collectAsState()
    val voltage by viewModel.voltage.collectAsState()
    val intakeTemp by viewModel.intakeTem.collectAsState()
    val throttlePosition by viewModel.throttlePosition.collectAsState()


    Column {

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            StatusCard("Speed", "$speed", Icons.Default.Speed)
            StatusCard("RPM", rpm, Icons.Default.Refresh)
            StatusCard("Engine Temp", engineTemp, Icons.Default.DeviceThermostat)

        }
        Spacer(Modifier.height(12.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            StatusCard("Voltage", "$voltage", Icons.Default.BatteryFull)

            StatusCard("Intake Temp", "$intakeTemp ", Icons.Default.Air)

            StatusCard("Throttle", "$throttlePosition", Icons.Default.Tune)

        }
    }
}

@Composable
fun StatusCard(
    title: String,
    value: String,
    icon: ImageVector
) {
    Column(
        modifier = Modifier
            .width(110.dp)
            .height(100.dp)
            .background(
                Card,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(12.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = Color.Gray,
                    //AccentGreen,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(title, color = Color.Gray, fontSize = 12.sp)
        }

        Text(value, color = Color(0xFF212121)
            //Color.White
            , fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}


@Composable
fun DiagnosticButton(
    bluetoothStatus: String? = null,
    onClick: () -> Unit
) {
    val isEnabled =true
    Button(
        onClick = onClick,
        enabled = isEnabled,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF007AFF),
            contentColor = Color.White,
            disabledContainerColor = Color(0xFF007AFF).copy(alpha = 0.4f),
            disabledContentColor = Color.White.copy(alpha = 0.6f)
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 4.dp,
            pressedElevation = 1.dp,
            disabledElevation = 0.dp
        )
    ) {
        Icon(
            imageVector = Icons.Default.AutoAwesome,
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = "Smart Diagnostic",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}






