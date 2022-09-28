package com.example.sampleappalivecordevice.utils

import android.util.Log
import com.example.sampleappalivecordevice.BuildConfig
import com.example.sampleappalivecordevice.utils.constants.AppConstants.BUILD_TYPE_DEBUG
import com.example.sampleappalivecordevice.utils.constants.AppConstants.BUILD_TYPE_DEMO
import com.example.sampleappalivecordevice.utils.constants.AppConstants.BUILD_TYPE_RELEASE
import com.example.sampleappalivecordevice.utils.constants.AppConstants.BUILD_TYPE_STAGE
import com.example.sampleappalivecordevice.utils.constants.AppConstants.BUILD_TYPE_STAGING
import java.lang.Exception

object Logger {

    private const val TAG = "HOMEHOSPITAL"
    private const val DEFAULT_MSG = "ERROR"
    private const val LOG_ENABLE =
        BuildConfig.BUILD_TYPE == BUILD_TYPE_STAGING || BuildConfig.BUILD_TYPE == BUILD_TYPE_STAGE ||
                BuildConfig.BUILD_TYPE == BUILD_TYPE_DEBUG || BuildConfig.BUILD_TYPE == BUILD_TYPE_DEMO
//            || BuildConfig.BUILD_TYPE == BUILD_TYPE_RELEASE

    private fun printMsg(logType: Int, tag: String?, message: String) {
        val maxLogSize = 2000
        for (i in 0..message.length / maxLogSize) {
            val start = i * maxLogSize
            var end = (i + 1) * maxLogSize
            end = if (end > message.length) message.length else end

            when (logType) {
                Log.ASSERT -> {
                    Log.v(tag, "=> ${message.substring(start, end)}")
                }
                Log.DEBUG -> {
                    Log.d(tag, "=> ${message.substring(start, end)}")
                }
                Log.ERROR -> {
                    Log.e(tag, "=> ${message.substring(start, end)}")
                }
                Log.INFO -> {
                    Log.i(tag, "=> ${message.substring(start, end)}")
                }
                Log.VERBOSE -> {
                    Log.v(tag, "=> ${message.substring(start, end)}")
                }
                Log.WARN -> {
                    Log.w(tag, "=> ${message.substring(start, end)}")
                }
            }
        }
    }

    fun v(tag: String?, msg: String?) {
        if (LOG_ENABLE) {
            printMsg(Log.VERBOSE, tag ?: TAG, msg ?: DEFAULT_MSG)
        }
    }

    fun d(tag: String?, msg: String?) {
        if (LOG_ENABLE) {
            printMsg(Log.DEBUG, tag ?: TAG, msg ?: DEFAULT_MSG)
        }
    }

    fun i(tag: String?, msg: String?) {
        if (LOG_ENABLE) {
            printMsg(Log.INFO, tag ?: TAG, msg ?: DEFAULT_MSG)
        }
    }

    fun w(tag: String?, msg: String?) {
        if (LOG_ENABLE) {
            printMsg(Log.WARN, tag ?: TAG, msg ?: DEFAULT_MSG)
        }
    }

    fun e(tag: String?, msg: String?) {
        if (LOG_ENABLE) {
            printMsg(Log.ERROR, tag ?: TAG, msg ?: DEFAULT_MSG)
        }
    }

    fun e(tag: String?, msg: String?, exception: Exception? = null) {
        if (LOG_ENABLE) {
            printMsg(Log.ERROR, tag ?: TAG, msg ?: DEFAULT_MSG)
            Log.e(tag, "$msg: ", exception)
        }
    }

}