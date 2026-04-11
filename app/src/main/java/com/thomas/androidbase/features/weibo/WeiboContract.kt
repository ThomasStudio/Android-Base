package com.thomas.androidbase.features.weibo

import com.thomas.base.viewmodel.BaseContract
import com.thomas.base.viewmodel.UIState

interface WeiboContract : BaseContract<UIState<WeiboData>> {
    fun showMessage()
}

data class WeiboData(
    val content: String = "Weibo content"
)