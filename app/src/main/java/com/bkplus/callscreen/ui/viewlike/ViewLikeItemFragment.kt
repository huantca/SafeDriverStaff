package com.bkplus.callscreen.ui.viewlike

import com.bkplus.callscreen.common.BaseFragment
import com.bkplus.callscreen.ultis.setOnSingleClickListener
import com.bumptech.glide.Glide
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.FragmentViewLikeItemBinding

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
    }

    override fun setupListener() {
        super.setupListener()

        binding.apply {
            previewBtn.setOnSingleClickListener {
                PreviewDialogFragment.newInstance(
                    item = wallPaper,
                    onDismiss = {  }
                ).show(childFragmentManager, "")
            }
        }
    }
}