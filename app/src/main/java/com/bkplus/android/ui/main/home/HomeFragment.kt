package com.bkplus.android.ui.main.home

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import com.bkplus.android.ads.AdsContainer
import com.bkplus.android.ads.EventTracking
import com.bkplus.android.ads.TrackingManager
import com.bkplus.android.common.BaseFragment
import com.bkplus.android.common.BasePrefers
import com.bkplus.android.ui.widget.ForceUpdateDialog
import com.harrison.myapplication.BuildConfig
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.FragmentHomeBinding
import com.qrcode.ai.app.ui.main.widget.OptionUpdateDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.math.max

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    @Inject
    lateinit var adsContainer: AdsContainer
    override val layoutId: Int
        get() = R.layout.fragment_home
    private val viewModel: HomeViewModel by activityViewModels()

    companion object {
        fun newInstance(): HomeFragment {
            val args = Bundle()
            val fragment = HomeFragment()
            fragment.arguments = args
            return fragment
        }

        var isShowDialogForceUpdate: Boolean? = true
    }

    override fun setupUI() {
        super.setupUI()
        TrackingManager.tracking(EventTracking.fb011_home_view)
        setupShowForceUpdate()
    }

    override fun setupData() {
        super.setupData()
    }

    override fun setupListener() {
        super.setupListener()
    }

    private fun setupShowForceUpdate() {
        when (compareVersions(BasePrefers.getPrefsInstance().app_version_latest.toString())) {
            0, -1 -> return
        }
        when (compareVersions(BasePrefers.getPrefsInstance().app_version_force_update.toString())) {
            1 -> {
                val dialog = ForceUpdateDialog()
                dialog.isCancelable = false
                dialog.show(childFragmentManager, null)
            }

            0, -1 -> {
                if (isShowDialogForceUpdate == true) {
                    OptionUpdateDialog().show(childFragmentManager, null)
                    isShowDialogForceUpdate = false
                }
            }
        }
    }

    private fun compareVersions(versionA: String): Int {
        val versionTokensA = versionA.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        val versionTokensB =
            BuildConfig.VERSION_NAME.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
        val versionNumbersA: MutableList<Int> = ArrayList()
        val versionNumbersB: MutableList<Int> = ArrayList()
        for (versionToken in versionTokensA) {
            versionNumbersA.add(versionToken.toInt())
        }
        for (versionToken in versionTokensB) {
            versionNumbersB.add(versionToken.toInt())
        }
        val versionASize = versionNumbersA.size
        val versionBSize = versionNumbersB.size
        val maxSize = max(versionASize, versionBSize)
        for (i in 0 until maxSize) {
            if ((if (i < versionASize) versionNumbersA[i] else 0) > (if (i < versionBSize) versionNumbersB[i] else 0)) {
                return 1
            } else if ((if (i < versionASize) versionNumbersA[i] else 0) < (if (i < versionBSize) versionNumbersB[i] else 0)) {
                return -1
            }
        }
        return 0
    }
}
