package com.thomas.androidbase.features.news

import com.thomas.base.viewmodel.BaseViewModel

class NewsViewModel : BaseViewModel<NewsContract.State>(), NewsContract {
    override fun initialState(): NewsContract.State {
        return NewsContract.State()
    }
}