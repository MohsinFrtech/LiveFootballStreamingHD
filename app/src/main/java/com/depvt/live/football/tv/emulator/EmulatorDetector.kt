package com.depvt.live.football.tv.emulator

import android.content.Context
import android.hardware.Sensor
import com.depvt.live.football.tv.emulator.property.PropertiesEmulatorDetector
import com.depvt.live.football.tv.emulator.sensor.SensorEmulatorDetector

abstract class EmulatorDetector {

    abstract suspend fun getState(): DeviceState

    class Builder(
        private val context: Context,
    ) {
        companion object {
            private const val ACCELEROMETER_EVENTS_COUNT = 10
            private const val GYROSCOPE_EVENTS_COUNT = 10
        }

        private val emulators = mutableListOf<EmulatorDetector>()

        fun checkSensors() = this.apply {
            emulators.add(
                SensorEmulatorDetector(
                    context,
                    Sensor.TYPE_ACCELEROMETER,
                    ACCELEROMETER_EVENTS_COUNT,
                )
            )
            emulators.add(
                SensorEmulatorDetector(
                    context,
                    Sensor.TYPE_GYROSCOPE,
                    GYROSCOPE_EVENTS_COUNT,
                )
            )
        }

        fun checkProperties() = this.apply {
            emulators.add(PropertiesEmulatorDetector())
        }

        fun build(): EmulatorDetector = ComplexEmulatorDetector(emulators)
    }
}