package com.example.myapplication

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

interface StepListener {
    fun onStep(count: Int)
}

class Krokomierz (private val context: Context, private val stepListener: StepListener) :
    SensorEventListener {

    var dailyGoal: Int = 10000
    private var sensorManager: SensorManager? = null
    private var accelerometer: Sensor? = null

    private var lastValues = DoubleArray(3)
    private var counter: Int = 0

    init {
        initializeSensors()
    }

    private fun initializeSensors() {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    fun start() {
        sensorManager?.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
    }

    fun stop() {
        sensorManager?.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor !=null && event?.sensor == accelerometer) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            val acceleration = Math.sqrt((x * x + y * y + z * z).toDouble())
            val delta = acceleration - lastValues[0]
            lastValues[0] = acceleration

            if (delta > 6) { // do konfiguracji
                counter++
                stepListener.onStep(counter)
            }
        }
    }
}