package com.example.obdapp.geminianalysis.model

data class AiDiagnosticResult(
    val status: String, // "NORMAL", "WARNING", "CRITICAL"
    val summary: String,
//    val speed: Int,
//    val rpm: Int,
//    val coolantTemp: Int,
    val explanationSimple: String,
    val explanationExpert: String,
    val recommendation: String,
    val confidence: Int
)