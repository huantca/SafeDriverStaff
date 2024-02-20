package com.bkplus.callscreen.ui.splash.onboard

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.bkplus.callscreen.common.BaseFragment
import com.bkplus.callscreen.common.BaseViewPagerAdapter
import com.bkplus.callscreen.ultis.setOnSingleClickListener
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.FragmentOnboardBinding

class OnboardFragment : BaseFragment<FragmentOnboardBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_onboard

    private lateinit var adapterVP: BaseViewPagerAdapter
    override fun setupUI() {
        super.setupUI()
        adapterVP = BaseViewPagerAdapter(childFragmentManager, lifecycle)
        val listFragment: ArrayList<Fragment> = arrayListOf(
            ChildOnboardFragment.newInstance(
                R.drawable.ob1,
                getString(R.string.ob1_title),
                getString(R.string.ob1_description)
            ),
            ChildOnboardFragment.newInstance(
                R.drawable.ob2,
                getString(R.string.ob2_title),
                getString(R.string.ob2_description)
            ),
            ChildOnboardFragment.newInstance(
                R.drawable.ob3,
                getString(R.string.ob3_title),
                getString(R.string.ob3_description)
            ),
            ChildOnboardFragment.newInstance(
                R.drawable.ob4,
                getString(R.string.ob4_title),
                getString(R.string.ob4_description)
            ),
        )
        adapterVP.init(listFragment)
        binding.viewpagerOnboard.adapter = adapterVP
        binding.viewpagerOnboard.offscreenPageLimit = 4
        binding.indicatorView.attachTo(binding.viewpagerOnboard)
    }

    override fun setupListener() {
        super.setupListener()
        binding.apply {
            viewpagerOnboard.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    if (position == 3) {
                        continueButton.setText(R.string.get_started_btn)
                    } else {
                        continueButton.setText(R.string.next)
                    }
                    when (position) {
//                    0 -> AppsFlyerLib.getInstance().logEvent(context, "fb001_onboarding_1", mapOf())
//                    1 -> AppsFlyerLib.getInstance().logEvent(context, "fb001_onboarding_2", mapOf())
//                    2 -> AppsFlyerLib.getInstance().logEvent(context, "fb001_onboarding_3", mapOf())
//                    3 -> AppsFlyerLib.getInstance().logEvent(context, "fb001_onboarding_4", mapOf())
                    }
                }
            })

            continueButton.setOnSingleClickListener {
                if (viewpagerOnboard.currentItem < 3) {
                    viewpagerOnboard.currentItem = viewpagerOnboard.currentItem + 1
                } else {
                    findNavController().navigate(R.id.welcomeFragment)
                }
            }
        }
    }
}
