package com.example.sampleappalivecordevice.utils.omron_2
import com.chibatching.kotpref.KotprefModel
import java.util.*

object AppPreffs: KotprefModel() {
  var locale by stringPref(Locale.getDefault().language)
  var country by stringPref(Locale.getDefault().country)
  var isLoggedIn by booleanPref(false)
  var token by nullableStringPref(null)
  var fcmToken by stringPref()
  var refreshtoken by nullableStringPref(null)
  var appId by nullableStringPref(null)
  var appUrl by nullableStringPref(null)

  var patientFirstName by nullableStringPref(null)
  var patientLastName by nullableStringPref(null)
  var patientMiddleName by nullableStringPref(null)
  var patientId by nullableStringPref(null)
  var patientMrn by nullableStringPref(null)
  var patientProfilePic by nullableStringPref(null)
  var patientGender by stringPref("NA")
  var patientAge by stringPref("0")
  var patientCity by nullableStringPref(null)
  var taskSchedulingInterval by intPref(0)
  var patientEnrollmentDate by longPref(0)

  var isPatientUpdateRequired by booleanPref(false)

  var siteId by nullableStringPref(null)
  var programId by nullableStringPref(null)
  var patientWeightUnit by nullableStringPref(null)
  var patientTemperatureUnit by nullableStringPref(null)
  var patientBloodGlucoseUnit by nullableStringPref(null)

  // Task ids on which the user didnt take any action.
  val pendingTaskRemindersIds by stringSetPref() {
    return@stringSetPref HashSet<String>()
  }

  // Task ids on which user took "Remind later" action.
  val remindLaterTaskIds by stringSetPref() {
    return@stringSetPref HashSet<String>()
  }
  var isCalendarTaskUpdated by booleanPref(false)

  // App lifecycle
  var isAppVisible by booleanPref(false)

  var isDeviceOnBoardingDone by booleanPref(false)
  var isVitalPatchStepsReset by booleanPref(false)

  var chatUnRead by booleanPref()
  var notificationUnReadMessagesCount by intPref()
  var isFromIntent by booleanPref()

  //Vital connect
  var vitalPatchLiveConnectionString by stringPref("")
  var vitalPatchHistoricConnectionString by stringPref("")

  //Unread message alert
  var unreadMessagesAlertHandled by booleanPref(true)

  val previouslyPairedDevices by stringSetPref()
  var isVideoCallLogAdded by booleanPref(default = false)
  var videoCallDuration by intPref(default = 0)

  //    emergency contact information
  var emergencyContactNumber by intPref(0)
  var shouldShowEmergencyContactNumber by booleanPref(false)
}