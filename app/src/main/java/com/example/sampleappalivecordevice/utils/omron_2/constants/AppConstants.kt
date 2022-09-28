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


}