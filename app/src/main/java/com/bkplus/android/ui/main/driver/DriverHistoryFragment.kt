package com.bkplus.android.ui.main.driver

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bkplus.android.SharedViewModel
import com.bkplus.android.common.BaseFragment
import com.bkplus.android.ui.main.driver.adapter.HistoryAdapter
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.FragmentDriverHistoryBinding

class DriverHistoryFragment : BaseFragment<FragmentDriverHistoryBinding>() {

    override val layoutId: Int
        get() = R.layout.fragment_driver_history

    private val adapter = HistoryAdapter()
    private val viewModel : SharedViewModel by activityViewModels()

    override fun setupData() {
        super.setupData()
        binding.rcyHistory.adapter = adapter

        viewModel.historyDriverLiveData.observe(viewLifecycleOwner){
            adapter.updateItems(it)
        }
    }

    override fun setupListener() {
        super.setupListener()
        binding.icBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}