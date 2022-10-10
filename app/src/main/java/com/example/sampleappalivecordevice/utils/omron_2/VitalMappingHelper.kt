package com.example.sampleappalivecordevice.utils.omron_2

import android.content.Context
import android.util.Log
import com.biofourmis.careathomerpm.devicecommunication.BleDeviceType
import com.example.sampleappalivecordevice.devicecommunication.DeviceVitalData
import com.example.sampleappalivecordevice.devicecommunication.VitalToDisplay
import com.example.sampleappalivecordevice.utils.LocaleHelper
import com.example.sampleappalivecordevice.utils.constants.VitalDisplayCode
import com.example.sampleappalivecordevice.utils.constants.VitalDisplayType
import com.google.gson.Gson
import java.util.ArrayList

object VitalMappingHelper {
  const val unitKeyFahrenheit = "f"
  const val unitKeyCelsius = "c"
  const val unitKeyKG = "kg"
  const val unitKeyLB = "lb"
  const val unitKeyMGDL = "mg/dL"
  const val unitKeyMMOL = "mmol/l"
  private val applicationContext: Context? = null

  private fun saveLocale(serverLocale: String?) {
    when (serverLocale) {
      LocaleHelper.SupportedLocale.ENGLISH.serverName -> {
        AppPreffs.locale = LocaleHelper.SupportedLocale.ENGLISH.locale.language
        AppPreffs.country = LocaleHelper.SupportedLocale.ENGLISH.locale.country
      }
      LocaleHelper.SupportedLocale.THAI.serverName -> {
        AppPreffs.locale = LocaleHelper.SupportedLocale.THAI.locale.language
        AppPreffs.country = LocaleHelper.SupportedLocale.THAI.locale.country
      }
      LocaleHelper.SupportedLocale.MALAY.serverName -> {
        AppPreffs.locale = LocaleHelper.SupportedLocale.MALAY.locale.language
        AppPreffs.country = LocaleHelper.SupportedLocale.MALAY.locale.country
      }
      LocaleHelper.SupportedLocale.SIMPLIFIED_CHINESE.serverName -> {
        AppPreffs.locale = LocaleHelper.SupportedLocale.SIMPLIFIED_CHINESE.locale.language
        AppPreffs.country = LocaleHelper.SupportedLocale.SIMPLIFIED_CHINESE.locale.country
      }
      LocaleHelper.SupportedLocale.TRADITIONAL_CHINESE.serverName -> {
        AppPreffs.locale = LocaleHelper.SupportedLocale.TRADITIONAL_CHINESE.locale.language
        AppPreffs.country = LocaleHelper.SupportedLocale.TRADITIONAL_CHINESE.locale.country
      }
      LocaleHelper.SupportedLocale.SPANISH_US.serverName -> {
        AppPreffs.locale = LocaleHelper.SupportedLocale.SPANISH_US.locale.language
        AppPreffs.country = LocaleHelper.SupportedLocale.SPANISH_US.locale.country
      }
      LocaleHelper.SupportedLocale.KOREAN.serverName -> {
        AppPreffs.locale = LocaleHelper.SupportedLocale.KOREAN.locale.language
        AppPreffs.country = LocaleHelper.SupportedLocale.KOREAN.locale.country
      }
      LocaleHelper.SupportedLocale.VIETNAMESE.serverName -> {
        AppPreffs.locale = LocaleHelper.SupportedLocale.VIETNAMESE.locale.language
        AppPreffs.country = LocaleHelper.SupportedLocale.VIETNAMESE.locale.country
      }
      LocaleHelper.SupportedLocale.HAITIAN_CREOLE.serverName -> {
        AppPreffs.locale = LocaleHelper.SupportedLocale.HAITIAN_CREOLE.locale.language
        AppPreffs.country = LocaleHelper.SupportedLocale.HAITIAN_CREOLE.locale.country
      }
      else -> {
        AppPreffs.locale = LocaleHelper.SupportedLocale.ENGLISH.locale.language
        AppPreffs.country = LocaleHelper.SupportedLocale.ENGLISH.locale.country
      }
    }
    LocaleHelper.setLocale(
      applicationContext!!,
      AppPreffs.locale,
      AppPreffs.country
    )
  }

  fun mapLastUpdatedDataToVitals(
    deviceVitalData: DeviceVitalData,
    deviceType: BleDeviceType,
    vitalsToDisplayList: ArrayList<VitalToDisplay>
  ): ArrayList<VitalToDisplay> {
    Log.d("ABHIJEETHALLUR90", "deviceVitalData = " + Gson().toJson(deviceVitalData))
    Log.d("ABHIJEETHALLUR90", "vitalsToDisplayList = " + Gson().toJson(vitalsToDisplayList))
    Log.d("ABHIJEETHALLUR90", "deviceType = " + Gson().toJson(deviceType))
    for (i in vitalsToDisplayList.indices) {
      when (deviceType) {
        BleDeviceType.BLE_DEVICE_EVERION -> {
          if (vitalsToDisplayList[i].deviceType == VitalDisplayType.everion.name) {
            when (vitalsToDisplayList[i].code) {
              VitalDisplayCode.hr.name -> {
                if (deviceVitalData.hr != null && deviceVitalData.hr ?: 0f > 0f) {
                  vitalsToDisplayList[i].lastUpdatedTime =
                    deviceVitalData.timestamp?.toLong()
                      ?.times(1000)
                  vitalsToDisplayList[i].lastUpdatedValue = deviceVitalData.hr
                }
              }
              VitalDisplayCode.rr.name -> {
                if (deviceVitalData.respirationRate != null && deviceVitalData.respirationRate ?: 0f > 0f) {
                  vitalsToDisplayList[i].lastUpdatedTime =
                    deviceVitalData.timestamp?.toLong()
                      ?.times(1000)
                  vitalsToDisplayList[i].lastUpdatedValue =
                    deviceVitalData.respirationRate
                }
              }
              VitalDisplayCode.hrv.name -> {
                if (deviceVitalData.hrv != null && deviceVitalData.hrv ?: 0f > 0f) {
                  vitalsToDisplayList[i].lastUpdatedTime =
                    deviceVitalData.timestamp?.toLong()
                      ?.times(1000)
                  vitalsToDisplayList[i].lastUpdatedValue = deviceVitalData.hrv
                }
              }
              VitalDisplayCode.bpw.name -> {
                if (deviceVitalData.bloodPulseWave != null && deviceVitalData.bloodPulseWave ?: 0f > 0f) {
                  vitalsToDisplayList[i].lastUpdatedTime =
                    deviceVitalData.timestamp?.toLong()
                      ?.times(1000)
                  vitalsToDisplayList[i].lastUpdatedValue =
                    deviceVitalData.bloodPulseWave
                }
              }
              VitalDisplayCode.steps.name -> {
                if (deviceVitalData.totalSteps != null && deviceVitalData.totalSteps ?: 0 > 0) {
                  vitalsToDisplayList[i].lastUpdatedTime =
                    deviceVitalData.timestamp?.toLong()
                      ?.times(1000)
                  vitalsToDisplayList[i].lastUpdatedValue =
                    deviceVitalData.totalSteps?.toFloat()
                }
              }
              VitalDisplayCode.spo2.name -> {
                if (deviceVitalData.spO2 != null && deviceVitalData.spO2 ?: 0f > 0f) {
                  vitalsToDisplayList[i].lastUpdatedTime =
                    deviceVitalData.timestamp?.toLong()
                      ?.times(1000)
                  vitalsToDisplayList[i].lastUpdatedValue = deviceVitalData.spO2
                }
              }
              VitalDisplayCode.o_temp.name -> {
                if (deviceVitalData.localTemp != null && deviceVitalData.localTemp ?: 0f > 0f) {
                  vitalsToDisplayList[i].lastUpdatedTime =
                    deviceVitalData.timestamp?.toLong()
                      ?.times(1000)
                  vitalsToDisplayList[i].lastUpdatedValue =
                    deviceVitalData.localTemp
                }
              }
              VitalDisplayCode.b_temp.name -> {
                if (deviceVitalData.bodyTemp != null && deviceVitalData.bodyTemp ?: 0f > 0f) {
                  vitalsToDisplayList[i].lastUpdatedTime =
                    deviceVitalData.timestamp?.toLong()
                      ?.times(1000)
                  vitalsToDisplayList[i].lastUpdatedValue =
                    deviceVitalData.bodyTemp
                }
              }
            }
          }
        }
        BleDeviceType.BLE_DEVICE_VITAL_PATCH -> {
          if (vitalsToDisplayList[i].deviceType == VitalDisplayType.vitalPatch.name) {
            when (vitalsToDisplayList[i].code) {
              VitalDisplayCode.hr.name -> {
                if (deviceVitalData.hr != null && deviceVitalData.hr ?: 0f > 0f) {
                  vitalsToDisplayList[i].lastUpdatedTime =
                    deviceVitalData.timestamp?.toLong()?.times(1000)
                  vitalsToDisplayList[i].lastUpdatedValue = deviceVitalData.hr
                }
              }
              VitalDisplayCode.rr.name -> {
                if (deviceVitalData.respirationRate != null && deviceVitalData.respirationRate ?: 0f > 0f) {
                  vitalsToDisplayList[i].lastUpdatedTime =
                    deviceVitalData.timestamp?.toLong()?.times(1000)
                  vitalsToDisplayList[i].lastUpdatedValue =
                    deviceVitalData.respirationRate
                }
              }
              VitalDisplayCode.hrv.name -> {
                if (deviceVitalData.hrv != null && deviceVitalData.hrv ?: 0f > 0f) {
                  vitalsToDisplayList[i].lastUpdatedTime =
                    deviceVitalData.timestamp?.toLong()?.times(1000)
                  vitalsToDisplayList[i].lastUpdatedValue = deviceVitalData.hrv
                }
              }
              VitalDisplayCode.bpw.name -> {
                if (deviceVitalData.bloodPulseWave != null && deviceVitalData.bloodPulseWave ?: 0f > 0f) {
                  vitalsToDisplayList[i].lastUpdatedTime =
                    deviceVitalData.timestamp?.toLong()?.times(1000)
                  vitalsToDisplayList[i].lastUpdatedValue =
                    deviceVitalData.bloodPulseWave
                }
              }
              VitalDisplayCode.steps.name -> {
                if (deviceVitalData.totalSteps != null && deviceVitalData.totalSteps ?: 0 > 0) {
                  vitalsToDisplayList[i].lastUpdatedTime =
                    deviceVitalData.timestamp?.toLong()?.times(1000)
                  vitalsToDisplayList[i].lastUpdatedValue =
                    deviceVitalData.totalSteps?.toFloat()
                }
              }
              VitalDisplayCode.spo2.name -> {
                if (deviceVitalData.spO2 != null && deviceVitalData.spO2 ?: 0f > 0f) {
                  vitalsToDisplayList[i].lastUpdatedTime =
                    deviceVitalData.timestamp?.toLong()?.times(1000)
                  vitalsToDisplayList[i].lastUpdatedValue = deviceVitalData.spO2
                }
              }
              VitalDisplayCode.o_temp.name -> {
                if (deviceVitalData.localTemp != null && deviceVitalData.localTemp ?: 0f > 0f) {
                  vitalsToDisplayList[i].lastUpdatedTime =
                    deviceVitalData.timestamp?.toLong()?.times(1000)
                  vitalsToDisplayList[i].lastUpdatedValue =
                    deviceVitalData.localTemp
                }
              }
              VitalDisplayCode.b_temp.name -> {
                if (deviceVitalData.bodyTemp != null && deviceVitalData.bodyTemp ?: 0f > 0f) {
                  vitalsToDisplayList[i].lastUpdatedTime =
                    deviceVitalData.timestamp?.toLong()?.times(1000)
                  vitalsToDisplayList[i].lastUpdatedValue =
                    deviceVitalData.bodyTemp
                }
              }
            }
          }
        }
        BleDeviceType.BLE_DEVICE_OMRON_BP,
        BleDeviceType.BLE_DEVICE_WELCHALLYN_BP1700,
        BleDeviceType.BLE_DEVICE_IHEALTH_BP5 -> {
          if (isBpDeviceType(vitalsToDisplayList[i].deviceType)) {
            when (vitalsToDisplayList[i].code) {
              VitalDisplayCode.bp.name -> {
                if (deviceVitalData.bpSystolic != null && deviceVitalData.bpSystolic ?: 0f > 0f &&
                  deviceVitalData.bpDiastolic != null && deviceVitalData.bpDiastolic ?: 0f > 0f
                ) {
                  vitalsToDisplayList[i].lastUpdatedTime =
                    deviceVitalData.timestamp?.toLong()?.times(1000)
                  vitalsToDisplayList[i].lastUpdatedValue =
                    deviceVitalData.bpSystolic
                  vitalsToDisplayList[i].lastUpdatedValue2 =
                    deviceVitalData.bpDiastolic
                }
              }
              VitalDisplayCode.hr_bp.name -> {
                if (deviceVitalData.hr_bp != null && deviceVitalData.hr_bp ?: 0f > 0f) {
                  vitalsToDisplayList[i].lastUpdatedTime =
                    deviceVitalData.timestamp?.toLong()
                      ?.times(1000)
                  vitalsToDisplayList[i].lastUpdatedValue = deviceVitalData.hr_bp
                }
              }
            }
          }
        }
        BleDeviceType.BLE_DEVICE_OMRON_WEIGHT,
        BleDeviceType.BLE_DEVICE_WELCHALLYN_WEIGHT,
        BleDeviceType.BLE_DEVICE_FORACARE_TNG_WEIGHT_SCALE,
        BleDeviceType.BLE_DEVICE_FORACARE_WEIGHT_SCALE_W550,
        BleDeviceType.BLE_DEVICE_OMRON_BCM
        -> {
          if (isWeightDeviceType(vitalsToDisplayList[i].deviceType)) {
            when (vitalsToDisplayList[i].code) {
              VitalDisplayCode.weight.name -> {
                if (deviceVitalData.weight != null && deviceVitalData.weight ?: 0f > 0f) {
                  vitalsToDisplayList[i].lastUpdatedTime =
                    deviceVitalData.timestamp?.toLong()?.times(1000)
                  vitalsToDisplayList[i].lastUpdatedValue = deviceVitalData.weight
                }
              }
            }
          }
        }
        BleDeviceType.BLE_DEVICE_MEGARING -> { }
        BleDeviceType.BLE_DEVICE_NONIN_SPO2 -> {
          Log.d(
            "SPO2_DATA_CHECK_HEALTH",
            "deviceTypeNONIN = ${vitalsToDisplayList[i].deviceType}"
          )
          if (vitalsToDisplayList[i].deviceType == VitalDisplayType.noninSpO2.name) {
            //if (vitalsToDisplayList[i].deviceType == "manual") {
            when (vitalsToDisplayList[i].code) {
              VitalDisplayCode.spo2.name -> {
                if (deviceVitalData.spO2 != null && deviceVitalData.spO2 ?: 0f > 0f) {
                  vitalsToDisplayList[i].lastUpdatedTime =
                    deviceVitalData.timestamp?.toLong()?.times(1000)
                  vitalsToDisplayList[i].lastUpdatedValue = deviceVitalData.spO2
                }
              }
            }
          }
        }
        BleDeviceType.BLE_DEVICE_IHEALTH_PO3M -> {
          Log.d(
            "SPO2_DATA_CHECK_HEALTH",
            "deviceTypePO3 = ${vitalsToDisplayList[i].deviceType}"
          )
          if (vitalsToDisplayList[i].deviceType == VitalDisplayType.iHealthPO3M.name) {
            //if (vitalsToDisplayList[i].deviceType == "manual") {
            when (vitalsToDisplayList[i].code) {
              VitalDisplayCode.spo2.name -> {
                if (deviceVitalData.spO2 != null && deviceVitalData.spO2 ?: 0f > 0f) {
                  vitalsToDisplayList[i].lastUpdatedTime =
                    deviceVitalData.timestamp?.toLong()?.times(1000)
                  vitalsToDisplayList[i].lastUpdatedValue = deviceVitalData.spO2
                }
              }
            }
          }
        }
        BleDeviceType.BLE_DEVICE_ITHERMONITER -> {
          if (vitalsToDisplayList[i].deviceType == VitalDisplayType.iThermonitor.name) {
            when (vitalsToDisplayList[i].code) {
              VitalDisplayCode.core_temp.name -> {
                if (deviceVitalData.coreTemp != null && deviceVitalData.coreTemp ?: 0f > 0f) {
                  vitalsToDisplayList[i].lastUpdatedTime =
                    deviceVitalData.timestamp?.toLong() // not multiplying by 1000 as the data already is in millis for iThermonitor
                  vitalsToDisplayList[i].lastUpdatedValue =
                    deviceVitalData.coreTemp
                }
              }
            }
          }
        }
        BleDeviceType.BLE_DEVICE_FORACARE_IR20 -> {
          Log.d("Gabriel", "mapLastUpdatedDataToVitals: ")
          if (vitalsToDisplayList[i].deviceType == VitalDisplayType.foraCareIR20B.name) {
//                    if (vitalsToDisplayList[i].deviceType == "manual") {
            when (vitalsToDisplayList[i].code) {
              VitalDisplayCode.t_temp.name -> {
                if (deviceVitalData.objectTemp != null && deviceVitalData.objectTemp ?: 0f > 0f) {
                  vitalsToDisplayList[i].lastUpdatedTime =
                    deviceVitalData.timestamp?.times(1000L)
                  vitalsToDisplayList[i].lastUpdatedValue =
                    deviceVitalData.objectTemp
                  Log.d("Gabriel", "${vitalsToDisplayList[i]}")
                }
              }
            }
          }
        }

        BleDeviceType.BLE_DEVICE_FORACARE_TNG_BLOOD_GLUCOSE_METER -> {
          if (vitalsToDisplayList[i].deviceType == VitalDisplayType.foraCareTNGBloodGlucose.name) {
            when (vitalsToDisplayList[i].code) {
              VitalDisplayCode.blood_glucose.name -> {
                if (deviceVitalData.bloodGlucose != null && deviceVitalData.bloodGlucose ?: 0f > 0f) {
                  vitalsToDisplayList[i].lastUpdatedTime =
                    deviceVitalData.timestamp?.toLong()?.times(1000)
                  vitalsToDisplayList[i].lastUpdatedValue = deviceVitalData.bloodGlucose
                }
              }
            }
          }
        }

        BleDeviceType.BLE_DEVICE_VALRT -> {
        }
      }
    }

    return vitalsToDisplayList
  }

  private fun isBpDeviceType(
    deviceType: String?
  ): Boolean {
    return deviceType == VitalDisplayType.omronBp.name
        || deviceType == VitalDisplayType.welchAllynBP1700.name
        || deviceType == VitalDisplayType.iHealthBP5.name
  }

  private fun isWeightDeviceType(
    deviceType: String?
  ): Boolean {
    return (deviceType == VitalDisplayType.omronWeight.name
        || deviceType == VitalDisplayType.welchAllynWeight.name
        || deviceType == VitalDisplayType.omronBcm.name
        || deviceType == VitalDisplayType.foraCareWeightScale.name)
  }
}