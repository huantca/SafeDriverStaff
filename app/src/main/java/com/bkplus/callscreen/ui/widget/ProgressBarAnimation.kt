package com.bkplus.callscreen.ui.widget

import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.ProgressBar

class ProgressBarAnimation(
    private val progressBar: ProgressBar,
    private val from: Float,
    private val to: Float,
    private val onAnimationEnd: () -> Unit = {}
) : Animation() {
    override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
        super.applyTransformation(interpolatedTime, t)
        val value = from + (to - from) * interpolatedTime
        progressBar.setProgress(value.toInt(), true)
        if (interpolatedTime == 1f) {
            onAnimationEnd()
        }
    }
}