package com.example.smartgarden

object SensorHumidity {
    var humidity: Float? = null
    private val listeners = mutableListOf<(Int) -> Unit>()


    fun setHumidity(value: Float) {
        listeners.forEach { it(value.toInt()) }
    }

    fun observe(callback: (Int) -> Unit) {
        humidity?.let { callback(it.toInt())}
        listeners.add(callback)
    }

    fun removeObservers() {
        listeners.clear()
    }

}
