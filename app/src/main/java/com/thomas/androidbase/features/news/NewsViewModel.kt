package com.thomas.androidbase.features.news

import com.thomas.base.viewmodel.BaseViewModel
import com.thomas.base.viewmodel.MessageEvent
import com.thomas.base.viewmodel.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor() : BaseViewModel<UIState<NewsData>>(), NewsContract {
    override fun initialState() = UIState<NewsData>()

    override fun showMessage() {
        send(MessageEvent("Hello from NewsViewModel"))
    }
}