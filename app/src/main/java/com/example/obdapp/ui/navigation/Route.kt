package com.example.obdapp.ui.navigation

sealed class Route(val name: String) {
    data object OnboardingScreen : Route("onboarding")
    data object OnboardingObdScreen : Route("onboardingobd")


    data object Language : Route("language")
    data object Home : Route("home")
    data object Engine : Route("dashboard")
    data object Sensors : Route("live")
    data object News : Route("logs")
    data object Dtc : Route("alerts")
    data object Units : Route("units")
    data object Bluetooth : Route("bluetooth")
    data object Report : Route("report")




}