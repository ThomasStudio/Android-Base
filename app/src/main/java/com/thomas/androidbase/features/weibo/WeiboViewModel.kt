package com.thomas.androidbase.features.weibo

import com.thomas.androidbase.data.WeiboHot
import com.thomas.androidbase.data.repositories.WeiboRepository
import com.thomas.base.domain.Result
import com.thomas.base.viewmodel.BaseDataViewModel
import com.thomas.base.viewmodel.MessageEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeiboViewModel @Inject constructor(
    private val weiboRepository: WeiboRepository
) : BaseDataViewModel<WeiboHot>(), WeiboContract {
    override fun showMessage() {
        send(MessageEvent("Hello from WeiboViewModel"))
    }

    private fun getWeiboHot() {
        scope.launch {
            when (val result = weiboRepository.getWeiboHot()) {
                is Result.Success -> {
                    updateData(result.data)
                }

                is Result.Error -> {
                    showError(code = result.code, message = result.message ?: "")
                }
            }
        }
    }

    override fun viewCreated() {
        getWeiboHot()
    }

}