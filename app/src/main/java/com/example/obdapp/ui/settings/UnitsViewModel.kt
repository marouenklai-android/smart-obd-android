package com.example.obdapp.ui.settings

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UnitsViewModel : ViewModel() {

    private val _unitType = MutableStateFlow(UnitType.METRIC)
    val unitType: StateFlow<UnitType> = _unitType

    fun setUnit(type: UnitType) {
        _unitType.value = type
    }
}