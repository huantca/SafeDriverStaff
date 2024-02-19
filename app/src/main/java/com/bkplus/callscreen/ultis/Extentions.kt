package com.bkplus.callscreen.ultis

import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import timber.log.Timber
import java.io.File

fun ImageView.loadImage(uri: Uri?) {
    if (uri == null) return
    Glide.with(this).load(uri).into(this)
}

fun ImageView.loadImage(string: String?) {
    Glide.with(this).load(string).into(this)
}

fun ImageView.loadImage(file: File) {
    Glide.with(this).load(file).into(this)
}

fun String.deleteFileIfExist() {
    try {
        val file = File(this)
        if (file.exists()) file.delete()
    } catch (e: Exception) {
        Timber.tag("FileException").e(e)
    }
}
