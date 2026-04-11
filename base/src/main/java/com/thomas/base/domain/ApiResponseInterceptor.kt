package com.thomas.base.domain

import com.google.gson.Gson
import com.google.gson.JsonParser
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okio.IOException
import kotlin.jvm.Throws
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import com.thomas.base.domain.Response as ApiResponse

/**
 * Created by thomas on 4/11/2026.
 */

open class ApiResponseInterceptor : Interceptor {
    private val gson = Gson()

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse = chain.proceed(chain.request())

        val originalBodyString = originalResponse.peekBody(Long.MAX_VALUE).string()

        val apiResponse = ApiResponse(
            code = originalResponse.code,
            message = originalResponse.message,
            data = parseData(originalBodyString)
        )

        val newBodyJson = gson.toJson(apiResponse)
        val newBody = newBodyJson.toResponseBody("application/json".toMediaType())

        return originalResponse.newBuilder().body(newBody).build()
    }

    private fun parseData(bodyString: String): Any? {
        return try {
            JsonParser.parseString(bodyString)
        } catch (e: Exception) {
            bodyString
        }
    }

}