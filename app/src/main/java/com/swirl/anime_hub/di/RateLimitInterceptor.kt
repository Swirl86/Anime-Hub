package com.swirl.anime_hub.di

import okhttp3.*
import java.util.*
import java.util.concurrent.TimeUnit

class RateLimitInterceptor(
    private val maxRequestsPerSecond: Int = 3,
    private val maxRequestsPerMinute: Int = 60
) : Interceptor {

    private val requestTimestampsPerSecond = LinkedList<Long>()
    private val requestTimestampsPerMinute = LinkedList<Long>()

    override fun intercept(chain: Interceptor.Chain): Response {
        val currentTime = System.currentTimeMillis()

        // Clean up timestamps older than a second (for per-second rate limiting)
        requestTimestampsPerSecond.removeIf { it <= currentTime - TimeUnit.SECONDS.toMillis(1) }

        // Clean up timestamps older than a minute (for per-minute rate limiting)
        requestTimestampsPerMinute.removeIf { it <= currentTime - TimeUnit.MINUTES.toMillis(1) }

        // Handle per-second rate limiting
        if (requestTimestampsPerSecond.size >= maxRequestsPerSecond) {
            val sleepTime = TimeUnit.MILLISECONDS.toMillis(1000) - (currentTime - (requestTimestampsPerSecond.peek() ?: 0L))
            if (sleepTime > 0) {
                Thread.sleep(sleepTime)
            }
        }

        // Handle per-minute rate limiting
        if (requestTimestampsPerMinute.size >= maxRequestsPerMinute) {
            val sleepTime = TimeUnit.MILLISECONDS.toMillis(1000) - (currentTime - (requestTimestampsPerSecond.peek() ?: 0L))
            if (sleepTime > 0) {
                Thread.sleep(sleepTime)
            }
        }

        // Log the current request timestamp
        requestTimestampsPerSecond.add(currentTime)
        requestTimestampsPerMinute.add(currentTime)

        // Proceed with the request
        return chain.proceed(chain.request())
    }
}