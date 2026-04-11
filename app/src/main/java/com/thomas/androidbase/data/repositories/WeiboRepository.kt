package com.thomas.androidbase.data.repositories

import com.thomas.androidbase.data.WeiboHot
import com.thomas.base.domain.Result

/**
 * Created by thomas on 4/11/2026.
 */

interface WeiboRepository {
    suspend fun getWeiboHot(): Result<WeiboHot>
}