package com.example.sampleappalivecordevice.utils.constants


object CountlyConstants {

    const val IS_COUNTLY_LOG_ENABLED = true

    const val KEY_EVENT_APP_OPEN = "event_app_open"
    const val KEY_EVENT_APP_LOGIN = "event_app_login"

    const val KEY_EVENT_DEVICE_SCAN_START = "event_device_scan_start"
    const val KEY_EVENT_DEVICE_FOUND = "event_device_found"
    const val KEY_EVENT_DEVICE_SESSION_SUCCESS = "event_device_session_success"
    const val KEY_EVENT_INIT_MANAGERS = "event_device_init_managers"
    const val KEY_EVENT_INIT_MQTT_SUCCESS = "init_mqtt_success"
    const val KEY_EVENT_INIT_MQTT_FAILED = "init_mqtt_failed"
    const val KEY_EVENT_DEVICE_SESSION_FAILED = "event_device_session_failed"
    const val KEY_EVENT_DEVICE_RESCAN_START = "event_device_rescan_start"
    const val KEY_EVENT_DEVICE_SCAN_STOP = "event_device_scan_stop"
    const val KEY_EVENT_DEVICE_SCAN_ERROR = "event_device_scan_error"
    const val KEY_EVENT_DEVICE_PAIRED = "event_device_paired"
    const val KEY_EVENT_DEVICE_UNPAIRED = "event_device_unpaired"
    const val KEY_EVENT_DEVICE_CONNECTION_START = "event_device_connection_start"
    const val KEY_EVENT_DEVICE_CONNECTED = "event_device_connected"
    const val KEY_EVENT_DEVICE_NOT_READY = "event_device_not_ready"
    const val KEY_EVENT_DEVICE_CONNECTION_ERROR = "event_device_connection_error"
    const val KEY_EVENT_DEVICE_DISCONNECTED = "event_device_disconnected"
    const val KEY_EVENT_PATCH_STATUS_CHANGED = "event_patch_status_changed"
    const val KEY_EVENT_DEVICE_DATA_SYNC_SUCCESS = "event_device_data_sync_success"
    const val KEY_EVENT_DEVICE_DATA_SYNC_FAILED = "event_device_data_sync_failed"
    const val KEY_EVENT_MQTT_CONNECTED = "event_mqtt_connected"
    const val KEY_EVENT_MQTT_DISCONNECTED = "event_mqtt_disconnected"
    const val KEY_EVENT_MQTT_RECONNECT_START = "event_mqtt_start_reconnect"
    const val KEY_EVENT_DEVICE_DATA_SYNC_TYPE = "event_device_data_sync_type"
    const val KEY_EVENT_SERVICE_STATUS = "event_vp_service_status"
    const val KEY_EVENT_LOGOUT = "event_log_out"
    const val KEY_EVENT_APP = "event_application"


    const val KEY_SEGMENT_TIME = "time"
    const val KEY_SEGMENT_DEVICE_ADDRESS = "deviceAddress"
    const val KEY_SEGMENT_DEVICE_SERIAL_NUM = "deviceSerialNumber"
    const val KEY_SEGMENT_DEVICE_FIRMWARE_VERSION = "deviceFirmwareVersion"
    const val KEY_SEGMENT_DEVICE_TYPE = "deviceType"
    const val KEY_SEGMENT_DEVICE_METADATA = "metadata"
    const val KEY_SEGMENT_STATUS = "status"
    const val KEY_IS_LOGGED_IN = "isLoggedIn"

    const val EVENT_ERROR = "event_error"
    const val KEY_TASK = "task"
    const val KEY_MESSAGE = "message"
    const val KEY_URL = "url"
    const val KEY_MQTT_HASH_CODE = "key_mqtt_hash_code"

    const val EVENT_APP_OPENED_AT = "event_app_opened_at"
    const val event_app_login = "event_app_login"
    const val KEY_EVENT_APP_CHANGE_PASSWORD = "event_app_change_password"
    const val KEY_EVENT_PRIVACY_POLICY = "event_privacy_policy"
    const val KEY_EVENT_FORGOT_PASSWORD = "event_forgot_password"


    // Call
    const val KEY_EVENT_ROOM_CONNECTED = "event_room_connected"
    const val KEY_EVENT_ROOM_DISCONNECTED = "event_room_disconnected"
    const val KEY_EVENT_CALL_CHANNEL_MESSAGE_RECEIVED = "event_call_channel_message_received"
    const val KEY_EVENT_CALL_CHANNEL_MESSAGE_SENT = "event_call_channel_message_sent"
    const val KEY_SEGMENT_ROOM_NAME = "room_name"
    const val KEY_SEGMENT_CALL_CHANNEL_MSG = "message"
    const val KEY_EVENT_GETTING_ROOM_EVENT = "event_fetching_room_name"
    const val KEY_SEGMENT_HCP_ID = "hcp_id"
    const val KEY_EVENT_ROOM_NAME_RECEIVED = "event_room_name_received"

    // Push notification
    const val KEY_EVENT_PUSH_NOTIFICATION_RECEIVED = "event_push_notification_received"
    const val KEY_SEGMENT_PUSH_TYPE = "push_type"
    const val KEY_SEGMENT_TITLE = "title"
    const val KEY_SEGMENT_BODY = "body"
    const val KEY_SEGMENT_SENT_TIME = "sent_time"

    // Language
    const val KEY_EVENT_LANGUAGE_CHANGED = "event_language_changed"
    const val KEY_SEGMENT_SELECTED_LANGUAGE_CODE = "selected_language_code"

    const val KEY_EVENT_DEVICE_DISCOVERED = "event_device_discovered"
    const val KEY_EVENT_DEVICE_PAIR = "event_device_pair"
    const val KEY_EVENT_DEVICE_PAIRING_FAILED = "event_device_pairing_failed"
    const val KEY_EVENT_DEVICE_BATTERY = "event_device_battery"
    const val KEY_EVENT_BLUETOOTH_STATUS = "event_bluetooth_status"
    const val KEY_EVENT_LOCATION_STATUS = "event_location_status"
    const val KEY_EVENT_PASSCODE_CHANGED = "event_passcode_changed"
    const val KEY_EVENT_APP_KILLED = "event_app_killed"
    const val KEY_EVENT_USER_LOGGEDOUT = "event_user_loggedout"
    const val KEY_EVENT_NETWORK_STATUS = "event_network_status"
    const val KEY_EVENT_BATTERY_STATUS = "event_battery_status"
    const val KEY_EVENT_AUDIO_CALL_STARTED = "event_audio_call_started"
    const val KEY_EVENT_AUDIO_CALL_ENDED = "event_audio_call_ended"
    const val KEY_EVENT_AUDIO_CALL_REJECTED = "event_audio_call_rejected"
    const val KEY_EVENT_VIDEO_CALL_STARTED = "event_video_call_started"
    const val KEY_EVENT_VIDEO_CALL_ENDED = "event_video_call_ended"
    const val KEY_EVENT_VIDEO_CALL_REJECTED = "event_video_call_rejected"
    const val KEY_EVENT_TEXT_CHAT = "event_text_chat"
    const val KEY_EVENT_CALENDAR_TASKS_UPDATED = "event_calendar_tasks_updated"
    const val KEY_EVENT_CALENDAR_TASK_PERFORM = "event_calendar_task_perform"
    const val KEY_EVENT_NOTE_CREATED = "event_note_created"
    const val KEY_EVENT_NOTE_EDITED = "event_note_edited"
    const val KEY_EVENT_NOTE_REFRESHED = "event_note_refreshed"
    const val KEY_EVENT_TASK_REMINDER = "event_task_reminder"
    const val KEY_EVENT_USER_LOGGED_OUT = "event_user_logged_out"
    const val KEY_EVENT_NEW_DEVICE_DISCOVERED = "event_new_device_discovered"

    const val KEY_SEGMENT_NOTE_ID = "noteId"

    // values
    const val VALUE_SUCCESS = "Success"
    const val VALUE_FAILED = "Failed"
    const val VALUE_ENABLED = "Enabled"
    const val VALUE_DISABLED = "Disabled"

}