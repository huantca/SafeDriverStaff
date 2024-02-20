package com.bkplus.callscreen.ui.viewlike

import android.app.WallpaperManager
import android.content.Context
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.bkplus.callscreen.common.BaseFragment
import com.bkplus.callscreen.ultis.setOnSingleClickListener
import com.bumptech.glide.Glide
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.FragmentViewLikeItemBinding
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL

class ViewLikeItemFragment : BaseFragment<FragmentViewLikeItemBinding>() {

    override val layoutId: Int
        get() = R.layout.fragment_view_like_item

    private var wallPaper: WallPaper? = null

    fun initData(item: WallPaper) {
        wallPaper = item
    }

    override fun setupUI() {
        super.setupUI()

        wallPaper?.let { item ->
            Glide.with(binding.wallPaperImage.context).load(item.url).into(binding.wallPaperImage)
        }

        binding.apply {
            likeCounts.text = wallPaper?.likes.toString()
        }
    }

    override fun setupListener() {
        super.setupListener()

        binding.apply {

            likeBtn.setOnClickListener() {
                if (wallPaper?.isLiked == true) {
                    likeBtn.setImageResource(R.drawable.ic_heart_unfill)
                    wallPaper?.isLiked = false
                    wallPaper?.likes =- 1
                } else {
                    likeBtn.setImageResource(R.drawable.ic_heart_fill)
                    wallPaper?.isLiked = true
                    wallPaper?.likes =+ 1
                }
            }

            previewBtn.setOnSingleClickListener {
                PreviewDialogFragment.newInstance(
                    item = wallPaper,
                    onDismiss = {  }
                ).show(childFragmentManager, "")
            }
        }
    }
}