package com.bkplus.callscreen.ui.viewlike

import android.net.Uri
import android.widget.ImageView
import com.bkplus.callscreen.ultis.loadImage
import com.harison.core.app.platform.BaseFullScreenDialogFragment
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.FragmentPreviewWallpaperBinding

class PreviewDialogFragment : BaseFullScreenDialogFragment<FragmentPreviewWallpaperBinding>() {

    override val layoutId: Int
        get() = R.layout.fragment_preview_wallpaper

    private fun setupLoadImage(uri: Uri?, url: String?, imageView: ImageView) {
        if (uri != null) {
            imageView.loadImage(uri)
        }

        if (url != null) {
            imageView.loadImage(url)
        }
    }
}