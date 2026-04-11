package com.thomas.androidbase.features.weibo

 import com.thomas.androidbase.data.WeiboHot
import com.thomas.base.viewmodel.BaseViewModel
import com.thomas.base.viewmodel.MessageEvent
import com.thomas.base.viewmodel.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.thomas.base.viewmodel.Error
import com.thomas.androidbase.data.repositories.WeiboRepository
import com.thomas.base.domain.Result
import com.thomas.base.viewmodel.Status

@HiltViewModel
class WeiboViewModel @Inject constructor(
    private val weiboRepository: WeiboRepository
) : BaseViewModel<UIState<WeiboHot>>(), WeiboContract {
    override fun initialState() = UIState<WeiboHot>()

    override fun showMessage() {
        send(MessageEvent("Hello from WeiboViewModel"))
    }

    override fun getWeiboHot() {
        scope.launch {
            when (val result = weiboRepository.getWeiboHot()) {
                is Result.Success -> {
                    updateState { copy(status = Status.SUCCESS, data = result.data) }
                }
                is Result.Error -> {
                    updateState { copy(status = Status.ERROR, error = Error(code = result.code, message = result.message ?: "")) }
                }
            }
        }
    }
}