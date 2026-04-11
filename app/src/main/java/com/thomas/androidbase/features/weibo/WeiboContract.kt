package com.thomas.androidbase.features.weibo

import com.thomas.androidbase.data.WeiboHot
import com.thomas.base.viewmodel.BaseContract
import com.thomas.base.viewmodel.UIState

interface WeiboContract : BaseContract<UIState<WeiboHot>> {
    fun showMessage()
}