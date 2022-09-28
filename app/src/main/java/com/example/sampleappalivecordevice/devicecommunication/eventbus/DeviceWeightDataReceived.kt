package com.example.sampleappalivecordevice.devicecommunication.eventbus

import com.biofourmis.careathomerpm.devicecommunication.BleDeviceType
import com.example.sampleappalivecordevice.devicecommunication.DeviceWeightData

class DeviceWeightDataReceived(
  val deviceWeightDatum: List<DeviceWeightData>,
  val deviceType: BleDeviceType
)