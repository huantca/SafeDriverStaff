package com.bkplus.android.ui.widget

import com.bkplus.android.ultis.setOnSingleClickListener
import com.harison.core.app.platform.BaseDialogFragment
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.LayoutCompleteDialogBinding

class CompleteDialog : BaseDialogFragment<LayoutCompleteDialogBinding>() {

    override val layoutId: Int
        get() = R.layout.layout_complete_dialog
    var action = {}
    override fun setupListener() {
        super.setupListener()
        binding.apply {
            btnHome.setOnSingleClickListener{
                action.invoke()
            }
        }
    }
}