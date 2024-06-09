package com.bkplus.android.ui.main.login.loginuser

import android.text.method.PasswordTransformationMethod
import android.view.View

class AsteriskPasswordTransformationMethod : PasswordTransformationMethod() {
    private val asteriskChar = '*'
    override fun getTransformation(source: CharSequence?, view: View?): CharSequence {
        if (source == null) {
            return ""
        }
        val buffer = CharArray(source.length)
        for (i in source.indices) {
            buffer[i] = asteriskChar
        }
        return String(buffer)
    }
}