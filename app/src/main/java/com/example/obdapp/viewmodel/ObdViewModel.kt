package com.example.obdapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.obdapp.bluetoothmanager.ObdJavaApiDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch


class ObdViewModel() : ViewModel() {
    private val obdDataSource = ObdJavaApiDataSource()


    private val _engineTemp = MutableStateFlow("--")
    val engineTemp: StateFlow<String> = _engineTemp

    private val _speed = MutableStateFlow("--")
    val speed: StateFlow<String> = _speed
    private val     _speedNoUnit = MutableStateFlow("--")
    val speedNoUnit: StateFlow<String> =     _speedNoUnit
    private val     _rpmNoUnit = MutableStateFlow("--")
    val rpmNoUnit: StateFlow<String> =     _rpmNoUnit


    private val _rpm = MutableStateFlow("--")
    val rpm: StateFlow<String> = _rpm

    private val _voltage = MutableStateFlow("--")
    val voltage: StateFlow<String> = _voltage

    private val _intakeTem = MutableStateFlow("--")
    val intakeTem: StateFlow<String> = _intakeTem

    private val _throttlePosition = MutableStateFlow("--")
    val throttlePosition: StateFlow<String> = _throttlePosition
    private var obdJob: Job? = null

    // Keep history of last 6 points
    val speedHistory = MutableStateFlow<List<Float>>(emptyList())
    val rpmHistory = MutableStateFlow<List<Float>>(emptyList())


    fun startObd() {
        obdJob?.cancel()

        obdJob = viewModelScope.launch(Dispatchers.IO) {
            try {
                obdDataSource.connectByName("MAROUANPC")

                while (isActive) {
                    val speedVal = obdDataSource.readSpeed()
                    val speedNoUnitVal = obdDataSource.readSpeedNoUnit()
                    val rpmNoUnitVal = obdDataSource.readRPMNoUnit()


                    val rpmVal = obdDataSource.readRpm()
                    val tempVal = obdDataSource.readEngineTemp()
                    val voltageVal = obdDataSource.readModuleVoltageCommand()
                    val intakeTemperatureVal = obdDataSource.readAirIntakeTemperatureCommand()
                    val throttlePositionVal = obdDataSource.readThrottlePositionCommand()

                    // Update history
                    val newSpeedHist = (speedHistory.value + speedNoUnitVal.toFloat()).takeLast(6)
                    val newRpmHist = (rpmHistory.value + rpmVal).takeLast(6)
                    speedHistory.value = newSpeedHist
                    //rpmHistory.value = newRpmHist
                    _speed.emit("$speedVal")
                    _speedNoUnit.emit("$speedNoUnitVal")
                    _rpmNoUnit.emit("$rpmNoUnitVal")


                    _rpm.emit("$rpmVal")
                    _engineTemp.emit("$tempVal")
                    _voltage.emit("$voltageVal")
                    _intakeTem.emit("$intakeTemperatureVal")
                    _throttlePosition.emit("$throttlePositionVal")

                    delay(100) // refresh every second
                }
            } catch (e: Exception) {
                Log.e("OBD", "OBD loop failed", e)
            }
        }
    }
    fun clear() {
        obdDataSource.disconnectOBD() // make sure socket is closed when ViewModel is destroyed
    }
    fun stopReadingOBD() {
        obdJob?.cancel()
    }
}
