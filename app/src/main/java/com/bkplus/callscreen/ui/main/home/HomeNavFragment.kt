package com.bkplus.callscreen.ui.main.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bkplus.callscreen.common.BaseFragment
import com.bkplus.callscreen.ui.main.category.CategoryFragment
import com.bkplus.callscreen.ui.main.history.FragmentHistory
import com.bkplus.callscreen.ui.setting.SettingFragment
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.tabs.TabLayoutMediator
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.FragmentHomeNavBinding

class HomeNavFragment : BaseFragment<FragmentHomeNavBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_home_nav

    private var mHomeFragment: HomeFragment? = null
    private var mSettingFragment : SettingFragment?= null
    private var mCategoryFragment : CategoryFragment?= null
    private var mHistoryFragment : FragmentHistory?= null
    private val mOnNavigationItemSelectedListener =
        NavigationBarView.OnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> {
                    binding.viewPager2.currentItem = 0
                    return@OnItemSelectedListener true
                }

                R.id.categoryFragment -> {
                   binding.viewPager2.currentItem = 1
                    return@OnItemSelectedListener true
                }

                R.id.historyFragment -> {
                    binding.viewPager2.currentItem = 2
                    return@OnItemSelectedListener true
                }

                R.id.settingFragment -> {
                    binding.viewPager2.currentItem = 3
                    return@OnItemSelectedListener true
                }
            }
            false
        }

    override fun setupUI() {
        super.setupUI()
        initiateView()
    }
    private fun initiateView() {
        val pagerAdapter =  ScreenSlidePagerAdapter(this)
        binding.viewPager2.offscreenPageLimit = 3
        binding.viewPager2.adapter = pagerAdapter
        binding.viewPager2.isUserInputEnabled = false
        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { _, _ -> }.attach()

        binding.bottomNav.setOnItemSelectedListener(mOnNavigationItemSelectedListener)

        binding.viewPager2.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    0 -> {
                        binding.bottomNav.menu.findItem(R.id.homeFragment).isChecked = true
                    }

                    1 -> {
                        binding.bottomNav.menu.findItem(R.id.categoryFragment).isChecked = true
                    }

                    2 -> {
                        binding.bottomNav.menu.findItem(R.id.historyFragment).isChecked = true
                    }

                    3 -> {
                        binding.bottomNav.menu.findItem(R.id.settingFragment).isChecked = true
                    }
                }
            }
        })
    }

    private inner class ScreenSlidePagerAdapter(fa: Fragment) : FragmentStateAdapter(fa) {
        override fun getItemCount() = 4
        override fun createFragment(position: Int): Fragment =
            handleOnNavigationItemSelected(position)
    }

    private fun handleOnNavigationItemSelected(itemId: Int) = when (itemId) {
        1 -> getFragmentForIndex(1)
        2 -> getFragmentForIndex(2)
        3 -> getFragmentForIndex(3)
        else -> getFragmentForIndex(0)
    }

    private fun initFragmentAt(position: Int): Fragment {
        when (position) {
            1 -> mCategoryFragment = CategoryFragment.newInstance()
            2 -> mHistoryFragment = FragmentHistory.newInstance()
            3 -> mSettingFragment = SettingFragment.newInstance()
            else -> mHomeFragment = HomeFragment.newInstance()
        }
        return handleOnNavigationItemSelected(position)
    }

    private fun getFragmentForIndex(index: Int) = when (index) {
        1 -> mCategoryFragment ?: initFragmentAt(index)
        2 ->  mHistoryFragment ?: initFragmentAt(index)
        3 -> mSettingFragment ?: initFragmentAt(index)
        else -> mHomeFragment ?: initFragmentAt(index)
    }
}