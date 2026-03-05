package com.example.obdapp.bluetoothmanager

sealed class BluetoothConnectionState {
    object Idle : BluetoothConnectionState()
    object Connecting : BluetoothConnectionState()
    object Connected : BluetoothConnectionState()
    object Disconnected : BluetoothConnectionState()
    data class Error(val message: String) : BluetoothConnectionState()
}