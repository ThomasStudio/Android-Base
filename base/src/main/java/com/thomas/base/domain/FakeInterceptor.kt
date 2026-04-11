package com.thomas.base.domain

import okhttp3.Interceptor
import java.io.IOException
import kotlin.jvm.Throws
import okhttp3.Response
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody

/**
 * Created by thomas on 4/11/2026.
 */

abstract class FakeInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val scenario = resolveScenario(code())

        scenario.throwable?.let { throw it }

        return Response.Builder()
            .request(request)
            .protocol(okhttp3.Protocol.HTTP_1_1)
            .code(scenario.httpCode)
            .message(scenario.message)
            .body(scenario.body.toResponseBody(scenario.contentType.toMediaType()))
            .build()
    }

    abstract fun code(): Int
    abstract fun resolveScenario(code:Int): Scenario

}

data class Scenario(
    val httpCode: Int,
    val message: String,
    val body: String,
    val contentType: String = "application/json",
    val throwable: Throwable? = null
)