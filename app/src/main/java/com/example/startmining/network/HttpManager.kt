package com.example.startmining.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.Semaphore

object HttpManager {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
        .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
        .writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
        .build()
    private val semaphore = Semaphore(4)

    private val json = Json { ignoreUnknownKeys = true }

    suspend fun <T> get(
        url: String,
        deserializer: DeserializationStrategy<T>,
        retries: Int = 5
    ): T {
        return withContext(Dispatchers.IO) {
            var attempt = 0
            var lastException: Exception? = null

            while (attempt < retries) {
                semaphore.acquire()
                try {
                    val request = Request.Builder().url(url).build()
                    val response = client.newCall(request).execute()

                    if (!response.isSuccessful) {
                        throw Exception("HTTP ${response.code} ${response.message}")
                    }

                    val body = response.body.string()

                    if ("limit" in body.lowercase()) {
                        throw Exception("API rate limit reached")
                    }

                    return@withContext json.decodeFromString(deserializer, body)
                } catch (e: Exception) {
                    lastException = e
                    attempt++
                    if (attempt < retries) {
                        delay(1000L * attempt) // Exponential backoff
                    }
                } finally {
                    semaphore.release()
                }
            }

            throw RuntimeException("HTTP request failed after $retries retries", lastException)
        }
    }
}
