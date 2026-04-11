package com.thomas.androidbase.features.news

import com.thomas.androidbase.data.ZhihuHot
import com.thomas.base.viewmodel.BaseContract
import com.thomas.base.viewmodel.UIState

interface NewsContract : BaseContract<UIState<ZhihuHot>> {
    fun showMessage()
}