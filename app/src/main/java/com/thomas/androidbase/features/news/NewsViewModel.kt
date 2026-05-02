package com.thomas.androidbase.features.news

import com.thomas.androidbase.data.ZhihuHot
import com.thomas.androidbase.data.ZhihuNewsItem
import com.thomas.androidbase.data.repositories.ZhihuRepository
import com.thomas.androidbase.navigation.MainRoute
import com.thomas.base.viewmodel.BaseDataViewModel
import com.thomas.base.viewmodel.MessageEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val zhihuRepository: ZhihuRepository
) : BaseDataViewModel<ZhihuHot>(), NewsContract {
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
                    updateData(result.data)
                }

                is com.thomas.base.domain.Result.Error -> {
                    showError(code = result.code, message = result.message ?: "")
                }
            }
        }
    }

    override fun onItemClick(item: ZhihuNewsItem) {
        val data = item.target
        navigateWithJson(MainRoute.Web, "url" to data.url, "title" to data.question.title)
    }
}