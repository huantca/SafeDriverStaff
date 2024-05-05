package com.bkplus.android.ui.widget

import com.harison.core.app.platform.BaseDialogFragment
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.LayoutDialogSelectCarBinding

class SelectCarDialog : BaseDialogFragment<LayoutDialogSelectCarBinding>() {
    override val layoutId: Int
        get() = R.layout.layout_dialog_select_car
    var action = {}

    override fun setupListener() {
        super.setupListener()
        binding.btnNext.setOnClickListener {
            action.invoke()
            dismiss()
        }
    }
}