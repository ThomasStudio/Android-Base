package com.thomas.androidbase.features.home

import com.thomas.base.viewmodel.BaseDataContract
import com.thomas.base.viewmodel.DefaultContract

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

class FakeHomeContract() : DefaultContract<HomeData>(), HomeContract {
    override fun initialData() = HomeData()

    override fun onClickNews() {}
    override fun onClickWeibo() {}
    override fun onClickComponents() {}
    override fun loadingEvent() {}
    override fun showMessage() {}
    override fun onClickExamples() {}
}
