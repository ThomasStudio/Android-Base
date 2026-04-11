package com.thomas.androidbase.network

import com.thomas.androidbase.data.ZhihuHot
import com.thomas.base.domain.Response
import retrofit2.http.GET

/**
 * Created by thomas on 2/21/2025.
 */


interface ZhihuService {
    @GET("topstory/hot")
    suspend fun getHot(): Response<ZhihuHot>
}