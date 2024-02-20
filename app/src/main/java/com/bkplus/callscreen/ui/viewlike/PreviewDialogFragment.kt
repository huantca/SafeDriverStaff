package com.bkplus.callscreen.ui.viewlike

import android.net.Uri
import android.widget.ImageView
import androidx.core.os.bundleOf
import com.bkplus.callscreen.ultis.loadImage
import com.harison.core.app.platform.BaseFullScreenDialogFragment
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.FragmentPreviewWallpaperBinding

class PreviewDialogFragment : BaseFullScreenDialogFragment<FragmentPreviewWallpaperBinding>() {

    override val layoutId: Int
        get() = R.layout.fragment_preview_wallpaper

    var onDismissDialog: () -> Unit = {}

    companion object {

        const val AVATAR_URL_BUNDLE = "AVATAR_URL_BUNDLE"

        fun newInstance(
            item: WallPaper? = null,
            onDismiss: () -> Unit
        ) : PreviewDialogFragment {
            val args = bundleOf(
                AVATAR_URL_BUNDLE to item?.url
            )
            val fragment = PreviewDialogFragment()
            fragment.arguments = args
            fragment.onDismissDialog = onDismiss
            return fragment
        }
    }

    private fun setupLoadImage(uri: Uri?, url: String?, imageView: ImageView) {
        if (uri != null) {
            imageView.loadImage(uri)
        }

        if (url != null) {
            imageView.loadImage(url)
        }
    }
}