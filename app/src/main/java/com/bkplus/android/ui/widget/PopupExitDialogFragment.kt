package com.bkplus.android.ui.widget

import com.bkplus.android.ultis.setOnSingleClickListener
import com.harison.core.app.platform.BaseDialogFragment
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.LayoutDialogExitBinding

class PopupExitDialogFragment : BaseDialogFragment<LayoutDialogExitBinding>() {

    override val layoutId: Int
        get() = R.layout.layout_dialog_exit

    var actionYes = {}
    override fun setupUI() {
        super.setupUI()
        isCancelable = false
    }


    override fun setupListener() {
        super.setupListener()
        binding.apply {
            btnExitYes.setOnSingleClickListener {
                dismiss()
                actionYes.invoke()
            }

            btnExitNo.setOnSingleClickListener {
                dismiss()
            }
        }
    }

}