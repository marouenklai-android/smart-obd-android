package com.example.obdapp.geminianalysis

import com.example.obdapp.ui.dtc.DtcInfo

data class ObdData(
    val speed: String,
    val rpm: String,
    val coolantTemp: String,
    val voltage : String,
    val intakeTemp : String,
    val throttlePosition: String,
    val dtcCodes : List<DtcInfo>
)
