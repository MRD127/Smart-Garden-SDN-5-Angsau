package com.example.smartgarden

object SensorData {
    var soilMoisture: Int? = null
    private val listeners = mutableListOf<(Int) -> Unit>()

    fun setSoilMoisture(value: Int) {
        soilMoisture = value
        listeners.forEach { it(value) }
    }

    fun observe(callback: (Int) -> Unit) {
        soilMoisture?.let { callback(it) }
        listeners.add(callback)
    }

    fun removeObservers() {
        listeners.clear()
    }
}
