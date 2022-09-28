package com.example.sampleappalivecordevice.utils.constants

object CalendarTaskConstants {

    const val DEFAULT_REMINDER_SNOOZE_MINS: Long = 10
    const val DEFAULT_REMINDER_SNOOZE_MILLIS =
        1000 * 60 * DEFAULT_REMINDER_SNOOZE_MINS

    private const val REMINDER_SOUND_INTERVAL_MINS = 10
    const val REMINDER_SOUND_INTERVAL_MILLIS = REMINDER_SOUND_INTERVAL_MINS * 60 * 1000

}