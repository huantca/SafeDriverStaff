package com.bkplus.callscreen.ui.setting

import android.os.Bundle
import com.bkplus.callscreen.common.BaseFragment
import com.bkplus.callscreen.ui.main.home.HomeFragment
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.FragmentSettingBinding

class SettingFragment : BaseFragment<FragmentSettingBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_setting

    companion object {
        fun newInstance(): SettingFragment {
            val args = Bundle()
            val fragment = SettingFragment()
            fragment.arguments = args
            return fragment
        }
    }
}