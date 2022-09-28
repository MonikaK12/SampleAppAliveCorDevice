package com.example.sampleappalivecordevice.utils.omron_2

import android.content.Context
import com.example.sampleappalivecordevice.utils.LocaleHelper
import com.example.sampleappalivecordevice.utils.constants.VitalDisplayType

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