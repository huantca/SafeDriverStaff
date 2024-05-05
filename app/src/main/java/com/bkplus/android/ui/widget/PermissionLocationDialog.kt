package com.bkplus.android.ui.widget

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.harison.core.app.platform.BaseDialogFragment
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.LayoutPermisisonDialogBinding

class PermissionLocationDialog : BaseDialogFragment<LayoutPermisisonDialogBinding>() {
    override val layoutId: Int
        get() = R.layout.layout_permisison_dialog


    override fun setupListener() {
        super.setupListener()
        binding.apply {
            btnClose.setOnClickListener {
                dismiss()
            }

            btnOpenSetting.setOnClickListener {
                context?.let { ctx ->
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", ctx.packageName, null)
                    intent.data = uri
                    context?.startActivity(intent)
                }
                dismiss()
            }
        }
    }
}