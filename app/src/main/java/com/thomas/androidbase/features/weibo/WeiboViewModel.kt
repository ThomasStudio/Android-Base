package com.thomas.androidbase.features.weibo

import com.thomas.base.viewmodel.BaseViewModel

class WeiboViewModel : BaseViewModel<WeiboContract.State>(), WeiboContract {
    override fun initialState(): WeiboContract.State {
        return WeiboContract.State()
    }
}