package com.bkplus.android.ui.widget

import android.annotation.SuppressLint
import com.bkplus.android.ultis.setOnSingleClickListener
import com.harison.core.app.platform.BaseDialogFragment
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.LayoutCompleteDialogBinding

class CompleteDialog : BaseDialogFragment<LayoutCompleteDialogBinding>() {

    override val layoutId: Int
        get() = R.layout.layout_complete_dialog
    private var currentStar: Int = 5
    var action = {}
    var vote : ((Int) -> Unit ) ?= null
    override fun setupData() {
        super.setupData()
        setStars(5)
    }
    override fun setupListener() {
        super.setupListener()
        binding.apply {
            btnHome.setOnSingleClickListener{
                action.invoke()
            }
            btnVote.setOnClickListener {
                vote?.invoke(currentStar)
            }

            star1.setOnSingleClickListener { setStars(1) }
            star2.setOnSingleClickListener { setStars(2) }
            star3.setOnSingleClickListener { setStars(3) }
            star4.setOnSingleClickListener { setStars(4) }
            star5.setOnSingleClickListener { setStars(5) }
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun setStars(numberOfStars: Int) {
        currentStar = numberOfStars
        binding.apply {
            when (numberOfStars) {
                0 -> {
                    star1.setImageResource(R.drawable.ic_star_non_fill)
                    star2.setImageResource(R.drawable.ic_star_non_fill)
                    star3.setImageResource(R.drawable.ic_star_non_fill)
                    star4.setImageResource(R.drawable.ic_star_non_fill)
                    star5.setImageResource(R.drawable.ic_star_non_fill)
                }

                1 -> {
                    star1.setImageResource(R.drawable.ic_star_fill)
                    star2.setImageResource(R.drawable.ic_star_non_fill)
                    star3.setImageResource(R.drawable.ic_star_non_fill)
                    star4.setImageResource(R.drawable.ic_star_non_fill)
                    star5.setImageResource(R.drawable.ic_star_non_fill)
                }

                2 -> {
                    star1.setImageResource(R.drawable.ic_star_fill)
                    star2.setImageResource(R.drawable.ic_star_fill)
                    star3.setImageResource(R.drawable.ic_star_non_fill)
                    star4.setImageResource(R.drawable.ic_star_non_fill)
                    star5.setImageResource(R.drawable.ic_star_non_fill)
                }

                3 -> {
                    star1.setImageResource(R.drawable.ic_star_fill)
                    star2.setImageResource(R.drawable.ic_star_fill)
                    star3.setImageResource(R.drawable.ic_star_fill)
                    star4.setImageResource(R.drawable.ic_star_non_fill)
                    star5.setImageResource(R.drawable.ic_star_non_fill)
                }

                4 -> {
                    star1.setImageResource(R.drawable.ic_star_fill)
                    star2.setImageResource(R.drawable.ic_star_fill)
                    star3.setImageResource(R.drawable.ic_star_fill)
                    star4.setImageResource(R.drawable.ic_star_fill)
                    star5.setImageResource(R.drawable.ic_star_non_fill)
                }

                5 -> {
                    star1.setImageResource(R.drawable.ic_star_fill)
                    star2.setImageResource(R.drawable.ic_star_fill)
                    star3.setImageResource(R.drawable.ic_star_fill)
                    star4.setImageResource(R.drawable.ic_star_fill)
                    star5.setImageResource(R.drawable.ic_star_fill)
                }
            }
        }
    }
}