package com.example.obdapp.vindecoder


data class VinDecodeResult(
    val vin: String,
    val make: String,
    val modelYear: Int,
    val model: String?,
    val engine: String?,
    val bodyType: String?,
    val plant: String,
    val serialNumber: String,
    val powertrain: Powertrain,
    val confidence: Int
)
