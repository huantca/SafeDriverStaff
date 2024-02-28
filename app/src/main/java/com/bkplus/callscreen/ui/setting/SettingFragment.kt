package com.bkplus.callscreen.ui.setting

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import com.ads.bkplus_ads.core.callforward.BkPlusAppOpenAdManager
import com.bkplus.callscreen.ads.EventTracking
import com.bkplus.callscreen.ads.TrackingManager
import com.bkplus.callscreen.common.BaseFragment
import com.bkplus.callscreen.ultis.Constants
import com.bkplus.callscreen.ultis.goToFeedback
import com.bkplus.callscreen.ultis.openAppInPlayStore
import com.bkplus.callscreen.ultis.openShare
import com.bkplus.callscreen.ultis.setOnSingleClickListener
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
        TrackingManager.tracking(EventTracking.fb003_settings_view)
        binding.tvVersion.text = "Ver " + BuildConfig.VERSION_NAME
    }

    override fun setupListener() {
        super.setupListener()
        binding.apply {
            btnLanguage.setOnSingleClickListener {
                findNavController().navigate(R.id.languageFragment)
            }
            btnRate.setOnSingleClickListener {
                BkPlusAppOpenAdManager.disableAdResume()
                activity.openAppInPlayStore()
            }

            btnShare.setOnSingleClickListener {
                activity.openShare()
            }
            btnPolicy.setOnSingleClickListener {
                BkPlusAppOpenAdManager.disableAdResume()
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(Constants.PRIVACY_POLICY_LINK)
                startActivity(intent)
            }
            btnTerm.setOnSingleClickListener {
                BkPlusAppOpenAdManager.disableAdResume()
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(Constants.TERMS_OF_USE_LINK)
                startActivity(intent)
            }
        }
    }
}