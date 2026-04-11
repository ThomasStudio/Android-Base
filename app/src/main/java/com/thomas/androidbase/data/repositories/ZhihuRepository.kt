package com.thomas.androidbase.data.repositories

import com.thomas.androidbase.data.ZhihuHot
import com.thomas.base.domain.Result

/**
 * Created by thomas on 4/11/2026.
 */

interface ZhihuRepository {
    suspend fun getZhihuHot(): Result<ZhihuHot>
}