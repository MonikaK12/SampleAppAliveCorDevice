package com.biofourmis.careathomerpm.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.ColorRes
import androidx.annotation.FontRes
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import com.example.sampleappalivecordevice.R
import com.example.sampleappalivecordevice.utils.LocaleHelper
import java.util.*


object Utils {

    private const val WEIGHT_CONVERSION_FACTOR: Float = 2.2046226218F

    fun convertDpToPixels(context: Context, dp: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        )
    }

    fun getCurrentTimeInSeconds(): Long {
        return Calendar.getInstance().timeInMillis / 1000
    }

    fun findClosestNumInArray(arr: LongArray, target: Long): Long {
        val n = arr.size

        // Corner cases
        if (target <= arr[0]) return arr[0]
        if (target >= arr[n - 1]) return arr[n - 1]

        // Doing binary search
        var i = 0
        var j = n
        var mid = 0
        while (i < j) {
            mid = (i + j) / 2
            if (arr[mid] == target) return arr[mid]

            /* If target is less than array element,
               then search in left */if (target < arr[mid]) {

                // If target is greater than previous
                // to mid, return closest of two
                if (mid > 0 && target > arr[mid - 1]) return getClosest(
                    arr[mid - 1],
                    arr[mid], target
                )

                /* Repeat for left half */j = mid
            } else {
                if (mid < n - 1 && target < arr[mid + 1]) return getClosest(
                    arr[mid],
                    arr[mid + 1], target
                )
                i = mid + 1 // update i
            }
        }

        // Only single element left after search
        return arr[mid]
    }

    fun findClosestNumInIntArray(arr: IntArray, target: Int): Int {
        val n = arr.size

        // Corner cases
        if (target <= arr[0]) return arr[0]
        if (target >= arr[n - 1]) return arr[n - 1]

        // Doing binary search
        var i = 0
        var j = n
        var mid = 0
        while (i < j) {
            mid = (i + j) / 2
            if (arr[mid] == target) return arr[mid]

            /* If target is less than array element,
               then search in left */if (target < arr[mid]) {

                // If target is greater than previous
                // to mid, return closest of two
                if (mid > 0 && target > arr[mid - 1]) return getClosestInt(
                    arr[mid - 1],
                    arr[mid], target
                )

                /* Repeat for left half */j = mid
            } else {
                if (mid < n - 1 && target < arr[mid + 1]) return getClosestInt(
                    arr[mid],
                    arr[mid + 1], target
                )
                i = mid + 1 // update i
            }
        }

        // Only single element left after search
        return arr[mid]
    }

    // Method to compare which one is the more close
    // We find the closest by taking the difference
    //  between the target and both values. It assumes
    // that val2 is greater than val1 and target lies
    // between these two.
    private fun getClosest(
        val1: Long, val2: Long,
        target: Long
    ): Long {
        return if (target - val1 >= val2 - target) val2 else val1
    }

    private fun getClosestInt(
        val1: Int, val2: Int,
        target: Int
    ): Int {
        return if (target - val1 >= val2 - target) val2 else val1
    }

    fun openSoftKeyboard(view: View) {
        val inputMethodManager: InputMethodManager? =
            view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        inputMethodManager?.toggleSoftInputFromWindow(
            view.windowToken,
            InputMethodManager.SHOW_FORCED,
            0
        )
    }

    fun closeSoftKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }

    @SuppressLint("ClickableViewAccessibility")
    fun setupTouch(view: View) {
        // Set up touch listener for non-text box views to hide keyboard.
        if (view !is EditText) {
            view.setOnTouchListener { _, _ ->
                closeSoftKeyboard(view)
                false
            }
        }

        //If a layout container, iterate over children and seed recursion.
        if (view is ViewGroup) {
            (0 until view.childCount)
                .asSequence()
                .map { view.getChildAt(it) }
                .forEach { setupTouch(it) }
        }
    }

    fun convertMgdlToMmol(mgdlValue: Float?): Float? {
        return if (mgdlValue == null) null else mgdlValue * 0.0555f
    }

    fun convertMmolToMgdl(mmolValue: Float?): Float? {
        return if (mmolValue == null) null else mmolValue / 0.0555f
    }

    fun convertCelsiusToFahrenheit(celsiusValue: Float?): Float? {
        if (celsiusValue == null) return null
        return celsiusValue * 9 / 5 + 32
    }

    fun convertFahrenheitToCelsius(fahrenheitValue: Float?): Float? {
        if (fahrenheitValue == null) return null
        return (fahrenheitValue - 32) * 5 / 9
    }

    fun convertKgToLbs(kgValue: Float?): Float? {
        if (kgValue == null) return null
        return (kgValue * WEIGHT_CONVERSION_FACTOR)  // Manual Entry working good
    }

    fun convertLbsToKg(lbValue: Float?): Float? {
        if (lbValue == null) return null
        return (lbValue / WEIGHT_CONVERSION_FACTOR)
    }

    fun unPairBluetoothDevice(address: String) {
        val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        val pairedDevices: Set<BluetoothDevice> = mBluetoothAdapter.bondedDevices
        if (pairedDevices.isNotEmpty()) {
            for (device in pairedDevices) {
                try {
                    if (device.address == address) {
                        device::class.java.getMethod("removeBond").invoke(device)
                        return
                    }
                } catch (e: Exception) {
                }
            }
        }
    }

    fun hasAllPermissionsGranted(grantResults: IntArray): Boolean {
        for (grantResult in grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false
            }
        }
        return true
    }

    /**
     * @param mainview of Fragment/ activity
     *  Hide keyboard, tap on any where in ui
     */

    @SuppressLint("ClickableViewAccessibility")
    fun setupTouchTwo(view: View) {
        // Set up touch listener for non-text box views to hide keyboard.
        if (view !is EditText) {
            view.setOnTouchListener { _, _ ->
                closeSoftKeyboard(view)
                false
            }
        }
        //If a layout container, iterate over children and seed recursion.
        if (view is ViewGroup) {
            (0 until view.childCount)
                .asSequence()
                .map { view.getChildAt(it) }
                .forEach { setupTouch(it) }
        }
    }

    data class SpanInfo(
        val text: String,
        @ColorRes val spanColor: Int? = null,
        @FontRes val font: Int? = null
    )

    enum class SpanType(val spanTypeInt: Int) {
        EXCLUSIVE_EXCLUSIVE(SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE),
        EXCLUSIVE_INCLUSIVE(SpannableString.SPAN_EXCLUSIVE_INCLUSIVE),
        INCLUSIVE_EXCLUSIVE(SpannableString.SPAN_INCLUSIVE_EXCLUSIVE),
        INCLUSIVE_INCLUSIVE(SpannableString.SPAN_INCLUSIVE_INCLUSIVE)
    }
}