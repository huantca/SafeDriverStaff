package com.bkplus.callscreen.common

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.viewbinding.ViewBinding
import timber.log.Timber

abstract class BaseFragment<T : ViewBinding> : Fragment() {
    private var nameScreen = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nameScreen = getNameScreen()
        Timber.tag("----onCreate:").d(this.javaClass.simpleName)
        setupData()
        setupUI()
        setupListener()
    }

    protected abstract val layoutId: Int
    protected lateinit var binding: T
    private var dialog: Dialog? = null
    override fun onResume() {
        Timber.tag("Resume:").d(this.javaClass.simpleName)
        super.onResume()
    }


    protected open fun setupUI() {}
    protected open fun setupData() {}
    protected open fun setupListener() {}
    fun requireContext(action: (nonNullContext: Context) -> Unit) {
        context?.let(action)
    }

//    fun populateNative(
//        adsNative: ApNativeAd,
//        adPlaceHolder: FrameLayout,
//        shimmer: ShimmerFrameLayout
//    ) {
//        activity?.let {
//            AperoAd.getInstance().populateNativeAdView(
//                it, adsNative, adPlaceHolder, shimmer
//            )
//        }
//    }

    fun toast(message: String, isLong: Boolean? = false) {
        context?.let {
            Toast.makeText(
                it,
                message,
                if (isLong == false) Toast.LENGTH_SHORT else Toast.LENGTH_LONG
            )
                .show()
        }
    }

    private fun getNameScreen(): String {
        val input = this.javaClass.simpleName.replace("Fragment", "Screen", ignoreCase = true)
        val result = StringBuilder()
        for (char in input) {
            if (char.isUpperCase()) {
                result.append('_').append(char.lowercaseChar())
            } else {
                result.append(char)
            }
        }
        return if (result.isNotEmpty() && result[0] == '_') result.substring(1) else result.toString()
    }

    fun NavController.safeNavigateWithArgs(direction: NavDirections, bundle: Bundle? = Bundle()) {
        currentDestination?.getAction(direction.actionId)?.run {
            navigate(direction.actionId, bundle)
        }
    }
}
