package com.example.sampleappalivecordevice

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import com.biofourmis.careathomerpm.devicecommunication.BleDeviceType
import com.biofourmis.careathomerpm.omron.OmronManager
import com.example.sampleappalivecordevice.databinding.ActivityMainBinding
import com.example.sampleappalivecordevice.devicecommunication.BleDeviceStateChanged
import com.example.sampleappalivecordevice.devicecommunication.DeviceCommunication
import com.example.sampleappalivecordevice.devicecommunication.eventbus.DeviceBPDataReceived
import com.example.sampleappalivecordevice.devicecommunication.eventbus.DeviceWeightDataReceived
import com.example.sampleappalivecordevice.devicecommunication.interfaces.BioBleDeviceConnectionListener
import com.example.sampleappalivecordevice.omron.listeners.OmronDataSyncListener
import com.example.sampleappalivecordevice.omron.model.entity.UserInfo
import com.example.sampleappalivecordevice.omron.model.system.AppConfig
import com.example.sampleappalivecordevice.omron.model.system.ExportResultFileManager
import com.example.sampleappalivecordevice.omron.model.system.HistoryManager
import com.example.sampleappalivecordevice.utils.constants.VitalDisplayType
import io.realm.Realm
import io.realm.RealmConfiguration
import jp.co.ohq.ble.OHQDeviceManager
import jp.co.ohq.ble.enumerate.OHQGender
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
    initSDKManagers(this)
  }

  override fun onBleDeviceConnectionStateChanged(bleDeviceStateChanged: BleDeviceStateChanged) {
   println(bleDeviceStateChanged.toString())
  }

  fun initSDKManagers(listener: BioBleDeviceConnectionListener) {
    deviceCommunication?.isRegistration = true
   // OmronManager.getInstance()?.init(this, true, listener)
    deviceCommunication?.initSDKManagers(VitalDisplayType.omronBp.name, this)
    setUpDataListeners()
    deviceCommunication?.startDeviceScan(VitalDisplayType.omronBp.name)
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

  private fun setUpDataListeners() {
    deviceCommunication?.setUpOmronDataSyncListeners(this)
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
  }

  override fun onOmronWeightDataSync(deviceWeightDataReceived: DeviceWeightDataReceived) {
    Log.d("WEIGHT_UPLOADCHECK", "received")
  }
}