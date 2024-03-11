package com.bkplus.android.ui.main.home

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import com.bkplus.android.ads.AdsContainer
import com.bkplus.android.common.BaseFragment
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

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

    }

    override fun setupUI() {
        super.setupUI()
    }

    override fun setupData() {
        super.setupData()
    }

    override fun setupListener() {
        super.setupListener()
    }

}
