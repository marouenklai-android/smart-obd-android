package com.example.obdapp.bluetoothmanager

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothSocket
import android.util.Log
import androidx.annotation.RequiresPermission
import com.github.pires.obd.commands.SpeedCommand
import com.github.pires.obd.commands.control.ModuleVoltageCommand
import com.github.pires.obd.commands.control.TroubleCodesCommand
import com.github.pires.obd.commands.control.VinCommand
import com.github.pires.obd.commands.engine.RPMCommand
import com.github.pires.obd.commands.engine.ThrottlePositionCommand
import com.github.pires.obd.commands.protocol.EchoOffCommand
import com.github.pires.obd.commands.protocol.HeadersOffCommand
import com.github.pires.obd.commands.protocol.LineFeedOffCommand
import com.github.pires.obd.commands.protocol.SelectProtocolCommand
import com.github.pires.obd.commands.protocol.TimeoutCommand
import com.github.pires.obd.commands.temperature.AirIntakeTemperatureCommand
import com.github.pires.obd.commands.temperature.EngineCoolantTemperatureCommand
import com.github.pires.obd.enums.ObdProtocols
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.UUID

class ObdJavaApiDataSource : ObdDataSource {

    private lateinit var socket: BluetoothSocket
    private var input: InputStream? = null
    private var output: OutputStream? = null
    private var isConnected = false


    val myUUID: UUID =
        UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    override suspend fun connect(mac: String) {


        val device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(mac)
//        socket = device.createRfcommSocketToServiceRecord(myUUID)
//        socket.connect()
        try {
            // Try secure socket first
            socket = device.createRfcommSocketToServiceRecord(myUUID)
            socket.connect()
            Log.d("OBD", "Connected securely!")
        } catch (e: IOException) {
            Log.e("OBD", "Secure socket failed, trying insecure", e)

            try {
                // Some OBD adapters require insecure connection
                socket = device.createInsecureRfcommSocketToServiceRecord(myUUID)
                socket.connect()
                Log.d("OBD", "Connected insecurely!")
            } catch (ex: IOException) {
                Log.e("OBD", "Connection failed", ex)
            }
        }

        input = socket.inputStream
        output = socket.outputStream

        initElm()
    }
    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    override suspend fun connectByName(mac: String) {
        //val socket = TcpObdTransport("192.168.1.12", 20000)
        val adapter = BluetoothAdapter.getDefaultAdapter()

        val device = adapter.bondedDevices.firstOrNull {
            it.name == mac
        } ?: throw Exception("Device not found")
//
        //socket = device.createRfcommSocketToServiceRecord(myUUID)
        Log.d("OBD", "Connecting...")

        //socket.connect()
        Log.d("OBD", "Connected")

        try {
            // Try secure socket first
            socket = device.createRfcommSocketToServiceRecord(myUUID)
            socket.connect()
            Log.d("OBD", "Connected securely!")
        } catch (e: IOException) {
            Log.e("OBD", "Secure socket failed, trying insecure", e)

            try {
                // Some OBD adapters require insecure connection
                socket = device.createInsecureRfcommSocketToServiceRecord(myUUID)
                socket.connect()
                Log.d("OBD", "Connected insecurely!")
            } catch (ex: IOException) {
                Log.e("OBD", "Connection failed", ex)
            }
        }


        input = socket.inputStream
        output = socket.outputStream
        Log.d("OBD", "Streams ready")
        isConnected = true



        initElm()
    }


    private fun initElm() {
        EchoOffCommand().run(input, output)
        LineFeedOffCommand().run(input, output)
        HeadersOffCommand().run(input, output)

        TimeoutCommand(125).run(input, output)
        SelectProtocolCommand(ObdProtocols.AUTO).run(input, output)
    }
    fun disconnectOBD() {
        try {
            //socket.close()
        } catch (e: IOException) { }
    }



    override suspend fun readSpeed(): String {
        if (!isConnected) throw IllegalStateException("Not connected to OBD")

        val inStream = input ?: throw IllegalStateException("Not connected to OBD")
        val outStream = output ?: throw IllegalStateException("Not connected to OBD")
        val cmd = SpeedCommand()
        cmd.run(inStream, outStream)
        return cmd.formattedResult
    }
     suspend fun readSpeedNoUnit(): String {
        if (!isConnected) throw IllegalStateException("Not connected to OBD")

        val inStream = input ?: throw IllegalStateException("Not connected to OBD")
        val outStream = output ?: throw IllegalStateException("Not connected to OBD")
        val cmd = SpeedCommand()
        cmd.run(inStream, outStream)
        return cmd.calculatedResult
    }
    suspend fun readRPMNoUnit(): String {
        if (!isConnected) throw IllegalStateException("Not connected to OBD")

        val inStream = input ?: throw IllegalStateException("Not connected to OBD")
        val outStream = output ?: throw IllegalStateException("Not connected to OBD")
        val cmd = RPMCommand()
        cmd.run(inStream, outStream)
        return cmd.calculatedResult
    }
    suspend fun readTemEngineNoUnit(): String {
        if (!isConnected) throw IllegalStateException("Not connected to OBD")

        val inStream = input ?: throw IllegalStateException("Not connected to OBD")
        val outStream = output ?: throw IllegalStateException("Not connected to OBD")
        val cmd = EngineCoolantTemperatureCommand()
        cmd.run(inStream, outStream)
        return cmd.calculatedResult
    }

    override suspend fun readEngineTemp(): String{
        if (!isConnected) throw IllegalStateException("Not connected to OBD")

        val inStream = input ?: throw IllegalStateException("Not connected to OBD")
        val outStream = output ?: throw IllegalStateException("Not connected to OBD")
        val cmd = EngineCoolantTemperatureCommand()
        cmd.run(inStream, outStream)
        return cmd.formattedResult
    }

     suspend fun readAirIntakeTemperatureCommand(): String {
        val cmd = AirIntakeTemperatureCommand()
        cmd.run(input, output)
        return cmd.formattedResult
    }

    suspend fun readVINCommand(): String {
        val cmd = VinCommand()
        cmd.run(input, output)
        return cmd.formattedResult
    }
     suspend fun readThrottlePositionCommand(): String {
        val cmd = ThrottlePositionCommand()
        cmd.run(input, output)
        return cmd.formattedResult
    }

     suspend fun readEngineTempNoUnit(): String{
        if (!isConnected) throw IllegalStateException("Not connected to OBD")

        val inStream = input ?: throw IllegalStateException("Not connected to OBD")
        val outStream = output ?: throw IllegalStateException("Not connected to OBD")
        val cmd = EngineCoolantTemperatureCommand()
        cmd.run(inStream, outStream)
        return cmd.calculatedResult
    }

    suspend fun readAirIntakeTemperatureNoUnitCommand(): String {
        val cmd = AirIntakeTemperatureCommand()
        cmd.run(input, output)
        return cmd.calculatedResult
    }

    suspend fun readThrottlePositionNoUnitCommand(): String {
        val cmd = ThrottlePositionCommand()
        cmd.run(input, output)
        return cmd.calculatedResult
    }
    override suspend fun readRpm(): String {
        val cmd = RPMCommand()
        cmd.run(input, output)
        return cmd.formattedResult
    }
     suspend fun readModuleVoltageCommand(): String {
        val cmd = ModuleVoltageCommand()
        cmd.run(input, output)
        return cmd.formattedResult
    }
    suspend fun readModuleVoltageNoUnit(): String {
        val cmd = ModuleVoltageCommand()
        cmd.run(input, output)
        return cmd.formattedResult
    }

    override suspend fun readDtc(): String {
        val cmd = TroubleCodesCommand()
        cmd.run(input, output)
        return cmd.formattedResult
    }

    override fun disconnect() {
        socket.close()
    }
}
