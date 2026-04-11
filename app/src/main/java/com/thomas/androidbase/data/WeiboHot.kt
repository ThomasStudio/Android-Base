package com.thomas.androidbase.data

/**
 * Created by thomas on 4/11/2026.
 */

data class WeiboHot(
    val code: Int,
    val msg: String,
    val data: List<HotItem>
)

data class HotItem(
    val hot: String,
    val index: Int,
    val title: String,
    val url: String
)
