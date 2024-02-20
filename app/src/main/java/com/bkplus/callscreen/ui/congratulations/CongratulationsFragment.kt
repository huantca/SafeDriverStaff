package com.bkplus.callscreen.ui.congratulations

import androidx.navigation.fragment.findNavController
import com.bkplus.callscreen.common.BaseFragment
import com.bkplus.callscreen.ultis.setOnSingleClickListener
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.FragmentCongratulationsBinding

class CongratulationsFragment: BaseFragment<FragmentCongratulationsBinding>() {

    override val layoutId: Int
        get() = R.layout.fragment_congratulations

    override fun setupListener() {
        super.setupListener()
        binding.apply {
            backBtn.setOnSingleClickListener {
                findNavController().popBackStack()
            }

            homeBtn.setOnSingleClickListener {
                findNavController().popBackStack(R.id.homeFragment, false)
            }
        }
    }
}