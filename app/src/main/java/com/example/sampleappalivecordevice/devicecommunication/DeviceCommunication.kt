package com.example.sampleappalivecordevice.devicecommunication

import android.content.Context
import android.util.Log
import com.biofourmis.careathomerpm.devicecommunication.BleDeviceType
import com.biofourmis.careathomerpm.omron.OmronManager
import com.example.sampleappalivecordevice.devicecommunication.interfaces.BioBleDeviceConnectionListener
import com.example.sampleappalivecordevice.omron.listeners.OmronDataSyncListener
import com.example.sampleappalivecordevice.utils.constants.VitalDisplayType
import com.example.sampleappalivecordevice.utils.omron_2.AppPreffs
import javax.inject.Inject


class DeviceCommunication @Inject constructor(

) {

    private val context: Context? = null

    var isRegistration = false
    var supportedDevices = ArrayList<String>()

    fun initSDKManagers(deviceList: String, listener: BioBleDeviceConnectionListener) {
       // Logger.e("supportedDevices", " = " + Gson().toJson(supportedDevices))
        if (deviceList.isNullOrEmpty()) {

            if (supportedDevices.contains(VitalDisplayType.omronBp.name) || supportedDevices.contains(
                    VitalDisplayType.omronBcm.name
                )
            ) {
                //Omron
                OmronManager.getInstance()?.init(isRegistration, listener)
            }

        } else {
            Log.d("init", "init")
            OmronManager.getInstance()?.init(isRegistration, listener)
        }
    }

    /*fun setUpEverionDataListeners(dataSyncListener: DataSyncListener) {
        if (supportedDevices.contains(VitalDisplayType.everion.name)) {
            EverionManager.getInstance().setDataSyncListener(dataSyncListener)
        }
    }*/

    fun setUpOmronDataSyncListeners(omronDataSyncListener: OmronDataSyncListener) {
        if (supportedDevices.contains(VitalDisplayType.omronBp.name)) {
            OmronManager.getInstance()?.setOmronDataSyncListeners(omronDataSyncListener)
        }
    }


    fun startDeviceScan(deviceList: String) {
        if (deviceList.isNullOrEmpty()) {
            if (supportedDevices.contains(VitalDisplayType.omronBp.name)
            ) {
                //Omron
                OmronManager.getInstance()?.startScan()
            }

        } else {
            Log.d("start_scan", "init")
                //Omron
            OmronManager.getInstance()?.startScan()
        }
    }

    fun stopDeviceScan(deviceList: List<String>?) {
        if (deviceList.isNullOrEmpty()) {

            if (supportedDevices.contains(VitalDisplayType.omronBp.name) || supportedDevices.contains(
                    VitalDisplayType.omronBcm.name
                )
            ) {
                //Omron
                OmronManager.getInstance()?.stopScan()
            }

        } else {
            deviceList.forEach {
                when (it) {
                    BleDeviceType.BLE_DEVICE_OMRON_BP.name,
                    BleDeviceType.BLE_DEVICE_OMRON_WEIGHT.name,
                    BleDeviceType.BLE_DEVICE_OMRON_BCM.name -> {
                        if (supportedDevices.contains(VitalDisplayType.omronBp.name) || supportedDevices.contains(
                                VitalDisplayType.omronBcm.name
                            )
                        ) {
                            OmronManager.getInstance()?.stopScan()
                        }
                    }
                }
            }
        }
    }

    fun connectDevice(bioBleDevice: BioBleDevice) {
      //  Logger.e("supportedDevices", " = " + Gson().toJson(supportedDevices))
        when (bioBleDevice.deviceType) {
            BleDeviceType.BLE_DEVICE_OMRON_BP,
            BleDeviceType.BLE_DEVICE_OMRON_WEIGHT,
            BleDeviceType.BLE_DEVICE_OMRON_BCM -> {
                if (supportedDevices.contains(VitalDisplayType.omronBp.name) || supportedDevices.contains(
                        VitalDisplayType.omronBcm.name
                    )
                ) {
                    OmronManager.getInstance()?.registerWithDevice(bioBleDevice)
                }
            }
        }
    }

    fun disconnectDevice(deviceList: List<String>?) {
      //  Logger.e("supportedDevices", " = " + Gson().toJson(supportedDevices))
        if (deviceList.isNullOrEmpty()) {

            if (supportedDevices.contains(VitalDisplayType.omronBp.name) || supportedDevices.contains(
                    VitalDisplayType.omronBcm.name
                )
            ) {
                //Omron
                //stopOmronScan()
            }

        } else {
            deviceList.forEach {
                when (it) {
                    BleDeviceType.BLE_DEVICE_OMRON_BP.name,
                    BleDeviceType.BLE_DEVICE_OMRON_WEIGHT.name,
                    BleDeviceType.BLE_DEVICE_OMRON_BCM.name -> {
                        if (supportedDevices.contains(VitalDisplayType.omronBp.name) || supportedDevices.contains(
                                VitalDisplayType.omronBcm.name
                            )
                        ) {

                        }
                    }
                }
            }
        }
    }

    fun unPairDevice(bioBleDevice: BioBleDevice) {

        //Add device address to prefs.
        // This will be used to alert users if trying to pair to a previously unpaired device
        bioBleDevice.deviceAddress?.let { address ->
            AppPreffs.previouslyPairedDevices.add(address)
        }

        when (bioBleDevice.deviceType) {
            BleDeviceType.BLE_DEVICE_OMRON_BP,
            BleDeviceType.BLE_DEVICE_OMRON_WEIGHT,
            BleDeviceType.BLE_DEVICE_OMRON_BCM -> {
                if (supportedDevices.contains(VitalDisplayType.omronBp.name) || supportedDevices.contains(
                        VitalDisplayType.omronBcm.name
                    )
                ) {
                    OmronManager.getInstance()?.forgetDevice(bioBleDevice)
                }
            }
        }
    }

    fun unPairAllDevices() {
        if (supportedDevices.contains(VitalDisplayType.omronBp.name)) {
            OmronManager.getInstance()?.clearDB()
        }
    }

    /*fun manipulateHistoricVitalsDataSet(
        firstCounter: Int,
        lastCounter: Int,
        isSyncSuccess: Boolean
    ) {
        EverionManager.getInstance()
            .manipulateHistoricVitalsDataSet(firstCounter, lastCounter, isSyncSuccess)
    }

    fun manipulateLiveVitalsDataSet(firstCounter: Int, lastCounter: Int, isSyncSuccess: Boolean) {
        EverionManager.getInstance()
            .manipulateLiveVitalsDataSet(firstCounter, lastCounter, isSyncSuccess)
    }*/

    fun setBioBleDeviceConnectionListener(listener: BioBleDeviceConnectionListener) {
        if (supportedDevices.contains(VitalDisplayType.omronBp.name) ||
            supportedDevices.contains(VitalDisplayType.omronBcm.name) ||
            supportedDevices.contains(VitalDisplayType.omronWeight.name)
        ) {
            OmronManager.getInstance()?.setDeviceConnectionListener(listener)
        }
    }

    fun clearAllPreferences() {
        OmronManager.getInstance()?.clearDB()
    }

}