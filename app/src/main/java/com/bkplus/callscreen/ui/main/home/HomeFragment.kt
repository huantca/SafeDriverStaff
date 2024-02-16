package com.bkplus.callscreen.ui.main.home

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import com.bkplus.callscreen.api.entity.HomeSectionEntity
import com.bkplus.callscreen.common.BaseFragment
import com.bkplus.callscreen.ui.main.home.adapter.HomeAdapter
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.FragmentHomeBinding

class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_home
    private var adapter: HomeAdapter? = null

    companion object {
        fun newInstance(): HomeFragment {
            val args = Bundle()
            val fragment = HomeFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun setupData() {
        super.setupData()
        adapter = HomeAdapter()
        val arrayList = ArrayList<HomeSectionEntity>()
        arrayList.add(HomeSectionEntity())
        adapter?.updateItems(arrayList)
        binding.rcyHome.adapter = adapter
        binding.rcyHome.setHasFixedSize(true)
    }

    override fun setupListener() {
        binding.apply {
            imgSearch.setOnClickListener {
                findNavController().navigate(R.id.searchFragment)
            }
        }
    }
}