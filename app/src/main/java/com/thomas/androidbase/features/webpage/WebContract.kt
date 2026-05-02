package com.thomas.androidbase.features.webpage

import com.thomas.base.viewmodel.BaseDataContract

/**
 * Created by thomas on 5/2/2026.
 */

interface WebContract : BaseDataContract<WebData>
data class WebData(
    val url: String,
    val title: String? = null,
)