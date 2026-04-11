package com.thomas.androidbase.data.repositories

import com.thomas.androidbase.data.WeiboHot
import com.thomas.androidbase.network.WeiboService
import com.thomas.base.domain.Repository
import com.thomas.base.domain.Result
import javax.inject.Inject

/**
 * Created by thomas on 4/11/2026.
 */

interface WeiboRepository {
    suspend fun getWeiboHot(): Result<WeiboHot>
}

class WeiboRepositoryImpl @Inject constructor(
    private val weiboService: WeiboService
) : WeiboRepository, Repository() {
    override suspend fun getWeiboHot(): Result<WeiboHot> {
        return runCall { weiboService.getNews() }
    }
}