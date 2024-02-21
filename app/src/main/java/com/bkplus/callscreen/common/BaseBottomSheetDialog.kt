package com.bkplus.callscreen.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import timber.log.Timber

abstract class BaseBottomSheetDialog<T : ViewDataBinding> : BottomSheetDialogFragment() {
    val mTag: String = this.javaClass.simpleName
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
        dialog?.window?.let {
            it.setBackgroundDrawableResource(android.R.color.transparent)
            WindowCompat.setDecorFitsSystemWindows(it, true)
            WindowInsetsControllerCompat(it, binding.root).let { controller ->
                controller.hide(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.navigationBars())
                controller.systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }
        setupData()
        setupUI()
        setupListener()
    }

    protected abstract val layoutId: Int
    protected lateinit var binding: T

    protected open fun setupUI() {}
    protected open fun setupData() {}
    protected open fun setupListener() {}

    override fun show(manager: FragmentManager, tag: String?) {
        try {
            val fragmentTransaction = manager.beginTransaction()
            fragmentTransaction.add(this, mTag)
            fragmentTransaction.commit()
        } catch (e: Exception) {
            Timber.d("Show Dialog Error: $mTag")
        }
    }

    fun show(manager: FragmentManager) {
        try {
            val fragmentTransaction = manager.beginTransaction()
            fragmentTransaction.add(this, mTag)
            fragmentTransaction.commit()
        } catch (e: Exception) {
            Timber.d("Show Dialog Error: $mTag")
        }
    }
}
