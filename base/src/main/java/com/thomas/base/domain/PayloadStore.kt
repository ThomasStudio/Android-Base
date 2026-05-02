package com.thomas.base.domain

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import org.json.JSONObject
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID.randomUUID
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by thomas on 5/1/2026.
 */

/**
 * A store for payloads that can be shared across different components, such as ViewModels.
 */
object PayloadStore {
    @PublishedApi
    internal val payloads = ConcurrentHashMap<String, Any>()
    private fun generateId() = randomUUID().toString()
    inline fun <reified T> convert(json: String) =
        runCatching { gson.fromJson(json, T::class.java) }.getOrNull()

    @PublishedApi
    internal val gson by lazy {
        GsonBuilder()
            .registerTypeAdapter(
                LocalDate::class.java,
                JsonDeserializer { json, _, _ -> LocalDate.parse(json.asString) }
            )
            .registerTypeAdapter(
                LocalTime::class.java,
                JsonDeserializer { json, _, _ -> LocalTime.parse(json.asString) }
            )
            .create()
    }


    fun put(payload: Any): String {
        val id = generateId()
        payloads[id] = payload
        return id
    }

    fun toJson(vararg kvs: Pair<String, Any?>): JSONObject {
        val json = JSONObject()
        kvs.forEach { json.put(it.first, it.second) }
        return json
    }

    fun putJson(vararg kvs: Pair<String, Any?>): String {
        val id = generateId()
        payloads[id] = toJson(*kvs)
        return id
    }

    /**
     * consume json and convert it to T.
     */
    inline fun <reified T> consume(id: String): T? {
        val payload = payloads.remove(id) ?: return null

        return when (payload) {
            is T -> payload
            is String -> convert(payload)
            is JSONObject -> convert(payload.toString())
            else -> null
        }
    }
}