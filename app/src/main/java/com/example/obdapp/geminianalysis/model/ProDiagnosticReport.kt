package com.example.obdapp.geminianalysis.model

import kotlinx.serialization.Serializable

@Serializable
data class ProDiagnosticReport(
    val verdict: String? = null,
    val summary: String? = null,
    val rawData: RawObdData? = null,
    val systemHealth: List<SystemHealth?>? = null,
    val correlationAnalysis: String? = null,
    val riskAssessment: RiskAssessment? = null,
    val recommendations: List<String?> = emptyList(),
    val confidence: String? = null,
    val scanQuality: String? = null,
    val timestamp:String? = null
)
@Serializable
data class DiagnosticResult(
    val verdict: String,
    val summary: String,
    val confidence: Int,
    val scanQuality: String,
    val timestamp: String
)
@Serializable
data class RawObdData(
    val speed: String,
    val rpm: String,
    val coolantTemp: String,
    val voltage: String,
    val intakeTemp: String,
    val throttlePosition: String,
    val dtcCodes: List<String>
)
@Serializable
data class SystemHealth(
    val system: String,
    val status: String,
    val reason: String
)

@Serializable
data class RiskAssessment(
    val shortTerm: String,
    val mediumTerm: String,
    val longTerm: String
)

