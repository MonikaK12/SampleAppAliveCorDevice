package com.example.sampleappalivecordevice.devicecommunication

import com.biofourmis.careathomerpm.devicecommunication.BleDeviceType

data class NewDeviceVitalData(
    val deviceVitalData: DeviceVitalData,
    val deviceType: BleDeviceType
)

data class DeviceVitalData(
    var counter: Int? = null,
    var timestamp: Int? = null,
    var spO2: Float? = null,
    var spO2Quality: Float? = null,
    var hr: Float? = null,
    var hr_bp: Float? = null,
    var hrQuality: Float? = null,
    var bloodPerfusion: Float? = null,
    var classActivity: Float? = null,
    var classActivityQuality: Float? = null,
    var activity: Float? = null,
    var steps: Float? = null,
    var bloodPulseWave: Float? = null,
    var hrv: Float? = null,
    var hrvQuality: Float? = null,
    var respirationRate: Float? = null,
    var respirationRateQuality: Float? = null,
    var energy: Float? = null,
    var energyQuality: Float? = null,
    var localTemp: Float? = null,
    var objectTemp: Float? = null,
    var barometerTemp: Float? = null,
    var mbar: Float? = null,
    var gsrAmplitude: Float? = null,
    var bpSystolic: Float? = null,
    var bpDiastolic: Float? = null,
    var weight: Float? = null,
    var bloodGlucose: Float? = null,

    var totalSteps: Int? = null,
    var bodyTemp: Float? = null,
    var coreTemp: Float? = null
)

data class VitalToDisplay(
    var position: Int,
    var name: String? = null,
    var code: String? = null,
    var unit: String? = null,
    var measurementUnit: String? = null,
    var icon: String? = null,
    var deviceType: String? = null,
    var vitalType: String? = null,
    var lastUpdatedValue: Float? = null,
    var lastUpdatedValue2: Float? = null,
    var lastUpdatedTime: Long? = null,
    var isManualAvailable: Boolean? = null,
    var task: String? = null
)