package com.thomas.androidbase.features.weibo

import com.thomas.base.viewmodel.BaseContract
import com.thomas.base.viewmodel.Error
import com.thomas.base.viewmodel.Status
import com.thomas.base.viewmodel.UIStateIF

interface WeiboContract : BaseContract<WeiboContract.State> {
    data class State(
        override val status: Status = Status.LOADING,
        override val data: Any? = null,
        override val error: Error? = null
    ) : UIStateIF
}