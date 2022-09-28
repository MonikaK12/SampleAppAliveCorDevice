package com.example.sampleappalivecordevice.utils

import com.example.sampleappalivecordevice.R
import com.example.sampleappalivecordevice.utils.constants.AppConstants.DAY_FULL_NAME_MAP
import com.example.sampleappalivecordevice.utils.constants.AppConstants.MONTH_FULL_NAME_MAP
import com.example.sampleappalivecordevice.utils.omron_2.AppPreffs
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.chrono.ThaiBuddhistChronology
import org.threeten.bp.format.DateTimeFormatter
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs


/**
 * Created by abhijeethallur on 13,November,2019
 */


object DateTimeUtils {

    const val TIME_OF_DAY_MORNING = 1
    const val TIME_OF_DAY_NOON = 2
    const val TIME_OF_DAY_EVENING = 3
    const val TIME_OF_DAY_NIGHT = 4
    const val TIME_OF_DAY_NONE = 5

    const val COUNTLY_DATE_TIME_FORMAT = "dd MMM yyyy, hh.mm.ss a"

    fun getSecondsFromDateString(givenDate: String?, format: String): Long {
        var millis = 0L
        givenDate?.let {
            val sdf = SimpleDateFormat(format)
            val date = sdf.parse(givenDate)
            millis = date?.time ?: 0
        }

        return millis
    }

    @JvmStatic
    fun getDateForCountlyTimeStamp(timestamp: Long): String {
        val date = Date(timestamp)
        val formatter = SimpleDateFormat(COUNTLY_DATE_TIME_FORMAT, Locale.getDefault())
        return formatter.format(date)
    }

}

