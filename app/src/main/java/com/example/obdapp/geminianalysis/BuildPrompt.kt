package com.example.obdapp.geminianalysis

fun buildPrompt(obd: ObdData): String {
    return """
    I have vehicle data:
    Speed: ${obd.speed} km/h
    RPM: ${obd.rpm}
    Coolant temperature: ${obd.coolantTemp} °C
    
    Analyze the situation and tell me if there is a problem and what I should do.
    Answer in simple language.
    """.trimIndent()
}
fun buildPromptLLM(obd: ObdData): String {
    return """
    You are an automotive diagnostic AI.
    Analyze the following OBD data:
    Speed: ${obd.speed} 
    RPM: ${obd.rpm}
    Coolant temperature: ${obd.coolantTemp} 
    voltage: ${obd.voltage} 
    intakeTemp: ${obd.intakeTemp}
    throttlePosition : ${obd.throttlePosition} 
    dtcCodes : ${obd.dtcCodes} 

    Return ONLY valid JSON with this structure:
        {
          "status": "NORMAL | WARNING | CRITICAL",
          "summary": "",
          "speed": number,
          "rpm": number,
          "coolantTemp": number,
          "explanationSimple": "",
          "explanationExpert": "",
          "recommendation": "",
          "confidence": number (0-100)
        }

    Rules:
    - Output ONLY JSON, no markdown, no commentary.
    - Use \\n inside the recommendation field for multiple steps.
    - Do not include speed, rpm or coolantTemp unless explicitly provided.
    - status must be one of: NORMAL, WARNING, CRITICAL.
    
    """.trimIndent()
}
fun buildPromptLLMReport(obd: ObdData): String {
    return """
    You are an automotive diagnostic expert.

    Analyze the following OBD data:
    
    Speed: ${obd.speed}
    RPM: ${obd.rpm}
    Coolant temperature: ${obd.coolantTemp}
    Voltage: ${obd.voltage}
    Intake temperature: ${obd.intakeTemp}
    Throttle position: ${obd.throttlePosition}
    DTC codes: ${obd.dtcCodes}
    
    Return ONLY valid JSON with this structure:
    
    {
      "verdict": "NORMAL | WARNING | CRITICAL",
      "summary": "",
    
      "rawData": {
        "speed": string,
        "rpm": string,
        "coolantTemp": string,
        "voltage": string,
        "intakeTemp": string,
        "throttlePosition": string,
        "dtcCodes": List<String>
      },
    
      "systemHealth": [
        {
          "system": "Cooling System",
          "status": "HEALTHY | WARNING | CRITICAL",
          "reason": ""
        },
        {
          "system": "Electrical System",
          "status": "HEALTHY | WARNING | CRITICAL",
          "reason": ""
        },
        {
          "system": "Air Intake System",
          "status": "HEALTHY | WARNING | CRITICAL",
          "reason": ""
        },
        {
          "system": "Engine Load & Combustion",
          "status": "HEALTHY | WARNING | CRITICAL",
          "reason": ""
        },
        {
          "system": "Fault Codes",
          "status": "CLEAR | PRESENT",
          "reason": ""
        }
      ],
    
      "correlationAnalysis": "",
    
      "riskAssessment": {
        "shortTerm": "",
        "mediumTerm": "",
        "longTerm": ""
      },
    
      "recommendations": [
        ""
      ],
    
      "confidence": string,
      "scanQuality": "HIGH | MEDIUM | LOW",
      "timestamp": "YYYY-MM-DD HH:MM"
    }
    
    Rules:
    - Return ONLY valid JSON.
    - Do not add markdown.
    - Do not add explanations outside JSON.
    - Do not repeat raw numeric values in summary or analysis.
    - Interpret the data like a professional mechanic.
    - If dtcCodes is empty, set Fault Codes status to CLEAR.
    - Do not scare the user if values are normal.
    
    """.trimIndent()
}