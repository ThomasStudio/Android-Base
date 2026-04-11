package com.thomas.androidbase.data.repositories

import com.thomas.androidbase.data.ZhihuHot
import com.thomas.androidbase.network.ZhihuService
import com.thomas.base.domain.Repository
import com.thomas.base.domain.Result
import javax.inject.Inject

/**
 * Created by thomas on 4/11/2026.
 */

class ZhihuRepositoryImpl @Inject constructor(
    private val zhihuService: ZhihuService
) : ZhihuRepository, Repository() {
    override suspend fun getZhihuHot(): Result<ZhihuHot> {
        return runCall { zhihuService.getHot() }
    }
}