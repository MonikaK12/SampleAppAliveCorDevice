package com.example.sampleappalivecordevice.devicecommunication

data class DeviceWeightData(
  var counter: Int? = null,
  var deviceId: String? = null,
  var deviceType: String? = null,
  var batteryLevel: String? = "0",
  var timestamp: Long,
  var weightUnit: String? = null,
  var heightUnit: String? = null,
  var weight: Float? = null,
  var height: Float? = null,
  var bmi: Float? = null,
  var bodyFatPercentage: Float? = null,
  var basalMetabolism: Float? = null,
  var musclePercentage: Float? = null,
  var muscleMass: Float? = null,
  var fatFreeMass: Float? = null,
  var softLeanMass: Float? = null,
  var bodyWaterMass: Float? = null,
  var impedance: Float? = null,
  var skeletalMusclePercentage: Float? = null,
  var visceralFatLevel: Float? = null,
  var bodyAge: Float? = null,
  var bodyFatPercentageStageEvaluation: Float? = null,
  var skeletalMusclePercentageStageEvaluation: Float? = null,
  var visceralFatLevelStageEvaluation: Float? = null
)