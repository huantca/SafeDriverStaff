package com.bkplus.android.ui.splash.welcome

import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bkplus.android.ads.EventTracking
import com.bkplus.android.ads.TrackingManager
import com.bkplus.android.common.BaseFragment
import com.bkplus.android.common.BasePrefers
import com.bkplus.android.ultis.setOnSingleClickListener
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.FragmentWelcomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomeFragment : BaseFragment<FragmentWelcomeBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_welcome

    private val viewModel: WelcomeViewModel by viewModels()
    private lateinit var adapter: WelcomeAdapter

    override fun setupUI() {
        super.setupUI()
        TrackingManager.tracking(EventTracking.fb011_welcome)
        context?.let {
            binding.continueButton.apply {
                backgroundTintList =
                    ContextCompat.getColorStateList(it, R.color.naviColor1)
                setTextColor(ContextCompat.getColorStateList(it, R.color.naviColor4))
                isEnabled = false
            }
        }
        adapter = WelcomeAdapter().apply {
            setOnItemClickListener {
                context?.let {
                    if (this.hasSelectedItem()) {
                        binding.continueButton.apply {
                            backgroundTintList = ContextCompat.getColorStateList(it, R.color.primary100)
                            elevation = 5f
                            setTextColor(ContextCompat.getColorStateList(it, R.color.white))
                            isEnabled = true
                        }
                    } else {
                        binding.continueButton.apply {
                            backgroundTintList =
                                ContextCompat.getColorStateList(it, R.color.naviColor1)
                            setTextColor(ContextCompat.getColorStateList(it, R.color.naviColor4))
                            isEnabled = false
                        }
                    }
                }
            }
        }
        viewModel.categoriesLiveData.observe(viewLifecycleOwner) {
            adapter.updateData(it)
        }
        binding.rv.adapter = adapter
        viewModel.fetchCategories()
    }

    override fun setupListener() {
        super.setupListener()
        binding.apply {
            continueButton.setOnSingleClickListener {
                TrackingManager.tracking(EventTracking.fb011_welcome_next)
                if (adapter.hasSelectedItem()) {
                    goToHome()
                }
            }
        }
    }

    private fun goToHome() {
        BasePrefers.getPrefsInstance().doneWelcome = true
        findNavController().navigate(R.id.homeFragment)
    }

    private fun removeNativeAds() {
        binding.shimmerContainerNative1.stopShimmer()
        binding.shimmerContainerNative1.visibility = View.GONE
        binding.flAdplaceholderActivity.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
