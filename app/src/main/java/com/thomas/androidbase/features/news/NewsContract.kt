package com.thomas.androidbase.features.news

import com.thomas.base.viewmodel.BaseContract
import com.thomas.base.viewmodel.UIState

interface NewsContract : BaseContract<UIState<NewsData>> {
    fun showMessage()
}

data class NewsData(
    val content: String = "News content"
)