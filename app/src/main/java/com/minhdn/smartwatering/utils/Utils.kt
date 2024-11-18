package com.minhdn.smartwatering.utils

import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import com.minhdn.smartwatering.R

fun SwitchCompat.updateSwitch(isChecked: Boolean) {
    trackTintList = if (isChecked) {
        ContextCompat.getColorStateList(context, R.color.main_color)
    } else {
        ContextCompat.getColorStateList(context, R.color.gray)
    }
}