package com.example.obdapp.geminianalysis.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.obdapp.geminianalysis.model.AiDiagnosticResult
import com.example.obdapp.geminianalysis.model.ProDiagnosticReport
import com.example.obdapp.geminianalysis.service.ChatRequest
import com.example.obdapp.geminianalysis.service.LLMApi
import com.example.obdapp.geminianalysis.service.Message
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class LLMViewModel @Inject constructor(
    private val api: LLMApi
) : ViewModel() {

    private val _response = MutableStateFlow("Awaiting prompt...")
    val response: StateFlow<String> = _response

    var showDiagnosis = MutableStateFlow(false)

    private val _diagnosticFlow = MutableStateFlow<ProDiagnosticReport?>(null)
        //MutableStateFlow(
        //ProDiagnosticReport()
    //)


    val getLatestDiagnostic: StateFlow<ProDiagnosticReport?> = _diagnosticFlow.asStateFlow()

    fun clearReport() {
        _diagnosticFlow.value = null
    }
    private val mockJson = """
{
  "verdict": "NORMAL",
  "summary": "Vehicle operating parameters appear stable during light to moderate driving conditions. No abnormal behavior or fault indications detected across monitored systems.",
  "rawData": {
    "speed": "51 km/h",
    "rpm": "1500 RPM",
    "coolantTemp": "62 C",
    "voltage": "14 V",
    "intakeTemp": "16 C",
    "throttlePosition": "31.4%",
    "dtcCodes": []
  },
  "systemHealth": [
    {
      "system": "Cooling System",
      "status": "HEALTHY",
      "reason": "Coolant temperature indicates the engine is warming up normally and remains within a safe operating range."
    },
    {
      "system": "Electrical System",
      "status": "HEALTHY",
      "reason": "Charging voltage indicates the alternator and battery system are operating correctly."
    },
    {
      "system": "Air Intake System",
      "status": "HEALTHY",
      "reason": "Intake air temperature appears consistent with ambient conditions and indicates proper airflow measurement."
    },
    {
      "system": "Engine Load & Combustion",
      "status": "HEALTHY",
      "reason": "Engine speed and throttle position suggest normal engine load and efficient combustion during steady driving."
    },
    {
      "system": "Fault Codes",
      "status": "CLEAR",
      "reason": "No diagnostic trouble codes detected in the ECU."
    }
  ],
  "correlationAnalysis": "The relationship between engine speed, throttle input, and vehicle movement suggests a stable cruising condition. Electrical charging values remain consistent, while intake and cooling behavior indicate the engine is operating within expected thermal conditions.",
  "riskAssessment": {
    "shortTerm": "No immediate risk detected.",
    "mediumTerm": "Vehicle systems appear stable with no signs of developing issues.",
    "longTerm": "Continue routine maintenance and periodic diagnostic scans to ensure sustained system health."
  },
  "recommendations": [
    "Continue normal vehicle operation.",
    "Allow the engine to reach full operating temperature during regular driving cycles.",
    "Perform periodic diagnostic scans to monitor long-term vehicle health."
  ],
  "confidence": "High confidence",
  "scanQuality": "HIGH",
  "timestamp": "2026-03-05 18:45"
}
"""
    fun sendPrompt(prompt: String,) {
        viewModelScope.launch {
            try {
                val body = ChatRequest(messages = listOf(Message(content = prompt)))
                // ⏳ delay before navigating
                delay(2000)
                //val result =
                    //api.sendMessage(
                    //"Bearer $apiKey",
                    //LLMRequest(prompt)
                //)
                 val gson = Gson()

                // 2️⃣ Parse JSON returned by LLM
                val diagnostic =gson.fromJson(
                    mockJson,
                    ProDiagnosticReport::class.java
                )
                _diagnosticFlow.update { diagnostic }


                //showDiagnosis.value = true
                //showDiagnosis.emit(true)

                _response.value = mockJson
                    //result.response.orEmpty()
                _navigateToReport.emit(Unit)


            } catch (e: Exception) {
                showDiagnosis.value = false
                _response.value = "Error: ${e.localizedMessage}"
            }
            finally {
                dismissDiagnosis()
                //_diagnosticFlow.value = ProDiagnosticReport()
//                showDiagnosis.value = false
//
            }
        }


    }
    private val _navigateToReport = MutableSharedFlow<Unit>()
    val navigateToReport = _navigateToReport.asSharedFlow()
    fun setDiagnosticFlow(){
        _diagnosticFlow.value = ProDiagnosticReport()
    }

    fun setShowDiagnosis(value: Boolean) {
        showDiagnosis.value = value
    }
    private val _showDiagnosisEvent = MutableSharedFlow<Unit>()
    val showDiagnosisEvent = _showDiagnosisEvent.asSharedFlow()


    sealed class DiagnosisEvent {
        object Show : DiagnosisEvent()
        object Dismiss : DiagnosisEvent()
    }

    private val _diagnosisEvent = MutableSharedFlow<DiagnosisEvent>()
    val diagnosisEvent = _diagnosisEvent.asSharedFlow()

    fun triggerDiagnosis() {
        viewModelScope.launch { _diagnosisEvent.emit(DiagnosisEvent.Show) }
    }

    fun dismissDiagnosis() {
        viewModelScope.launch { _diagnosisEvent.emit(DiagnosisEvent.Dismiss) }
    }

    fun extractAfterKeyword(text: String, keyword: String): String? {
        val index = text.lowercase().indexOf(keyword.lowercase())
        return if (index != -1) {
            text.substring(index + keyword.length).trim()
        } else null
    }

}