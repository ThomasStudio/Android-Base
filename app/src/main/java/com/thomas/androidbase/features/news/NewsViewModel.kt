package com.thomas.androidbase.features.news

import com.thomas.androidbase.data.ZhihuHot
import com.thomas.androidbase.data.repositories.ZhihuRepository
import com.thomas.base.viewmodel.BaseViewModel
import com.thomas.base.viewmodel.Error
import com.thomas.base.viewmodel.MessageEvent
import com.thomas.base.viewmodel.Status
import com.thomas.base.viewmodel.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val zhihuRepository: ZhihuRepository
) : BaseViewModel<UIState<ZhihuHot>>(), NewsContract {
    override fun initialState() = UIState<ZhihuHot>()

    override fun showMessage() {
        send(MessageEvent("Hello from NewsViewModel"))
    }

    override fun viewCreated() {
        getZhihuHot()
    }

    fun getZhihuHot() {
        scope.launch {
            when (val result = zhihuRepository.getZhihuHot()) {
                is com.thomas.base.domain.Result.Success -> {
                    updateState { copy(status = Status.SUCCESS, data = result.data) }
                }
                is com.thomas.base.domain.Result.Error -> {
                    updateState { copy(status = Status.ERROR, error = Error(code = result.code, message = result.message ?: "")) }
                }
            }
        }
    }
}