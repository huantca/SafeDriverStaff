package com.qrcode.ai.app.ui.main.widget

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.core.view.isVisible
import com.bkplus.callscreen.ultis.Constants
import com.bkplus.callscreen.ultis.setOnSingleClickListener
import com.bumptech.glide.Glide
import com.harison.core.app.platform.BaseDialogFragment
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.DialogForceUpdateBinding

class OptionUpdateDialog : BaseDialogFragment<DialogForceUpdateBinding>() {
    override val layoutId: Int
        get() = R.layout.dialog_force_update

    override fun setupUI() {
        super.setupUI()
        Glide.with(this).asGif().load(R.drawable.ic_update).into(binding.icForce)
        binding.btnUpdateNow.isVisible = false
    }

    override fun setupListener() {
        super.setupListener()
        binding.btnCancel.setOnSingleClickListener{
            dismiss()
        }
        binding.btnUpdate.setOnSingleClickListener{
            openAppInPlayStore()
        }
    }

    private fun openAppInPlayStore() {
        val uri = Uri.parse(Constants.store_uri)
        val goToMarketIntent = Intent(Intent.ACTION_VIEW, uri)

        var flags = Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        flags = flags or Intent.FLAG_ACTIVITY_NEW_DOCUMENT
        goToMarketIntent.addFlags(flags)

        try {
            startActivityForResult(goToMarketIntent, Constants.request_code_share)
        } catch (e: ActivityNotFoundException) {
            val intent = Intent(
                Intent.ACTION_VIEW, Uri.parse(Constants.LINK_STORE)
            )
            startActivityForResult(intent, Constants.request_code_share)
        }
    }

}
