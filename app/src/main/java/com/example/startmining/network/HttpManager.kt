package com.example.startmining.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.Semaphore

object HttpManager {
    private val client = OkHttpClient()
    private val semaphore = Semaphore(7)
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun <T> get(url: String, deserializer: DeserializationStrategy<T>): T {
        return withContext(Dispatchers.IO) {
            semaphore.acquire()
            try {
                val request = Request.Builder().url(url).build()
                val response = client.newCall(request).execute()

                if (!response.isSuccessful) {
                    throw Exception("HTTP ${response.code} ${response.message}")
                }

                val body = response.body?.string()
                    ?: throw Exception("Empty response body")

                json.decodeFromString(deserializer, body)
            } catch (e: Exception) {
                throw RuntimeException("HTTP request failed: ${e.message}", e)
            } finally {
                semaphore.release()
            }
        }
    }
}
