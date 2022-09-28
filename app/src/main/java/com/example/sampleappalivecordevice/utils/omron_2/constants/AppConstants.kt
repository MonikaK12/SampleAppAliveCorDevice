package com.example.sampleappalivecordevice.utils.constants

import com.example.sampleappalivecordevice.R

object AppConstants {

    const val PACKAGE_NAME = "com.example.sampleappalivecordevice"
    const val CARE_AT_HOME_PATIENT_INTENT_EXTRA_PREFIX = "care_at_home_patient"

    const val BUILD_TYPE_STAGING = "staging"
    const val BUILD_TYPE_STAGE = "stage"
    const val BUILD_TYPE_CE_STAGE = "ce_stage"
    const val BUILD_TYPE_DEBUG = "debug"
    const val BUILD_TYPE_DEMO = "demo"
    const val BUILD_TYPE_QA = "qaenv"
    const val BUILD_TYPE_DRDEMO = "drdemo"
    const val BUILD_TYPE_RELEASE = "release"

    const val EXTRA_AUTH_LOGOUT = "AuthLogout"
    const val EXTRA_AUTH_LOGOUT_OLD_DEVICE = "AuthLogoutOldDevice"
    const val EXTRA_AUTH_LOGOUT_DEACTIVATED = "AuthLogoutDeactivated"

    const val ALARM_SERVICE_DAILY_TASK_RESET = 9001
    const val ALARM_SERVICE_DEVICE_CONN_RESET = 9002

    // timestamp of 1 Jan 2020 00:00:00
    const val TIMESTAMP_JAN_2020 = 1577836800000

    const val CAL_ACTIVITY_TYPE_ALLOW_TEXT = "allowText"
    const val CAL_ACTIVITY_TYPE_MANUAL_INPUT = "manualInput"
    const val CAL_ACTIVITY_TYPE_GENERAL = "generalTask"
    const val CAL_ACTIVITY_TYPE_OTHER_ACTIVITY = "otherActivity"
    const val CAL_ACTIVITY_TYPE_MEDICATION = "medication"
    const val CAL_ACTIVITY_TYPE_CHECK_IN = "checkin"
    const val CAL_ACTIVITY_QUESTIONNAIRE = "questionnaire"

    // Call Status
    const val CALL_ACCEPT_STATUS = 1
    const val CALL_REJECT_STATUS = 2

    const val APP_TYPE = "home_hospital"

    // No internet code
    const val NO_INTERNET = "9999"

    object TextChat {
        const val NOTIFICATION_ID_CHAT = 6
        const val EXTRA_HCP_ID = "${PACKAGE_NAME}.HCP_ID"
    }

    val MONTH_FULL_NAME_MAP = hashMapOf(
        "jan" to R.string.text_full_month_jan,
        "feb" to R.string.text_full_month_feb,
        "mar" to R.string.text_full_month_mar,
        "apr" to R.string.text_full_month_apr,
        "may" to R.string.text_full_month_may,
        "jun" to R.string.text_full_month_jun,
        "jul" to R.string.text_full_month_jul,
        "aug" to R.string.text_full_month_aug,
        "sep" to R.string.text_full_month_sep,
        "oct" to R.string.text_full_month_oct,
        "nov" to R.string.text_full_month_nov,
        "dec" to R.string.text_full_month_dec
    )

    val MONTH_SHORT_NAME_MAP = hashMapOf(
        "jan" to R.string.text_short_month_jan,
        "feb" to R.string.text_short_month_feb,
        "mar" to R.string.text_short_month_mar,
        "apr" to R.string.text_short_month_apr,
        "may" to R.string.text_short_month_may,
        "jun" to R.string.text_short_month_jun,
        "jul" to R.string.text_short_month_jul,
        "aug" to R.string.text_short_month_aug,
        "sep" to R.string.text_short_month_sep,
        "oct" to R.string.text_short_month_oct,
        "nov" to R.string.text_short_month_nov,
        "dec" to R.string.text_short_month_dec
    )

    val DAY_FULL_NAME_MAP = hashMapOf(
        "mon" to R.string.text_full_day_mon,
        "tue" to R.string.text_full_day_tue,
        "wed" to R.string.text_full_day_wed,
        "thu" to R.string.text_full_day_thu,
        "fri" to R.string.text_full_day_fri,
        "sat" to R.string.text_full_day_sat,
        "sun" to R.string.text_full_day_sun
    )

    val DAY_SHORT_NAME_MAP = hashMapOf(
        "mon" to R.string.text_short_day_mon,
        "tue" to R.string.text_short_day_tue,
        "wed" to R.string.text_short_day_wed,
        "thu" to R.string.text_short_day_thu,
        "fri" to R.string.text_short_day_fri,
        "sat" to R.string.text_short_day_sat,
        "sun" to R.string.text_short_day_sun
    )


}