package com.example.obdapp.ui.language

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class LanguageViewModel : ViewModel() {

    val languages = listOf(
        Language("en", "English", "🇬🇧"),
        Language("fr", "Français", "🇫🇷"),
        Language("ar", "العربية", "🇹🇳")
    )

    var selectedLanguage by mutableStateOf<Language?>(null)
        private set

    fun selectLanguage(language: Language) {
        selectedLanguage = language
    }
}