package com.biofourmis.careathomerpm.devicecommunication

enum class BleDeviceState(var value: String) {
    SCAN_STARTED("scan_started"),
    SCAN_STOPPED("scan_stopped"),
    FOUND("found"),
    CONNECTION_START("connection_start"),
    CONNECTION_FAILED("connection_failed"),
    BATTERY_UPDATE("battery_update"),
    CONFIGURING_START("configuring_start"),
    CONFIGURING_STOP("configuring_stop"),
    CONNECTED("connect"),
    DISCONNECTED("disconnect"),
    HISTORIC_START("historic_start"),
    HISTORIC_END("historic_end"),
    KNOCK_DEVICE("knock_device"),
    CHARGING("charging"),
    NOT_APPLIED("not_applied"),
    UNPAIRED("unpaired")
}

enum class BleDeviceStatusCode(var value: String) {
    APP_DISCONNECTED("APP_DISCONNECTED"),
    DEVICE_ON_SKIN("DEVICE_ON_SKIN"),
    DEVICE_OFF_SKIN("DEVICE_OFF_SKIN"),
    DEVICE_POOR_SKIN("DEVICE_POOR_SKIN"),
    DEVICE_UNKNOWN_SKIN("DEVICE_UNKNOWN_SKIN"),
    BATTERY_UPDATE("BATTERY_UPDATE"),
    CHARGING("CHARGING"),
    NO_BLUETOOTH("NO_BLUETOOTH"),
    BLE_OUT_OF_RANGE("BLE_OUT_OF_RANGE"),
    MQTT_FAILURE("MQTT_FAILURE"),
    BLE_CONNECTED("BLE_CONNECTED"),
    BLE_DISCONNECTED("BLE_DISCONNECTED"),
}