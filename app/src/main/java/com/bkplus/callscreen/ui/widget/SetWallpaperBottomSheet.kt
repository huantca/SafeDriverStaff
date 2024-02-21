package com.bkplus.callscreen.ui.widget

import com.bkplus.callscreen.ultis.setOnSingleClickListener
import com.bkplus.callscreen.common.BaseBottomSheetDialog
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.BottomSheetSetWallpaperBinding

class SetWallpaperBottomSheet : BaseBottomSheetDialog<BottomSheetSetWallpaperBinding>() {
    override val layoutId: Int
        get() = R.layout.bottom_sheet_set_wallpaper

    override fun getTheme(): Int {
        return R.style.BottomSheetDialog
    }

    companion object {
        fun newInstance(
            onClickSetLockScreen: () -> Unit,
            onClickSeHomeScreen: () -> Unit,
            onClickSetBothScreen: () -> Unit,
        ): SetWallpaperBottomSheet {
            val fragment = SetWallpaperBottomSheet()
            fragment.onClickSetLockScreen = onClickSetLockScreen
            fragment.onClickSeHomeScreen = onClickSeHomeScreen
            fragment.onClickSetBothScreen = onClickSetBothScreen
            return fragment
        }
    }

    private var onClickSetLockScreen: () -> Unit = {}
    private var onClickSeHomeScreen: () -> Unit = {}
    private var onClickSetBothScreen: () -> Unit = {}

    override fun setupListener() {
        binding.apply {
            btnLockWallpaper.setOnSingleClickListener {
                onClickSetLockScreen.invoke()
                this@SetWallpaperBottomSheet.dismiss()
            }
            btnHomeWallpaper.setOnSingleClickListener {
                onClickSeHomeScreen.invoke()
                this@SetWallpaperBottomSheet.dismiss()
            }
            btnHomeAndLockWallpaper.setOnSingleClickListener {
                onClickSetBothScreen.invoke()
                this@SetWallpaperBottomSheet.dismiss()
            }
        }
    }
}