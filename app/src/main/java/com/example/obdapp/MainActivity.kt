package com.example.obdapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.obdapp.bluetoothmanager.ObdJavaApiDataSource
import com.example.obdapp.ui.language.LanguageViewModel
import com.example.obdapp.ui.navigation.AppNavGraph
import com.example.obdapp.ui.theme.ObdAppTheme
import com.example.obdapp.vindecoder.FuelType
import com.example.obdapp.vindecoder.ObdData
import com.example.obdapp.vindecoder.VinDecoder
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.OutputStream
import java.net.Socket

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    private val obdDataSource = ObdJavaApiDataSource()

    @Composable
    fun AppSystemBars() {
        val systemUiController = rememberSystemUiController()
        val isDark = isSystemInDarkTheme()

        val barColor = if (isDark) {
            Color(0xFF102B21) // dark green / black
        } else {
            Color(0xFF102B21) // soft light green (NOT pure white)
        }

        SideEffect {
            systemUiController.setStatusBarColor(
                color = barColor,
                darkIcons = !isDark
            )

            systemUiController.setNavigationBarColor(
                color = barColor,
                darkIcons = !isDark
            )
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
//        WindowCompat.setDecorFitsSystemWindows(window, false)
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            window.isNavigationBarContrastEnforced = false
//        }
//
//        window.navigationBarColor = android.graphics.Color.TRANSPARENT
//        window.statusBarColor = android.graphics.Color.TRANSPARENT
//        WindowCompat.setDecorFitsSystemWindows(window, false)

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            window.isNavigationBarContrastEnforced = false
//        }

        setContent {
            ObdAppTheme {
                val languageViewModel = viewModel<LanguageViewModel>()

//                LanguageScreen(
//                    viewModel = languageViewModel,
//                    onContinue = { /* next */ }
//                )
                //AppSystemBars()
                AppNavGraph(
                    languageSelected = false // DataStore later
                )
                val vin = "KNAB3511AKT381913"
                val obd = ObdData(maxRpm = 6500, fuelType = FuelType.GASOLINE)

                val result = VinDecoder(context = applicationContext).decode(vin,obd)
                LaunchedEffect(Unit) {
                withContext(Dispatchers.IO) @androidx.annotation.RequiresPermission(android.Manifest.permission.BLUETOOTH_CONNECT)  {
                    try {
                       // ObdJavaApiDataSource().connectByName("MAROUANPC")
                    }catch (e:Exception){
                        e.printStackTrace()
                    }

                    //val transport = TcpObdTransport("192.168.1.12", 20000)
                    //transport.connect()
//
                    //val input = transport.inputStream
                    //val output = transport.outputStream

                    //val obdConnection = ObdDeviceConnection(input, output)
                    //com.github.pires.obd.commands.engine.RPMCommand().run(input, output)
                    val rpmJava =com.github.pires.obd.commands.engine.RPMCommand().rpm
                    //val speed = obdConnection.run(SpeedCommand(), delayTime = 400L)
                    //val rpm = obdConnection.run(RPMCommand(),delayTime = 500L)
//                    // Retrieving OBD Speed Command
                    //val vin = obdConnection.run(VINCommand())
                    //val dtdcNumberCommand = obdConnection.run(DTCNumberCommand())
                    //println("hello test $speed $rpm $vin dtdcNumberCommand$dtdcNumberCommand  rpmJava$rpmJava")


                        //try {

//                    lifecycleScope.launch {
//                        try {
//                            obdDataSource.connectByName("MAROUANPC")
//                            val speed = obdDataSource.readSpeed() // only after connection
//                            val rpm = obdDataSource.readRpm() // only after connection
//                            //val dtc = obdDataSource.readDtc() // only after connection
//
//
//                            Log.d("OBD", "speed $speed")
//                        } catch (e: Exception) {
//                            Log.e("OBD", "Connection or read failed", e)
//                        }
//                    }

                    }
                        //val a = ObdJavaApiDataSource().readRpm()
//                        ObdJavaApiDataSource().connectByName("MAROUANPC")}
//
//                            val b = ObdJavaApiDataSource().readSpeed()
//                            val c = ObdJavaApiDataSource().readDtc()
//                            println("hello  b $b c $c")



//
//                    } catch(e: Exception) {
//                        Log.e("OBD", "Read error", e)
//                    }
                    delay(1000)
                //}
                }
////
////                     Commands with extra delay
//              val speed = obdConnection.run(SpeedCommand(), delayTime = 400L)
                //println("hello test $result")
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)
//                    )
//
//
//                }
            }
//            LaunchedEffect(Unit) {
//
//                withContext(Dispatchers.IO) {
//                    val transport = TcpObdTransport("192.168.1.12", 4444)
//                    transport.connect()
//
//                    val input = transport.inputStream
//                    val output = transport.outputStream
//
//                    //                    // Now wrap connection
//                    val obdConnection = ObdDeviceConnection(input, output)
////
////                    // Commands with extra delay
//                    //val speed = obdConnection.run(SpeedCommand(), delayTime = 400L)
//                    //val vin = obdConnection.run(VINCommand(), delayTime = 400L)
//                    //val rpm = obdConnection.run(RPMCommand(), delayTime = 500L)
//                    // Retrieving OBD Speed Command
//                    val speed = obdConnection.run(SpeedCommand())
//
//// Using cache (use with caution)
//                    val vin = obdConnection.run(VINCommand(), useCache = true)
//
//                    val rpm = obdConnection.run(RPMCommand(), delayTime = 500L)
//                    //val rawSpeed = sendObdCommand(input, output, "010D")  // 41 0D 04 41 0D 04
//                    //val rawRPM = sendObdCommand(input, output, "010C")    // 41 0C 0A 00 41 0C 0A 00
//                    //val rawVIN = sendObdCommand(input, output, "0902")    // ASCII hex
//                    //val speedKmH = parseSpeed(rawSpeed)
//                    //val rpmVal = parseRPM(rawRPM)
//                    //val vinStr = parseVIN(rawVIN)
//                    println("speed=$speed rpm=$rpm vin=$vin")
//                //println("speed=$rawSpeed vin=$rawRPM rpm=$rawVIN")
////
////                    // Initialize emulator manually
////                    sendObdCommand(input, output, "ATZ")
////                    sendObdCommand(input, output, "ATE0")
////
////                    // Read Speed, RPM, VIN
////                    val speed = sendObdCommand(input, output, "010D")
////                    val rpm = sendObdCommand(input, output, "010C")
////                    val vin = sendObdCommand(input, output, "0902")
////
////                    println("speed=$speed rpm=$rpm vin=$vin") // ✅ NOW this WILL print
//
////                    val transport = TcpObdTransport("10.0.2.2", 4444)
////                    transport.connect()
////
////                    val input = transport.inputStream
////                    val output = transport.outputStream
////                    // Manual initialization (emulator only)
////                    output.write("ATZ\r".toByteArray())
////                    output.flush()
////                    Thread.sleep(500)  // Wait for emulator to respond
////
////                    output.write("ATE0\r".toByteArray())
////                    output.flush()
////                    Thread.sleep(200)
////
////                    val obdConnection = ObdDeviceConnection(input, output)
////
////                    // Run commands with long delay
////                    val speed = obdConnection.run(SpeedCommand(), delayTime = 500L)
////                    val vin = obdConnection.run(VINCommand(), delayTime = 500L)
////                    val rpm = obdConnection.run(RPMCommand(), delayTime = 500L)
////
////                    println("speed=$speed vin=$vin rpm=$rpm")  // ✅ Now this will print
//
//
////                    // Send basic AT init commands manually
////                    sendCommand(output, input, "ATZ")
////                    sendCommand(output, input, "ATE0")
////                    sendCommand(output, input, "ATL0")
////                    sendCommand(output, input, "ATS0")
////                    sendCommand(output, input, "ATH0")
////                    sendCommand(output, input, "ATSP0")
////
////                    // Now wrap connection
////                    val obdConnection = ObdDeviceConnection(input, output)
////
////                    // Commands with extra delay
////                    val speed = obdConnection.run(SpeedCommand(), delayTime = 400L)
////                    val vin = obdConnection.run(VINCommand(), delayTime = 400L)
////                    val rpm = obdConnection.run(RPMCommand(), delayTime = 500L)
////
////                    println("speed=$speed vin=$vin rpm=$rpm")
//                }
//
////                withContext(Dispatchers.IO) {
////                    val transport = TcpObdTransport("10.0.2.2", 4444)
////                    println("hello response 0")
////
////                    transport.connect()
////                    println("hello response 1")
////
////                    val obdConnection = ObdDeviceConnection(
////                        transport.inputStream,
////                        transport.outputStream
////                    )
////                    println("hello response 2")
////
////
////                    // Retrieving OBD Speed Command
////                    val response = obdConnection.run(SpeedCommand(), delayTime = 200L)
////
////                    val cachedResponse = obdConnection.run(VINCommand(), useCache = true, delayTime = 200L)
////
////                    val delayedResponse = obdConnection.run(RPMCommand(), delayTime = 500L)
////                    println("hello response $response cachedResponse $cachedResponse delayedResponse $delayedResponse")
////                }
//            }
        }
    }
    fun parseSpeed(raw: String): Int {
        val bytes = raw.split(" ").mapNotNull { it.toIntOrNull(16) }
        for (i in 0 until bytes.size - 2) {
            if (bytes[i] == 0x41 && bytes[i + 1] == 0x0D) return bytes[i + 2]
        }
        return -1
    }

    fun parseRPM(raw: String): Int {
        val bytes = raw.split(" ").mapNotNull { it.toIntOrNull(16) }
        for (i in 0 until bytes.size - 3) {
            if (bytes[i] == 0x41 && bytes[i + 1] == 0x0C) return ((bytes[i + 2]*256)+bytes[i + 3])/4
        }
        return -1
    }

    fun parseVIN(raw: String): String {
        val hex = raw.split(" ").filter { it.isNotBlank() }
        val sb = StringBuilder()
        for (i in hex.indices) {
            val v = hex[i].toIntOrNull(16) ?: continue
            if (v in 32..126) sb.append(v.toChar())
        }
        // remove header 'I' if present
        return (if (sb.startsWith("I")) sb.drop(1) else sb.toString()) as String
    }


    suspend fun sendObdCommand(input: InputStream, output: OutputStream, command: String): String =
        withContext(Dispatchers.IO) {
            // Send command
            output.write((command + "\r").toByteArray())
            output.flush()

            // Wait for response until '>'
            val buffer = StringBuilder()
            val start = System.currentTimeMillis()
            while (true) {
                if (input.available() > 0) {
                    val c = input.read().toChar()
                    buffer.append(c)
                    if (c == '>') break
                } else {
                    kotlinx.coroutines.delay(10)
                }
                if (System.currentTimeMillis() - start > 5000) break
            }
            buffer.toString().replace(">", "").trim()
        }
}
fun sendAt(
    output: OutputStream,
    input: InputStream,
    cmd: String,
    delayMs: Long = 200
) {
    output.write((cmd + "\r").toByteArray())
    output.flush()
    Thread.sleep(delayMs)

    // Drain input until '>'
    val buffer = StringBuilder()
    while (true) {
        val c = input.read().toChar()
        buffer.append(c)
        if (c == '>') break
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ObdAppTheme {
        Greeting("Android")
    }
}
fun readUntilPrompt(input: InputStream, timeoutMs: Long = 2000): String {
    val buffer = StringBuilder()
    val start = System.currentTimeMillis()
    while (true) {
        if (input.available() > 0) {
            val c = input.read().toChar()
            buffer.append(c)
            if (c == '>') break
        } else {
            Thread.sleep(10)
        }
        if (System.currentTimeMillis() - start > timeoutMs) break
    }
    return buffer.toString()
}

fun sendCommand(output: OutputStream, input: InputStream, cmd: String): String {
    output.write((cmd + "\r").toByteArray())
    output.flush()
    return readUntilPrompt(input)
}


class TcpObdTransport(
    private val host: String,
    private val port: Int
) {

    private lateinit var socket: Socket
    lateinit var inputStream: InputStream
        private set
    lateinit var outputStream: OutputStream
        private set

    fun connect() {
        socket = Socket(host, port)
        socket.soTimeout = 8000
        inputStream = socket.getInputStream()
        outputStream = socket.getOutputStream()
    }

    fun close() {
        socket.close()
    }
}



class TcpObdHelper(
    private val input: InputStream,
    private val output: OutputStream
) {

    // Send a command and read until '>' prompt
    suspend fun runCommand(command: String, delayMs: Long = 400L): String =
        withContext(Dispatchers.IO) {
            output.write((command + "\r").toByteArray())
            output.flush()
            kotlinx.coroutines.delay(delayMs)

            val buffer = StringBuilder()
            val startTime = System.currentTimeMillis()

            while (true) {
                if (input.available() > 0) {
                    val c = input.read().toChar()
                    buffer.append(c)
                    if (c == '>') break
                } else {
                    kotlinx.coroutines.delay(10)
                }

                // Timeout 5 seconds
                if (System.currentTimeMillis() - startTime > 5000) break
            }

            buffer.toString().replace(">", "").trim()
        }

    // Convenience methods for common OBD commands
    suspend fun getSpeed(): String = runCommand("010D")
    suspend fun getRPM(): String = runCommand("010C")
    suspend fun getVIN(): String = runCommand("0902")

    // Optional: AT initialization
    suspend fun initEmulator() {
        runCommand("ATZ", 500)
        runCommand("ATE0", 200)
        runCommand("ATL0", 200)
        runCommand("ATS0", 200)
        runCommand("ATH0", 200)
        runCommand("ATSP0", 300)
    }
}
