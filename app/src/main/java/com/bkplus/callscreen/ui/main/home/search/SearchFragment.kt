package com.bkplus.callscreen.ui.main.home.search

import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.bkplus.callscreen.common.BaseFragment
import com.bkplus.callscreen.ui.main.home.search.adapter.Category
import com.bkplus.callscreen.ui.main.home.search.adapter.CategoryAdapter
import com.bkplus.callscreen.ui.main.home.search.adapter.HashTagAdapter
import com.bkplus.callscreen.ui.main.home.search.adapter.HashTag
import com.bkplus.callscreen.ui.main.home.search.adapter.SearchAdapter
import com.bkplus.callscreen.ui.main.home.search.adapter.SearchItem
import com.bkplus.callscreen.ui.main.home.search.adapter.Trending
import com.bkplus.callscreen.ui.main.home.search.adapter.TrendingAdapter
import com.bkplus.callscreen.ultis.gone
import com.bkplus.callscreen.ultis.visible
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.FragmentSearchBinding

class SearchFragment : BaseFragment<FragmentSearchBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_search

    override fun setupData() {
        binding.apply {
            recyclerViewCategory.adapter = CategoryAdapter().apply { updateItems(listCategory) }
            recyclerViewHashtag.adapter = HashTagAdapter().apply { updateItems(listHashTag) }
            recyclerViewTrending.adapter = TrendingAdapter().apply { updateItems(listTrending) }
            recyclerSearch.adapter = SearchAdapter().apply { updateItems(listSearch) }
        }
    }

    override fun setupListener() {
        binding.apply {
            icBack.setOnClickListener {
                findNavController().popBackStack()
            }

            editTextId.addTextChangedListener {
                if (editTextId.text.isNotBlank()) {
                    recyclerSearch.visible()
                    layoutBottom.gone()
                } else {
                    recyclerSearch.gone()
                    layoutBottom.visible()
                }
            }
        }
    }

    val listCategory = arrayListOf(
        Category("", "", 0),
        Category("", "", 0),
        Category("", "", 0),
        Category("", "", 0),
    )

    val listTrending = arrayListOf(
        Trending("", 0, true),
        Trending("", 0, true),
        Trending("", 0, true),
        Trending("", 0, true),
        Trending("", 0, true),
        Trending("", 0, true),
        Trending("", 0, true),
        Trending("", 0, true),
        Trending("", 0, true),
        Trending("", 0, true),
    )

    val listHashTag = arrayListOf(
        HashTag(""),
        HashTag(""),
        HashTag(""),
        HashTag(""),
        HashTag(""),
        HashTag(""),
        HashTag(""),
        HashTag(""),
        HashTag(""),
    )

    val listSearch = arrayListOf(
        SearchItem(""),
        SearchItem(""),
        SearchItem(""),
        SearchItem(""),
        SearchItem(""),
        SearchItem(""),
        SearchItem(""),
        SearchItem(""),
        SearchItem(""),
        SearchItem(""),
        SearchItem(""),
        SearchItem(""),
    )
}
