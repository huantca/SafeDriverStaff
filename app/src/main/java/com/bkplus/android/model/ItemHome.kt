package com.bkplus.android.model

data class ItemHome(
    val name: String = "",
    var listItem: List<Item> = listOf(),
    var type: Int = 0
)
