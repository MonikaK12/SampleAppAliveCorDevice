package com.example.sampleappalivecordevice.omron.listeners

import com.example.sampleappalivecordevice.devicecommunication.eventbus.DeviceBPDataReceived
import com.example.sampleappalivecordevice.devicecommunication.eventbus.DeviceWeightDataReceived

interface OmronDataSyncListener {

    fun onOmronBPDataSync(deviceBPDataReceived : DeviceBPDataReceived)
    fun onOmronWeightDataSync(deviceWeightDataReceived : DeviceWeightDataReceived)
}