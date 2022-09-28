package com.example.sampleappalivecordevice.devicecommunication.eventbus

import com.biofourmis.careathomerpm.devicecommunication.BleDeviceType
import com.example.sampleappalivecordevice.devicecommunication.DeviceBPData

class DeviceBPDataReceived (val deviceBPDatum: List<DeviceBPData>, val deviceType: BleDeviceType)