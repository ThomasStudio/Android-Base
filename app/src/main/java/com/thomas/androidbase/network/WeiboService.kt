package com.thomas.androidbase.network

import com.thomas.androidbase.data.WeiboHot
import com.thomas.base.domain.Response
import retrofit2.http.GET

/**
 * Created by thomas on 4/11/2026.
 */

interface WeiboService {
    @GET("weibohot")
    suspend fun getNews(): Response<WeiboHot>
}

