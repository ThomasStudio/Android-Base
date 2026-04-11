package com.thomas.androidbase.features.weibo

import com.thomas.base.viewmodel.BaseViewModel
import com.thomas.base.viewmodel.MessageEvent
import com.thomas.base.viewmodel.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WeiboViewModel @Inject constructor() : BaseViewModel<UIState<WeiboData>>(), WeiboContract {
    override fun initialState() = UIState<WeiboData>()

    override fun showMessage() {
        send(MessageEvent("Hello from WeiboViewModel"))
    }
}