package com.bkplus.callscreen.ui.widget

import android.os.Bundle
import com.bkplus.callscreen.ultis.openPickWifiSetting
import com.bkplus.callscreen.ultis.setOnSingleClickListener
import com.harison.core.app.platform.BaseFullScreenDialogFragment
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.FragmentNoInternetBinding

class NoInternetDialogFragment : BaseFullScreenDialogFragment<FragmentNoInternetBinding>() {
    companion object {
        fun newInstance(): NoInternetDialogFragment {
            val args = Bundle()
            val fragment = NoInternetDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override val layoutId: Int
        get() = R.layout.fragment_no_internet

    override fun setupListener() {
        super.setupListener()
        binding.btnConnect.setOnSingleClickListener {
            context.openPickWifiSetting()
        }
    }
}
