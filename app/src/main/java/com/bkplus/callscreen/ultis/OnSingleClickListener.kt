package com.bkplus.callscreen.ultis

import android.view.View
import androidx.databinding.BindingAdapter
import java.util.concurrent.atomic.AtomicBoolean

class OnSingleClickListener(
    private val clickListener: View.OnClickListener,
    private var millisecond: Long = 1000
) : View.OnClickListener {
    constructor(clickListener: View.OnClickListener, time: Long?) : this(clickListener) {
        if (time != null) {
            millisecond = time
        }
    }

    private var isCanClick = AtomicBoolean(true)

    override fun onClick(view: View?) {
        if (isCanClick.getAndSet(false)) {
            view?.run {
                postDelayed({ isCanClick.set(true) }, millisecond)
                clickListener.onClick(view)
            }
        }
    }
}
@BindingAdapter("onSingleClick")
fun View.setOnSingleClickListener(clickListener: View.OnClickListener?) {
    clickListener?.also {
        setOnClickListener(OnSingleClickListener(it))
    } ?: setOnClickListener(null)
}
