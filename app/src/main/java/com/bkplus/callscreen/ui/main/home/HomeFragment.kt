package com.bkplus.callscreen.ui.main.home

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bkplus.callscreen.api.entity.HomeSectionEntity
import com.bkplus.callscreen.common.BaseFragment
import com.bkplus.callscreen.common.BasePrefers
import com.bkplus.callscreen.ui.main.home.adapter.HomeAdapter
import com.bkplus.callscreen.ui.main.home.viewmodel.HomeViewModel
import com.bkplus.callscreen.ui.viewlike.WallPaper
import com.bkplus.callscreen.ui.widget.ForceUpdateDialog
import com.harrison.myapplication.BuildConfig
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.FragmentHomeBinding
import com.qrcode.ai.app.ui.main.widget.OptionUpdateDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.max

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_home
    private var adapter: HomeAdapter? = null
    private val viewModel: HomeViewModel by viewModels()

    companion object {
        fun newInstance(): HomeFragment {
            val args = Bundle()
            val fragment = HomeFragment()
            fragment.arguments = args
            return fragment
        }

        var data = ArrayList<HomeSectionEntity>()
        var isShowDialogForceUpdate: Boolean? = true
    }

    override fun setupData() {
        super.setupData()
        viewModel.getHomeSection()
        adapter = HomeAdapter()
        adapter?.onItemRcvClick = { item, listData ->
            val item = WallPaper(id = item.id, url = item.url)
            val listItem = listData.map { item ->
                WallPaper(id = item.id, url = item.url)
            }.toTypedArray()
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToViewLikeContainerFragment(
                    item,
                    listItem
                )
            )
        }
        adapter?.viewAll = {
            val topTrendyFragment = TopTrendyFragment().apply {
                setData(it)
            }

            topTrendyFragment.dismissDialog = {
            }
            topTrendyFragment.show(childFragmentManager, "")
        }
        viewModel.homeSectionLiveData.observe(viewLifecycleOwner) {
            adapter?.updateItems(it)
        }
        binding.rcyHome.adapter = adapter
    }

    override fun setupListener() {
        super.setupListener()
        binding.apply {
            imgSearch.setOnClickListener {
                findNavController().navigate(R.id.searchFragment)
            }
        }
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