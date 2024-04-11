package com.bkplus.android.ultis

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.widget.ImageView
import androidx.core.content.ContextCompat
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
fun <T> List<T>.toArrayList(): ArrayList<T> { return ArrayList(this) }

fun String.deleteFileIfExist() {
    try {
        val file = File(this)
        if (file.exists()) file.delete()
    } catch (e: Exception) {
        Timber.tag("FileException").e(e)
    }
}

fun Context.getBitmapFromVectorDrawable(drawableId: Int): Bitmap {
    val drawable = ContextCompat.getDrawable(this, drawableId)
    val bitmap = Bitmap.createBitmap(
        drawable!!.intrinsicWidth,
        drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
}
