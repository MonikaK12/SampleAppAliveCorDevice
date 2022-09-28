package com.example.sampleappalivecordevice.utils.constants

import com.example.sampleappalivecordevice.R
import com.example.sampleappalivecordevice.application.HospitalAtHomeApp
import com.example.sampleappalivecordevice.utils.LocaleHelper

enum class TaskStatus(
    val status: Int,
    val value: Int,
    val color: Int,
    val icon: Int
) {
    NOT_COMPLETED(
        0,
        R.string.task_status_not_completed,
        R.color.colorDarkishPink,
        R.drawable.ic_task_status_cross
    ),
    TAKEN(
        1,
        R.string.task_status_taken,
        R.color.colorDarkSeaFoam,
        R.drawable.ic_task_status_check
    ),
    DO_N0T_HAVE_IT(
        2,
        R.string.task_status_dont_have,
        R.color.colorDarkishPink,
        R.drawable.ic_task_status_cross
    ),
    NOT_ABLE_TO_TAKE(
        3,
        R.string.task_status_not_able_to_take,
        R.color.colorDarkishPink,
        R.drawable.ic_task_status_cross
    );

    companion object {
        fun getValue(status: Int): TaskStatus {
            values().forEach {
                if (it.status == status) {
                    return it
                }
            }
            throw IllegalArgumentException("$status is not valid status.")
        }
    }
}