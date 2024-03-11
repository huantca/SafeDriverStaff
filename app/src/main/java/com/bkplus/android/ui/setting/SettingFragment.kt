package com.bkplus.android.ui.setting

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import com.bkplus.android.ads.EventTracking
import com.bkplus.android.ads.TrackingManager
import com.bkplus.android.common.BaseFragment
import com.bkplus.android.ultis.Constants
import com.bkplus.android.ultis.openAppInPlayStore
import com.bkplus.android.ultis.openShare
import com.bkplus.android.ultis.setOnSingleClickListener
import com.harrison.myapplication.BuildConfig
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

    @SuppressLint("SetTextI18n")
    override fun setupUI() {
        super.setupUI()
        TrackingManager.tracking(EventTracking.fb011_settings_view)
        binding.tvVersion.text = "Ver " + BuildConfig.VERSION_NAME
    }

    override fun setupListener() {
        super.setupListener()
        binding.apply {
            btnLanguage.setOnSingleClickListener {
                findNavController().navigate(R.id.languageFragment)
            }
            btnRate.setOnSingleClickListener {
                activity.openAppInPlayStore()
            }

            btnShare.setOnSingleClickListener {
                activity.openShare()
            }
            btnPolicy.setOnSingleClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(Constants.PRIVACY_POLICY_LINK)
                startActivity(intent)
            }
            btnTerm.setOnSingleClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(Constants.TERMS_OF_USE_LINK)
                startActivity(intent)
            }
        }
    }
}