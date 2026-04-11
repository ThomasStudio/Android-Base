package com.thomas.androidbase.features.home

import androidx.hilt.navigation.compose.hiltViewModel
import com.thomas.base.viewmodel.BaseViewModel
import com.thomas.androidbase.navigation.MainRoute
import com.thomas.base.viewmodel.MessageEvent
import com.thomas.base.viewmodel.UIState
import com.thomas.base.viewmodel.UIStateIF
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : BaseViewModel<UIState<HomeData>>(), HomeContract {
    override fun initialState() = UIState<HomeData>()

    override fun onClickNews() {
        navigate(MainRoute.News)
    }

    override fun onClickWeibo() {
        navigate(MainRoute.Weibo)
    }

    override fun showMessage() {
        send(MessageEvent("Hello from HomeViewModel"))
    }
}