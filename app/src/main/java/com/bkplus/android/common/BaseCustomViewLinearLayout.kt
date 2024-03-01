package com.harison.core.app.platform

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**Base custom view
 * @sample
 * */
abstract class BaseCustomViewLinearLayout<VB : ViewDataBinding> : LinearLayout {
    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        attrs?.let {
            initStyleable(it)
        }
        setLayout()
        initView()
    }

    protected abstract val layoutId: Int
    protected lateinit var binding: VB
    protected lateinit var layout: View
    open fun initView() {}
    private fun initStyleable(attrs: AttributeSet) {}
    private fun setLayout() {
        val inflater = LayoutInflater.from(context)
        binding = DataBindingUtil.inflate(inflater, layoutId, this, true)
        layout = binding.root
    }
}
