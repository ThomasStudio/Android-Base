package com.thomas.androidbase.data

import com.google.gson.annotations.SerializedName

/**
 * Created by thomas on 4/11/2026.
 */

data class WeiboHot(
    @SerializedName("code")
    val code: Int,
    
    @SerializedName("msg")
    val msg: String,
    
    @SerializedName("data")
    val data: List<HotItem>
)

data class HotItem(
    @SerializedName("hot")
    val hot: String,
    
    @SerializedName("index")
    val index: Int,
    
    @SerializedName("title")
    val title: String,
    
    @SerializedName("url")
    val url: String
)