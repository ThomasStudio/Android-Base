package com.thomas.androidbase.features.home

import com.thomas.base.viewmodel.BaseContract
import com.thomas.base.viewmodel.Error
import com.thomas.base.viewmodel.Status
import com.thomas.base.viewmodel.UIState
import com.thomas.base.viewmodel.UIStateIF

interface HomeContract : BaseContract<UIState<HomeData>> {
    fun onClickNews()
    fun onClickWeibo()
    fun showMessage()
}

data class HomeData(
    val content: String = "hello world"
)