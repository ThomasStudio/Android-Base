package com.thomas.androidbase.features.home

import com.thomas.base.viewmodel.BaseDataContract

interface HomeContract : BaseDataContract<HomeData> {
    fun onClickNews()
    fun onClickWeibo()
    fun onClickComponents()
    fun loadingEvent()
    fun showMessage()
    fun onClickExamples()
}

data class HomeData(
    val content: String = "Android Base"
)