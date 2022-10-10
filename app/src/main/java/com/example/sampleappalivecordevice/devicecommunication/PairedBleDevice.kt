package com.example.sampleappalivecordevice.devicecommunication

import com.biofourmis.careathomerpm.devicecommunication.BleDeviceState
import com.biofourmis.careathomerpm.devicecommunication.BleDeviceStatusCode
import com.biofourmis.careathomerpm.devicecommunication.BleDeviceType

data class PairedBleDevice(
    var deviceSerialNum: String? = null,
    var deviceName: String? = null,
    var deviceAddress: String? = null,
    var deviceType: BleDeviceType? = null,
    var deviceState: BleDeviceState? = null,
    var deviceBattery: Int? = null,
    var isSelected: Boolean = false,
    var isConnected: Boolean = false,
    var isUnpairShown: Boolean = false,
    var code: BleDeviceStatusCode? = null,
    var lastUpdatedTime: Long? = null
)