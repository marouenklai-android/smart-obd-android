package com.example.obdapp.vindecoder

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
//
//class VinDecoder(private val context: Context) {
//
//    private val wmiData by lazy {
//        loadMapFromAssets("wmi.json")
//    }
//
//    private val plantData by lazy {
//        loadMapFromAssets("plant_codes.json")
//    }
//
//    private val vdsPatterns by lazy {
//        loadArrayFromAssets("vds_patterns.json")
//    }
//
//    // ---------- Loaders ----------
//
//    private fun loadMapFromAssets(fileName: String): Map<String, String> {
//        val json = loadTextFromAssets(fileName)
//        val obj = JSONObject(json)
//        return obj.keys().asSequence().associateWith { obj.getString(it) }
//    }
//
//    private fun loadArrayFromAssets(fileName: String): JSONArray {
//        val json = loadTextFromAssets(fileName)
//        return JSONArray(json)
//    }
//
//    private fun loadTextFromAssets(fileName: String): String {
//        return context.assets.open(fileName)
//            .bufferedReader()
//            .use { it.readText() }
//    }
//
//    // ---------- Public API ----------
//
//    fun resolveManufacturer(vin: String): String? {
//        return wmiData[vin.take(3)]
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    fun decode(vin: String, obdData: ObdData? = null): VinDecodeResult? {
//        println("hello test decode")
//        if (vin.length != 17) return null
//
//        val make = wmiData[vin.substring(0, 3)] ?: "Unknown"
//        val modelYear = VinYearResolver.resolve(vin) ?: return null
//        val plant = plantData[vin[10].toString()] ?: "Unknown"
//        val serialNumber = vin.substring(11, 17)
//
//        val vds = vin.substring(3, 8)
//
//        var model: String? = null
//        var engine: String? = null
//        var bodyType: String? = null
//        var confidence = 60
//        var powertrain = Powertrain.ICE
//        var fuelType: String? = null
//
//        for (i in 0 until vdsPatterns.length()) {
//            val obj = vdsPatterns.getJSONObject(i)
//            if (vds == obj.getString("pattern")) {
//                val (start, end) = obj.getString("yearRange")
//                    .split("-")
//                    .map { it.toInt() }
//
//                if (modelYear in start..end) {
//                    model = obj.optString("model")
//                    engine = obj.optString("engine")
//                    bodyType = obj.optString("bodyType")
//                    fuelType = obj.optString("fuel")
//
//                    confidence = 90
//                    break
//                }
//            }
//        }
//
//        powertrain = when (fuelType) {
//            "EV" -> Powertrain.EV
//            "Hybrid" -> Powertrain.HYBRID
//            "PHEV" -> Powertrain.PLUG_IN_HYBRID
//            else -> Powertrain.ICE
//        }
//
//        obdData?.maxRpm?.let {
//            if (engine != null && it in 4000..7000) confidence += 5
//        }
//
//        println("vin = $vin" +
//                "make = make,\n" +
//                "            modelYear = $modelYear,\n" +
//                "            powertrain = $powertrain,\n" +
//
//                "            model = $model,\n" +
//                "            engine = $engine,\n" +
//                "            bodyType = $bodyType,\n" +
//                "            plant = $plant,\n" +
//                "            serialNumber = $serialNumber,\n" +
//                "            confidence = $confidence")
//        return VinDecodeResult(
//            vin = vin,
//            make = make,
//            modelYear = modelYear,
//            model = model,
//            engine = engine,
//            bodyType = bodyType,
//            plant = plant,
//            serialNumber = serialNumber,
//            confidence = confidence
//        )
//    }
//}


class VinDecoder(private val context: Context) {

    // Load WMI (first 3 VIN chars → manufacturer)
    private val wmiData: Map<String, String> by lazy {
        loadJsonMapFromAssets("wmi.json")
    }

    // Plant codes (VIN 11th char → plant)
    private val plantData: Map<String, String> by lazy {
        loadJsonMapFromAssets("plant_codes.json")
    }

    // VDS patterns (VIN 4–8 → model info)
    private val vdsPatterns: JSONArray by lazy {
        JSONArray(loadJsonFromAssets("kia_vds.json"))
    }

    private fun loadJsonFromAssets(fileName: String): String =
        context.assets.open(fileName).bufferedReader().use { it.readText() }

    private fun loadJsonMapFromAssets(fileName: String): Map<String, String> {
        val obj = JSONObject(loadJsonFromAssets(fileName))
        return obj.keys().asSequence().associateWith { key -> obj.getString(key) }
    }

    // -------------------------
    // Resolve manufacturer from VIN
    // -------------------------
    fun resolveManufacturer(vin: String): String? = wmiData[vin.take(3)]

    // -------------------------
    // Decode VIN to full result
    // -------------------------
    fun decode(vin: String, obdData: ObdData? = null): VinDecodeResult? {
        if (vin.length != 17) return null

        val make = wmiData[vin.substring(0, 3)] ?: "Unknown"
        val modelYear = VinYearResolver.resolve(vin) ?: 0
        val plant = plantData[vin[10].toString()] ?: "Unknown"
        val serialNumber = vin.substring(11, 17)

        // VDS match (positions 4–8)
        val vds = vin.substring(3, 8)
        var model: String? = null
        var engine: String? = null
        var bodyType: String? = null
        var fuelType: FuelType = FuelType.GASOLINE
        var powertrain: Powertrain = Powertrain.ICE
        var confidence = 50

        for (i in 0 until vdsPatterns.length()) {
            val obj = vdsPatterns.getJSONObject(i)
            if (vds == obj.getString("pattern")) {
                val range = obj.getString("yearRange").split("-").map { it.toInt() }
                if (modelYear in range[0]..range[1]) {
                    model = obj.getString("model")
                    engine = obj.getString("engine")
                    bodyType = obj.getString("bodyType")

                    // Fuel type from JSON
                    fuelType = when (obj.optString("fuel")) {
                        "Diesel" -> FuelType.DIESEL
                        "Hybrid" -> FuelType.HYBRID
                        "PHEV" -> FuelType.PLUG_IN_HYBRID
                        "EV" -> FuelType.ELECTRIC
                        else -> FuelType.GASOLINE
                    }

                    // Powertrain derived from fuel type
                    powertrain = when (fuelType) {
                        FuelType.ELECTRIC -> Powertrain.EV
                        FuelType.HYBRID -> Powertrain.HYBRID
                        FuelType.PLUG_IN_HYBRID -> Powertrain.PHEV
                        else -> Powertrain.ICE
                    }

                    confidence = 90
                    break
                }
            }
        }

        // -------------------------
        // Optional OBD consistency checks
        // -------------------------
        obdData?.let { obd ->
            when (powertrain) {
                Powertrain.ICE -> {
                    if (obd.maxRpm != null && obd.maxRpm in 4000..7000) confidence += 5
                }
                Powertrain.HYBRID, Powertrain.PHEV -> {
                    if (obd.tractionBatterySoc != null && obd.maxRpm != null) confidence += 5
                }
                Powertrain.EV -> {
                    if (obd.maxRpm == null && obd.tractionBatterySoc != null) confidence += 10
                }
            }
        }

        return VinDecodeResult(
            vin = vin,
            make = make,
            modelYear = modelYear,
            model = model,
            engine = engine,
            bodyType = bodyType,
            plant = plant,
            serialNumber = serialNumber,
            powertrain = powertrain,
            confidence = confidence
        )
    }
}