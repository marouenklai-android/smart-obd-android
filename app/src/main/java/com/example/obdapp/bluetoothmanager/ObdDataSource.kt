package com.example.obdapp.bluetoothmanager

interface ObdDataSource {
    suspend fun connect(mac: String)
    suspend fun connectByName(mac: String)

    suspend fun readSpeed(): String
    suspend fun readRpm(): String
    suspend fun readDtc(): String
    suspend fun readEngineTemp(): String
    fun disconnect()
}
