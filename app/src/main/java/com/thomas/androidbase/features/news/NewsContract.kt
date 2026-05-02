package com.thomas.androidbase.features.news

import com.thomas.androidbase.data.ZhihuHot
import com.thomas.androidbase.data.ZhihuNewsItem
import com.thomas.base.viewmodel.BaseContract
import com.thomas.base.viewmodel.BaseDataContract
import com.thomas.base.viewmodel.UIState

interface NewsContract : BaseDataContract<ZhihuHot> {
    fun showMessage()
    fun onItemClick(item: ZhihuNewsItem)
}