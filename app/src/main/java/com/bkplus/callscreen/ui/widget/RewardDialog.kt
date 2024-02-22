package com.bkplus.callscreen.ui.widget

import com.bkplus.callscreen.ultis.setOnSingleClickListener
import com.bumptech.glide.Glide
import com.harison.core.app.platform.BaseDialogFragment
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.LayoutRewardDialogBinding

class RewardDialog : BaseDialogFragment<LayoutRewardDialogBinding>() {

    override val layoutId: Int
        get() = R.layout.layout_reward_dialog

    var action: () -> Unit = {}
    override fun setupUI() {
        super.setupUI()
        binding.apply {
            context?.let { Glide.with(it).load(R.drawable.ic_reward_dialog).into(icReward) }
        }
    }

    override fun setupListener() {
        super.setupListener()
        binding.apply {
            btnWatchAd.setOnSingleClickListener {
                action.invoke()
                dismiss()
            }

            btnCancel.setOnSingleClickListener {
                dismiss()
            }
        }
    }

}