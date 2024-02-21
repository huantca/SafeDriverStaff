package com.bkplus.callscreen.ui.viewlike

import android.annotation.SuppressLint
import android.app.WallpaperManager
import android.content.Context
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import com.bkplus.callscreen.common.BaseFragment
import com.bkplus.callscreen.ultis.openShare
import com.bkplus.callscreen.ultis.setOnSingleClickListener
import com.bumptech.glide.Glide
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.FragmentViewLikeItemBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL

@AndroidEntryPoint
class ViewLikeItemFragment : BaseFragment<FragmentViewLikeItemBinding>() {

    override val layoutId: Int
        get() = R.layout.fragment_view_like_item

    private var wallPaper: WallPaper? = null
    private val viewModel: ViewLikeViewModel by viewModels()
    private var likeCountLive: Int? = null

    fun initData(item: WallPaper) {
        wallPaper = item
        likeCountLive = wallPaper?.likeCount
    }

    override fun setupUI() {
        super.setupUI()

        wallPaper?.let { item ->
            Glide.with(binding.wallPaperImage.context).load(item.url).into(binding.wallPaperImage)
        }

        binding.apply {
            likeCounts.text = likeCountLive.toString()
            if (wallPaper?.isLiked == false) likeBtn.setImageResource(R.drawable.ic_heart_unfill)
            else likeBtn.setImageResource(R.drawable.ic_heart_fill)
        }
    }

    override fun setupListener() {
        super.setupListener()

        binding.apply {

            likeBtn.setOnClickListener() {
                handleLikeAction()
            }

            shareBtn.setOnSingleClickListener {
                activity.openShare()
            }

            previewBtn.setOnSingleClickListener {
                PreviewDialogFragment.newInstance(
                    item = wallPaper,
                    onDismiss = {  }
                ).show(childFragmentManager, "")
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun handleLikeAction() {
        binding.apply {
                if (wallPaper?.isLiked == true) {
                    likeBtn.setImageResource(R.drawable.ic_heart_unfill)
                    wallPaper?.isLiked = false
                    likeCountLive = likeCountLive?.minus(1)
                    viewModel.disperse(wallPaper)
                } else {
                    likeBtn.setImageResource(R.drawable.ic_heart_fill)
                    wallPaper?.isLiked = true
                    likeCountLive = likeCountLive?.plus(1)
                    viewModel.saveFavourite(wallPaper)
                }
            likeCounts.text = likeCountLive.toString()
            likeCountLive = likeCounts.text.toString().toInt()
        }
    }
}