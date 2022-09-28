package com.example.sampleappalivecordevice.utils.constants

object NotificationConstants {

    const val NOTIFICATION_CHANNEL_ID = "com.example.sampleappalivecordevice"
    const val FIREBASE_NOTIFICATION_ID = 1001
    const val PACKAGE_NAME = "com.example.sampleappalivecordevice"
    const val EXTRA_STARTED_FROM_FIREBASE_NOTIFICATION = "$PACKAGE_NAME.started_from_firebase_notification"


    const val CALL_RESPONSE_ACTION_KEY = "CALL_RESPONSE_ACTION_KEY"
    const val CALL_RECEIVE_ACTION = "CALL_RECEIVE_ACTION"
    const val CALL_CANCEL_ACTION = "CALL_CANCEL_ACTION"

}

object NotificationIds{
    const val VIDEO_CALL = 501
    const val CHAT = 502
    const val REMINDER = 503
    const val CALL = 120
    const val INCOMING_CALL = 90
}

object NotificationTypes{
    const val NOTIFICATION_TYPE_CALL_ACCEPTED = "push-type-call-accepted"
    const val NOTIFICATION_TYPE_CALL_REJECTED = "push-type-call-rejected"
    const val NOTIFICATION_TYPE_CALL_RINGING = "push-type-call-ringing"
    const val NOTIFICATION_TYPE_CALL_DISCONNECTED = "push-type-call-disconnected"
    const val NOTIFICATION_TYPE_CALL_CALLENDED = "push-type-call-callended"
    const val NOTIFICATION_TYPE_CALL_TIMEDOUT = "push-type-call-timedout"
}