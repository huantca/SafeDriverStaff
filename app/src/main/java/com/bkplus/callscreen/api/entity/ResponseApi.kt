package com.bkplus.callscreen.api.entity

import com.google.gson.annotations.SerializedName

data class ResponseApi (
    @SerializedName("home_section")
    var homeSection: ArrayList<HomeSectionEntity> = arrayListOf()
 )