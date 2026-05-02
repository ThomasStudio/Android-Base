package com.thomas.androidbase.features.home

import com.thomas.androidbase.navigation.MainRoute
import com.thomas.base.viewmodel.BaseDataViewModel
import com.thomas.base.viewmodel.HideLoadingIndicatorEvent
import com.thomas.base.viewmodel.MessageEvent
import com.thomas.base.viewmodel.ShowLoadingIndicatorEvent
import com.thomas.base.viewmodel.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : BaseDataViewModel<HomeData>(), HomeContract {
    init {
        scope.launch {
            delay(1000)
            updateState {
                copy(
                    status = Status.SUCCESS,
                    data = HomeData()
                )
            }
        }
    }

    override fun onClickNews() {
        navigate(MainRoute.News)
    }

    override fun onClickWeibo() {
        navigate(MainRoute.Weibo)
    }

    override fun onClickComponents() {
        navigate(MainRoute.Components)
    }

    override fun loadingEvent() {
        scope.launch {
            send(ShowLoadingIndicatorEvent)
            delay(1000)
            send(HideLoadingIndicatorEvent)
        }
    }

    override fun showMessage() {
        send(MessageEvent("Hello from HomeViewModel"))
    }
}