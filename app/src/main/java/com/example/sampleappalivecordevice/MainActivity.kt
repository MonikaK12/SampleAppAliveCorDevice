package com.example.sampleappalivecordevice

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import com.biofourmis.careathomerpm.devicecommunication.BleDeviceState
import com.biofourmis.careathomerpm.devicecommunication.BleDeviceType
import com.biofourmis.careathomerpm.omron.OmronManager
import com.example.sampleappalivecordevice.databinding.ActivityMainBinding
import com.example.sampleappalivecordevice.devicecommunication.*
import com.example.sampleappalivecordevice.devicecommunication.eventbus.DeviceBPDataReceived
import com.example.sampleappalivecordevice.devicecommunication.eventbus.DeviceWeightDataReceived
import com.example.sampleappalivecordevice.devicecommunication.interfaces.BioBleDeviceConnectionListener
import com.example.sampleappalivecordevice.omron.listeners.OmronDataSyncListener
import com.example.sampleappalivecordevice.omron.model.entity.UserInfo
import com.example.sampleappalivecordevice.omron.model.system.AppConfig
import com.example.sampleappalivecordevice.omron.model.system.ExportResultFileManager
import com.example.sampleappalivecordevice.omron.model.system.HistoryManager
import com.example.sampleappalivecordevice.utils.constants.VitalDisplayType
import com.example.sampleappalivecordevice.utils.omron_2.VitalMappingHelper
import com.google.gson.Gson
import io.realm.Realm
import io.realm.RealmConfiguration
import jp.co.ohq.ble.OHQDeviceManager
import jp.co.ohq.ble.enumerate.OHQGender
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import java.math.BigDecimal
import java.util.*

class MainActivity : AppCompatActivity(), BioBleDeviceConnectionListener, OmronDataSyncListener {

  private lateinit var appBarConfiguration: AppBarConfiguration
  private lateinit var binding: ActivityMainBinding

  var deviceCommunication: DeviceCommunication? =
    DeviceCommunication()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    if (deviceCommunication == null)
      deviceCommunication = DeviceCommunication()

    initOmronData()
    //AppConfig.init(this)
   // deviceCommunication?.unPairAllDevices()
    initSDKManagers(this)
    OmronManager.getInstance()?.applicationContext = applicationContext
  }

  override fun onBleDeviceConnectionStateChanged(bleDeviceStateChanged: BleDeviceStateChanged) {
   println(bleDeviceStateChanged.toString())
    val bioBleDevice = bleDeviceStateChanged.bioBleDevice
    CoroutineScope(Dispatchers.Main.immediate).launch {
      doOnDeviceStateChanged(bioBleDevice)
    }
  }

  private fun doOnDeviceStateChanged(bioBleDevice: BioBleDevice) {
    when (bioBleDevice.deviceState) {
      BleDeviceState.SCAN_STARTED -> {
        Log.d("STATE_CHANGE", "SCAN_STARTED = " + Gson().toJson(bioBleDevice))
        sendDeviceConnectionStatus(bioBleDevice)
      }
      BleDeviceState.SCAN_STOPPED -> {
        Log.d("STATE_CHANGE", "SCAN_STOPPED = " + Gson().toJson(bioBleDevice))
        sendDeviceConnectionStatus(bioBleDevice)
      }
      BleDeviceState.FOUND -> {
        Log.d("STATE_CHANGE", "FOUND = " + Gson().toJson(bioBleDevice))
        sendDeviceConnectionStatus(bioBleDevice)
      }
      BleDeviceState.CONFIGURING_START -> {
        Log.d("STATE_CHANGE", "CONFIGURING_START = " + Gson().toJson(bioBleDevice))
        sendDeviceConnectionStatus(bioBleDevice)
      }
      BleDeviceState.CONFIGURING_STOP -> {
        Log.d("STATE_CHANGE", "CONFIGURING_STOP = " + Gson().toJson(bioBleDevice))
        sendDeviceConnectionStatus(bioBleDevice)
      }
      BleDeviceState.CONNECTED -> {
        Log.d("STATE_CHANGE", "CONNECTED = " + Gson().toJson(bioBleDevice))
        bioBleDevice.isConnected = true
        savePairedDevice(bioBleDevice)
        sendDeviceConnectionStatus(bioBleDevice)
       /* updateDeviceDetailsToServer(
          bioBleDevice,
          "$trace -> connected(${bioBleDevice.deviceType}"
        )*/

       // checkIfPreviouslyUnpaired(bioBleDevice)
      }
      BleDeviceState.DISCONNECTED -> {
        Log.d("STATE_CHANGE", "DISCONNECTED = " + Gson().toJson(bioBleDevice))
        bioBleDevice.isConnected = false
        savePairedDevice(bioBleDevice)
        sendDeviceConnectionStatus(bioBleDevice)
       /* updateDeviceDetailsToServer(
          bioBleDevice,
          "$trace -> disconnected(${bioBleDevice.deviceType}"
        )*/
      }
      BleDeviceState.HISTORIC_START -> {
        Log.d("STATE_CHANGE", "HISTORIC_START = " + Gson().toJson(bioBleDevice))
        sendDeviceConnectionStatus(bioBleDevice)
      }
      BleDeviceState.HISTORIC_END -> {
        Log.d("STATE_CHANGE", "HISTORIC_END = " + Gson().toJson(bioBleDevice))
        sendDeviceConnectionStatus(bioBleDevice)
      }
      BleDeviceState.CHARGING -> {
        Log.d("STATE_CHANGE", "CHARGING = " + Gson().toJson(bioBleDevice))
        sendDeviceConnectionStatus(bioBleDevice)
      }
      BleDeviceState.NOT_APPLIED -> {
        Log.d("STATE_CHANGE", "NOT_APPLIED = " + Gson().toJson(bioBleDevice))
        bioBleDevice.isConnected = true
        savePairedDevice(bioBleDevice)
        sendDeviceConnectionStatus(bioBleDevice)

        // Specifically for VitalPatch
       // checkIfPreviouslyUnpaired(bioBleDevice)
      }
      else -> {
        Log.d("CHECK_FLOW", "False")
      }
    }
  }

  private fun sendDeviceConnectionStatus(bioBleDevice: BioBleDevice) {
    Log.d("CHECK_FLOW", "CONNECTED")
    /*CoroutineScope(Dispatchers.Main.immediate).launch {
      doOnDeviceStateChanged(bioBleDevice)
    }*/
  }

  fun savePairedDevice(bioBleDevice: BioBleDevice) {
      PairedBleDevice(
        deviceSerialNum = bioBleDevice.deviceSerialNum,
        deviceName = bioBleDevice.deviceName,
        deviceAddress = bioBleDevice.deviceAddress,
        deviceType = bioBleDevice.deviceType,
        deviceState = bioBleDevice.deviceState,
        deviceBattery = bioBleDevice.deviceBattery,
        isSelected = bioBleDevice.isSelected,
        isConnected = bioBleDevice.isConnected,
        isUnpairShown = bioBleDevice.isUnpairShown,
        code = bioBleDevice.code
      )
    Log.d("SaveDevice", "Save (HHMainViewModel) BioBleDevice " + Gson().toJson(bioBleDevice))
  }


  fun initSDKManagers(listener: BioBleDeviceConnectionListener) {
    deviceCommunication?.isRegistration = false
    deviceCommunication?.initSDKManagers(VitalDisplayType.omronBp.name, this)
    deviceCommunication?.startDeviceScan(VitalDisplayType.omronBp.name)
    deviceCommunication?.setUpOmronDataSyncListeners(this)
  }

  private fun initOmronData() {
    Realm.init(applicationContext)
    val config = RealmConfiguration.Builder()
      .allowWritesOnUiThread(true)
      .build()
    Realm.setDefaultConfiguration(config)

    AppConfig.init(applicationContext)

    HistoryManager.init(applicationContext)

    ExportResultFileManager.init(applicationContext)

    OHQDeviceManager.init(applicationContext)

    val realm = Realm.getDefaultInstance()
    if (0 == realm.where(UserInfo::class.java).findAll().size) {
      registerPresetUsers(realm)
    }
    realm.where(UserInfo::class.java).findFirst()?.name?.let {
      AppConfig.sharedInstance().nameOfCurrentUser = it
    }
    realm.close()
  }


  private fun registerPresetUsers(realm: Realm): List<UserInfo>? {
    val userInfoList: MutableList<UserInfo> =
      LinkedList()
    var userInfo = UserInfo()
    userInfo.name = "com.biofourmis.careathomerpm.data.model.fcm.User A"
    userInfo.dateOfBirth = "2000-04-01"
    userInfo.height = BigDecimal("180.1")
    userInfo.gender = OHQGender.Male
    userInfoList.add(userInfo)

    userInfo = UserInfo()
    userInfo.name = "com.biofourmis.careathomerpm.data.model.fcm.User B"
    userInfo.dateOfBirth = "2001-05-01"
    userInfo.height = BigDecimal("175.2")
    userInfo.gender = OHQGender.Male
    userInfoList.add(userInfo)

    userInfo = UserInfo()
    userInfo.name = "com.biofourmis.careathomerpm.data.model.fcm.User C"
    userInfo.dateOfBirth = "2002-06-01"
    userInfo.height = BigDecimal("170.3")
    userInfo.gender = OHQGender.Female
    userInfoList.add(userInfo)

    userInfo = UserInfo()
    userInfo.name = "com.biofourmis.careathomerpm.data.model.fcm.User D"
    userInfo.dateOfBirth = "2003-07-01"
    userInfo.height = BigDecimal("165.4")
    userInfo.gender = OHQGender.Female
    userInfoList.add(userInfo)

    realm.executeTransaction { realmDB ->
      for (userInfoItem in userInfoList) {
        realmDB.copyToRealm(userInfoItem)
      }
    }
    return userInfoList
  }

  override fun onOmronBPDataSync(deviceBPDataReceived: DeviceBPDataReceived) {
    Log.d("BPUPLOADCHECK", "received")
    CoroutineScope(Dispatchers.IO).launch {
      if (deviceBPDataReceived.deviceBPDatum.isNotEmpty()) {
        val lastIndex = deviceBPDataReceived.deviceBPDatum.size - 1
        saveLastUpdatedData(
          DeviceVitalData(
            bpDiastolic = deviceBPDataReceived.deviceBPDatum[lastIndex].diastolic,
            bpSystolic = deviceBPDataReceived.deviceBPDatum[lastIndex].systolic,
            hr_bp = deviceBPDataReceived.deviceBPDatum[lastIndex].heartRate,
            timestamp = deviceBPDataReceived.deviceBPDatum[lastIndex].timestamp.toInt()
          ), deviceBPDataReceived.deviceType
        )
      }
    }
  }

  override fun onOmronWeightDataSync(deviceWeightDataReceived: DeviceWeightDataReceived) {
    Log.d("WEIGHT_UPLOADCHECK", "received")
  }

  private fun saveLastUpdatedData(
    deviceVitalData: DeviceVitalData,
    deviceType: BleDeviceType
  ) {
    val vitalsToDisplayList = ArrayList<VitalToDisplay>()
    vitalsToDisplayList.clear()
    //vitalsToDisplayList.addAll(hhPreferenceRepo.getVitalsToDisplay())
    val mappedVitalsToDisplay = VitalMappingHelper.mapLastUpdatedDataToVitals(
      deviceVitalData,
      deviceType,
      vitalsToDisplayList
    )
   // hhPreferenceRepo.saveVitalsToDisplay(mappedVitalsToDisplay)
  }
}