package com.bkplus.android.ui.splash.onboard

import android.os.Bundle
import com.bkplus.android.ads.AdsContainer
import com.bkplus.android.common.BaseFragment
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.FragmentOnboardChildBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChildOnboardFragment : BaseFragment<FragmentOnboardChildBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_onboard_child

    @Inject
    lateinit var adsContainer: AdsContainer
    private var gotNativeAdResponse = false
    private var stopped = false

    companion object {
        private const val IMAGE_BUNDLE = "IMAGE_BUNDLE"
        private const val TEXT_TITLE = "TEXT_TITLE"
        private const val TEXT_DES = "TEXT_DES"
        fun newInstance(
            image: Int?,
            textTitle: String?,
            textDescription: String?
        ): ChildOnboardFragment {
            val args = Bundle()
            args.putInt(IMAGE_BUNDLE, image ?: R.drawable.ob1)
            args.putString(TEXT_TITLE, textTitle.orEmpty())
            args.putString(TEXT_DES, textDescription.orEmpty())
            val fragment = ChildOnboardFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private var image = R.drawable.ob1
    private var textTitle = ""
    private var textDescription = ""

    override fun setupData() {
        super.setupData()
        arguments?.let {
            image = it.getInt(IMAGE_BUNDLE)
            textTitle = it.getString(TEXT_TITLE, "")
            textDescription = it.getString(TEXT_DES, "")
        }
    }

    override fun setupUI() {
        super.setupUI()
        binding.apply {
            imageOnboard.setImageResource(image)
            tvTitle.text = textTitle
            tvDescription.text = textDescription
        }
    }

}
