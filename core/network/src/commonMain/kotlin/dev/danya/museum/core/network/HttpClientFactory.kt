package dev.danya.museum.core.network

import dev.danya.museum.core.network.profiling.installProfiling
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

fun createHttpClient(): HttpClient = HttpClient {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            isLenient = true
        })
    }
    install(HttpTimeout) {
        connectTimeoutMillis = 10_000
        requestTimeoutMillis = 30_000
        socketTimeoutMillis = 15_000
    }
    install(HttpRequestRetry) {
        maxRetries = 2
        retryOnServerErrors()
        retryOnException(retryOnTimeout = true)
        exponentialDelay()
    }
    install(Logging) {
        level = LogLevel.INFO
    }
    installProfiling()
}
