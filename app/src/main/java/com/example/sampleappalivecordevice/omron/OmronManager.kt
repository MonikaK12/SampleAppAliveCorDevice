package com.biofourmis.careathomerpm.omron

import android.content.Context
import android.os.Bundle
import com.biofourmis.careathomerpm.devicecommunication.BleDeviceState
import com.biofourmis.careathomerpm.devicecommunication.BleDeviceType
import com.biofourmis.careathomerpm.utils.Utils
import com.example.sampleappalivecordevice.devicecommunication.BioBleDevice
import com.example.sampleappalivecordevice.devicecommunication.BleDeviceStateChanged
import com.example.sampleappalivecordevice.devicecommunication.DeviceBPData
import com.example.sampleappalivecordevice.devicecommunication.DeviceWeightData
import com.example.sampleappalivecordevice.devicecommunication.eventbus.DeviceBPDataReceived
import com.example.sampleappalivecordevice.devicecommunication.eventbus.DeviceWeightDataReceived
import com.example.sampleappalivecordevice.devicecommunication.interfaces.BioBleDeviceConnectionListener
import com.example.sampleappalivecordevice.omron.controller.BluetoothPowerController
import com.example.sampleappalivecordevice.omron.controller.ScanController
import com.example.sampleappalivecordevice.omron.controller.SessionController
import com.example.sampleappalivecordevice.omron.controller.util.AppLog
import com.example.sampleappalivecordevice.omron.controller.util.Common
import com.example.sampleappalivecordevice.omron.listeners.OmronDataSyncListener
import com.example.sampleappalivecordevice.omron.model.entity.*
import com.example.sampleappalivecordevice.omron.model.enumerate.ComType
import com.example.sampleappalivecordevice.omron.model.enumerate.Protocol
import com.example.sampleappalivecordevice.omron.model.enumerate.ResultType
import com.example.sampleappalivecordevice.omron.model.system.AppConfig
import com.example.sampleappalivecordevice.omron.model.system.LoggingManager
import com.example.sampleappalivecordevice.utils.DateTimeUtils
import com.example.sampleappalivecordevice.utils.omron_2.VitalMappingHelper
import com.google.gson.Gson
import io.realm.Realm
import io.realm.RealmList
import jp.co.ohq.androidcorebluetooth.AndroidPeripheral
import jp.co.ohq.androidcorebluetooth.CBConfig
import jp.co.ohq.ble.OHQDeviceManager.DebugMonitor
import jp.co.ohq.ble.enumerate.*
import jp.co.ohq.utility.Handler
import jp.co.ohq.utility.Types
import okhttp3.internal.toImmutableList
import org.greenrobot.eventbus.EventBus
import java.math.BigDecimal
import java.util.*


class OmronManager :
    BluetoothPowerController.Listener,
    SessionController.Listener,
    ScanController.Listener,
    DebugMonitor {

    private enum class Mode {
        Normal, UnregisteredUser
    }


    private var isForRegistration: Boolean = false
    private val CONSENT_CODE_OHQ = 0x020E
    private val CONSENT_CODE_UNREGISTERED_USER = 0x0000
    private val USER_INDEX_UNREGISTERED_USER = 0xFF
    private val CONNECTION_WAIT_TIME_TRANSFER: Long = 10 * 1000
    private val CONNECTION_WAIT_TIME_REGISTRATION: Long = 15 * 1000

    //private var mContext: Context? = null
    private var mScanController: ScanController? = null
    private var mSessionController: SessionController? = null
    private val mDiscoveredDevices = ArrayList<DiscoveredDevice>()

    private var mHistoryData: HistoryData = HistoryData()
    private var mMode: Mode = Mode.Normal
    private var mCurrentUserName = AppConfig.sharedInstance().nameOfCurrentUser
    private val mLoggingManager = LoggingManager()
    var mIsOnlyPairingMode = false
    private var scanCount = 0
    private var mCurrentConnectionIndex = 0
    private var mStopConnection = false
    private var devicesToFetchData = ArrayList<String>()

    private var mConnectedDevice: DeviceInfo? = null
    private var mSessionOption: HashMap<OHQSessionOptionKey, Any> = HashMap()
    private var mIsConnectionInProgress: Boolean = false
    private var mOmronDataSyncListener: OmronDataSyncListener? = null
    private var bioBleDeviceConnectionListener: BioBleDeviceConnectionListener? = null

    private var mUserRegisteredDeviceAddresses = mutableSetOf<String>()
    private var mRealM: Realm? = null
    private var mIsInitiated = false

    private val applicationContext: Context? = null


    companion object {
        private const val TAG = "OmronManagerMain"

        private var mInstance: OmronManager? = null
        fun getInstance(): OmronManager? {
            if (mInstance == null)
                mInstance = OmronManager()
            return mInstance
        }
    }

    override fun onBluetoothStateChanged(enable: Boolean) {
        if (enable) {
            //startSession()
        }
    }

    override fun onConnectionStateChanged(connectionState: OHQConnectionState) {
        if (OHQConnectionState.Connected == connectionState) {
//            mCancelButton.setEnabled(false)
//            getActivity().setTitle(getString(R.string.processing).toUpperCase())
        }
    }

    override fun onSessionComplete(sessionData: SessionData) {

        val sessionResult: ResultType =
            validateSessionWithData(mHistoryData.protocol, sessionData)
        mHistoryData.resultType = sessionResult
        mHistoryData.receivedDate = System.currentTimeMillis()
        mHistoryData.userName = mCurrentUserName
        mHistoryData.deviceCategory = sessionData.deviceCategory
        mHistoryData.modelName = sessionData.modelName
        mHistoryData.currentTime = sessionData.currentTime
        mHistoryData.batteryLevel = sessionData.batteryLevel
        mHistoryData.userIndex = sessionData.userIndex
        mHistoryData.setUserData(sessionData.userData)
        mHistoryData.databaseChangeIncrement = sessionData.databaseChangeIncrement
        mHistoryData.sequenceNumberOfLatestRecord = sessionData.sequenceNumberOfLatestRecord
        mHistoryData.setMeasurementRecords(sessionData.measurementRecords)
        mHistoryData.completionReason = sessionData.completionReason
        if (null != mHistoryData.userIndex) {
            val consentCode =
                Types.autoCast<Int>(mSessionOption[OHQSessionOptionKey.ConsentCodeKey])
            mHistoryData.consentCode = consentCode
        }
        AppLog.i(mHistoryData.toString())

        if (Mode.Normal == mMode && ResultType.Success === sessionResult) {
            updateUserInfo(mHistoryData)
        }

        val handler = Handler()
        mLoggingManager.stop(object : LoggingManager.ActionListener {
            override fun onSuccess() {
                onFinished()
            }

            override fun onFailure() {
                AppLog.vMethodIn()
                onFinished()
            }

            private fun onFinished() {

                handler.post {

                    if (ResultType.Success == sessionResult) {
                        val deviceAddress =
                            if (devicesToFetchData.size > 0) devicesToFetchData[0] else ""
                        onHistoryDataReceived(mHistoryData, deviceAddress)

                        val bioBleDevice = BioBleDevice()
                        bioBleDevice.deviceSerialNum = mConnectedDevice?.address
                        bioBleDevice.deviceAddress = mConnectedDevice?.address
                        bioBleDevice.deviceName = sessionData.modelName
                        bioBleDevice.deviceBattery = sessionData.batteryLevel
                        bioBleDevice.deviceState =
                            if (mIsOnlyPairingMode) BleDeviceState.CONNECTED else BleDeviceState.DISCONNECTED

                        when (sessionData.deviceCategory) {
                            OHQDeviceCategory.Unknown -> {

                            }
                            OHQDeviceCategory.BloodPressureMonitor -> {
                                bioBleDevice.deviceType = BleDeviceType.BLE_DEVICE_OMRON_BP
                                bioBleDeviceConnectionListener?.onBleDeviceConnectionStateChanged(
                                    BleDeviceStateChanged(bioBleDevice)
                                )
                                /*EventBus.getDefault()
                                    .postSticky(BleDeviceStateChanged(bioBleDevice))*/
                            }
                            OHQDeviceCategory.WeightScale -> {
                                bioBleDevice.deviceType = BleDeviceType.BLE_DEVICE_OMRON_WEIGHT
                                bioBleDeviceConnectionListener?.onBleDeviceConnectionStateChanged(
                                    BleDeviceStateChanged(bioBleDevice)
                                )
                                /*EventBus.getDefault()
                                    .postSticky(BleDeviceStateChanged(bioBleDevice))*/
                            }
                            OHQDeviceCategory.BodyCompositionMonitor -> {
                                bioBleDevice.deviceType = BleDeviceType.BLE_DEVICE_OMRON_BCM
                                bioBleDeviceConnectionListener?.onBleDeviceConnectionStateChanged(
                                    BleDeviceStateChanged(bioBleDevice)
                                )
                                /*EventBus.getDefault()
                                    .postSticky(BleDeviceStateChanged(bioBleDevice))*/
                            }
                            null -> {

                            }
                        }

                    } else {
                        if (devicesToFetchData.size > 0 && mIsOnlyPairingMode) {
                            val bioBleDevice = BioBleDevice()
                            bioBleDevice.deviceSerialNum = mConnectedDevice?.address
                            bioBleDevice.deviceAddress = mConnectedDevice?.address
                            bioBleDevice.deviceName = sessionData.modelName
                            bioBleDevice.deviceBattery = sessionData.batteryLevel
                            bioBleDevice.deviceState = BleDeviceState.CONNECTED

                            // TODO: 08/03/21 Fix for HOHP-1087, revert if any errors occur
                            //when (sessionData.deviceCategory) {
                            when (mConnectedDevice?.deviceCategory) {
                                OHQDeviceCategory.Unknown -> {

                                }
                                OHQDeviceCategory.BloodPressureMonitor -> {
                                    bioBleDevice.deviceType = BleDeviceType.BLE_DEVICE_OMRON_BP
                                    bioBleDeviceConnectionListener?.onBleDeviceConnectionStateChanged(
                                        BleDeviceStateChanged(bioBleDevice)
                                    )
                                    /*EventBus.getDefault()
                                        .postSticky(BleDeviceStateChanged(bioBleDevice))*/
                                }
                                OHQDeviceCategory.WeightScale -> {
                                    bioBleDevice.deviceType = BleDeviceType.BLE_DEVICE_OMRON_WEIGHT
                                    bioBleDeviceConnectionListener?.onBleDeviceConnectionStateChanged(
                                        BleDeviceStateChanged(bioBleDevice)
                                    )
                                    /*EventBus.getDefault()
                                        .postSticky(BleDeviceStateChanged(bioBleDevice))*/
                                }
                                OHQDeviceCategory.BodyCompositionMonitor -> {
                                    bioBleDevice.deviceType = BleDeviceType.BLE_DEVICE_OMRON_BCM
                                    bioBleDeviceConnectionListener?.onBleDeviceConnectionStateChanged(
                                        BleDeviceStateChanged(bioBleDevice)
                                    )
                                    /*EventBus.getDefault()
                                        .postSticky(BleDeviceStateChanged(bioBleDevice))*/
                                }
                                null -> {

                                }
                            }
                        }
                    }

                    if (!mIsOnlyPairingMode && sessionData.completionReason != OHQCompletionReason.PoweredOff && !mStopConnection && devicesToFetchData.isNotEmpty()) {
                        connectNextDevice()
                    }
                }
            }
        })
    }

    override fun onBondStateChanged(bondState: AndroidPeripheral.BondState) {

    }

    override fun onDetailedStateChanged(newState: OHQDetailedState) {

    }

    override fun onGattConnectionStateChanged(gattConnectionState: AndroidPeripheral.GattConnectionState) {

    }

    override fun onPairingRequest() {

    }

    override fun onAclConnectionStateChanged(aclConnectionState: AndroidPeripheral.AclConnectionState) {

    }

    override fun onScanCompletion(reason: OHQCompletionReason) {
       // Logger.d("onScanCompletion1", " = " + reason.name)
    }

    override fun onScan(discoveredDevices: List<DiscoveredDevice>) {
        val deviceList: MutableList<DiscoveredDevice> = LinkedList()

       // Logger.d("DATA_CHECK", "device list size" + discoveredDevices.size)
        if (mIsOnlyPairingMode) {
            for (device in discoveredDevices) {
                val eachUserData = device.eachUserData
                if (null == eachUserData) {
                  //  Logger.d("AddDevice", "eachUserData is null")
                    deviceList.add(device)
                } else if (eachUserData.isPairingMode) {
                    deviceList.add(device)
                }
            }

            deviceList.forEach { device ->
                val isAvailable = mDiscoveredDevices.any {
                    it.address == device.address
                }
                if (!isAvailable) {
                    mDiscoveredDevices.add(device)

                    val bioBleDevice = BioBleDevice()
                    bioBleDevice.sdkLevelDevice = device
                    bioBleDevice.deviceSerialNum = device.localName
                    bioBleDevice.deviceAddress = device.address
                    bioBleDevice.deviceName = device.modelName ?: device.deviceCategory?.name
                    bioBleDevice.deviceState = BleDeviceState.FOUND

                    when (device.deviceCategory) {
                        OHQDeviceCategory.Unknown -> {

                        }
                        OHQDeviceCategory.BloodPressureMonitor -> {
                            bioBleDevice.deviceType = BleDeviceType.BLE_DEVICE_OMRON_BP
                            bioBleDeviceConnectionListener?.onBleDeviceConnectionStateChanged(
                                BleDeviceStateChanged(bioBleDevice)
                            )
                            //EventBus.getDefault().postSticky(BleDeviceStateChanged(bioBleDevice))
                        }
                        OHQDeviceCategory.WeightScale -> {
                            bioBleDevice.deviceType = BleDeviceType.BLE_DEVICE_OMRON_WEIGHT
                            bioBleDeviceConnectionListener?.onBleDeviceConnectionStateChanged(
                                BleDeviceStateChanged(bioBleDevice)
                            )
                            //EventBus.getDefault().postSticky(BleDeviceStateChanged(bioBleDevice))
                        }
                        OHQDeviceCategory.BodyCompositionMonitor -> {
                            bioBleDevice.deviceType = BleDeviceType.BLE_DEVICE_OMRON_BCM
                            bioBleDeviceConnectionListener?.onBleDeviceConnectionStateChanged(
                                BleDeviceStateChanged(bioBleDevice)
                            )
                            //EventBus.getDefault().postSticky(BleDeviceStateChanged(bioBleDevice))
                        }
                        null -> {

                        }
                    }
                }
            }

        }
    }

    fun init(context: Context, isRegistration: Boolean, listener: BioBleDeviceConnectionListener) {
        isForRegistration = isRegistration
        if (mIsInitiated)
            return
        //mContext = context
        mScanController = ScanController(this)
        mSessionController = SessionController(this, this)

        mCurrentUserName = AppConfig.sharedInstance().nameOfCurrentUser
        if (mRealM == null)
            mRealM = Realm.getDefaultInstance()

        //val realm = Realm.getDefaultInstance()
        val userInfo: UserInfo? = mRealM?.where(UserInfo::class.java)?.equalTo(
            "name", mCurrentUserName
        )?.findFirst()
        val registeredDevices: RealmList<DeviceInfo>? = userInfo?.registeredDevices
        val results = registeredDevices?.toImmutableList()

        mUserRegisteredDeviceAddresses.clear()
        results?.forEach { device ->
            device?.address?.let {
                mUserRegisteredDeviceAddresses.add(device.address)
            }
        }
        mIsInitiated = true
        //realm.close()
        setDeviceConnectionListener(listener)
    }

    fun setOmronDataSyncListeners(omronDataSyncListener: OmronDataSyncListener) {
        mOmronDataSyncListener = omronDataSyncListener
    }

    fun setDeviceConnectionListener(listener: BioBleDeviceConnectionListener?) {
        bioBleDeviceConnectionListener = listener
    }

    fun startScan() {
        mIsOnlyPairingMode = true
        mDiscoveredDevices.clear()
        mStopConnection = true
        devicesToFetchData.clear()
        mSessionController?.cancel()
        mScanController?.stopScan()

        if (isForRegistration) {
            mScanController?.startScan()
        } else {
            startContinuousConnection()
        }
    }

    fun startContinuousConnection() {
        mIsOnlyPairingMode = false
        mStopConnection = false
        mCurrentConnectionIndex = 0
        mUserRegisteredDeviceAddresses.forEach {
            devicesToFetchData.add(it)
        }
        if (devicesToFetchData.size > 0)
            connectToTransfer(devicesToFetchData[mCurrentConnectionIndex])
    }


    fun stopScan() {
        mScanController?.stopScan()
        mDiscoveredDevices.clear()
//        sendCountlyEvent(
//            KEY_EVENT_DEVICE_SCAN_STOP,
//            "NA",
//            "OMRON",
//            "Stop scan from device communication"
//        )

        stopConnection()
    }

    fun stopConnection() {
        mCurrentConnectionIndex = 0
        mStopConnection = true
        devicesToFetchData.clear()
        mSessionController?.cancel()
    }

    fun forgetDevice(bioBleDevice: BioBleDevice) {
        stopConnection()

        val userInfo: UserInfo? = mRealM?.where(UserInfo::class.java)?.equalTo(
            "name", mCurrentUserName
        )?.findFirst()

        val deviceInfo =
            mRealM?.where(DeviceInfo::class.java)?.equalTo("users.name", mCurrentUserName)
                ?.equalTo("address", bioBleDevice.deviceAddress)?.findFirst()
        mRealM?.beginTransaction()
        userInfo?.registeredDevices?.remove(deviceInfo)
        mRealM?.commitTransaction()
        deviceInfo?.address?.let {
            mUserRegisteredDeviceAddresses.remove(it)
            Utils.unPairBluetoothDevice(it)
        }

        val matchingDevice = mDiscoveredDevices.find { it.address == bioBleDevice.deviceAddress }
        if (matchingDevice != null)
            mDiscoveredDevices.remove(matchingDevice)
        //Logger.d("DATA_CHECK", "Forget device " + foundDevice?.address)
        mConnectedDevice = null
        mScanController?.stopScan()
        //realm.close()

        if (mIsOnlyPairingMode) {
            startScan()
        } else {
            startContinuousConnection()
        }

    }

    fun clearDB() {
        //val realmDb = Realm.getDefaultInstance()
        //if (mRealM == null) mRealM = Realm.getDefaultInstance()
        //mRealM?.executeTransaction { realm -> realm.deleteAll() }
        //realmDb.close()

        mUserRegisteredDeviceAddresses.forEach {
            Utils.unPairBluetoothDevice(it)
        }
        val userInfo: UserInfo? = mRealM?.where(UserInfo::class.java)?.equalTo(
            "name", mCurrentUserName
        )?.findFirst()

        var deviceInfo: DeviceInfo?

        val tempList = mUserRegisteredDeviceAddresses.toList()
        tempList.forEach { address ->
            deviceInfo =
                mRealM?.where(DeviceInfo::class.java)?.equalTo("users.name", mCurrentUserName)
                    ?.equalTo("address", address)?.findFirst()
            mRealM?.beginTransaction()
            userInfo?.registeredDevices?.remove(deviceInfo)
            mRealM?.commitTransaction()
            deviceInfo?.address?.let { mUserRegisteredDeviceAddresses.remove(it) }
        }
    }

    private fun connectToTransfer(deviceAddress: String) {

        mIsConnectionInProgress = true
        mScanController?.stopScan()

        //val mRealm = Realm.getDefaultInstance()
        val deviceInfo: DeviceInfo? = mRealM?.where(DeviceInfo::class.java)?.equalTo(
            "users.name", mCurrentUserName
        )?.equalTo("address", deviceAddress)?.findFirst()
        val userInfo: UserInfo? = mRealM?.where(UserInfo::class.java)?.equalTo(
            "name", mCurrentUserName
        )?.findFirst()
        val userData: MutableMap<OHQUserDataKey, Any> = HashMap()
        userData[OHQUserDataKey.DateOfBirthKey] = userInfo?.dateOfBirth ?: ""
        userData[OHQUserDataKey.HeightKey] = userInfo?.height ?: ""
        userData[OHQUserDataKey.GenderKey] = userInfo?.gender ?: ""

        mMode = Mode.Normal
        val option: HashMap<OHQSessionOptionKey, Any> = HashMap()
        if (null != deviceInfo?.userIndex) {
            option[OHQSessionOptionKey.UserIndexKey] = deviceInfo.userIndex
            option[OHQSessionOptionKey.ConsentCodeKey] = CONSENT_CODE_OHQ
        }
        if (null != deviceInfo?.databaseChangeIncrement) {
            option[OHQSessionOptionKey.DatabaseChangeIncrementValueKey] =
                deviceInfo.databaseChangeIncrement
        }
        if (null != userData) {
            option[OHQSessionOptionKey.UserDataKey] = userData
            option[OHQSessionOptionKey.UserDataUpdateFlagKey] =
                deviceInfo?.isUserDataUpdateFlag ?: true
        }
        if (Protocol.OmronExtension === deviceInfo?.protocol) {
            option[OHQSessionOptionKey.AllowAccessToOmronExtendedMeasurementRecordsKey] = true
            option[OHQSessionOptionKey.AllowControlOfReadingPositionToMeasurementRecordsKey] = true
            if (null != deviceInfo.sequenceNumberOfLatestRecord) {
                option[OHQSessionOptionKey.SequenceNumberOfFirstRecordToReadKey] =
                    deviceInfo.sequenceNumberOfLatestRecord + 1
            }
        }
        option[OHQSessionOptionKey.ReadMeasurementRecordsKey] = true

        mHistoryData = HistoryData()
        mHistoryData.comType = ComType.Transfer
        mHistoryData.address = deviceInfo?.address
        mHistoryData.localName = deviceInfo?.localName
        mHistoryData.completeLocalName = deviceInfo?.completeLocalName
        mHistoryData.deviceCategory = deviceInfo?.deviceCategory
        mHistoryData.protocol = deviceInfo?.protocol

        //mRealm.close()

        deviceInfo?.let {
            try {
                startSession(it, option)
            } catch (ex: Exception) {
                connectNextDevice()
            }
        } ?: run {
            connectNextDevice()
        }
    }

    fun connectNextDevice() {
        if (mCurrentConnectionIndex >= devicesToFetchData.size - 1) {
            mCurrentConnectionIndex = 0
        } else {
            mCurrentConnectionIndex++
        }
        connectToTransfer(devicesToFetchData[mCurrentConnectionIndex])
    }

    fun registerWithDevice(bioBleDevice: BioBleDevice) {
        mIsConnectionInProgress = true
        val discoveredDevice = (bioBleDevice.sdkLevelDevice as DiscoveredDevice)
        val protocol = Protocol.BluetoothStandard
        //val mRealm = Realm.getDefaultInstance()
        val userInfo: UserInfo? = mRealM?.where(UserInfo::class.java)?.equalTo(
            "name", mCurrentUserName
        )?.findFirst()

        val selectedUserIndex: Int? = null
        val userData: HashMap<OHQUserDataKey, Any> = HashMap()
        userData[OHQUserDataKey.DateOfBirthKey] = userInfo?.dateOfBirth ?: ""
        userData[OHQUserDataKey.HeightKey] = userInfo?.height ?: ""
        userData[OHQUserDataKey.GenderKey] = userInfo?.gender ?: ""

        mMode = Mode.Normal
        val option: HashMap<OHQSessionOptionKey, Any> = HashMap()
        var specifiedUserControl = false
        if (OHQDeviceCategory.WeightScale == discoveredDevice.deviceCategory) {
            specifiedUserControl = true
        }
        if (OHQDeviceCategory.BodyCompositionMonitor == discoveredDevice.deviceCategory) {
            specifiedUserControl = true
        }
        if (Protocol.OmronExtension === protocol) {
            specifiedUserControl = true
        }

        if (specifiedUserControl) {
            option[OHQSessionOptionKey.RegisterNewUserKey] = true
            option[OHQSessionOptionKey.ConsentCodeKey] = CONSENT_CODE_OHQ
            if (null != selectedUserIndex) {
                option[OHQSessionOptionKey.UserIndexKey] = selectedUserIndex
            }
            if (null != userData) {
                option[OHQSessionOptionKey.UserDataKey] = userData
            }
            option[OHQSessionOptionKey.DatabaseChangeIncrementValueKey] = 0.toLong()
            option[OHQSessionOptionKey.UserDataUpdateFlagKey] = true
        }
        if (Protocol.OmronExtension === protocol) {
            option[OHQSessionOptionKey.AllowAccessToOmronExtendedMeasurementRecordsKey] = true
            option[OHQSessionOptionKey.AllowControlOfReadingPositionToMeasurementRecordsKey] = true
        }
        option[OHQSessionOptionKey.ReadMeasurementRecordsKey] = true

        mHistoryData = HistoryData()
        mHistoryData.comType = ComType.Register
        mHistoryData.address = discoveredDevice.address
        mHistoryData.localName = discoveredDevice.localName
        mHistoryData.completeLocalName = discoveredDevice.completeLocalName
        mHistoryData.deviceCategory = discoveredDevice.deviceCategory
        mHistoryData.protocol = protocol

        //mRealm.close()

        val deviceInfo = DeviceInfo()
        deviceInfo.address = discoveredDevice.address
        deviceInfo.localName = discoveredDevice.localName
        deviceInfo.completeLocalName = discoveredDevice.localName
        deviceInfo.deviceCategory = discoveredDevice.deviceCategory
        deviceInfo.modelName = discoveredDevice.modelName

        try {
            startSession(deviceInfo, option)
        } catch (ex: Exception) {
            //Do nothing. Timeout in app should handle connection error
        }
    }

    /**
     * Call this method in a try block and catch the exception
     * Exception is thrown in mLoggingManager.start
     */
    private fun startSession(
        deviceInfo: DeviceInfo,
        option: HashMap<OHQSessionOptionKey, Any>
    ) {
        if (mSessionController!!.isInSession) {
            AppLog.i("Already started session.")
            return
        }

        val handler = Handler()
        mLoggingManager.start(object : LoggingManager.ActionListener {
            override fun onSuccess() {
                onStarted()
            }

            override fun onFailure() {
                onStarted()
            }

            private fun onStarted() {
                handler.post {
                    Common.outputDeviceInfo(applicationContext)
                    mSessionController!!.setConfig(getConfig(applicationContext!!))
                    option[OHQSessionOptionKey.ConnectionWaitTimeKey] =
                        if (mIsOnlyPairingMode) CONNECTION_WAIT_TIME_REGISTRATION else CONNECTION_WAIT_TIME_TRANSFER
                    mConnectedDevice = deviceInfo
                    mSessionOption = option
                    mSessionController!!.startSession(deviceInfo.address, option)
                }
            }
        })
    }


    private fun getConfig(context: Context): Bundle {
        /*val pref: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        var s: String?
        val cOption: CreateBondOption
        s = pref.getString(SettingKey.create_bond_option.name, null)
        cOption = when {
            mContext?.getString(R.string.create_bond_before_catt_connection).equals(s) -> {
                CreateBondOption.UsedBeforeGattConnection
            }
            mContext?.getString(R.string.create_bond_after_services_discovered).equals(s) -> {
                CreateBondOption.UsedAfterServicesDiscovered
            }
            else -> {
                CreateBondOption.NotUse
            }
        }
        val rOption: RemoveBondOption
        s = pref.getString(SettingKey.remove_bond_option.name, null)
        rOption = if (mContext?.getString(R.string.remove_bond_use).equals(s)) {
            RemoveBondOption.UsedBeforeConnectionProcessEveryTime
        } else {
            RemoveBondOption.NotUse
        }
        return Bundler.bundle(
            Key.CreateBondOption.name,
            cOption,
            Key.RemoveBondOption.name,
            rOption,
            Key.AssistPairingDialogEnabled.name,
            pref.getBoolean(SettingKey.assist_pairing_dialog.name, false),
            Key.AutoPairingEnabled.name,
            pref.getBoolean(SettingKey.auto_pairing.name, false),
            Key.AutoEnterThePinCodeEnabled.name,
            pref.getBoolean(SettingKey.auto_enter_the_pin_code.name, false),
            Key.PinCode.name,
            pref.getString(SettingKey.pin_code.name, "123456"),
            Key.StableConnectionEnabled.name,
            pref.getBoolean(SettingKey.stable_connection.name, false),
            Key.StableConnectionWaitTime.name,
            (pref.getString(SettingKey.stable_connection_wait_time.name, "123456"))?.toLong(),
            Key.ConnectionRetryEnabled.name,
            pref.getBoolean(SettingKey.connection_retry.name, false),
            Key.ConnectionRetryDelayTime.name,
            pref.getString(SettingKey.connection_retry_delay_time.name, "123456")?.toLong(),
            Key.ConnectionRetryCount.name,
            pref.getString(SettingKey.connection_retry_count.name, "123456")?.toInt(),
            Key.UseRefreshWhenDisconnect.name,
            pref.getBoolean(SettingKey.refresh_use.name, false)
        )*/

        val cbConfig = CBConfig()
        return cbConfig.getDefault(
            listOf(
                CBConfig.Key.CreateBondOption,
                CBConfig.Key.RemoveBondOption,
                CBConfig.Key.AssistPairingDialogEnabled,
                CBConfig.Key.AutoPairingEnabled,
                CBConfig.Key.AutoEnterThePinCodeEnabled,
                CBConfig.Key.PinCode,
                CBConfig.Key.StableConnectionEnabled,
                CBConfig.Key.StableConnectionWaitTime,
                CBConfig.Key.ConnectionRetryEnabled,
                CBConfig.Key.ConnectionRetryDelayTime,
                CBConfig.Key.ConnectionRetryCount,
                CBConfig.Key.UseRefreshWhenDisconnect
            )
        )

    }


    private fun validateSessionWithData(protocol: Protocol, sessionData: SessionData): ResultType {
        AppLog.e("OHQCompletionReason.Disconnected =" + Gson().toJson(sessionData))
        if (OHQCompletionReason.Disconnected != sessionData.completionReason) {
            AppLog.e("OHQCompletionReason.Disconnected != sessionData.getCompletionReason()")
            return ResultType.Failure
        }
        if (Protocol.OmronExtension === protocol && null == sessionData.userIndex) {
            AppLog.e("Protocol.OmronExtension == protocol && null == sessionData.getUserIndex()")
            return ResultType.Failure
        }
        if (null == sessionData.batteryLevel) {
            AppLog.e("null == sessionData.getBatteryLevel()")
            return ResultType.Failure
        }
        if (null == sessionData.currentTime) {
            AppLog.e("null == sessionData.getCurrentTime()")
            return ResultType.Failure
        }
        val option =
            sessionData.option
        if (null == option) {
            AppLog.e("null == option")
            return ResultType.Failure
        }
        if (option.containsKey(OHQSessionOptionKey.UserDataKey)
            && option.containsKey(OHQSessionOptionKey.DatabaseChangeIncrementValueKey)
        ) {
            if (null != sessionData.userIndex && null == sessionData.databaseChangeIncrement) {
                AppLog.e("null != sessionData.getUserIndex() && null == sessionData.getDatabaseChangeIncrement()")
                return ResultType.Failure
            }
        }
        return ResultType.Success
    }

    private fun updateUserInfo(historyData: HistoryData) {
        //val mRealm = Realm.getDefaultInstance()
        val userInfo: UserInfo? = mRealM?.where(UserInfo::class.java)?.equalTo(
            "name", mCurrentUserName
        )?.findFirst()
        val userData = historyData.userData
        if (userData.isNotEmpty()) {
            var updated = false
            mRealM?.beginTransaction()
            val dateOfBirth =
                Types.autoCast<String>(userData[OHQUserDataKey.DateOfBirthKey])
            if (null != dateOfBirth && !userInfo?.dateOfBirth.equals(dateOfBirth)) {
                userInfo?.dateOfBirth = dateOfBirth
                updated = true
            }
            val height =
                Types.autoCast<BigDecimal>(userData[OHQUserDataKey.HeightKey])
            if (null != height && 0 != userInfo?.height?.compareTo(height)) {
                userInfo?.height = height
                updated = true
            }
            val gender: OHQGender =
                Types.autoCast(userData[OHQUserDataKey.GenderKey])
            if (userInfo?.gender !== gender) {
                userInfo?.gender = gender
                updated = true
            }
            if (updated) {
                for (d in mRealM!!.where(DeviceInfo::class.java)
                    .equalTo("users.name", mCurrentUserName).findAll()) {
                    d.isUserDataUpdateFlag = true
                }
            }
            mRealM?.commitTransaction()
        }
        val deviceInfo: DeviceInfo?
        when (historyData.comType) {
            ComType.Register -> {
                deviceInfo = DeviceInfo()
                deviceInfo.address = historyData.address
                deviceInfo.localName = historyData.localName
                deviceInfo.completeLocalName = historyData.completeLocalName
                deviceInfo.modelName = historyData.modelName
                deviceInfo.deviceCategory = historyData.deviceCategory
                deviceInfo.protocol = historyData.protocol
                deviceInfo.userIndex = historyData.userIndex
                deviceInfo.consentCode = historyData.consentCode
                deviceInfo.sequenceNumberOfLatestRecord = historyData.sequenceNumberOfLatestRecord
                deviceInfo.databaseChangeIncrement = historyData.databaseChangeIncrement
                deviceInfo.isUserDataUpdateFlag = false
                AppLog.d(deviceInfo.toString())
                mRealM?.beginTransaction()
                userInfo?.registeredDevices?.add(deviceInfo)
                mRealM?.commitTransaction()
                mUserRegisteredDeviceAddresses.add(deviceInfo.address)
            }
            ComType.Delete -> {
                deviceInfo =
                    mRealM?.where(DeviceInfo::class.java)?.equalTo("users.name", mCurrentUserName)
                        ?.equalTo("address", historyData.address)?.findFirst()
                mRealM?.beginTransaction()
                userInfo?.registeredDevices?.remove(deviceInfo)
                mRealM?.commitTransaction()
                deviceInfo?.address?.let { mUserRegisteredDeviceAddresses.remove(it) }
            }
            ComType.Transfer -> {
                deviceInfo =
                    mRealM?.where(DeviceInfo::class.java)?.equalTo("users.name", mCurrentUserName)
                        ?.equalTo("address", historyData.address)?.findFirst()
                mRealM?.beginTransaction()
                deviceInfo?.sequenceNumberOfLatestRecord = historyData.sequenceNumberOfLatestRecord
                deviceInfo?.databaseChangeIncrement = historyData.databaseChangeIncrement
                deviceInfo?.isUserDataUpdateFlag = false
                mRealM?.commitTransaction()
            }
            else -> throw IllegalArgumentException("Illegal com type.")
        }
        //mRealm.close()
    }

    private fun onHistoryDataReceived(historyData: HistoryData, deviceAddress: String) {
        when (historyData.deviceCategory) {
            OHQDeviceCategory.BloodPressureMonitor -> {
                val dataList = ArrayList<OmronDeviceData>()
                historyData.measurementRecords.forEach {
                    dataList.add(recordToItems(it))
                }

                val deviceBPDataList = dataList.map {
                    DeviceBPData(
                        it.sequenceNumber?.toInt(),
                        deviceAddress,
                        BleDeviceType.BLE_DEVICE_OMRON_BP.name,
                        (DateTimeUtils.getSecondsFromDateString(
                            it.timeStamp,
                            "yyyy-MM-dd HH:mm:ss"
                        ) / 1000),
                        it.systolic?.toFloat(),
                        it.diastolic?.toFloat(),
                        it.pulseRate?.toFloat(),
                        historyData.batteryLevel?.toString(),
                        null
                    )
                }

                EventBus.getDefault().postSticky(
                    DeviceBPDataReceived(
                        deviceBPDataList,
                        BleDeviceType.BLE_DEVICE_OMRON_BP
                    )
                )


                mOmronDataSyncListener?.onOmronBPDataSync(
                    DeviceBPDataReceived(
                        deviceBPDataList,
                        BleDeviceType.BLE_DEVICE_OMRON_BP
                    )
                )
            }
            OHQDeviceCategory.WeightScale -> {

                val dataList = ArrayList<OmronDeviceData>()
                historyData.measurementRecords.forEach {
                    dataList.add(recordToItems(it))
                }

                val deviceWeightDataList = dataList.map {
                    DeviceWeightData(
                        it.sequenceNumber?.toInt(),
                        deviceAddress,
                        BleDeviceType.BLE_DEVICE_OMRON_WEIGHT.name,
                        historyData.batteryLevel.toString(),
                        (DateTimeUtils.getSecondsFromDateString(
                            it.timeStamp,
                            "yyyy-MM-dd HH:mm:ss"
                        ) / 1000),
                        it.weightUnit,
                        it.heightUnit,
                        it.weight?.toFloat(),
                        it.height?.toFloat(),
                        it.bmi?.toFloat(),
                        it.bodyFatPercentage?.toFloat(),
                        it.basalMetabolism?.toFloat(),
                        it.musclePercentage?.toFloat(),
                        it.muscleMass?.toFloat(),
                        it.fatFreeMass?.toFloat(),
                        it.softLeanMass?.toFloat(),
                        it.bodyWaterMass?.toFloat(),
                        it.impedance?.toFloat(),
                        it.skeletalMusclePercentage?.toFloat(),
                        it.visceralFatLevel?.toFloat(),
                        it.bodyAge?.toFloat(),
                        it.bodyFatPercentageStageEvaluation?.toFloat(),
                        it.skeletalMusclePercentageStageEvaluation?.toFloat(),
                        it.visceralFatLevelStageEvaluation?.toFloat()
                    )
                }

                //Weight Unit (Type of value : String, Value is ["kg" or "lb"])
                //Converting it to kg always as we need to send data to core backend in kg itself
                deviceWeightDataList.forEach {
                    if (it.weightUnit == VitalMappingHelper.unitKeyLB) {
                        it.weight = Utils.convertLbsToKg(it.weight)
                        it.weightUnit = VitalMappingHelper.unitKeyKG
                    }
                }

                EventBus.getDefault().postSticky(
                    DeviceWeightDataReceived(
                        deviceWeightDataList,
                        BleDeviceType.BLE_DEVICE_OMRON_WEIGHT
                    )
                )

                mOmronDataSyncListener?.onOmronWeightDataSync(
                    DeviceWeightDataReceived(
                        deviceWeightDataList,
                        BleDeviceType.BLE_DEVICE_OMRON_WEIGHT
                    )
                )

            }
            OHQDeviceCategory.BodyCompositionMonitor -> {
                val dataList = ArrayList<OmronDeviceData>()
                historyData.measurementRecords.forEach {
                    dataList.add(recordToItems(it))
                }

                val deviceWeightDataList = dataList.map {
                    DeviceWeightData(
                        it.sequenceNumber?.toInt(),
                        deviceAddress,
                        BleDeviceType.BLE_DEVICE_OMRON_BCM.name,
                        historyData.batteryLevel.toString(),
                        (DateTimeUtils.getSecondsFromDateString(
                            it.timeStamp,
                            "yyyy-MM-dd HH:mm:ss"
                        ) / 1000),
                        it.weightUnit,
                        it.heightUnit,
                        it.weight?.toFloat(),
                        it.height?.toFloat(),
                        it.bmi?.toFloat(),
                        it.bodyFatPercentage?.toFloat(),
                        it.basalMetabolism?.toFloat(),
                        it.musclePercentage?.toFloat(),
                        it.muscleMass?.toFloat(),
                        it.fatFreeMass?.toFloat(),
                        it.softLeanMass?.toFloat(),
                        it.bodyWaterMass?.toFloat(),
                        it.impedance?.toFloat(),
                        it.skeletalMusclePercentage?.toFloat(),
                        it.visceralFatLevel?.toFloat(),
                        it.bodyAge?.toFloat(),
                        it.bodyFatPercentageStageEvaluation?.toFloat(),
                        it.skeletalMusclePercentageStageEvaluation?.toFloat(),
                        it.visceralFatLevelStageEvaluation?.toFloat()
                    )
                }

                //Weight Unit (Type of value : String, Value is ["kg" or "lb"])
                //Converting it to kg always as we need to send data to core backend in kg itself
                deviceWeightDataList.forEach {
                    if (it.weightUnit == VitalMappingHelper.unitKeyLB) {
                        it.weight = Utils.convertLbsToKg(it.weight)
                        it.weightUnit = VitalMappingHelper.unitKeyKG
                    }
                }

                EventBus.getDefault().postSticky(
                    DeviceWeightDataReceived(
                        deviceWeightDataList,
                        BleDeviceType.BLE_DEVICE_OMRON_BCM
                    )
                )

                mOmronDataSyncListener?.onOmronWeightDataSync(
                    DeviceWeightDataReceived(
                        deviceWeightDataList,
                        BleDeviceType.BLE_DEVICE_OMRON_BCM
                    )
                )
            }
            OHQDeviceCategory.Unknown -> {

            }
            null -> {

            }
        }

    }

    private fun recordToItems(record: Map<OHQMeasurementRecordKey, Any?>): OmronDeviceData {

        val deviceData = OmronDeviceData()
        if (record.containsKey(OHQMeasurementRecordKey.SequenceNumberKey)) {
            deviceData.sequenceNumber =
                record[OHQMeasurementRecordKey.SequenceNumberKey] as BigDecimal?
        }
        if (record.containsKey(OHQMeasurementRecordKey.SystolicKey)) {
            deviceData.systolic = record[OHQMeasurementRecordKey.SystolicKey] as BigDecimal?
            deviceData.bloodPressureUnit =
                record[OHQMeasurementRecordKey.BloodPressureUnitKey] as String?
        }
        if (record.containsKey(OHQMeasurementRecordKey.DiastolicKey)) {
            deviceData.diastolic = record[OHQMeasurementRecordKey.DiastolicKey] as BigDecimal?
            deviceData.bloodPressureUnit =
                record[OHQMeasurementRecordKey.BloodPressureUnitKey] as String?
        }
        if (record.containsKey(OHQMeasurementRecordKey.MeanArterialPressureKey)) {
            deviceData.meanArterialPressure =
                record[OHQMeasurementRecordKey.MeanArterialPressureKey] as BigDecimal?
            deviceData.bloodPressureUnit =
                record[OHQMeasurementRecordKey.BloodPressureUnitKey] as String?
        }
        if (record.containsKey(OHQMeasurementRecordKey.PulseRateKey)) {
            deviceData.pulseRate = record[OHQMeasurementRecordKey.PulseRateKey] as BigDecimal?
        }
        if (record.containsKey(OHQMeasurementRecordKey.UserIndexKey)) {
            deviceData.userIndex = record[OHQMeasurementRecordKey.UserIndexKey] as BigDecimal?
        }
        if (record.containsKey(OHQMeasurementRecordKey.BloodPressureMeasurementStatusKey)) {
            deviceData.bloodPressureMeasurementStatus =
                Types.autoCast(record[OHQMeasurementRecordKey.BloodPressureMeasurementStatusKey])
        }
        if (record.containsKey(OHQMeasurementRecordKey.BMIKey)) {
            deviceData.bmi = record[OHQMeasurementRecordKey.BMIKey] as BigDecimal?
        }
        if (record.containsKey(OHQMeasurementRecordKey.BodyFatPercentageKey)) {
            deviceData.bodyFatPercentage =
                record[OHQMeasurementRecordKey.BodyFatPercentageKey] as BigDecimal?
        }
        if (record.containsKey(OHQMeasurementRecordKey.BasalMetabolismKey)) {
            deviceData.basalMetabolism =
                record[OHQMeasurementRecordKey.BasalMetabolismKey] as BigDecimal?
        }
        if (record.containsKey(OHQMeasurementRecordKey.MusclePercentageKey)) {
            deviceData.musclePercentage =
                record[OHQMeasurementRecordKey.MusclePercentageKey] as BigDecimal?
        }
        if (record.containsKey(OHQMeasurementRecordKey.MuscleMassKey)) {
            deviceData.muscleMass = record[OHQMeasurementRecordKey.MuscleMassKey] as BigDecimal?
            deviceData.weightUnit = record[OHQMeasurementRecordKey.WeightUnitKey] as String?
        }
        if (record.containsKey(OHQMeasurementRecordKey.FatFreeMassKey)) {
            deviceData.fatFreeMass = record[OHQMeasurementRecordKey.FatFreeMassKey] as BigDecimal?
            deviceData.weightUnit = record[OHQMeasurementRecordKey.WeightUnitKey] as String?
        }
        if (record.containsKey(OHQMeasurementRecordKey.SoftLeanMassKey)) {
            deviceData.softLeanMass = record[OHQMeasurementRecordKey.SoftLeanMassKey] as BigDecimal?
            deviceData.weightUnit = record[OHQMeasurementRecordKey.WeightUnitKey] as String?
        }
        if (record.containsKey(OHQMeasurementRecordKey.BodyWaterMassKey)) {
            deviceData.bodyWaterMass =
                record[OHQMeasurementRecordKey.BodyWaterMassKey] as BigDecimal?
            deviceData.weightUnit = record[OHQMeasurementRecordKey.WeightUnitKey] as String?
        }
        if (record.containsKey(OHQMeasurementRecordKey.ImpedanceKey)) {
            deviceData.impedance = record[OHQMeasurementRecordKey.ImpedanceKey] as BigDecimal?
        }
        if (record.containsKey(OHQMeasurementRecordKey.SkeletalMusclePercentageKey)) {
            deviceData.skeletalMusclePercentage =
                record[OHQMeasurementRecordKey.SkeletalMusclePercentageKey] as BigDecimal?
        }
        if (record.containsKey(OHQMeasurementRecordKey.VisceralFatLevelKey)) {
            deviceData.visceralFatLevel =
                record[OHQMeasurementRecordKey.VisceralFatLevelKey] as BigDecimal?
        }
        if (record.containsKey(OHQMeasurementRecordKey.BodyAgeKey)) {
            deviceData.bodyAge = record[OHQMeasurementRecordKey.BodyAgeKey] as BigDecimal?
        }
        if (record.containsKey(OHQMeasurementRecordKey.BodyFatPercentageStageEvaluationKey)) {
            deviceData.bodyFatPercentageStageEvaluation =
                record[OHQMeasurementRecordKey.BodyFatPercentageStageEvaluationKey] as BigDecimal?
        }
        if (record.containsKey(OHQMeasurementRecordKey.SkeletalMusclePercentageStageEvaluationKey)) {
            deviceData.skeletalMusclePercentageStageEvaluation =
                record[OHQMeasurementRecordKey.SkeletalMusclePercentageStageEvaluationKey] as BigDecimal?
        }
        if (record.containsKey(OHQMeasurementRecordKey.VisceralFatLevelStageEvaluationKey)) {
            deviceData.visceralFatLevelStageEvaluation =
                record[OHQMeasurementRecordKey.VisceralFatLevelStageEvaluationKey] as BigDecimal?
        }
        if (record.containsKey(OHQMeasurementRecordKey.WeightKey)) {
            deviceData.weight = record[OHQMeasurementRecordKey.WeightKey] as BigDecimal?
            deviceData.weightUnit = record[OHQMeasurementRecordKey.WeightUnitKey] as String?
        }
        if (record.containsKey(OHQMeasurementRecordKey.HeightKey)) {
            deviceData.height = record[OHQMeasurementRecordKey.HeightKey] as BigDecimal?
            deviceData.heightUnit = record[OHQMeasurementRecordKey.HeightUnitKey] as String?
        }
        if (record.containsKey(OHQMeasurementRecordKey.TimeStampKey)) {
            deviceData.timeStamp = record[OHQMeasurementRecordKey.TimeStampKey] as String?
        }
        return deviceData
    }

}