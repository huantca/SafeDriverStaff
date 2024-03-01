package com.bkplus.android.api.entity

import com.bkplus.android.ultis.Constants

data class ItemEntity(
    val id: Int,
    val url: String? = null,
    val category: String? = null
) {
    companion object {
        fun sampleListItem(): List<ItemEntity> {
            val rs = arrayListOf<ItemEntity>()
            for (i in 1..10) {
                rs.add(
                    ItemEntity(
                        id = i,
                        url = Constants.sample_url
                    )
                )
            }
            return rs
        }
    }
}