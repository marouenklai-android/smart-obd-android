package com.example.obdapp.vindecoder

data class ObdData(
    val maxRpm: Int? = null,             // engine RPM (null for EV)
    val displacement: Float? = null,     // engine displacement
    val fuelType: FuelType? = null,      // GASOLINE / DIESEL / ELECTRIC / HYBRID

    // Optional EV / Hybrid info
    val batteryVoltage12V: Float? = null,      // typical 12V battery voltage
    val tractionBatterySoc: Int? = null,       // EV / Hybrid traction battery % SOC
    val electricMotorPowerKw: Float? = null    // EV / Hybrid electric motor power
)

