package com.bkplus.android.ultis

import android.view.View
import java.text.DecimalFormat

fun View.gone() {
    if (visibility != View.GONE) visibility = View.GONE
}

fun View.visible() {
    if (visibility != View.VISIBLE) visibility = View.VISIBLE
}

fun View.invisible() {
    if (visibility != View.INVISIBLE) visibility = View.INVISIBLE
}

fun numberToVND(number: Double?): String {
    if (number == null) return ""
    val formatter = DecimalFormat("##,###,###.## VND")
    return formatter.format(number)
}
