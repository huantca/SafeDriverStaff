package com.bkplus.android.ui.widget

import com.harison.core.app.platform.BaseDialogFragment
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.LayoutConfirmDialogBinding

class ConfirmDialog : BaseDialogFragment<LayoutConfirmDialogBinding>() {

    override val layoutId: Int
        get() = R.layout.layout_confirm_dialog
    var cancel = {}
    override fun setupListener() {
        super.setupListener()
        binding.btnNo.setOnClickListener {
            dismiss()
        }

        binding.btnYes.setOnClickListener {
            cancel.invoke()
        }
    }
}