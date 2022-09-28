package com.example.sampleappalivecordevice.devicecommunication

data class DeviceBPData(
  var counter: Int? = null,
  var deviceId: String? = null,
  var deviceType: String? = null,
  var timestamp: Long,
  var systolic: Float? = null,
  var diastolic: Float? = null,
  var heartRate: Float? = null,
  var batteryLevel: String? = null,
  var bloodPressureMeasurementStatus: String? = null
)
