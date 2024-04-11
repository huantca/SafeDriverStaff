package com.bkplus.android.ui.main.user

import androidx.navigation.fragment.findNavController
import com.bkplus.android.common.BaseFragment
import com.bkplus.android.ultis.setOnSingleClickListener
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.FragmentUserBinding

class UserFragment : BaseFragment<FragmentUserBinding>() {

    override val layoutId: Int
        get() = R.layout.fragment_user


    override fun setupListener() {
        super.setupListener()
        binding.apply {
            tvNext.setOnSingleClickListener {
                findNavController().navigate(R.id.tripUserFragment)
            }
        }
    }
}