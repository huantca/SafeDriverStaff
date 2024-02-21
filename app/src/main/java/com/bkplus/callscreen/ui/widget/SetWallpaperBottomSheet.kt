package com.bkplus.callscreen.ui.widget

import com.bkplus.callscreen.common.BaseBottomSheetDialog
import com.bkplus.callscreen.ultis.setOnSingleClickListener
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
            onClickSetWallpaper: () -> Unit
        ): SetWallpaperBottomSheet {
            val fragment = SetWallpaperBottomSheet()
            fragment.onClickSetLockScreen = onClickSetLockScreen
            fragment.onClickSeHomeScreen = onClickSeHomeScreen
            fragment.onClickSetBothScreen = onClickSetBothScreen
            fragment.onClickSetWallpaper = onClickSetWallpaper
            return fragment
        }
    }

    private var onClickSetLockScreen: () -> Unit = {}
    private var onClickSeHomeScreen: () -> Unit = {}
    private var onClickSetBothScreen: () -> Unit = {}
    private var onClickSetWallpaper: () -> Unit = {}

    override fun setupListener() {
        binding.apply {
            btnLockWallpaper.setOnSingleClickListener {
                this@SetWallpaperBottomSheet.dismiss()
                onClickSetLockScreen.invoke()
                onClickSetWallpaper.invoke()
            }
            btnHomeWallpaper.setOnSingleClickListener {
                this@SetWallpaperBottomSheet.dismiss()
                onClickSeHomeScreen.invoke()
                onClickSetWallpaper.invoke()
            }
            btnHomeAndLockWallpaper.setOnSingleClickListener {
                this@SetWallpaperBottomSheet.dismiss()
                onClickSetBothScreen.invoke()
                onClickSetWallpaper.invoke()
            }
        }
    }
}