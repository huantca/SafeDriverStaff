package com.bkplus.callscreen.ui.home

import android.os.Bundle
import com.bkplus.callscreen.common.BaseFragment
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.FragmentHomeBinding

class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_home

    companion object {
        fun newInstance(): HomeFragment {
            val args = Bundle()
            val fragment = HomeFragment()
            fragment.arguments = args
            return fragment
        }
    }

}