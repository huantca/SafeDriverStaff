package com.bkplus.callscreen.ui.history

import android.os.Bundle
import com.bkplus.callscreen.common.BaseFragment
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.FragmentHistoryBinding

class HistoryFragment: BaseFragment<FragmentHistoryBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_history

    companion object {
        fun newInstance(): HistoryFragment {
            val args = Bundle()
            val fragment = HistoryFragment()
            fragment.arguments = args
            return fragment
        }
    }
}