package com.bkplus.callscreen.ui.category

import android.os.Bundle
import com.bkplus.callscreen.common.BaseFragment
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.FragmentCategoryBinding

class CategoryFragment: BaseFragment<FragmentCategoryBinding>() {

    override val layoutId: Int
        get() = R.layout.fragment_category

    companion object {
        fun newInstance(): CategoryFragment {
            val args = Bundle()
            val fragment = CategoryFragment()
            fragment.arguments = args
            return fragment
        }
    }
}