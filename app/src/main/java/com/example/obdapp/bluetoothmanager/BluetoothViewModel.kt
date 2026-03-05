package com.example.obdapp.bluetoothmanager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.obdapp.App
import com.example.obdapp.ui.dtc.DtcInfo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class BluetoothViewModel() : ViewModel() {

    private val obdDataSource= ObdJavaApiDataSource()
    private val _connectionState =
        MutableStateFlow<BluetoothConnectionState>(BluetoothConnectionState.Idle)
    val connectionState: StateFlow<BluetoothConnectionState> = _connectionState

    private var lastDeviceName: String? = null

    private val _engineTemp = MutableStateFlow("--")
    val engineTemp: StateFlow<String> = _engineTemp
    private val _engineTempNoUnit = MutableStateFlow("--")
    val engineTempNoUnit: StateFlow<String> = _engineTempNoUnit

    private val _speed = MutableStateFlow("--")
    val speed: StateFlow<String> = _speed
    private val     _speedNoUnit = MutableStateFlow("--")
    val speedNoUnit: StateFlow<String> =     _speedNoUnit
    private val     _rpmNoUnit = MutableStateFlow("--")
    val rpmNoUnit: StateFlow<String> =     _rpmNoUnit
    private val     _temEngineNoUnit = MutableStateFlow("--")
    val temEngineNoUnit: StateFlow<String> =     _temEngineNoUnit
    private val _dtc = MutableStateFlow("--")
    val dtc: StateFlow<String> = _dtc



    private val _rpm = MutableStateFlow("--")
    val rpm: StateFlow<String> = _rpm

    private val _voltage = MutableStateFlow("--")
    val voltage: StateFlow<String> = _voltage
    private val _voltageNoUnit = MutableStateFlow("--")
    val voltageNoUnit: StateFlow<String> = _voltageNoUnit

    private val _intakeTem = MutableStateFlow("--")
    val intakeTem: StateFlow<String> = _intakeTem


    private val _intakeTemNoUnit = MutableStateFlow("--")
    val intakeTemUnit: StateFlow<String> = _intakeTemNoUnit

    private val _throttlePosition = MutableStateFlow("--")
    val throttlePosition: StateFlow<String> = _throttlePosition

    private val _throttlePositionNoUnit = MutableStateFlow("--")
    val throttlePositionNoUnit: StateFlow<String> = _throttlePositionNoUnit
    private var obdJob: Job? = null

    // Keep history of last 6 points
    val speedHistory = MutableStateFlow<List<Float>>(emptyList())
    val rpmHistory = MutableStateFlow<List<Float>>(emptyList())


    val dtcMap: Map<String, DtcInfo> by lazy {
        // get application context from your singleton / app class
        val context = App.instance.applicationContext

        val jsonString = context.assets.open("dtc_p0xx_fr.json")
            .bufferedReader()
            .use { it.readText() }

        val type = object : TypeToken<Map<String, DtcInfo>>() {}.type
        Gson().fromJson<Map<String, DtcInfo>>(jsonString, type)
    }

    private val _dtcList = MutableStateFlow<List<DtcInfo>>(emptyList())
    val dtcList: StateFlow<List<DtcInfo>> = _dtcList

    fun updateDtcFromString(dtcString: String) {
        val codes = dtcString
            .split("\n", "\r\n")
            .map { it.trim() }
            .filter { it.isNotEmpty() }

        _dtcList.value = codes.map { code ->
            dtcMap[code]?.copy(code = code) ?: DtcInfo(
                code= code,
                title = "Unknown code",
                severity = "UNKNOWN",
                description = "No description available."
            )
        }
    }

    fun connect(deviceName: String) {
        lastDeviceName = deviceName

        obdJob?.cancel()

        obdJob = viewModelScope.launch(Dispatchers.IO) {
        //viewModelScope.launch {
            try {
                _connectionState.value = BluetoothConnectionState.Connecting
                obdDataSource.connectByName(deviceName)
                _connectionState.value = BluetoothConnectionState.Connected
                while (isActive) {
                    val speedVal = obdDataSource.readSpeed()
                    val speedNoUnitVal = obdDataSource.readSpeedNoUnit()
                    val rpmNoUnitVal = obdDataSource.readRPMNoUnit()
                    val tempEngineNoUnitVal = obdDataSource.readTemEngineNoUnit()





                    val rpmVal = obdDataSource.readRpm()
                    val tempVal = obdDataSource.readEngineTemp()
                    val voltageVal = obdDataSource.readModuleVoltageCommand()

                    //obdDataSource.readDtc()
                    val intakeTemperatureVal = obdDataSource.readAirIntakeTemperatureCommand()
                    val throttlePositionVal = obdDataSource.readThrottlePositionCommand()

                    println("tempEngineNoUnitVal: $tempEngineNoUnitVal")
                    println("tempVal: $tempVal")
                    // Update history
                    val newSpeedHist = (speedHistory.value + speedNoUnitVal.toFloat()).takeLast(6)
                    val newRpmHist = (rpmHistory.value + rpmVal).takeLast(6)
                    speedHistory.value = newSpeedHist
                    //rpmHistory.value = newRpmHist
                    _speed.emit("$speedVal")
                    _speedNoUnit.emit("${speedNoUnitVal.toInt()}")
                    //_speedNoUnit.emit("$speedNoUnitVal")
                    _rpmNoUnit.emit("$rpmNoUnitVal")
                    _engineTempNoUnit.emit(obdDataSource.readEngineTempNoUnit())
                    //("${tempEngineNoUnitVal}")
                    _voltageNoUnit.emit(obdDataSource.readModuleVoltageNoUnit())
                    _intakeTemNoUnit.emit(obdDataSource.readAirIntakeTemperatureNoUnitCommand())
                    _throttlePositionNoUnit.emit(obdDataSource.readThrottlePositionNoUnitCommand())
                    //_engineTempNoUnit

                    _dtc.emit(obdDataSource.readDtc())
                    updateDtcFromString(obdDataSource.readDtc())



                    _rpm.emit("$rpmVal")
                    _engineTemp.emit("$tempVal")
                    _voltage.emit("$voltageVal")
                    _intakeTem.emit("$intakeTemperatureVal")
                    _throttlePosition.emit("$throttlePositionVal")

                    delay(100) // refresh every second
                }
            } catch (e: Exception) {
                _connectionState.value =
                    BluetoothConnectionState.Error(e.message ?: "Connection failed")
            }
        }
    }

    fun retry() {
        lastDeviceName?.let { connect(it) }
    }

    fun disconnect() {
        obdDataSource.disconnect()
        _connectionState.value = BluetoothConnectionState.Disconnected
        _engineTemp.value = "--"
        _speed.value = "--"
        _rpm.value = "--"
        _voltage.value = "--"
        _intakeTem.value = "--"
        _throttlePosition.value = "--"
        _speedNoUnit.value = "--"
        _rpmNoUnit.value = "--"

    }

}